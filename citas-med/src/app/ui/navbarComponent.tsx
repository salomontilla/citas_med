
import { Button, Navbar, NavbarBrand, NavbarCollapse, NavbarLink, NavbarToggle } from "flowbite-react";

export function NavbarComponent() {
  return (
    <Navbar fluid >
      <NavbarBrand href="https://flowbite-react.com">
        
        <span className="self-center whitespace-nowrap text-xl font-semibold dark:text-white">CitasMed</span>
      </NavbarBrand>
      <div className="flex md:order-2">
        <Button>Iniciar Sesion</Button>
        <NavbarToggle />
      </div>
      <NavbarCollapse>
        <NavbarLink href="#" active>
          Home
        </NavbarLink>
      </NavbarCollapse>
    </Navbar>
  );
}
