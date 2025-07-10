import axios from "axios";
import { showSessionExpiredModal } from "@/app/store/tokenStore"; // un store que crearemos

const api = axios.create({
  baseURL: "http://localhost:8080/api/citasmed",
  withCredentials: true,
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response.status === 403 || error.response.status === 401) {
      // Mostrar modal
      showSessionExpiredModal();
    }

    return Promise.reject(error);
  }
);

export default api;
