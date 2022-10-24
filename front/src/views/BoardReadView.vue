<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "../config/axios-config";
// useRoute랑 useRouter랑 다르다.
import {useRoute, useRouter} from "vue-router";

const title = ref("");
const content = ref("");
const route = useRoute();
const router = useRouter();

readBoard(route.query.boardId as string)

function readBoard (boardId:string){
    console.log(boardId);
}

</script>

<template>
    <div>
        <el-input class="mt-2" v-model="title" placeholder="제목을 입력 해 주세요"/>
        <el-input class="mt-2" v-model="content" type="textarea" rows="15"/>
        <el-button class="mt-2" type="primary" @click="write()">작성완료</el-button>
    </div>
</template>

<style>
</style>
