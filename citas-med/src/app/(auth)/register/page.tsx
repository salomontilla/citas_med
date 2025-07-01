import Footer from "../../ui/components/footer";
import Navbar from "../../ui/components/navbar";
import RegisterForm from "../../ui/components/registerForm";
import Image from "next/image";

export default function RegisterPage() {
  return (
    <div className="min-h-screen flex flex-col relative">
     
      <header className="absolute w-full z-50">
        <Navbar />
      </header>

      
      <div className="grid grid-cols-1 md:grid-cols-5 h-full bg-[#03045e]">
        
        
        <section className="md:col-span-3 flex justify-center items-center px-4">
          <div className="bg-blue-50 text-gray-800 w-full max-w-2xl p-10 rounded-2xl shadow-xl my-24 md:mt-20">
            <h2 className="text-blue-600 text-lg font-semibold">¡Únete gratis!</h2>
            <h1 className="text-4xl md:text-5xl font-bold mb-2">Crea una nueva cuenta</h1>
            <p className="text-gray-600 mb-4">
              ¿Ya tienes una cuenta?{" "}
              <a href="/login" className="underline text-blue-500 hover:text-blue-700 transition-colors ">
                Inicia sesión
              </a>
            </p>

            <RegisterForm />
          </div>
        </section>

        
        <section className="hidden md:flex col-span-2 relative h-full">
          
          <Image
            priority
            src="/banner_register2.webp"
            alt="Banner de registro"
            width={1335}
            height={2000}
            className="object-cover w-full h-full"
          />

          <div className="absolute top-0 left-0 w-full h-full  bg-blue-900/70 z-10" />

          <div className="absolute inset-0 z-20 flex flex-col items-center justify-center text-white text-center px-6">
            <h1 className="text-3xl md:text-4xl font-bold mb-3">Bienvenido a CistasMed</h1>
            <p className="text-lg max-w-md">Agenda y gestiona tus citas médicas de forma rápida y sencilla.</p>
          </div>
        </section>
      </div>
    </div>
  );
}
