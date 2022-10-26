<script setup lang="ts">
import {onMounted, ref} from "vue";
import axios from "../config/axios-config";
// useRoute랑 useRouter랑 다르다.
import {useRoute, useRouter} from "vue-router";

const route = useRoute();
const router = useRouter();
const id = ref(route.query.boardId);
const title = ref("");
const content = ref("");

readBoard()

function readBoard (){
    axios.get('/board/' + id.value)
    .then((response) => {
        id.value = response.data.data.id
        title.value = response.data.data.title
        content.value = response.data.data.content
    })
    .catch(error => {
        console.log(error)
    });
}

function updateBoard (){
    axios.put('/board/board',{
        id: id.value,
        title: title.value,
        content: content.value
    })
    .then(() => {
        readBoard()
    })
    .catch(error => {
        console.log(error)
    })
}

function deleteBoard (){
    axios.delete('/board/' + id.value)
    .then(() => {
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
        <el-button class="mt-2" type="primary" @click="updateBoard()">수정</el-button>
        <el-button class="mt-2" type="danger" @click="deleteBoard()">삭제</el-button>
    </div>
</template> 

<style>
</style>
