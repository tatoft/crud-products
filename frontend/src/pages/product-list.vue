<script setup>
import { computed, onMounted, ref } from "vue";
import BaseAlert from "../components/base-alert.vue";
import BaseModal from "../components/base-modal.vue";
import { getCategories } from "../services/category-service.js";
import {
  createProduct,
  deleteProduct,
  getProducts,
  updateProduct,
} from "../services/product-service.js";

const products = ref([]);
const categories = ref([]);
const loading = ref(true);
const error = ref("");

// filtros
const search = ref("");
const selectedCategory = ref("");

// modal
const showModal = ref(false);
const editingId = ref(null);
const form = ref({ name: "", description: "", price: "", categoryId: "" });
const formError = ref("");

async function load() {
  loading.value = true;
  error.value = "";
  try {
    products.value = await getProducts();
    categories.value = await getCategories();
  } catch (e) {
    error.value = e.message;
  } finally {
    loading.value = false;
  }
}

function openModal(product) {
  editingId.value = product ? product.id : null;
  form.value = {
    name: product ? product.name : "",
    description: product ? (product.description ?? "") : "",
    price: product ? product.price : "",
    categoryId: product ? (product.category?.id ?? "") : "",
  };
  formError.value = "";
  showModal.value = true;
}

async function save() {
  const f = form.value;
  if (!f.name.trim() || f.price === "" || Number(f.price) < 0 || !f.categoryId) {
    formError.value = "Completa nombre, precio (0 o más) y categoría";
    return;
  }
  // El backend espera la categoría como objeto con id
  const data = {
    name: f.name.trim(),
    description: f.description.trim(),
    price: Number(f.price),
    category: { id: f.categoryId },
  };
  try {
    if (editingId.value) await updateProduct(editingId.value, data);
    else await createProduct(data);
    showModal.value = false;
    await load();
  } catch (e) {
    formError.value = e.message;
  }
}

async function remove(product) {
  if (!confirm(`¿Eliminar "${product.name}"?`)) return;
  try {
    await deleteProduct(product.id);
    await load();
  } catch (e) {
    error.value = e.message;
  }
}

const filtered = computed(() =>
  products.value.filter((p) => {
    const matchesName = p.name.toLowerCase().includes(search.value.toLowerCase());
    const matchesCategory = !selectedCategory.value || p.category?.id === selectedCategory.value;
    return matchesName && matchesCategory;
  }),
);

onMounted(load);
</script>

<template>
  <div class="mb-6 flex items-center justify-between">
    <h1 class="text-xl font-semibold">Productos</h1>
    <button class="btn btn-primary" @click="openModal()">Nuevo producto</button>
  </div>

  <BaseAlert :message="error" />

  <div class="mb-4 flex flex-col gap-2 sm:flex-row">
    <input v-model="search" placeholder="Buscar por nombre" class="input sm:flex-1" />
    <select v-model="selectedCategory" class="input sm:w-56">
      <option value="">Todas las categorías</option>
      <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
    </select>
  </div>

  <p v-if="loading" class="text-slate-500">Cargando...</p>
  <p v-else-if="filtered.length === 0 && !error" class="text-slate-500">No hay productos.</p>

  <div v-else-if="filtered.length" class="overflow-x-auto">
    <table class="w-full bg-white text-left text-sm">
      <thead class="border-b border-slate-200 text-slate-500">
        <tr>
          <th class="px-3 py-2 font-medium">Nombre</th>
          <th class="px-3 py-2 font-medium">Categoría</th>
          <th class="px-3 py-2 font-medium">Descripción</th>
          <th class="px-3 py-2 text-right font-medium">Precio</th>
          <th class="px-3 py-2 text-right font-medium">Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="p in filtered" :key="p.id" class="border-b border-slate-100">
          <td class="px-3 py-2">{{ p.name }}</td>
          <td class="px-3 py-2">{{ p.category?.name ?? "-" }}</td>
          <td class="px-3 py-2 text-slate-500">{{ p.description }}</td>
          <td class="px-3 py-2 text-right">{{ Number(p.price).toFixed(2) }}</td>
          <td class="space-x-3 px-3 py-2 text-right whitespace-nowrap">
            <button class="link" @click="openModal(p)">Editar</button>
            <button class="link-danger" @click="remove(p)">Eliminar</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <BaseModal v-if="showModal" :title="editingId ? 'Editar producto' : 'Nuevo producto'" @close="showModal = false">
    <BaseAlert :message="formError" />
    <form class="space-y-4" @submit.prevent="save">
      <div>
        <label class="label">Nombre</label>
        <input v-model="form.name" class="input" />
      </div>
      <div>
        <label class="label">Descripción</label>
        <textarea v-model="form.description" rows="2" class="input" />
      </div>
      <div>
        <label class="label">Precio</label>
        <input v-model="form.price" type="number" step="0.01" min="0" class="input" />
      </div>
      <div>
        <label class="label">Categoría</label>
        <select v-model="form.categoryId" class="input">
          <option value="" disabled>Selecciona...</option>
          <option v-for="c in categories" :key="c.id" :value="c.id">{{ c.name }}</option>
        </select>
      </div>
      <div class="flex justify-end gap-2">
        <button type="button" class="btn" @click="showModal = false">Cancelar</button>
        <button type="submit" class="btn btn-primary">Guardar</button>
      </div>
    </form>
  </BaseModal>
</template>
