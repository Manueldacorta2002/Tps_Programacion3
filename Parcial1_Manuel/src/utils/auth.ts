import type { IUser } from "../types/IUser";
import { getCurrentUser, removeUser } from "./localStorage";
import { navigate } from "./navigate";

export const isAuthenticated = (): boolean => getCurrentUser() !== null;

export const isAdmin = (): boolean => getCurrentUser()?.rol === "ADMIN";

export const logout = (): void => {
  removeUser();
  navigate("/src/pages/auth/login/login.html");
};

/**
 * Protects a page by requiring a specific role.
 * Returns the user if authorized, otherwise redirects and returns null.
 */
export const requireAuth = (required: "ADMIN" | "USUARIO"): IUser | null => {
  const user = getCurrentUser();
  if (!user) {
    navigate("/src/pages/auth/login/login.html");
    return null;
  }
  if (user.rol !== required) {
    const fallback =
      user.rol === "ADMIN"
        ? "/src/pages/admin/home/home.html"
        : "/src/pages/store/home/home.html";
    navigate(fallback);
    return null;
  }
  return user;
};

/**
 * Legacy alias. Maps old "admin"/"client" role strings to new ADMIN/USUARIO.
 */
export const checkAuhtUser = (
  redirectIfNoAuth: string,
  redirectIfWrongRole: string,
  rol: string
): void => {
  const user = getCurrentUser();
  if (!user) {
    navigate(redirectIfNoAuth);
    return;
  }
  const expected = rol === "admin" ? "ADMIN" : "USUARIO";
  if (user.rol !== expected) {
    navigate(redirectIfWrongRole);
  }
};

