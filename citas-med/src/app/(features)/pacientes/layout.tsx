import Providers from "@/app/lib/providers";
import { Sidebar } from "../../ui/components/sidebar";
import SessionExpiredModal from "@/app/(auth)/token/sessionExpiredModal";


export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <>
      <section className="w-full flex min-h-full">
        <Sidebar />
        <main className="ml-[56px] px-3 flex-1 md:ml-0 md:p-10 bg-blue-50 ">
          <Providers>
            {children}
            <SessionExpiredModal />
          </Providers>
        </main>
      </section>
    </>
  )
}