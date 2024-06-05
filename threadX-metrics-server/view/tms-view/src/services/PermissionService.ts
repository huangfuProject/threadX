import ApiUtils from './api';

class PermissionService {
    /**
     * 查询当前用户下拥有的左侧菜单栏 
     * @returns 
     */
    public static findAllPermission(): Promise<any> {
        return ApiUtils.get("/permission/findAllPermission")
        .then((response) =>{
            return response
        })
        .catch((error: any) => {
            // 处理错误情况
            console.error("查询全部权限信息请求失败：", error);
            return []
        });
    }
}
export default PermissionService


