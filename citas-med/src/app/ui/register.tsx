export default function Register() {
    return (
        
            <div className="min-w-full max-w-md p-8 bg-white rounded-2xl shadow-md">
                <h2 className="mb-6 text-2xl font-bold text-center text-gray-900">Registrarse</h2>
                <form>
                    <div className="mb-4">
                        <label className="block mb-2 text-sm font-medium text-gray-700" htmlFor="name">Nombre Completo</label>
                        <input
                            type="text"
                            id="name"
                            className="w-full px-3 py-2 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
                            placeholder="Ingrese su nombre completo"
                            required
                        />
                    </div>
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
                    </div>
                    <button
                        type="submit"
                        className="w-full px-4 py-2 rounded-2xl text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                        Registrarse
                    </button>
                </form>
            </div>
        
    );
}