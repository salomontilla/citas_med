import { Sidebar } from "../../ui/components/sidebar";

 
export default function Layout({ children }: { children: React.ReactNode }) {
  return (
      <section className="w-full flex min-h-screen">
      <Sidebar />
      <main className="ml-[56px] px-3 flex-1 md:ml-0 md:p-10 overflow-y-auto bg-blue-50 ">{children}</main>
    </section>
  )
}