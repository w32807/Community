<script setup lang="ts">
    import {ref} from "vue";
</script>

<template>
    <el-scrollbar>
        <el-menu>
            <el-menu-item-group>
                <el-menu-item>
                    <!--
                        RouterLink를 쓰지않고 프로그래밍 방법으로 라우터를 사용한다.
                        https://v3.router.vuejs.org/kr/guide/essentials/navigation.html
                    -->
                    <el-link :underline="false" @click="$router.push({ path: '/' })">Home</el-link>
                </el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group>
                <el-menu-item>
                    <el-link :underline="false" @click="$router.push({ path: '/write' })">글작성</el-link>
                </el-menu-item>
            </el-menu-item-group>
            <el-menu-item-group>
                <el-menu-item>
                    <el-link :underline="false" @click="$router.push({ path: '/login' })">로그인</el-link>
                </el-menu-item>
            </el-menu-item-group>
        </el-menu>
    </el-scrollbar>
</template>

<style>
.layout-container .el-menu {
  border-right: none;
}
.el-link {
  margin-right: 8px;
}
.el-link .el-icon--right.el-icon {
  vertical-align: text-bottom;
}
</style>
