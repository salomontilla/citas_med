'use client';

import { Table, TableBody, TableCell, TableColumn, TableHeader, TableRow, Card, Button, Tooltip } from "@heroui/react";
import { CalendarDays, Clock4, UserCircle2, Stethoscope, Pencil, XCircle } from "lucide-react";

// Simulación de citas (deberías traerlas del backend)
const citas = [
  {
    id: 1,
    medico: "Dra. Carolina Pérez",
    especialidad: "Cardiología",
    fecha: "2025-07-10",
    hora: "10:00",
    estado: "AGENDADA"
  },
  {
    id: 2,
    medico: "Dr. Juan López",
    especialidad: "Dermatología",
    fecha: "2025-07-15",
    hora: "08:30",
    estado: "CANCELADA"
  }
];

export default function MisCitasSection() {
  const editarCita = (id: number) => {
    console.log("Editar cita ID:", id);
    // Aquí abres un modal o rediriges al formulario de edición
  };

  const cancelarCita = (id: number) => {
    console.log("Cancelar cita ID:", id);
    // Aquí llamas a tu endpoint de cancelación
  };
  
  return (
    <section className="p-6 bg-white rounded-2xl shadow-xl border border-blue-200">
      <h2 className="text-2xl font-bold text-blue-800 mb-2 flex items-center gap-2">
        <CalendarDays className="w-6 h-6" />
        Mis Citas Médicas
      </h2>
      <p className="text-blue-700 mb-6 text-sm">
        Consulta el historial y estado de tus próximas citas médicas.
      </p>

      
        <Table
      isStriped
      aria-label="Tabla editable de citas médicas"
    >
      <TableHeader>
        <TableColumn>Fecha</TableColumn>
        <TableColumn>Hora</TableColumn>
        <TableColumn>Médico</TableColumn>
        <TableColumn>Especialidad</TableColumn>
        <TableColumn>Estado</TableColumn>
        <TableColumn>Acciones</TableColumn>
      </TableHeader>
      <TableBody emptyContent="No tienes citas registradas.">
        {citas.map((cita) => (
          <TableRow key={cita.id}>
            <TableCell>
              <div className="flex items-center gap-2 text-blue-800 font-medium">
                <CalendarDays className="w-4 h-4" />
                {cita.fecha}
              </div>
            </TableCell>
            <TableCell>
              <div className="flex items-center gap-2 text-blue-700">
                <Clock4 className="w-4 h-4" />
                {cita.hora}
              </div>
            </TableCell>
            <TableCell>
              <div className="flex items-center gap-2">
                <UserCircle2 className="w-4 h-4 text-blue-700" />
                {cita.medico}
              </div>
            </TableCell>
            <TableCell>
              <div className="flex items-center gap-2">
                <Stethoscope className="w-4 h-4 text-blue-700" />
                {cita.especialidad}
              </div>
            </TableCell>
            <TableCell>
                  <span className={`text-xs font-semibold px-2 py-1 rounded-lg ${
                    cita.estado === "AGENDADA"
                      ? "bg-green-100 text-green-700"
                      : cita.estado === "CANCELADA"
                      ? "bg-red-100 text-red-700"
                      : "bg-gray-100 text-gray-700"
                  }`}>
                    {cita.estado}
                  </span>
                </TableCell>
            <TableCell>
              <div className="flex gap-2">
                <Tooltip content="Editar cita" placement="top">
                  <Button
                    size="sm"
                    color="primary"
                    onPress={() => editarCita(cita.id)}
                    isIconOnly
                    aria-label="Editar"
                  >
                    <Pencil className="w-4 h-4" />
                  </Button>
                </Tooltip>
                <Tooltip content="Cancelar cita" placement="top">
                  <Button
                    size="sm"
                    color="danger"
                    onPress={() => cancelarCita(cita.id)}
                    isIconOnly
                    aria-label="Cancelar"
                  >
                    <XCircle className="w-4 h-4" />
                  </Button>
                </Tooltip>
              </div>
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
      
    </section>
  );
}
