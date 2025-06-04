

export function NavbarComponent() {
  return (
    <nav className="bg-[#03045e] text-gray-100">
      <div className="w-full mx-auto px-4 sm:px-40 shadow">
        <div className="flex items-center justify-center h-16">
          
            <h1 className="text-3xl font-bold text-gray-100">CitasMed</h1>
          
          <div className="ml-auto">
            <ul className="flex space-x-4">
              <li>
                <a href="/" className="hover:text-blue-300">Inicio</a>
              </li>
              <li>
                <a href="/about" className="hover:text-blue-300">Acerca de</a>
              </li>
              <li>
                <a href="/contact" className="hover:text-blue-300">Contacto</a>
              </li>
            </ul> 
          </div>
        </div>
      </div>
      <style>
        {
          `
          a {
            color: #1d4ed;
          }
          a:hover {
            color: #2563eb;
          }`
        }
      </style>
    </nav>
  );
}
