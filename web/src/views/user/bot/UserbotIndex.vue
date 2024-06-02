<template>
    <ContentField>User Page</ContentField>
</template>

<script>
import ContentField from "../../../components/ContentField.vue";
// import $ from "jquery";
// import { useStore } from "vuex";

export default{
    components:{
        ContentField
    },
    // 测试新增bot功能
    // setup() {
    //     const store =  useStore();
    //     $.ajax({
    //         url: "http://localhost:3000/user/bot/add/",
    //         type: "post",
    //         data: {
    //             title: "Jin's bot",
    //             description: "Description of Jin's bot",
    //             content: "Content of Jin's bot",
    //         },
    //         headers: {
    //             Authorization: "Bearer " + store.state.user.token,
    //         },
    //         success(resp) {
    //             console.log(resp);
    //         },
    //         error(resp) {
    //             console.log(resp);
    //         }
    //     })
    // }

    // 测试删除bot功能
    // setup() {
    //     const store =  useStore();
    //     $.ajax({
    //         url: "http://localhost:3000/user/bot/remove/",
    //         type: "post",
    //         data: {
    //             bot_id: 2,
    //         },
    //         headers: {
    //             Authorization: "Bearer " + store.state.user.token,
    //         },
    //         success(resp) {
    //             console.log(resp);
    //         },
    //         error(resp) {
    //             console.log(resp);
    //         }
    //     })
    // }

    // 测试修改bot功能
    // setup() {
    //     const store =  useStore();
    //     $.ajax({
    //         url: "http://localhost:3000/user/bot/update/",
    //         type: "post",
    //         data: {
    //             bot_id: 5,
    //             title: "My first bot",
    //             description: "Description of my first bot.",
    //             content: "System.out.println(\"Hello World\")",

    //         },
    //         headers: {
    //             Authorization: "Bearer " + store.state.user.token,
    //         },
    //         success(resp) {
    //             console.log(resp);
    //         },
    //         error(resp) {
    //             console.log(resp);
    //         }
    //     })
    // }

    // 测试查询bot功能
    // setup() {
    //     const store =  useStore();
    //     $.ajax({
    //         url: "http://localhost:3000/user/bot/getlist/",
    //         type: "get",
    //         headers: {
    //             Authorization: "Bearer " + store.state.user.token,
    //         },
    //         success(resp) {
    //             console.log(resp);
    //         },
    //         error(resp) {
    //             console.log(resp);
    //         }
    //     })
    // }

}
</script>

<style scoped>

</style>