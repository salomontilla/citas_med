'use client';
import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  NavbarMenuToggle,
  NavbarMenu,
  NavbarMenuItem,
  Link,
  Button,
} from "@heroui/react";
import { useState } from "react";


export default function App() {
  const [activeItem, setActiveItem] = useState<string | null>('inicio');
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const items = [
    { id: 'inicio', label: 'Inicio', href: '/#inicio' },
    { id: 'servicios', label: 'Servicios', href: '/#servicios' },
    { id: 'contacto', label: 'Contacto', href: '/#contacto' },
  ];

  return (
    <Navbar
      isBlurred={false}
      isMenuOpen={isMenuOpen}
      onMenuOpenChange={setIsMenuOpen}
    >
      <NavbarContent>
        <NavbarMenuToggle
          aria-label={isMenuOpen ? "Close menu" : "Open menu"}
          className="sm:hidden"
          onClick={() => setIsMenuOpen(prev => !prev)}
        />
        <NavbarBrand>
          <p className="font-bold text-inherit">CitasMed</p>
        </NavbarBrand>
      </NavbarContent>

      
      <NavbarContent className="hidden sm:flex gap-4" justify="center">
        {items.map((item) => (
          <NavbarItem key={item.id} isActive={activeItem === item.id}>
            <Link
              color={activeItem === item.id ? 'primary' : 'foreground'}
              href={item.href}
              onClick={() => setActiveItem(item.id)}
            >
              {item.label}
            </Link>
          </NavbarItem>
        ))}
      </NavbarContent>

      <NavbarContent justify="end">
        <NavbarItem>
          <Link href="/login">Iniciar Sesión</Link>
        </NavbarItem>
        <NavbarItem>
          <Button as={Link} color="primary" href="/register" variant="flat">
            Registrarse
          </Button>
        </NavbarItem>
      </NavbarContent>

      {/* Menú móvil */}
      <NavbarMenu>
        {items.map((item, index) => (
          <NavbarMenuItem key={index}>
            <Link
              className="w-full"
              size="lg"
              href={item.href}
              color={activeItem === item.id ? 'primary' : 'foreground'}
              onClick={() => {
                setActiveItem(item.id);
                setIsMenuOpen(false); 
              }}
            >
              {item.label}
            </Link>
          </NavbarMenuItem>
        ))}
      </NavbarMenu>
    </Navbar>
  );
}
