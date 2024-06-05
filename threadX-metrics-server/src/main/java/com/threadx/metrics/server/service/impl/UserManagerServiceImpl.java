package com.threadx.metrics.server.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.threadx.metrics.server.common.code.CurrencyRequestEnum;
import com.threadx.metrics.server.common.code.UserExceptionCode;
import com.threadx.metrics.server.common.exceptions.GeneralException;
import com.threadx.metrics.server.common.exceptions.UserException;
import com.threadx.metrics.server.conditions.UserPageConditions;
import com.threadx.metrics.server.constant.RedisCacheKey;
import com.threadx.metrics.server.constant.UserConstant;
import com.threadx.metrics.server.entity.User;
import com.threadx.metrics.server.entity.UserRole;
import com.threadx.metrics.server.mapper.UserMapper;
import com.threadx.metrics.server.service.*;
import com.threadx.metrics.server.vo.ThreadxPage;
import com.threadx.metrics.server.vo.UserRoleVo;
import com.threadx.metrics.server.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * *************************************************<br/>
 * 用户管理服务<br/>
 * ************************************************<br/>
 *
 * @author huangfu
 * @date 2023/6/3 13:59
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserManagerServiceImpl extends ServiceImpl<UserMapper, User> implements UserManagerService {

    private final StringRedisTemplate redisTemplate;
    private final ActiveLogService activeLogService;
    private final UserRoleService userRoleService;


    public UserManagerServiceImpl(StringRedisTemplate redisTemplate, ActiveLogService activeLogService, UserRoleService userRoleService) {
        this.redisTemplate = redisTemplate;
        this.activeLogService = activeLogService;
        this.userRoleService = userRoleService;
    }

    @Override
    public List<User> findByUserIds(Collection<Long> userIds) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", userIds);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public void saveUserAndRole(UserRoleVo userRoleVo) {
        if (userRoleVo == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        String userName = userRoleVo.getUserName();
        if (StrUtil.isBlank(userName)) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        String nickName = userRoleVo.getNickName();
        if (StrUtil.isBlank(nickName)) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        String email = userRoleVo.getEmail();
        if (StrUtil.isBlank(email)) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        String password = userRoleVo.getPassword();
        if (StrUtil.isBlank(password)) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        User newUser = new User();
        newUser.init();
        Long id = userRoleVo.getId();
        if (id != null) {
            User user = baseMapper.selectById(id);
            if (user == null) {
                throw new UserException(UserExceptionCode.NOT_EXIST_USER);
            }
            BeanUtil.copyProperties(user, newUser);
            newUser.setUpdateTime(System.currentTimeMillis());
            ((UserManagerService) AopContext.currentProxy()).deleteUser(user.getId());
        }
        //开始更新数据
        newUser.setUserName(userName);
        newUser.setNickName(nickName);
        newUser.setEmail(email);
        if (!password.equals(newUser.getPassword())) {
            newUser.setPassword(BCrypt.hashpw(password));
        }
        //插入数据
        baseMapper.insert(newUser);
        //删除权限信息
        userRoleService.deleteByUserId(newUser.getId());
        //开始新增权限信息
        List<Long> selectRoleList = userRoleVo.getSelectRoleList();
        if (CollUtil.isNotEmpty(selectRoleList)) {
            Set<UserRole> userRoles = selectRoleList.stream().map(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(newUser.getId());
                userRole.setRoleId(roleId);
                return userRole;
            }).collect(Collectors.toSet());
            //插入角色信息
            userRoleService.batchSave(userRoles);
        }
        //开始将id下线
        Set<String> keys = redisTemplate.keys(String.format(RedisCacheKey.USER_CACHE, newUser.getId()) + "*");
        if (CollUtil.isNotEmpty(keys)) {
            keys.forEach(redisTemplate::delete);
        }

    }

    @Override
    public ThreadxPage<UserVo> findAllUser(UserPageConditions userPageConditions) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        String userName = userPageConditions.getUserName();
        String nickName = userPageConditions.getNickName();
        Integer pageNumber = userPageConditions.getPageNumber();
        Integer pageSize = userPageConditions.getPageSize();

        if (StrUtil.isNotBlank(userName)) {
            queryWrapper.like(StrUtil.isNotBlank(userName), "user_name", userName);
        }

        if (StrUtil.isNotBlank(nickName)) {
            queryWrapper.or(con -> con.like(StrUtil.isNotBlank(nickName), "nick_name", nickName));
        }

        Page<User> userPage = new Page<>(pageNumber, pageSize);
        baseMapper.selectPage(userPage, queryWrapper);
        //转换数据为需要的对象
        List<User> records = userPage.getRecords();
        List<UserVo> userVos = records.stream().map(record -> {
            UserVo userVo = new UserVo();
            userVo.setUserName(record.getUserName());
            userVo.setNickName(record.getNickName());
            userVo.setCreateTime(DateUtil.format(new Date(record.getCreateTime()), "yyyy-MM-dd HH:mm:ss"));
            userVo.setUpdateTime(DateUtil.format(new Date(record.getUpdateTime()), "yyyy-MM-dd HH:mm:ss"));
            userVo.setId(record.getId());
            userVo.setState(record.getState());
            return userVo;
        }).collect(Collectors.toList());

        ThreadxPage<UserVo> threadPage = new ThreadxPage<>();
        threadPage.setData(userVos);
        threadPage.setTotal(userPage.getTotal());
        return threadPage;
    }

    @Override
    public void freezeUser(Long userId) {
        if (userId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        User user = new User();
        user.setId(userId);
        user.setState(UserConstant.DISABLE);
        baseMapper.updateById(user);

        Set<String> keys = redisTemplate.keys(String.format(RedisCacheKey.USER_CACHE, userId) + "*");
        if (CollUtil.isNotEmpty(keys)) {
            keys.forEach(redisTemplate::delete);
        }
    }

    @Override
    public void unsealUser(Long userId) {

        if (userId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }

        User user = new User();
        user.setId(userId);
        user.setState(UserConstant.ENABLE);
        baseMapper.updateById(user);
    }

    @Override
    public void forceDeleteUser(Long userId) {
        //首先查询用户
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new UserException(UserExceptionCode.NOT_EXIST_USER);
        }

        String state = user.getState();
        if (UserConstant.ENABLE.equals(state)) {
            throw new UserException(UserExceptionCode.USER_STATUS_EXCEPTION);
        }
        //清除当前用户的缓存信息
        Set<String> keys = redisTemplate.keys(String.format(RedisCacheKey.USER_CACHE, userId) + "*");
        if (CollUtil.isNotEmpty(keys)) {
            keys.forEach(redisTemplate::delete);
        }
        // 开始删除当前用户的日志信息
        activeLogService.deleteLogByUserId(userId);
        //开始删除当前用户的角色信息
        userRoleService.deleteByUserId(userId);
        //删除用户信息
        baseMapper.deleteById(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        //删除用户信息
        baseMapper.deleteById(userId);
    }

    @Override
    public UserRoleVo findUserDesc(Long userId) {
        if (userId == null) {
            throw new GeneralException(CurrencyRequestEnum.PARAMETER_MISSING);
        }
        User user = baseMapper.selectById(userId);
        if (user == null) {
            throw new UserException(UserExceptionCode.NOT_EXIST_USER);
        }
        //查询用户的角色
        List<Long> roleIdByUserId = userRoleService.findRoleIdByUserId(userId);
        UserRoleVo userRoleVo = new UserRoleVo();
        userRoleVo.setUserName(user.getUserName());
        userRoleVo.setEmail(user.getEmail());
        userRoleVo.setNickName(user.getNickName());
        userRoleVo.setId(userId);
        userRoleVo.setPassword(user.getPassword());
        userRoleVo.setSelectRoleList(roleIdByUserId);

        return userRoleVo;
    }
}
