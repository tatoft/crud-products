import { request } from "./http";

export const getProducts = () => request("/products");

export const getProductById = (id) => request(`/products/${id}`);

export const createProduct = (product) =>
  request("/products", { method: "POST", body: JSON.stringify(product) });

export const updateProduct = (id, product) =>
  request(`/products/${id}`, { method: "PUT", body: JSON.stringify(product) });

export const deleteProduct = (id) =>
  request(`/products/${id}`, { method: "DELETE" });
