import ApiUtils from './api';

class UserManagerService {

    /**
     * 根据条件查询所有的用户
     *
     * @param data 查询条件
     * @return 返回的分页结果集
     */
    public static findAllUser(data:any): Promise<any> {
        return ApiUtils.post("/manager/user/getAllUser", data).then((res) =>{
            return res
        }).catch((error: any) => {
            // 处理错误情况
            console.error("查询所有用户信息失败", error);
            return {}
        });
    }

    /**
     * 冻结用户
     * @param data 参数信息
     */
    public static freezeUser(userId:any): Promise<any> {
        return ApiUtils.get("/manager/user/freezeUser", {
                    userId
                }).catch((error: any) => {
                    // 处理错误情况s
                    console.error("冻结用户失败", error);
                });
    }

    /**
     * 解封用户
     * @param data 参数信息
     */
    public static unsealUser(userId:any): Promise<any> {
        return ApiUtils.get("/manager/user/unsealUser", {
                    userId
                }).catch((error: any) => {
                    // 处理错误情况
                    console.error("解封用户失败", error);
                });
    }

    /**
     * 强制删除用户 包含删除所有的依赖关系以及操作日志
     * @param userId 用户的id
     */
    public static forceDeleteUser(userId:any): Promise<any> {
        return ApiUtils.get("/manager/user/forceDeleteUser", {
            userId
        }).catch((error: any) => {
            // 处理错误情况
            console.error("删除用户失败", error);
        });
    }

    /**
     * 保存用户信息
     * @param userRoleVo 用户角色
     * @returns 
     */
    public static saveUser(userRoleVo:any):Promise<any> {
        return ApiUtils.post("/manager/user/saveUser", userRoleVo).catch((error: any) => {
            // 处理错误情况
            console.error("保存用户信息失败", error);
            Promise.reject(new Error(error));
        });
    }

    /**
     * 查询用户详情
     * @param userId 用户id
     * @returns 
     */
    public static findUserDesc(userId:any):Promise<any> {
        return ApiUtils.get('/manager/user/findUserDesc', {
            userId
        }).catch((error: any) => {
            // 处理错误情况
            console.error("查询用户详情失败", error);
            return {}
        });
    }
}

export default UserManagerService