<script setup lang="ts">
// https://element-plus.org/en-US/component/button.html 에서 디자인 가능
import { RouterLink, RouterView } from "vue-router";
import HeaderView from "./views/layout/HeaderView.vue";
import FooterView from "./views/layout/FooterView.vue";

// 다른 vue 파일을 import 하는 법
// import HeaderView from "./views/layout/HeaderView.vue"; 로 import
// 1. <script setup>를 쓸 때는 그냥 template 태그 아래에서 활용
// 2. export component로 컴포넌트 등록 후 template 태그 아래에서 활용하기

</script>

<template>
    <div class="common-layout">
        <el-container>
            <el-aside width="200px">Aside</el-aside>
            <el-container>
                <el-header>
                    <HeaderView/>
                </el-header>
                <el-main>
                    <RouterView/> <!-- 라우터를 통해 가져온 View.vue의 내용을 보여줄 영역 -->
                </el-main>
                <el-footer>
                    <FooterView/>
                </el-footer>
            </el-container>
        </el-container>
    </div>
</template>

<style scoped>
header {
  line-height: 1.5;
  max-height: 100vh;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  margin-top: 2rem;
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }

  nav {
    text-align: left;
    margin-left: -1rem;
    font-size: 1rem;

    padding: 1rem 0;
    margin-top: 1rem;
  }
}
</style>
