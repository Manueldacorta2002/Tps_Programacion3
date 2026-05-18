import type { Product } from "../types/product";
import type { Categoria } from "../types/categoria";

export const PRODUCTS: Product[] = [
  {
    id: 1,
    name: "Hamburguesa",
    price: 1500,
    image: "/src/assets/icons/burger.svg",
    category: "Hamburguesas",
    description: "La de siempre: carne, queso, lechuga y tomate",
  },
  {
    id: 2,
    name: "Pizza",
    price: 2800,
    image: "/src/assets/icons/pizza.svg",
    category: "Pizzas",
    description: "Grande, con mucha muzza y bien al horno",
  },
  {
    id: 3,
    name: "Papas Fritas",
    price: 1200,
    image: "/src/assets/icons/fries.svg",
    category: "Acompañamientos",
    description: "Bien crocantes, ideales para acompañar",
  },
  {
    id: 4,
    name: "Bebida",
    price: 800,
    image: "/src/assets/icons/drink.svg",
    category: "Bebidas",
    description: "Gaseosa de 500ml, elegí la que quieras",
  },
  {
    id: 5,
    name: "Combo",
    price: 3500,
    image: "/src/assets/icons/combo.svg",
    category: "Combos",
    description: "Hamburguesa + papas + bebida, todo junto",
  },
  {
    id: 6,
    name: "Empanada",
    price: 600,
    image: "/src/assets/icons/empanada.svg",
    category: "Acompañamientos",
    description: "De carne cortada a cuchillo, bien jugosa",
  },
  {
    id: 7,
    name: "Helado",
    price: 900,
    image: "/src/assets/icons/ice-cream.svg",
    category: "Postres",
    description: "Cucurucho doble con el sabor que elijas",
  },
  {
    id: 8,
    name: "Hamburguesa Doble",
    price: 2200,
    image: "/src/assets/icons/double-burger.svg",
    category: "Hamburguesas",
    description: "Doble medallón con extra queso y panceta",
  },
  {
    id: 9,
    name: "Agua",
    price: 400,
    image: "/src/assets/icons/water.svg",
    category: "Bebidas",
    description: "Botella de agua mineral sin gas, 500ml",
  },
  {
    id: 10,
    name: "Combo Familiar",
    price: 5500,
    image: "/src/assets/icons/family-combo.svg",
    category: "Combos",
    description: "Pizza + 4 empanadas + 2 bebidas, ideal para compartir",
  },
];

export const getCategories = (): Categoria[] => {
  const categories = PRODUCTS.map((p) => p.category);
  return [...new Set(categories)];
};
