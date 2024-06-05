<template>
    <div class="pageBody">
        <div :span="4" class="treeColClass">
            <el-input style="margin-top: 0px;" v-model="treeFilterText" clearable placeholder="搜索实例" :prefix-icon="Search"/>
            <el-tree
                ref="treeRef"
                :data="serverTreeData"
                empty-text="没有实例数据"
                :props="defaultProps"
                :default-expand-all="false"
                accordion
                :filter-node-method="filterNode"
                :highlight-current="true"
                @node-click="handleNodeClick"
            />
        </div>
        <div :span="20" class="instanceDataBody">
            <div class="em-logo-class" v-if="emLogo">
                <img src="../assets/icon/douding.png" alt="">
                <el-text  type="info">请选择要查看的实例...</el-text>
            </div>
            <div v-else>
                <div class="instance-top">
                    <el-card class="box-card">
                        <template #header>
                        <div class="card-header">
                            <span>监控持续时间</span>
                        </div>
                        </template>
                        <div class="cred-value">
                            <span>{{ instanceListeningState.monitoringDuration }}</span>
                        </div>
                        
                    </el-card>

                    <el-card class="box-card">
                        <template #header>
                        <div class="card-header">
                            <span>线程池数量</span>
                        </div>
                        </template>
                        <div class="cred-value">
                            <span>{{ instanceListeningState.threadPoolCount }}</span>
                        </div>
                        
                    </el-card>

                    <el-card class="box-card">
                        <template #header>
                        <div class="card-header">
                            <span>等待任务的线程池数量</span>
                        </div>
                        </template>
                        <div class="cred-value">
                            <span>{{ instanceListeningState.waitThreadPoolCount }}</span>
                        </div>
                        
                    </el-card>

                    <el-card class="box-card">
                        <template #header>
                        <div class="card-header">
                            <span>活跃线程池数量</span>
                        </div>
                        </template>
                        <div class="cred-value">
                            <span>{{instanceListeningState.activeThreadPoolCount}}</span>
                        </div>
                        
                    </el-card>
                </div>

                <div class="search-from-class">
                    <el-input :prefix-icon="Search" v-model="threadPoolGroupNameSearch" placeholder="请输入线程池组的名称" clearable @keyup.enter="loadThreadPoolData"/>
                    <el-button type="primary" @click="loadThreadPoolData">搜索</el-button>
                </div>

                <div class="thread-pool-table">
                    
                    <el-table :data="threadPoolTableData">
                        <el-table-column prop="threadPoolName" label="线程池名称"  align="center">
                            <template #default="scope">

                                <el-tooltip :content="scope.row.threadPoolName" placement="top">
                                    <el-button
                                    link
                                    type="primary"
                                    size="small"
                                    @click.prevent="threadPoolDetailsPage(scope.row.instanceId, scope.row.threadPoolName)">
                                        <span class="truncate-text">{{ scope.row.threadPoolName.length > 20 ? scope.row.threadPoolName.substring(0, 20) + '...' : scope.row.threadPoolName }}</span>
                                    </el-button>
                                </el-tooltip>  
                            </template>
                        </el-table-column>
                        <el-table-column prop="threadPoolGroupName" label="所属组" align="center">
                            <template #default="scope">
                                <el-tooltip :content="scope.row.threadPoolGroupName" placement="top">
                                    <span class="truncate-text">{{ scope.row.threadPoolGroupName.length > 20 ? scope.row.threadPoolGroupName.substring(0, 20) + '...' : scope.row.threadPoolGroupName }}</span>
                                </el-tooltip>  
                            </template>
                        </el-table-column>
                        <el-table-column prop="activeCount" label="活跃线程数量" align="center"/>
                        <el-table-column prop="thisThreadCount" label="当前线程数量" align="center"/>
                        <el-table-column prop="completedTaskCount" label="完成任务数量" align="center"/>
                        <el-table-column prop="createTime" label="数据更新时间" align="center"/>
                        <el-table-column prop="state" label="线程池状态" align="center"/>
                        <el-table-column fixed="right" label="操作" align="center">
                            <template #default="scope">
                                <el-button link type="primary" size="small" @click="showThreadPoolDetails(scope.row.id)">修改</el-button>
                                <el-button link type="primary" size="small" @click="threadPoolDetailsPage(scope.row.instanceId, scope.row.threadPoolName)">详情</el-button>
                                <el-button link type="primary" size="small" @click="viewThreadPoolCreateFlow(scope.row.createThreadPoolFlow)">流程</el-button>
                            </template>
                        </el-table-column>
                    </el-table>

                    <div class="page-class">
                        <el-pagination
                        :page-size="pageSize"
                        :pager-count="5"
                        v-model:current-page="currentPage"
                        layout="prev, pager, next"
                        :total="totalSize"
                        @current-change="loadThreadPoolData"
                        />
                    </div>
                </div>
            </div>
        </div>


        <el-dialog v-model="createThreadPoolFlowVisible" title="创建步骤" width="800px">
            <div >
                <el-scrollbar height="500px">
                    <el-steps :active="procedureData.length" align-center direction="vertical">
                        <el-step 
                        v-for="(procedure) in procedureData"
                        :key="procedure.title"
                        :title="procedure.title" 
                        :description="procedure.details" 
                        />
                    </el-steps>
                </el-scrollbar>

            </div>
        </el-dialog>

        <el-dialog
            v-model="showUpdateThreadPoolParam"
            title="修改线程池参数"
            width="40%"
            :before-close="emptyThreadPoolParam"
        >
            <el-form
                ref="threadPoolParamFormRef"
                :model="threadPoolParamForm"
                :rules="threadPoolParamFormRules"
                label-width="120px"
                label-position="right"
                status-icon
            >
                <el-form-item label="核心线程数" prop="coreSize">
                    <el-input v-model="threadPoolParamForm.coreSize" />
                </el-form-item>

                <el-form-item label="最大线程数" prop="maximumPoolSize">
                    <el-input v-model="threadPoolParamForm.maximumPoolSize" />
                </el-form-item>

                <el-form-item label="空闲时间(毫秒)" prop="keepAliveTime">
                    <el-input v-model="threadPoolParamForm.keepAliveTime" />
                </el-form-item>

                <el-form-item label="拒绝策略" prop="rejectedExecutionHandlerClass">
                    <el-input v-model="threadPoolParamForm.rejectedExecutionHandlerClass" />
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="showUpdateThreadPoolParam = false">取消修改</el-button>
                    <el-button type="primary" @click="submitThreadPoolparam(threadPoolParamFormRef)">
                        确认修改
                    </el-button>
                </span>
            </template>
        </el-dialog>

    </div>
