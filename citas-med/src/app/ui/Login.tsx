export default function Login() {
    return (
        
        <div className="min-w-fit max-w-md p-8 bg-white rounded-2xl shadow-md">
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
                className=" mb-1 w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="Ingrese su contraseña"
                required
                />
            <a href="#" className="text-sm text-blue-600 hover:underline ">¿Olvidaste tu contraseña?</a>
            </div>
            
            <button
                type="submit"
                className="w-full px-4 py-2 rounded-2xl text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
                Iniciar Sesión
            </button>
            </form>
        </div>
        
    );
    }