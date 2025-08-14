'use client'
import api from '@/app/lib/axios'
import { EyeFilledIcon, EyeSlashFilledIcon } from '@/app/ui/components/passwordEyes';
import { addToast, Button, Chip, DatePicker, Input, Link, Select, SelectItem, Skeleton, Spinner } from '@heroui/react';
import { use, useEffect, useState } from 'react'
import { CalendarDate, today, getLocalTimeZone } from "@internationalized/date";
import { formatearFecha } from '@/app/lib/utils';
import { is } from 'date-fns/locale';
import router from 'next/router';
import { Stethoscope } from 'lucide-react';
import type { Selection } from "@heroui/react";

export default function InfoPaciente({
    params,
}: {
    params: Promise<{ id: string }>
}) {
    interface DatosMedico {
        nombre?: string;
        email?: string;
        documento?: string;
        telefono?: string;
        especialidad?: string;
        isActivo?: boolean;
        contrasena?: string;
    }

    const { id } = use(params)

    const [datos, setDatos] = useState<DatosMedico>({
        nombre: '',
        email: '',
        documento: '',
        telefono: '',
        especialidad: '',
        isActivo: true,
        contrasena: ''
    });
    const [editando, setEditando] = useState(false);
    const [loading, setLoading] = useState(false);

    const [nombre, setNombre] = useState('');
    const [documento, setDocumento] = useState('');
    const [correo, setCorreo] = useState('');
    const [telefono, setTelefono] = useState('');
    const [contrasena, setContrasena] = useState<string | null>('');
    const [especialidad, setEspecialidad] = useState<string>('');
    const [especialidadSeleccionada, setEspecialidadSeleccionada] = useState<Selection>(new Set());
    const [isLoadingInfo, setIsLoadingInfo] = useState(true);
    const [isMedicoActive, setIsMedicoActive] = useState<boolean | undefined>(undefined);

    // Lista de especialidades disponibles
    const especialidades = [
        { key: 'general', nombre: 'General' },
        { key: 'pediatria', nombre: 'Pediatría' },
        { key: 'cardiologia', nombre: 'Cardiología' }
    ];

    const toggleVisibility = () => setIsVisible(!isVisible);
    const [isVisible, setIsVisible] = useState(false);

    const obtenerDatos = () => {
        api.get(`/admin/medicos/${id}`)
            .then((response) => {
                setDatos(response.data);
                setIsMedicoActive(response.data.isActivo);
            })
            .catch((error) => {
                addToast({
                    title: 'Error',
                    description: 'No se pudieron obtener los datos del usuario.',
                    color: 'danger',
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            })
            .finally(() => {
                setIsLoadingInfo(false);
            });
    };

    // Obtener los datos del usuario al cargar el componente
    useEffect(() => {
        obtenerDatos();
    }, []);

    // Actualizar los estados locales con los datos obtenidos
    useEffect(() => {
        if (datos.email) {
            setCorreo(datos.email);
        }
        if (datos.telefono) {
            setTelefono(datos.telefono);
        }
        if (datos.nombre) {
            setNombre(datos.nombre);
        }

        if (datos.documento) {
            setDocumento(datos.documento);
        }
        if (datos.especialidad) {
            setEspecialidad(datos.especialidad);
        }
    }, [datos]);

    const editarDatos = () => {
        setLoading(true);
        setEditando(true);

        // Create an object with only the changed fields
        const changedFields: Partial<DatosMedico> = {};

        if (nombre !== datos.nombre) {
            changedFields.nombre = nombre;
        }
        if (correo !== datos.email) {
            changedFields.email = correo;
        }
        if (telefono !== datos.telefono) {
            changedFields.telefono = telefono;
        }
        if (documento !== datos.documento) {
            changedFields.documento = documento;
        }
        //esta condicion obtiene el valor de la especialidad seleccionada que viene de un set
        const seleccionArray = Array.from(especialidadSeleccionada);
        if (
            seleccionArray.length > 0 &&
            seleccionArray[0] !== datos.especialidad
        ) {
            const valor = seleccionArray[0].toString().toUpperCase();
            changedFields.especialidad = String(valor);
        }

        if (contrasena) {
            changedFields.contrasena = contrasena;
        }

        api.patch(`admin/medicos/editar/${id}`, changedFields)
            .then((response) => {
                setDatos(response.data);
                addToast({
                    title: 'Éxito',
                    description: 'Datos actualizados correctamente.',
                    color: 'success',
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            })
            .catch(() => {
                addToast({
                    title: 'Error',
                    description: 'No se pudieron actualizar los datos.',
                    color: 'danger',
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            });
    }
    const guardarCambios = () => {
        setLoading(true);
        if (!correo || !telefono || !nombre || !documento || !especialidad) {
            // Campos vacíos
            addToast({
                title: 'Error',
                description: 'Por favor, completa todos los campos requeridos.',
                color: 'danger',
                shouldShowTimeoutProgress: true,
                timeout: 5000,
            });
            setLoading(false);
            return;
        }

        // Check if any field has changed
        const hasChanges = (
            nombre !== datos.nombre ||
            correo !== datos.email ||
            telefono !== datos.telefono ||
            documento !== datos.documento ||
            especialidadSeleccionada !== datos.especialidad
        );


        if (!hasChanges) {
            // Nada ha cambiado
            addToast({
                title: 'Sin cambios',
                description: 'No hiciste ningún cambio en los datos.',
                color: 'warning',
                shouldShowTimeoutProgress: true,
                timeout: 5000,
            });
            setLoading(false);
            return;
        }


        editarDatos();
        setLoading(false);
        setEditando(false);
    };

    function handleCancelar(): void {
        setEditando(false);
    }

    function handleEstadoMedico(): void {
        setIsMedicoActive(!isMedicoActive);
        if (isMedicoActive) {
            api.delete(`admin/medicos/${id}`)
                .then(() => {
                    addToast({
                        title: 'Éxito',
                        description: 'La cuenta ha sido desactivada.',
                        color: 'success',
                        shouldShowTimeoutProgress: true,
                        timeout: 5000,
                    });
                })
                .catch(() => {
                    addToast({
                        title: 'Error',
                        description: 'No se pudo desactivar la cuenta.',
                        color: 'danger',
                        shouldShowTimeoutProgress: true,
                        timeout: 5000,
                    });
                });
        } else {
            api.patch(`admin/medicos/activar/${id}`)
                .then(() => {
                    addToast({
                        title: 'Éxito',
                        description: 'La cuenta ha sido reactivada.',
                        color: 'success',
                        shouldShowTimeoutProgress: true,
                        timeout: 5000,
                    });
                })
                .catch(() => {
                    addToast({
                        title: 'Error',
                        description: 'No se pudo reactivar la cuenta.',
                        color: 'danger',
                        shouldShowTimeoutProgress: true,
                        timeout: 5000,
                    });
                });
        }
    }

    return (
        <div className='min-h-screen md:min-h-fit w-full flex items-center justify-center bg-blue-50 px-4 py-8 md:py-0'>
            <section className="w-full bg-blue-200 rounded-2xl max-w-4xl shadow-md">
                <div className='h-20 flex justify-center items-center rounded-t-2xl bg-gradient-to-r from-blue-500 to-blue-800'>
                    <h1 className='text-white text-2xl font-bold'>Datos Personales</h1>
                </div>

                <div className="bg-white w-full p-8 rounded-b-2xl">
                    <div className="flex flex-col gap-4 items-center mb-6">
                        <img src="/medico.jpg" alt="doctor" className='rounded-full h-24 w-24 object-cover' />
                        <div className='flex flex-col items-center gap-1'>
                            <h1 className='font-bold text-xl'>{datos.nombre}</h1>
                            <span className="text-sm text-gray-500">{datos.email}</span>
                            <Chip color={isMedicoActive ? 'success' : 'danger'}>
                                {isMedicoActive ? 'Activo' : 'Inactivo'}
                            </Chip>
                        </div>
                    </div>
                    <form>
                        <div className='grid grid-cols-1 md:grid-cols-2 gap-4'>
                            <Skeleton isLoaded={!isLoadingInfo} className='rounded-2xl bg-blue-100'>
                                <Input
                                    label="Nombre"
                                    color='primary'
                                    value={nombre}
                                    onValueChange={setNombre}
                                    isDisabled={!editando}
                                />
                            </Skeleton>
                            <Skeleton isLoaded={!isLoadingInfo} className='rounded-2xl bg-blue-100'>
                                <Input
                                    label="Correo Electrónico"
                                    color='primary'
                                    value={correo}
                                    onValueChange={setCorreo}
                                    isDisabled={!editando}
                                />
                            </Skeleton>
                            <Skeleton isLoaded={!isLoadingInfo} className='rounded-2xl bg-blue-100'>
                                <Input
                                    label="Documento de Identidad"
                                    color='primary'
                                    value={documento}
                                    onValueChange={setDocumento}
                                    isDisabled={!editando}
                                />
                            </Skeleton>
                            <Skeleton isLoaded={!isLoadingInfo} className='rounded-2xl bg-blue-100'>
                                <Input
                                    label="Teléfono"
                                    color='primary'
                                    value={telefono}
                                    onValueChange={setTelefono}
                                    isDisabled={!editando}
                                />
                            </Skeleton>
                            <Skeleton isLoaded={!isLoadingInfo} className='rounded-2xl bg-blue-100'>
                                <Select
                                    className="max-full"
                                    isDisabled={!editando}
                                    label="Cambiar especialidad"
                                    color="primary"
                                    onSelectionChange={setEspecialidadSeleccionada}
                                    disabledKeys={[especialidad.toLowerCase()]}
                                    placeholder={`Actual: ${datos?.especialidad || "Ninguna"}`}
                                    startContent={<Stethoscope />}
                                >
                                    {especialidades.map((esp) => (
                                        <SelectItem key={esp.key}>{esp.nombre}</SelectItem>
                                    ))}
                                </Select>
                            </Skeleton>
                            <Skeleton isLoaded={!isLoadingInfo} className='rounded-2xl bg-blue-100'>
                                <Input
                                    color="primary"
                                    onValueChange={setContrasena}
                                    isDisabled={!editando}
                                    endContent={
                                        <button
                                            aria-label="toggle password visibility"
                                            className="focus:outline-none"
                                            type="button"
                                            onClick={toggleVisibility}
                                        >
                                            {isVisible ? <EyeSlashFilledIcon /> : <EyeFilledIcon />}
                                        </button>
                                    }
                                    label="Nueva contraseña"
                                    type={isVisible ? "text" : "password"}
                                />
                            </Skeleton>
                        </div>

                        <div className="flex flex-col md:flex-row justify-end mt-10 gap-4">
                            <div className="flex-1 flex justify-start">
                                <Button className='left-0' variant='light'>
                                    <Link href='/admin/gestionar-medicos'>Volver</Link>
                                </Button>
                            </div>
                            <Button color={isMedicoActive ? "danger" : "success"} onPress={() => handleEstadoMedico()}>
                                {isMedicoActive ? "Desactivar cuenta" : "Activar cuenta"}
                            </Button>
                            {editando ? (
                                <>
                                    <Button color="default" onPress={() => handleCancelar()} isDisabled={loading}>
                                        Cancelar
                                    </Button>
                                    <Button color="primary" onPress={guardarCambios} isLoading={loading}>
                                        Guardar
                                    </Button>
                                </>
                            ) : (
                                <Button color="primary" onPress={() => setEditando(true)}>
                                    Editar perfil
                                </Button>
                            )}
                        </div>
                    </form>

                </div>
            </section>
        </div>



    );



}