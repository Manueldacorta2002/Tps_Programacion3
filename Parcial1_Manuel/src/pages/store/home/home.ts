import "../store.css";
import "./improvements.css";
import { api } from "../../../api/api";
import { addToCart, getCartCount } from "../../../utils/cart";
import type { Product } from "../../../types/product";
import type { Categoria } from "../../../types/categoria";
import { requireAuth, logout } from "../../../utils/auth";
import pizzaUrl from "../../../assets/products/pizza.svg";
import burgerUrl from "../../../assets/products/burger.svg";
import drinkUrl from "../../../assets/products/drink.svg";
import dessertUrl from "../../../assets/products/dessert.svg";
import foodDefaultUrl from "../../../assets/products/default-food.svg";

// Elementos del DOM
const productsGrid = document.getElementById("productsGrid") as HTMLDivElement;
const searchInput = document.getElementById("searchInput") as HTMLInputElement;
const categoryFilters = document.getElementById("categoryFilters") as HTMLDivElement;
const noResults = document.getElementById("noResults") as HTMLDivElement;
const cartCount = document.getElementById("cartCount") as HTMLSpanElement;
const toast = document.getElementById("toast") as HTMLDivElement;
const logoutBtn = document.getElementById("logoutButton") as HTMLButtonElement;
const heroBadge = document.getElementById("heroBadge") as HTMLSpanElement;
const heroProductVisual = document.getElementById("heroProductVisual") as HTMLDivElement;
const heroProductCategory = document.getElementById("heroProductCategory") as HTMLParagraphElement;
const heroProductName = document.getElementById("heroProductName") as HTMLHeadingElement;
const heroProductDescription = document.getElementById("heroProductDescription") as HTMLParagraphElement;
const heroProductPrice = document.getElementById("heroProductPrice") as HTMLParagraphElement;

let ALL_PRODUCTS: Product[] = [];
let selectedCategory = localStorage.getItem("store_category") || "";

const getProductImage = (nombre: string, categoriaNombre: string): string => {
  const n = nombre.toLowerCase();
  const c = categoriaNombre.toLowerCase();
  if (c.includes("pizza") || n.includes("pizza")) return pizzaUrl;
  if (c.includes("hamburguesa") || n.includes("burger") || n.includes("hamburguesa")) return burgerUrl;
  if (c.includes("bebida") || n.includes("coca") || n.includes("agua") || n.includes("jugo") || n.includes("gaseosa")) return drinkUrl;
  if (c.includes("postre") || n.includes("tiramisú") || n.includes("tiramisu") || n.includes("brownie") || n.includes("helado")) return dessertUrl;
  return foodDefaultUrl;
};

type ProductBadge = { label: string; className: string };

const getProductBadge = (product: Product): ProductBadge => {
  const cat = product.categoriaNombre;
  if (cat === "Pizzas") return { label: "20% OFF", className: "discount" };
  if (cat === "Hamburguesas") return { label: "Clásico", className: "favorite" };
  if (cat === "Bebidas") return { label: "Refrescante", className: "favorite" };
  if (cat === "Postres") return { label: "Para cerrar", className: "discount" };
  return { label: "Recomendado", className: "favorite" };
};

const renderVisual = (container: HTMLDivElement, imageSrc: string, imageAlt: string) => {
  container.innerHTML = "";
  const img = document.createElement("img");
  img.src = imageSrc;
  img.alt = imageAlt;
  img.loading = "lazy";
  img.addEventListener("load", () => img.classList.add("is-loaded"));
  img.addEventListener("error", () => {
    img.src = foodDefaultUrl;
    img.onerror = null;
  });
  container.appendChild(img);
};

const renderHeroProduct = () => {
  if (ALL_PRODUCTS.length === 0) return;
  const featured = [...ALL_PRODUCTS].sort((a, b) => b.precio - a.precio)[0];
  const badge = getProductBadge(featured);
  heroBadge.textContent = badge.label;
  heroProductCategory.textContent = featured.categoriaNombre;
  heroProductName.textContent = featured.nombre;
  heroProductDescription.textContent = featured.descripcion;
  heroProductPrice.textContent = `$${featured.precio.toLocaleString("es-AR")}`;
  renderVisual(heroProductVisual, getProductImage(featured.nombre, featured.categoriaNombre), featured.nombre);
};

