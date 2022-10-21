import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import WriteView from "../views/WriteView.vue";
import LoginView from "../views/LoginView.vue";
import store from "../store";
import { ElStep } from "element-plus";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView
    },
    {
      path: "/write",
      name: "write",
      component: WriteView,
      meta: { authorization: true }
    },
    {
      path: "/login",
      name: "login",
      component: LoginView
    }
  ],
});

router.beforeEach(function (to, from, next) {
  // 타입스크립트에서는 as로 타입캐스팅을 한다.
  const isRequiredAuth: boolean = to.meta.authorization as boolean || false ;

  if(isRequiredAuth && !store.getters.isAuthenticated){
    next({name: "login"})
  }else{
    next() // 마치 서블릿 필터처럼 next를 꼭 선언해 주어야 한다.
  }
});

export default router
