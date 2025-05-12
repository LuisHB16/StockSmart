'use client';

import Image from 'next/image';
import { useState } from 'react';

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

interface ProductCardProps {
  product: Product;
}

export default function ProductCard({ product }: ProductCardProps) {
  const [showDetails, setShowDetails] = useState(false);
  
  // Función para convertir el BLOB a una URL de imagen
  const getImageUrl = (base64String: string) => {
    if (!base64String) return '/placeholder-product.jpg';
    try {
      return `data:image/jpeg;base64,${base64String}`;
    } catch (error) {
      console.error('Error al procesar la imagen:', error);
      return '/placeholder-product.jpg';
    }
  };

  return (
    <div className="bg-white rounded-lg shadow-md overflow-hidden hover:shadow-lg transition-shadow duration-300">
      <div className="relative h-48 bg-gray-100">
        {product.Imagen ? (
          <Image 
            src={getImageUrl(product.Imagen)}
            alt={product.Nombre}
            fill
            sizes="(max-width: 768px) 100vw, (max-width: 1200px) 50vw, 33vw"
            className="object-contain"
          />
        ) : (
          <div className="flex items-center justify-center h-full">
            <p className="text-gray-400">Sin imagen</p>
          </div>
        )}
      </div>
      
      <div className="p-4">
        <h3 className="text-lg font-semibold text-blue-800 mb-1 truncate">{product.Nombre}</h3>
        <p className="text-gray-600 text-sm mb-2">{product.Marca}</p>
        
        <div className="flex justify-between items-center mb-2">
          <span className="text-xl font-bold text-blue-700">${product.Precio}</span>
          <span className="text-sm text-gray-500">{product.Unidad}</span>
        </div>
        
        <div className="flex justify-between items-center text-sm">
          <span className={`${product.Existencia > 0 ? 'text-green-600' : 'text-red-600'}`}>
            {product.Existencia > 0 ? 'En existencia' : 'Agotado'}
          </span>
          <button 
            onClick={() => setShowDetails(!showDetails)}
            className="text-blue-600 hover:underline"
          >
            {showDetails ? 'Menos detalles' : 'Más detalles'}
          </button>
        </div>
        
        {showDetails && (
          <div className="mt-3 pt-3 border-t border-gray-200">
            <p className="text-sm text-gray-700">{product.Descripcion || 'Sin descripción disponible'}</p>
            <div className="mt-2 text-xs text-gray-500">
              <p>Código: {product.ClaveAlterna}</p>
              <p>Categoría: {product.Categoria}</p>
              <p>Existencia: {product.Existencia} {product.Unidad}</p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}