<template>
    <div>
        <div></div>
        <div class="row-div">
            <el-descriptions
                class="margin-top"
                :column="3"
                size="small"
                border
            >
                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池所属的服务"
                        placement="top-start"
                        >
                            所属服务
                        </el-tooltip>
                    </template>
                    {{ threadPoolData.serverName }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池所属的实例"
                        placement="top-start"
                        >
                            所属实例
                        </el-tooltip>
                    </template>
                    {{ threadPoolData.instanceName }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="线程池所属的组名称，生成规则为：类全限定名#创建线程池的方法:创建该线程池的行数"
                        placement="top-start"
                        >
                            所属组名称
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.threadPoolGroupName }}
                </el-descriptions-item>


                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="线程池所属的组名称，生成规则为：所属组名称-对象内存地址"
                        placement="top-start"
                        >
                            线程池名称
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.threadPoolName }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池的状态，活跃证明线程池正在执行任务，否则是等待！"
                        placement="top-start"
                        >
                            线程池状态
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.state }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="本次监控任务，所有成功与拒绝执行的比例！"
                        placement="top-start"
                        >
                            拒绝率
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.refuseRate }}
                </el-descriptions-item>

                
                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="本次监控任务的成功几率"
                        placement="top-start"
                        >
                            成功率
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.successRate }}
                </el-descriptions-item>


                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="本次监控任务，所有成功任务执行的平均耗时（毫秒）"
                        placement="top-start"
                        >
                            任务执行平均耗时
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.averageTimeConsuming }} 毫秒
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="本次监控任务，所有任务从提交到执行的等待的平均耗时（毫秒）"
                        placement="top-start"
                        >
                            任务执行等待耗时
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.averageWaitTimeConsuming }} 毫秒
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后，可并发核心数量"
                        placement="top-start"
                        >
                            核心线程数
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.coreSize }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后，线程池的最大并发数量"
                        placement="top-start"
                        >
                            最大线程数
                        </el-tooltip>
                    </template>
                    {{ threadPoolData.maxSize }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后，正在执行任务的线程数量"
                        placement="top-start"
                        >
                            当前活跃数
                        </el-tooltip>
                    </template>
                    {{ threadPoolData.activeCount }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始的线程数量，包含没有执行任务的线程还没有来得及被销毁的非核心线程"
                        placement="top-start"
                        >
                            存活线程数
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.surviveThreadCount }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后曾达到的最大的线程数，历史最大数量"
                        placement="top-start"
                        >
                            历史最大线程数
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.historyMaxThreadCount }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后，拒绝执行任务的数量！"
                        placement="top-start"
                        >
                            任务被拒绝次数
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.refuseCount }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后，堆积的、执行中的、已经完成的任务的总和"
                        placement="top-start"
                        >
                            任务总计
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.taskTotalCount }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后，已经完成的任务总数量"
                        placement="top-start"
                        >
                            任务的完成数量
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.completedCount }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后，空闲时间"
                        placement="top-start"
                        >
                            空闲时间
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.freeTime }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后, 当前线程池数据的来源的ip"
                        placement="top-start"
                        >
                            采集机器
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.collectAddress }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后, 线程池使用的队列类型"
                        placement="top-start"
                        >
                            队列类型
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.queueType }}
                </el-descriptions-item>

                <el-descriptions-item>
                    <template #label>
                        <el-tooltip
                        effect="dark"
                        content="当前线程池从本次监控开始后, 线程池使用的拒绝策略类型"
                        placement="top-start"
                        >
                            拒绝策略
                        </el-tooltip>
                        
                    </template>
                    {{ threadPoolData.refuseType }}
                </el-descriptions-item>
            </el-descriptions>
        </div>

        <div class="row-div search" @keyup.enter="searchTaskData">
            <div class="block">
                <el-select v-model="resultStatus"  placeholder="结果状态"  size="large"  clearable>
                    <el-option label="成功" value="1"/>
                    <el-option label="失败" value="0"/>
                </el-select>
            </div>


            <div class="block">

                <el-config-provider :locale="locale">
                    <el-date-picker
                    value-format="YYYY-MM-DD HH:mm:ss"
                    format="YYYY-MM-DD HH:mm:ss"
                    size="large"
                    v-model="runTime"
                    type="datetimerange"
                    range-separator="-"
                    start-placeholder="开始时间"
                    end-placeholder="结束时间"
                    
                    />
                </el-config-provider>
            </div>

            <div class="block">
                <el-button type="primary" @click="searchTaskData">检索任务</el-button>
            </div>
            
            
        </div>

        <div>
            <el-table 
                :data="taskData" 
                style="width: 100%"
                sortable="custom"
                @sort-change="sortTableChange"
            >

                <el-table-column type="expand">
                    <template #default="props">
                        <div>
                            {{ props.row.throwable === ''? "没有异常": props.row.throwable}}
                        </div>
                    </template>
                </el-table-column>

                <el-table-column prop="threadName" label="线程名" width="180" />
                <el-table-column prop="submitDate" label="任务提交时间" width="180" />
                <el-table-column prop="startDate" label="任务开始时间" />
                <el-table-column prop="endDate" label="任务结束时间" />
                <el-table-column prop="runIngConsumingTime" label="任务执行耗时" sortable/>
                <el-table-column prop="waitTime" label="任务等待时间"  sortable/>
                <el-table-column prop="consumingTime" label="任务总耗时" sortable/>
                <el-table-column prop="refuse" label="是否拒绝" />
                <el-table-column prop="success" label="是否成功" />
            </el-table>

            <div class="pagination-block">
                <el-pagination :default-current-page="1" :hide-on-single-page="true" @current-change="changePage" v-model:page-size="pageSizes" v-model:current-page="thisPage" pager-count="10" layout="prev, pager, next" :total="dataCountTotal" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent , ref, onMounted} from 'vue'
