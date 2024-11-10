<template>
    <ContentField>            
      <table class="table table-hover table-striped">
        <thead class="table-dark">
          <tr>
            <th>Username</th>
            <th>Ranking</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <!-- 玩家昵称 -->
            <td>
              <img :src="user.photo" alt="" class="record-user-photo">
              &nbsp;
              <span class="record-user-username">{{ user.username }}</span>
            </td>

            <!-- 玩家天梯积分 -->
             <td>{{ user.ranking }}</td>

          </tr>
        </tbody>
      </table>

      <nav aria-label="Page navigation example" style="float: right; margin-right: 1vw;">
        <ul class="pagination">
          <!-- 按钮：前一页 -->
          <li class="page-item" @click="click_page(-2)">
            <a class="page-link" href="#" aria-label="Previous">
              <span aria-hidden="true">pre</span>
            </a>
          </li>
          <li :class="'page-item ' + page.is_active" v-for="page in pages" :key="page.number" @click="click_page(page.number)">
            <a class="page-link" href="#">{{ page.number }}</a>
          </li>
          <!-- 按钮：后一页 -->
          <li class="page-item" @click="click_page(-1)">
            <a class="page-link" href="#" aria-label="Next">
              <span aria-hidden="true">next</span>
            </a>
          </li>
        </ul>
      </nav>

</ContentField>
</template>

<script>
import ContentField from "../../components/ContentField.vue";
import { useStore } from "vuex";
import { ref } from "vue";
import $ from "jquery";

export default {
  components: {
    ContentField
  },
  setup() {
    const store = useStore();
    let users = ref([]);
    let current_page = 1;
    let total_users = 0;
    let pages = ref([]);

    const click_page = page => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_users / 5));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    }

    // 查看记录时的翻页功能
    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total_users / 5));
      let new_pages = [];
      for (let i = current_page - 2; i <= current_page + 2; i++) {
        if (i >= 1 && i <=max_pages) {
          new_pages.push({
            number: i,
            is_active: i === current_page ? "active" : "",
          });
        }
      }
      pages.value = new_pages;
    }

    const pull_page = page => {
      current_page = page;
      $.ajax({
        url: "http://localhost:3000/ranklist/getlist/",
        data: {
          page,
        },
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp){
          users.value = resp.users;
          total_users = resp.usersCount; // 该变量名在后端GetRanklistServiceImpl.java文件中定义
          update_pages();
        },
        error(resp) {
          console.log(resp);
        },
      })
    }

    pull_page(current_page);

    return{
      users,
      pages,
      click_page,
    }
  }
}
</script>

<style scoped>
img.record-user-photo {
  width: 4vh;
  border-radius: 50%;
}

/* 表头和表身代码居中 */
.table th, .table td {
  text-align: center;
  vertical-align: middle;
}

span#gameResult {
  font-weight: bold;
}
</style>