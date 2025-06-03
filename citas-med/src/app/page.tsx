import {NavbarComponent} from './ui/navbarComponent';
import MainHero from './ui/mainHero';
import Cta from './ui/cta';

export default function Home() {
  return (
    <div>
      <header>
        <NavbarComponent />
      </header>
      <main className='bg-[#03045e] md:h-full lg:h-fit pt-4 relative' >
       <MainHero />
      </main>
      <section>
        <Cta/>
      </section>
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
