<script setup>
import { onMounted, ref } from "vue";
import BaseAlert from "../components/base-alert.vue";
import BaseModal from "../components/base-modal.vue";
import {
  createCategory,
  deleteCategory,
  getCategories,
  updateCategory,
} from "../services/category-service.js";

const categories = ref([]);
const loading = ref(true);
const error = ref("");

const showModal = ref(false);
const editingId = ref(null);
const name = ref("");
const formError = ref("");

async function load() {
  loading.value = true;
  error.value = "";
  try {
    categories.value = await getCategories();
  } catch (e) {
    error.value = e.message;
  } finally {
    loading.value = false;
  }
}

function openModal(category) {
  editingId.value = category ? category.id : null;
  name.value = category ? category.name : "";
  formError.value = "";
  showModal.value = true;
}

async function save() {
  if (!name.value.trim()) {
    formError.value = "El nombre es obligatorio";
    return;
  }
  try {
    const data = { name: name.value.trim() };
    if (editingId.value) await updateCategory(editingId.value, data);
    else await createCategory(data);
    showModal.value = false;
    await load();
  } catch (e) {
    formError.value = e.message;
  }
}

async function remove(category) {
  if (!confirm(`¿Eliminar "${category.name}"?`)) return;
  try {
    await deleteCategory(category.id);
    await load();
  } catch (e) {
    error.value = e.message;
  }
}

onMounted(load);
</script>

<template>
  <div class="mb-6 flex items-center justify-between">
    <h1 class="text-xl font-semibold">Categorías</h1>
    <button class="btn btn-primary" @click="openModal()">Nueva categoría</button>
  </div>

  <BaseAlert :message="error" />

  <p v-if="loading" class="text-slate-500">Cargando...</p>
  <p v-else-if="categories.length === 0 && !error" class="text-slate-500">No hay categorías.</p>

  <table v-else-if="categories.length" class="w-full bg-white text-left text-sm">
    <thead class="border-b border-slate-200 text-slate-500">
      <tr>
        <th class="px-3 py-2 font-medium">ID</th>
        <th class="px-3 py-2 font-medium">Nombre</th>
        <th class="px-3 py-2 text-right font-medium">Acciones</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="c in categories" :key="c.id" class="border-b border-slate-100">
        <td class="px-3 py-2">{{ c.id }}</td>
        <td class="px-3 py-2">{{ c.name }}</td>
        <td class="space-x-3 px-3 py-2 text-right">
          <button class="link" @click="openModal(c)">Editar</button>
          <button class="link-danger" @click="remove(c)">Eliminar</button>
        </td>
      </tr>
    </tbody>
  </table>

  <BaseModal v-if="showModal" :title="editingId ? 'Editar categoría' : 'Nueva categoría'" @close="showModal = false">
    <BaseAlert :message="formError" />
    <form class="space-y-4" @submit.prevent="save">
      <div>
        <label class="label">Nombre</label>
        <input v-model="name" class="input" />
      </div>
      <div class="flex justify-end gap-2">
        <button type="button" class="btn" @click="showModal = false">Cancelar</button>
        <button type="submit" class="btn btn-primary">Guardar</button>
      </div>
    </form>
  </BaseModal>
</template>
