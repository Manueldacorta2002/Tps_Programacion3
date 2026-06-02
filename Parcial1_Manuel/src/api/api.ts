export const API_URL = "http://localhost:8080/api";

async function request<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  const url = `${API_URL}${endpoint}`;
  const res = await fetch(url, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
  });

  if (!res.ok) {
    let errorMsg = `Error ${res.status}`;
    try {
      const errBody = await res.json();
      errorMsg = errBody.message ?? errBody.error ?? JSON.stringify(errBody) ?? errorMsg;
    } catch {
      // no-op: ignore parse errors
    }
    throw new Error(errorMsg);
  }

  if (res.status === 204) return undefined as T;
  return res.json() as Promise<T>;
}

export const api = {
  get:    <T>(endpoint: string)                => request<T>(endpoint),
  post:   <T>(endpoint: string, body: unknown) => request<T>(endpoint, { method: "POST",   body: JSON.stringify(body) }),
  put:    <T>(endpoint: string, body: unknown) => request<T>(endpoint, { method: "PUT",    body: JSON.stringify(body) }),
  patch:  <T>(endpoint: string, body: unknown) => request<T>(endpoint, { method: "PATCH",  body: JSON.stringify(body) }),
  delete: <T>(endpoint: string)               => request<T>(endpoint, { method: "DELETE" }),
};
