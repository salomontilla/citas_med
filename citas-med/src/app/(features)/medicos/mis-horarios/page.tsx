"use client"
import React, { useEffect, useState } from "react";
import { Select, SelectItem, Input, Button, addToast } from "@heroui/react";
import api from "@/app/lib/axios";
import { add } from "date-fns";

const diasSemana = [
    "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES", "SÁBADO", "DOMINGO"
];

type Horario = {
    id: number;
    diaSemana: string;
    horaInicio: string;
    horaFin: string;
};

export default function GestionHorarios() {
    const [horarios, setHorarios] = useState<Horario[]>([]);
    const [isEditando, setIsEditando] = useState<boolean>(false);

    const obtenerHorarios = () => {
        api.get("/medicos/mis-disponibilidades")
            .then((response) => {
                setHorarios(response.data.content);
                console.log("Horarios obtenidos:", response.data.content);
            })
            .catch((error) => {
                console.error("Error al obtener los horarios:", error);
            });
    };

    useEffect(() => {
        obtenerHorarios();
    }, []);

    // Agregar un nuevo horario con datos por defecto
    const agregarHorario = () => {
        setHorarios([
            ...horarios,
            {
                id: 0,
                diaSemana: "LUNES",
                horaInicio: "",
                horaFin: ""
            }
        ]);
    };

    //
    const actualizarHorario = (index: number, campo: string, valor: string) => {
        const nuevos = [...horarios];
        nuevos[index] = { ...nuevos[index], [campo]: valor };
        setHorarios(nuevos);
    };

    const handleEditarHorarios = (id: number, index: number) => {
        
        api.patch(`/medicos/editar-disponibilidades/${id}`, horarios[index])
            .then(() => {
                addToast({
                    title: "Horario actualizado",
                    description: "El horario se ha actualizado correctamente.",
                    color: "success",
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            })
            .catch((error) => {
                addToast({
                    title: "Error al actualizar horario",
                    description: "Ha ocurrido un error al actualizar el horario.",
                    color: "danger",
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            }); 
    };

    const handleEliminarHorario = (index: number) => {
        if (horarios[index].id === 0) {
            setHorarios(horarios.filter((_, i) => i !== index));
            return;
        }
        api.delete(`/medicos/eliminar-disponibilidad/${horarios[index].id}`)
            .then(() => {
                setHorarios(horarios.filter((_, i) => i !== index));
                addToast({
                    title: "Horario eliminado",
                    description: "El horario se ha eliminado correctamente.",
                    color: "success",
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            })
            .catch((error) => {
                addToast({
                    title: "Error al eliminar horario",
                    description: "Ha ocurrido un error al eliminar el horario.",
                    color: "danger",
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            });
    };

    function handleGuardarHorario(index: number): void {
        api.post("/medicos/registrar-disponibilidad", horarios[index])
            .then(() => {

                addToast({
                    title: "Horario guardado",
                    description: "El horario se ha guardado correctamente.",
                    color: "success",
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            })
            .catch((error) => {
                console.log(error)
                addToast({
                    title: "Error al guardar horario",
                    description: "Ha ocurrido un error al guardar el horario.",
                    color: "danger",
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            });
    }

    return (
        <div className="p-6 sm:p-10 space-y-6 max-w-3xl mx-auto bg-white rounded-3xl shadow-2xl border border-blue-200">
            <div className="flex items-center gap-3 mb-2">
                <span className="inline-block bg-blue-100 text-blue-700 rounded-full px-3 py-1 text-lg font-semibold shadow">
                    🩺
                </span>
                <h1 className="text-3xl font-extrabold text-blue-900 tracking-tight">
                    Gestionar Horarios
                </h1>
            </div>
            <p className="text-blue-700 mb-4">
                Añade y administra tus horarios disponibles para citas médicas.
            </p>

            {horarios.map((horario, index) => (
                <form
                    key={index}
                    className="flex flex-col sm:flex-row items-center gap-3 p-3"
                    onSubmit={(e) => {
                        e.preventDefault();
                        handleGuardarHorario(index);
                    }}
                >
                    <Select
                        isDisabled={!isEditando}
                        label="Día"
                        color="primary"
                        className="w-full sm:w-40"
                        selectedKeys={[horario.diaSemana]}
                        onChange={(e) => actualizarHorario(index, "diaSemana", e.target.value)}
                    >
                        {diasSemana.map((dia) => (
                            <SelectItem key={dia}>{dia}</SelectItem>
                        ))}
                    </Select>

                    <span className="font-medium hidden sm:inline">de</span>
                    <span className="font-medium sm:hidden">De</span>

                    <Input
                        isDisabled={!isEditando}
                        type="time"
                        size="lg"
                        className="w-full sm:w-32"
                        color="primary"
                        value={horario.horaInicio}
                        onChange={(e) => actualizarHorario(index, "horaInicio", e.target.value.concat(":00"))}
                        isRequired
                    />

                    <span className="font-medium hidden sm:inline">a</span>
                    <span className="font-medium sm:hidden">a</span>

                    <Input
                        isDisabled={!isEditando}
                        type="time"
                        color="primary"
                        size="lg"
                        className="w-full sm:w-32"
                        value={horario.horaFin}
                        onChange={(e) => actualizarHorario(index, "horaFin", e.target.value.concat(":00"))}
                        isRequired
                    />

                    <div className="flex gap-2 mt-2 sm:mt-0">
                        {
                            isEditando ? 
                                <Button 
                                color="warning" 
                                onPress={() => handleEditarHorarios(horario.id, index)} 
                                className="w-full sm:w-auto"
                                >
                                    Editar
                                </Button> :
                                <Button
                                    color="primary"
                                    type="submit"
                                    className="w-full sm:w-auto"
                                    isDisabled={horario.id !== 0}
                                >
                                    Guardar
                                </Button>
                        }
                        <Button
                            color="danger"
                            onPress={() => handleEliminarHorario(index)}
                            className="w-full sm:w-auto"
                        >
                            Eliminar
                        </Button>
                    </div>
                </form>
            ))}
            <div className="flex flex-col sm:flex-row items-center gap-3">
                <Button color="primary" onPress={() => agregarHorario()} className="w-full sm:w-auto">
                    Agregar horario
                </Button>
                <Button color="warning" onPress={() => setIsEditando(!isEditando)} className="w-full sm:w-auto">
                    Editar Horarios
                </Button>
            </div>
        </div>
    );
}
