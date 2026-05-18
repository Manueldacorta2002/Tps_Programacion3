import "./login.css";
import type { IUser } from "../../../types/IUser";
import type { Rol } from "../../../types/Rol";
import { navigate } from "../../../utils/navigate";
import { saveUser } from "../../../utils/localStorage";

const form = document.getElementById("form") as HTMLFormElement;
const inputEmail = document.getElementById("email") as HTMLInputElement;
//const inputPassword = document.getElementById("password") as HTMLInputElement;
const inputRol = document.getElementById("rol") as HTMLInputElement;
const roleButtons = document.querySelectorAll(".role-btn");

/* MEJORA: selector visual que mantiene el valor en el input oculto #rol */
const selectRol = (button: Element) => {
  roleButtons.forEach((currentButton) => currentButton.classList.remove("active"));
  button.classList.add("active");
  inputRol.value = (button as HTMLButtonElement).dataset.role as Rol;
};

roleButtons.forEach((button) => {
  button.addEventListener("click", () => {
    selectRol(button);
  });
});

form.addEventListener("submit", (e: SubmitEvent) => {
  e.preventDefault();
  const valueEmail = inputEmail.value.trim().toLowerCase();
  //const valuePassword = inputPassword.value;
  const valueRol = inputRol.value as Rol;

  const user: IUser = {
    email: valueEmail,
    role: valueRol,
    loggedIn: true,
  };

  saveUser(user);

  if (valueRol === "admin") {
    navigate("/src/pages/admin/home/home.html");
  } else if (valueRol === "client") {
    navigate("/src/pages/store/home/home.html");
  }
});
