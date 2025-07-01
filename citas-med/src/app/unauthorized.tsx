export default function UnauthorizedPage() {
    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100">
        <h1 className="text-2xl font-bold mb-4">Acceso no autorizado</h1>
        <p className="text-gray-600">Lo sentimos, no tienes permiso para acceder a esta página.</p>
        <p className="text-gray-600">Si crees que esto es un error, por favor contacta al administrador.</p>
        </div>
    );
    }