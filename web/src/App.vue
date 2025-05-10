<template>
  <NavBar></NavBar>
  <router-view></router-view>
</template>

<script>
import NavBar from "@/components/NavBar.vue"
// import NavBarNew from "@/components/NavBar-new.vue"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
// import $ from "jquery"

export default {
  components:{
    NavBar
  },
}

</script>

<style>
body {
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
}
</style>