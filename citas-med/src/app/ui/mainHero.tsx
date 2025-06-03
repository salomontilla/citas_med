import Register from '../ui/register';
import Image from 'next/image'
import Link from 'next/link';
import Button from '../ui/button';

export default function MainHero() {
    return (

        <div>

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
                        ¿Eres nuevo? Regístrate y reserva tus citas!
                    </h1>

                    <Register />

                </section>

                <section className='w-1/2 flex items-center text-center flex-col md:flex-row h-full'>

                    <div className="flex flex-col py-10 w-screen z-10  items-center text-white">
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
                        <Link href="/login" passHref>
                            <Button >Iniciar Sesion</Button>
                        </Link>


                    </div>

                </section>
            </div>
        </div>
    )
}