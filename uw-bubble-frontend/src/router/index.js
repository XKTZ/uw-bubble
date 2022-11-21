import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView";
import RegisterView from "@/views/RegisterView";
import RecommendView from "@/views/RecommendView";
import MessageView from "@/views/MessageView";

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/login',
    name: 'login',
    component: LoginView
  },
  {
    path: "/register",
    name: "register",
    component: RegisterView
  },
  {
    path: "/recommend",
    name: "recommend",
    component: RecommendView
  },
  {
    path: "/message",
    name: "message",
    component: MessageView
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
