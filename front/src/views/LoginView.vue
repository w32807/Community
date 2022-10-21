<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import store from "../store";
import axios from "../config/axios-config";
import Member from "../store/models/member";

// ref란 vue에서 컴포넌트 또는 DOM에 접근하기 위해 사용하는 속성이다.
const email = ref("");
const password = ref("");
const router = useRouter();

const login = function (){
    axios.post('/login',{
        //email: email.value,
        //password: password.value
        email: 'admin1@google.com',
        password: '1234'
    })
    .then(function(response){
        localStorage.setItem('accessToken', response.data.token.accessToken);
        localStorage.setItem('refreshToken', response.data.token.refreshToken);

        // store에 회원정보 저장하기
        store.commit('loginSuccess', new Member(response.data.email, response.data.nickName));

        // vue-router에서 useRouter를 import 후 화면이동하기
        router.push({name: "home"});
    })
    .catch(function(error){
    });
}

</script>

<template>
    <div class="login">
        <!-- v-model란 자동으로 vue의 데이터 속성에 연결된다., -->
        <el-input class="mt-2" v-model="email" type="email" placeholder="이메일을 입력 해 주세요"/>
        <el-input class="mt-2" v-model="password" type="password" placeholder="비밀번호를 입력 해 주세요"/>
        <el-button class="mt-2" type="primary" @click="login()">로그인</el-button>
    </div>
</template>

<style>
</style>
