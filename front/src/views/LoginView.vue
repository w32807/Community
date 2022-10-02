<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import axios from "../config/axiosConfig.ts";

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
        localStorage.setItem('accessToken', response.data.accessToken);
        localStorage.setItem('refreshToken', response.data.refreshToken);
        // vue-router에서 useRouter를 import 후 화면이동하기.
        router.push({name: "home"});
    })
    .catch(function(error){
    });


/*
    // 타겟 지정없이 v-model로 바로 접근 가능하다.
    console.log(email.value);
    console.log(password.value);
    axios.post('/login',{
        //email: email.value,
        //password: password.value
        email: 'admin1@google.com',
        password: '1234'
    })
    .then(function(response){
        console.log("정상응답");
        console.log(response);
        console.log(response.data.accessToken);
        console.log(response.data.refreshToken);
        console.log(localStorage);
        localStorage.setItem('accessToken', response.data.accessToken);
        localStorage.setItem('refreshToken', response.data.refreshToken);

        Axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.accessToken}`
    })
    .catch(function(error){
       console.log("로그인 실패");
       console.log(error);
    });*/
}

</script>

<template>
    <div class="login">
        <!-- v-model란 자동으로 vue의 데이터 속성에 연결된다., -->
        <el-input v-model="email" type="email" placeholder="이메일을 입력 해 주세요"/>
    </div>
    <div class="mt-2">
        <el-input v-model="password" type="password" placeholder="비밀번호를 입력 해 주세요"/>
    </div>
    <div class="mt-2">
        <el-button type="primary" @click="login()">로그인</el-button>
    </div>
</template>

<style>
</style>
