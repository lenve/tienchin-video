import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import My01 from "@/views/My01";
import My02 from "@/views/My02";
import My03 from "@/views/My03";
import My04 from "@/views/My04";

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },  {
    path: '/my',
    name: 'My01',
    component: My01
  },{
    path: '/my02',
    name: 'My02',
    component: My02
  },{
    path: '/my03',
    name: 'My03',
    component: My03
  },{
    path: '/my04',
    name: 'My04',
    component: My04
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
