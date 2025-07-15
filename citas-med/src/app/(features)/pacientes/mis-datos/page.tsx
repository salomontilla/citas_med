'use client';

import { useState } from 'react';
import { Input, Button, addToast } from '@heroui/react';
import { div } from 'framer-motion/client';

interface DatosUsuario {
    nombre: string;
    email: string;
    documento: string;
    telefono: string;
}

export default function PerfilUsuario() {
    const [datos, setDatos] = useState<DatosUsuario>({
        nombre: 'Juan Pérez',
        email: 'juan.perez@email.com',
        documento: '123456789',
        telefono: '3101234567',
    });

    const [editando, setEditando] = useState(false);
    const [loading, setLoading] = useState(false);

    const handleChange = (field: keyof DatosUsuario, value: string) => {
        setDatos({ ...datos, [field]: value });
    };

    const guardarCambios = () => {
        setLoading(true);
        setLoading(false);
        setEditando(false);
        addToast({
            title: 'Cambios guardados',
            description: 'Tu perfil fue actualizado con éxito.',
            color: 'success',
            shouldShowTimeoutProgress: true,
            timeout: 5000,
        });
    };

    return (
        <div className='min-h-fit w-full flex items-center justify-center bg-blue-50 px-4'>
            <section className="w-full bg-blue-200 rounded-2xl max-w-4xl shadow-md">
                <div className='h-20 flex justify-center items-center rounded-t-2xl bg-gradient-to-r from-blue-500 to-blue-800'>
                    <h1 className='text-white text-2xl font-bold'>Mis Datos</h1>
                </div>

                <div className="bg-white w-full p-8 rounded-b-2xl">
                    <div className="flex flex-col gap-4 items-center mb-6">
                        <img src="/medico.jpg" alt="doctor" className='rounded-full h-24 w-24 object-cover' />
                        <div className='flex flex-col items-center'>
                            <h1 className='font-bold text-xl'>{datos.nombre}</h1>
                            <span className="text-sm text-gray-500">{datos.email}</span>
                        </div>
                    </div>

                    <div className='grid grid-cols-1 md:grid-cols-2 gap-4'>
                        <Input
                            label="Nombre"
                            color='primary'
                            value={datos.nombre}
                            onValueChange={(value) => handleChange('nombre', value)}
                            isDisabled={true}
                        />
                        <Input
                            label="Correo Electrónico"
                            color='primary'
                            value={datos.email}
                            onValueChange={(value) => handleChange('email', value)}
                            isDisabled={!editando}
                        />
                        <Input
                            label="Documento de Identidad"
                            color='primary'
                            value={datos.documento}
                            onValueChange={(value) => handleChange('documento', value)}
                            isDisabled={true}
                        />
                        <Input
                            label="Teléfono"
                            color='primary'
                            value={datos.telefono}
                            onValueChange={(value) => handleChange('telefono', value)}
                            isDisabled={!editando}
                        />
                        <Input
                            label="Fecha de nacimiento"
                            color='primary'
                            value="01/01/1990"
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
                </div>
            </section>
        </div>

    );
}
