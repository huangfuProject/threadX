import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    redirect: '/login' // 添加重定向规则
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginPage.vue')
  },
  {
    path: '/',
    name: 'home',
    component: () => import('../views/MainHome.vue'),
    children: [
      {
        path: '/worktable',
        name: 'worktable',
        component: () => import('../views/TmsWorktable.vue')
      },{
        path: '/threadPoolMonitor',
        name: 'ThreadPoolMonitor',
        component: () => import('../views/ThreadPoolMonitorPage.vue')
      },{
        path: '/servicePage',
        name: 'Service',
        component: () => import('../views/ServicePage.vue')
      },{
        path: '/roleManager',
        name: 'RoleManager',
        component: () => import('../views/RoleManager.vue')
      },{
        path: '/userManager',
        name: 'UserManager',
        component: () => import('../views/UserManager.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
