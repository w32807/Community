<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";

// ref로 감싸서 반응형 데이터로 취급한다.
const boards = ref([]);


/*
이렇게 boards를 넣어준다.
axios.get(url).then((response)) => {
    response.data.forEach((r:any) => {
        boards.push(r);
    });
});
*/
</script>

<template>
    <ul>
        <li v-for="board in boards" :key="board.id">
            <div>
                {{board.title}}
            </div>
            <div>
                {{board.content}}
            </div>
        </li>
    </ul>
</template>

<style>
li{
    margin-bottom: 1rem;
}
</style>
