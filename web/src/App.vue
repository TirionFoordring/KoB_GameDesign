<template>
  <NavBar></NavBar>
  <router-view></router-view>
</template>

<script>
import NavBar from "@/components/NavBar.vue"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
// import $ from "jquery"

export default {
  components:{
    NavBar
  },
  setup() {

    // $.ajax({
    //   url: "http://localhost:3000/user/account/register/",
    //   type: "post",
    //   data: {
    //     username: "jinshaobo",
    //     password: "123456",
    //     confirmedPassword: "123456",
    //   },
    //   success(resp){
    //     console.log(resp);
    //   },
    //   error(resp){
    //     console.log(resp);
    //   }
    // })
  }
}

</script>

<style>
body {
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
}
</style>
