'use client';

import {
  Table, TableBody, TableCell, TableColumn, TableHeader, TableRow, Card, Button, Tooltip, Pagination, Spinner,
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  useDisclosure,
  Calendar,
  CalendarDate,
  Alert
} from "@heroui/react";
import { CalendarDays, Clock4, UserCircle2, Stethoscope, Pencil, XCircle, CalendarIcon } from "lucide-react";
import { useEffect, useMemo, useState } from "react";
import { Key } from "@react-types/shared";
import api from "@/app/lib/axios";
import { formatearFecha } from "@/app/lib/utils";

type Cita = {
  id: string;
  nombreMedico: string;
  especialidad: string;
  fecha: string;
  hora: string;
  estado: "PENDIENTE" | "CANCELADA" | "CONFIRMADA" | "ATENDIDA";
};

const editarCita = (idCita: number, fecha: string | null, hora: string | null) => {
  api.patch(`/pacientes/citas/editar-cita/${idCita}`, {
    fechaNueva: fecha,
    horaNueva: hora
  })
    .then(() => {

    })
    .catch((error) => {
      console.error("Error al editar la cita:", error);
    });
}

export default function MisCitasSection() {

  const [page, setPage] = useState(1);
  const [loading, setIsLoading] = useState(true);
  const [citas, setCitas] = useState<Cita[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [idCitaSeleccionada, setIdCitaSeleccionada] = useState<number | null>(null);
  const rowsPerPage = 5;
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

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
  }, [page]);

  const pages = Math.ceil(totalPages / rowsPerPage);

  const handleEditarCita = (idCita: number) => {
    onOpen();
    setIdCitaSeleccionada(idCita);
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
                onPress={() => handleEditarCita(parseInt(item.id))}
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
          {
            (item: Cita) => {
              console.log("Cita actual:", item.id);

              return (
                <TableRow key={item?.id}>
                  {(columnKey) => <TableCell>{renderCell(item, columnKey)}</TableCell>}
                </TableRow>
              )
            }
          }
        </TableBody>
      </Table>

      {/* Modal de edición de cita */}
      {
        idCitaSeleccionada !== null && (
          <ModalEdicionCita
            isOpen={isOpen}
            onOpenChange={onOpenChange}
            idCita={idCitaSeleccionada}
          />
        )
      }

    </section>
  );
}
interface ModalEdicionCitaProps {
  isOpen: boolean;
  onOpenChange: (isOpen: boolean) => void;
  idCita: number;

}
const ModalEdicionCita = ({ isOpen, onOpenChange, idCita }: ModalEdicionCitaProps) => {
  const [fechaSeleccionada, setFechaSeleccionada] = useState<CalendarDate | null>(null);
  const [horaSeleccionada, setHoraSeleccionada] = useState(null);
  const [horariosDisponibles, setHorariosDisponibles] = useState([]);
  const [loading, setLoading] = useState(false);
  const { isOpen: isModalOpen, onOpen, onOpenChange: onModalOpenChange } = useDisclosure();

  const puedeAgendar =
    fechaSeleccionada !== null &&
    horaSeleccionada !== null;

  return (
    <>
      <Modal isOpen={isOpen} onOpenChange={onOpenChange} size="xl">
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">
                Seleccionar cita
              </ModalHeader>
              <ModalBody>
                <div className="flex flex-col md:flex-row gap-4">
                  <div className="flex-1">
                    <label className="text-sm font-semibold text-blue-700 flex items-center gap-2 mb-2">
                      <CalendarIcon className="w-4 h-4" />
                      Selecciona una fecha:
                    </label>
                    <Calendar
                      aria-label="Calendario de citas"
                      value={fechaSeleccionada}
                      onChange={setFechaSeleccionada}
                    />
                  </div>

                  <div className="flex-1">
                    <label className="text-sm font-semibold text-blue-700 flex items-center gap-2 mb-2">
                      <Clock4 className="w-4 h-4" />
                      Horarios disponibles:
                    </label>
                    <div className="flex flex-wrap gap-2">
                      {/* ESTA PARTE SE REEMPLAZA CON EL COMPONENTE */}
                      {horariosDisponibles.length > 0 ? (
                        horariosDisponibles.map((hora, idx) => (
                          <Button
                            key={idx}
                            size="sm"
                            variant={hora === horaSeleccionada ? "solid" : "light"}
                            color={horaSeleccionada === hora ? "primary" : "default"}
                            onPress={() => setHoraSeleccionada(hora)}
                          >
                            {hora}
                          </Button>
                        ))
                      ) : (
                        <p className="text-gray-500 text-sm">No hay horarios para esta fecha.</p>
                      )}
                    </div>
                  </div>
                </div>
              </ModalBody>
              <ModalFooter>
                <Button color="default" onPress={onClose}>
                  Cancelar
                </Button>
                {puedeAgendar && (
                  <Button
                    isLoading={loading}
                    color="primary"
                    radius="lg"
                    className="mt-4 self-start"
                    onPress={() => onOpen()}
                  >
                    Agendar cita
                  </Button>

                )}
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
      {/* MODAL DE CONFIRMACIÓN DE CITA */}
      <Modal isOpen={isModalOpen} onOpenChange={onModalOpenChange}>
        <ModalContent>
          {(onClose) => (
            <>
              <ModalHeader className="flex flex-col gap-1">Deseas agendar la cita?</ModalHeader>
              <ModalBody>
                <p>
                  Al confirmar, se editará la cita con el médico previamente seleccionado para la fecha y hora indicadas.
                </p>

              </ModalBody>
              <ModalFooter className="flex flex-col">
                <div className="flex w-full justify-center gap-3">
                  <Button color="default" onPress={onClose}>
                    Cancelar
                  </Button>
                  <Button
                    isLoading={loading}
                    color="primary"
                    onPress={() => {
                      editarCita(idCita, formatearFecha(fechaSeleccionada), horaSeleccionada);
                    }}>
                    Agendar cita
                  </Button>
                </div>
                
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>);
}
