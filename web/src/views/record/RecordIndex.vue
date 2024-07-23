<template>
    <ContentField>Record Page</ContentField>
</template>

<script>
import ContentField from "../../components/ContentField.vue"
import { useStore } from "vuex";
import $ from "jquery";

export default {
  components: {
    ContentField
  },
  setup() {
    const store = useStore();
    let current_page = 1;

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
          console.log(resp);
        },
        error(resp) {
          console.log(resp);
        },
      })
    }

    pull_page(current_page);
  }
}
</script>

<style scoped>

</style>