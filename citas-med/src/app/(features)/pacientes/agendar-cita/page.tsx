
'use client';
import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import api from '../../../lib/axios';
import { useState } from 'react'
import MedicoGrid from './medicoGrid';

export default function PacienteDashboard() {

  const router = useRouter();
  const [loading, setLoading] = useState(true);

  // useEffect(() => {
  //   api.get('/auth/me')
  //     .then(res => {
  //       const rol = res.data.rol;

  //       if (rol !== 'PACIENTE') {
  //         router.push('/unauthorized'); 
  //       } else {
  //         setLoading(false); 
  //       }
  //     })
  //     .catch(() => {
  //       router.push('/login'); 
  //     });
  // }, []);

  // if (loading) return <p>Cargando...</p>

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4 text-blue-900">Agendar Cita Médica</h1>
      <p className="text-gray-600 mb-6">
        En esta sección puedes seleccionar al médico de tu preferencia, consultar su disponibilidad y agendar una cita de forma rápida y sencilla.
      </p>

      <div className="bg-gradient-to-br from-blue-50 to-blue-100 p-8 rounded-2xl shadow-xl border border-blue-200">
        <div className="mb-6">
          <h2 className="text-xl font-semibold text-blue-800">Selecciona un médico disponible</h2>
          <p className="text-blue-600 text-sm">
            Revisa los médicos activos, haz clic sobre uno para consultar horarios disponibles y continuar con el proceso de agendamiento.
          </p>
        </div>

        <MedicoGrid />
      </div>
    </div>

  );
}