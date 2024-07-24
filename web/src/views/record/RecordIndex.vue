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
          console.log(total_records);
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