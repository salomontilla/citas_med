'use client'
import React, { useEffect, useState } from "react";
import { CalendarDays, ClipboardList, UserCog} from 'lucide-react';
import { motion } from "framer-motion";
import {
  Skeleton,
  Tooltip,
} from "@heroui/react";
import { TitleSection, Option, OptionLogout, ToggleClose } from "./sidebarPacientes";



export const Sidebar = () => {
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState<string>("Citas Programadas");
  const [isLoaded, setIsLoaded] = useState(false);

  useEffect(() => {

    const saved = localStorage.getItem("sidebar-selected");

    // Por defecto 
    if (saved !== "Citas Programadas" && saved !== "Mis datos" && saved !== "Mi Horario") {
      setSelected("Citas Programadas");
      localStorage.setItem("sidebar-selected", "Citas Programadas");
    } else if (saved) {
      setSelected(saved);
    }
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
          <Skeleton className="rounded-lg" isLoaded={isLoaded}>
            <Tooltip content="Ver mis horarios" placement="right">
              <Option
                Icon={ClipboardList}
                title="Mi Horario"
                selected={selected}
                setSelected={handleSelect}
                open={open}
                href="/medicos/mis-horarios"
              />
            </Tooltip>
          </Skeleton>
          <OptionLogout open={open} />
        </div>

        <ToggleClose open={open} setOpen={setOpen} />
      </motion.nav>
    
  );
};




