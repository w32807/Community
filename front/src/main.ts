import { createApp } from "vue";
import { createPinia } from "pinia";
import axios from "./config/axios-config";

import App from "./App.vue";
import router from "./router";

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import "bootstrap/dist/css/bootstrap-utilities.css";

const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(ElementPlus)

// 내가 만든 axios를 사용하도록 등록해쥰다,
app.config.globalProperties.axios = axios;

app.mount("#app");