const createProductCard = (product: Product): HTMLDivElement => {
  const card = document.createElement("div");
  card.className = "product-card";
  const badge = getProductBadge(product);
  card.innerHTML = `
    <span class="product-badge ${badge.className}">${badge.label}</span>
    <div class="product-img"></div>
    <div class="product-info">
      <span class="category-tag">${product.categoriaNombre}</span>
      <h3>${product.nombre}</h3>
      <p class="description">${product.descripcion}</p>
      <div class="product-meta">
        <p class="price">$${product.precio.toLocaleString("es-AR")}</p>
        <button class="btn-add" data-id="${product.id}">
          <span class="btn-add-text">Agregar</span>
        </button>
      </div>
    </div>
  `;
  const imgContainer = card.querySelector(".product-img") as HTMLDivElement;
  renderVisual(imgContainer, getProductImage(product.nombre, product.categoriaNombre), product.nombre);

  const btn = card.querySelector(".btn-add") as HTMLButtonElement;
  const btnText = btn.querySelector(".btn-add-text") as HTMLSpanElement;
  btn.addEventListener("click", () => {
    addToCart({ id: product.id, name: product.nombre, price: product.precio, stock: product.stock, imagen: product.imagen });
    updateCartCount();
    showToast(product.nombre);
    btnText.textContent = "Agregado";
    btn.classList.add("btn-added");
    btn.disabled = true;
    setTimeout(() => {
      btnText.textContent = "Agregar";
      btn.classList.remove("btn-added");
      btn.disabled = false;
    }, 1000);
  });

  return card;
};

const renderProducts = () => {
  const searchTerm = searchInput.value.toLowerCase().trim();
  const filtered = ALL_PRODUCTS.filter((p) => {
    const matchName = p.nombre.toLowerCase().includes(searchTerm);
    const matchCategory = selectedCategory === "" || p.categoriaNombre === selectedCategory;
    return matchName && matchCategory && p.disponible;
  });
  productsGrid.innerHTML = "";
  if (filtered.length === 0) {
    noResults.style.display = "block";
  } else {
    noResults.style.display = "none";
    filtered.forEach((product) => productsGrid.appendChild(createProductCard(product)));
  }
};

const renderCategoryFilters = (categories: Categoria[]) => {
  categoryFilters.innerHTML = "";
  const allBtn = document.createElement("button");
  allBtn.textContent = "Todas";
  allBtn.className = selectedCategory === "" ? "active" : "";
  allBtn.addEventListener("click", () => {
    selectedCategory = "";
    localStorage.setItem("store_category", "");
    updateActiveCategory();
    renderProducts();
  });
  categoryFilters.appendChild(allBtn);

  categories.forEach((cat) => {
    const btn = document.createElement("button");
    btn.textContent = cat.nombre;
    btn.className = selectedCategory === cat.nombre ? "active" : "";
    btn.addEventListener("click", () => {
      selectedCategory = cat.nombre;
      localStorage.setItem("store_category", cat.nombre);
      updateActiveCategory();
      renderProducts();
    });
    categoryFilters.appendChild(btn);
  });
};

const updateActiveCategory = () => {
  categoryFilters.querySelectorAll("button").forEach((btn) => {
    const isAll = selectedCategory === "" && btn.textContent === "Todas";
    const matches = selectedCategory !== "" && btn.textContent?.includes(selectedCategory);
    btn.className = isAll || matches ? "active" : "";
  });
};

const updateCartCount = () => {
  cartCount.textContent = String(getCartCount());
};

const showToast = (name: string) => {
  toast.innerHTML = `${name} agregado al carrito — <a href="../cart/cart.html" class="toast-action">Ver carrito</a>`;
  toast.classList.add("show");
  setTimeout(() => toast.classList.remove("show"), 2000);
};

searchInput.addEventListener("input", () => {
  localStorage.setItem("store_search", searchInput.value);
  renderProducts();
});

logoutBtn.addEventListener("click", logout);

const init = async () => {
  const user = requireAuth("USUARIO");
  if (!user) return;

  searchInput.value = localStorage.getItem("store_search") || "";
  updateCartCount();

  // Mostrar un indicador de carga en la grilla
  productsGrid.innerHTML = `<p style="grid-column:1/-1;text-align:center;color:#aaa;padding:40px 0;">Cargando productos…</p>`;

  try {
    const [products, categories] = await Promise.all([
      api.get<Product[]>("/products"),
      api.get<Categoria[]>("/categories"),
    ]);
    ALL_PRODUCTS = products;
    renderHeroProduct();
    renderCategoryFilters(categories);
    updateActiveCategory();
    renderProducts();
  } catch (err) {
    productsGrid.innerHTML = `<p style="grid-column:1/-1;text-align:center;color:#c0392b;padding:40px 0;">No se pudieron cargar los productos. Verificá que el servidor esté corriendo.</p>`;
  }
};

init();

