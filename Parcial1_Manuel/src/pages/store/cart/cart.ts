import "../store.css";
import {
  getCart,
  updateQuantity,
  removeFromCart,
  clearCart,
} from "../../../utils/cart";
import type { CartItem } from "../../../utils/cart";
import { api } from "../../../api/api";
import type { PedidoCreate, FormaPago } from "../../../types/pedido";
import { getCurrentUser } from "../../../utils/localStorage";
import { logout } from "../../../utils/auth";
import pizzaUrl from "../../../assets/products/pizza.svg";
import burgerUrl from "../../../assets/products/burger.svg";
import drinkUrl from "../../../assets/products/drink.svg";
import dessertUrl from "../../../assets/products/dessert.svg";
import foodDefaultUrl from "../../../assets/products/default-food.svg";

const cartContent = document.getElementById("cartContent") as HTMLDivElement;
const logoutBtn = document.getElementById("logoutButton") as HTMLButtonElement;
const modal = document.getElementById("confirmModal") as HTMLDivElement;
const modalStage1 = document.getElementById("modalStage1") as HTMLDivElement;
const modalStage2 = document.getElementById("modalStage2") as HTMLDivElement;
const modalItemCount = document.getElementById("modalItemCount") as HTMLSpanElement;
const modalTotal = document.getElementById("modalTotal") as HTMLSpanElement;
const modalTotalFinal = document.getElementById("modalTotalFinal") as HTMLSpanElement;
const modalFormaPagoFinal = document.getElementById("modalFormaPagoFinal") as HTMLSpanElement;
const formaPagoSelect = document.getElementById("formaPago") as HTMLSelectElement;
const btnPagar = document.getElementById("btnPagar") as HTMLButtonElement;
const modalCancel = document.getElementById("modalCancel") as HTMLButtonElement;
const modalError = document.getElementById("modalError") as HTMLDivElement;

// Pequeño efecto visual al cambiar cantidad
const bumpQuantity = (btn: HTMLElement) => {
  const controls = btn.closest(".item-controls");
  const span = controls?.querySelector("span");
  if (span) {
    span.classList.add("bump");
    setTimeout(() => span.classList.remove("bump"), 150);
  }
};

const getProductImageByName = (name: string): string => {
  const n = name.toLowerCase();
  if (n.includes("pizza")) return pizzaUrl;
  if (n.includes("burger") || n.includes("hamburguesa")) return burgerUrl;
  if (n.includes("coca") || n.includes("agua") || n.includes("jugo") || n.includes("gaseosa")) return drinkUrl;
  if (n.includes("tiramisú") || n.includes("tiramisu") || n.includes("brownie") || n.includes("helado")) return dessertUrl;
  return foodDefaultUrl;
};

const openModal = () => {
  modalStage1.style.display = "block";
  modalStage2.style.display = "none";
  modal.classList.add("modal-visible");
  modal.setAttribute("aria-hidden", "false");
  modalError.style.display = "none";
};

const closeModal = () => {
  modal.classList.remove("modal-visible");
  modal.setAttribute("aria-hidden", "true");
  renderCart();
};

