import { NextResponse } from 'next/server';
import mysql from 'mysql2/promise';

// Función para convertir BLOB a base64
function blobToBase64(blob: Buffer | null): string {
  if (!blob) return '';
  return blob.toString('base64');
}

export async function GET() {
  try {
    // Configuración de la conexión a la base de datos
    const connection = await mysql.createConnection({
      host: 'localhost',
      port: 3306,
      user: 'root',  // Ajusta según tu configuración
      password: '',  // Ajusta según tu configuración
      database: 'tlapaleria'
    });

    // Consulta para obtener todos los productos
    const [rows] = await connection.execute('SELECT * FROM producto');
    await connection.end();

    // Procesar los resultados para convertir las imágenes BLOB a base64
    const productos = Array.isArray(rows) ? rows.map(producto => {
      // Convertir la imagen BLOB a base64 si existe
      if ('Imagen' in producto && producto.Imagen) {
        return {
          ...producto,
          Imagen: blobToBase64(producto.Imagen)
        };
      }
      return producto;
    }) : [];

    return NextResponse.json(productos);
  } catch (error) {
    console.error('Error al obtener productos:', error);
    return NextResponse.json(
      { error: 'Error al obtener productos de la base de datos' },
      { status: 500 }
    );
  }
}