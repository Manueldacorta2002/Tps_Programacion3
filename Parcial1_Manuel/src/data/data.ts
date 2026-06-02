import type { Product } from "../types/product";
import type { Categoria } from "../types/categoria";

export const PRODUCTS: Product[] = [
  {
    id: 1,
    nombre: "Hamburguesa",
    precio: 1500,
    imagen: "/src/assets/icons/burger.svg",
    categoriaId: 1,
    categoriaNombre: "Hamburguesas",
    descripcion: "La de siempre: carne, queso, lechuga y tomate",
    stock: 10,
    disponible: true,
  },
  {
    id: 2,
    nombre: "Pizza",
    precio: 2800,
    imagen: "/src/assets/icons/pizza.svg",
    categoriaId: 2,
    categoriaNombre: "Pizzas",
    descripcion: "Grande, con mucha muzza y bien al horno",
    stock: 10,
    disponible: true,
  },
  {
    id: 3,
    nombre: "Papas Fritas",
    precio: 1200,
    imagen: "/src/assets/icons/fries.svg",
    categoriaId: 3,
    categoriaNombre: "Acompañamientos",
    descripcion: "Bien crocantes, ideales para acompañar",
    stock: 10,
    disponible: true,
  },
  {
    id: 4,
    nombre: "Bebida",
    precio: 800,
    imagen: "/src/assets/icons/drink.svg",
    categoriaId: 4,
    categoriaNombre: "Bebidas",
    descripcion: "Gaseosa de 500ml, elegí la que quieras",
    stock: 10,
    disponible: true,
  },
  {
    id: 5,
    nombre: "Combo",
    precio: 3500,
    imagen: "/src/assets/icons/combo.svg",
    categoriaId: 5,
    categoriaNombre: "Combos",
    descripcion: "Hamburguesa + papas + bebida, todo junto",
    stock: 10,
    disponible: true,
  },
  {
    id: 6,
    nombre: "Empanada",
    precio: 600,
    imagen: "/src/assets/icons/empanada.svg",
    categoriaId: 3,
    categoriaNombre: "Acompañamientos",
    descripcion: "De carne cortada a cuchillo, bien jugosa",
    stock: 10,
    disponible: true,
  },
  {
    id: 7,
    nombre: "Helado",
    precio: 900,
    imagen: "/src/assets/icons/ice-cream.svg",
    categoriaId: 6,
    categoriaNombre: "Postres",
    descripcion: "Cucurucho doble con el sabor que elijas",
    stock: 10,
    disponible: true,
  },
  {
    id: 8,
    nombre: "Hamburguesa Doble",
    precio: 2200,
    imagen: "/src/assets/icons/double-burger.svg",
    categoriaId: 1,
    categoriaNombre: "Hamburguesas",
    descripcion: "Doble medallón con extra queso y panceta",
    stock: 10,
    disponible: true,
  },
  {
    id: 9,
    nombre: "Agua",
    precio: 400,
    imagen: "/src/assets/icons/water.svg",
    categoriaId: 4,
    categoriaNombre: "Bebidas",
    descripcion: "Botella de agua mineral sin gas, 500ml",
    stock: 10,
    disponible: true,
  },
  {
    id: 10,
    nombre: "Combo Familiar",
    precio: 5500,
    imagen: "/src/assets/icons/family-combo.svg",
    categoriaId: 5,
    categoriaNombre: "Combos",
    descripcion: "Pizza + 4 empanadas + 2 bebidas, ideal para compartir",
    stock: 10,
    disponible: true,
  },
];

export const getCategories = (): Categoria[] => {
  const seen = new Set<number>();
  const cats: Categoria[] = [];
  for (const p of PRODUCTS) {
    if (!seen.has(p.categoriaId)) {
      seen.add(p.categoriaId);
      cats.push({ id: p.categoriaId, nombre: p.categoriaNombre, descripcion: "" });
    }
  }
  return cats;
};

