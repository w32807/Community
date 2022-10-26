<script setup lang="ts">
import {ref} from "vue";
import axios from "../config/axios-config";
import {useRouter} from "vue-router";

const title = ref("");
const content = ref("");
const router = useRouter();

const writeBoard = function (){
    axios.post('/board/board',{
        title: title.value,
        content: content.value
    })
    .then(() => {
        // 글 작성 완료 후 화면 이동하는 방법
        router.push({name: "home"});
    })
    .catch(error => {
        console.log(error)
    })
}

</script>

<template>
    <div>
        <el-input class="mt-2" v-model="title" placeholder="제목을 입력 해 주세요"/>
        <el-input class="mt-2" v-model="content" type="textarea" rows="15"/>
        <el-button class="mt-2" type="primary" @click="writeBoard()">작성완료</el-button>
    </div>
</template>

<style>
</style>
