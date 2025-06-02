import {NavbarComponent} from './ui/navbarComponent';
import Login from './ui/Login';
import Register from './ui/register';
import Image from 'next/image'
import { Span } from 'next/dist/trace';
export default function Home() {
  return (
    <div>

      <header>
        <NavbarComponent />
      </header>
      <main className='bg-[#03045e] md:min-h-screen' >
        <div className="text-white text-center px-4">
  <h1 className="text-4xl font-bold">
    Bienvenido a <span className="text-blue-300">CitasMed</span>
  </h1>
  <p className="text-lg text-blue-100 opacity-80">
    Un sistema moderno de gestión de salud construido con Next.js, TypeScript y Spring Boot.
  </p>
</div>
        <div className='flex flex-col md:flex-row items-center justify-center gap-y-3'>
          <section className="w-full md:w-1/2 px-6 py-10 flex flex-col gap-6 text-white">
            <h1 className="text-3xl font-bold text-center">
              ¿Eres nuevo? Regístrate reserva citas!
            </h1>
            
              <Register />
            
          </section>

          <section className='w-1/2 flex items-center text-center flex-col md:flex h-full '>

           <div className="flex flex-col py-10 w-screen md:w-full  items-center text-center text-white">
              <h1 className="font-bold text-3xl leading-tight">
                Ofrecemos <span className="text-blue-300">servicios</span> médicos de <span className="text-blue-300">confianza</span>!
              </h1>
              <p className="text-2xl text-blue-100 mt-6 px-7 opacity-80">
                Reserva una cita con nosotros de una forma rápida, fácil y sencilla.
              </p>
              <Image src="/banner_medicos.webp" alt="Doctor" width={300} height={300} className="object-contain aspect-square hidden md:flex" />
             
          </div>
            
          </section>
        </div>
      </main>
      <footer>
        <div className="bg-gray- text-center py-4">
          <p className="text-gray-600">© 2025 CitasMed. Todos los derechos reservados.</p>
        </div>
      </footer>
        <style>
        {
          `h1 {
            color: #f3f4f6;}`
        }
        </style>
    </div>
  );
}
