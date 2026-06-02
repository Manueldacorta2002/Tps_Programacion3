import "../home/admin.css";
import { requireAuth, logout } from "../../../utils/auth";
import { api } from "../../../api/api";
import type { Categoria } from "../../../types/categoria";

const logoutBtn = document.getElementById("logoutButton") as HTMLButtonElement;
const btnNueva = document.getElementById("btnNuevaCategoria") as HTMLButtonElement;
const formPanel = document.getElementById("formPanel") as HTMLDivElement;
const formTitle = document.getElementById("formTitle") as HTMLHeadingElement;
const categoriaForm = document.getElementById("categoriaForm") as HTMLFormElement;
const inputId = document.getElementById("inputId") as HTMLInputElement;
const inputNombre = document.getElementById("inputNombre") as HTMLInputElement;
const inputDescripcion = document.getElementById("inputDescripcion") as HTMLInputElement;
const formError = document.getElementById("formError") as HTMLParagraphElement;
const btnCancelar = document.getElementById("btnCancelar") as HTMLButtonElement;
const btnSubmit = document.getElementById("btnSubmit") as HTMLButtonElement;
const tableContainer = document.getElementById("tableContainer") as HTMLDivElement;

logoutBtn.addEventListener("click", logout);

let categories: Categoria[] = [];

const showError = (msg: string) => {
  formError.textContent = msg;
  formError.style.display = "block";
};

const hideError = () => {
  formError.style.display = "none";
};

const openForm = (cat?: Categoria) => {
  hideError();
  categoriaForm.reset();
  if (cat) {
    formTitle.textContent = "Editar categoría";
    inputId.value = String(cat.id);
    inputNombre.value = cat.nombre;
    inputDescripcion.value = cat.descripcion;
    btnSubmit.textContent = "Actualizar";
  } else {
    formTitle.textContent = "Nueva categoría";
    inputId.value = "";
    btnSubmit.textContent = "Guardar";
  }
  formPanel.style.display = "block";
  inputNombre.focus();
};

const closeForm = () => {
  formPanel.style.display = "none";
  categoriaForm.reset();
  hideError();
};

const renderTable = () => {
  if (categories.length === 0) {
    tableContainer.innerHTML = `<p class="list-empty">No hay categorías cargadas.</p>`;
    return;
  }

  let html = `
    <table class="products-table">
      <thead>
        <tr>
          <th>#</th>
          <th>Nombre</th>
          <th>Descripción</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
  `;

  categories.forEach((cat) => {
    html += `
      <tr>
        <td>${cat.id}</td>
        <td class="product-name">${cat.nombre}</td>
        <td>${cat.descripcion}</td>
        <td class="product-actions">
          <button class="btn-edit" data-id="${cat.id}">Editar</button>
          <button class="btn-delete" data-id="${cat.id}">Eliminar</button>
        </td>
      </tr>
    `;
  });

  html += `</tbody></table>`;
  tableContainer.innerHTML = `<div class="table-wrapper">${html}</div>`;

  tableContainer.querySelectorAll<HTMLButtonElement>(".btn-edit").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number(btn.dataset.id);
      const cat = categories.find((c) => c.id === id);
      if (cat) openForm(cat);
    });
  });

  tableContainer.querySelectorAll<HTMLButtonElement>(".btn-delete").forEach((btn) => {
    btn.addEventListener("click", async () => {
      const id = Number(btn.dataset.id);
      const cat = categories.find((c) => c.id === id);
      if (!cat) return;
      if (!confirm(`¿Eliminar la categoría "${cat.nombre}"?`)) return;
      try {
        await api.delete(`/categories/${id}`);
        await loadCategories();
      } catch (err) {
        alert(`No se pudo eliminar: ${(err as Error).message}`);
      }
    });
  });
};

const loadCategories = async () => {
  tableContainer.innerHTML = `<p style="text-align:center;color:#aaa;padding:32px;">Cargando…</p>`;
  categories = await api.get<Categoria[]>("/categories");
  renderTable();
};

categoriaForm.addEventListener("submit", async (e) => {
  e.preventDefault();
  hideError();
  const nombre = inputNombre.value.trim();
  const descripcion = inputDescripcion.value.trim();
  if (!nombre || !descripcion) {
    showError("Todos los campos son obligatorios.");
    return;
  }

  btnSubmit.disabled = true;
  const id = inputId.value ? Number(inputId.value) : null;

  try {
    if (id) {
      await api.put<Categoria>(`/categories/${id}`, { nombre, descripcion });
    } else {
      await api.post<Categoria>("/categories", { nombre, descripcion });
    }
    closeForm();
    await loadCategories();
  } catch (err) {
    showError((err as Error).message);
  } finally {
    btnSubmit.disabled = false;
  }
});

btnNueva.addEventListener("click", () => openForm());
btnCancelar.addEventListener("click", closeForm);

const init = async () => {
  const user = requireAuth("ADMIN");
  if (!user) return;
  await loadCategories();
};

init();
