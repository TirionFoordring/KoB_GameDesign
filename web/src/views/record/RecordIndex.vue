<template>
    <ContentField>            
      <table class="table table-hover">
        <thead>
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
            <!-- 第1个玩家的信息-->
            <td>
              <img :src="record.a_photo" alt="" class="record-user-photo">
              <span class="record-user-username">{{ record.a_username }}</span>
            </td>
            <!-- 第2个玩家的信息-->
            <td>
              <img :src="record.b_photo" alt="" class="record-user-photo">
              <span class="record-user-username">{{ record.b_username }}</span>
            </td>
            <td>{{ record.result }}</td>
            <td>
              <button type="button" class="btn btn-outline-secondary btn-sm">View</button>
            </td>

          </tr>
        </tbody>
      </table>
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

    return{
      records,
    }
  }
}
</script>

<style scoped>

</style>