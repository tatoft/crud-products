import { createRouter, createWebHistory } from "vue-router";
import ProductList from "../pages/product-list.vue";
import CategoryList from "../pages/category-list.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: "/", component: ProductList },
    { path: "/categories", component: CategoryList },
    { path: "/:pathMatch(.*)*", redirect: "/" },
  ],
});

export default router;
