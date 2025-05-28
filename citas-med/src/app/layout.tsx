import type { Metadata } from "next";
import { Raleway } from "next/font/google";
import "./ui/globals.css";

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
    <html lang="">
      <body
        className={`${raleway.className} antialiased`}
      >
        {children}
      </body>
    </html>
  );
}
