import Navbar from './ui/components/navbar';
import MainHero from './ui/sections/mainHero';
import Cta from './ui/sections/cta';

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
      
    </div>
  );
}
