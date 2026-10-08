<template>
  <header class="border-b border-slate-200 bg-white">
    <nav class="mx-auto flex max-w-4xl items-center justify-between px-4 py-3">
      <RouterLink to="/" class="font-semibold text-slate-900 py-3">Inventario</RouterLink>
      <div class="space-x-5 text-sm">
        <RouterLink to="/" class="text-slate-600 hover:text-slate-900">Productos</RouterLink>
        <RouterLink to="/categories" class="text-slate-600 hover:text-slate-900">Categorías</RouterLink>
      </div>
    </nav>
  </header>
</template>
