<template>
    <div ref="parent" class="GameMap">
        <canvas ref="canvas" tabindex="0"></canvas>
    </div>
</template>

<script>
import { Game_Map } from '@/assets/script/GameMap.js';
import { ref, onMounted } from "vue"; //onMounted表示挂载完后需要执行的操作
import { useStore } from "vuex";

export default {
    setup() {
        const store = useStore();
        let parent = ref(null);
        let canvas = ref(null);

        onMounted(() => {
            store.commit(
                "updateGameObject",
                new Game_Map(canvas.value.getContext('2d'), parent.value, store)
            );
        });

        return{
            parent,
            canvas
        }
    }
}
</script>

<style scoped>
div.GameMap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center; /*水平居中*/
    align-items: center; /*竖直居中*/
}
</style>