<script setup lang="ts">
import {ref} from "vue";
import { RouterLink, RouterView } from "vue-router";
import {useRouter} from "vue-router";
import store from "../../store";

const router = useRouter();
const logout = function(){
    store.commit('logout');
    router.push({name: "home"});
}
</script>

<template>
    <el-row :gutter="20" align="middle">
        <el-col :span="16">
            <el-link :underline="false" @click="$ .push({ path: '/' })">로고</el-link>
        </el-col>
        <el-col :span="8">
            <el-descriptions v-if="store.getters.isAuthenticated">
                <el-descriptions-item width="100%">
                    {{store.getters.getMember.name}}님 안녕하세요
                    <el-button type="danger" @click="logout">로그아웃</el-button>
                </el-descriptions-item>
            </el-descriptions>
            <el-button v-else type="primary" @click="$router.push({ path: '/login' })">로그인</el-button>
        </el-col>
    </el-row>
</template>

<style>
.el-row {
  margin-bottom: 20px;
  height: inherit;
}
</style>
