import axios from "axios";

const instance = axios.create({
    baseURL: '/api',
    timeout: 1000
})

// 요청 인터셉터 추가하기
instance.interceptors.request.use(
    function (config) {
        // 요청이 전달되기 전에 작업 수행
        let accessToken: string | null = null;
        accessToken = localStorage.getItem("accessToken");

        if(accessToken != null) {
            config.headers['Authorization'] = 'Bearer ' + accessToken;
        }
        return config;
    },
    function (error) {
        // 요청 오류가 있는 작업 수행
        return Promise.reject(error);
    }
);

export default instance;