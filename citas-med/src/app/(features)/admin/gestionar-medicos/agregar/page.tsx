'use client'
import React, { useState } from "react";
import { addToast, Button, Input, Select, SelectItem, User } from "@heroui/react";
import api from "@/app/lib/axios";
import { Eye, FileText, Lock, Mail, Phone, User2 } from "lucide-react";
import Link from "next/link";
import { EyeFilledIcon, EyeSlashFilledIcon } from "@/app/ui/components/passwordEyes";

interface MedicoFormData {
    nombreCompleto: string;
    email: string;
    contrasena: string;
    documento: string;
    telefono: string;
    especialidad: string;
}

export default function AgregarMedicoPage() {
    const [form, setForm] = useState<MedicoFormData>({
        nombreCompleto: "",
        especialidad: "",
        telefono: "",
        email: "",
        contrasena: "",
        documento: "",
    });
    const [isPasswordVisible, setIsPasswordVisible] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const togglePasswordVisibility = () => setIsPasswordVisible(!isPasswordVisible);

    const handleChange = (field: keyof MedicoFormData, value: string) => {
        setForm((prev) => ({ ...prev, [field]: value }));
    };

    const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        console.log(form)
        setIsLoading(true);
        api.post("/admin/medicos/register", form)
        .then(() => {
            
                addToast({
                    title: "Éxito",
                    description: "Médico agregado correctamente",
                    color: "success",
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,

                });
                setForm({
                    nombreCompleto: "",
                    especialidad: "",
                    telefono: "",
                    email: "",
                    contrasena: "",
                    documento: "",
                });
            })
            .catch((error) => {
                addToast({
                    title: "Error",
                    description: error.message,
                    color: "danger",
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            })
            .finally(() => {
                setIsLoading(false);
            });
    };

    return (
        <div className="max-w-md mx-auto p-6 bg-white rounded-xl shadow-lg space-y-4">
            <h2 className="text-2xl font-bold text-center">Registro de Médico</h2>
            <Link
                href="/admin/gestionar-medicos"
                className="text-primary hover:underline text-sm flex items-center gap-1 mb-2"
            >
                ← Volver atrás
            </Link>
            <form onSubmit={handleSubmit} className="space-y-4">
                <Input
                    label="Nombre completo"
                    placeholder="Ingrese el nombre"
                    value={form.nombreCompleto}
                    onChange={(e) => handleChange("nombreCompleto", e.target.value)}
                    startContent={<User2 className="w-4 h-4 text-gray-500" />}
                    isClearable
                    onClear={() => handleChange("nombreCompleto", "")}
                    color="primary"
                    errorMessage="Campo requerido"
                    isRequired={true}
                />

                <Select
                    label="Especialidad"
                    placeholder="Seleccione una especialidad"
                    selectedKeys={form.especialidad ? [form.especialidad] : []}
                    onSelectionChange={(keys) =>
                        handleChange("especialidad", Array.from(keys)[0] as string)
                    }
                    color="primary"
                    isRequired={true}
                    errorMessage="Campo requerido"
                >
                    <SelectItem key="PEDIATRIA">Pediatría</SelectItem>
                    <SelectItem key="CARDIOLOGIA">Cardiología</SelectItem>
                    <SelectItem key="GENERAL">Medicina General</SelectItem>

                </Select>

                <Input
                    label="Teléfono"
                    placeholder="Ingrese el teléfono"
                    inputMode="numeric"
                    pattern="[0-9]*"
                    value={form.telefono}
                    onChange={(e) => handleChange("telefono", e.target.value)}
                    startContent={<Phone className="w-4 h-4 text-gray-500" />}
                    isClearable
                    onClear={() => handleChange("telefono", "")}
                    color="primary"
                    errorMessage="Campo requerido"
                    isRequired={true}
                />

                <Input
                    label="Correo electrónico"
                    placeholder="Ingrese el correo"
                    type="email"
                    value={form.email}
                    onChange={(e) => handleChange("email", e.target.value)}
                    startContent={<Mail className="w-4 h-4 text-gray-500" />}
                    isClearable
                    onClear={() => handleChange("email", "")}
                    color="primary"
                    errorMessage="Campo requerido"
                    isRequired={true}
                />

                <Input
                    label="Contraseña"
                    placeholder="Ingrese la contraseña"
                    value={form.contrasena}
                    onChange={(e) => handleChange("contrasena", e.target.value)}
                    startContent={<Lock className="w-4 h-4 text-gray-500" />}
                    color="primary"
                    type={isPasswordVisible ? "text" : "password"}
                    endContent={
                        <button
                            aria-label="toggle password visibility"
                            className="focus:outline-none"
                            type="button"
                            onClick={togglePasswordVisibility}
                        >
                            {isPasswordVisible ? <EyeSlashFilledIcon /> : <EyeFilledIcon />}
                        </button>
                    }
                    errorMessage="Campo requerido"
                    isRequired={true}
                />

                <Input
                    label="Documento"
                    placeholder="Número de documento"
                    value={form.documento}
                    onChange={(e) => handleChange("documento", e.target.value)}
                    startContent={<FileText className="w-4 h-4 text-gray-500" />}
                    isClearable
                    onClear={() => handleChange("documento", "")}
                    color="primary"
                    errorMessage="Campo requerido"
                    isRequired={true}
                />

                <Button
                    type="submit"
                    isLoading={isLoading}
                    className="p-6"
                    radius="lg"
                    color="primary"
                >
                    Registrar
                </Button>
            </form>

        </div>
    );
}