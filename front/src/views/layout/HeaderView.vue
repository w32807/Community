<script setup lang="ts">
    import {ref} from "vue";
    import { RouterLink, RouterView } from "vue-router";
</script>

<template>
    <RouterLink to="/">Home</RouterLink>
    <RouterLink to="/write">글작성</RouterLink>
    <RouterLink to="/login">로그인</RouterLink>
</template>

<style>
</style>
