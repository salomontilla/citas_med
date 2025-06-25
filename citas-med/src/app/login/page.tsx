'use client'
import Navbar from '../ui/components/navbar';
export default function LoginPage() {
    return (
        <div className="relative w-full h-screen flex flex-col items-center justify-center">
            <header className="bg-blue-50 absolute top-0 left-0 w-full z-20">
                <Navbar />

            </header>

  
            <div className="fondo-lineas">
                {[...Array(10)].map((_, i) => (
                <div
                    key={i}
                    className="linea"
                    style={{
                    left: `${i * 10}%`,
                    animationDelay: `${i * 0.4}s`,
                    }}
                />
                ))}
            </div>
            <div className="relative z-10 flex items-center justify-center min-h-fit">
                <div className="w-[80%] md:w-full max-w-md p-8 bg-white rounded-2xl shadow-md">
                    <h2 className="mb-6 text-2xl font-bold text-center">Iniciar Sesión</h2>
                    <form>
                        <div className="mb-4">
                            <label className="block mb-2 text-sm font-medium text-gray-700" htmlFor="email">Correo Electrónico</label>
                            <input
                                type="email"
                                id="email"
                                className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                placeholder="Ingrese su correo electrónico"
                                required
                            />
                        </div>
                        <div className="mb-6">
                            <label className="block mb-2 text-sm font-medium text-gray-700" htmlFor="password">Contraseña</label>
                            <input
                                type="password"
                                id="password"
                                className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                                placeholder="Ingrese su contraseña"
                                required
                            />
                            <a href="#" className="text-sm text-blue-600 hover:underline">¿Olvidaste tu contraseña?</a>
                        </div>
                        <button
                            onSubmit={() => console.log("Iniciar sesión")}
                            type="submit"
                            className="w-full px-4 py-2 rounded-lg text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 transition-colors duration-300"
                        >
                            Iniciar Sesión
                        </button>
                    </form>
                </div>
            </div>
            <style>
                {`
                    .fondo-lineas {
                        position: absolute;
                        inset: 0;
                        background-color: #03045e; 
                        overflow: hidden;
                        z-index: 0;
                    }   

                    .fondo-lineas .linea {
                        position: absolute;
                        top: 0;
                        width: 1px;
                        height: 100%;
                        background-color: rgba(255, 255, 255, 0.1);
                        animation: mover-linea 6s linear infinite;
                    }

                    @keyframes mover-linea {
                    0% {
                        transform: translateY(100%);
                    }
                    100% {
                        transform: translateY(-100%);
                    }
                    }
                `}
            </style>
        </div>
        
    );
}
