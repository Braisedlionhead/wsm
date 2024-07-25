import { login, logout } from "@/api/user";
import storage from "@/util/storage.js";

const user = {
    state: {
        userName: '',
        nickName: '',
        token: '',
        roles: [],
        permissions: []
    },
    getters: {
        isLogin(state) {
            return !!state.userName;
        },
        roles(state) {
            return state.roles;
        },
        permissions(state) {
            return state.permissions;
        },
    },
    // 通常使用mutations来修改state的值
    mutations: {
        SAVE_USERNAME(state, userName) {
            state.userName = userName;
        },
        SAVE_NICKNAME(state, nickName) {
            state.nickName = nickName;
        },
        SAVE_TOKEN(state, token) {
            state.token = token;
        },
        SAVE_ROLES(state, roles) {
            state.roles = roles;
        },
        SAVE_PERMISSIONS(state, permissions) {
            state.permissions = permissions;
        }
    },
    actions: {
        LOGIN({ commit }, user) {
            return new Promise(function (resolve) {
                login(user).then(res => {
                    if (res.data.code === 200) {
                        console.log('suc')
                        // 需要将获取的数据存储到vuex中
                        commit("SAVE_USERNAME", res.data.data.userName);
                        commit("SAVE_NICKNAME", res.data.data.nickName);
                        storage.saveSessionObject("loginUser", res.data.data);
                    }
                    resolve(res); // res会被传递到then中
                }).catch(err => {
                    console.log('err1', err);
                    resolve(err);
                })
            })
        },
        LOGOUT({ commit }) {
            return new Promise(function (resolve) {
                logout().then(res => {
                    commit("SAVE_USERNAME", '');
                    commit("SAVE_NICKNAME", '');
                    storage.remove("loginUser");
                    resolve(res);
                })
            })
        },
        RECOVERY_USER({ commit }) {
            let loginUser = storage.getSessionObject("loginUser");
            if (loginUser) {
                commit("SAVE_USERNAME", loginUser.userName);
                commit("SAVE_NICKNAME", loginUser.nickName);
            }
        },

    }
}

export default user
