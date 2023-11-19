import VuexPersistence from 'vuex-persist';
import Vuex from "vuex";

const vuexLocal = new VuexPersistence({
   storage: window.localStorage
});

export const userStore = new Vuex.Store({
    state: {
            isAdmin: false,
            token: null
    },
    mutations: {
        setAdmin(state, isAdmin) {
            state.isAdmin = isAdmin;
        },

        setToken(state, token) {
            state.token = token;
        }
    },
    getters: {
        isAuthenticated: state => state.token !== null,
        getToken: state => state.token
    },
    plugins:[vuexLocal.plugin]
});