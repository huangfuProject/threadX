import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import "element-plus/theme-chalk/el-message.css";
import "element-plus/theme-chalk/el-message-box.css"; 

createApp(App).use(router).mount('#app')
