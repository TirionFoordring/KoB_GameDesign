<template>
  <NavBar></NavBar>
  <router-view></router-view>
</template>

<script>
import NavBar from "@/components/NavBar.vue"
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap"
import $ from "jquery"

export default {
  components:{
    NavBar
  },
  setup() {
    $.ajax({
      url: "http://localhost:3000/user/account/token/",
      type: "post",
      data: {
        username: "jin",
        password: "jinpass",
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      }
    });

    $.ajax({
      url: "http://localhost:3000/user/account/info/",
      type: "get",
      headers: {
        // "Bearer "在config.filter.JwtAuthenticationTokenFilter.java这个文件中定义
        Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxZWM0ZmVhODJlZWU0YzAyODE1ZTEyNDZhMWFkMjU2YiIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTcxNjY0MDg3NCwiZXhwIjoxNzE3ODUwNDc0fQ.aCvtOU2Bo-L8lldm84iNWsz8h2bTz8gUVLIRl0e3szs",
      },
      success(resp){
        console.log(resp);
      },
      error(resp){
        console.log(resp);
      }
    });

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
