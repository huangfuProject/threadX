<template>
    <div class="login-body">
        <div class="login-box">
            <div id="owl" :class="`owl ${passwordIsHover ? 'password' : ''}`">
                <div class="hand"></div>
                <div class="hand hand-r"></div>
                <div class="arms">
                    <div class="arm"></div>
                    <div class="arm arm-r"></div>
                </div>
            </div>
            
            <el-form
                ref="ruleFormRef"
                :model="userLoginForm"
                :rules="rules"
                status-icon
            >
                <div class="input-box">
                    <el-form-item label="" prop="userName">
                        <el-input type="text" @keyup.enter="loginSubmit(ruleFormRef)" v-model="userLoginForm.userName" placeholder="账号"/>
                    </el-form-item>
                    <el-form-item label="" prop="password">
                        <el-input type="password" @keyup.enter="loginSubmit(ruleFormRef)" v-model="userLoginForm.password" placeholder="密码" @blur="removePasswordFocus" @focus="passwordFocus" id="password" />
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="loginSubmit(ruleFormRef)">登录</el-button>
                    </el-form-item>
                    
                </div>
            </el-form>

            
    </div>
    </div>
</template>

<script lang="ts">
import { defineComponent,ref,reactive } from 'vue'
import type { FormInstance, FormRules } from 'element-plus'
import UserService from '../services/UserService'

export default defineComponent({
    setup () {
        
        /**
         * 创建一个对象
         */
        const userLoginForm = reactive({
            userName: '',
            password: ''
        })
        /**
         * 规则验证实例
         */
        const ruleFormRef = ref<FormInstance>();

        /**
         * 定义规则实现
         */
        const rules = reactive<FormRules>({
            userName: [
                { required: true, message: '用户名不能为空', trigger: 'blur' },
                { min: 5, max: 20, message: '账号最小5个字符，最大20个字符', trigger: 'blur' }
            ],
            password: [
                { required: true, message: '密码不能为空', trigger: 'blur' },
                { min: 6, max: 20, message: '密码最小6个字符，最大20个字符', trigger: 'blur' }
            ]
        });


        //是否需要追加password class
        const passwordIsHover = ref(false);

        /**
         * 密码框当鼠标移入的时候 新增事件
         */
         const passwordFocus = ()=>{
            passwordIsHover.value = true
        };

        /**
         * 当鼠标移出密码框
         */
        const removePasswordFocus = ()=>{
            passwordIsHover.value = false
        }
        
        /**
         * 登录提交
         * @param formEl 登录表单
         */
        const loginSubmit = async (formEl: FormInstance | undefined) => {
            if (!formEl) return
            await formEl.validate((valid, fields) => {
                if (valid) {
                    UserService.login(userLoginForm.userName, userLoginForm.password)
                } else {
                    console.log('error submit!', fields)
                }
            })
        }

        return {
            passwordIsHover,
            userLoginForm,
            ruleFormRef,
            rules,
            passwordFocus,
            removePasswordFocus,
            loginSubmit
        }
    }
})
</script>

<style scoped>
    .login-body{
        /* 100%窗口高度 */
        height: 100vh;
        /* 弹性布局 居中 */
        display: flex;
        justify-content: center;
        align-items: center;
        /* 渐变背景 */
        background: linear-gradient(200deg,#72afd3,#96fbc4);
    }
    .login-box{
        /* 相对定位 */
        position: relative;
        width: 320px;
    }
    .input-box{
        /* 弹性布局 垂直排列 */
        display: flex;
        flex-direction: column;
    }

    .input-box .el-input{
        height: 40px;
        border-radius: 3px;
        /* 缩进15像素 */
        text-indent: 15px;
        outline: none;
        border: none;
    }
    .input-box .el-input:focus{
        outline: 1px solid lightseagreen;
    }
    .input-box .el-button{
        border: none;
        height: 45px;
        background-color: lightseagreen;
        color: #fff;
        border-radius: 3px;
        cursor: pointer;
        width: 100%;
    }
    /* 接下来是猫头鹰的样式 */
    .owl{
        width: 211px;
        height: 108px;
        /* 背景图片 */
        background: url("../assets/images/owl-login.png") no-repeat;
        /* 绝对定位 */
        position: absolute;
        top: -100px;
        /* 水平居中 */
        left: 50%;
        transform: translateX(-50%);
    }
    .owl .hand{
        width: 34px;
        height: 34px;
        border-radius: 40px;
        background-color: #472d20;
        /* 绝对定位 */
        position: absolute;
        left: 12px;
        bottom: -8px;
        /* 沿Y轴缩放0.6倍（压扁） */
        transform: scaleY(0.6);
        /* 动画过渡 */
        transition: 0.3s ease-out;
    }
    .owl .hand.hand-r{
        left: 170px;
    }
    .owl.password .hand{
        transform: translateX(42px) translateY(-15px) scale(0.7);
    }
    .owl.password .hand.hand-r{
        transform: translateX(-42px) translateY(-15px) scale(0.7);
    }
    .owl .arms{
        position: absolute;
        top: 58px;
        width: 100%;
        height: 41px;
        overflow: hidden;
    }
    .owl .arms .arm{
        width: 40px;
        height: 65px;
        position: absolute;
        left: 20px;
        top: 40px;
        background: url("../assets/images/owl-login-arm.png") no-repeat;
        transform: rotate(-20deg);
        transition: 0.3s ease-out;
    }
    .owl .arms .arm.arm-r{
        transform: rotate(20deg) scaleX(-1);
        left: 158px;
    }
    .owl.password .arms .arm{
        transform: translateY(-40px) translateX(40px);
    }
    .owl.password .arms .arm.arm-r{
        transform: translateY(-40px) translateX(-40px) scaleX(-1);
    }

</style>