<script setup lang="ts">
import {ref} from "vue";
import axios from "../config/axios-config";

// ref로 감싸서 반응형 데이터로 취급한다.
let boards = ref([]);

// 제이쿼리에서는 document.ready로 페이지가 호출될 때 로직을 수행하지만
// 여기서는 그냥 <script> 태그 안에 작성하면 된다.
axios.get('/board/boards')
.then((response) => {
    boards.value = response.data.list;
})
.catch(error => {
    console.log(error)
});


</script>
<!--
    [[element-plus에서 속성, 이벤트, 메소드 적용하기]]
    1. 반응형 데이터가 들어갈 때는 앞에 : 붙여주기. ex) :data="boards"
    2. 반응형 데이터가 안 들어갈 때는 그냥 속성명 쓰기. ex) style="width: 100px;"
    3. boolean일 때는 속성을 명시하면 true 됨 ex) border -> 테투리 넣어라
    4. 이벤트에는 앞에 @붙여야함
-->
<template>
    <el-table :data="boards" border style="width: 100%">
        <el-table-column prop="id" label="글번호" width="100" header-align="center" align="center"/>
        <el-table-column prop="title" label="제목" header-align="center"/>
    </el-table>
</template>

<style>
</style>
