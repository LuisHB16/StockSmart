'use client';

import { useEffect, useState } from 'react';
import ProductCard from './ProductCard';
import LoadingSpinner from './LoadingSpinner';

interface Product {
  Codigo: number;
  ClaveAlterna: string;
  Nombre: string;
  Existencia: number;
  Marca: string;
  Precio: number;
  Descripcion: string;
  Granel: string;
  Unidad: string;
  Imagen: string;
  Categoria: string;
}

export default function ProductGrid() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await fetch('/api/productos');
        if (!response.ok) {
          throw new Error('Error al cargar los productos');
        }
        const data = await response.json();
        setProducts(data);
      } catch (err) {
        setError('No se pudieron cargar los productos. Por favor, intente más tarde.');
        console.error('Error fetching products:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  if (loading) return <LoadingSpinner />;
  
  if (error) {
    return (
      <div className="text-center p-8">
        <p className="text-red-500">{error}</p>
      </div>
    );
  }

  if (products.length === 0) {
    return (
      <div className="text-center p-8">
        <p>No hay productos disponibles en este momento.</p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      {products.map((product) => (
        <ProductCard key={product.Codigo} product={product} />
      ))}
    </div>
  );
}