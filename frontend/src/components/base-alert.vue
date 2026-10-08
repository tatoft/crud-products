<script setup>
defineProps({ message: { type: String, default: "" } });
</script>

<template>
  <p v-if="message" class="mb-4 rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">
    {{ message }}
  </p>
</template>
