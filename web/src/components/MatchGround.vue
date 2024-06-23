<template>
  <div class="matchground">
    <div class="row">
      <div class="col-6">
        <div class="user-photo">
          <img :src="$store.state.user.photo" alt="">
        </div>
        <div class="user-username">
          {{ $store.state.user.username }}
        </div>

        <!-- 选择出战的Bot -->
         <div class="user-select-bot">
          <select v-model="select_bot" class="form-select" aria-label="Default select example">
            <option value="-1" selected>Human</option>
            <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
          </select>
         </div>

      </div>

      <div class="col-6">
        <div class="user-photo">
          <img :src="$store.state.pk.opponent_photo" alt="">
        </div>
        <div class="user-username">
          {{ $store.state.pk.opponent_username }}
        </div>
      </div>

      <div class="col-12" style="text-align: center; padding-top: 18vh;">
        <button @click="click_match_btn" type="button" :class="[match_btn_isClicked ? 'btn btn-lg btn-dark' : 'btn btn-lg btn-primary']">{{ match_btn_info }}</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";
import { useStore } from "vuex";
import $ from "jquery";

export default {
  setup() {
    const store = useStore();
    const match_btn_isClicked = ref(false);
    const match_btn_info = ref("Start Matching");
    const bots = ref([]);
    let select_bot = ref("-1");

    const click_match_btn = () => {
      if(!match_btn_isClicked.value){
        match_btn_info.value = "Cancel Matching";
        // console.log("select_bot = " + select_bot.value);
        store.state.pk.socket.send(JSON.stringify({
          event: "start-matching",
          bot_id: select_bot.value, //向后端传输bot_id数据，代表要出战的bot
        }));
      } else {
        match_btn_info.value = "Start Matching";
        store.state.pk.socket.send(JSON.stringify({
          event: "cancel-matching",
        }));
      }
      match_btn_isClicked.value = !match_btn_isClicked.value;
    }

    //前段获取bot的列表，实现方法：调用后端的getList这个api，遍历所有当前登录用户的bot
    const refresh_bots = () => {
      $.ajax({
        url: "http://localhost:3000/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp){
          bots.value = resp;
        },
        error(resp) {
          console.log(resp);
        },
      })
    };

    refresh_bots();
    
    return {
      match_btn_info,
      click_match_btn,
      match_btn_isClicked,
      bots,
      select_bot,
    }
  }
}
</script>


<style scoped>
div.matchground{
  width: 60vw;  /*60vw代表屏幕宽度的60%*/
  height: 70vh; /*70vh代表屏幕高度的70%*/
  margin: 25px auto 0px;
  background-image: url("../img/matchground-background.jpg");
  background-size: cover;
  background-position: center;
}

div.user-photo {
  text-align: center;
  padding-top: 8vh;
}

div.user-photo > img {
  border-radius: 50%;
  width: 20vh;
}

div.user-username {
  color: white;
  text-align: center;
  font-size: 35px;
  font-weight: 600;
  padding-top: 2vh;
}

div.user-select-bot {
  margin: 20px;
}

div.user-select-bot > select {
  width: 50%;
  margin: 0 auto;
}
</style>