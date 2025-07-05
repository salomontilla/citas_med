'use client';

import { useMedicoStore } from '@/app/store/medicoStore';
import { CalendarCheck, Clock4, UserCircle2 } from 'lucide-react';
import { Alert, Button } from '@heroui/react';
import api from '@/app/lib/axios';
import { formatearFecha } from '@/app/lib/utils';
import { useState } from 'react';
import { set } from 'date-fns';

interface Props {
    fechaSeleccionada: string | null;
    horaInicio: string | null;
}

export default function ConfirmacionCita({
    fechaSeleccionada,
    horaInicio,
}: Props) {
    const { medicoSeleccionado } = useMedicoStore();
    const [isAgendadaExitosamente, setIsAgendadaExitosamente] = useState(false);
    const [description, setDescription] = useState("");
    const [title, setTitle] = useState("");
    const [isVisibleAlert, setIsVisibleAlert] = useState(false);
    const [loading, setLoading] = useState(false);


    const puedeAgendar =
        fechaSeleccionada !== null &&
        horaInicio !== null &&
        medicoSeleccionado !== null;

    const agendarCita = () => {
        console.log(fechaSeleccionada, horaInicio, medicoSeleccionado);
        setLoading(true);
        api.post('/pacientes/citas/agendar', {
            medicoId: medicoSeleccionado?.id,
            fecha: fechaSeleccionada,
            hora: horaInicio,
        })
            .then((response) => {
                setIsAgendadaExitosamente(true);
                setIsVisibleAlert(true);
                setDescription("Cita agendada exitosamente");
                setTitle("Éxito");
            }
            )
            .catch((error) => {
                setIsAgendadaExitosamente(false);
                setIsVisibleAlert(true);
                setTitle("Error");
                setDescription(error.response?.data || "Error al agendar la cita");
                  }).finally(() => setLoading(false));

    }

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
                        ? `${horaInicio}`
                        : 'No seleccionada'}
                </p>
            </div>

            {puedeAgendar && (
                <Button
                    isLoading={loading}
                    color="primary"
                    radius="lg"
                    className="mt-4 self-start"
                    onPress={() => agendarCita()}
                >
                    Agendar cita
                </Button>

            )}
            <Alert
                color={isAgendadaExitosamente ? "success" : "danger"}
                className="w-full mt-3"
                description={description}
                title={title}
                isVisible={isVisibleAlert}
                variant="faded"
                onClose={() => setIsVisibleAlert(false)}
            />
        </div>
    );
}
