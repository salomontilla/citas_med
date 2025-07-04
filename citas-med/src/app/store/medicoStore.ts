import { create } from 'zustand';

type MedicoState = {
  medicoSeleccionado: number | null;
  setMedicoSeleccionado: (id: number) => void;
};

export const useMedicoStore = create<MedicoState>((set) => ({
  medicoSeleccionado: null,
  setMedicoSeleccionado: (id) => set({ medicoSeleccionado: id }),
}));
