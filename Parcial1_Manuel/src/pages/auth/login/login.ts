import "./login.css";
import { api } from "../../../api/api";
import type { IUser } from "../../../types/IUser";
import { saveUser } from "../../../utils/localStorage";
import { navigate } from "../../../utils/navigate";

const form = document.getElementById("form") as HTMLFormElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
const inputPassword = document.getElementById("password") as HTMLInputElement;
const btnLogin = document.getElementById("btnLogin") as HTMLButtonElement;
const errorMsg = document.getElementById("errorMsg") as HTMLDivElement;

const showError = (msg: string) => {
  errorMsg.textContent = msg;
  errorMsg.style.display = "block";
};

const setLoading = (loading: boolean) => {
  btnLogin.disabled = loading;
  btnLogin.textContent = loading ? "Ingresando..." : "Ingresar";
};

form.addEventListener("submit", async (e: SubmitEvent) => {
  e.preventDefault();
  errorMsg.style.display = "none";

  const mail = inputEmail.value.trim().toLowerCase();
  const contrasenia = inputPassword.value;

  if (!mail || !contrasenia) {
    showError("Completá email y contraseña para continuar.");
    return;
  }

  setLoading(true);
  try {
    const user = await api.post<IUser>("/auth/login", { mail, contrasenia });
    saveUser(user);
    if (user.rol === "ADMIN") {
      navigate("/src/pages/admin/home/home.html");
    } else {
      navigate("/src/pages/store/home/home.html");
    }
  } catch (err) {
    showError(err instanceof Error ? err.message : "Credenciales incorrectas.");
  } finally {
    setLoading(false);
  }
});

