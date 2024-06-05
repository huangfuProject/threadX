<template>
    <div>
        <div class="search-class">
            <el-input :prefix-icon="Search" @keyup.enter="loadRoleList" v-model="searchValue" placeholder="请输入要搜索的角色名称" clearable />
            <el-button type="primary" @click="loadRoleList">搜索角色</el-button>
            <el-button type="success" @click="createRoleMethod" :icon="Plus">新建角色</el-button>
        </div>

        <div class="role-data-class">
            <el-table :data="roleDataTable" style="width: 100%" height="79.5vh">
                <el-table-column prop="roleName" label="角色名称" align="center"/>
                <el-table-column prop="roleDesc" label="角色说明" align="center" />
                <el-table-column prop="createDate" label="创建时间" align="center"/>
                <el-table-column prop="updateDate" label="修改时间" align="center"/>
                <el-table-column fixed="right" label="操作" align="center">
                    <template #default="scope">
                        <el-button  link type="primary" size="small" @click="findUser(scope.row.roleId)">查看用户</el-button>
                        <!-- <el-button  link type="primary" size="small" @click="updateRole(scope.row.roleId)">分配用户</el-button>   -->
                        <el-button  link type="primary" size="small" @click="updateRole(scope.row.roleId)">修改</el-button>
                        <el-button  link type="primary" size="small" @click="deleteRole(scope.row.roleId)">删除</el-button>                      
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

        <el-dialog v-model="createRoleDialogVisible" append-to-body title="角色设置" width="50%" draggable open-delay="200" close-delay="200" :close-on-click-modal="false">
            <div class="create-dialog-class">
                <el-form
                    ref="createRoleFormRef"
                    :model="createRoleFormModel"
                    label-position="right"
                    :rules="rules"
                    label-width="100px"
                    status-icon
                >
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="角色名称:" prop="roleName">
                                <el-input placeholder="角色名称" v-model="createRoleFormModel.roleName" style="width: 100%"/>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="角色介绍:" prop="roleDesc">
                                <el-input
                                    v-model="createRoleFormModel.roleDesc"
                                    :autosize="{ minRows: 2, maxRows: 10 }"
                                    type="textarea"
                                    placeholder="角色简介"
                                />
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-divider>角色菜单</el-divider>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="菜单权限:" prop="selectMenuList" >
                                <el-checkbox-group v-model="createRoleFormModel.selectMenuList">
                                    <el-checkbox v-for="menu in menus" :key="menu.id" :label="menu.id">
                                        <el-tooltip
                                            effect="dark"
                                            :content="menu.menuDesc"
                                            placement="top"
                                        >
                                            {{ menu.name }}
                                        </el-tooltip>
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-divider>角色权限</el-divider>
                    <el-row>
                        <el-col :span="24">
                            <el-form-item label="操作权限:" prop="selectPermissionList" >
                                <el-checkbox-group v-model="createRoleFormModel.selectPermissionList">
                                    <el-checkbox v-for="permission in permissions" :key="permission.id" :label="permission.id">
                                        <el-tooltip
                                            effect="dark"
                                            :content="permission.permissionDesc"
                                            placement="top"
                                        >
                                            {{ permission.name }}
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
                    <el-button @click="createRoleDialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="saveOrUpdateRole(createRoleFormRef)">确认</el-button>
                </span>
            </template>
        </el-dialog>

        <el-dialog v-model="roleUserDialogVisible" append-to-body title="角色关联用户" width="80%" draggable open-delay="200" close-delay="200" :close-on-click-modal="false">
            <div>
                <el-table empty-text="当前角色没有关联用户" :data="roleUserDataTable" style="width: 100%;height: 50vh;">
                    <el-table-column prop="nickName" label="昵称"/>
                    <el-table-column prop="userName" label="用户名"/>
                    <el-table-column prop="email" label="邮箱" />
                    <el-table-column prop="createTime" label="创建时间" />
                    <el-table-column prop="updateTime" label="修改时间" />
                    <el-table-column prop="state" label="当前状态" :formatter="stateFormatter" />
                    <el-table-column fixed="right" label="操作" align="center">
                        <template #default="scope">
                            <el-button  link type="primary" size="small" @click="untie(scope.row.id)">解绑</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div class="table-page-class">
                <el-pagination layout="prev, pager, next" 
                    :page-size="roleUserPageSize" 
                    :pager-count="5" 
                    :total="roleUserDataTotal" 
                    v-model:current-page="roleUserCurrentPage" 
                    @current-change="roleUserCurrentPageChange"
                />
            </div>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">
    import { ref , onMounted} from 'vue'
    import { Search,Plus } from '@element-plus/icons-vue'
    import RoleService from '@/services/RoleService'
    import MenuService from '@/services/MenuService'
    import PermissionService from '@/services/PermissionService'
    import { ElMessage, ElMessageBox } from 'element-plus'
    import type { FormInstance, FormRules } from 'element-plus'
    import type { TableColumnCtx } from 'element-plus'

    interface Menu {
        id: string,
        name:string,
        menuDesc:string
    }

    interface Permission {
        id: string,
        name:string,
        permissionDesc:string
    }


    interface CreateRoleForm {
        id:string
        roleName: string
        roleDesc: string
        selectMenuList: string[]
        selectPermissionList:string[]
    }

    //对应表单的值
    const createRoleFormModel = ref<CreateRoleForm>({
        id:'',
        roleName:'',
        roleDesc:'',
        selectMenuList: [],
        selectPermissionList: []
    })

    //对应表单的引用
    const createRoleFormRef = ref<FormInstance>()

    onMounted(() =>{
        loadRoleList();
        loadAllAuthority();
    });
    //当前选中的角色的id
    const selectRoleId = ref()
    //所有的菜单
    const menus = ref<Menu[]>([])
    //所有的权限信息
    const permissions = ref<Permission[]>([])
    //显示新建修改弹出框
    const createRoleDialogVisible = ref(false)
    //角色关联用户对话框
    const roleUserDialogVisible = ref(false)
    //搜索值
    const searchValue = ref()
    //角色表格数据
    const roleDataTable = ref([])
    //当前页
    const currentPage = ref(1)
    //每一页显示的数据
    const pageSize = ref(18)
    //数据总条数
    const dataTotal = ref(0)

    //角色用户
    const roleUserDataTable = ref([])
    //当前页
    const roleUserCurrentPage = ref(1)
    //每一页显示的数据
    const roleUserPageSize = ref(10)
    //数据总条数
    const roleUserDataTotal = ref(0)
    //翻页
    const currentPageChange = () =>{
        loadRoleList();
    }
    //表单规则
    const rules = ref<FormRules>(
        {
            roleName: [
                { 
                    required: true, 
                    message: '角色名不允许为空', 
                    trigger: 'blur' 
                }
            ],
            roleDesc: [
                { 
                    required: true, 
                    message: '角色介绍不允许为空', 
                    trigger: 'blur' 
                }
            ],
            selectMenuList: [
                {
                    type: 'array',
                    required: true,
                    message: '至少选择一个菜单权限',
                    trigger: 'change',
                },
            ]
        }
    )

    const findUser = (id:any) => {
        selectRoleId.value = id
        RoleService.findRoleUser({
            roleId: id,
            pageSize:roleUserPageSize.value,
            currentPage:roleUserCurrentPage.value
        }).then(res =>{
            console.log(res)
            roleUserDataTotal.value = res.total
            roleUserDataTable.value = res.data
            roleUserDialogVisible.value = true
        })

        
    }

    //修改角色信息
    const updateRole = (roleId:any) => {
        RoleService.findRoleAuthority(roleId).then(response =>{
            createRoleFormModel.value.id = response.roleId
            createRoleFormModel.value.roleName = response.roleName
            createRoleFormModel.value.roleDesc = response.roleDesc
            createRoleFormModel.value.selectMenuList = response.menuIds
            createRoleFormModel.value.selectPermissionList = response.permissionIds
            createRoleDialogVisible.value = true;
        })
        
    }

    //删除角色信息
    const deleteRole = (roleId:any) => {
        ElMessageBox.confirm(
            '点击确认后将不可恢复的删除该角色所有的权限信息以及菜单权限，并强制下线所有关联用户，请慎重操作！！！',
            '警告',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'error'
            }
        )
        .then(() => {
            RoleService.deleteRole(roleId).then(() =>{
                loadRoleList();
            })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消操作',
            })
        })
    }

    //加载角色数据
    const loadRoleList = () =>{
        RoleService.findAllByPage({
            roleName:searchValue.value,
            pageSize: pageSize.value,
            currentPage:currentPage.value
        }).then(response =>{
            dataTotal.value = response.total;
            roleDataTable.value = response.data;
        });
    }

    /**
     * 创建一个新的角色
     */
    const createRoleMethod = () => {
        createRoleFormModel.value.id = ''
        createRoleFormModel.value.roleName = ''
        createRoleFormModel.value.roleDesc = ''
        createRoleFormModel.value.selectMenuList = []
        createRoleFormModel.value.selectPermissionList = []
        createRoleDialogVisible.value = true;
    }

    /**
     * 获取所有的权限  菜单权限和权限
     */
    const loadAllAuthority = ()=>{
        MenuService.findAllMenu().then(response =>{
            menus.value = response
        })

        PermissionService.findAllPermission().then(response =>{
            permissions.value = response
        })
    }

    /**
     * 保存角色信息  同时关闭对话框
     */
    const saveOrUpdateRole = (formEl: FormInstance | undefined)=>{
        if (!formEl) return
        formEl.validate((valid, fields) => {
                if (valid) {
                    
                    RoleService.saveRole({
                        menuIds:createRoleFormModel.value.selectMenuList,
                        permissionIds:createRoleFormModel.value.selectPermissionList,
                        roleName:createRoleFormModel.value.roleName,
                        roleDesc:createRoleFormModel.value.roleDesc,
                        roleId:createRoleFormModel.value.id

                    }).then(response =>{
                        loadRoleList();
                        createRoleDialogVisible.value = false;
                    }).catch(error =>{
                        console.log("保存修改角色信息失败!", error)
                    })
                } else {
                    console.log('error submit!', fields)
                }
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
     * 解绑用户信息
     * @param userId 用户的id
     */
    const untie = (userId:any) =>{
        ElMessageBox.confirm(
            '是否确认去除选中用户角色?',
            '警告',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning'
            }
        )
        .then(() => {
            RoleService.untieUserRole(selectRoleId.value, userId).then(res =>{
                findUser(selectRoleId.value)
            })
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消操作',
            })
        })

    }

    const roleUserCurrentPageChange = () =>{
        findUser(selectRoleId.value)
    }


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

    .role-data-class {
        margin-top: 10px;
    }

    .table-page-class {
        display: flex;
        justify-content: flex-end;
        background-color: rgb(255, 255, 255);
    }

</style>