import "../home/admin.css";
import { requireAuth, logout } from "../../../utils/auth";
import { api } from "../../../api/api";
import type { PedidoDto, Estado } from "../../../types/pedido";

const logoutBtn = document.getElementById("logoutButton") as HTMLButtonElement;
const tableContainer = document.getElementById("tableContainer") as HTMLDivElement;
const filterEstado = document.getElementById("filterEstado") as HTMLSelectElement;

logoutBtn.addEventListener("click", logout);

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

const ESTADOS: Estado[] = ["PENDIENTE", "CONFIRMADO", "TERMINADO", "CANCELADO"];

let allOrders: PedidoDto[] = [];

const renderTable = (orders: PedidoDto[]) => {
  if (orders.length === 0) {
    tableContainer.innerHTML = `<p class="list-empty">No hay pedidos para mostrar.</p>`;
    return;
  }

  let html = `
    <table class="products-table">
      <thead>
        <tr>
          <th>#</th>
          <th>Fecha</th>
          <th>Usuario ID</th>
          <th>Items</th>
          <th>Pago</th>
          <th>Total</th>
          <th>Estado</th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
  `;

  orders.forEach((order) => {
    const fecha = new Date(order.fecha).toLocaleDateString("es-AR", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
    });

    const stateOptions = ESTADOS.map(
      (e) => `<option value="${e}" ${e === order.estado ? "selected" : ""}>${estadoLabel[e]}</option>`
    ).join("");

    html += `
      <tr>
        <td>${order.id}</td>
        <td>${fecha}</td>
        <td>${order.usuarioId}</td>
        <td>${order.detalles.length}</td>
        <td>${formaLabel[order.formaPago] || order.formaPago}</td>
        <td class="product-price">$${order.total.toLocaleString("es-AR")}</td>
        <td><span class="order-estado ${estadoClass[order.estado]}">${estadoLabel[order.estado]}</span></td>
        <td class="product-actions">
          <select class="select-estado" data-id="${order.id}">${stateOptions}</select>
          <button class="btn-edit btn-save-estado" data-id="${order.id}">Guardar</button>
          <button class="btn-delete btn-del-order" data-id="${order.id}">Eliminar</button>
        </td>
      </tr>
    `;
  });

  html += `</tbody></table>`;
  tableContainer.innerHTML = `<div class="table-wrapper">${html}</div>`;

  tableContainer.querySelectorAll<HTMLButtonElement>(".btn-save-estado").forEach((btn) => {
    btn.addEventListener("click", async () => {
      const id = Number(btn.dataset.id);
      const row = tableContainer.querySelector<HTMLSelectElement>(`.select-estado[data-id="${id}"]`);
      if (!row) return;
      const estado = row.value as Estado;
      btn.disabled = true;
      try {
        await api.patch(`/orders/${id}/status`, { estado });
        allOrders = allOrders.map((o) => (o.id === id ? { ...o, estado } : o));
        applyFilter();
      } catch (err) {
        alert(`No se pudo actualizar: ${(err as Error).message}`);
        btn.disabled = false;
      }
    });
  });

  tableContainer.querySelectorAll<HTMLButtonElement>(".btn-del-order").forEach((btn) => {
    btn.addEventListener("click", async () => {
      const id = Number(btn.dataset.id);
      if (!confirm(`¿Eliminar el pedido #${id}?`)) return;
      try {
        await api.delete(`/orders/${id}`);
        allOrders = allOrders.filter((o) => o.id !== id);
        applyFilter();
      } catch (err) {
        alert(`No se pudo eliminar: ${(err as Error).message}`);
      }
    });
  });
};

const applyFilter = () => {
  const estado = filterEstado.value as Estado | "";
  const filtered = estado ? allOrders.filter((o) => o.estado === estado) : allOrders;
  renderTable(filtered);
};

filterEstado.addEventListener("change", applyFilter);

const init = async () => {
  const user = requireAuth("ADMIN");
  if (!user) return;

  tableContainer.innerHTML = `<p style="text-align:center;color:#aaa;padding:32px;">Cargando pedidos…</p>`;
  try {
    allOrders = await api.get<PedidoDto[]>("/orders");
    applyFilter();
  } catch {
    tableContainer.innerHTML = `<p style="text-align:center;color:#c0392b;padding:32px;">No se pudieron cargar los pedidos.</p>`;
  }
};

init();
