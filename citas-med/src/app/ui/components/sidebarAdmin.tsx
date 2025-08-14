'use client'
import React, { useEffect, useState } from "react";
import { BriefcaseMedical, Contact } from 'lucide-react';
import { motion } from "framer-motion";
import { TitleSection, Option, OptionLogout, ToggleClose } from "./sidebarPacientes";
import { Skeleton } from "@heroui/react";



export const Sidebar = () => {
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState<string>("Gestionar Pacientes");
  const [isLoaded, setIsLoaded] = useState(false);

  useEffect(() => {
  
      const saved = localStorage.getItem("sidebar-selected");
      
      if (saved !== "Gestionar Pacientes" && saved !== "Gestionar Medicos") {
        setSelected("Gestionar Pacientes");
        localStorage.setItem("sidebar-selected", "Gestionar Pacientes");
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
            Icon={Contact}
            title="Gestionar Pacientes"
            selected={selected}
            setSelected={handleSelect}
            open={open}
            href="/admin/gestionar-pacientes"
          />
        </Skeleton>

        <Skeleton className="rounded-lg" isLoaded={isLoaded}>
          
            <Option
              Icon={BriefcaseMedical}
              title="Gestionar Medicos"
              selected={selected}
              setSelected={handleSelect}
              open={open}
              href="/admin/gestionar-medicos"
            />
          
        </Skeleton>
        <OptionLogout open={open} />
      </div>

      <ToggleClose open={open} setOpen={setOpen} />
    </motion.nav>

  );
};
