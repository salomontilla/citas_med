'use client'
import Navbar from '../ui/components/navbar';
import { Button, Input } from "@heroui/react";
import React, { useState } from "react";
import { useRouter } from "next/navigation";
import axios from 'axios';
import api from '../lib/axios'; 

export default function LoginPage() {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [errorMsg, setErrorMsg] = useState("");
    const router = useRouter();


    const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {

      const res = await api.post("/auth/login", {
        email: email,
        contrasena: password,
      });

      const rol = res.data.rol;

    // Redirige según el rol
    if (rol === "ADMIN") {
      router.push("dashboard/admin");
    } else if (rol === "PACIENTE") {
      router.push("dashboard/pacientes");
    } else if (rol === "MEDICO") {
      router.push("dashboard/medicos");
    }

    } catch (err: any) {
      const msg = err.response.data || "Error al iniciar sesión";
      setErrorMsg(msg);
    }
  };

    return (
        <div className="relative w-full h-screen flex flex-col items-center justify-center">
            <header className="bg-blue-50 absolute top-0 left-0 w-full z-20">
                <Navbar />

            </header>

  
            <div className="fondo-lineas">
                {[...Array(10)].map((_, i) => (
                <div
                    key={i}
                    className="linea"
                    style={{
                    left: `${i * 10}%`,
                    animationDelay: `${i * 0.4}s`,
                    }}
                />
                ))}
            </div>
            <div className="relative z-10 flex items-center justify-center min-h-fit w-full">
                <div className="w-[80%] md:w-full max-w-sm p-8 bg-white rounded-2xl shadow-md">
                    <h2 className="mb-6 text-2xl font-bold text-center">Iniciar Sesión</h2>
                    <form onSubmit={handleLogin}>
                        <div className="mb-4 w-full">
                            <Input
                                      value={email}
                                      onValueChange={setEmail}
                                      label="Ingresa tu correo electrónico"
                                      labelPlacement="inside"
                                      color="primary"
                                      size="md"
                                      type="text"
                                      errorMessage="Campo requerido"
                                      required
                                    />
                        </div>
                        <div className="mb-4">
                            <Input
                                      value={password}
                                      onValueChange={setPassword}
                                      label="Ingresa tu contraseña"
                                      labelPlacement="inside"
                                      color="primary"
                                      size="md"
                                      type="password"
                                      isInvalid={!!errorMsg}
                                      errorMessage={errorMsg || "Campo requerido"}
                                      required
                                    />
                            <a href="#" className="text-sm text-blue-600 hover:underline">¿Olvidaste tu contraseña?</a>
                        </div>
                        <Button
                                type="submit"
                                className="p-6 w-full"
                                radius="lg"
                                color="primary"
                              >
                                Iniciar Sesión
                              </Button>
                    </form>
                </div>
            </div>
            <style>
                {`
                    .fondo-lineas {
                        position: absolute;
                        inset: 0;
                        background-color: #03045e; 
                        overflow: hidden;
                        z-index: 0;
                    }   

                    .fondo-lineas .linea {
                        position: absolute;
                        top: 0;
                        width: 1px;
                        height: 100%;
                        background-color: rgba(255, 255, 255, 0.1);
                        animation: mover-linea 6s linear infinite;
                    }

                    @keyframes mover-linea {
                    0% {
                        transform: translateY(100%);
                    }
                    100% {
                        transform: translateY(-100%);
                    }
                    }
                `}
            </style>
        </div>
        
    );
}
