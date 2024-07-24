//mutations和actions的区别：
//mutations中的函数必须都是同步的，需要从云端拉取信息后再执行的函数属于异步函数，不能出现在mutations中
//actions用于存储异步函数
export default {
    state: {
        is_record: false,
        a_steps: "",
        b_steps: "",
        record_loser: "",
    },
    getters: {
    },
    mutations: {
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateSteps(state, data) {
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
        },
        updateRecordLoser(state, loser) {
            state.record_loser = loser;
        },
    },
    actions: {
    },
    modules: {
    }
}