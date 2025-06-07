'use client';
import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  Link,
  Button,
} from "@heroui/react";
 import { useState } from "react";


export default function App() {

  const [activeItem, setActiveItem] = useState<string | null>('inicio');
   const items = [
    { id: 'inicio', label: 'Inicio', href: '/#inicio' },
    { id: 'servicios', label: 'Servicios', href: '/#servicios' },
    { id: 'contacto', label: 'Contacto', href: '/#contacto' },
  ];

  return (
    <Navbar shouldHideOnScroll isBlurred= {false} >
      <NavbarContent>
        
        <NavbarBrand>
          <p className="font-bold text-inherit">CitasMed</p>
        </NavbarBrand>
      </NavbarContent>
     

      <NavbarContent className="hidden sm:flex gap-4" justify="center">
        {
          items.map((item) => (
        <NavbarItem
          key={item.id}
          isActive={activeItem === item.id}
          
        >
          <Link color={activeItem === item.id ? 'primary' : 'foreground'} onClick={() => setActiveItem(item.id)}  href={item.href}>{item.label}</Link>
        </NavbarItem>
      ))
        }
      </NavbarContent>
      <NavbarContent justify="end">
        <NavbarItem className="">
          <Link href="/login" >Inicar Sesion</Link>
        </NavbarItem>
        <NavbarItem>
          <Button as={Link} color="primary" href="/register" variant="flat">
            Registrarse
          </Button>
        </NavbarItem>
      </NavbarContent>
    </Navbar>
  );
}

