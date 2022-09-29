<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";

const title = ref("");
const content = ref("");

const = router = useRouter();

const write = function (){
    console.log(title.value)
    console.log(content.value)
    axios.get('http://google.com')
    .then(() => {
        // 글 작성 완료 후 화면 이동하는 방법
      router.replace({name: "home"});
    })

}

</script>

<template>
    <div>
        <el-input v-model="title" placeholder="제목을 입력 해 주세요"/>
    </div>
    <div class="mt-2">
        <el-input v-model="content" type="textarea" rows="15"/>
    </div>
    <div class="mt-2">
        <el-button type="primary" @click="write()">작성완료</el-button>
    </div>
</template>

<style>
</style>
