import "../store.css";
import { api } from "../../../api/api";
import type { PedidoDto, Estado } from "../../../types/pedido";
import { requireAuth, logout } from "../../../utils/auth";

const ordersContent = document.getElementById("ordersContent") as HTMLDivElement;
const logoutBtn = document.getElementById("logoutButton") as HTMLButtonElement;

const estadoLabel: Record<Estado, string> = {
  PENDIENTE: "Pendiente",
  CONFIRMADO: "Confirmado",
  TERMINADO: "Terminado",
  CANCELADO: "Cancelado",
};

const estadoClass: Record<Estado, string> = {
  PENDIENTE: "estado-pendiente",
  CONFIRMADO: "estado-confirmado",
  TERMINADO: "estado-terminado",
  CANCELADO: "estado-cancelado",
};

const formaLabel: Record<string, string> = {
  TARJETA: "Tarjeta",
  TRANSFERENCIA: "Transferencia",
  EFECTIVO: "Efectivo",
};

const renderOrders = (orders: PedidoDto[]) => {
  if (orders.length === 0) {
    ordersContent.innerHTML = `
      <div class="cart-empty">
        <div class="cart-empty-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/></svg>
        </div>
        <p class="cart-empty-title">No tenés pedidos todavía</p>
        <p class="cart-empty-hint">Realizá tu primera compra desde el catálogo</p>
        <a href="../home/home.html" class="btn-back">Ver productos</a>
      </div>
    `;
    return;
  }

  const sorted = [...orders].sort(
    (a, b) => new Date(b.fecha).getTime() - new Date(a.fecha).getTime()
  );

  let html = `<div class="orders-list">`;

  sorted.forEach((order) => {
    const fecha = new Date(order.fecha).toLocaleDateString("es-AR", {
      year: "numeric",
      month: "long",
      day: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
    const estadoCls = estadoClass[order.estado] || "";
    html += `
      <div class="order-card">
        <div class="order-header">
          <span class="order-id">Pedido #${order.id}</span>
          <span class="order-estado ${estadoCls}">${estadoLabel[order.estado] || order.estado}</span>
        </div>
        <div class="order-body">
          <div class="order-row"><span>Fecha</span><span>${fecha}</span></div>
          <div class="order-row"><span>Forma de pago</span><span>${formaLabel[order.formaPago] || order.formaPago}</span></div>
          <div class="order-row"><span>Productos</span><span>${order.detalles.length} ítem${order.detalles.length !== 1 ? "s" : ""}</span></div>
          <div class="order-row order-total"><span>Total</span><span>$${order.total.toLocaleString("es-AR")}</span></div>
        </div>
      </div>
    `;
  });

  html += `</div>`;

  html += `
    <style>
      .orders-list { display: flex; flex-direction: column; gap: 16px; max-width: 700px; margin: 32px auto; padding: 0 16px; }
      .order-card { background: white; border-radius: 16px; box-shadow: 0 2px 12px rgba(0,0,0,0.07); overflow: hidden; }
      .order-header { display: flex; justify-content: space-between; align-items: center; padding: 14px 20px; background: #faf7f5; border-bottom: 1px solid #f0e8e0; }
      .order-id { font-weight: 700; font-size: 0.95rem; color: #333; }
      .order-estado { font-size: 0.82rem; font-weight: 600; padding: 4px 10px; border-radius: 20px; }
      .estado-pendiente { background: #fef9c3; color: #854d0e; }
      .estado-confirmado { background: #dcfce7; color: #166534; }
      .estado-terminado { background: #e0f2fe; color: #0369a1; }
      .estado-cancelado { background: #fee2e2; color: #991b1b; }
      .order-body { padding: 14px 20px; display: flex; flex-direction: column; gap: 8px; }
      .order-row { display: flex; justify-content: space-between; font-size: 0.88rem; color: #555; }
      .order-total { font-weight: 700; font-size: 1rem; color: #d35400; border-top: 1px solid #f0e8e0; padding-top: 8px; margin-top: 4px; }
    </style>
  `;

  ordersContent.innerHTML = html;
};

const init = async () => {
  const user = requireAuth("USUARIO");
  if (!user) return;

  ordersContent.innerHTML = `<p style="text-align:center;color:#aaa;padding:40px 0;">Cargando pedidos…</p>`;

  try {
    const orders = await api.get<PedidoDto[]>(`/orders/user/${user.id}`);
    renderOrders(orders);
  } catch {
    ordersContent.innerHTML = `<p style="text-align:center;color:#c0392b;padding:40px 0;">No se pudieron cargar los pedidos.</p>`;
  }
};

logoutBtn.addEventListener("click", logout);
init();
