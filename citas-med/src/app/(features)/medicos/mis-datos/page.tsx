'use client';

import { useEffect, useState } from 'react';
import { Input, Button, addToast, Skeleton } from '@heroui/react';
import api from '@/app/lib/axios';
import { EyeFilledIcon, EyeSlashFilledIcon } from '@/app/ui/components/passwordEyes';

interface DatosUsuario {
    nombre?: string;
    email?: string;
    documento?: string;
    telefono?: string;
    especialidad?: string;
    contrasena?: string;
}

export default function PerfilUsuario() {
    const [datos, setDatos] = useState<DatosUsuario>(
        {
            nombre: '',
            email: '',
            documento: '',
            telefono: '',
            especialidad: '',
            contrasena: ''
        }
    );
    const [editando, setEditando] = useState(false);
    const [loading, setLoading] = useState(false);
    const [isLoadingInfo, setIsLoadingInfo] = useState(true);

    const [nombre, setNombre] = useState<string>('');
    const [correo, setCorreo] = useState<string>('');
    const [telefono, setTelefono] = useState<string>('');
    const [documento, setDocumento] = useState<string>('');
    const [especialidad, setEspecialidad] = useState<string>('');
    const [contrasena, setContrasena] = useState<string>('');
    const togglePasswordVisibility = () => setIsPasswordVisible(!isPasswordVisible);
    const [isPasswordVisible, setIsPasswordVisible] = useState(false);

    const obtenerDatos = () => {
        api.get('/medicos/mis-datos')
            .then((response) => {
                setDatos(response.data);
            })
            .catch((error) => {
                addToast({
                    title: 'Error',
                    description: error.message || 'No se pudieron obtener los datos del usuario.',
                    color: 'danger',
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            }).finally(() => {
                setIsLoadingInfo(false);
            });
    };

    // Obtener los datos del usuario al cargar el componente
    useEffect(() => {
        obtenerDatos();
    }, []);

    // Actualizar datos al cargar el componente
    useEffect(() => {
        if (datos.nombre) {
            setNombre(datos.nombre);
        }
        if (datos.email) {
            setCorreo(datos.email);
        }
        if (datos.telefono) {
            setTelefono(datos.telefono);
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
        const changedFields: Partial<DatosUsuario> = {};


        if (correo !== datos.email) {
            changedFields.email = correo;
        }
        if (telefono !== datos.telefono) {
            changedFields.telefono = telefono;
        }
        if (contrasena) {
            changedFields.contrasena = contrasena;
        }

        api.patch('/medicos/editar-datos', changedFields)
            .then((response) => {
                setDatos(response.data);
                addToast({
                    title: 'Éxito',
                    description: 'Datos actualizados correctamente.',
                    color: 'success',
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
                setEditando(false);
            })
            .catch((error) => {
                addToast({
                    title: 'Error',
                    description: error.response.data.message || 'No se pudieron actualizar los datos.',
                    color: 'danger',
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            });
    }
    const guardarCambios = () => {
        setLoading(true);


        if (!correo || !telefono) {
            addToast({
            title: 'Campos vacíos',
            description: 'Por favor completa todos los campos requeridos.',
            color: 'danger',
            shouldShowTimeoutProgress: true,
            timeout: 5000,
            });
            setLoading(false);
            return;
        }
        const hasChanges = (
            correo !== datos.email ||
            telefono !== datos.telefono ||
            contrasena !== datos.contrasena
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

    function handleCancelarEdicion() {
        setEditando(false);
        setCorreo(datos.email ?? '');
        setTelefono(datos.telefono ?? '');
    }

    return (
        <div className='min-h-screen md:min-h-fit w-full flex items-center justify-center bg-blue-50 px-4 py-8 md:py-0'>
            <section className="w-full bg-blue-200 rounded-2xl max-w-4xl shadow-md">
                <div className='h-20 flex justify-center items-center rounded-t-2xl bg-gradient-to-r from-blue-500 to-blue-800'>
                    <h1 className='text-white text-2xl font-bold'>Mis Datos</h1>
                </div>

                <div className="bg-white w-full p-8 rounded-b-2xl">
                    <div className="flex flex-col gap-4 items-center mb-6">
                        <img src="/medico.jpg" alt="doctor" className='rounded-full h-24 w-24 object-cover' />
                        <div className='flex flex-col items-center'>
                            <h1 className='font-bold text-xl'>{nombre}</h1>
                            <span className="text-sm text-gray-500">{correo}</span>
                        </div>
                    </div>
                    <form>
                        <div className='grid grid-cols-1 md:grid-cols-2 gap-4'>
                            <Skeleton className='bg-blue-50 rounded-xl' isLoaded={!isLoadingInfo}>
                                <Input
                                    label="Nombre"
                                    color='primary'
                                    value={nombre}
                                    isDisabled={true}
                                />
                            </Skeleton>
                            <Skeleton className='bg-blue-50 rounded-xl' isLoaded={!isLoadingInfo}>
                                <Input
                                    label="Correo Electrónico"
                                    type='email'
                                    color='primary'
                                    value={correo}
                                    onValueChange={setCorreo}
                                    isDisabled={!editando}
                                    isRequired
                                />
                            </Skeleton>

                            <Skeleton className='bg-blue-50 rounded-xl' isLoaded={!isLoadingInfo}>
                                <Input
                                    label="Documento de Identidad"
                                    color='primary'
                                    value={documento}
                                    isDisabled={true}
                                />
                            </Skeleton>

                            <Skeleton className='bg-blue-50 rounded-xl' isLoaded={!isLoadingInfo}>
                                <Input
                                    label="Teléfono"
                                    color='primary'
                                    value={telefono}
                                    onValueChange={setTelefono}
                                    isDisabled={!editando}
                                    isRequired
                                />
                            </Skeleton>
                            <Skeleton className='bg-blue-50 rounded-xl' isLoaded={!isLoadingInfo}>
                                <Input
                                    label="Especialidad"
                                    color='primary'
                                    value={especialidad}
                                    isDisabled={true}
                                />
                            </Skeleton>
                            <Skeleton className='bg-blue-50 rounded-xl' isLoaded={!isLoadingInfo}>
                                <Input
                                    color="primary"
                                    onValueChange={setContrasena}
                                    isDisabled={!editando}
                                    endContent={
                                        <button
                                            aria-label="toggle password visibility"
                                            className="focus:outline-none"
                                            type="button"
                                            onClick={togglePasswordVisibility}
                                        >
                                            {isPasswordVisible ? <EyeSlashFilledIcon /> : <EyeFilledIcon />}
                                        </button>
                                    }
                                    label="Nueva contraseña"
                                    type={isPasswordVisible ? "text" : "password"}
                                />
                            </Skeleton>
                        </div>

                        <div className="flex justify-end mt-10 gap-4">
                            {editando ? (
                                <>
                                    <Button color="default" onPress={handleCancelarEdicion} isDisabled={loading}>
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
