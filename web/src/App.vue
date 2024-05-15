<template>
  <div>
    <div>Bot1 name: {{ bot1_name }}</div>
    <div>Bot1 Version: {{ bot1_version }}</div>
  </div>
  <router-view />
</template>

<script>
import $ from "jquery";
import { ref } from "vue";

export default {
  name: "App",
  setup: () => {
    let bot1_name = ref("");
    let bot1_version = ref("");

    $.ajax({
      url: "http://localhost:3000/pk/getbotinfo/",
      type: "get",
      success: resp => {
        console.log(resp);
        bot1_name.value = resp.Name;
        bot1_version.value = resp.Version;
      }
    })

    return {
      bot1_name,
      bot1_version
    }
  }
}

</script>

<style>
body {
  background-image: url("@/assets/background.png");
  background-size: cover;
}
</style>
