'use client';

import { useEffect, useState } from 'react';
import { CalendarCheck, CalendarDays, Check, Clock4, IdCard, UserCircle2, XCircle } from 'lucide-react';
import { addToast, Button, Card, Pagination, Skeleton, Spinner, Table, TableBody, TableCell, TableColumn, TableHeader, TableRow, Tooltip } from '@heroui/react';
import api from '@/app/lib/axios';
import { Key } from '@react-types/shared';
import { add } from 'date-fns';

interface Cita {
  id: string;
  fecha: string; // ISO
  hora: string;
  nombrePaciente: string;
  documento: string;
  estado: string;
}

export default function VerCitas() {
  const [citas, setCitas] = useState<Cita[]>([]);
  const [loading, setLoading] = useState(true);

  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [errorCargarCitas, setErrorCargarCitas] = useState<string | null>(null);
  const rowsPerPage = 5;
  const pages = Math.ceil(totalPages / rowsPerPage);



  const obtenerCitas = () => {
    setLoading(true);
    api.get(`/medicos/mis-citas?page=${page-1}&size=${rowsPerPage}`)
      .then((response) => {
        setCitas(response.data.content);
        setTotalPages(response.data.totalElements);
        console.log(totalPages);
      })
      .catch((error) => {
        setErrorCargarCitas("No se pudieron cargar las citas. Inténtalo más tarde.");
      })
      .finally(() => {
        setLoading(false);
      });
  }

  useEffect(() => {
    obtenerCitas();
  }, [page]);

  // Renderiza el contenido de cada celda según la clave de la columna
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
      case "paciente":
        return (
          <div className="flex items-center gap-2 text-blue-800 font-medium">
            <UserCircle2 className="w-4 h-4" />
            {item.nombrePaciente}
          </div>
        );
      case "documento":
        
        return (
          <div className="flex items-center gap-2 text-blue-800 font-medium">
            <IdCard className="w-4 h-4" />
            {item.documento}
          </div>
        );
      case "estado":
        return (
          <span
            className={`text-xs font-semibold px-2 py-1 rounded-lg ${item.estado === "PENDIENTE"
              ? "bg-yellow-100 text-yellow-700"
              : item.estado === "ATENDIDA"
              ? "bg-blue-100 text-blue-700"
              : item.estado === "CONFIRMADA"
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
            function handleConfirmarCita(id: number): void {
                api.patch(`/medicos/confirmar-cita/${id}`)
                    .then(() => {
                        obtenerCitas();
                        addToast({
                            title: 'Cita confirmada',
                            description: 'La cita ha sido confirmada exitosamente.',
                            color: 'success',
                            shouldShowTimeoutProgress: true,
                            timeout: 5000,
                        });
                        
                    })
                    .catch(() => {
                        setErrorCargarCitas("No se pudo confirmar la cita.");
                    });
            }

            function handleCancelarCita(id: number, estado: string): void {

                api.patch(`/medicos/cancelar-cita/${id}`)
                    .then(() => {
                        obtenerCitas();
                        addToast({
                            title: 'Cita cancelada',
                            description: 'La cita ha sido cancelada exitosamente.',
                            color: 'danger',
                            shouldShowTimeoutProgress: true,
                            timeout: 5000,
                        });
                    })
                    .catch(() => {
                        setErrorCargarCitas("No se pudo cancelar la cita.");
                    });
            }

            function handleAtenderCita(arg0: number): void {
                api.patch(`/medicos/atender-cita/${arg0}`)
                    .then(() => {
                        obtenerCitas();
                        addToast({
                            title: 'Cita atendida',
                            description: 'La cita ha sido marcada como atendida.',
                            color: 'success',
                            shouldShowTimeoutProgress: true,
                            timeout: 5000,
                        });
                    })
                    .catch(() => {
                        setErrorCargarCitas("No se pudo marcar la cita como atendida.");
                    });
            }

        return (
          <div className="flex items-center gap-2">
            <Tooltip content="Marcar como cita atendida" placement="top">
              <Button
                isIconOnly
                isDisabled={item.estado === "CANCELADA" || item.estado === "ATENDIDA"}
                color="primary"
                variant="light"
                onPress={() => handleAtenderCita(parseInt(item.id))}
              >
                <CalendarCheck className="w-4 h-4" />
              </Button>
            </Tooltip>
            <Tooltip content="Confirmar Cita" placement="top">
              <Button
                isIconOnly
                isDisabled={item.estado === "CANCELADA" || item.estado === "ATENDIDA" || item.estado === "CONFIRMADA"}
                color="success"
                variant="light"
                onPress={() =>  handleConfirmarCita(parseInt(item.id)) }
              >
                <Check className="w-4 h-4" />
              </Button>
            </Tooltip>
            
            <Tooltip content="Cancelar cita" placement="top">
              <Button
                isIconOnly
                isDisabled={item.estado === "CANCELADA" || item.estado === "ATENDIDA"}
                color="danger"
                variant="light"
                onPress={() => handleCancelarCita(parseInt(item.id), item.estado)}
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
    <div className="p-6 space-y-6">
      <h1 className="text-2xl font-bold flex items-center gap-2">
        <CalendarDays className="w-6 h-6 text-primary" /> Citas Próximas
      </h1>

      <Table
        isStriped
        aria-label="Tabla de citas médicas"
        bottomContent={
          pages > 0 ? (
            <div className="flex w-full justify-center">
              <Pagination
                isCompact={true}
                showControls
                showShadow
                color="primary"
                initialPage={1}
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
          <TableColumn key="paciente">Nombre del Paciente</TableColumn>
          <TableColumn key="documento">Documento</TableColumn>
          <TableColumn key="estado">Estado</TableColumn>
          <TableColumn key="acciones">Acciones</TableColumn>
        </TableHeader>
        <TableBody
          emptyContent={"No tienes citas registradas."}
          items={citas}
          loadingContent={<Spinner />}
          isLoading={loading}
        >
          {
            (item: Cita) => {
              return (
                <TableRow
                  key={item?.id}
                >
                  {(columnKey) => <TableCell>{renderCell(item, columnKey)}</TableCell>}
                </TableRow>
              )
            }
          }
        </TableBody>
      </Table>
    </div>
  );
}
