<template>
    <ContentField>            
      <table class="table table-hover table-striped">
        <thead class="table-dark">
          <tr>
            <th>Player A</th>
            <th>Player B</th>
            <th>Result</th>
            <th>Create Time</th>
            <th>View</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="record in records" :key="record.record.id">
            <!-- 第1个玩家的信息 -->
            <td>
              <img :src="record.a_photo" alt="" class="record-user-photo">
              &nbsp;
              <span class="record-user-username">{{ record.a_username }}</span>
            </td>

            <!-- 第2个玩家的信息 -->
            <td>
              <img :src="record.b_photo" alt="" class="record-user-photo">
              &nbsp;
              <span class="record-user-username">{{ record.b_username }}</span>
            </td>

            <!-- 对战结果 -->
            <td>
              <span v-if="record.result == 'A' || record.result == 'B'">The winner is </span>
              <span id="gameResult">{{ record.result }}</span><span v-if="record.result == 'A' || record.result == 'B'">.</span>
            </td>

            <!-- 对战创建时间 -->
            <td>
              {{ record.record.createtime }}
            </td>

            <!-- 观看对局记录按钮 -->
            <td>
              <button @click="open_record_view(record.record.id)" type="button" class="btn btn-outline-primary btn-sm">View Record</button>
            </td>
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
import router from "../../router/index.js";
import { useStore } from "vuex";
import { ref } from "vue";
import $ from "jquery";

export default {
  components: {
    ContentField
  },
  setup() {
    const store = useStore();
    let records = ref([]);
    let current_page = 1;
    let total_records = 0;
    let pages = ref([]);

    const click_page = page => {
      if (page === -2) page = current_page - 1;
      else if (page === -1) page = current_page + 1;
      let max_pages = parseInt(Math.ceil(total_records / 10));
      if (page >= 1 && page <= max_pages) {
        pull_page(page);
      }
    }

    // 查看记录时的翻页功能
    const update_pages = () => {
      let max_pages = parseInt(Math.ceil(total_records / 10));
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
        url: "http://localhost:3000/record/getlist/",
        data: {
          page,
        },
        type: "get",
        headers: {
          Authorization: "Bearer " + store.state.user.token,
        },
        success(resp){
          records.value = resp.records;
          total_records = resp.recordsCount; // 该变量名在后端GetRecordListServiceImpl.java文件中定义
          update_pages();
        },
        error(resp) {
          console.log(resp);
        },
      })
    }

    pull_page(current_page);

    const stringTo2DArray = map => {
      let g = [];
      for (let i = 0, k = 0; i < 13; i++) {
        let line = [];
        for (let j = 0; j < 14; j++, k++) {
          if (map[k] === '0') line.push(0);
          else line.push(1);
        }
        g.push(line);
      }
      return g;
    };

    const open_record_view = recordId => {
      for (const record of records.value) {
        if (record.record.id === recordId){
          store.commit("updateIsRecord", true);
          // console.log(record);
          store.commit("updateGame", {
            map: stringTo2DArray(record.record.map),
            a_id: record.record.aid,
            a_sx: record.record.asx,
            a_sy: record.record.asy,
            b_id: record.record.bid,
            b_sx: record.record.bsx,
            b_sy: record.record.bsy,
          });
          store.commit("updateSteps", {
            a_steps: record.record.asteps,
            b_steps: record.record.bsteps,
          });
          store.commit("updateRecordLoser", record.record.loser);
          router.push({
            name: "recordView",
            params: {
              recordId: recordId,
            }
          });
          break;
        }
      }
    }

    return{
      records,
      open_record_view,
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