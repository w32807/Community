import { createStore } from "vuex";
import Member from "./store/models/member";

export default createStore({
    state: {
        isAuthenticated: false,
        member: new Member("", "")
    },
    getters: {
        // 위의 state를 매개변수로 받는다.
        getMember(state) {
            return state.member;
        },
        isAuthenticated(state) {
            return state.isAuthenticated;
        }
    },
    mutations: {
        loginSuccess(state, member) {
            state.isAuthenticated = true;
            state.member = member;
        },
        logout(state) {
            state.isAuthenticated = false;
            state.member.email = "";
            state.member.name = "";
        },
    }
});