import Navbar from "../ui/navbar";
import RegisterForm from "../ui/registerForm";

export default function RegisterPage() {
    return (
        <>
            
            <Navbar/>
            
            <div className=" grid grid-cols-5 h-full relative">
            <section className="col-span-3">      
                  
                <div className="text-gray-800 flex flex-col justify-center items-start h-full gap-2 px-8">
                    <h2 className="text-blue-600 text-lg font-semibold">¡Únete gratis!</h2>
                    <h1 className="text-5xl font-bold">Crea una nueva cuenta</h1>
                    <p className="text-lg text-gray-600">
                        ¿Ya tienes una cuenta?{" "}
                        <a href="/login" className="underline text-blue-500 hover:text-blue-700 transition-colors">
                            Inicia sesión
                        </a>
                    </p>

                    <RegisterForm />
                </div>
            </section>
            <section className="col-span-2 bg-blue-300 relative overflow-hidden">
                
                    <img className="object-cover h-screen absolute" src="/banner_register.webp" alt="Med banner" />
                
                
                <div>
                    <h1>Lorem ipsum dolor, sit amet consectetur adipisicing elit</h1>
                </div>
            </section>
        </div>
        </>
        
    )
}