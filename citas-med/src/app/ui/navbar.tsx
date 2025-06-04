'use client';
import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
  Link,
  Button,
} from "@heroui/react";


export default function App() {

  return (
    <Navbar >
      <NavbarContent>
        
        <NavbarBrand>
          <p className="font-bold text-inherit">CitasMed</p>
        </NavbarBrand>
      </NavbarContent>

      <NavbarContent className="hidden sm:flex gap-4" justify="center">
        <NavbarItem>
          <Link color="primary" href="#inicio">
            Inicio
          </Link>
        </NavbarItem>
        <NavbarItem>
          <Link href="#servicios" color="foreground" >
            Servicios
          </Link>
        </NavbarItem>
        <NavbarItem>
          <Link color="foreground" href="#contacto">
            Contacto
          </Link>
        </NavbarItem>
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

