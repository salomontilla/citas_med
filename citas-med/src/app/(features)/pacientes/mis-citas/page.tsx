'use client';

import { Table, TableBody, TableCell, TableColumn, TableHeader, TableRow, Card, Button, Tooltip, Pagination, Spinner } from "@heroui/react";
import { CalendarDays, Clock4, UserCircle2, Stethoscope, Pencil, XCircle } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { Key } from "@react-types/shared";
import api from "@/app/lib/axios";

type Cita = {
  id: string;
  nombreMedico: string;
  especialidad: string;
  fecha: string;
  hora: string;
  estado: "PENDIENTE" | "CANCELADA" | "CONFIRMADA" | "ATENDIDA";
};


export default function MisCitasSection() {

  const [page, setPage] = useState(1);
  const [loading, setIsLoading] = useState(true);
  const [citas, setCitas] = useState<Cita[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const rowsPerPage = 5;

  const obtenerCitas = () => {
    api.get(`/pacientes/mis-citas?page=${page - 1}`)
      .then((response) => {
        setCitas(response.data.content);
        setTotalPages(response.data.totalElements);
        console.log(`ENDPOINT /pacientes/mis-citas?page=:${page - 1}`);

      })
      .catch((error) => {
        console.error("Error al cargar las citas:", error);
      })
      .finally(() => {
        setIsLoading(false)
      });
  }
  // Cargar citas al montar el componente y al cambiar de página
  useEffect(() => {
    obtenerCitas();
    console.log("Pagina actual: ", page);
  }, [page]);

  const pages = Math.ceil(totalPages / rowsPerPage);

  const items = useMemo(() => {
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;

    return citas.slice(start, end);
  }, [page, citas])


  const editarCita = (id: number) => {
    console.log("Editar cita ID:", id);
    // Aquí abres un modal o rediriges al formulario de edición
  };

  const cancelarCita = (id: number) => {
    console.log("Cancelar cita ID:", id);
    // Aquí llamas a tu endpoint de cancelación
  };
  const loadingState = loading || citas.length === 0 ? "loading" : "idle";

  function renderCell(item: Cita, columnKey: Key): React.ReactNode {
    const cellValue = item[columnKey as keyof typeof item];
    switch (columnKey) {
      case "fecha":
        return (
          <div className="flex items-center gap-2 text-blue-800 font-medium">
            <CalendarDays className="w-4 h-4" />
            {item.fecha}
          </div>
        );
      case "hora":
        return (
          <div className="flex items-center gap-2 text-blue-800 font-medium">
            <Clock4 className="w-4 h-4" />
            {item.hora}
          </div>
        );
      case "medico":
        return (
          <div className="flex items-center gap-2 text-blue-800 font-medium">
            <UserCircle2 className="w-4 h-4" />
            {item.nombreMedico}
          </div>
        );
      case "especialidad":
        return (
          <div className="flex items-center gap-2 text-blue-800 font-medium">
            <Stethoscope className="w-4 h-4" />
            {item.especialidad}
          </div>
        );
      case "estado":
        return (
          <span
            className={`text-xs font-semibold px-2 py-1 rounded-lg ${item.estado === "PENDIENTE"
                ? "bg-yellow-100 text-yellow-700"
                : item.estado === "CONFIRMADA"
                ? "bg-blue-100 text-blue-700"
                : item.estado === "ATENDIDA"
                ? "bg-green-100 text-green-700"
                : item.estado === "CANCELADA"
                ? "bg-red-100 text-red-700"
                : "bg-gray-100 text-gray-700"
              }`}
          >
            {item.estado}
          </span>

        );
      case "acciones":
        return (
          <div className="flex items-center gap-2">
            <Tooltip content="Editar cita" placement="top">
              <Button
                isIconOnly
                color="primary"
                variant="light"
                onPress={() => editarCita(parseInt(item.id))}
              >
                <Pencil className="w-4 h-4" />
              </Button>
            </Tooltip>
            <Tooltip content="Cancelar cita" placement="top">
              <Button
                isIconOnly
                color="danger"
                variant="light"
                onPress={() => cancelarCita(parseInt(item.id))}
              >
                <XCircle className="w-4 h-4" />
              </Button>
            </Tooltip>
          </div>
        );
      default:
        return cellValue;

    }
  }

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
        aria-label="Tabla de citas médicas"
        bottomContent={
          pages > 0 ? (
            <div className="flex w-full justify-center">
              <Pagination
                isCompact
                showControls
                showShadow
                color="primary"
                page={page}
                total={pages}
                onChange={(page) => setPage(page)}
              />
            </div>
          ) : null
        }
      >
        <TableHeader>
          <TableColumn key="fecha">Fecha</TableColumn>
          <TableColumn key="hora">Hora</TableColumn>
          <TableColumn key="medico">Médico</TableColumn>
          <TableColumn key="especialidad">Especialidad</TableColumn>
          <TableColumn key="estado">Estado</TableColumn>
          <TableColumn key="acciones">Acciones</TableColumn>
        </TableHeader>
        <TableBody
          emptyContent="No tienes citas registradas."
          items={citas}
          loadingContent={<Spinner />}
          loadingState={loadingState}
        >
          {(item: Cita
          ) => (
            <TableRow key={item?.id}>
              {(columnKey) => <TableCell>{renderCell(item, columnKey)}</TableCell>}
            </TableRow>
          )}
        </TableBody>
      </Table>

    </section>
  );
}
