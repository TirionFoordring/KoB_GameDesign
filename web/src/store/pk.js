//mutations和actions的区别：
//mutations中的函数必须都是同步的，需要从云端拉取信息后再执行的函数属于异步函数，不能出现在mutations中
//actions用于存储异步函数
export default {
    state: {
        status: "matching", //matching为匹配界面，playing为对战界面
        socket: null,
        opponent_username: "",
        opponent_photo: "",
    },
    getters: {
    },
    mutations: {
        updateSocket(state, socket) {
            state.socket = socket;
        },
        updateOpponent(state, opponent) {
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state, status) {
            state.status = status;
        },
    },
    actions: {
    },
    modules: {
    }
}