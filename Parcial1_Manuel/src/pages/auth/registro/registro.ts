import "../login/login.css";
import { api } from "../../../api/api";
import { navigate } from "../../../utils/navigate";

const form = document.getElementById("form") as HTMLFormElement;
const inputNombre = document.getElementById("nombre") as HTMLInputElement;
const inputApellido = document.getElementById("apellido") as HTMLInputElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
const inputCelular = document.getElementById("celular") as HTMLInputElement;
const inputPassword = document.getElementById("password") as HTMLInputElement;
const btnRegister = document.getElementById("btnRegister") as HTMLButtonElement;
const errorMsg = document.getElementById("errorMsg") as HTMLDivElement;
const successMsg = document.getElementById("successMsg") as HTMLDivElement;

const showError = (msg: string) => {
  errorMsg.textContent = msg;
  errorMsg.style.display = "block";
  successMsg.style.display = "none";
};

const showSuccess = (msg: string) => {
  successMsg.textContent = msg;
  successMsg.style.display = "block";
  errorMsg.style.display = "none";
};

const setLoading = (loading: boolean) => {
  btnRegister.disabled = loading;
  btnRegister.textContent = loading ? "Creando cuenta..." : "Crear cuenta";
};

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  errorMsg.style.display = "none";
  successMsg.style.display = "none";

  const nombre = inputNombre.value.trim();
  const apellido = inputApellido.value.trim();
  const mail = inputEmail.value.trim().toLowerCase();
  const celular = inputCelular.value.trim();
  const contrasenia = inputPassword.value;

  if (!nombre || !apellido || !mail || !celular || !contrasenia) {
    showError("Completá todos los campos para continuar.");
    return;
  }

  if (contrasenia.length < 6) {
    showError("La contraseña debe tener al menos 6 caracteres.");
    return;
  }

  setLoading(true);
  try {
    await api.post("/auth/register", { nombre, apellido, mail, celular, contrasenia });
    showSuccess("¡Cuenta creada exitosamente! Redirigiendo al login...");
    setTimeout(() => navigate("../login/login.html"), 1800);
  } catch (err) {
    showError(err instanceof Error ? err.message : "No se pudo crear la cuenta.");
  } finally {
    setLoading(false);
  }
});
