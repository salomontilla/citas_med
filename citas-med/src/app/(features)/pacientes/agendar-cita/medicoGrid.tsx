'use client';
import { useState, useEffect } from 'react';
import { Card, Pagination, Select, SelectItem, Spinner } from '@heroui/react';
import { useMedicoStore } from '@/app/store/medicoStore';
import api from '@/app/lib/axios';

interface Medico {
  id: number;
  nombre: string;
  email: string;
  documento: string;
  telefono: string;
  especialidad: string;
}

export default function GridMedicos() {
  const [currentPage, setCurrentPage] = useState(1);
  const { medicoSeleccionado, setMedicoSeleccionado } = useMedicoStore();
  const [especialidadSeleccionada, setEspecialidadSeleccionada] = useState("Todas");
  const [medicos, setMedicos] = useState<Medico[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    api.get("/pacientes/ver-medicos")
      .then((response) => {
        setMedicos(response.data.content);
      })
      .catch((error) => {
        setError(error.message ||"No se pudieron cargar los médicos.");
      })
      .finally(() => setLoading(false));
  }, []);

  const pageSize = 4;
  const especialidades = ["Todas", "GENERAL", "CARDIOLOGIA"];

  // 1. Filtra primero
  const medicosFiltrados = especialidadSeleccionada === "Todas"
    ? medicos
    : medicos.filter((medico) => medico.especialidad === especialidadSeleccionada);

  // 2. Luego hace paginación sobre los filtrados
  const totalPages = Math.ceil(medicosFiltrados.length / pageSize);

  const paginatedMedicos = medicosFiltrados.slice(
    (currentPage - 1) * pageSize,
    currentPage * pageSize
  );

  // 3. Reinicia página al cambiar filtro
  useEffect(() => {
    setCurrentPage(1);
  }, [especialidadSeleccionada]);

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
        value={especialidadSeleccionada}
        onChange={(e) => setEspecialidadSeleccionada(e.target.value)}
      >
        {especialidades.map((esp) => (
          <SelectItem key={esp}>
            {esp}
          </SelectItem>
        ))}
      </Select>

      {/* Muestra los médicos filtrados y paginados */}
      {paginatedMedicos.length > 0 ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-2">
          {paginatedMedicos.map((medico) => (
            <Card
              isPressable
              key={medico.id}
              onPress={() => setMedicoSeleccionado(medico)}
              className={`p-4 shadow-md max-w-72 transition-colors duration-300 ${medicoSeleccionado?.id === medico.id ? "bg-blue-500 text-white" : "bg-white hover:bg-blue-50"
                }`}
            >
              <img
                src={"/medico.jpg"}
                alt={medico.nombre}
                className="w-full h-40 object-cover rounded-md mb-4"
              />
              <h3 className="text-lg font-semibold">{medico.nombre}</h3>
              <p className="text-sm">{medico.especialidad}</p>
            </Card>
          ))}
        </div>
      ) : (
        <div className="text-center text-blue-700 font-medium">No hay médicos disponibles para esta especialidad.</div>
      )}


      {/* PAGINADOR */}
      {totalPages > 1 && (
        <div className="flex justify-center z-0">
          <Pagination
            loop
            isCompact
            showControls
            total={totalPages}
            initialPage={1}
            page={currentPage}
            onChange={setCurrentPage}
            color="primary"
          />
        </div>
      )}

    </section>
  );
}