</template>


<script setup lang="ts">
    import { reactive,ref,watch,onMounted } from 'vue'
    import type { ElTree } from 'element-plus'
    import { Search } from '@element-plus/icons-vue'
    import ServerService from '../services/ServerService'
    import InstanceService from '@/services/InstanceService'
    import ThreadPoolService from '@/services/ThreadPoolService'
    import router from '@/router'
    import { tr } from 'element-plus/es/locale'
    import type { FormInstance, FormRules } from 'element-plus'
    import { ElMessage } from 'element-plus'


    interface ThreadPoolParamForm {
        coreSize: number|null,
        maximumPoolSize: number|null,
        keepAliveTime: number|null,
        rejectedExecutionHandlerClass:string
    }

    onMounted(() =>{
        loadRouterParam();
        loadTreeData();
    });



    //分页容量
    const pageSize = ref(14)
    const totalSize = ref()
    const currentPage = ref(1)
    //是否显示空logo
    const emLogo = ref(true)
    //搜索条件
    const threadPoolGroupNameSearch = ref()
    //表格数据
    const threadPoolTableData = ref([])
    //线程池创建步骤是否显示
    const createThreadPoolFlowVisible = ref(false)
    //步骤长度
    const procedureData = ref()

    const threadPooltId = ref()
    const showUpdateThreadPoolParam = ref(false)


    const threadPoolParamFormRef = ref<FormInstance>()
    const threadPoolParamForm = reactive<ThreadPoolParamForm>({
        coreSize: null,
        maximumPoolSize: null,
        keepAliveTime: null,
        rejectedExecutionHandlerClass:''
    })

    const threadPoolParamFormRules = reactive<FormRules>({
        coreSize: [
            { required: true, message: '当前线程核心数量必填', trigger: 'blur' },
            // { type: 'number', message: '当前线程核心数量必须为数字类型' },
        ],
        maximumPoolSize:[
            { required: true, message: '当前线程最大并行数量必填', trigger: 'blur' },
            // { type: 'number', message: '当前线程最大并行数量必须为数字类型' },
        ],
        keepAliveTime:[
            { required: true, message: '当前线程空闲时间（毫秒）必填', trigger: 'blur' },
            // { type: 'number', message: '当前线程空闲时间必须为数字类型' },
        ],
        rejectedExecutionHandlerClass:[
            { required: true, message: '当前线程拒绝色略全限定名必填', trigger: 'blur' }
        ]
    })


    const instanceListeningState = ref({
        monitoringDuration:"0毫秒",
        threadPoolCount:0,
        activeThreadPoolCount:0,
        waitThreadPoolCount:0
    })
    // 定义树结构的数据体系
    const serverTreeData= ref();
    //点击的实例的id
    const instanceId = ref()
    // 定义标签和子标签的属性
    const defaultProps = ref({
        children: 'children',
        label: 'label',
    });
    // 定义筛选条件值
    const treeFilterText = ref()
    // 定义树引用
    const treeRef = ref<InstanceType<typeof ElTree>>()
    // 监听搜索框  将搜索值传入引用对象中
    watch(treeFilterText, (val) => {
        filterTreeData()
    })

    /**
     * 对树节点进行筛选时执行的方法， 返回 false 则表示这个节点会被隐藏
     * @param value 搜索的值
     * @param data 当前标签
     */
    const filterNode = (value: string, data:any) => {
        if (!value) return true
        //是否包含搜索值
        return data.label.includes(value)
    }


    /**
     * 当出发节点点击的时候回调的方法
     * @param data 点击的值
     */
    const handleNodeClick = (data: any) => {
        if (data.parentId != null) {
            instanceId.value = data.id
            loadInstanceData()
        }
        
    }


    /**
     * 加载实例的数据
     */
        const loadInstanceData = async ()=>{
        const instanceIdValue = instanceId.value
        if (instanceIdValue == null) {
            emLogo.value = true
        }else {
            emLogo.value = false
            instanceListeningState.value = await InstanceService.instanceListeningState({
                instanceId:instanceIdValue
            })
            loadThreadPoolData()    
        }
    }

    /**
     * 加载线程池表格数据
     */
    const loadThreadPoolData =async () => {
        const threadPoolTableDataReq = await ThreadPoolService.findPageByThreadPoolPageDataConditions({
            instanceId: instanceId.value,
            threadGroupName: threadPoolGroupNameSearch.value,
            pageNumber: currentPage.value,
            pageSize: pageSize.value
        })
        threadPoolTableData.value = threadPoolTableDataReq.data
        totalSize.value = threadPoolTableDataReq.total
    }

    /**
     * 加载路由参数
     */
    const loadTreeData = () =>{
        ServerService.findServerAndInstanceData().then(response =>{
            serverTreeData.value = response
        }).finally(() =>{
            filterTreeData();
            loadInstanceData();
        })
    }

    /**
     * 根据输入数据过滤数据
     */
    const filterTreeData = ()=>{
        treeRef.value!.filter(treeFilterText.value)
    }

    /**
     * 加载路由参数
     */
    const loadRouterParam = ()=>{
        treeFilterText.value = router.currentRoute.value.query.instanceName
        instanceId.value = router.currentRoute.value.query.instanceId
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
     * 显示线程池的创建流
     * @param datas 创建流程
     */
    const viewThreadPoolCreateFlow = (datas:any)=>{
        procedureData.value = datas
        createThreadPoolFlowVisible.value = true
    }

    /**
     * 显示修改线程池参数的对话框
     * @param id 线程池的id
     */
    const showThreadPoolDetails = (id:string) =>{
        emptyThreadPoolParam()
        //拉取当前的线程池的参数信息
        ThreadPoolService.findThreadPoolParam(id).then(res =>{
            threadPooltId.value = res.threadPoolId;
            threadPoolParamForm.coreSize = res.coreSize;
            threadPoolParamForm.maximumPoolSize = res.maximumPoolSize;
            threadPoolParamForm.keepAliveTime = res.keepAliveTime;
            threadPoolParamForm.rejectedExecutionHandlerClass = res.rejectedExecutionHandlerClass;
            showUpdateThreadPoolParam.value = true
        });
    }

     /**
      * 清空线程池修改的表单
      */
    const emptyThreadPoolParam = () =>{
        showUpdateThreadPoolParam.value = false
        threadPooltId.value = "";
        threadPoolParamForm.coreSize =  null;
        threadPoolParamForm.maximumPoolSize = null;
        threadPoolParamForm.keepAliveTime = null;
        threadPoolParamForm.rejectedExecutionHandlerClass = "";
    }

    const submitThreadPoolparam = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate((valid, fields) => {
            if (valid) {
                ThreadPoolService.updateThreadPoolParam({
                    threadPoolId: threadPooltId.value,
                    coreSize: threadPoolParamForm.coreSize,
                    maximumPoolSize: threadPoolParamForm.maximumPoolSize,
                    keepAliveTime: threadPoolParamForm.keepAliveTime,
                    rejectedExecutionHandlerClass: threadPoolParamForm.rejectedExecutionHandlerClass
                }).then(() =>{
                    showUpdateThreadPoolParam.value = false
                    ElMessage({
                        message: '修改线程参数成功.',
                        type: 'success',
                    })
                }).catch(error =>{
                    ElMessage({
                        message: '修改线程参数失败.',
                        type: 'error',
                    })
                })
                
            } else {
                console.log('error submit!', fields)
            }
        })
    }

