<template>
    <playground v-if="$store.state.pk.status === 'playing'"></playground>
    <pkInformation v-if="$store.state.pk.status === 'playing'"></pkInformation>

    <matchground v-if="$store.state.pk.status === 'matching'"></matchground>

    <ResultBoard v-if="$store.state.pk.loser !== 'none'"></ResultBoard>
</template>

<script>
import playground from "../../components/PlayGround.vue";
import pkInformation from "../../components/PkInformation.vue";
import { onMounted, onUnmounted } from "vue";
import { useStore } from "vuex";
import matchground from "../../components/MatchGround.vue";
import ResultBoard from "../../components/ResultBoard.vue";

export default {
    components:{
        playground,
        matchground,
        ResultBoard,
        pkInformation,
    },
    setup() {
        const store = useStore();
        const socketUrl = `ws://localhost:3000/websocket/${store.state.user.token}`; //此处由于链接内有表达式，所以必须用``来将链接括起来

        // 每次点开homepage时先更新一下updateLoser为none，防止显示结果的模态框未被清除
        store.commit("updateLoser", "none");
        // 每次点开homepage时先更新一下状态：现在不是录像
        store.commit("updateIsRecord", false);

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
                console.log("Received message:", data);
                if (data.event === "start-game") {
                    store.commit("updateOpponent", {
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                        ranking: data.opponent_ranking,
                    });
                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 2000);
                    store.commit("updateGame", data.game);
                } else if (data.event === "move"){
                    console.log("Move event received:", data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_move);
                    snake1.set_direction(data.b_move);
                } else if (data.event === "result"){
                    console.log("Game result:", data);
                    store.commit("updateLoser", data.loser); // 将败方从后端传来的msg中提取出来，并更新到全局变量
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;

                    if(data.loser === "all") {
                        snake0.status = "die";
                        snake1.status = "die";
                    } else if (data.loser === "A") {
                        snake0.status = "die";
                    } else if (data.loser === "B") {
                        snake1.status = "die";
                    }
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