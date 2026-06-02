export interface CartItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
  stock?: number;
  imagen?: string;
}

const CART_KEY = "cart";

export const getCart = (): CartItem[] => {
  const data = localStorage.getItem(CART_KEY);
  if (!data) return [];
  try {
    return JSON.parse(data) as CartItem[];
  } catch {
    return [];
  }
};

export const saveCart = (cart: CartItem[]): void => {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
};

export const addToCart = (product: {
  id: number;
  name: string;
  price: number;
  stock?: number;
  imagen?: string;
}): void => {
  const cart = getCart();
  const existing = cart.find((item) => item.id === product.id);
  const maxStock = product.stock ?? Infinity;

  if (existing) {
    if (existing.quantity < maxStock) {
      existing.quantity += 1;
    }
  } else {
    cart.push({
      id: product.id,
      name: product.name,
      price: product.price,
      quantity: 1,
      stock: product.stock,
      imagen: product.imagen,
    });
  }

  saveCart(cart);
};

export const removeFromCart = (productId: number): void => {
  saveCart(getCart().filter((item) => item.id !== productId));
};

export const updateQuantity = (productId: number, quantity: number): void => {
  const cart = getCart();
  const item = cart.find((i) => i.id === productId);
  if (!item) return;
  if (quantity <= 0) {
    saveCart(cart.filter((i) => i.id !== productId));
  } else {
    const max = item.stock ?? Infinity;
    item.quantity = Math.min(quantity, max);
    saveCart(cart);
  }
};

export const clearCart = (): void => {
  localStorage.removeItem(CART_KEY);
};

export const getCartCount = (): number => {
  return getCart().reduce((total, item) => total + item.quantity, 0);
};

