'use client';
import { useState, useEffect } from 'react';
import { Card, Pagination, Select, SelectItem, Spinner } from '@heroui/react';
import { useMedicoStore } from '@/app/store/medicoStore';
import api from '@/app/lib/axios';

type Paciente = {
    id: number;
    nombreCompleto: string;
    documento: string;
    email: string;
    estado: string;
    telefono: string;
    fechaNacimiento: string;
}

const estados = ["Todos", "Activo", "Inactivo"];

export default function GridPacientes() {
    const [currentPage, setCurrentPage] = useState(1);
    const [estadoSeleccionado, setEstadoSeleccionado] = useState("Todos");
    const [pacientes, setPacientes] = useState<Paciente[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [pacienteSeleccionado, setPacienteSeleccionado] = useState<Paciente | null>(null);

    const [page, setPage] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        api.get(`/admin/pacientes?page=${page - 1}&size=${rowsPerPage}`)
            .then((response) => {
                setPacientes(response.data.content);
                console.log(response.data.totalPages);
                setTotalPages(response.data.totalPages);
                console.log(pacientes);
            })
            .catch((error) => {
                setError(error.message || "No se pudieron cargar los pacientes.");
            })
            .finally(() => setLoading(false));
    }, [page]);


    // 1. Filtra primero
    const pacientesFiltrados = estadoSeleccionado === "Todos"
        ? pacientes
        : pacientes.filter((paciente) => paciente.estado === estadoSeleccionado);

    // 2. Luego hace paginación sobre los filtrados


    const paginatedPacientes = pacientesFiltrados.slice(
        (currentPage - 1) * rowsPerPage,
        currentPage * rowsPerPage
    );

    // 3. Reinicia página al cambiar filtro
    useEffect(() => {
        setCurrentPage(1);
    }, [estadoSeleccionado]);

    if (loading) return <Spinner />
    if (error) return <div className="text-red-500 text-center">{error}</div>;
    return (
        <section className="flex flex-col gap-4">
            {/* FILTRO */}
            <Select
                color='primary'
                label="Busca por especialidad"
                labelPlacement='outside'
                className="max-w-xs z-0"
                value={estadoSeleccionado}
                onChange={(e) => setEstadoSeleccionado(e.target.value)}
            >
                {estados.map((estado) => (
                    <SelectItem key={estado}>
                        {estado}
                    </SelectItem>
                ))}
            </Select>

            {/* Muestra los pacientes filtrados y paginados */}
            {paginatedPacientes.length > 0 ? (
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-2">
                    {paginatedPacientes.map((paciente) => (
                        <Card
                            isPressable
                            key={paciente.id}
                            onPress={() => setPacienteSeleccionado(paciente)}
                            className={`group relative flex flex-col justify-between p-4 rounded-2xl border transition-all duration-300 shadow-sm max-w-72 cursor-pointer
    ${pacienteSeleccionado?.id === paciente.id
                                    ? "border-blue-600 bg-blue-50 text-blue-900 shadow-md"
                                    : "bg-white hover:bg-blue-100 border-gray-200"
                                }`}
                        >
                            <div className="relative w-full h-40 mb-4 overflow-hidden rounded-xl">
                                <img
                                    src="/medico.jpg"
                                    alt={`Foto de ${paciente.nombreCompleto}`}
                                    className="w-full h-full object-cover transform group-hover:scale-105 transition-transform duration-300"
                                />
                            </div>

                            <div className="space-y-1">
                                <h3 className="text-lg font-bold truncate">{paciente.nombreCompleto}</h3>
                                <p className="text-sm text-gray-600 truncate group-hover:text-gray-800">
                                    {paciente.email}
                                </p>
                            </div>

                            <div className="mt-3 text-xs text-gray-500 flex justify-between">
                                <span>📞 {paciente.telefono}</span>
                                <span>🆔 {paciente.documento}</span>
                            </div>
                        </Card>

                    ))}
                </div>
            ) : (
                <div className="text-center text-blue-700 font-medium">No hay pacientes disponibles para esta especialidad.</div>
            )}


            {/* PAGINADOR */}
            {totalPages > 0 ? (
            <div className="flex w-full justify-center">
              <Pagination
                isCompact
                showControls
                showShadow
                color="primary"
                page={page}
                total={totalPages}
                onChange={(currentPage) => setPage(currentPage)}
              />
            </div>
          ) : null}

        </section>
    );
}
