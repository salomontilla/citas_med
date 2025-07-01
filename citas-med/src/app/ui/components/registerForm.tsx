'use client';
import { Button, DatePicker, Input, Alert } from "@heroui/react";
import React, { BaseSyntheticEvent, useState } from "react";
import { CalendarDate, today, getLocalTimeZone } from "@internationalized/date";
import { EyeFilledIcon, EyeSlashFilledIcon } from "./passwordEyes";
import axios from 'axios';
import { useRouter } from "next/navigation";

export default function RegisterForm() {
  const [isVisible, setIsVisible] = React.useState(false);
  const [name, setName] = useState("");
  const [id, setId] = useState("");
  const [correo, setCorreo] = useState("");
  const [telefono, setTelefono] = useState("");
  const [contra, setContra] = useState("");
  const [confirm, setConfirm] = useState("");
  const [fecha, setFecha] = useState<CalendarDate | null>(null);
  const [isVisibleAlert, setIsVisibleAlert] = React.useState(false);
  const [isBadRequest, setIsBadRequest] = React.useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [description, setDescription] = useState("");
  const [title, setTitle] = useState("");

  const toggleVisibility = () => setIsVisible(!isVisible);
  const errors = []
  const router = useRouter();
  

  
  
  if (contra !== confirm) {
    errors.push("las contraseñas no coinciden");
  }

  const handleSubmit = (e:BaseSyntheticEvent) => {
    e.preventDefault();
    setIsLoading(false);
    setIsBadRequest(false);
    setIsVisibleAlert(false);
    setDescription("");
    setTitle("");
    

    console.log(name, id, correo, telefono, contra, confirm, fecha);

    axios.post('http://localhost:8080/api/citasmed/pacientes/register', {
      nombreCompleto: name,
      email: correo,
      contrasena: contra,
      telefono: telefono,
      documento: id,
      fechaNacimiento: fecha,
    })
    .then(() => {

      setTitle("Registro exitoso");
      setDescription("Cuenta creada exitosamente!");
      setIsVisibleAlert(true);
      setIsLoading(true);

      setTimeout(()=>{
        router.push("/login");
      }, 200)
      

    }).catch(error => {
      setIsBadRequest(true);
      setIsVisibleAlert(true);
      setIsLoading(false);
      setTitle("Error al crear cuenta");
      setDescription(error.response.data);
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
    errorMessage={errors[0]}
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
        isLoading={isLoading}
        className="p-6"
        radius="lg"
        color="primary"
      >
        Crear cuenta
      </Button>
      <Alert
          color={isBadRequest ? "danger" : "success"}
          className="w-full col-span-1 md:col-span-2"
          description={description}
          title={title}
          isVisible={isVisibleAlert}
          variant="faded"
          onClose={() => setIsVisibleAlert(false)}
        />
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
