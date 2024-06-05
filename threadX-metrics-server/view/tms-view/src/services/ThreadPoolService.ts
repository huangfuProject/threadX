import ApiUtils from './api';

/**
 * 线程池查询服务
 */
class ThreadPoolService {

    /**
     * 查询线程池的具体详情 
     * @param data 参数
     * @returns  线程池的具体详情
     */
    public static findThreadPoolDetail(data:any): Promise<any> {
        return ApiUtils.post('/threadPool/findThreadPoolDetail', data).then((response) =>{
            return response;
        }).catch(error =>{
            console.log(error)
            return {}
        })
    }


    /**
     * 根据查询条件分页查询线程池
     * @param data 参数
     * @returns  根据查询条件分页查询线程池
     */
    public static findPageByThreadPoolPageDataConditions(data:any): Promise<any> {
        return ApiUtils.post('/threadPool/findPageByThreadPoolPageDataConditions', data).then((response) =>{
            return response;
        }).catch(error =>{
            console.log(error)
            return []
        })
    }

   /**
     * 查询线程池的核心参数
     *
     * @param threadPoolDataId 线程池的id
     * @return 对应线程池的核心参数
     */
    public static findThreadPoolParam(threadPoolDataId:string):Promise<any> {
        return ApiUtils.get('/threadPool/findThreadPoolParam', {
            threadPoolDataId
        }).then((response) =>{
            return response;
        }).catch(error =>{
            console.log(error)
            return {}
        })
    }

    /**
     * 修改线程池参数
     * @param threadPoolParam 线程池参数
     */
    public static updateThreadPoolParam(threadPoolParam:any):Promise<any> {
        return ApiUtils.post('/threadPool/updateThreadPoolParam', threadPoolParam)
    }

    /**
     * 查找线程池的修改日志
     * @returns 修改日志
     */
    public static findThreadPoolUpdateLog():Promise<any> {
        return ApiUtils.get('/threadPool/findThreadPoolUpdateLog');
    }
}

export default ThreadPoolService