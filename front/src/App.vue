<script setup lang="ts">
// https://element-plus.org/en-US/component/button.html 에서 디자인 가능
import { RouterLink, RouterView } from "vue-router";
import AsideView from "./views/layout/AsideView.vue";
import HeaderView from "./views/layout/HeaderView.vue";

// 다른 vue 파일을 import 하는 법
// import HeaderView from "./views/layout/HeaderView.vue"; 로 import
// 1. <script setup>를 쓸 때는 그냥 template 태그 아래에서 활용
// 2. export component로 컴포넌트 등록 후 template 태그 아래에서 활용하기

</script>

<template>
    <el-container class="layout-container">
        <el-aside width="200px">
            <AsideView/>
        </el-aside>
        <el-container>
            <el-header>
                <HeaderView/>
            </el-header>
            <el-main>
                <RouterView/> <!-- 라우터를 통해 가져온 View.vue의 내용을 보여줄 영역 -->
            </el-main>
        </el-container>
    </el-container>
</template>

<style>
body {
    margin: 0;
}
#app .layout-container {
    width : 100vw;
    height : 100vh;
}
</style>
