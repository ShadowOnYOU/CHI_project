import Vue from 'vue'
import VueRouter from 'vue-router'
import Main from '@/views/Main.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login',
    component: Main,
    children: [
      {
        path: '/home',
        component: () => import('@/views/home/Home.vue')
      },
      {
        path: '/course-scheduling',
        component: () => import('@/views/course-scheduling/CourseScheduling.vue')
      },
      {
        path: '/course-scheduling-list',
        component: () => import('@/views/course-scheduling-list/CourseSchedulingList.vue')
      },
      {
        path: '/stop-course-scheduling-list',
        component: () => import('@/views/stop-course-scheduling-list/StopCourseSchedulingList.vue')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/login.vue')
  },
  {
    path: '/register',
    component: () => import('@/views/register/register.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
