import {NavbarComponent} from './ui/navbarComponent';
import Login from './ui/Login';
export default function Home() {
  return (
    <div>

      <header className=''>
        <NavbarComponent />
      </header>
      <main>
        <div>
          <h1 className="text-4xl font-bold text-center my-4">Bienvenido a CitasMed</h1>
          <p className="text-center text-gray-600 mb-8">Un sistema moderno de gestión de salud construido con Next.js, TypeScript y Spring Boot.</p>
        </div>
        <div className='flex '>
          <section>

          </section>
          <section className='w-1/2 flex flex-col items-center justify-center min-h-screen'>

            <Login />
          </section>
        </div>
      </main>
    </div>
  );
}
