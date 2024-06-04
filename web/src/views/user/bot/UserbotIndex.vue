<template>
  <div class="container">
    <div class="row">

      <div class="col-3">
        <div class="card" style="margin: 20px;">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" style="width: 100%;">
          </div>
        </div>
      </div>

      <div class="col-9">
        <div class="card" style="margin-top: 20px;">
          <div class="card-header">
            <span class="header">My Bots</span>
            <button type="button" class="btn btn-dark float-end" data-bs-toggle="modal" data-bs-target="#addBotModal">Create a new bot</button>

            <!-- Modal for create a new bot -->
            <div class="modal fade" id="addBotModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
              <div class="modal-dialog modal-xl">
                <div class="modal-content">
                  <div class="modal-header">
                    <h1 class="modal-title fs-5" id="staticBackdropLabel" style="font-weight: bold;">Create A New Bot</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <form>
                      <!-- 用 v-model="xxx" 语句将输入内容绑定至js中的变量或对象 -->
                      <div class="mb-3">
                        <label for="addBotTitle" class="form-label" style="font-weight: bold; font-size: 120%;">Title</label>
                        <input v-model="botadd.title" type="text" class="form-control" id="addBotTitle" placeholder="Please enter the title of your new bot.">
                      </div>
                      <div class="mb-3">
                        <label for="addBotDescription" class="form-label" style="font-weight: bold; font-size: 120%;">Description</label>
                        <textarea v-model="botadd.description" class="form-control" placeholder="Please enter the description of your new bot." id="addBotDescription" style="height: 100px"></textarea>
                      </div>
                      <div class="mb-3">
                        <label for="addBotContent" class="form-label" style="font-weight: bold; font-size: 120%;">Code</label>
                        <!-- 在线编译器 -->
                        <VAceEditor v-model:value="botadd.content" @init="editorInit" lang="c_cpp"
                          theme="textmate" style="height: 300px" :options="{
                              enableBasicAutocompletion: true, //启用基本自动完成
                              enableSnippets: true, // 启用代码段
                              enableLiveAutocompletion: true, // 启用实时自动完成
                              fontSize: 18, //设置字号
                              tabSize: 4, // 标签大小
                              showPrintMargin: false, //去除编辑器里的竖线
                              highlightActiveLine: true,
                            }" />
                      </div>
                    </form>
                  </div>
                  <div class="modal-footer">
                    <div id="error_message">{{ botadd.error_message }}</div>
                    <button type="button" class="btn btn-outline-primary" @click="add_bot">Save and UpLoad</button>
                    <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                  </div>
                </div>
              </div>
            </div>

          </div>
          <div class="card-body">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th>Bots Name</th>
                  <th>Create Time</th>
                  <th>Handle</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="bot in bots" :key="bot.id">
                  <td>{{ bot.title }}</td>
                  <td>{{ bot.createtime }}</td>
                  <td>
                    <button id="editButton" type="button" class="btn btn-outline-secondary btn-sm" data-bs-toggle="modal" :data-bs-target="'#updateBotModal-' + bot.id">Edit</button>
                    <button @click="remove_bot(bot)" id="deleteButton" type="button" class="btn btn-outline-danger btn-sm">Delete</button>

                    <!-- Modal for edit a bot -->
                    <div class="modal fade" :id="'updateBotModal-' + bot.id" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                      <div class="modal-dialog modal-xl">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h1 class="modal-title fs-5" id="staticBackdropLabel" style="font-weight: bold;">Edit My Bot</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                          </div>
                          <div class="modal-body">
                            <form>
                            <!-- 用 v-model="xxx" 语句将输入内容绑定至js中的变量或对象 -->
                              <div class="mb-3" style="text-align: left;">
                                <label for="addBotTitle" class="form-label" style="font-weight: bold; font-size: 120%;" >Title</label>
                                <input v-model="bot.title" type="text" class="form-control" id="addBotTitle" placeholder="Please enter the title of your new bot.">
                              </div>
                              <div class="mb-3" style="text-align: left;">
                                <label for="addBotDescription" class="form-label" style="font-weight: bold; font-size: 120%;">Description</label>
                                <textarea v-model="bot.description" class="form-control" placeholder="Please enter the description of your new bot." id="addBotDescription" style="height: 100px"></textarea>
                              </div>
                              <div class="mb-3" style="text-align: left;">
                                <label for="addBotContent" class="form-label" style="font-weight: bold; font-size: 120%;">Code</label>
                                <!-- 在线编译器 -->
                                <VAceEditor v-model:value="bot.content" @init="editorInit"
                                  lang="c_cpp" theme="textmate" style="height: 300px"
                                  :options="{
                                      enableBasicAutocompletion: true, //启用基本自动完成
                                      enableSnippets: true, // 启用代码段
                                      enableLiveAutocompletion: true, // 启用实时自动完成
                                      fontSize: 18, //设置字号
                                      tabSize: 4, // 标签大小
                                      showPrintMargin: false, //去除编辑器里的竖线
                                      highlightActiveLine: true,
                                    }" />
                              </div>
                            </form>
                          </div>
                          <div class="modal-footer">
                            <div id="error_message">{{ bot.error_message }}</div>
                            <button type="button" class="btn btn-outline-primary" @click="update_bot(bot)">Save and UpLoad</button>
                            <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">Cancel</button>
                          </div>
                        </div>
                      </div>
                    </div>

                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import $ from "jquery";
