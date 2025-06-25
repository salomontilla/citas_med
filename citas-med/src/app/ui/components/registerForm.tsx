'use client';
import { Button, DatePicker, Input } from "@heroui/react";
import React, { BaseSyntheticEvent, useState } from "react";
import { CalendarDate, today, getLocalTimeZone } from "@internationalized/date";
import { EyeFilledIcon, EyeSlashFilledIcon } from "./passwordEyes";
import axios from 'axios';

export default function RegisterForm() {
  const [isVisible, setIsVisible] = React.useState(false);
  const [name, setName] = useState("");
  const [id, setId] = useState("");
  const [correo, setCorreo] = useState("");
  const [telefono, setTelefono] = useState("");
  const [contra, setContra] = useState("");
  const [confirm, setConfirm] = useState("");
  const [fecha, setFecha] = useState<CalendarDate | null>(null);

  const toggleVisibility = () => setIsVisible(!isVisible);
  const errors = []

  const fechaFormateada = `${fecha?.day.toString().padStart(2, '0')}-${fecha?.month.toString().padStart(2, '0')}-${fecha?.year}`;
  
  if (contra !== confirm) {
    errors.push("las contraseñas no coinciden");
  }

  const handleSubmit = (e:BaseSyntheticEvent) => {
    e.preventDefault();
    console.log(name, id, correo, telefono, contra, confirm, fechaFormateada);

    axios.post('http://localhost:8080/api/citasmed/pacientes/register', {
      nombreCompleto: name,
      email: correo,
      contrasena: contra,
      telefono: telefono,
      documento: id,
      fechaNacimiento: fechaFormateada?.toString(),
    })
    .then(response => {
      console.log('Registro exitoso:', response.data);
      // Aquí puedes manejar la respuesta del servidor, como redirigir al usuario o mostrar un mensaje de éxito
    }).catch(error => {
      console.error('Error al registrar:', error);
      // Aquí puedes manejar el error, como mostrar un mensaje de error al usuario
    })

    
  };

 const passwordValidation = () => {
  if (contra.length === 0) {
    return "Campo requerido";
  }
  return "";
};



  return (
    <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 3xl:grid-cols-3 gap-4 items-end ">
      <div> 
        <Input
          value={name}
          onValueChange={setName}
          label="Nombres y apellidos"
          labelPlacement="outside"
          color="primary"
          size="lg"
          type="text"
          errorMessage="Campo requerido"
          required
        />
      </div>

      <div>
        <Input

          onValueChange={setCorreo}

          label="Correo electrónico"
          labelPlacement="outside"
          color="primary"
          size="lg"
          type="email"
          errorMessage="Campo requerido o inválido"
          required
        />
      </div>

      <div>
        <Input
          onValueChange={setId}


          inputMode="numeric"
          pattern="[0-9]*"
          label="Número de documento"
          labelPlacement="outside"
          color="primary"
          size="lg"
          type="number"
          errorMessage="Campo requerido"
          required
        />
      </div>

      <div>
        <Input
          onValueChange={setTelefono}

          inputMode="numeric"
          pattern="[0-9]*"
          label="Número de teléfono"
          labelPlacement="outside"
          color="primary"
          size="lg"
          type="number"
          errorMessage="Campo requerido"
          required
        />
      </div>
      <div>
  <Input
    size="lg"
    color="primary"
    onValueChange={setContra}
    required
    errorMessage={passwordValidation()}
    endContent={
      <button
        aria-label="toggle password visibility"
        className="focus:outline-none"
        type="button"
        onClick={toggleVisibility}
      >
        {isVisible ? <EyeSlashFilledIcon /> : <EyeFilledIcon />}
      </button>
    }
    label="Contraseña"
    labelPlacement="outside"
    type={isVisible ? "text" : "password"}
  />
</div>

<div>
  <Input
    onValueChange={setConfirm}
    label="Confirmar contraseña"
    labelPlacement="outside"
    color="primary"
    size="lg"
    type="password"
    required
    isInvalid={contra !== confirm}
    errorMessage={
      errors.map((error, index) => (
        <li key={index} >{error}</li>
      ))
    }
  />
</div>

      <div>
        <DatePicker
          showMonthAndYearPickers
          color="primary"
          value={fecha}
          onChange={(value) => setFecha(value)}
          size="lg"
          label="Fecha de nacimiento"
          labelPlacement="inside"
          isRequired
          className="rounded-2xl" 
          errorMessage="Campo requerido"
          maxValue={today(getLocalTimeZone())}/>
          
          
      </div>

      <Button
        type="submit"

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
