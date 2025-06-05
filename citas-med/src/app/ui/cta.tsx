import Link from 'next/link';
import CtaCard from './ctaCard';
import Button from './button';
export default function Cta() {
    return (
        <div>
            <section className='bg-blue-50 flex flex-col items-start justify-center gap-8 p-8 md:p-16 '>
                <div className='shadow-2xs flex gap-2 bg-white rounded-2xl px-4 py-1 w-fit text-blue-800'>
                    <img src="/cta/logo_services.svg" alt="servicios" className='aspect-square object-contain' />
                    <p>Nuestros servicios</p>
                </div>
                <div className='flex flex-col lg:flex-row  text-blue-800 gap-3'>
                    <h1 className="text-4xl font-bold">Soluciones únicas e <span className="text-blue-500">increíbles</span></h1>
                    <p className="text-lg w-fit max-w-xl">
                        Te ofrecemos una plataforma intuitiva, rápida y segura para gestionar tus citas médicas en un solo lugar. Desde la reserva hasta el historial clínico, todo al alcance de un clic.
                    </p>
                    <Link href={'#'}>
                        <Button>Contacto</Button>
                    </Link>
                </div>
                <div className='grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4  gap-6 w-full'>
                    <CtaCard
                        number="1"
                        title="Atención Personalizada"
                        icon="/cta_card_1.svg"
                    >
                        Nuestros profesionales están comprometidos con tu bienestar, brindándote una atención humana y adaptada a tus necesidades.
                    </CtaCard>

                    <CtaCard
                        number="2"
                        title="Citas Rápidas"
                        icon="/cta_card_2.svg"
                    >
                        Reserva tu cita en segundos desde cualquier dispositivo. Sin filas, sin llamadas, sin complicaciones.
                    </CtaCard>


                    <CtaCard
                        number="3"
                        title="Profesionalismo"
                        icon="/cta_card_3.svg"
                    >
                        Contamos con un equipo médico altamente calificado y con experiencia en diversas especialidades, listo para atenderte.
                    </CtaCard>


                    <CtaCard
                        number="4"
                        title="Seguridad y Privacidad"
                        icon="/cta_card_4.svg"
                    >
                        Tus datos médicos están protegidos con los más altos estándares de seguridad. Tu información, siempre en buenas manos.
                    </CtaCard>

                </div>
                <div>
                    <p></p>
                </div>
            </section>
        </div>
    )
}