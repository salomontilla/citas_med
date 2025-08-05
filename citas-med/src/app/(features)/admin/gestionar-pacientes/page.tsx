import { Table } from "lucide-react";
import TableExample from "./tableExample";
import PacientesGrid from "./pacientesGrid";

export default function PacienteDashboard() {
  return (
    <section className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-gray-100 to-gray-200 px-4">
      <div className="w-full bg-white rounded-lg shadow-md p-8 mt-8">
      <div className="flex items-center mb-6 gap-3">
        <Table className="w-8 h-8 text-blue-600" />
        <h1 className="text-3xl font-extrabold text-gray-800">Gestión de Pacientes</h1>
      </div>
      <p className="text-gray-600 mb-8">
        Administra y visualiza la información de los pacientes registrados en el sistema.
      </p>
      <PacientesGrid />
      </div>
    </section>
  );
}