class LocalStorageUtil {
    /**
     * 登录票据的缓存信息
     */
    static readonly THREADX_TMS_TOKEN:string = "THREADX_TMS_TOKEN";

    /**
     * 用户名称信息
     */
    static readonly THREADX_TMS_USER_NICK_NAME:string = "THREADX_TMS_USER_NICK_NAME";

    /**
     * 设置缓存的值
     * @param key 缓存的主键
     * @param value 缓存的值
     */
    static setItem(key: string, value: string): void {
      const serializedValue = JSON.stringify(value);
      localStorage.setItem(key, serializedValue);
    }
  

    /**
     * 获取缓存信息根据主键
     * @param key 需要获取的缓存的主键
     * @returns  缓存的值，当不存在的时候会返回空字符串
     */
    static getItem(key: string): string {
      const serializedValue = localStorage.getItem(key);
      if (serializedValue) {
        return JSON.parse(serializedValue) as string;
      }
      return '';
    }
  
    /**
     * 删除一个本地缓存
     * @param key 需要删除缓存的key
     */
    static removeItem(key: string): void {
      localStorage.removeItem(key);
    }

    /**
     * 登录的时候存储token  
     * @param token 票据信息
     */
    static loginDataSave(token: string, nickName:string): void {
        LocalStorageUtil.setItem(LocalStorageUtil.THREADX_TMS_TOKEN, token)
        LocalStorageUtil.setItem(LocalStorageUtil.THREADX_TMS_USER_NICK_NAME, nickName)
    }

    /**
     * 退出登录的时候删除票据
     */
    static logoutDataRemove():void {
        LocalStorageUtil.removeItem(LocalStorageUtil.THREADX_TMS_TOKEN)
        LocalStorageUtil.removeItem(LocalStorageUtil.THREADX_TMS_USER_NICK_NAME)
    }

    /**
     * 获取登录的票据信息
     * @returns 登录的票据信息
     */
    static getLoginToken():string{
        return LocalStorageUtil.getItem(LocalStorageUtil.THREADX_TMS_TOKEN)
    }

    /**
     * 获取登录的用户名
     * @returns 登录的用户名
     */
      static getLoginNickName():string{
          return LocalStorageUtil.getItem(LocalStorageUtil.THREADX_TMS_USER_NICK_NAME)
      }
  }
  //导出缓存工具包
  export default LocalStorageUtil;
  