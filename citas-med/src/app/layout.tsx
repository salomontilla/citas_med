import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./ui/globals.css";

const geistSans = Inter({
  variable: "--font-inter-sans",
  subsets: ["latin"],
});


export const metadata: Metadata = {
  title: "CitasMed",
  description: "A modern health management system built with Next.js, TypeScrip and spring boot",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ES">
      <body
        className={`${geistSans.variable} antialiased`}
      >
        {children}
      </body>
    </html>
  );
}
