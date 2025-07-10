import { create } from 'zustand';

interface TokenState {
  isSessionExpiredModalOpen: boolean;
  setSessionExpiredModalOpen: (value: boolean) => void;
}

export const useTokenStore = create<TokenState>((set) => ({
  isSessionExpiredModalOpen: false,
  setSessionExpiredModalOpen: (value: boolean) => set({ isSessionExpiredModalOpen: value }),
}));

// Función auxiliar para disparar desde axios.ts
export const showSessionExpiredModal = () => {
  const { setState } = useTokenStore;
  setState({ isSessionExpiredModalOpen: true });
};
