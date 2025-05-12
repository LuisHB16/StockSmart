'use client';

import { useState, useEffect } from 'react';


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

export default function BuscarPage() {
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCategory, setSelectedCategory] = useState('');
  const [selectedBrand, setSelectedBrand] = useState('');
  const [categories, setCategories] = useState<string[]>([]);
  const [brands, setBrands] = useState<string[]>([]);
  const [products, setProducts] = useState<Product[]>([]);
  const [filteredProducts, setFilteredProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const response = await fetch('/api/productos');
        if (!response.ok) {
          throw new Error('Error al cargar los productos');
        }
        const data = await response.json();
        setProducts(data);
        setFilteredProducts(data);
        
        // Extraer categorías únicas
        const uniqueCategories = [...new Set(data.map((product: Product) => product.Categoria))];
        setCategories(uniqueCategories);
        
        // Extraer marcas únicas
        const uniqueBrands = [...new Set(data.map((product: Product) => product.Marca))];
        setBrands(uniqueBrands);
      } catch (err) {
        console.error('Error fetching products:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  useEffect(() => {
    // Filtrar productos basados en búsqueda, categoría y marca
    let results = products;
    
    if (searchTerm) {
      results = results.filter(product => 
        product.Nombre.toLowerCase().includes(searchTerm.toLowerCase()) ||
        product.Descripcion.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }
    
    if (selectedCategory) {
      results = results.filter(product => product.Categoria === selectedCategory);
    }
    
    if (selectedBrand) {
      results = results.filter(product => product.Marca === selectedBrand);
    }
    
    setFilteredProducts(results);
  }, [searchTerm, selectedCategory, selectedBrand, products]);

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };

  const handleCategoryChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedCategory(e.target.value);
  };

  const handleBrandChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedBrand(e.target.value);
  };

  const resetFilters = () => {
    setSearchTerm('');
    setSelectedCategory('');
    setSelectedBrand('');
  };

  return (
    <main className="container mx-auto py-8 px-6">
      <h1 className="text-3xl font-bold text-gray-800 mb-6">Buscar Productos</h1>
      
      {/* Advertencia */}
      <div className="bg-yellow-100 border-l-4 border-yellow-500 text-yellow-700 p-4 mb-6" role="alert">
        <div className="flex items-center">
          <div className="py-1">
            <svg className="fill-current h-6 w-6 text-yellow-500 mr-4" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20">
              <path d="M2.93 17.07A10 10 0 1 1 17.07 2.93 10 10 0 0 1 2.93 17.07zm12.73-1.41A8 8 0 1 0 4.34 4.34a8 8 0 0 0 11.32 11.32zM9 11V9h2v6H9v-4zm0-6h2v2H9V5z"/>
            </svg>
          </div>
          <div>
            <p className="font-bold">Importante</p>
            <p className="text-sm">Esta búsqueda muestra disponibilidad solo para la tienda principal. Por favor llame para verificar disponibilidad en otras sucursales.</p>
          </div>
        </div>
      </div>
      
      {/* Filtros */}
      <div className="bg-white p-6 rounded-lg shadow-md mb-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
          <div className="col-span-1 md:col-span-2">
            <label htmlFor="search" className="block text-sm font-medium text-gray-700 mb-1">Buscar</label>
            <input
              type="text"
              id="search"
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="Nombre o descripción del producto"
              value={searchTerm}
              onChange={handleSearch}
            />
          </div>
          
          <div>
            <label htmlFor="category" className="block text-sm font-medium text-gray-700 mb-1">Categoría</label>
            <select
              id="category"
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={selectedCategory}
              onChange={handleCategoryChange}
            >
              <option value="">Todas las categorías</option>
              {categories.map((category, index) => (
                <option key={index} value={category}>{category}</option>
              ))}
            </select>
          </div>
          
          <div>
            <label htmlFor="brand" className="block text-sm font-medium text-gray-700 mb-1">Marca</label>
            <select
              id="brand"
              className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              value={selectedBrand}
              onChange={handleBrandChange}
            >
              <option value="">Todas las marcas</option>
              {brands.map((brand, index) => (
                <option key={index} value={brand}>{brand}</option>
              ))}
            </select>
          </div>
        </div>
        
        <div className="mt-4 flex justify-end">
          <button
            onClick={resetFilters}
            className="px-4 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300 transition-colors"
          >
            Limpiar filtros
          </button>
        </div>
      </div>
      
      {/* Resultados */}
      <div>
        <h2 className="text-2xl font-bold text-gray-800 mb-6">Resultados ({filteredProducts.length})</h2>
        {loading ? (
          <div className="text-center py-8">
            <p>Cargando productos...</p>
          </div>
        ) : filteredProducts.length > 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            {filteredProducts.map((product) => (
              <div key={product.Codigo} className="bg-white rounded-lg shadow-md overflow-hidden">
                <div className="p-4">
                  <h3 className="text-lg font-semibold text-gray-800 mb-2">{product.Nombre}</h3>
                  <p className="text-sm text-gray-600 mb-2">{product.Descripcion}</p>
                  <div className="flex justify-between items-center">
                    <span className="text-blue-700 font-bold">${product.Precio}</span>
                    <span className="text-sm text-gray-500">{product.Marca}</span>
                  </div>
                  <div className="mt-2">
                    <span className="inline-block bg-blue-100 text-blue-800 text-xs px-2 py-1 rounded">
                      {product.Categoria}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="text-center py-8 bg-gray-100 rounded-lg">
            <p className="text-gray-600">No se encontraron productos que coincidan con tu búsqueda.</p>
            <button
              onClick={resetFilters}
              className="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition-colors"
            >
              Ver todos los productos
            </button>
          </div>
        )}
      </div>
    </main>
  );
}