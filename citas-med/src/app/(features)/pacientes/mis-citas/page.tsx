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
  Alert,
  addToast
} from "@heroui/react";
import { CalendarDays, Clock4, UserCircle2, Stethoscope, Pencil, XCircle, CalendarIcon, AlertTriangle } from "lucide-react";
import { use, useEffect, useMemo, useState } from "react";
import { Key } from "@react-types/shared";
import api from "@/app/lib/axios";
import { formatearFecha, formatearHora } from "@/app/lib/utils";
import { cargarDisponibilidades } from "../agendar-cita/seleccionDisponibilidad";
import { TabItem } from "flowbite-react";
import { getLocalTimeZone, today } from "@internationalized/date";

type Cita = {
  id: string;
  idMedico: number;
  nombreMedico: string;
  especialidad: string;
  fecha: string;
  hora: string;
  estado: "PENDIENTE" | "CANCELADA" | "CONFIRMADA" | "ATENDIDA";
};



export default function MisCitasSection() {
  // Estados para manejar la paginación y el estado de carga
  const [page, setPage] = useState(1);
  const [loading, setIsLoading] = useState(true);
  const [citas, setCitas] = useState<Cita[]>([]);
  const [totalPages, setTotalPages] = useState(0);
  const [errorCargarCitas, setErrorCargarCitas] = useState<string | null>(null);

  // Estados para manejar la edición de citas
  const [idCitaSeleccionada, setIdCitaSeleccionada] = useState<number | null>(null);
  const [idCitaSeleccionadaCancelar, setIdCitaSeleccionadaCancelar] = useState<number | null>(null);
  const [idMedicoSeleccionada, setIdMedicoSeleccionada] = useState<number | null>(null);
  const [isEditarCitaExitoso, setIsEditarCitaExitoso] = useState(false);
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  // Estados para manejar la cancelación de citas
  const [estadoCitaSeleccionada, setEstadoCitaSeleccionada] = useState<string | null>(null);
  const [isOpenModalCancelar, setIsOpenModalCancelar] = useState(false);
  const rowsPerPage = 5;

  const obtenerCitas = () => {
    api.get(`/pacientes/mis-citas?page=${page - 1}`)
      .then((response) => {
        setCitas(response.data.content);
        setTotalPages(response.data.totalElements);
      })
      .catch((error) => {
        setErrorCargarCitas(error.response?.data || "Error al cargar las citas");
      })
      .finally(() => {
        setIsLoading(false)
      });
  }

  // Cargar citas al montar el componente y al cambiar de página
  useEffect(() => {
    obtenerCitas();
  }, [page, isEditarCitaExitoso]);

  const pages = Math.ceil(totalPages / rowsPerPage);


  const handleEditarCita = (idCita: number, idMedico: number, estado: string) => {
    setIdCitaSeleccionada(null);
    setIdMedicoSeleccionada(null);
    onOpen();
    setIdCitaSeleccionada(idCita);
    setIdMedicoSeleccionada(idMedico);
  };

  const handleCancelarCita = (id: number, estado: string) => {
    setEstadoCitaSeleccionada(estado);
    setIdCitaSeleccionadaCancelar(id);
    setIsOpenModalCancelar(true);
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
                isDisabled={item.estado === "CANCELADA" || item.estado === "ATENDIDA"}
                color="primary"
                variant="light"
                onPress={() => handleEditarCita(parseInt(item.id), item.idMedico, item.estado)}
              >
                <Pencil className="w-4 h-4" />
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

      {/* Modal de edición de cita */}
      {
        idCitaSeleccionada !== null && idMedicoSeleccionada && (
          <ModalEdicionCita
            isOpen={isOpen}
            onOpenChange={onOpenChange}
            idCita={idCitaSeleccionada}
            idMedico={idMedicoSeleccionada}
            onEditSuccess={() => (obtenerCitas())}
          />
        )
      }

      {/* Modal de confirmación de cancelación */}
      <ModalCancelarCita
        isOpen={isOpenModalCancelar}
        onOpenChange={setIsOpenModalCancelar}
        idCita={idCitaSeleccionadaCancelar}
        onCancelSuccess={() => {
          obtenerCitas();
          setIsOpenModalCancelar(false);

        }}
      />

    </section>
  );
}
interface ModalEdicionCitaProps {
  isOpen: boolean;
  onOpenChange: (isOpen: boolean) => void;
  idCita: number;
  idMedico: number | null;
  onEditSuccess: () => void;

}
const ModalEdicionCita = ({ isOpen, onOpenChange, idCita, idMedico, onEditSuccess }: ModalEdicionCitaProps) => {
  const [fechaSeleccionada, setFechaSeleccionada] = useState<CalendarDate | null>(null);
  const [loading, setLoading] = useState(false);
  const { isOpen: isModalOpen, onOpen, onOpenChange: onModalOpenChange } = useDisclosure();
  const [disponibilidades, setDisponibilidades] = useState<string[]>([]);
  const [bloqueSeleccionado, setBloqueSeleccionado] = useState<string | null>(null);
  const [isVisibleAlert, setIsVisibleAlert] = useState(false);
  const [description, setDescription] = useState("");
  const [title, setTitle] = useState("");

  const editarCita = (idCita: number, fecha: string | null, hora: string | null) => {
    setLoading(true);
    api.patch(`/pacientes/editar-cita/${idCita}`, {
      fechaNueva: fecha,
      horaNueva: formatearHora(hora)
    })
      .then(() => {
        onEditSuccess();
        addToast({
          color: "success",
          title: "Cita editada Exitosamente",
          description: "Tu cita ha sido editada correctamente.",
          timeout: 5000,
          shouldShowTimeoutProgress: true,
        });
        onModalOpenChange();
        onOpenChange(false);
        setFechaSeleccionada(null);
        setBloqueSeleccionado(null);
        setDisponibilidades([]);

      })

      .catch((error) => {
        setIsVisibleAlert(true);
        setDescription(error.response?.data || "Error al editar la cita");
        setTitle("Error");
      })
      .finally(() => (setLoading(false)));
  }

  useEffect(() => {
    if (fechaSeleccionada) {
      cargarDisponibilidades(
        fechaSeleccionada,
        idMedico,
        setDisponibilidades,
        setLoading,
        (error) => console.error("Error al cargar disponibilidades:", error)
      );
      setBloqueSeleccionado(null);
    } else {
      setDisponibilidades([]);
    }
  }, [fechaSeleccionada, idMedico]);

  const puedeAgendar =
    fechaSeleccionada !== null &&
    bloqueSeleccionado !== null;

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
                      minValue={today(getLocalTimeZone())}
                    />
                  </div>

                  <div className="flex-1">
                    <label className="text-sm font-semibold text-blue-700 flex items-center gap-2 mb-2">
                      <Clock4 className="w-4 h-4" />
                      Horarios disponibles:
                    </label>
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
                </div>
              </ModalBody>
              <ModalFooter className="flex gap-4">
                <Button color="default" onPress={onClose}>
                  Cancelar
                </Button>
                {puedeAgendar && (
                  <Button
                    isLoading={loading}
                    color="primary"
                    radius="lg"
                    onPress={() => onOpen()}
                  >
                    Editar cita
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
              <ModalHeader className="flex items-center gap-2 text-yellow-600">
                <AlertTriangle className="w-5 h-5" />
                Confirmar modificación
              </ModalHeader>

              <div className="border-t border-gray-200" />

              <ModalBody>
                <p className="text-sm text-gray-700">
                  Estás a punto de modificar una cita médica. Asegúrate de que la nueva fecha y hora sean correctas.
                </p>

                <div className="mt-4 flex flex-col gap-2 text-blue-800 font-medium">
                  <div className="flex items-center gap-2">
                    <CalendarDays className="w-4 h-4" />
                    <span><strong>Fecha seleccionada:</strong> {fechaSeleccionada?.toString()}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <Clock4 className="w-4 h-4" />
                    <span><strong>Hora seleccionada:</strong> {formatearHora(bloqueSeleccionado)}</span>
                  </div>
                </div>
              </ModalBody>

              <div className="border-t border-gray-200" />

              <ModalFooter className="flex flex-col gap-2">
                <div className="flex w-full justify-end gap-3">
                  {isVisibleAlert && (
                    <Alert
                      color="danger"
                      className="w-full mb-4"
                      onClose={() => setIsVisibleAlert(false)}
                    >
                      <span className="font-semibold">{title}</span>
                      <p>{description}</p>
                    </Alert>
                  )}
                  <Button variant="light" onPress={onClose}>
                    Cancelar
                  </Button>

                  <Button
                    isLoading={loading}
                    color="warning"
                    onPress={() => {
                      editarCita(idCita, formatearFecha(fechaSeleccionada), bloqueSeleccionado);
                    }}
                    startContent={<AlertTriangle className="w-4 h-4" />}
                  >
                    Modificar cita
                  </Button>
                </div>
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>
    </>);
}

interface ModalCancelarCitaProps {
  isOpen: boolean;
  onOpenChange: (isOpen: boolean) => void;
  idCita: number | null;
  onCancelSuccess: () => void;
}

const ModalCancelarCita = ({ isOpen, onOpenChange, idCita, onCancelSuccess }: ModalCancelarCitaProps) => {
  const [loading, setLoading] = useState(false);
  const [isVisibleAlert, setIsVisibleAlert] = useState(false);
  const [description, setDescription] = useState("");
  const [title, setTitle] = useState("");
  console.log("ID de cita en ModalCancelarCita:", idCita);

  const cancelarCita = () => {
    if (idCita === null) return;

    setLoading(true);
    api.patch(`/pacientes/cancelar-cita/${idCita}`)
      .then(() => {
        onCancelSuccess();
        addToast({
          color: "success",
          title: "Cita cancelada Exitosamente",
          description: "Tu cita ha sido cancelada correctamente.",
          timeout: 5000,
          shouldShowTimeoutProgress: true,
        });
        onOpenChange(false);
      })
      .catch((error) => {
        setIsVisibleAlert(true);
        setDescription(error.response?.data || "Error al cancelar la cita");
        setTitle("Error");

      })
      .finally(() => setLoading(false));
  };

  return (
    <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
      <ModalContent>
        {(onClose) => (
          <>
            <ModalHeader className="flex items-center gap-2 text-red-600">
              <XCircle className="w-5 h-5" />
              Confirmar cancelación
            </ModalHeader>

            <div className="border-t border-gray-200" />

            <ModalBody>
              <p className="text-sm text-gray-700">
                Estás a punto de cancelar una cita médica. Esta acción no se puede deshacer.
              </p>
            </ModalBody>

            <div className="border-t border-gray-200" />

            <ModalFooter className="flex flex-col gap-2">
              <div className="flex w-full justify-end gap-3">
                {isVisibleAlert && (
                  <Alert
                    color="danger"
                    className="w-full mb-4"
                    onClose={() => setIsVisibleAlert(false)}
                  >
                    <span className="font-semibold">{title}</span>
                    <p>{description}</p>
                  </Alert>
                )}
                <Button variant="light" onPress={onClose}>
                  Cancelar
                </Button>

                <Button
                  isLoading={loading}
                  color="danger"
                  onPress={cancelarCita}
                  startContent={<XCircle className="w-4 h-4" />}
                >
                  Cancelar cita
                </Button>
              </div>
            </ModalFooter>
          </>
        )}
      </ModalContent>
    </Modal>
  );
}
