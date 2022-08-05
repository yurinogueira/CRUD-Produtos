import { createRouter, createWebHistory } from "vue-router";
import EnterView from "../views/EnterView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: EnterView,
    },
    {
      path: "/cadastro",
      name: "cadastro",
      component: () => import("../views/RegisterView.vue"),
    },
    {
      path: "/inicio",
      name: "inicio",
      component: () => import("../views/HomeView.vue"),
    },
  ],
});

export default router;
