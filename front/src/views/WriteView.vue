<script setup lang="ts">
import {ref} from "vue";
import axios from "../config/axios-config";
import {useRouter} from "vue-router";

const title = ref("");
const content = ref("");
const router = useRouter();

const write = function (){
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
        <el-input v-model="title" placeholder="제목을 입력 해 주세요"/>
    </div>
    <div class="mt-2">
        <el-input v-model="content" type="textarea" rows="15"/>
    </div>
    <div class="mt-2">
        <el-button type="primary" @click="write()">작성완료</el-button>
    </div>
</template>

<style>
</style>
