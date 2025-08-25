'use client';

import { use, useEffect, useState } from "react";
import { CalendarDays, Clock4 } from "lucide-react";
import { CalendarDate, getLocalTimeZone, today } from "@internationalized/date";
import { Button, DatePicker } from "@heroui/react";
import { formatearFecha } from "@/app/lib/utils";
import { useMedicoStore } from "@/app/store/medicoStore";
import ConfirmacionCita from "./confirmacionCita";
import api from "@/app/lib/axios";


const diasSemanaMap = [
    "DOMINGO", "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO"
];

const mesesMap = [
    "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO",
    "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"
];

export default function SeleccionHorarioConFecha() {
    const { medicoSeleccionado, limpiarMedico } = useMedicoStore();
    const [fechaSeleccionada, setFechaSeleccionada] = useState<CalendarDate | null>(null);
    const [bloqueSeleccionado, setBloqueSeleccionado] = useState<string | null>(null);
    const [disponibilidades, setDisponibilidades] = useState<string[]>([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        cargarDisponibilidades(
            fechaSeleccionada,
            medicoSeleccionado?.id ?? null,
            setDisponibilidades,
            setLoading,
            setError
        );
    }, [fechaSeleccionada, medicoSeleccionado]);


    // Función auxiliar para obtener el nombre del día
    const obtenerDiaSemana = (fecha: CalendarDate | null): string | null => {
        if (!fecha) return null;
        const jsDate = new Date(fecha.year, fecha.month - 1, fecha.day);
        return diasSemanaMap[jsDate.getDay()];
    };

    // Función auxiliar para obtener el mes
    const obtenerMes = (fecha: CalendarDate | null): string | null => {
        if (!fecha) return null;
        const jsDate = new Date(fecha.year, fecha.month - 1, fecha.day);
        return mesesMap[jsDate.getMonth()];
    }

    const diaSemana = obtenerDiaSemana(fechaSeleccionada);
    const mes = obtenerMes(fechaSeleccionada);



    return (
        <div className="space-y-6">
            {/* SELECCIÓN DE FECHA */}
            <div className="flex items-center gap-2 text-blue-800">
                <CalendarDays className="w-6 h-6" />
                <h2 className="text-xl font-bold">Selecciona una fecha para agendar</h2>
            </div>
            {
                medicoSeleccionado && (
                    <DatePicker
                        color="primary"
                        label="Fecha"
                        value={fechaSeleccionada}
                        onChange={setFechaSeleccionada}
                        labelPlacement="outside"
                        className="w-fit"
                        minValue={today(getLocalTimeZone())}
                        showMonthAndYearPickers
                        isRequired
                    />
                )
            }

            {/* HORARIOS DISPONIBLES */}
            {fechaSeleccionada && (
                <div>
                    <h3 className="text-lg text-blue-800 font-semibold mb-2">
                        Horarios disponibles para el{" "}
                        <span className="inline-block bg-blue-100 text-blue-900 px-2 py-1 rounded-lg font-bold">
                            {diaSemana?.toLowerCase()} {fechaSeleccionada.day} de {mes?.toLowerCase()}
                        </span>
                    </h3>

                    <div className="flex flex-wrap items-center justify-center gap-3">
                        {disponibilidades.length > 0 ? (
                            disponibilidades.map((hora, index) => {
                                const bloqueId = `${index}-${hora}`;

                                return (
                                    <Button
                                        key={bloqueId}
                                        onPress={() => setBloqueSeleccionado(bloqueId)}
                                        className={`px-4 py-2 rounded-lg border text-sm flex items-center gap-2 ${bloqueSeleccionado === bloqueId
                                            ? "bg-blue-600 text-white border-blue-700"
                                            : "bg-white text-blue-700 border-blue-300 hover:bg-blue-100"
                                            }`}
                                    >
                                        <Clock4 className="w-4 h-4" />
                                        {hora}
                                    </Button>
                                );
                            })

                        ) : (
                            <p className="text-sm text-gray-500">
                                No hay horarios disponibles para este día.
                            </p>
                        )}
                    </div>

                </div>
            )}
            <ConfirmacionCita
                fechaSeleccionada={formatearFecha(fechaSeleccionada)}
                horaInicio={bloqueSeleccionado}
                onCitaAgendada={() => {
                    setFechaSeleccionada(null);
                    setBloqueSeleccionado(null);
                    limpiarMedico();
                    cargarDisponibilidades(
                        null,
                        null,
                        setDisponibilidades,
                        setLoading,
                        setError
                    );
                }
                }

            />
        </div>
    );
}
export const cargarDisponibilidades = (
    fechaSeleccionada: CalendarDate | null,
    medicoSeleccionado: number | null,
    setDisponibilidades: (disponibilidades: string[]) => void,
    setLoading: (loading: boolean) => void,
    setError: (error: string | null) => void
) => {
        if (fechaSeleccionada && medicoSeleccionado) {
            setLoading(true);
            api.get(`/pacientes/${medicoSeleccionado}/disponibilidades?fecha=${fechaSeleccionada?.toString()}`)
                .then((response) => {
                    setDisponibilidades(response.data || []);
                })
                .catch((error) => {
                    setError(error.response?.data || "Error al cargar las disponibilidades");
                })
                .finally(() => setLoading(false));
        }
    }
