'use client';

import { useEffect, useState } from 'react';
import { Input, Button, addToast } from '@heroui/react';
import api from '@/app/lib/axios';

interface DatosUsuario {
    nombreCompleto?: string;
    email?: string;
    documento?: string;
    telefono?: string;
    fechaNacimiento?: string;
}

export default function PerfilUsuario() {
    const [datos, setDatos] = useState<DatosUsuario>({
        nombreCompleto: '',
        email: '',
        documento: '',
        telefono: '',
        fechaNacimiento: ''
    });
    const [editando, setEditando] = useState(false);
    const [loading, setLoading] = useState(false);
    const [correo, setCorreo] = useState('');
    const [telefono, setTelefono] = useState('');
    const [contrasena, setContrasena] = useState('');

    const obtenerDatos = () => {
        api.get('/pacientes/mis-datos')
            .then((response) => {
                setDatos(response.data);
            })
            .catch((error) => {
                addToast({
                    title: 'Error',
                    description: 'No se pudieron obtener los datos del usuario.',
                    color: 'danger',
                    shouldShowTimeoutProgress: true,
                    timeout: 5000,
                });
            });
    };

    // Obtener los datos del usuario al cargar el componente
    useEffect(() => {
        obtenerDatos();
    }, []);

    // Actualizar el correo electrónico al cargar el componente
    useEffect(() => {
        if (datos.email) {
            setCorreo(datos.email);
        }
    }, [datos.email]);
    // Obtener el teléfono del usuario al cargar el componente
    useEffect(() => {
        if (datos.telefono) {
            setTelefono(datos.telefono);
        }
    }, [datos.telefono]);

    const editarDatos = () => {
        setLoading(true);
        setEditando(true);
        api.patch('/pacientes/editar-datos', {
            nombreCompleto: null,
            email: correo,
            telefono: telefono,
            documento: null,
            fechaNacimiento: null,

        })
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
        if (!correo || !telefono) {
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

        if (correo === datos.email && telefono === datos.telefono) {
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
                            <h1 className='font-bold text-xl'>{datos.nombreCompleto}</h1>
                            <span className="text-sm text-gray-500">{datos.email}</span>
                        </div>
                    </div>
                    <form>
                        <div className='grid grid-cols-1 md:grid-cols-2 gap-4'>
                            <Input
                                label="Nombre"
                                color='primary'
                                value={datos.nombreCompleto}
                                isDisabled={true}
                            />
                            <Input
                                label="Correo Electrónico"
                                color='primary'
                                value={correo}
                                onValueChange={setCorreo}
                                isDisabled={!editando}
                            />
                            <Input
                                label="Documento de Identidad"
                                color='primary'
                                value={datos.documento}
                                isDisabled={true}
                            />
                            <Input
                                label="Teléfono"
                                color='primary'
                                value={telefono}
                                onValueChange={setTelefono}
                                isDisabled={!editando}
                            />
                            <Input
                                label="Fecha de nacimiento"
                                color='primary'
                                value={datos.fechaNacimiento}
                                isDisabled={true}
                            />
                        </div>

                        <div className="flex justify-end mt-10 gap-4">
                            {editando ? (
                                <>
                                    <Button color="default" onPress={() => setEditando(false)} isDisabled={loading}>
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
