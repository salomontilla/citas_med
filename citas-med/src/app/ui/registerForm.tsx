'use client';
import { Button, DatePicker } from "@heroui/react";
import React, { useState } from "react";

export default function RegisterForm() {
  const [form, setForm] = useState({
    nombre: "",
    correo: "",
    cedula: "",
    telefono: "",
    fechaNacimiento: "",
    password: "",
    confirmPassword: "",
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (form.password !== form.confirmPassword) {
      alert("Las contraseñas no coinciden");
      return;
    }

    // Aquí iría la lógica para enviar datos al backend
    console.log("Registrando usuario:", form);
  };

  return (
    <form onSubmit={handleSubmit} className="grid grid-cols-2 gap-4 items-end ">
      <div>
        <label htmlFor="nombre" className="font-semibold">Nombre</label>
        <input
          type="text"
          id="nombre"
          name="nombre"
          value={form.nombre}
          onChange={handleChange}
          required
          
          placeholder="Tu nombre completo"
        />
      </div>

      <div>
        <label htmlFor="correo" className="block font-semibold ">Correo electrónico</label>
        <input
          type="email"
          id="correo"
          name="correo"
          value={form.correo}
          onChange={handleChange}
          required
          
          placeholder="ejemplo@correo.com"
        />
      </div>

      <div>
        <label htmlFor="cedula" className="block font-semibold">Cédula</label>
        <input
          type="text"
          id="cedula"
          name="cedula"
          value={form.cedula}
          onChange={handleChange}
          required
          placeholder="Número de cédula"
        />
      </div>

      <div>
        <label htmlFor="telefono" className="block font-semibold ">Número telefónico</label>
        <input
          type="tel"
          id="telefono"
          name="telefono"
          value={form.telefono}
          onChange={handleChange}
          required
          
          placeholder="Ej: 3001234567"
        />
      </div>

      <div>
        <label htmlFor="password" className="block font-semibold ">Fecha de nacimiento</label>
        <DatePicker size="lg" color="primary" className="max-w-[284px] rounded-2xl"/>
      </div>

      <div>
        <label htmlFor="password" className="block font-semibold ">Contraseña</label>
        <input
          type="password"
          id="password"
          name="password"
          value={form.password}
          onChange={handleChange}
          required
          
          placeholder="********"
        />
      </div>

      <div>
        <label htmlFor="confirmPassword" className="block font-semibold ">Confirmar contraseña</label>
        <input
          type="password"
          id="confirmPassword"
          name="confirmPassword"
          value={form.confirmPassword}
          onChange={handleChange}
          required
          placeholder="********"
        />
      </div>

       <Button
      className="p-6"
      radius="lg"
      color="primary"
    >
      Crear cuenta
    </Button>
      <style>
        {
          `input{
            background-color: #eff6ff;
            width: 100%;
            border-radius: 16px;
            padding: 13px;
          }`
        }
      </style>
    </form>
  );
}
