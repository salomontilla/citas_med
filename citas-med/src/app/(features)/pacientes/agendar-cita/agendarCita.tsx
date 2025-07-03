'use client';

import { useState } from "react";
import { CalendarDays, Clock4 } from "lucide-react";
import { CalendarDate, DateValue, getLocalTimeZone, today } from "@internationalized/date";
import { DatePicker } from "@heroui/react";

// Simulamos la disponibilidad por día de la semana
const DISPONIBILIDADES = [
    { id: 1, diaSemana: "LUNES", horaInicio: "08:00", horaFin: "10:00" },
    { id: 2, diaSemana: "LUNES", horaInicio: "14:00", horaFin: "16:00" },
    { id: 3, diaSemana: "MARTES", horaInicio: "10:00", horaFin: "11:00" },
    { id: 4, diaSemana: "MIÉRCOLES", horaInicio: "12:00", horaFin: "13:00" },
];

const diasSemanaMap = [
    "DOMINGO", "LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO"
];

const mesesMap = [
    "ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO",
    "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"
];

export default function SeleccionHorarioConFecha() {
    const [fechaSeleccionada, setFechaSeleccionada] = useState<CalendarDate | null>(null);
    const [bloqueSeleccionado, setBloqueSeleccionado] = useState<number | null>(null);

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
    const horariosFiltrados = DISPONIBILIDADES.filter((d) => d.diaSemana === diaSemana);

    return (
        <div className="space-y-6">
            {/* SELECCIÓN DE FECHA */}
            <div className="flex items-center gap-2 text-blue-800">
                <CalendarDays className="w-6 h-6" />
                <h2 className="text-xl font-bold">Selecciona una fecha para agendar</h2>
            </div>

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

            {/* HORARIOS DISPONIBLES */}
            {fechaSeleccionada && (
                <div>
                    <h3 className="text-lg text-blue-800 font-semibold mb-2">
                        Horarios disponibles para el{" "}
                        <span className="inline-block bg-blue-100 text-blue-900 px-2 py-1 rounded-lg font-bold">
                            {diaSemana?.toLowerCase()} {fechaSeleccionada.day} de {mes?.toLowerCase()}
                        </span>
                    </h3>

                    <div className="flex flex-wrap gap-3">
                        {horariosFiltrados.length > 0 ? horariosFiltrados.map((bloque) => (
                            <button
                                key={bloque.id}
                                onClick={() => setBloqueSeleccionado(bloque.id)}
                                className={`px-4 py-2 rounded-lg border text-sm flex items-center gap-2 transition-all duration-200 ${bloqueSeleccionado === bloque.id
                                    ? "bg-blue-600 text-white border-blue-700"
                                    : "bg-white text-blue-700 border-blue-300 hover:bg-blue-100"
                                    }`}
                            >
                                <Clock4 className="w-4 h-4" />
                                {bloque.horaInicio} - {bloque.horaFin}
                            </button>
                        )) : (
                            <p className="text-sm text-gray-500">No hay horarios disponibles para este día.</p>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}
