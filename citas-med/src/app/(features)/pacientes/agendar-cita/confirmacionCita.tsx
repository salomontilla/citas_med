'use client';

import { useMedicoStore } from '@/app/store/medicoStore';
import { CalendarCheck, Clock4, Router, UserCircle2 } from 'lucide-react';
import {
    Alert, Button, useDisclosure, Modal,
    ModalContent,
    ModalHeader,
    ModalBody,
    ModalFooter,
    addToast
} from '@heroui/react';
import api from '@/app/lib/axios';
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { set } from 'date-fns';

interface Props {
    fechaSeleccionada: string | null;
    horaInicio: string | null;
    onCitaAgendada?: () => void;
}

export default function ConfirmacionCita({
    fechaSeleccionada,
    horaInicio,
    onCitaAgendada
}: Props) {
    const { medicoSeleccionado } = useMedicoStore();
    const { isOpen, onOpen, onOpenChange } = useDisclosure();

    const [isAgendadaExitosamente, setIsAgendadaExitosamente] = useState(false);
    const [description, setDescription] = useState("");
    const [title, setTitle] = useState("");
    const [isVisibleAlert, setIsVisibleAlert] = useState(false);
    const [loading, setLoading] = useState(false);
    const router = useRouter();

    const puedeAgendar =
        fechaSeleccionada !== null &&
        horaInicio !== null &&
        medicoSeleccionado !== null;

    const agendarCita = () => {
        setLoading(true);
        setIsVisibleAlert(false);
        setDescription("");
        setTitle("");

        api
            .post("/pacientes/citas/agendar", {
                medicoId: medicoSeleccionado?.id,
                fecha: fechaSeleccionada,
                hora: horaInicio?.substring(horaInicio.indexOf("-") + 1) + ":00",
            })
            .then(() => {
                setIsAgendadaExitosamente(true);
                setIsVisibleAlert(true);
                setDescription("Cita agendada exitosamente");
                setTitle("Éxito");

                addToast({
                    color: "success",
                    title: "Cita Agendada Exitosamente",
                    description: "Tu cita ha sido agendada correctamente.",
                    timeout: 5000,
                    shouldShowTimeoutProgress: true,
                });
                onCitaAgendada?.();

                    setIsVisibleAlert(false);
                    onOpenChange();
                

            })
            .catch((error) => {
                setIsAgendadaExitosamente(false);
                setIsVisibleAlert(true);
                setTitle("Error");
                setDescription(error.response?.data || "Error al agendar la cita");
            })
            .finally(() => setLoading(false));
    };


    return (
        <div className="mt-8 bg-white p-6 rounded-2xl shadow-lg border border-blue-200 flex flex-col gap-4">
            <h2 className="text-xl font-semibold text-blue-800">Resumen de la cita</h2>

            <div className="flex items-center gap-3 text-blue-700">
                <UserCircle2 className="w-5 h-5" />
                <p className="text-sm">
                    <strong>Médico:</strong>{' '}
                    {medicoSeleccionado ? medicoSeleccionado.nombre : 'No seleccionado'}
                </p>
            </div>

            <div className="flex items-center gap-3 text-blue-700">
                <CalendarCheck className="w-5 h-5" />
                <p className="text-sm">
                    <strong>Fecha:</strong>{' '}
                    {fechaSeleccionada ? fechaSeleccionada : 'No seleccionada'}
                </p>
            </div>

            <div className="flex items-center gap-3 text-blue-700">
                <Clock4 className="w-5 h-5" />
                <p className="text-sm">
                    <strong>Hora:</strong>{' '}
                    {horaInicio
                        ? `${horaInicio.substring(horaInicio.indexOf('-') + 1)}`
                        : 'No seleccionada'}
                </p>
            </div>

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
            {/* MODAL DE CONFIRMACION */}
            <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
                <ModalContent>
                    {(onClose) => (
                        <>
                            <ModalHeader className="flex flex-col gap-1">Deseas agendar la cita?</ModalHeader>
                            <ModalBody>
                                <p>
                                    Al confirmar, se agendará la cita con el médico seleccionado para la fecha y hora indicadas.
                                </p>

                            </ModalBody>
                            <ModalFooter className="flex flex-col">
                                <div className="flex w-full justify-center gap-3">
                                    <Button color="default" onPress={onClose}>
                                        Cancelar
                                    </Button>
                                    <Button isLoading={loading} color="primary" onPress={agendarCita}>
                                        Agendar cita
                                    </Button>
                                </div>
                                <Alert
                                    color={isAgendadaExitosamente ? "success" : "danger"}
                                    className="w-full mt-3"
                                    description={description}
                                    title={title}
                                    isVisible={isVisibleAlert}
                                    variant="faded"
                                    onClose={() => setIsVisibleAlert(false)}
                                />
                            </ModalFooter>
                        </>
                    )}
                </ModalContent>
            </Modal>
        </div>
    );
}
