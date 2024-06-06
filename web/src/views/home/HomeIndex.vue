<template>
    <playground v-if="$store.state.pk.status === 'playing'">
        
    </playground>

    <matchground v-if="$store.state.pk.status === 'matching'">

    </matchground>

</template>

<script>
import playground from "../../components/PlayGround.vue";
import { onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";
import matchground from "../../components/MatchGround.vue";

export default {
    components:{
        playground,
        matchground,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://localhost:3000/websocket/${store.state.user.token}`; //此处由于链接内有表达式，所以必须用``来将链接括起来

        let socket = null;
        onMounted(() => {
            store.commit("updateOpponent", {
                username: "Your Opponent",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            })
            socket = new WebSocket(socketUrl);
            socket.onopen = () => {
                console.log("Connected successfully.");
                store.commit("updateSocket", socket);
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                if (data.event === "start-game") {
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);
                    store.commit("updateGame", data.game);
                } else if (data.event === "move"){
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_move);
                    snake1.set_direction(data.b_move); //a.move和b.move变量名在后端Game.java文件中定义
                } else if (data.event === "result"){
                    console.log(data);
                }
            }

            socket.onclose = () => {
                console.log("Disconnected successfully.");
            }
        });

        onUnmounted(() => {
            socket.close();
            store.commit("updateStatus", "matching");
        })
    }
}

</script>

<style scoped>

</style>