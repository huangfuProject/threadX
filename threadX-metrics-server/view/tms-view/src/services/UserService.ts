import ApiUtils from './api';
import LocalStorageUtil from '@/common/LocalStorageUtil';
import router from '@/router';

class UserService{

    /**
     * 用户登录
     * @param userName 用户名 
     * @param password  用户密码
     */
    public static login(userName:string, password:string):void {
        ApiUtils.post("/user/login", {
            "userName":userName,
            "password":password
        }).then((response) =>{
            const userVo:UserVo = response as UserVo
            const token = userVo.token
            const nickName = userVo.nickName
            LocalStorageUtil.loginDataSave(token, nickName)
            router.push('/worktable')
        }).catch(error =>{
            console.log(error)
        })
        
    }

    /**
     * 退出登录
     */
    public static logout():void {
        ApiUtils.get('/user/logout').catch((error) =>{
            console.log(error)
        }).finally(()=>{
            LocalStorageUtil.logoutDataRemove()
            router.push('/login')
        })
        
    }
}

interface UserVo {
    token:string,
    nickName:string
}

export default UserService;