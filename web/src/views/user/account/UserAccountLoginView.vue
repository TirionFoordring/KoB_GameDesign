<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">Username</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="Please enter your username.">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="Please enter your password.">
                    </div>
                    <div class="mb-3 form-check">
                        <input type="checkbox" class="form-check-input" id="exampleCheck1">
                        <label class="form-check-label" for="exampleCheck1">Check me out</label>
                    </div>
                    <div id="error_message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary" id="submit">Submit</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from "@/components/ContentField.vue"
import { useStore } from "vuex"
import { ref } from "vue"
import router from "@/router/index.js"

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let username = ref("");
        let password = ref("");
        let error_message = ref("");

        //为了保证刷新后不退出更新，将token存在浏览器的localStorage里，每次登陆之前先查看本地是否已有token
        const jwt_token = localStorage.getItem("jwt_token");
        if (jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo",{
                success() {
                    router.push({name: "home"});
                    store.commit("updatePullingInfo", false); //向mutations中的函数传数据用commit，向actions中的函数传数据用dispatch
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        } else {
            store.commit("updatePullingInfo", false);
        }

        const login = () => {
            error_message.value = "";
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    //getinfo函数在user.js中定义
                    store.dispatch("getinfo", {
                        success(){
                            router.push({ name: "home"});
                        }
                    }); 
                    router.push({ name: "home"});
                },
                error() {
                    error_message.value = "Incorrect username or password";
                }
            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }

    }
}
</script>

<style scoped>
    button#submit{
        width: 100%
    }
    div#error_message{
        color: red;
        margin: 0 0 8px;
    }

</style>