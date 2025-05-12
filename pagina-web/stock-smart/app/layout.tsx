import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import Image from "next/image";
import Link from "next/link";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "StockSmart - Tlapalería y Materiales",
  description: "Catálogo de productos de la Tlapalería StockSmart. Materiales para construcción y más.",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="es">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased bg-gray-50`}
      >
        {/* Header */}
        <header className="bg-blue-800 text-white shadow-md">
          <div className="container mx-auto py-4 px-6 flex justify-between items-center">
            <div className="flex items-center space-x-2">
              <Image
                src="/logoStockSmart.png"
                alt="StockSmart Logo"
                width={50}
                height={50}
                className="rounded-full bg-white p-1"
              />
              <div>
                <h1 className="text-2xl font-bold">StockSmart</h1>
                <p className="text-sm text-blue-200">Tlapalería y Materiales</p>
              </div>
            </div>
            <nav>
              <ul className="flex space-x-6">
                <li><Link href="/" className="hover:text-blue-200 transition-colors">Inicio</Link></li>
                <li><Link href="/buscar" className="hover:text-blue-200 transition-colors">Buscar</Link></li>
                <li><Link href="/contacto" className="hover:text-blue-200 transition-colors">Contacto</Link></li>
              </ul>
            </nav>
          </div>
        </header>
        
        {children}
        
        {/* Footer */}
        <footer className="bg-gray-800 text-white py-8 px-6">
          <div className="container mx-auto">
            <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
              <div>
                <h3 className="text-xl font-bold mb-4">StockSmart</h3>
                <p className="text-gray-300">Tu tienda de confianza para materiales de construcción y artículos para el hogar.</p>
              </div>
              <div>
                <h3 className="text-xl font-bold mb-4">Contacto</h3>
                <p className="text-gray-300">Dirección: Av. Principal #123</p>
                <p className="text-gray-300">Teléfono: (123) 456-7890</p>
                <p className="text-gray-300">Email: info@stocksmart.com</p>
              </div>
              <div>
                <h3 className="text-xl font-bold mb-4">Horario</h3>
                <p className="text-gray-300">Lunes a Viernes: 8:00 AM - 8:00 PM</p>
                <p className="text-gray-300">Sábados: 8:00 AM - 6:00 PM</p>
                <p className="text-gray-300">Domingos: 9:00 AM - 2:00 PM</p>
              </div>
            </div>
            <div className="border-t border-gray-700 mt-8 pt-6 text-center text-gray-400">
              <p>© {new Date().getFullYear()} StockSmart. Todos los derechos reservados.</p>
            </div>
          </div>
        </footer>
      </body>
    </html>
  );
}
