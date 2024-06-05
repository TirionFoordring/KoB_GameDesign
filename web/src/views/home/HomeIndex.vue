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
            }

            socket.onmessage = msg => {
                const data = JSON.parse(msg.data);
                console.log(data);
            }

            socket.onclose = () => {
                console.log("Disconnected successfully.");
            }
        });

        onUnmounted(() => {
            socket.close();
        })
    }
}

</script>

<style scoped>

</style>