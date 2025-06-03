export default function Register() {
    return (
        
            <div className="z-10 md:w-[70%] p-8 bg-white rounded-2xl shadow-md relative">
                <h2 className="mb-6 text-2xl font-bold text-center text-gray-900">Registrarse</h2>
                <form >
                    <div className="mb-4">
                        <label className="block mb-2 text-sm font-medium text-gray-700" htmlFor="name">Nombre Completo</label>
                        <input
                            type="text"
                            id="name"
                            className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 text-black"
                            placeholder="Ingrese su nombre completo"
                            required
                        />
                    </div>
                    <div className="mb-4">
                        <label className="block mb-2 text-sm font-medium text-gray-700" htmlFor="email">Correo Electrónico</label>
                        <input
                            type="email"
                            id="email"
                            className="w-full px-3 py-2 border text-black border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="Ingrese su correo electrónico"
                            required
                        />
                    </div>
                    <div className="mb-6">
                        <label className="block mb-2 text-sm font-medium text-gray-700" htmlFor="password">Contraseña</label>
                        <input
                            type="password"
                            id="password"
                            className="w-full px-3 py-2 border text-black border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="Ingrese su contraseña"
                            required
                        />
                    </div>
                    <div className="flex w-full justify-center">
                        <button
                            type="submit"
                            className="mt-6 w-80 px-6 py-3 rounded-2xl bg-blue-600 hover:bg-blue-700 text-white transition-colors duration-300 text-center cursor-pointer"
                        >
                            Registrarse
                        </button>
                    </div>
                </form>
            </div>
        
    );
}