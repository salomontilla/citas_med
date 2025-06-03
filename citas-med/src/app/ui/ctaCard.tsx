export default function CtaCard() {
    return(
        <div>
            <div className="bg-[#03045e] text-gray-100 p-6 rounded-lg shadow-lg">
                <h2 className="text-2xl font-bold mb-4">¡Agenda tu cita médica hoy!</h2>
                <p className="mb-4">Con CitasMed, reservar tu cita nunca ha sido tan fácil. Conéctate con profesionales de la salud y cuida de tu bienestar.</p>
                <button className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors duration-300">
                    Reservar Cita
                </button>
            </div>
        </div>
    )
}