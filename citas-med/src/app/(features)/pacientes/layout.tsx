import { Sidebar } from "../../ui/components/sidebar";

 
export default function Layout({ children }: { children: React.ReactNode }) {
  return (
      <section className="w-full flex min-h-screen">
      <Sidebar />
      <main className="ml-[60px] flex-1 md:ml-0 overflow-y-auto pl-10">{children}</main>
    </section>
  )
}