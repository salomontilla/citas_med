

export function NavbarComponent() {
  return (
    <nav className="bg-white ">
      <div className="w-screen mx-auto px-4 sm:px-8 shadow ">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <a href="/" className="text-xl font-bold text-gray-900">CitasMed</a>
          </div>
          <div className="flex space-x-4">
            <a href="/" className="text-gray-700 hover:text-blue-600">Home</a>
            <a href="/about" className="text-gray-700 hover:text-blue-600">About</a>
            <a href="/contact" className="text-gray-700 hover:text-blue-600">Contact</a>
          </div>
        </div>
      </div>
    </nav>
  );
}
