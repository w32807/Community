<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";

// ref란 vue에서 컴포넌트 또는 DOM에 접근하기 위해 사용하는 속성이다.
const email = ref("")
const password = ref("")

const login = function (){
    // 타겟 지정없이 v-model로 바로 접근 가능하다.
    console.log(email.value)
    console.log(password.value)
    axios.get('http://localhost:8080/api')
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
