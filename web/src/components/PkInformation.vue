<template>
  <div class="pkinformation">
    <div class="row">
      <div class="col-1"></div>
      <div class="col-4">
        <!-- playerA的信息卡片 -->
        <div class="card mb-3 border-dark border-3 player-infocard">
          <div class="row g-0">
            <div class="col-md-4 d-flex justify-content-center align-items-center">
              <img :src="$store.state.user.photo" class="img-fluid rounded-start player-photo" alt="">
            </div>
            <div class="col-md-8">
              <div class="card-body" style="background-color: #E5E5FF;">
                <h4 class="card-title" style="font-weight: bold;">{{$store.state.user.username}}</h4>
                <p class="card-text" style="margin: 0; font-weight: bold;">Ranking: {{ $store.state.user.ranking }}</p>
                <p class="card-text" style="font-weight: bold;">Snake: <span v-if="$store.state.user.id == $store.state.pk.a_id" style="color: #4876EC;">blue</span> <span v-else-if="$store.state.user.id == $store.state.pk.b_id" style="color: #F94848;">red</span></p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-2">
        <div id="pkinfo-pk">VS</div>
      </div>
      <div class="col-4">
        <!-- playerB的信息卡片 -->
        <div class="card mb-3 border-dark border-3 player-infocard">
          <div class="row g-0">
            <div class="col-md-4 d-flex justify-content-center align-items-center">
              <img :src="$store.state.pk.opponent_photo" class="img-fluid rounded-start player-photo" alt="">
            </div>
            <div class="col-md-8">
              <div class="card-body" style="background-color: #E5E5FF;">
                <h4 class="card-title" style="font-weight: bold;">{{$store.state.pk.opponent_username}}</h4>
                <p class="card-text" style="margin: 0; font-weight: bold;">Ranking: {{ $store.state.pk.opponent_ranking }}</p>
                <p class="card-text" style="font-weight: bold;">Snake: <span v-if="$store.state.user.id == $store.state.pk.a_id" style="color: #F94848;">red</span> <span v-else-if="$store.state.user.id == $store.state.pk.b_id" style="color: #4876EC;">blue</span></p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="col-1"></div>
    </div>
  </div>
</template>

<script>
import { useStore } from "vuex";

export default{
  setup() {
    const store = useStore();
    console.log("My ranking is: " + store.state.user.ranking);
    console.log("The opponent player ranking is: " + store.state.user.opponent_ranking);
  }
}
</script>

<style scoped>
div.pkinformation {
  width: 60vw;  /* 60vw代表屏幕宽度的60% */
  height: 15vh; /* 15vh代表屏幕高度的15% */
  margin: 0 auto;
}

div.row,
div.col-1,
div.col-2,
div.col-4,
div.card,
div.card-body,
div.col-md-8,
div.col-md-4 {
  height: 100%;
}

div.player-infocard {
  height: 100%;
  /* background-color: pink; */
}

div#pkinfo-pk {
  height: 100%;
  text-align: center;
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;     /* 垂直居中 */
  font-style: italic;
  font-size: 500%;
  color: red;
  font-weight: bold;
}

img.player-photo {
  max-width: 99%;
  max-height: 99%
}
</style>
