<template>
    <div class="main-class">
        <el-card class="main-card">
            <template #header>
            <div class="card-header">
                <span class="card-title">常用实例列表</span>
                <i class="icon iconfont  icon-shuaxin mouse-shadow server-shuaxin" @click="commonlyUsedTop10"></i>
            </div>
            </template>

            <div class="card-main">
                <!-- 数据表格组件 -->
                <el-table 
                    :data="instanceList" 
                    stripe 
                    :style="`width: 100%; height: ${cardMainyHeight}px;`" 
                    :max-height="cardMainyHeight" 
                    row-key="id"
                    empty-text="近七天无常用实例"
                    :border="false">
                    <el-table-column fixed prop="instanceName" label="实例名称">
                        <template #default="scope">
                            <el-button
                            link
                            type="primary"
                            size="small"
                            @click.prevent="instanceDetailsPage(scope.row.instanceName,scope.row.id)"
                            >
                            {{ scope.row.instanceName}}
                            </el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="serverName" label="所属服务"/>
                    <el-table-column prop="createDate" label="创建时间" />
                    <el-table-column prop="state" label="存活状态">
                        <template #default="scope">
                            <el-popover effect="light" trigger="hover" placement="top" width="auto">
                            <template #default>
                                <div  v-if="instanceStateCheck(scope.row.state)">【{{ scope.row.instanceName }}】 正在被持续监控！</div>
                                <div style="color: red;" v-else>
                                    【{{ scope.row.instanceName }}】 实例不可用，请检查该实例！
                                </div>
                            </template>
                            <template #reference>
                                <el-tag :type="instanceStateTagType(scope.row.state)">{{ instanceState(scope.row.state) }}</el-tag>
                            </template>
                            </el-popover>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            
        </el-card>

        <el-card class="main-card">
            <template #header>
            <div class="card-header">
                <span class="card-title">异常任务数量线程池Top10</span>
                <i class="icon iconfont  icon-shuaxin mouse-shadow server-shuaxin" @click="errorTaskThreadPoolTop"></i>
            </div>
            </template>
            
            <div class="card-main">
                <!-- 数据表格组件 -->
                <el-table 
                    :data="errorTaskThreadPoolTopList" 
                    stripe 
                    :style="`width: 100%; height: ${cardMainyHeight}px;`" 
                    :max-height="cardMainyHeight" 
                    row-key="threadPoolName"
                    empty-text="恭喜你，线程池没有错误数据"
                    :border="false"
                    fit>
                    
                    <el-table-column fixed prop="threadPoolName" label="线程池名">
                        <template #default="scope">

                            <el-tooltip :content="scope.row.threadPoolName" placement="top">
                                <el-button
                                link
                                type="primary"
                                size="small"
                                @click.prevent="threadPoolDetailsPage(scope.row.instanceId, scope.row.threadPoolName)">
                                    <span class="truncate-text">{{ scope.row.threadPoolName.length > 30 ? scope.row.threadPoolName.substring(0, 30) + '...' : scope.row.threadPoolName }}</span>
                                </el-button>
                            </el-tooltip>  
                        </template>
                    </el-table-column>

                    <el-table-column prop="threadPoolGroupName" label="线程池组">
                        <template #default="scope">
                            <el-tooltip :content="scope.row.threadPoolGroupName" placement="top">
                                <span class="truncate-text">{{ scope.row.threadPoolGroupName.length > 20 ? scope.row.threadPoolGroupName.substring(0, 20) + '...' : scope.row.threadPoolGroupName }}</span>
                            </el-tooltip>
                        </template>
                    </el-table-column>

                    <el-table-column prop="instanceName" label="所属实例信息" />

                    <el-table-column prop="errorCount" label="错误数量" width="100" align="center"/>
                </el-table>
            </div>
        </el-card>
    </div>
    <div class="main-class2">
        <el-card class="main-card">
            <template #header>
            <div class="card-header">
                <span class="card-title">欢迎使用ThreadX</span>
            </div>
            </template>
            <div class="card-main">
                <div class="welcome-main">
                    <div class="welcome-left">
                        <img src="../assets/threadx-xiezuo.png" alt="欢迎使用ThreadX">
                    </div>

                    <div class="welcome-right">
                        <div class="welcome-text">
                            <div>
                                Hi 开发者，我是ThreadX，是你开发中必不可少的伙伴！
                            </div>
                            <br>
                            <div>
                                ThreadX是一个业务无侵入式的线程池监控管理平台，
                                <br>
                                帮助团队更好的监控线程任务状态，更小的修改成本。
                            </div>
                        </div>

                        <div class="welcome-link">
                            <span class="welcome-title">操作指引</span>

                            <ul>
                                <li>
                                    <span class="welcome_li_title">更新日志：</span>
                                    <a class="welcome_li_a" href="">社区更新日志</a>
                                </li>

                                <li>
                                    <span class="welcome_li_title">官方文档：</span>
                                    <a class="welcome_li_a" href="">社区官方网文档</a>
                                </li>
                            </ul>
                        </div>
                        
                    </div>

                    

                </div>
            </div>
            
        </el-card>

        <el-card class="main-card">
            <template #header>
            <div class="card-header">
                <span class="card-title">最近修改的线程池</span>
                <i class="icon iconfont  icon-shuaxin mouse-shadow server-shuaxin" @click="findThreadPoolUpdateLog"></i>
            </div>
            </template>
            
            <div class="card-main">
                <el-table 
                    :data="threadPoolUpdateDatas" 
                    stripe 
                    :style="`width: 100%; height: ${cardMainyHeight}px;`" 
                    :max-height="cardMainyHeight" 
                    row-key="id"
                    empty-text="最近没有修改过线程池"
                    :border="false">

                    <el-table-column width="100" prop="serverName" label="服务名称" >
                        <template #default="scope">
                            <el-tooltip :content="scope.row.serverName" placement="top">
                                <span class="truncate-text">{{ scope.row.serverName.length > 10 ? scope.row.serverName.substring(0, 10) + '...' : scope.row.serverName }}</span>
                            </el-tooltip>  
                        </template>
                    </el-table-column>
                    <el-table-column prop="instanceName" label="实例名称">
                        <template #default="scope">
                            <el-tooltip :content="scope.row.instanceName" placement="top">
                                <span class="truncate-text">{{ scope.row.instanceName.length > 10 ? scope.row.instanceName.substring(0, 10) + '...' : scope.row.instanceName }}</span>
                            </el-tooltip>  
                        </template>
                    </el-table-column>
                    <el-table-column prop="threadPoolName" label="线程池名称">
                        <template #default="scope">
                            <el-tooltip :content="scope.row.threadPoolName" placement="top">
                                <el-button
                                link
                                type="primary"
                                size="small"
                                @click.prevent="threadPoolDetailsPage(scope.row.instanceId, scope.row.threadPoolName)">
                                    <span class="truncate-text">{{ scope.row.threadPoolName.length > 30 ? scope.row.threadPoolName.substring(0, 30) + '...' : scope.row.threadPoolName }}</span>
                                </el-button>
                            </el-tooltip>  
                        </template>
                    </el-table-column>
                    <el-table-column prop="updateNickName" label="修改人" width="80"/>
                    <el-table-column prop="updateDate" label="修改时间">
                        <template #default="scope">
                            <el-tooltip :content="scope.row.updateDate" placement="top">
                                <span class="truncate-text">{{ scope.row.updateDate.length > 10 ? scope.row.updateDate.substring(0, 10) + '...' : scope.row.updateDate }}</span>
                            </el-tooltip>  
                        </template>
                    </el-table-column>
                    <el-table-column prop="resultMessage" label="修改结果" width="100"/>
                    <el-table-column prop="details" label="修改详情">
                        <template #default="scope">
                            <el-tooltip :content="scope.row.details" placement="top">
                                <span class="truncate-text">{{ scope.row.details.length > 10 ? scope.row.details.substring(0, 10) + '...' : scope.row.details }}</span>
                            </el-tooltip>  
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-card>
    </div>
