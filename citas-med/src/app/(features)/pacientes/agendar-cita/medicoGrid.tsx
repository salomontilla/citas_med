'use client';
import { useState, useEffect } from 'react';
import { Card, Pagination, Select, SelectItem } from '@heroui/react';

type Medico = {
  id: number;
  nombre: string;
  especialidad: string;
  imagen: string;
};

const TODOS_LOS_MEDICOS: Medico[] = [
  // Simulación de médicos (reemplaza con datos reales o de API)
  { id: 1, nombre: "Dr. Carlos", especialidad: "Cardiología", imagen: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTi9pkWsBdG5b1shPiwNzUx517y1-V57tuQ1Q&s" },
  { id: 2, nombre: "Dra. Ana", especialidad: "Pediatría", imagen: "/medico.jpg" },
  { id: 3, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  { id: 4, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  { id: 5, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  { id: 6, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  { id: 7, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  { id: 8, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  { id: 9, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  { id: 10, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  { id: 11, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  { id: 12, nombre: "Dr. Juan", especialidad: "Neurología", imagen: "/medico.jpg" },
  // agrega más médicos...
];


export default function GridMedicos() {
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedMedico, setSelectedMedico] = useState<number | null>(null);
  const [especialidadSeleccionada, setEspecialidadSeleccionada] = useState("Todas");

  const pageSize = 4;
  const especialidades = ["Todas", "Cardiología", "Pediatría", "Dermatología", "Neurología"];

  // 1. Filtras primero
  const medicosFiltrados = especialidadSeleccionada === "Todas"
    ? TODOS_LOS_MEDICOS
    : TODOS_LOS_MEDICOS.filter((medico) => medico.especialidad === especialidadSeleccionada);

  // 2. Luego haces paginación sobre los filtrados
  const totalPages = Math.ceil(medicosFiltrados.length / pageSize);

  const paginatedMedicos = medicosFiltrados.slice(
    (currentPage - 1) * pageSize,
    currentPage * pageSize
  );

  // 3. Reiniciar página al cambiar filtro
  useEffect(() => {
    setCurrentPage(1);
  }, [especialidadSeleccionada]);


  return (
    <section className="flex flex-col gap-6">
      {/* GRID RESPONSIVE */}
      <Select
        label="Busca por especialidad"
        labelPlacement='outside'
        className="max-w-xs"
        value={especialidadSeleccionada}
        onChange={(e) => setEspecialidadSeleccionada(e.target.value)}
      >
        {especialidades.map((esp) => (
          <SelectItem key={esp}>
            {esp}
          </SelectItem>
        ))}
      </Select>
      {paginatedMedicos.length > 0 ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-2">
          {paginatedMedicos.map((medico) => (
            <Card
              isPressable
              key={medico.id}
              onPress={() => setSelectedMedico(medico.id)}
              className={`p-4 shadow-md max-w-72 transition-colors duration-300 ${selectedMedico === medico.id ? "bg-blue-500 text-white" : "bg-white hover:bg-blue-50"
                }`}
            >
              <img
                src={medico.imagen}
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
        <div className="flex justify-center">
          <Pagination
            loop
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