// Renderizar el carrito completo
const renderCart = () => {
  const cart = getCart();

  if (cart.length === 0) {
    cartContent.innerHTML = `
      <div class="cart-empty">
        <div class="cart-empty-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><circle cx="9" cy="21" r="1"/><circle cx="20" cy="21" r="1"/><path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"/></svg>
        </div>
        <p class="cart-empty-title">Tu carrito está vacío</p>
        <p class="cart-empty-hint">Agregá algo rico del catálogo</p>
        <a href="../home/home.html" class="btn-back">Ver productos</a>
      </div>
    `;
    return;
  }

  let html = "";

  cart.forEach((item: CartItem) => {
    const subtotal = item.price * item.quantity;
    const imgSrc = getProductImageByName(item.name);
    html += `
      <div class="cart-item" data-id="${item.id}">
        <img class="item-img" src="${imgSrc}" alt="${item.name}">
        <div class="item-info">
          <h3>${item.name}</h3>
          <p class="item-price">$${item.price.toLocaleString("es-AR")} c/u</p>
        </div>
        <div class="item-controls">
          <button class="btn-decrease" data-id="${item.id}">−</button>
          <span>${item.quantity}</span>
          <button class="btn-increase" data-id="${item.id}">+</button>
        </div>
        <span class="item-subtotal">$${subtotal.toLocaleString("es-AR")}</span>
        <button class="btn-remove" data-id="${item.id}">Quitar</button>
      </div>
    `;
  });

  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
  const itemCount = cart.reduce((sum, item) => sum + item.quantity, 0);

  html += `
    <div class="cart-total">
      <span class="total-label">${itemCount} ${itemCount === 1 ? "producto" : "productos"}</span>
      <span class="total-amount">$${total.toLocaleString("es-AR")}</span>
    </div>
    <div class="cart-actions">
      <a href="../home/home.html" class="btn-secondary">← Seguir viendo</a>
      <button class="btn-primary" id="btnConfirm">Confirmar pedido ✓</button>
    </div>
    <p class="borrar-todo-link" id="btnClear">Borrar todo el carrito</p>
  `;

  cartContent.innerHTML = html;

  // Event listeners para los botones
  cartContent.querySelectorAll(".btn-increase").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number((btn as HTMLButtonElement).dataset.id);
      const item = cart.find((i) => i.id === id);
      if (item) {
        bumpQuantity(btn as HTMLElement);
        updateQuantity(id, item.quantity + 1);
        renderCart();
      }
    });
  });

  cartContent.querySelectorAll(".btn-decrease").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number((btn as HTMLButtonElement).dataset.id);
      const item = cart.find((i) => i.id === id);
      if (item) {
        bumpQuantity(btn as HTMLElement);
        updateQuantity(id, item.quantity - 1);
        renderCart();
      }
    });
  });

  cartContent.querySelectorAll(".btn-remove").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number((btn as HTMLButtonElement).dataset.id);
      removeFromCart(id);
      renderCart();
    });
  });

  const btnClear = document.getElementById("btnClear");
  btnClear?.addEventListener("click", () => {
    clearCart();
    renderCart();
  });

  const btnConfirm = document.getElementById("btnConfirm");
  btnConfirm?.addEventListener("click", () => {
    const cart = getCart();
    if (cart.length === 0) return;
    const itemCount = cart.reduce((sum, item) => sum + item.quantity, 0);
    const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);
    modalItemCount.textContent = `${itemCount} ${itemCount === 1 ? "producto" : "productos"}`;
    modalTotal.textContent = `$${total.toLocaleString("es-AR")}`;
    openModal();
  });
};

// Botón "Pagar": POST /api/orders
btnPagar.addEventListener("click", async () => {
  const cart = getCart();
  if (cart.length === 0) return;

  const user = getCurrentUser();
  if (!user) {
    modalError.textContent = "Sesión expirada. Por favor, iniciá sesión nuevamente.";
    modalError.style.display = "block";
    return;
  }

  const formaPago = formaPagoSelect.value as FormaPago;
  const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

  const payload: PedidoCreate = {
    formaPago,
    usuarioId: user.id,
    detalles: cart.map((item) => ({ cantidad: item.quantity, productoId: item.id })),
  };

  btnPagar.disabled = true;
  btnPagar.textContent = "Procesando...";
  modalError.style.display = "none";

  try {
    await api.post("/orders", payload);
    clearCart();
    modalTotalFinal.textContent = `$${total.toLocaleString("es-AR")}`;
    const labels: Record<FormaPago, string> = { TARJETA: "Tarjeta", TRANSFERENCIA: "Transferencia", EFECTIVO: "Efectivo" };
    modalFormaPagoFinal.textContent = labels[formaPago];
    modalStage1.style.display = "none";
    modalStage2.style.display = "block";
  } catch (err) {
    modalError.textContent = err instanceof Error ? err.message : "No se pudo procesar el pedido. Intentá de nuevo.";
    modalError.style.display = "block";
  } finally {
    btnPagar.disabled = false;
    btnPagar.textContent = "Pagar";
  }
});

// Cancelar modal
modalCancel.addEventListener("click", closeModal);

// Cerrar modal al hacer click en el overlay
modal.addEventListener("click", (e) => {
  if ((e.target as HTMLElement).id === "confirmModal") closeModal();
});

// Cerrar sesión
logoutBtn.addEventListener("click", logout);

// Inicializar
renderCart();

