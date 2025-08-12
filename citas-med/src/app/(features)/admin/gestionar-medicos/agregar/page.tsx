'use client'
import React, { useState } from "react";
import { addToast, Button, Input, Select, SelectItem, User } from "@heroui/react";
import api from "@/app/lib/axios";
import { FileText, Lock, Mail, Phone, User2 } from "lucide-react";

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

    const handleChange = (field: keyof MedicoFormData, value: string) => {
    setForm((prev) => ({ ...prev, [field]: value }));
  };

    const handleSubmit = () => {
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
            });
    };

    return (
            <div className="max-w-md mx-auto p-6 bg-white rounded-xl shadow-lg space-y-4">
                <h2 className="text-2xl font-bold text-center">Registro de Médico</h2>

                <Input
                    label="Nombre completo"
                    placeholder="Ingrese el nombre"
                    value={form.nombreCompleto}
                    onChange={(e) => handleChange("nombreCompleto", e.target.value)}
                    startContent={<User2 className="w-4 h-4 text-gray-500" />}
                    isClearable
                />

                <Select
                    label="Especialidad"
                    placeholder="Seleccione una especialidad"
                    selectedKeys={form.especialidad ? [form.especialidad] : []}
                    onSelectionChange={(keys) =>
                        handleChange("especialidad", Array.from(keys)[0] as string)
                    }
                >
                    <SelectItem key="pediatria">Pediatría</SelectItem>
                    <SelectItem key="cardiologia">Cardiología</SelectItem>
                    <SelectItem key="dermatologia">Dermatología</SelectItem>
                </Select>

                <Input
                    label="Teléfono"
                    placeholder="Ingrese el teléfono"
                    type="tel"
                    value={form.telefono}
                    onChange={(e) => handleChange("telefono", e.target.value)}
                    startContent={<Phone className="w-4 h-4 text-gray-500" />}
                    isClearable
                />

                <Input
                    label="Correo electrónico"
                    placeholder="Ingrese el correo"
                    type="email"
                    value={form.email}
                    onChange={(e) => handleChange("email", e.target.value)}
                    startContent={<Mail className="w-4 h-4 text-gray-500" />}
                    isClearable
                />

                <Input
                    label="Contraseña"
                    placeholder="Ingrese la contraseña"
                    type="password"
                    value={form.contrasena}
                    onChange={(e) => handleChange("contrasena", e.target.value)}
                    startContent={<Lock className="w-4 h-4 text-gray-500" />}
                />

                <Input
                    label="Documento"
                    placeholder="Número de documento"
                    value={form.documento}
                    onChange={(e) => handleChange("documento", e.target.value)}
                    startContent={<FileText className="w-4 h-4 text-gray-500" />}
                    isClearable
                />

                <Button
                    className="w-full"
                    color="primary"
                    onPress={() => handleSubmit()}
                >
                    Registrar
                </Button>
            </div>
        );
}