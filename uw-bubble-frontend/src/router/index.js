import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView";
import RegisterView from "@/views/RegisterView";
import RecommendView from "@/views/RecommendView";
import MessageView from "@/views/MessageView";
import CommunicationsView from "@/views/CommunicationsView";

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
    path: "/communications",
    name: "communications",
    component: CommunicationsView
  },
  {
    path: "/message/:lastPage/:opponent",
    name: "message",
    component: MessageView,
    props: true
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
