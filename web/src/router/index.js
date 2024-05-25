import { createRouter, createWebHistory } from 'vue-router'

// import each pages
import HomeIndex from "../views/home/HomeIndex.vue"
import RecordIndex from "../views/record/RecordIndex.vue"
import RanklistIndex from "../views/ranklist/RanklistIndex.vue"
import UserbotIndex from "../views/user/bot/UserbotIndex.vue"
import NotFound from "../views/error/NotFound.vue"
import UserAccountLoginView from "../views/user/account/UserAccountLoginView.vue"
import UserAccountRegisterView from "../views/user/account/UserAccountRegisterView.vue"

const routes = [
  {
    path: "/",
    redirect: "/home/"
  },

  {
    path: "/home/",
    name: "home",
    component: HomeIndex
  },

  {
    path: "/record/",
    name: "record",
    component: RecordIndex
  },

  {
    path: "/ranklist/",
    name: "ranklist",
    component: RanklistIndex
  },

  {
    path: "/user/bots/",
    name: "user_bots",
    component: UserbotIndex
  },

  {
    path: "/404/",
    name: "404",
    component: NotFound
  },

  {
    path: "/:catchAll(.*)",
    redirect: "/404/"
  },

  {
    path: "/user/account/login/",
    name: "user_account_login",
    component: UserAccountLoginView
  },

  {
    path: "/user/account/register/",
    name: "user_account_register",
    component: UserAccountRegisterView
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
