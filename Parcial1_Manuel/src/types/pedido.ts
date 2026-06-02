export type Estado = "PENDIENTE" | "CONFIRMADO" | "TERMINADO" | "CANCELADO";
export type FormaPago = "TARJETA" | "TRANSFERENCIA" | "EFECTIVO";

export interface DetallePedidoDto {
  id: number;
  cantidad: number;
  subtotal: number;
  productoId: number;
}

export interface PedidoDto {
  id: number;
  fecha: string;
  estado: Estado;
  formaPago: FormaPago;
  total: number;
  usuarioId: number;
  detalles: DetallePedidoDto[];
}

export interface PedidoCreate {
  formaPago: FormaPago;
  usuarioId: number;
  detalles: Array<{ cantidad: number; productoId: number }>;
}