import zhCn from "element-plus/lib/locale/lang/zh-cn";
import router from '@/router'
import ThreadPoolService from '@/services/ThreadPoolService';
import TaskService from '@/services/TaskService'


export default defineComponent({
    setup () {
        onMounted(() =>{
            loadRouterParam();
            loadThreadPoolData();
            loadThreadTaskList();
        });

        //定义传递的线程池名称
        const threadPoolName = ref()
        //定义传递的实例的id信息
        const instanceId = ref()
        //线程池的数据信息
        const threadPoolData = ref({})
        //线程池下的任务信息
        const taskData = ref([])
        //单选筛选结果
        const resultStatus = ref('')
        //筛选事件
        const runTime = ref([])
        //任务信息总条数
        const dataCountTotal = ref(0)
        //当前页码
        const thisPage = ref(1)
        //每一页显示10
        const pageSizes = ref(12)
        //排序名称
        const sortName = ref('create_time')
        //默认为降序
        const sortType = ref('1')

        const searchTaskData = ()=> {
            sortName.value = 'create_time'
            sortType.value = '1'
            loadThreadTaskList();
        }

        const changePage = ()=>{
            loadThreadTaskList();
        }

        /**
         * 加载线程池的数据
         */
        const loadThreadPoolData = async ()=>{
            threadPoolData.value = await ThreadPoolService.findThreadPoolDetail({
                instanceId: instanceId.value,
                threadPoolName: threadPoolName.value
            })
        }

        /**
         * 加载路由参数
         */
        const loadRouterParam = ()=>{
            threadPoolName.value = router.currentRoute.value.query.threadPoolName
            instanceId.value = router.currentRoute.value.query.instanceId
        }

        /**
         * 加载线程池任务数据
         */
        const loadThreadTaskList = async ()=>{
            let startTimeStr = "";
            let endTimeStr = "";
            if (runTime.value != null && runTime.value.length == 2) {
                startTimeStr = runTime.value[0]
                endTimeStr = runTime.value[1]
            }

            const pageData = await TaskService.threadTaskData({
                startTime:startTimeStr,
                endTime:endTimeStr,
                instanceId:instanceId.value,
                pageSizes:pageSizes.value,
                resultState:resultStatus.value,
                thisPage: thisPage.value,
                threadPoolName:threadPoolName.value,
                sortName:sortName.value,
                //0 升序    1降序
                sortType:sortType.value
            })

            taskData.value = pageData.data;
            dataCountTotal.value = pageData.total;

        }

        const sortTableChange = (sortData:any)=>{
            thisPage.value = 1

            const clickSortName = sortData.prop
            if(clickSortName == "runIngConsumingTime") {
                sortName.value = "runIng_consuming_time"
            }

            if(clickSortName == "consumingTime") {
                sortName.value = "consuming_time"
            }

            if(clickSortName == "waitTime") {
                sortName.value = "wait_time"
            }

           
            const  orderName = sortData.order
            if(orderName == "ascending") {
                sortType.value = "0";
            }else {
                sortType.value = "1";
            }
            loadThreadTaskList();
        }

        return {
            taskData,
            resultStatus,
            locale: zhCn,
            runTime,
            dataCountTotal,
            threadPoolData,
            thisPage,
            pageSizes,
            searchTaskData,
            changePage,
            loadThreadPoolData,
            sortTableChange
        }
    }
})
</script>

<style scoped lang="scss">
    .row-div {
        margin-bottom: 10px;
    }
    .search {
        display: flex;
        width: 100%;
        justify-content: flex-end;
        align-items: center;
       .block {
        margin-left: 10px;
       }

    }

    .pagination-block {
        display: flex;
        justify-content: flex-end;
    }
</style>../services/TaskService