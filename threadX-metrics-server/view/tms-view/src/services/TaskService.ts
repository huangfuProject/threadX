import ApiUtils from './api';

class TaskService {

    /**
     * 查询错误排名top10 
     * @returns  top10的错误数据
     */
    public static findThreadTaskDataErrorCalculationTop10(): Promise<any> {
        return ApiUtils.get("/threadTaskData/findThreadTaskDataErrorCalculationTop10")
        .then((response) =>{
            return response
        })
        .catch((error: any) => {
            // 处理错误情况
            console.error("查询线程池任务错误数据top10请求失败：", error);
            return []
        });
    }

    /**
     * 分页查询线程池数据
     * @returns  根据条件返回的数据
     */
    public static threadTaskData(param:any): Promise<any> {
        return ApiUtils.post("/threadTaskData/findByThreadTaskConditions", param)
        .then((response) =>{
            return response
        })
        .catch((error: any) => {
            // 处理错误情况
            console.error("分页查询线程池数据", error);
            return []
        });
    }
}

export default TaskService