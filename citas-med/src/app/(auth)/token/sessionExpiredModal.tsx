'use client';

import { Modal, ModalContent, ModalHeader, ModalBody, ModalFooter, Button } from "@heroui/react";
import { useTokenStore } from "@/app/store/tokenStore";
import { useRouter } from "next/navigation";

export default function SessionExpiredModal() {
  const router = useRouter();
  const { isSessionExpiredModalOpen, setSessionExpiredModalOpen } = useTokenStore();

  const handleRedirect = () => {
    setSessionExpiredModalOpen(false);
    router.push("/login");
  };

  return (
    <Modal isOpen={isSessionExpiredModalOpen} onOpenChange={setSessionExpiredModalOpen} isDismissable={false}>
      <ModalContent>
        <ModalHeader>Sesión expirada</ModalHeader>
        <ModalBody>
          <p>Tu sesión ha expirado. Por favor, vuelve a iniciar sesión para continuar.</p>
        </ModalBody>
        <ModalFooter>
          <Button color="primary" onPress={handleRedirect}>
            Iniciar sesión
          </Button>
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
}
