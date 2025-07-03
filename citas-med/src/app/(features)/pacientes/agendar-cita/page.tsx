
'use client';
import { useEffect } from 'react';
import { useRouter } from 'next/navigation';
import api from '../../../lib/axios';
import { useState } from 'react'
import  MedicoGrid  from './medicoGrid';

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
    <div className="">
      <h1 className="text-2xl font-bold mb-4">Bienvenido al Dashboard del Paciente</h1>
      <p className="text-gray-600">Aquí podrás gestionar tus citas médicas y acceder a tu información personal.</p>
      <div>
        <MedicoGrid/>
      </div>
    </div>
  );
}