import { useStore } from "vuex";
import { Modal } from "bootstrap/dist/js/bootstrap";
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-c_cpp';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';

export default{

  components: {
    VAceEditor,
  },

  setup() {

    //将编译器集成进页面的配置
    ace.config.set(
      "basePath",
      "https://cdn.jsdelivr.net/npm/ace-builds@" +
      require("ace-builds").version +
      "/src-noconflict/"
    );

    const store = useStore();
    let bots = ref([]);

    const botadd = reactive({
      title: "",
      description: "",
      content: "",
      error_message: "",
    });

    //刷新前段显示bot的列表，实现方法：调用后端的getList这个api，遍历所有当前登录用户的bot
    const refresh_bots = () => {
      $.ajax({
        url: "http://localhost:3000/user/bot/getlist/",
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp){
          bots.value = resp;
        },
        error(resp) {
          console.log(resp);
        },
      })
    };

    refresh_bots();

    //点击创建按钮后触发该函数，向后端发送创建新bot的请求及全部信息
    const add_bot = () => {
      botadd.error_message = "";
      $.ajax({
        url: "http://localhost:3000/user/bot/add/",
        type: "post",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data: {
          title: botadd.title,
          description: botadd.description,
          content: botadd.content,
        },
        success(resp){
          if(resp.error_message === "Add bot successfully."){
            botadd.title = "";
            botadd.description = "";
            botadd.content = "";
            Modal.getInstance("#addBotModal").hide();
            refresh_bots();
          } else {
            botadd.error_message = resp.error_message;
          }
        }
      });
    };

    //删除bot
    const remove_bot = (bot) => {
      $.ajax({
        url: "http://localhost:3000/user/bot/remove/",
        type: "post",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data: {
          bot_id: bot.id,
        },
        success(resp) {
          if (resp.error_message === "SUCCESS! The bot has been deleted.") {
            refresh_bots();
          }
        },
      })
    }

    //编辑和更新现有bot
    const update_bot = (bot) => {
      botadd.error_message = "";
      $.ajax({
        url: "http://localhost:3000/user/bot/update/",
        type: "post",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        data: {
          bot_id: bot.id,
          title: bot.title,
          description: bot.description,
          content: bot.content,
        },
        success(resp){
          if(resp.error_message === "SUCCESS! Bot has been updated."){
            Modal.getInstance("#updateBotModal-" + bot.id).hide();
            refresh_bots();
          } else {
            botadd.error_message = resp.error_message;
          }
        }
      });
    };

    return {
      bots,
      botadd,
      add_bot,
      remove_bot,
      update_bot,
    }
  }
}
</script>

<style scoped>
span.header{
  font-size: 150%;
  font-weight: bold;
}

button#editButton {
  margin-right: 5px;
}

button#deleteButton {
  margin-left: 5px;
}

/* 表头和表身代码居中 */
.table th, .table td {
  text-align: center;
  vertical-align: middle;
}

div#error_message{
  color: red;
}
</style>