</template>

<script setup lang="ts">
    import { ref,computed, onMounted } from 'vue'
    import InstanceService from '../services/InstanceService'
    import TaskService from '../services/TaskService'
    import ThreadPoolService from '../services/ThreadPoolService'
    import '../assets/css/index.css'
    import { useRouter } from 'vue-router';

    onMounted(() =>{
            commonlyUsedTop10()
            errorTaskThreadPoolTop()
            findThreadPoolUpdateLog()
        });
        const router = useRouter();

        //当前屏幕的高度
        const windowHeight = ref(window.innerHeight);

        //线程池修改数据
        const threadPoolUpdateDatas = ref([])
        //内容的高度
        const cardMainyHeight = computed(() => {
            return (windowHeight.value - 310)/2;
        });

        //返回当前实例的状态  监控中的返回true  断联的返回false
        const instanceStateCheck = computed(()=>(state:string) => {
            return state === "0"
        });
        // 判断当前 实例是否是运行中
        const instanceState = computed(()=>(state:string) => {
            if(instanceStateCheck.value(state)) {
                return "活跃"
            }
            return "断联";
        });
        // 返回当前的实例状态的标签的类型  如果正在运行返回  success 否则返回  danger
        const instanceStateTagType = computed(() =>(state:string)=> {
            if(instanceStateCheck.value(state)) {
                return "success"
            }
            return "danger";
        })
        // 实例数据
        const instanceList = ref([])
        //异常任务数量线程池数组
        const errorTaskThreadPoolTopList = ref([])

        //实例详情页面
        const instanceDetailsPage = (instanceName:any, instanceId:any)=>{
            router.push({
                name:'Service',
                query: {
                    instanceName:instanceName,
                    instanceId:instanceId
                }
            })
        }
        /**
         * 常用实例查询
         */
        const commonlyUsedTop10 = async ()=>{
            instanceList.value = await InstanceService.commonlyUsedTop10()
        }
        /**
         * 异常任务数量线程池Top10查询
         */
        const errorTaskThreadPoolTop = async () => {
            errorTaskThreadPoolTopList.value = await TaskService.findThreadTaskDataErrorCalculationTop10()
        }

        /**
         * 跳转页面到线程池详情页面，携带线程池的id信息
         * 
         * @param threadPoolId 点击的线程池的id
         */
        const threadPoolDetailsPage = (instanceId:string, threadPoolName:string)=>{
            router.push({
                name:'ThreadPoolMonitor',
                query: {
                    instanceId:instanceId,
                    threadPoolName:threadPoolName
                }
            })
        }

        /**
         * 查询线程池的修改数据
         */
        const findThreadPoolUpdateLog = ()=>{
            ThreadPoolService.findThreadPoolUpdateLog().then(res =>{
                threadPoolUpdateDatas.value = res
            }).catch(error =>{
                threadPoolUpdateDatas.value = []
            })
        }
