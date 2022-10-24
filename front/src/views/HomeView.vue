<script setup lang="ts">
import {ref} from "vue";
import {Search} from '@element-plus/icons-vue'
import axios from "../config/axios-config";

// ref로 감싸서 반응형 데이터로 취급한다.
const boards = ref([] as any);
const pageNumber = ref(1);
const pageSize = ref(0);
const totalSize = ref(0);
const keyword = ref("");
const searchType = ref("T");

const handleCurrentChange = (val: number) => {
    getBoards();
}

getBoards();

function getBoards(){
    axios.get('/board/boards',{
        params: {
            page: pageNumber.value,
            keyword: keyword.value,
            searchType: searchType.value
        },
    })
    .then((response) => {
        boards.value = response.data.list;
        pageNumber.value = response.data.pageInfo.pageNumber + 1;
        pageSize.value = response.data.pageInfo.pageSize;
        totalSize.value = response.data.pageInfo.totalSize;
    })
    .catch(error => {
        console.log(error)
    });
}

function getBoardsWithCondition(){
    pageNumber.value = 1;
    getBoards();
}

</script>
<!--
    [[element-plus에서 속성, 이벤트, 메소드 적용하기]]
    1. 반응형 데이터가 들어갈 때는 앞에 : 붙여주기. ex) :data="boards"
    2. 반응형 데이터가 안 들어갈 때는 그냥 속성명 쓰기. ex) style="width: 100px;"
    3. boolean일 때는 속성을 명시하면 true 됨 ex) border -> 테투리 넣어라
    4. 이벤트에는 앞에 @붙여야함
-->
<template>
    <div>
        <el-row>
            <el-col :span="14"><div class="grid-content ep-bg-purple" /></el-col>
            <el-col :span="10">
                <el-input
                    v-model="keyword"
                    placeholder="검색어를 입력해 주세요."
                    class="input-with-select">
                    <template #prepend>
                        <el-select v-model="searchType" placeholder="구분" style="width: 210px" :default-first-option="true">
                            <el-option label="글제목" value="T"/>
                            <el-option label="글내용" value="C"/>
                            <el-option label="작성자" value="W"/>
                            <el-option label="글제목 + 글내용" value="TC"/>
                            <el-option label="글제목 + 작성자" value="TW"/>
                            <el-option label="글내용 + 작성자" value="CW"/>
                            <el-option label="글제목 + 글내용 + 작성자" value="TWC"/>
                        </el-select>
                    </template>
                    <template #append>
                        <el-button :icon="Search" @click="getBoardsWithCondition()"/>
                    </template>
                </el-input>
            </el-col>
        </el-row>
        <el-table class="mt-2" :data="boards" border style="width: 100%">
            <el-table-column prop="id" label="글번호" header-align="center" align="center" width="100"/>
            <el-table-column prop="title" label="제목" header-align="center"/>
            <el-table-column prop="regDate" label="등록일" header-align="center" align="center" width="200"/>
            <el-table-column prop="member.nickname" label="작성자" header-align="center" align="center" width="200"/>
            <el-table-column prop="views" label="조회수" header-align="center" align="center" width="200"/>
        </el-table>
        <el-pagination class="mt-2"
            layout="prev, pager, next"
            v-model:currentPage="pageNumber"
            :page-size="pageSize" 
            :total="totalSize"
            :background="true"
            :pager-count="11"
            @current-change="handleCurrentChange"/>
    </div>
</template>

<style>
</style>
