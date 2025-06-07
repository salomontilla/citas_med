import Navbar from './ui/components/navbar';
import MainHero from './ui/mainHero';
import Cta from './ui/cta';

export default function Home() {
  return (
    <div id='inicio'>
      
      <Navbar />
      <main className='bg-[#03045e] md:h-full lg:h-fit p-8'>
       <MainHero />
      </main>
      <section id='servicios'>
        <Cta/>
      </section>
      <footer>
        
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
