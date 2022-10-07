import { createApp } from "vue";
import { createPinia } from "pinia";
import store from "./store";
import axios from "./config/axios-config";
import App from "./App.vue";
import router from "./router";
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import "bootstrap/dist/css/bootstrap-utilities.css";

const app = createApp(App);

const rootComponent = app.use(createPinia())
    .use(router)
    .use(ElementPlus)
    .use(store)
    .mount("#app");

// [앱 글로벌 변수 선언 실시]
app.config.globalProperties.axios = axios; // axios를 글로벌로 선언