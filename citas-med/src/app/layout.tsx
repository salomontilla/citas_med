import type { Metadata } from "next";
import { Raleway } from "next/font/google";
import "./styles/globals.css";
import Navbar from "./ui/components/navbar";
import Footer from "./ui/components/footer";

const raleway = Raleway({
  subsets: ["latin"],
});


export const metadata: Metadata = {
  title: "CitasMed",
  description: "A modern health management system built with Next.js, TypeScript and spring boot",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ES" className="light scroll-smooth">
      
      
      <body
        className={`${raleway.className} antialiased`}
      >
        
        
          {children}
        
      </body>
      <footer>
        <Footer />
      </footer>
    </html>
  );
}
