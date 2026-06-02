import "./admin.css";
import { requireAuth, logout } from "../../../utils/auth";
import { api } from "../../../api/api";
import type { Product } from "../../../types/product";
import type { Categoria } from "../../../types/categoria";
import type { PedidoDto } from "../../../types/pedido";

const logoutBtn = document.getElementById("logoutButton") as HTMLButtonElement;
const welcomeMsg = document.getElementById("welcomeMsg") as HTMLHeadingElement;
const statProductos = document.getElementById("statProductos") as HTMLSpanElement;
const statCategorias = document.getElementById("statCategorias") as HTMLSpanElement;
const statPedidos = document.getElementById("statPedidos") as HTMLSpanElement;
const statPendientes = document.getElementById("statPendientes") as HTMLSpanElement;
const badgeProductos = document.getElementById("badgeProductos") as HTMLSpanElement;
const badgeCategorias = document.getElementById("badgeCategorias") as HTMLSpanElement;
const badgePedidos = document.getElementById("badgePedidos") as HTMLSpanElement;

logoutBtn.addEventListener("click", logout);

const init = async () => {
  const user = requireAuth("ADMIN");
  if (!user) return;

  welcomeMsg.textContent = `Bienvenido, ${user.nombre}`;

  try {
    const [products, categories, orders] = await Promise.all([
      api.get<Product[]>("/products"),
      api.get<Categoria[]>("/categories"),
      api.get<PedidoDto[]>("/orders"),
    ]);

    const pendientes = orders.filter((o) => o.estado === "PENDIENTE").length;

    statProductos.textContent = String(products.length);
    statCategorias.textContent = String(categories.length);
    statPedidos.textContent = String(orders.length);
    statPendientes.textContent = String(pendientes);

    badgeProductos.textContent = `${products.length} productos`;
    badgeCategorias.textContent = `${categories.length} categorías`;
    badgePedidos.textContent = `${orders.length} pedidos · ${pendientes} pendientes`;
  } catch {
    statProductos.textContent = "—";
    statCategorias.textContent = "—";
    statPedidos.textContent = "—";
    statPendientes.textContent = "—";
  }
};

init();

