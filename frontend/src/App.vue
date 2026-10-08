<script setup>
import AppNavbar from "./components/app-navbar.vue";
import AppFooter from "./components/app-footer.vue";
</script>

<template>
  <div class="flex min-h-screen flex-col bg-slate-50 text-slate-800">
    <AppNavbar />
    <main class="mx-auto w-full max-w-4xl flex-1 px-4 py-8">
      <RouterView />
    </main>
    <AppFooter />
  </div>
</template>
