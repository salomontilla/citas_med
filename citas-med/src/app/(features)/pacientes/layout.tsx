import { Sidebar } from "../../ui/components/sidebar";
import Link from "next/link";

 
export default function Layout({ children }: { children: React.ReactNode }) {
  return (
      <section className="w-full flex h-screen">
        <Sidebar/>
        {children}
      </section>
  )
}