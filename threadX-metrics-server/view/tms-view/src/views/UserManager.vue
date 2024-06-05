<template>
    <div>
        <div class="search-class">
            <el-input :prefix-icon="Search" @keyup.enter="searchMethod" v-model="searchValue" placeholder="请输入要搜索的用户名或昵称" clearable />
            <el-button type="primary" @click="searchMethod">搜索用户</el-button>

            <el-button type="success" @click="createUser" class="create-user-button-class" :icon="Plus">新建用户</el-button>
        </div>
        <div class="user-data-class">
            <el-table :data="userDataTable" style="width: 100%" height="79.5vh">
                <el-table-column prop="nickName" label="用户昵称" align="center"/>
                <el-table-column prop="userName" label="用户名" align="center" />
                <el-table-column prop="email" label="用户邮箱" align="center"/>
                <el-table-column prop="createTime" label="创建时间" align="center"/>
                <el-table-column prop="updateTime" label="修改时间" align="center"/>
                <el-table-column prop="state" label="是否可用" width="100" :formatter="stateFormatter" align="center"/>
                <el-table-column fixed="right" label="操作" align="center">
                    <template #default="scope">
                        <el-button v-if="scope.row.state == '1'" link type="primary" size="small" @click="freezeUser(scope.row.id)">冻结用户</el-button>

                        <div v-else style="display: inline;">
                            <el-button  link type="primary" size="small" @click="unsealUser(scope.row.id)">解除冻结</el-button>
                            <el-button  link type="primary" size="small" @click="forceDeleteUser(scope.row.id)">删除用户</el-button>
                        </div>
                        
                        <el-tooltip
                            effect="dark"
                            content="修改用户操作权限、用户菜单权限、用户信息"
                            placement="top"
                        >
                            <el-button link type="primary" size="small" @click="updateUser(scope.row.id)">修改用户</el-button>
                        </el-tooltip>
                        
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div class="table-page-class">
            <el-pagination layout="prev, pager, next" 
                :page-size="pageSize" 
                :pager-count="5" 
                :total="dataTotal" 
                v-model:current-page="currentPage" 
                @current-change="currentPageChange"
            />
        </div>


        <el-dialog v-model="createUserDialogVisible" append-to-body title="用户设置" width="80%" draggable open-delay="200" close-delay="200" :close-on-click-modal="false">
            <div class="create-dialog-class">
                <el-form
                    ref="createUserFormRef"
                    :model="createUserFormModel"
                    label-position="right"
                    :rules="rules"
                    label-width="100px"
                    status-icon
                >
                    <el-divider>用户信息</el-divider>
                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="用户昵称:" prop="nickName">
                                <el-input v-model="createUserFormModel.nickName" style="width: 100%"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="用户邮箱:" prop="email">
                                <el-input v-model="createUserFormModel.email" style="width: 100%"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="12">
                            <el-form-item label="用户名称:" prop="userName">
                                <el-input v-model="createUserFormModel.userName" style="width: 100%"/>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label="用户密码:" prop="password">
                                <el-input type="password" v-model="createUserFormModel.password" style="width: 100%"/>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-divider>用户角色</el-divider>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="用户角色:" prop="selectRoleList" >
                                <el-checkbox-group v-model="createUserFormModel.selectRoleList">
                                    <el-checkbox v-for="role in roles" :key="role.roleId" :label="role.roleId">
                                        <el-tooltip
                                            effect="dark"
                                            :content="role.roleDesc"
                                            placement="top"
                                        >
                                            {{ role.roleName }}
                                        </el-tooltip>
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>

                
            </div>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="createUserDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="saveUserDesc(createUserFormRef)">确认</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">
    import {ref, onMounted} from 'vue'
    import type { TableColumnCtx } from 'element-plus'
    import { ElMessage, ElMessageBox } from 'element-plus'
    import { Search,Plus } from '@element-plus/icons-vue'
    import UserManagerService from '@/services/UserManagerService'
    import RoleService from '@/services/RoleService'
    import type { FormInstance, FormRules } from 'element-plus'


    interface Role {
        roleId: string,
        roleDesc: string,
        roleName:string
    }


    interface CreateUserForm {
        id:number| null;
        nickName: string
        userName: string
        password: string
        email: string,
        selectRoleList: number[],
    }

    onMounted(() =>{
        loadAllUserData()
        loadAllRole()
    });

    //所有的角色信息
    const roles = ref<Role[]>([])
    //对应表单的引用
    const createUserFormRef = ref<FormInstance>()
    //对应表单的值
    const createUserFormModel = ref<CreateUserForm>({
        id: null,
        nickName:'',
        userName:'',
        password:'',
        email:'',
        selectRoleList: []
    })
    //创建用户的对话框
    const createUserDialogVisible = ref(false)

    //搜索值
    const searchValue = ref()
    //用户表格数据
    const userDataTable = ref([])
    //当前也
    const currentPage = ref(1)
    //每一页显示的数据
    const pageSize = ref(18)
    //数据总条数
    const dataTotal = ref(0)
    //搜索方法
    const searchMethod = () =>{
        loadAllUserData();
    }
    //翻页
    const currentPageChange = () =>{
        loadAllUserData();
    }

    /**
     * 冻结用户
     * @param userId 用户的id信息
     */
    const freezeUser = (userId:any)=>{
        ElMessageBox.confirm(
            '是否确认冻结用户且强制被冻结用户下线?',
            '警告',
            {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
            }
        )
        .then(() => {
            UserManagerService.freezeUser(userId).then(() =>{
                loadAllUserData();
            })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消操作',
            })
        })
    }

    /**
     * 解除冻结
     * @param userId 用户的id信息
     */
    const unsealUser = (userId:any) =>{
        ElMessageBox.confirm(
            '是否确认解除冻结用户?',
            '警告',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )
        .then(() => {
            UserManagerService.unsealUser(userId).then(() =>{
                loadAllUserData();
            })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消操作',
            })
        })
    }

    const forceDeleteUser = (userId:any) =>{
        ElMessageBox.confirm(
            '点击确认后将不可恢复的删除该用户所有的角色信息以及操作日志信息，请慎重操作！！！',
            '警告',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'error'
            }
        )
        .then(() => {
            UserManagerService.forceDeleteUser(userId).then(() =>{
                loadAllUserData();
            })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消操作',
            })
        })
    }

    /**
     * 格式化列的值
     * @param row 当前行
     * @param column 当前列
     */
    const stateFormatter = (row:any, column: TableColumnCtx<any>)=> {
        if (row.state === '1') {
            return "有效"
        } else {
            return "冻结"
        }
            
    }

    /**
     * 加载所有的用户数据
     */
    const loadAllUserData = ()=>{
        UserManagerService.findAllUser({
            userName: searchValue.value,
            nickName: searchValue.value,
            pageSize: pageSize.value,
            pageNumber: currentPage.value
        }).then(response =>{
            userDataTable.value = response.data
            dataTotal.value = response.total
        })
        
    }

    const loadAllRole = () =>{
        RoleService.findAllRole().then(response =>{
            roles.value = response
        })
    }

    const updateUser = (id:any) =>{
        UserManagerService.findUserDesc(id).then(res =>{
            createUserFormModel.value = res
            createUserDialogVisible.value = true
        })
    }

    const saveUserDesc = (formEl: FormInstance | undefined)=>{


        if (!formEl) return
        formEl.validate((valid, fields) => {
                if (valid) {
                    
                    UserManagerService.saveUser(createUserFormModel.value).then(()=>{
                        createUserDialogVisible.value = false;
                        loadAllUserData();
                    }).catch(error =>{
                        console.log(error)
                    })
                } else {
                    console.log('error submit!', fields)
                }
        })


    }

    const createUser  = () =>{
        createUserFormModel.value = {
            id: null,
            nickName:'',
            userName:'',
            password:'',
            email:'',
            selectRoleList: []
        }
        createUserDialogVisible.value = true
    }

    //表单规则
    const rules = ref<FormRules>(
        {
            nickName: [
                { 
                    required: true, 
                    message: '用户昵称不能为空', 
                    trigger: 'blur' 
                }
            ],
            email: [
                { 
                    required: true, 
                    message: '邮箱不能为空', 
                    trigger: 'blur' 
                }
            ],
            userName: [
                { 
                    required: true, 
                    message: '用户名不能为空', 
                    trigger: 'blur' 
                }
            ],
            password: [
                { 
                    required: true, 
                    message: '密码不能为空', 
                    trigger: 'blur' 
                },
                { 
                    min: 6, 
                    message: '密码最少为6位', 
                    trigger: 'blur' 
                }
            ],
            selectRoleList: [
                {
                    type: 'array',
                    required: true,
                    message: '至少选择一个角色',
                    trigger: 'change',
                },
            ]
            
        }
    )

</script>

<style scoped lang="scss">
    .search-class {
        background-color: rgb(255, 255, 255);
        height: 60px;
        border-radius: 5px;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        padding-right: 10px;

        .el-input {
            margin-right: 5px;
            width: 15%;
        }
    }

    .user-data-class {
        margin-top: 10px;
    }

    .table-page-class {
        display: flex;
        justify-content: flex-end;
        background-color: rgb(255, 255, 255);
    }

    .create-dialog-class {
        
    }
</style>