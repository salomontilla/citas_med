'use client';
import { useState, useEffect } from 'react';
import { Button, Card, Input, Modal, ModalBody, ModalContent, ModalFooter, ModalHeader, Pagination, Select, SelectItem, Spinner } from '@heroui/react';
import { useMedicoStore } from '@/app/store/medicoStore';
import api from '@/app/lib/axios';
import { useRouter } from 'next/navigation';
import { Search } from 'lucide-react';

type Medico = {
    id: number;
    nombre: string;
    documento: string;
    email: string;
    isActivo: boolean;
    telefono: string;
    especialidad: string;
}

const estados = ["Todos", "Activo", "Inactivo"];

export default function Gridmedicos() {
    const [currentPage, setCurrentPage] = useState(1);
    const [estadoSeleccionado, setEstadoSeleccionado] = useState("Todos");
    const [medicos, setMedicos] = useState<Medico[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [medicoSeleccionado, setMedicoSeleccionado] = useState<Medico | null>(null);
    const [busqueda, setBusqueda] = useState("");
    const router = useRouter();

    const [page, setPage] = useState(1);
    const [rowsPerPage, setRowsPerPage] = useState(10);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        api.get("/admin/medicos", {
            params: {
                page: currentPage - 1,
                size: rowsPerPage,
                search: busqueda || null,
                estado: estadoSeleccionado
            }
        })
            .then((res) => {
                setMedicos(res.data.content);
                setTotalPages(res.data.totalPages);
            })
            .catch((err) => {
                setError(err.message);
            })
            .finally(() => setLoading(false));
    }, [currentPage, rowsPerPage, busqueda, estadoSeleccionado]);

    // 1️⃣ Filtro combinado por estado y por nombre
    const medicosFiltrados = medicos
        // Filtro por estado
        .filter((medico) => {
            if (estadoSeleccionado === "Todos") return true;
            return estadoSeleccionado === "Activo" ? medico.isActivo : !medico.isActivo;
        })
        // Filtro por nombre
        .filter((medico) =>
            medico.nombre.toLowerCase().includes(busqueda.toLowerCase())
        );
    console.log(medicosFiltrados.length);

    // 2️⃣ Calcular total de páginas
    useEffect(() => {
        setTotalPages(totalPages);
    }, [medicosFiltrados, rowsPerPage]);

    // 3️⃣ Aplicar paginación
    const paginatedMedicos = medicosFiltrados.slice(
        (currentPage - 1) * rowsPerPage,
        currentPage * rowsPerPage
    );

    // 4️⃣ Reiniciar página al cambiar estado o búsqueda
    useEffect(() => {
        setCurrentPage(1);
    }, [estadoSeleccionado, busqueda]);

    if (loading) return <Spinner />
    if (error) return <div className="text-red-500 text-center">{error}</div>;

    const handleMedicoSeleccionado = (medico: Medico) => {
        setMedicoSeleccionado((prev) => prev?.id === medico.id ? null : medico);
        router.push(`/admin/gestionar-medicos/${medico.id}`);

    };

    return (
        <section className="flex flex-col  gap-4">
            <div className="flex gap-4">
                <Input
                    startContent={<Search />}
                    isClearable
                    onClear={() => setBusqueda("")}
                    className='max-w-xs'
                    placeholder="Buscar medico"
                    value={busqueda}
                    onChange={(e) => setBusqueda(e.target.value)}
                />
                {/* FILTRO */}
                <Select
                    color='primary'
                    label="Busca por estado"
                    labelPlacement='inside'
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
            </div>


            {/* Muestra los medicos filtrados y paginados */}
            {paginatedMedicos.length > 0 ? (
                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-5 gap-2">
                    {paginatedMedicos.map((medico) => (
                        <Card
                            isPressable
                            key={medico.id}
                            onPress={() => handleMedicoSeleccionado(medico)}
                            className={`group relative flex flex-col justify-between p-4 rounded-2xl border transition-all duration-300 shadow-sm max-w-72 cursor-pointer
                            ${medicoSeleccionado?.id === medico.id
                                    ? "border-blue-600 bg-blue-50 text-blue-900 shadow-md"
                                    : "bg-white hover:bg-blue-100 border-gray-200"
                                }`
                            }
                        >
                            <div className="relative w-full h-40 mb-4 overflow-hidden rounded-xl">
                                <img
                                    src="/medico.jpg"
                                    alt={`Foto de ${medico.nombre}`}
                                    className="w-full h-full object-cover transform group-hover:scale-105 transition-transform duration-300"
                                />
                            </div>

                            <div className="space-y-1">
                                <h3 className="text-lg font-bold truncate">{medico.nombre}</h3>
                                <p className="text-sm text-gray-600 truncate group-hover:text-gray-800">
                                    {medico.email}
                                </p>

                                <p className="text-sm text-gray-600 truncate group-hover:text-gray-800">
                                    {medico.especialidad}
                                </p>
                            </div>

                            <div className="mt-3 text-xs text-gray-500 flex justify-between">
                                <span>📞 {medico.telefono}</span>
                                <span>🆔 {medico.documento}</span>
                            </div>
                        </Card>

                    ))}
                </div>
            ) : (
                <div className="text-center text-blue-700 font-medium">No hay médicos disponibles.</div>
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
