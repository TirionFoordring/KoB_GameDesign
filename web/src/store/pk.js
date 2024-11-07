//mutations和actions的区别：
//mutations中的函数必须都是同步的，需要从云端拉取信息后再执行的函数属于异步函数，不能出现在mutations中
//actions用于存储异步函数
export default {
    state: {
        status: "matching", //matching为匹配界面，playing为对战界面
        socket: null,
        opponent_username: "",
        opponent_photo: "",
        opponent_ranking: "",
        gamemap: null,
        a_id: 0,
        a_sx: 0,
        a_sy: 0,
        b_id: 0,
        b_sx: 0,
        b_sy: 0,
        gameObject: null,
        loser: "none", //none表示游戏正在进行中，all为平局，A/B代表胜负方
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
            state.opponent_ranking = opponent.ranking;
        },
        updateStatus(state, status) {
            state.status = status;
        },
        //相关变量名在后端定义
        updateGame(state, game) {
            state.gamemap = game.map;
            state.a_id = game.a_id;
            state.a_sx = game.a_sx;
            state.a_sy = game.a_sy;
            state.b_id = game.b_id;
            state.b_sx = game.b_sx;
            state.b_sy = game.b_sy;
        },
        updateGameObject(state, gameObject) {
            state.gameObject = gameObject;
        },
        updateLoser(state, loser) {
            state.loser = loser;
        }
    },
    actions: {
    },
    modules: {
    }
}