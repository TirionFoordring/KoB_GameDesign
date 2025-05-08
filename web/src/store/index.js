import { createStore } from 'vuex';
import ModuleUser from "./user.js";
import ModulePk from "./pk.js";
import ModuleRecord from "./record.js"

export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
    updatePhoto(state, newPhotoUrl) {
      state.user.photo = newPhotoUrl;
    },
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: ModulePk,
    record: ModuleRecord,
  }
})
