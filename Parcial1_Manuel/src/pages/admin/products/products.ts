import "../home/admin.css";
import { requireAuth, logout } from "../../../utils/auth";
import { api } from "../../../api/api";
import type { Product } from "../../../types/product";
import type { Categoria } from "../../../types/categoria";

const logoutBtn = document.getElementById("logoutButton") as HTMLButtonElement;
const btnNuevo = document.getElementById("btnNuevoProducto") as HTMLButtonElement;
const formPanel = document.getElementById("formPanel") as HTMLDivElement;
const formTitle = document.getElementById("formTitle") as HTMLHeadingElement;
const productoForm = document.getElementById("productoForm") as HTMLFormElement;
const inputId = document.getElementById("inputId") as HTMLInputElement;
const inputNombre = document.getElementById("inputNombre") as HTMLInputElement;
const inputCategoria = document.getElementById("inputCategoria") as HTMLSelectElement;
const inputPrecio = document.getElementById("inputPrecio") as HTMLInputElement;
const inputStock = document.getElementById("inputStock") as HTMLInputElement;
const inputDescripcion = document.getElementById("inputDescripcion") as HTMLInputElement;
const inputImagen = document.getElementById("inputImagen") as HTMLInputElement;
const inputDisponible = document.getElementById("inputDisponible") as HTMLInputElement;
const formError = document.getElementById("formError") as HTMLParagraphElement;
const btnCancelar = document.getElementById("btnCancelar") as HTMLButtonElement;
const btnSubmit = document.getElementById("btnSubmit") as HTMLButtonElement;
const tableContainer = document.getElementById("tableContainer") as HTMLDivElement;

logoutBtn.addEventListener("click", logout);

let products: Product[] = [];
let categories: Categoria[] = [];

const showError = (msg: string) => {
  formError.textContent = msg;
  formError.style.display = "block";
};

const hideError = () => {
  formError.style.display = "none";
};

const populateCategorySelect = () => {
  inputCategoria.innerHTML = `<option value="">Seleccioná una categoría</option>`;
  categories.forEach((cat) => {
    const opt = document.createElement("option");
    opt.value = String(cat.id);
    opt.textContent = cat.nombre;
    inputCategoria.appendChild(opt);
  });
};

const openForm = (product?: Product) => {
  hideError();
  productoForm.reset();
  populateCategorySelect();
  if (product) {
    formTitle.textContent = "Editar producto";
    inputId.value = String(product.id);
    inputNombre.value = product.nombre;
    inputCategoria.value = String(product.categoriaId);
    inputPrecio.value = String(product.precio);
    inputStock.value = String(product.stock);
    inputDescripcion.value = product.descripcion;
    inputImagen.value = product.imagen ?? "";
    inputDisponible.checked = product.disponible;
    btnSubmit.textContent = "Actualizar";
  } else {
    formTitle.textContent = "Nuevo producto";
    inputId.value = "";
    inputDisponible.checked = true;
    btnSubmit.textContent = "Guardar";
  }
  formPanel.style.display = "block";
  inputNombre.focus();
};

const closeForm = () => {
  formPanel.style.display = "none";
  productoForm.reset();
  hideError();
};

const renderTable = () => {
  if (products.length === 0) {
    tableContainer.innerHTML = `<p class="list-empty">No hay productos cargados.</p>`;
    return;
  }

  let html = `
    <table class="products-table">
      <thead>
        <tr>
          <th>#</th>
          <th>Nombre</th>
          <th>Categoría</th>
          <th>Precio</th>
          <th>Stock</th>
          <th>Disponible</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
  `;

  products.forEach((p) => {
    html += `
      <tr>
        <td>${p.id}</td>
        <td class="product-name">${p.nombre}</td>
        <td><span class="category-badge">${p.categoriaNombre}</span></td>
        <td class="product-price">$${p.precio.toLocaleString("es-AR")}</td>
        <td>${p.stock}</td>
        <td><span class="disponible-badge ${p.disponible ? 'badge-activo' : 'badge-inactivo'}">${p.disponible ? "Activo" : "Inactivo"}</span></td>
        <td class="product-actions">
          <button class="btn-edit" data-id="${p.id}">Editar</button>
          <button class="btn-delete" data-id="${p.id}">Eliminar</button>
        </td>
      </tr>
    `;
  });

  html += `</tbody></table>`;
  tableContainer.innerHTML = `<div class="table-wrapper">${html}</div>`;

  tableContainer.querySelectorAll<HTMLButtonElement>(".btn-edit").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number(btn.dataset.id);
      const product = products.find((p) => p.id === id);
      if (product) openForm(product);
    });
  });

  tableContainer.querySelectorAll<HTMLButtonElement>(".btn-delete").forEach((btn) => {
    btn.addEventListener("click", async () => {
      const id = Number(btn.dataset.id);
      const product = products.find((p) => p.id === id);
      if (!product) return;
      if (!confirm(`¿Eliminar el producto "${product.nombre}"?`)) return;
      try {
        await api.delete(`/products/${id}`);
        await loadProducts();
      } catch (err) {
        alert(`No se pudo eliminar: ${(err as Error).message}`);
      }
    });
  });
};

const loadProducts = async () => {
  tableContainer.innerHTML = `<p style="text-align:center;color:#aaa;padding:32px;">Cargando…</p>`;
  try {
    products = await api.get<Product[]>("/products");
    renderTable();
  } catch {
    tableContainer.innerHTML = `<p style="text-align:center;color:#c0392b;padding:32px;">No se pudieron cargar los productos.</p>`;
  }
};

productoForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  hideError();
  const nombre = inputNombre.value.trim();
  const descripcion = inputDescripcion.value.trim();
  const categoriaId = Number(inputCategoria.value);
  const precio = Number(inputPrecio.value);
  const stock = Number(inputStock.value);
  const imagen = inputImagen.value.trim();
  const disponible = inputDisponible.checked;

  if (!nombre || !descripcion || !categoriaId || precio <= 0 || stock < 0) {
    showError("Completá todos los campos obligatorios correctamente.");
    return;
  }

  btnSubmit.disabled = true;
  const id = inputId.value ? Number(inputId.value) : null;
  const body = { nombre, descripcion, categoriaId, precio, stock, imagen, disponible };

  try {
    if (id) {
      await api.put<Product>(`/products/${id}`, body);
    } else {
      await api.post<Product>("/products", body);
    }
    closeForm();
    await loadProducts();
  } catch (err) {
    showError((err as Error).message);
  } finally {
    btnSubmit.disabled = false;
  }
});

btnNuevo.addEventListener("click", () => openForm());
btnCancelar.addEventListener("click", closeForm);

const init = async () => {
  const user = requireAuth("ADMIN");
  if (!user) return;
  [categories, products] = await Promise.all([
    api.get<Categoria[]>("/categories"),
    api.get<Product[]>("/products"),
  ]);
  renderTable();
};

init();
