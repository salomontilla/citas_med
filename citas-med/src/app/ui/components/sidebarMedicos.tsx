'use client'
import React, { useEffect, useState } from "react";
import { CalendarDays, ClipboardList, UserCog, LogOut, Menu, X } from 'lucide-react';
import { motion } from "framer-motion";
import { useRouter } from "next/navigation";
import {
  Modal,
  ModalContent,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Button,
  useDisclosure,
  Alert,
  Skeleton,
} from "@heroui/react";
import api from '../../lib/axios';



export const Sidebar = () => {
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState<string>("Agendar Cita");
  const [isLoaded, setIsLoaded] = useState(false);

  useEffect(() => {
    const saved = localStorage.getItem("sidebar-selected");
    if (saved) setSelected(saved);
    setIsLoaded(true);
  }, []);

  const handleSelect = (option: string) => {
    setSelected(option);
    localStorage.setItem("sidebar-selected", option);
  };

  return (
    
      <motion.nav
        layout
        className="fixed z-10 md:sticky top-0 h-screen shrink-0 border-r border-slate-300 bg-white p-2"
        style={{
          width: open ? "225px" : "fit-content",
        }}
      >
        <TitleSection open={open} />

        <div className="space-y-1">
          <Skeleton className="rounded-lg" isLoaded={isLoaded}>
          <Option
            Icon={CalendarDays}
            title="Citas Programadas"
            selected={selected}
            setSelected={handleSelect}
            open={open}
            href="/medicos/ver-citas"
          />
          </Skeleton>

          <Skeleton className="rounded-lg" isLoaded={isLoaded}>
          <Option
            Icon={UserCog}
            title="Mis datos"
            selected={selected}
            setSelected={handleSelect}
            open={open}
            href="/medicos/mis-datos"
          />
          </Skeleton>
          <OptionLogout open={open} />
        </div>

        <ToggleClose open={open} setOpen={setOpen} />
      </motion.nav>
    
  );
};
type OptionLogoutProps = {
  open: boolean;
};

//Este componente maneja la opción de cerrar sesión en la barra lateral
const OptionLogout = ({ open }: OptionLogoutProps) => {
  const { isOpen, onOpen, onOpenChange } = useDisclosure();
  const [isVisibleAlert, setIsVisibleAlert] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");


  const router = useRouter();

  const handleLogout = () => {
    setIsLoading(true);
    setIsVisibleAlert(false);
    setError("");

    api.post("/auth/logout")
      .then(() => {
        setIsLoading(false);
        router.push("/login")
      })
      .catch((error) => {
        setIsLoading(false);
        setIsVisibleAlert(true);
        setError(error.message || "Error al cerrar sesión");
      })
  }
  return (
    <>
      <Modal isOpen={isOpen} onOpenChange={onOpenChange}>
        <ModalContent>

          {(onClose) => (
            <>

              <ModalHeader className="flex flex-col gap-1">Cerrar Sesion</ModalHeader>
              <ModalBody>
                <p>
                  Deseas cerrar sesión? Esto te llevará a la página de inicio de sesión y perderás tu sesión actual.
                </p>

              </ModalBody>
              <ModalFooter className="flex flex-col">
                <div className="flex w-full justify-center gap-3">
                  <Button color="default" variant="light" onPress={onClose}>
                    Cancelar
                  </Button>
                  <Button isLoading={isLoading} color="danger" onPress={handleLogout}>
                    Cerrar sesión
                  </Button>
                </div>
                <Alert
                  color="danger"
                  description={error}
                  title="Error"
                  isVisible={isVisibleAlert}
                  variant="faded"
                  onClose={() => setIsVisibleAlert(false)}
                />
              </ModalFooter>
            </>
          )}
        </ModalContent>
      </Modal>

      <motion.button
        layout
        whileTap={{ scale: 0.95 }}
        onClick={() => onOpen()}
        className="relative flex h-10 w-full items-center rounded-md transition-colors text-slate-500 hover:bg-red-200 "
      >
        <motion.div
          layout
          className="grid h-full w-10 place-content-center text-lg"
        >

          <LogOut />
        </motion.div>
        {open && (
          <motion.span
            layout
            initial={{ opacity: 0, y: 12 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.125 }}
            className="text-xs font-medium"
          >
            Cerrar sesión
          </motion.span>
        )}


      </motion.button>
    </>
  )
}

