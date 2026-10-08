<script setup>
defineProps({ title: String });
defineEmits(["close"]);
</script>

<template>
  <div class="fixed inset-0 z-10 flex items-center justify-center bg-black/40 p-4" @click.self="$emit('close')">
    <div class="w-full max-w-md rounded bg-white p-5 shadow">
      <h2 class="mb-4 text-lg font-semibold">{{ title }}</h2>
      <slot />
    </div>
  </div>
</template>
