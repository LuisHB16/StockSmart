'use client';

import { useState } from 'react';
import { GoogleMap, LoadScript, Marker } from '@react-google-maps/api';


interface Store {
  id: number;
  name: string;
  address: string;
  phone: string;
  email?: string; // Email ahora es opcional
  hours: {
    weekdays: string;
    saturday: string;
    sunday: string;
  };
  coordinates: {
    lat: number;
    lng: number;
  };
  services: string[]; // Lista de servicios disponibles en cada tienda
}

export default function ContactoPage() {
  // Datos de ejemplo de las tiendas
  const stores: Store[] = [
    {
      id: 1,
      name: 'StockSmart Principal',
      address: 'Av. Principal #123, Col. Centro',
      phone: '(123) 456-7890',
      email: 'principal@stocksmart.com',
      hours: {
        weekdays: '8:00 AM - 8:00 PM',
        saturday: '8:00 AM - 6:00 PM',
        sunday: '9:00 AM - 2:00 PM',
      },
      coordinates: {
        lat: 19.432608,
        lng: -99.133209,
      },
      services: ['Estacionamiento', 'Corte de materiales', 'Asesoría técnica', 'Entrega a domicilio', 'Instalación profesional'],
    },
    {
      id: 2,
      name: 'StockSmart Norte',
      address: 'Blvd. Norte #456, Col. Industrial',
      phone: '(123) 456-7891',
      // Email opcional, esta tienda no tiene
      hours: {
        weekdays: '8:00 AM - 8:00 PM',
        saturday: '8:00 AM - 6:00 PM',
        sunday: '9:00 AM - 2:00 PM',
      },
      coordinates: {
        lat: 19.442608,
        lng: -99.143209,
      },
      services: ['Estacionamiento', 'Corte de materiales', 'Renta de herramientas', 'Capacitación'],
    },
    {
      id: 3,
      name: 'StockSmart Sur',
      address: 'Av. Sur #789, Col. Jardines',
      phone: '(123) 456-7892',
      email: 'sur@stocksmart.com',
      hours: {
        weekdays: '8:00 AM - 8:00 PM',
        saturday: '8:00 AM - 6:00 PM',
        sunday: '9:00 AM - 2:00 PM',
      },
      coordinates: {
        lat: 19.422608,
        lng: -99.123209,
      },
      services: ['Estacionamiento', 'Asesoría técnica', 'Entrega a domicilio', 'Diseño personalizado'],
    },
  ];

  const [selectedStore, setSelectedStore] = useState<Store>(stores[0]);

  return (
    <main className="container mx-auto py-8 px-6">
      <h1 className="text-3xl font-bold text-gray-800 mb-6">Nuestras Tiendas</h1>
      
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Lista de tiendas */}
        <div className="lg:col-span-1">
          <div className="bg-white rounded-lg shadow-md overflow-hidden">
            <div className="p-4 bg-blue-700 text-white">
              <h2 className="text-xl font-bold">Selecciona una tienda</h2>
            </div>
            <div className="divide-y divide-gray-200">
              {stores.map((store) => (
                <div 
                  key={store.id} 
                  className={`p-4 cursor-pointer hover:bg-gray-50 transition-colors ${selectedStore.id === store.id ? 'bg-blue-50' : ''}`}
                  onClick={() => setSelectedStore(store)}
                >
                  <h3 className="font-semibold text-lg text-gray-800">{store.name}</h3>
                  <p className="text-gray-600 text-sm">{store.address}</p>
                </div>
              ))}
            </div>
          </div>

          <div className="mt-6 bg-white rounded-lg shadow-md p-6">
            <h2 className="text-xl font-bold text-gray-800 mb-4">Contáctanos</h2>
            <form className="space-y-4" onSubmit={(e) => {
              e.preventDefault();
              alert('Mensaje enviado correctamente. Nos pondremos en contacto contigo pronto.');
            }}>
              <div>
                <label htmlFor="name" className="block text-sm font-medium text-gray-700 mb-1">Nombre</label>
                <input 
                  type="text" 
                  id="name" 
                  className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="Tu nombre"
                  required
                />
              </div>
              <div>
                <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">Email (opcional)</label>
                <input 
                  type="email" 
                  id="email" 
                  className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="tu@email.com"
                />
              </div>
              <div>
                <label htmlFor="message" className="block text-sm font-medium text-gray-700 mb-1">Mensaje</label>
                <textarea 
                  id="message" 
                  rows={4} 
                  className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="¿En qué podemos ayudarte?"
                  required
                ></textarea>
              </div>
              <button 
                type="submit" 
                className="w-full bg-blue-700 text-white py-2 px-4 rounded-md hover:bg-blue-800 transition-colors"
              >
                Enviar mensaje
              </button>
            </form>
          </div>
        </div>

        {/* Detalles de la tienda seleccionada */}
        <div className="lg:col-span-2">
          <div className="bg-white rounded-lg shadow-md overflow-hidden">
            {/* Mapa de Google Maps */}
            <div className="h-64 relative">
              <LoadScript googleMapsApiKey="AIzaSyBDaeWicvigtP9xPv919E-RNoxfvC-Hqik">
                <GoogleMap
                  mapContainerStyle={{
                    width: '100%',
                    height: '100%'
                  }}
                  center={{
                    lat: selectedStore.coordinates.lat,
                    lng: selectedStore.coordinates.lng
                  }}
                  zoom={15}
                >
                  <Marker
                    position={{
                      lat: selectedStore.coordinates.lat,
                      lng: selectedStore.coordinates.lng
                    }}
                    title={selectedStore.name}
                  />
                </GoogleMap>
              </LoadScript>
            </div>
            
            <div className="p-6">
              <h2 className="text-2xl font-bold text-gray-800 mb-4">{selectedStore.name}</h2>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <h3 className="text-lg font-semibold text-gray-800 mb-2">Información de contacto</h3>
                  <ul className="space-y-2">
                    <li className="flex items-start">
                      <svg className="h-5 w-5 text-blue-700 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                      </svg>
                      <span>{selectedStore.address}</span>
                    </li>
                    <li className="flex items-start">
                      <svg className="h-5 w-5 text-blue-700 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                      </svg>
                      <span>{selectedStore.phone}</span>
                    </li>
                    {selectedStore.email && (
                      <li className="flex items-start">
                        <svg className="h-5 w-5 text-blue-700 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                        </svg>
                        <span>{selectedStore.email}</span>
                      </li>
                    )}
                  </ul>
                </div>
                
                <div>
                  <h3 className="text-lg font-semibold text-gray-800 mb-2">Horario de atención</h3>
                  <ul className="space-y-2">
                    <li className="flex items-start">
                      <svg className="h-5 w-5 text-blue-700 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                      <div>
                        <span className="font-medium">Lunes a Viernes:</span>
                        <span className="block text-gray-600">{selectedStore.hours.weekdays}</span>
                      </div>
                    </li>
                    <li className="flex items-start">
                      <svg className="h-5 w-5 text-blue-700 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                      <div>
                        <span className="font-medium">Sábados:</span>
                        <span className="block text-gray-600">{selectedStore.hours.saturday}</span>
                      </div>
                    </li>
                    <li className="flex items-start">
                      <svg className="h-5 w-5 text-blue-700 mr-2 mt-0.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                      </svg>
                      <div>
                        <span className="font-medium">Domingos:</span>
                        <span className="block text-gray-600">{selectedStore.hours.sunday}</span>
                      </div>
                    </li>
                  </ul>
                </div>
              </div>
              
              <div className="mt-6">
                <h3 className="text-lg font-semibold text-gray-800 mb-2">Servicios disponibles</h3>
                <div className="grid grid-cols-2 gap-2">
                  {selectedStore.services.map((service, index) => (
                    <div key={index} className="flex items-center">
                      <svg className="h-5 w-5 text-green-500 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                      </svg>
                      <span>{service}</span>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}