import "../store.css";
import "./improvements.css";
import { PRODUCTS, getCategories } from "../../../data/data";
import { addToCart, getCartCount } from "../../../utils/cart";
import type { Product } from "../../../types/product";

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

let selectedCategory = localStorage.getItem("store_category") || "";

// Emojis por categoría para usar como placeholder de imagen
const categoryEmojis: Record<string, string> = {
  Hamburguesas: "🍔",
  Pizzas: "🍕",
  Empanadas: "🥟",
  Bebidas: "🥤",
  Acompañamientos: "🍟",
  Postres: "🍫",
  Combos: "🍱",
};

type ProductBadge = {
  label: string;
  className: string;
};

/* MEJORA: badges promocionales discretos para enriquecer las cards */
const getProductBadge = (product: Product): ProductBadge => {
  if (product.category === "Combos") {
    return { label: "Best Seller", className: "favorite" };
  }

  if (product.category === "Pizzas") {
    return { label: "20% Off", className: "discount" };
  }

  return { label: "Recomendado", className: "favorite" };
};

/* MEJORA: utilitario para aplicar imagen con transición o fallback visual */
const renderVisual = (
  container: HTMLDivElement,
  imageSrc: string,
  imageAlt: string,
  fallbackEmoji: string,
) => {
  container.innerHTML = "";

  const img = document.createElement("img");
  img.src = imageSrc;
  img.alt = imageAlt;
  img.loading = "lazy";

  img.addEventListener("load", () => {
    img.classList.add("is-loaded");
  });

  img.addEventListener("error", () => {
    img.src = "/src/assets/icons/food.svg";
    img.onerror = null;
  });

  container.appendChild(img);
};

/* MEJORA: hero dinámico alimentado con el producto más fuerte del menú */
const renderHeroProduct = () => {
  const featuredProduct = [...PRODUCTS].sort((current, next) => next.price - current.price)[0];
  const emoji = categoryEmojis[featuredProduct.category] || "🍽️";
  const badge = getProductBadge(featuredProduct);

  heroBadge.textContent = badge.label;
  heroProductCategory.textContent = featuredProduct.category;
  heroProductName.textContent = featuredProduct.name;
  heroProductDescription.textContent = featuredProduct.description;
  heroProductPrice.textContent = `$${featuredProduct.price.toLocaleString("es-AR")}`;
  renderVisual(heroProductVisual, featuredProduct.image, featuredProduct.name, emoji);
};

// Renderizar un producto en una card
const createProductCard = (product: Product): HTMLDivElement => {
  const card = document.createElement("div");
  card.className = "product-card";

  const emoji = categoryEmojis[product.category] || "🍽️";
  const badge = getProductBadge(product);

  card.innerHTML = `
    <span class="product-badge ${badge.className}">${badge.label}</span>
    <div class="product-img">
    </div>
    <div class="product-info">
      <span class="category-tag">${product.category}</span>
      <h3>${product.name}</h3>
      <p class="description">${product.description}</p>
      <div class="product-meta">
        <p class="price">$${product.price.toLocaleString("es-AR")}</p>
        <button class="btn-add" data-id="${product.id}">
          <span class="btn-add-text">Sumar</span>
          <span class="btn-add-icon">🛒</span>
        </button>
      </div>
    </div>
  `;

  const imgContainer = card.querySelector(".product-img") as HTMLDivElement;
  renderVisual(imgContainer, product.image, product.name, emoji);

  const btn = card.querySelector(".btn-add") as HTMLButtonElement;
  const btnText = btn.querySelector(".btn-add-text") as HTMLSpanElement;
  btn.addEventListener("click", () => {
    addToCart({ id: product.id, name: product.name, price: product.price });
    updateCartCount();
    showToast(product.name);

    btnText.textContent = "Agregado";
    btn.disabled = true;
    setTimeout(() => {
      btnText.textContent = "Sumar";
      btn.disabled = false;
    }, 1000);
  });

  return card;
};

// Filtrar y renderizar productos
const renderProducts = () => {
  const searchTerm = searchInput.value.toLowerCase().trim();

  const filtered = PRODUCTS.filter((p) => {
    const matchName = p.name.toLowerCase().includes(searchTerm);
    const matchCategory = selectedCategory === "" || p.category === selectedCategory;
    return matchName && matchCategory;
  });

  productsGrid.innerHTML = "";

  if (filtered.length === 0) {
    noResults.style.display = "block";
  } else {
    noResults.style.display = "none";
    filtered.forEach((product) => {
      productsGrid.appendChild(createProductCard(product));
    });
  }
};

// Renderizar botones de categoría
const renderCategoryFilters = () => {
  const categories = getCategories();

  // Botón "Todas"
  const allBtn = document.createElement("button");
  allBtn.textContent = "🍽️ Todas";
  allBtn.className = "active";
  allBtn.addEventListener("click", () => {
    selectedCategory = "";
    localStorage.setItem("store_category", "");
    updateActiveCategory();
    renderProducts();
  });
  categoryFilters.appendChild(allBtn);

  categories.forEach((cat) => {
    const btn = document.createElement("button");
    const emoji = categoryEmojis[cat] || "";
    btn.textContent = `${emoji} ${cat}`;
    btn.addEventListener("click", () => {
      selectedCategory = cat;
      localStorage.setItem("store_category", cat);
      updateActiveCategory();
      renderProducts();
    });
    categoryFilters.appendChild(btn);
  });
};

// Actualizar botón activo de categoría
const updateActiveCategory = () => {
  const buttons = categoryFilters.querySelectorAll("button");
  buttons.forEach((btn) => {
    if (selectedCategory === "" && btn.textContent === "🍽️ Todas") {
      btn.classList.add("active");
    } else if (btn.textContent?.includes(selectedCategory) && selectedCategory !== "") {
      btn.classList.add("active");
    } else {
      btn.classList.remove("active");
    }
  });
};

// Actualizar contador del carrito
const updateCartCount = () => {
  cartCount.textContent = String(getCartCount());
};

// Mostrar notificación toast
const showToast = (name: string) => {
  toast.innerHTML = `${name} agregado al carrito \u{1F6D2} <a href="../cart/cart.html" class="toast-action">Ver carrito</a>`;
  toast.classList.add("show");
  setTimeout(() => {
    toast.classList.remove("show");
  }, 2000);
};

// Evento de búsqueda en vivo
searchInput.addEventListener("input", () => {
  localStorage.setItem("store_search", searchInput.value);
  renderProducts();
});

// Salir: va al login
logoutBtn.addEventListener("click", () => {
  window.location.href = "/src/pages/auth/login/login.html";
});

// Inicializar página
const init = () => {
  searchInput.value = localStorage.getItem("store_search") || "";
  renderHeroProduct();
  renderCategoryFilters();
  updateActiveCategory();
  renderProducts();
  updateCartCount();
};

init();
