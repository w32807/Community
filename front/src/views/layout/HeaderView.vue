<script setup lang="ts">
    import {ref} from "vue";
    import { RouterLink, RouterView } from "vue-router";
</script>

<template>
    <el-row :gutter="20" align="middle">
        <el-col :span="16">로고</el-col>
        <el-col :span="8">
            <el-button type="primary" @click="$router.push({ path: '/login' })">로그인</el-button>
        </el-col>
    </el-row>
</template>

<style>
.el-row {
  margin-bottom: 20px;
  height: inherit;
}
</style>
