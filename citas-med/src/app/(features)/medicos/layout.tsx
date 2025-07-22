import Providers from "@/app/lib/providers";
import { Sidebar } from "../../ui/components/sidebarMedicos";
import SessionExpiredModal from "@/app/(auth)/token/sessionExpiredModal";


export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <>
      <section className="w-full flex min-h-full">
        <Sidebar />
        <main className="ml-[56px] overflow-hidden px-3 min-h-screen flex-1 md:ml-0 md:p-10 bg-blue-50 ">
          <Providers>
            {children}
            <SessionExpiredModal />
          </Providers>
        </main>
      </section>
    </>
  )
}