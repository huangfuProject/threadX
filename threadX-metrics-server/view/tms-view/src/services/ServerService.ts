import ApiUtils from './api';

/**
 * 服务数据查询服务
 */
class ServerService {

    /**
     * 查询服务和实例（所有） 
     * @returns 返回树状结构数据
     */
    public static findServerAndInstanceData(): Promise<any> {
        return ApiUtils.get('/serverItem/findServerAndInstanceData').then((response) =>{
            return response;
        }).catch(error =>{
            console.log(error)
            return []
        })
    }
}

export default ServerService