</script>


<style scoped lang="scss">
    .pageBody {
        display: flex;
    }
    .treeColClass {
        height: 91vh;
        background-color: rgb(255, 255, 255);
        border: 1px solid rgb(225, 227, 225);
        border-radius: 5px;
        width: 15%;
        /* box-shadow:水平位置 垂直位置 模糊距离 阴影尺寸（影子大小） 阴影颜色  内/外阴影； */
        box-shadow: 0 10px 10px  rgba(0, 0, 0, .3);
        .el-input {
            margin-bottom: 10px;
        }
    }

    .instanceDataBody {
        height: 91vh;
        background-color: rgb(255, 255, 255);
        margin-left: 10px;
        width: 85%;
        border: 1px solid rgb(225, 227, 225);
        box-shadow: 0 10px 10px  rgba(0, 0, 0, .3);
        border-radius: 5px;
    }

    .em-logo-class {
        display: flex;
        flex-direction: column;
        height: 100%;
        justify-content: center;
        align-items: center;

        .el-text {
            margin-top: 10px;
        }
    }

    .instance-top {
        display: flex;
        justify-content: space-between;
        .box-card {
            width: 24.5%;
            height: 17vh;
        }

        .cred-value {
            display: flex;
            justify-content: center;
            align-items: center;

            span {
                font-size: 20px;
                font-weight: bold;
            }
        }
    }

    .search-from-class {
        display: flex;
        margin-top: 5px;
        width: 100%;
        justify-content: flex-end;

        .el-input {
            width: 20%;
            margin-right: 10px;
        }
    }
    .thread-pool-table {
        margin-top: 5px;
    }

    .page-class {
        display: flex;
        justify-content: flex-end;
    }
</style>