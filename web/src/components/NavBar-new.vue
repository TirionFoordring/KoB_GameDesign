<template>
  <div class="d-flex">
    <!-- 左侧导航栏 -->
    <nav class="sidebar">
      <!-- 导航栏标题部分 -->
      <div class="Navbar-title">
        <router-link class="Navbar-title" :to="{name: 'home'}">Your Toy Box</router-link>
      </div>
      <!-- 用户中心与设置部分 -->
      <!-- 如果登录成功-->
      <div class="navbar-nav" v-if="$store.state.user.is_login">
        <div class="Navbar-userAndSetting">
          <!-- 用户中心 -->
          <span class="dropdown" id="userCenterDropdown">
            <!-- 用户信息区域 -->
            <button id="userCenterButton" class="btn btn-outline-secondary dropdown-toggle d-flex align-items-center position-relative" type="button" data-bs-toggle="dropdown" aria-expanded="false">
              <div class="d-flex align-items-center flex-grow-1" style="gap: 10px;">
                <img :src="$store.state.user.photo" alt="头像" width="48" height="48" class="rounded-circle" />
                <div class="d-flex flex-column text-start">
                  <div class="fw-bold">{{ $store.state.user.username }}</div>
                  <div class="small">Lv.5</div>
                </div>
              </div>
            </button>
            <!-- 下拉菜单 -->
            <ul class="dropdown-menu">
              <li><router-link class="dropdown-item" :to="{name: 'user_bots'}"><img src="../assets/icon/icon-myAccount.png" class="Navbar-contentIcon">My Account</router-link></li>
              <li><a class="dropdown-item" href="#"><img src="../assets/icon/icon-profilePhoto.png" class="Navbar-contentIcon">Change Profile Photo</a></li>
              <li><a class="dropdown-item" href="#" @click="logout"><img src="../assets/icon/icon-logout.png" class="Navbar-contentIcon">Log Out</a></li>
            </ul>
          </span>

          <!-- 设置按钮 -->
          <a href="#" id="settingButton">
            <img src="../assets/icon/icon-setting.png" width="30px">
          </a>
        </div>
      </div>

      <!-- 如果未登录 -->
      <div class="navbar-nav" v-else-if="!$store.state.user.pulling_info" >
        <div class="Navbar-userAndSetting">
          <div class="d-flex gap-2 w-100">
            <router-link :to="{ name: 'user_account_login' }" custom v-slot="{ navigate }" class="flex-fill">
              <button type="button" class="btn btn-outline-success w-100" @click="navigate">
                Login
              </button>
            </router-link>

            <router-link :to="{ name: 'user_account_register' }" custom v-slot="{ navigate }" class="flex-fill">
              <button type="button" class="btn btn-outline-primary w-100" @click="navigate">
                Register
              </button>
            </router-link>
          </div>
        </div>
      </div>


      <!-- 侧边导航栏内容卡1: 主页 + 商城 -->
      <div class="Navbar-contentCard">
        <ul class="nav flex-column">
          <li class="nav-item">
            <a class="nav-link" href="#"><img src="../assets/icon/icon-home.png" class="Navbar-contentIcon">Homepage</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#"><img src="../assets/icon/icon-store.png" class="Navbar-contentIcon">Store</a>
          </li>
        </ul>
      </div>

      <!-- 侧边导航栏内容卡2: 游戏 -->
      <div class="Navbar-contentCard">
        <div class="Navbar-contentCard-title">Games</div>
        <ul class="nav flex-column">
          <li class="nav-item">
            <a class="nav-link" href="#"><img src="../assets/icon/icon-GreedySnake.png" class="Navbar-contentIcon">Greedy Snake: Duel</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#"><img src="../assets/icon/icon-Gobang.png" class="Navbar-contentIcon">Gobang</a>
          </li>
        </ul>
      </div>
    </nav>
  </div>
</template>

<script>
//Judging the current page and highlight it in Navbar.
import { useRoute } from "vue-router"
import { computed } from "vue"
import { useStore } from "vuex";

export default {
  setup() {
    const store = useStore();
    const route = useRoute();
    let route_name = computed(() => route.name);

    const logout = () => {
      store.dispatch("logout");
    }

    return {
      route_name,
      logout
    };
  }
}
</script>

<style scoped>
.sidebar {
  min-width: 220px;
  width: 17vw;
  max-width: 280px;
  height: 100vh;
  position: fixed;
  top: 0;
  left: 0;
  background-color: #F1F3F5;
  border-right: 1px solid #cbcbcc;
  display: flex;
  flex-direction: column;
}

.Navbar-title {
  padding: 1rem;
  text-align: center;
}

a.Navbar-title {
  font-size: 1.5rem;
  font-weight: bold;
  color: black;
  text-decoration: none;
}

.Navbar-userAndSetting {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 1rem;
  border-bottom: 1px solid #cbcbcc;
  box-sizing: border-box;
}

#userCenterDropdown {
  flex-grow: 1;
  margin-right: 0.5rem;
}

#userCenterButton {
  width: 100%;
}

#settingButton {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

#settingButton:hover {
  background-color: #DDDDDD;
}

.nav {
  flex-grow: 1;
  padding-left: 1rem;
}

.nav-link {
  font-size: 17px;
  display: flex;
  align-items: center;
  color: #333;
}

.nav-link:hover {
  background-color: #ddd;
  border-radius: 5px;
  color: #333;
}

.nav-link:focus,
.nav-link:active {
  color: #333;
  outline: none;
  box-shadow: none;
  text-decoration: none;
}

.Navbar-contentCard{
  width: 100%;
  padding: 1rem 0;
  border-bottom: 1px solid #cbcbcc;
}

ul.flex-column{
  padding: 0;
}

img.Navbar-contentIcon{
  width: 10%;
  margin-right: 1rem;
}

.Navbar-contentCard-title{
  margin: 0 0 0.5rem 0.5rem;
  font-size: 16px;
  font-weight: bold;
}

.dropdown-item{
  color: black;
  font-size: small;
}

.dropdown-item:hover{
  color: black;
}

.dropdown-item:active{
  background-color: #DCDCDC;
  color: black;
}

/*响应式：小屏自动隐藏sidebar */
@media (max-width: 768px) {
  .sidebar {
    position: absolute;
    transform: translateX(-100%);
    transition: transform 0.3s ease-in-out;
    z-index: 1000;
  }

  .sidebar.active {
    transform: translateX(0);
  }
}
</style>