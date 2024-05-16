<template>
    <div ref="parent" class="GameMap">
        <canvas ref="canvas"></canvas>
    </div>
</template>

<script>
import { Game_Map } from '@/assets/script/GameMap'
import { ref, onMounted } from "vue" //onMounted表示挂载完后需要执行的操作
export default {
    setup() {
        let parent = ref(null);
        let canvas = ref(null);

        onMounted(() => {
            new Game_Map(canvas.value.getContext('2d'), parent.value);
        })

        return{
            parent,
            canvas
        }
    }
}
</script>

<style scoped>
div.GameMap{
    width: 100%;
    height: 100%;
}
</style>