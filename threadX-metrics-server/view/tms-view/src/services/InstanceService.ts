import ApiUtils from './api';

class InstanceService {

    /**
     * 分页查询实例信息 
     * @param instanceItemFindConditions 查询条件
     * @returns 
     */
    public static commonlyUsedTop10():Promise<any> {
        return ApiUtils.get("/instanceItem/commonlyUsedTop10").then((response) =>{
            return response;
        }).catch(error =>{
            console.log(error)
            return []
        })
    }

    /**
     * 
     * @returns 查询监控的实例的状态信息，总监听数  活跃数  等待数   监听时间等
     */
    public static instanceListeningState(data:any):Promise<any> {
        return ApiUtils.get("/instanceItem/instanceListeningState", data).then((response) =>{
            return response;
        }).catch(error =>{
            console.log(error)
            return {}
        })
    }
}
export default InstanceService