type OptionProps = {
  Icon: React.ElementType;
  title: string;
  selected: string;
  setSelected: (title: string) => void;
  open: boolean;
  href?: string;
};

const Option = ({ Icon, title, selected, setSelected, open, href }: OptionProps) => {
  const router = useRouter();

  const handleClick = () => {
    setSelected(title);
    if (href) {
      router.push(href);
    }
  };

  return (
    <motion.button
      layout
      onClick={() => handleClick()}
      className={`relative flex h-10 w-full items-center rounded-md transition-colors ${selected === title ? "bg-blue-100 text-blue-800" : "text-slate-500 hover:bg-slate-100"}`}
    >
      <motion.div
        layout
        className="grid h-full w-10 place-content-center text-lg"
      >

        <Icon />
      </motion.div>
      {open && (
        <motion.span
          layout
          initial={{ opacity: 0, y: 12 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ delay: 0.125 }}
          className="text-xs font-medium"
        >
          {title}
        </motion.span>
      )}


    </motion.button>
  );
};

type TitleSectionProps = {
  open: boolean;
};
const TitleSection = ({ open }: TitleSectionProps) => {
  const [nombre, setNombre] = useState("");
  const [rol, setRol] = useState("");

  useEffect(() => {
    api.get("/auth/me")
      .then((r) => {
        setNombre(r.data.nombre);
        setRol(r.data.rol);
      }).catch((error) => {
        console.log("Error al obtener los datos del usuario:", error.response.status);
      })
  }, []);
  return (
    <div className="mb-3 border-b border-slate-300 pb-3">
      <div className="flex cursor-auto items-center justify-between rounded-md transition-colors">
        <div className="flex items-center gap-2">
          <Logo />
          {open && (
            <motion.div
              layout
              initial={{ opacity: 0, y: 12 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.125 }}
            >
              <span className="block text-xs font-semibold">{nombre}</span>
              <span className="block text-xs text-slate-500">{rol}</span>
            </motion.div>
          )}
        </div>

      </div>
    </div>
  );
};

const Logo = () => {
  return (
    <motion.div
      layout
      className="grid size-10 shrink-0 place-content-center rounded-md bg-indigo-600"
    >
      <svg
        width="24"
        height="24"
        viewBox="0 0 50 39"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        className="fill-slate-50"
      >
        <path
          d="M16.4992 2H37.5808L22.0816 24.9729H1L16.4992 2Z"
          stopColor="#000000"
        ></path>
        <path
          d="M17.4224 27.102L11.4192 36H33.5008L49 13.0271H32.7024L23.2064 27.102H17.4224Z"
          stopColor="#000000"
        ></path>
      </svg>
    </motion.div>
  );
};

type ToggleCloseProps = {
  open: boolean;
  setOpen: React.Dispatch<React.SetStateAction<boolean>>;
};

const ToggleClose = ({ open, setOpen }: ToggleCloseProps) => {

  return (
    <motion.button
      layout
      onClick={() => setOpen((pv) => !pv)}
      className="absolute bottom-0 left-0 right-0 border-t border-slate-300 transition-colors hover:bg-slate-100"
    >
      <div className="flex items-center p-2">
        <motion.div
          layout
          className="grid size-10 place-content-center text-lg"
          animate={{ rotate: open ? 180 : 0 }}
          transition={{ duration: 0.3 }}
        >
          {open ? (
            <X className="w-6 h-6" />
          ) : (
            <Menu className="w-6 h-6" />
          )}
        </motion.div>

        {open && (
          <motion.span
            layout
            initial={{ opacity: 0, y: 12 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ delay: 0.125 }}
            className="text-xs font-medium"
          >
            Esconder
          </motion.span>
        )}
      </div>
    </motion.button>
  );
};


