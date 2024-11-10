<template>
    <div class="playground">
        <GameMap></GameMap>
    </div>
</template>

<script>
import GameMap from "./GameMap.vue"
export default {
    components:{
        GameMap,
    }
}

</script>

<style scoped>
div.playground {
    width: 60vw;  /*60vw代表屏幕宽度的60%*/
    height: 70vh; /*70vh代表屏幕高度的70%*/
    margin: 25px auto 0px;
    /* background-color: aqua; */
}
</style>