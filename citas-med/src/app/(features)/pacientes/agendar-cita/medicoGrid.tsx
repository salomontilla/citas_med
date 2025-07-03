'use client';
import { useState} from 'react';
import { Card, Pagination } from '@heroui/react';

type Medico = {
  id: number;
  nombre: string;
  especialidad: string;
  imagen: string;
};

const TODOS_LOS_MEDICOS: Medico[] = [
  // Simulación de médicos (reemplaza con datos reales o de API)
  { id: 1, nombre: "Dr. Carlos", especialidad: "Cardiología", imagen: "/medico.jpg" },
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

const pageSize = 3; // 6 cards por página

export default function GridMedicos() {
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedMedico, setSelectedMedico] = useState<number | null>(null);
  const totalPages = Math.ceil(TODOS_LOS_MEDICOS.length / pageSize);

  const paginatedMedicos = TODOS_LOS_MEDICOS.slice(
    (currentPage - 1) * pageSize,
    currentPage * pageSize
  );

  return (
    <section className="p-4 flex flex-col gap-6">
      {/* GRID RESPONSIVE */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
        {paginatedMedicos.map((medico) => (
          <div
          key={medico.id}
          onClick={() => setSelectedMedico(medico.id)}
          className={`cursor-pointer rounded-xl p-4 shadow-md max-w-md transition-colors duration-300 ${
            selectedMedico === medico.id
              ? "bg-blue-500 text-white"
              : "bg-white hover:bg-blue-50"
          }`}
        >
          <img
            src={medico.imagen}
            alt={medico.nombre}
            className="w-full h-40 object-cover rounded-md mb-4"
          />
          <h3 className="text-lg font-semibold">{medico.nombre}</h3>
          <p className="text-sm">{medico.especialidad}</p>
          
      
        
        </div>
        ))}
      </div>

      {/* PAGINADOR */}
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
    </section>
  );
}
