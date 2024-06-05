<template>
    <div>
        <div class="topClass">
            <div class="welcomeText">
                <span>
                    hi 欢迎回来，{{nickName}}
                </span>
            </div>
            <div class="avatarClass">
                <el-dropdown @command="avatarCommand">
                    <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"/>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <!-- todo/ 后续实现 -->
                            <!-- <el-dropdown-item>修改个人资料</el-dropdown-item> -->
                            <!-- <el-dropdown-item>修改密码</el-dropdown-item> -->
                            <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
            
        </div>
    </div>
    
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'
import LocalStorageUtil from '@/common/LocalStorageUtil';
import UserService from '../services/UserService'

export default defineComponent({
    setup () {
        onMounted(() =>{
            loadNickName()
        });

        //定义昵称
        const nickName = ref('');
        /**
         * 加载昵称
         */
        const loadNickName = ()=>{
            nickName.value = LocalStorageUtil.getLoginNickName()
        }

        const avatarCommand = (command: string | number | object) => {
            if ('logout' === command) {
                UserService.logout()
            }
        }

        return {
            nickName,
            avatarCommand
        }
    }
})
</script>

<style scoped>
    .topClass {
        border-radius: 10px;
        background-color: rgb(255, 255, 255);
        display: flex;
        padding: 10px;
       
    }

    .avatarClass {
        display: flex;
        width: 100%;
        justify-content: flex-end;
    }

    .welcomeText {
        display: flex;
        width: 100%;
        justify-content: flex-start;
        align-items: center;
    }
</style>