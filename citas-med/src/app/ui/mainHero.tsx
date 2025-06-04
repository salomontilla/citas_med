import Register from '../ui/register';
import Image from 'next/image'
import Link from 'next/link';
import Button from '../ui/button';
import HeroCard from './heroCard';

export default function MainHero() {
    return (

        <div className='flex flex-col items-center justify-evenly gap-8 h-full md:h-screen'>
            <div className='grid grid-cols-1 xl:grid-cols-2'>
                <section>

                </section>
                <section className='flex flex-col items-start justify-center gap-4 w-fit text-gray-50'>
                    <div className='shadow-2xs flex gap-2 bg-white rounded-2xl px-4 py-1 w-fit text-blue-800'>
                        <img src="/cta/logo_services.svg" alt="servicios" className='aspect-square object-contain' />
                        <p>Health Care</p>
                    </div>
                    <div className='flex flex-col gap-4'>
                        <h1 className='text-5xl font-bold'>La mejor atención, los mejores doctores</h1>
                        <p className='text-lg'>Lorem, ipsum dolor sit amet consectetur adipisicing elit. Eveniet incidunt minima harum, sed natus saepe accusamus labore iste obcaecati fugiat rem. A, ratione accusantium voluptatibus in eligendi dolores porro repellat!</p>
                        <div className='flex gap-4'>
                            <Link href={'/login'}>
                                <Button>Iniciar sesión</Button>
                            </Link>

                            <Link href={'/register'}>
                                <Button>Soy nuevo</Button>
                            </Link>
                        </div>
                    </div>
                </section>
            </div>

            <div className='relative flex flex-col px-4 sm:px-8 md:flex-row gap-8 items-center justify-end w-[60%] h-fit bg-blue-600 rounded-2xl py-8 '>
                <div>
                    <Image src="/doctor2.png" alt="Hero Image" width={300} height={500} className="left-0 bottom-0 absolute object-cover h-[700px] hidden xl:flex" />
                </div>
                <HeroCard img="card_1.svg" title="Servicio de emergencias"> Contamos con un equipo médico listo para actuar en cualquier momento.

                </HeroCard>
                <HeroCard img="card_2.svg" title="Diagnóstico Avanzado">Equipos de última generación para brindar diagnósticos precisos y confiables.

                </HeroCard>

            </div>
        </div>
    )
}