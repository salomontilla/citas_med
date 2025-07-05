// store/medicoStore.ts
import { create } from 'zustand';

interface Medico {
  id: number;
  nombre: string;
  email: string;
  documento: string;
  telefono: string;
  especialidad: string;
}

interface MedicoStore {
  medicoSeleccionado: Medico | null;
  setMedicoSeleccionado: (medico: Medico) => void;
  limpiarMedico: () => void;
}

export const useMedicoStore = create<MedicoStore>((set) => ({
  medicoSeleccionado: null,
  setMedicoSeleccionado: (medico) => set({ medicoSeleccionado: medico }),
  limpiarMedico: () => set({ medicoSeleccionado: null }),
}));
