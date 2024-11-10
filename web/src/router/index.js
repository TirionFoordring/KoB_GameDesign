import { createRouter, createWebHistory } from 'vue-router'

// import each pages
import HomeIndex from "../views/home/HomeIndex.vue";
import RecordIndex from "../views/record/RecordIndex.vue";
import RecordView from "../views/record/RecordView.vue";
import RanklistIndex from "../views/ranklist/RanklistIndex.vue";
import UserbotIndex from "../views/user/bot/UserbotIndex.vue";
import NotFound from "../views/error/NotFound.vue";
import UserAccountLoginView from "../views/user/account/UserAccountLoginView.vue";
import UserAccountRegisterView from "../views/user/account/UserAccountRegisterView.vue";
import store from "../store/index.js";

const routes = [
  {
    path: "/",
    redirect: "/home/",
    meta: {
      requireAuth: true,
    },
  },

  {
    path: "/home/",
    name: "home",
    component: HomeIndex,
    meta: {
      requireAuth: true,
    },
  },

  {
    path: "/record/",
    name: "record",
    component: RecordIndex,
    meta: {
      requireAuth: true,
    },
  },

  // 在path里面使用冒号可以向路由中加入参数
  {
    path: "/record/:recordId/",
    name: "recordView",
    component: RecordView,
    meta: {
      requireAuth: true,
    },
  },

  {
    path: "/ranklist/",
    name: "ranklist",
    component: RanklistIndex,
    meta: {
      requireAuth: true,
    },
  },

  {
    path: "/user/bots/",
    name: "user_bots",
    component: UserbotIndex,
    meta: {
      requireAuth: true,
    },
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

//重定向功能：当未登录的时候，无论点击哪个页面均跳转至登录页面
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth && !store.state.user.is_login) {
    next({ name: "user_account_login" });
  } else {
    next();
  }
})

export default router
