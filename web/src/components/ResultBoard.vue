<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.loser === 'all'">
            Draw
        </div>

        <div class="result-board-text" v-else-if="$store.state.pk.loser === 'A' && $store.state.pk.a_id === parseInt($store.state.user.id)">
            Lose
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser === 'B' && $store.state.pk.b_id === parseInt($store.state.user.id)">
            Lose
        </div>

        <div class="result-board-text" v-else>
            Win
        </div>

        <div class="result-board-btn">
            <button @click="backToHomePage" type="button" class="btn btn-light">Back to Home Page</button>
        </div>
    </div>
</template>

<script>
import { useStore } from "vuex";

export default {
    setup() {
        const store = useStore();
        
        const backToHomePage = () => {
            store.commit("updateStatus", "matching");
            store.commit("updateLoser", "none");
            store.commit("updateOpponent", {
                username: "Your Opponent",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            });
        };

        return {
            backToHomePage
        };
    }
}

</script>

<style scoped>
div.result-board {
    height: 30vh;
    width: 30vw;
    background-color: rgba(50, 50, 50, 0.5);
    position: absolute;
    top: 30vh;
    left: 35vw;
}

div.result-board-text {
    text-align: center;
    color: red;
    font-size: 50px;
    font-weight: 600;
    padding-top: 5vh;
}

div.result-board-btn {
    padding-top: 8vh;
    text-align: center;
}
</style>