</script>

<style lang="scss" scoped>
    .main-class {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        align-items: flex-start; /* 第一行的垂直对齐 */
       
    }

    .main-class2 {
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        align-items: flex-end; /* 第二行的垂直对齐 */
        margin-top: 10px;
       
    }


    .main-card {
        width: calc((100% - 20px) / 2);
        height: calc((100vh - 99px) / 2);
        overflow-x: auto;
        overflow-y: auto;
    }

    .icon-shuaxin {
        cursor: pointer;
        font-size: 22px;
    }

    .card-header {
        display: flex;
        height: 20px;
        justify-content: space-between;
        align-items: center;
    }

    .card-main {
        .page_div {
            display: flex;
            justify-content: end;
            margin-top: 20px;
        }
    }

    .card-title {
        font-weight: bold;
    }

    .welcome-main {
        display: flex;
        box-sizing: border-box;
        height: 323px;
        

    }

    .welcome-left {
        width: 30%;
        display: flex;
        align-items: center;
        img {
            width: 257px;
            height: 187px;
        }
    }

    .welcome-right {
        width: 70%;
        margin-top: 6%;
        padding-left: 10%;
        .welcome-text {
            width: 80%;
            line-height: 30px;
            color: #182b50;
            font-size: 14px;
        }

    }

    .welcome-link {
        margin-top: 20px;
    }

    .welcome-title {
            
            font-weight: bold;
            margin-bottom: 20px;
        }

        ul {

            li {
                margin: 0;
                padding: 0;
                list-style: none;
            }
        }

        .welcome_li_title {
            padding-left: 7px;
            position: relative;
            font-size: 12px;
            color: #8c95a8;
            margin-bottom: 10px;
        }

        .welcome_li_a {
            font-size: 12px;
        }

</style>../services/TaskService