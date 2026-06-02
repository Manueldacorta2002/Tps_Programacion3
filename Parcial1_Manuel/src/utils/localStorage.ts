import type { IUser } from "../types/IUser";

const USER_KEY = "userData";

export const saveUser = (user: IUser): void => {
  localStorage.setItem(USER_KEY, JSON.stringify(user));
};

export const getCurrentUser = (): IUser | null => {
  const raw = localStorage.getItem(USER_KEY);
  if (!raw) return null;
  try {
    return JSON.parse(raw) as IUser;
  } catch {
    return null;
  }
};

/** @deprecated Use getCurrentUser() */
export const getUSer = (): string | null => localStorage.getItem(USER_KEY);

export const removeUser = (): void => {
  localStorage.removeItem(USER_KEY);
};

