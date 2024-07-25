import { createRouter, createWebHistory } from 'vue-router'
import store from '@/store'
import storage from "@/util/storage.js";


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/login/index.vue')
    },
    {
      path: '/main',
      name: 'main',
      component: () => import('@/components/Index.vue'),
      children: [
        {
          path: '', // 默认子路由
          name: 'home',
          component: () => import('@/(system)/index.vue')
        },
        {
          path: '/member',
          name: 'member',
          component: () => import('@/(system)/(user)/(admin)/manage-user/index.vue')
        },
        {
          path: '/profile',
          name: 'profile',
          component: () => import('@/(system)/(user)/profile.vue')
        },
        {
          path: '/part_view',
          name: 'part_view',
          component: () => import('@/(system)/(part)/part.vue')
        },
        {
          path: '/order',
          name: 'order',
          component: () => import('@/(system)/(user)/(admin)/manage-orders/index.vue')
        },
        {
          path: '/create_team',
          name: 'create_team',
          component: () => import('@/(system)/(team)/create-team/index.vue')
        },
        {
          path: '/part',
          name: 'part',
          component: () => import('@/(system)/(user)/(admin)/(manage-parts)/index.vue')
        },
      ]
    },

  ]
})


router.beforeEach((to) => {
  if (to.name === 'login') {
    if (store.getters.isLogin) {
      router.push({ name: 'home' })
      return true
    }
    return true
  }
  
  if (!store.getters.isLogin) {
    if (!storage.getSessionObject('loginUser')) {
      router.push({ name: 'login' })
    }
    else {
      store.dispatch('RECOVERY_USER')
    }
  }
  if (!router.hasRoute(to.name)) {
    router.push({ name: 'home' })
  }
  return true
})


export default router
