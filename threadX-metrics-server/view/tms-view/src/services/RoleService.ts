import ApiUtils from './api';

class RoleService {
    /**
     * 分页查询角色信息
     * @param rolePageConditions 查询条件
     * @returns 角色信息
     */
    public static findAllByPage(rolePageConditions:any):Promise<any> {
        return ApiUtils.post('/role/findAllByPage', rolePageConditions).then((response) =>{
            return response;
        })
        .catch((error: any) => {
            // 处理错误情况
            console.error("查询全部角色信息请求失败：", error);
            return {}
        });
    }

    /**
     * 查询全部的角色信息
     * @returns 全部的角色信息
     */
    public static findAllRole():Promise<any> {
        return ApiUtils.get('/role/findAllRole').catch((error: any) => {
            // 处理错误情况
            console.error("查询全部的角色信息错误", error);
            return {}
        });
    }

    /**
     * 查询对应角色的权限信息
     * @param roleId 角色的id
     * @returns 对应角色的权限信息
     */
    public static findRoleAuthority(roleId:any):Promise<any> {
        return ApiUtils.get('/role/findRoleAuthority', {
            roleId
        }).then((response) =>{
            return response;
        })
        .catch((error: any) => {
            // 处理错误情况
            console.error("查询对应角色的权限信息失败！", error);
            return {}
        });
    }

    /**
     * 保存角色配置
     * @param roleVo 角色配置
     * @returns 
     */
    public static saveRole(roleVo:any):Promise<any> {
        return ApiUtils.post('/role/save', roleVo);
    }

    /**
     * 删除角色
     * @param roleId 角色id
     */
    public static deleteRole(roleId:any):Promise<any> {
        return  ApiUtils.get('/role/deleteRole', {
                    roleId
                }).catch((error: any) => {
                    // 处理错误情况
                    console.error("查询对应角色的权限信息失败！", error);
                });
    }

    /**
     * 解绑用户角色
     * @param roleId 角色id
     * @param userId 用户id
     * @returns 
     */
    public static untieUserRole(roleId:any, userId:any):Promise<any> {
        return  ApiUtils.get('/role/untieUserRole', {
            roleId,
            userId
        }).catch((error: any) => {
            // 处理错误情况
            console.error("解绑用户角色失败！", error);
        });
    }

    /**
     * 解绑用户角色
     * @param roleId 角色id
     * @param userId 用户id
     * @returns 
     */
        public static findRoleUser(data:any):Promise<any> {
            return  ApiUtils.post('/role/findRoleUser', data).catch((error: any) => {
                // 处理错误情况
                console.error("查询角色下的用户失败！", error);
            });
        }
    
}


export default RoleService