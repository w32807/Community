import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";
import vueJsx from "@vitejs/plugin-vue-jsx";

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue(), vueJsx()],
    resolve: {
        alias: {
            "@": fileURLToPath(new URL("./src", import.meta.url)),
        },
    },
    server: {
        proxy: {
            // vite.config.js에서 proxy를 두어 특정 url에 매핑할 수 있다.
            // 프록시를 두어 CORS를 해결한다.
            // https://vitejs.dev/config/server-options.html 참고
            "^/api/.*": {
                target: "http://localhost:8080",
                changeOrigin: true
            },
        },
    },
});
