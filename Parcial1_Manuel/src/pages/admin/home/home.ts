import "./admin.css";
import { checkAuhtUser, logout } from "../../../utils/auth";
import { PRODUCTS, getCategories } from "../../../data/data";
import type { Product } from "../../../types/product";

const buttonLogout = document.getElementById(
  "logoutButton"
) as HTMLButtonElement;
buttonLogout?.addEventListener("click", () => {
  logout();
});

// Lista simulada (copia local, no afecta el catálogo)
let localProducts: Product[] = [...PRODUCTS];

const productList = document.getElementById("productList") as HTMLDivElement;
const btnVer = document.getElementById("btnVerProductos") as HTMLButtonElement;
const btnAgregar = document.getElementById("btnAgregarProducto") as HTMLButtonElement;
const formPanel = document.getElementById("formAgregarProducto") as HTMLDivElement;

btnAgregar?.addEventListener("click", () => {
  const isVisible = formPanel.style.display !== "none";
  formPanel.style.display = isVisible ? "none" : "block";
});

const renderProductList = () => {
  if (localProducts.length === 0) {
    productList.innerHTML = `<p class="list-empty">No quedan productos en la lista</p>`;
    return;
  }

  let html = `
    <h3 class="list-title">📋 Productos cargados</h3>
    <table class="products-table">
      <thead>
        <tr>
          <th>Producto</th>
          <th>Categoría</th>
          <th>Precio</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
  `;

  localProducts.forEach((p) => {
    html += `
      <tr data-id="${p.id}">
        <td class="product-name">${p.name}</td>
        <td><span class="category-badge">${p.category.toUpperCase()}</span></td>
        <td class="product-price">$${p.price.toLocaleString("es-AR")}</td>
        <td class="product-actions">
          <button class="btn-edit" data-id="${p.id}">Editar</button>
          <button class="btn-delete" data-id="${p.id}">Eliminar</button>
        </td>
      </tr>
    `;
  });

  html += `</tbody></table>`;
  productList.innerHTML = html;

  // Editar (no funcional, solo feedback)
  productList.querySelectorAll(".btn-edit").forEach((btn) => {
    btn.addEventListener("click", () => {
      alert("Función de edición no disponible (simulado)");
    });
  });

  // Eliminar (simulado, solo visual)
  productList.querySelectorAll(".btn-delete").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number((btn as HTMLButtonElement).dataset.id);
      const product = localProducts.find((p) => p.id === id);
      if (product && confirm(`¿Seguro que querés eliminar "${product.name}"? Esta acción no se puede deshacer.`)) {
        localProducts = localProducts.filter((p) => p.id !== id);
        renderProductList();
      }
    });
  });
};

btnVer.addEventListener("click", () => {
  if (productList.style.display === "none") {
    productList.style.display = "block";
    btnVer.textContent = "Ocultar productos";
    renderProductList();
  } else {
    productList.style.display = "none";
    btnVer.textContent = "Ver productos";
  }
});

// Renderizar chips de categorías
const categoryChips = document.getElementById("categoryChips") as HTMLDivElement;
const categories = getCategories();
const categoryEmojis: Record<string, string> = {
  Hamburguesas: "🍔",
  Pizzas: "🍕",
  Bebidas: "🥤",
  Acompañamientos: "🍟",
  Combos: "🍱",
};

categories.forEach((cat) => {
  const chip = document.createElement("span");
  chip.className = "category-chip";
  const emoji = categoryEmojis[cat] || "";
  chip.textContent = `${emoji} ${cat}`;
  categoryChips.appendChild(chip);
});

// ====== FORMULARIO AGREGAR PRODUCTO ======
const nuevoProductoForm = document.getElementById("nuevoProductoForm") as HTMLFormElement;
const inputNombre = document.getElementById("inputNombre") as HTMLInputElement;
const inputCategoria = document.getElementById("inputCategoria") as HTMLSelectElement;
const inputPrecio = document.getElementById("inputPrecio") as HTMLInputElement;
const btnCancelar = document.getElementById("btnCancelarAgregar") as HTMLButtonElement;
const statProductos = document.getElementById("statProductos") as HTMLSpanElement;
const badgeProductos = document.getElementById("badgeProductos") as HTMLSpanElement;

// Poblar el select de categorías
categories.forEach((cat) => {
  const opt = document.createElement("option");
  opt.value = cat;
  opt.textContent = cat;
  inputCategoria.appendChild(opt);
});

btnCancelar.addEventListener("click", () => {
  formPanel.style.display = "none";
  nuevoProductoForm.reset();
});

nuevoProductoForm.addEventListener("submit", (e) => {
  e.preventDefault();
  const nombre = inputNombre.value.trim();
  const categoria = inputCategoria.value;
  const precio = Number(inputPrecio.value);
  if (!nombre || !categoria || precio <= 0) return;

  const nuevoId =
    localProducts.length > 0
      ? Math.max(...localProducts.map((p) => p.id)) + 1
      : 1;

  localProducts.push({
    id: nuevoId,
    name: nombre,
    category: categoria,
    price: precio,
    image: "",
    description: "",
  });

  statProductos.textContent = String(localProducts.length);
  badgeProductos.textContent = `${localProducts.length} cargados`;

  if (productList.style.display !== "none") renderProductList();

  formPanel.style.display = "none";
  nuevoProductoForm.reset();
});

const initPage = () => {
  checkAuhtUser(
    "/src/pages/auth/login/login.html",
    "/src/pages/client/home/home.html",
    "admin"
  );
};
initPage();
