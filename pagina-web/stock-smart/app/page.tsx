import Image from "next/image";
import ProductGrid from "../components/ProductGrid";

export default function Home() {
  return (
    <main className="container mx-auto py-8 px-6">
      <section className="mb-10">
        <div className="bg-blue-700 text-white rounded-lg p-6 mb-8 shadow-lg">
          <h2 className="text-3xl font-bold mb-2">Bienvenido a StockSmart</h2>
          <p className="text-blue-100 mb-4">Encuentra todo lo que necesitas para tus proyectos de construcción y hogar.</p>
          <div className="flex space-x-4">
            <button className="bg-white text-blue-700 px-4 py-2 rounded-md font-medium hover:bg-blue-50 transition-colors">
              Ver ofertas
            </button>
            <button className="border border-white text-white px-4 py-2 rounded-md font-medium hover:bg-blue-600 transition-colors">
              Categorías populares
            </button>
          </div>
        </div>

        <h2 className="text-2xl font-bold text-gray-800 mb-6">Nuestros Productos</h2>
        <ProductGrid />
      </section>
    </main>
  );
}
