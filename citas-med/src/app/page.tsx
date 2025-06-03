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
      <main className='bg-[#03045e] md:min-h-screen pt-4 relative' >
       <div className="bubble left-10 z-0" />
       <div className="bubble left-1/3 delay-1000 z-0" />
       <div className="bubble left-2/3 delay-2000 z-0" />

        <div className="text-white text-center px-4 z-10">
          <h1 className="text-4xl font-bold">
            Bienvenido a <span className="text-blue-300">CitasMed</span>
          </h1>
          <p className="text-lg text-blue-100 opacity-80">
            Un sistema moderno de gestión de salud construido con Next.js, TypeScript y Spring Boot.
          </p>
        </div>
        <div className='flex flex-col md:flex-row items-center justify-center gap-y-3 md:gap-x-10'>
          <section className="w-full md:w-1/2 px-6 py-10 flex flex-col items-center gap-6 text-white">
            <h1 className="text-3xl font-bold text-center">
              ¿Eres nuevo? Regístrate y tus reserva citas!
            </h1>
            
              <Register />
            
          </section>

          <section className='w-1/2 flex items-center text-center flex-col md:flex-row h-full'>

           <div className="flex flex-col py-10 w-screen  items-center text-white">
              <h1 className="font-bold text-3xl leading-tight">
                Ofrecemos <span className="text-blue-300">servicios</span> médicos de <span className="text-blue-300">confianza</span>!
              </h1>
              <p className="text-2xl text-blue-100 mt-6 px-7 opacity-80">
                Inicia sesión para acceder a tu cuenta y gestionar tus citas médicas.
              </p>
              <div className="relative w-[400px] h-[400px] hidden md:flex">
                <Image
                  src="/banner_medicos.webp"
                  alt="Doctor"
                  fill
                  className="object-contain aspect-square"
                />
                
                <div className="absolute bottom-0 left-0 w-full h-[30%] bg-gradient-to-t from-[#03045e] to-transparent pointer-events-none" />
                
            </div>
            <button
              type="submit"
              className="w-full max-w-3xs px-4 py-2 rounded-2xl text-white bg-transparent border hover:bg-blue-700 z-10 mt-7"
            >
                  Iniciar Sesión
            </button>
             
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
          `
          .bubble {
            position: absolute;
            bottom: -50px;
            width: 40px;
            height: 40px;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 50%;
            animation: rise 3s infinite ease-in;
        }

        @keyframes rise {
          0% { transform: translateY(0) scale(1); }
          100% { transform: translateY(-500px) scale(1.5); opacity: 0; }
        }
          `
        }
      </style>
    </div>
  );
}
