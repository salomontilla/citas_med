import {NavbarComponent} from './ui/navbarComponent';
import Login from './ui/Login';
export default function Home() {
  return (
    <div>

      <header className=''>
        <NavbarComponent />
      </header>
      <main>
        <Login />
      </main>
    </div>
  );
}
