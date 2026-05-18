export interface CartItem {
  id: number;
  name: string;
  price: number;
  quantity: number;
}

const CART_KEY = "cart";

export const getCart = (): CartItem[] => {
  const data = localStorage.getItem(CART_KEY);
  if (!data) return [];
  return JSON.parse(data) as CartItem[];
};

export const saveCart = (cart: CartItem[]): void => {
  localStorage.setItem(CART_KEY, JSON.stringify(cart));
};

export const addToCart = (product: {
  id: number;
  name: string;
  price: number;
}): void => {
  const cart = getCart();
  const existing = cart.find((item) => item.id === product.id);

  if (existing) {
    existing.quantity += 1;
  } else {
    cart.push({
      id: product.id,
      name: product.name,
      price: product.price,
      quantity: 1,
    });
  }

  saveCart(cart);
};

export const removeFromCart = (productId: number): void => {
  const cart = getCart().filter((item) => item.id !== productId);
  saveCart(cart);
};

export const updateQuantity = (productId: number, quantity: number): void => {
  const cart = getCart();
  const item = cart.find((i) => i.id === productId);
  if (item) {
    item.quantity = quantity;
    if (item.quantity <= 0) {
      saveCart(cart.filter((i) => i.id !== productId));
    } else {
      saveCart(cart);
    }
  }
};

export const clearCart = (): void => {
  localStorage.removeItem(CART_KEY);
};

export const getCartCount = (): number => {
  const cart = getCart();
  return cart.reduce((total, item) => total + item.quantity, 0);
};
