import ApiUtils from './api';

class MenuService {
    /**
     * 查询当前用户下拥有的左侧菜单栏 
     * @returns 
     */
    public static getLeftMenu(): Promise<any> {
        return ApiUtils.post("/menu/findThisUserMenu")
        .then((response) =>{
            return response
        })
        .catch((error: any) => {
            // 处理错误情况
            console.error("查询左侧菜单栏信息请求失败：", error);
            return []
        });
    }


    /**
     * 获取所有的菜单信息
     * @returns 所有的菜单信息
     */
    public static findAllMenu(): Promise<any> {
        return ApiUtils.get("/menu/findAllMenu")
        .then((response) =>{
            return response
        })
        .catch((error: any) => {
            // 处理错误情况
            console.error("查询全部菜单栏信息请求失败：", error);
            return []
        });
    }
}
export default MenuService


