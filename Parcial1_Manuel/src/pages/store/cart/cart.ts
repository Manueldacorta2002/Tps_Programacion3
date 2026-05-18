import "../store.css";
import {
  getCart,
  updateQuantity,
  removeFromCart,
  clearCart,
} from "../../../utils/cart";
import type { CartItem } from "../../../utils/cart";

const cartContent = document.getElementById("cartContent") as HTMLDivElement;
const logoutBtn = document.getElementById("logoutButton") as HTMLButtonElement;

// Pequeño efecto visual al cambiar cantidad
const bumpQuantity = (btn: HTMLElement) => {
  const controls = btn.closest(".item-controls");
  const span = controls?.querySelector("span");
  if (span) {
    span.classList.add("bump");
    setTimeout(() => span.classList.remove("bump"), 150);
  }
};

// Emojis por nombre de producto
const productEmojis: Record<string, string> = {
  Hamburguesa: "🍔",
  "Hamburguesa Doble": "🍔",
  Pizza: "🍕",
  "Papas Fritas": "🍟",
  Bebida: "🥤",
  Agua: "🍾",
  Combo: "🍱",
  "Combo Familiar": "🥘",
  Empanada: "🥟",
  Helado: "🍦",
};

// Renderizar el carrito completo
const renderCart = () => {
  const cart = getCart();

  if (cart.length === 0) {
    cartContent.innerHTML = `
      <div class="cart-empty">
        <div class="cart-empty-icon">🛒</div>
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
    const emoji = productEmojis[item.name] || "🍽️";
    html += `
      <div class="cart-item" data-id="${item.id}">
        <div class="item-emoji">${emoji}</div>
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

    // Calcular totales para mostrar en el modal
    const itemCount = cart.reduce((sum, item) => sum + item.quantity, 0);
    const total = cart.reduce((sum, item) => sum + item.price * item.quantity, 0);

    // Rellenar datos del modal
    const modalItemCount = document.getElementById("modalItemCount");
    const modalTotal = document.getElementById("modalTotal");
    if (modalItemCount) modalItemCount.textContent = `${itemCount} ${itemCount === 1 ? "producto" : "productos"}`;
    if (modalTotal) modalTotal.textContent = `$${total.toLocaleString("es-AR")}`;

    // Mostrar modal
    const modal = document.getElementById("confirmModal");
    if (modal) {
      modal.classList.add("modal-visible");
      modal.setAttribute("aria-hidden", "false");
    }

    // Vaciar carrito
    clearCart();
  });
};

// Botón cerrar del modal
document.getElementById("modalClose")?.addEventListener("click", () => {
  const modal = document.getElementById("confirmModal");
  if (modal) {
    modal.classList.remove("modal-visible");
    modal.setAttribute("aria-hidden", "true");
  }
  renderCart(); // Refresca para mostrar carrito vacío
});

// Cerrar modal al hacer click en el overlay (fuera de la caja)
document.getElementById("confirmModal")?.addEventListener("click", (e) => {
  if ((e.target as HTMLElement).id === "confirmModal") {
    const modal = document.getElementById("confirmModal");
    if (modal) {
      modal.classList.remove("modal-visible");
      modal.setAttribute("aria-hidden", "true");
    }
    renderCart();
  }
});

// Salir: vuelve al catálogo
logoutBtn.addEventListener("click", () => {
  window.location.href = "../home/home.html";
});

// Inicializar
renderCart();
