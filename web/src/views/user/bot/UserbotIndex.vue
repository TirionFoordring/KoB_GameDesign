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
    // setup() {
    //     const store =  useStore();
    //     $.ajax({
    //         url: "http://localhost:3000/user/bot/add/",
    //         type: "post",
    //         data: {
    //             title: "PDH's bot",
    //             description: "Description of PDH's bot",
    //             content: "Content of PDH's bot",
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

}
</script>

<style scoped>

</style>