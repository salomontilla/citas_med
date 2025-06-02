

export function NavbarComponent() {
  return (
    <nav>
      <div className="w-screen mx-auto px-4 sm:px-40 shadow ">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <h1 className="text-xl font-bold">CitasMed</h1>
          </div>
          <div className="flex space-x-4">
            <a href="/">Home</a>
            <a href="/about">About</a>
            <a href="/contact">Contact</a>
          </div>
        </div>
      </div>
      <style>
        {
          `nav {
            background-color: #03045e;
          }
          a {
            color: #f3f4f6;
          }
          a:hover {
            color: #90e0ef;
          }`
        }
      </style>
    </nav>
  );
}
