package codigo;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.filechooser.FileSystemView;

public class PDF {

    Connection con;
    conexion cn = new conexion();
    PreparedStatement ps;
    ResultSet rs;

    public void pdfV(int id_ventas, int cliente, double total, String usuario) {
        try {
            Date date = new Date();
            FileOutputStream archivo;
            String url = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
            File salida = new File(url + "venta.pdf");
            archivo = new FileOutputStream(salida);

            // Define el tamaño personalizado para el documento (ticket)
            // Define el tamaño personalizado para el documento (ticket)
Document doc = new Document(new Rectangle(150f, 1000f));  // Ancho de 170 mm
  

            // Elimina los márgenes del documento
            doc.setMargins(2, 2, 2,2);  // Puedes ajustar los valores según sea necesario


            PdfWriter.getInstance(doc, archivo);
            doc.open();

            Font fuenteEncabezado = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);
            Font fuenteNormal = new Font(Font.FontFamily.TIMES_ROMAN, 9
                    , Font.NORMAL, BaseColor.BLACK);
            Font fuenteEmpresa = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL, BaseColor.BLACK);

            // Encabezado
            Image img = Image.getInstance(getClass().getResource("/assests/images/ya.png"));
            img.scaleAbsolute(100, 100); // Ajusta el tamaño de la imagen

            Paragraph fecha = new Paragraph();
            fecha.setFont(fuenteEncabezado);
            fecha.add(Chunk.NEWLINE);
            fecha.add("Vendedor: " + usuario + "\nFolio: " + id_ventas + "\nFecha: " + new SimpleDateFormat("dd/MM/yyyy").format(date));

            PdfPTable Encabezado = new PdfPTable(4);
            Encabezado.setWidthPercentage(100);
            Encabezado.getDefaultCell().setBorder(0);
            float[] columnWidthsEncabezado = new float[]{25f, 25f, 25f, 25f};
            Encabezado.setWidths(columnWidthsEncabezado);
            Encabezado.setHorizontalAlignment(Element.ALIGN_LEFT);

// Info de la empresa
            String config = "SELECT * FROM empresa";
            try {
                con = cn.getConnection();
                ps = con.prepareStatement(config);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String empresaInfo = "RFC: " + rs.getString("RFC") + "\nEmpresa: " + rs.getString("Nombre") + "\nTeléfono: " + rs.getString("Telefono") + "\nDirección: " + rs.getString("Direccion");
                    PdfPCell empresaCell = new PdfPCell(new Phrase(empresaInfo + "\nVendedor: " + usuario + "\nFolio: " + id_ventas + "\nFecha: " + new SimpleDateFormat("dd/MM/yyyy").format(date), fuenteEmpresa));
                    empresaCell.setColspan(4); // Ocupa cuatro columnas
                    Encabezado.addCell(empresaCell);
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
            doc.add(Encabezado);

            Paragraph cli = new Paragraph();
            cli.setFont(fuenteNormal);
            cli.add("DATOS DEL CLIENTE\n");
            doc.add(cli);

            PdfPTable proveedor = new PdfPTable(3);
            proveedor.setWidthPercentage(100);
            proveedor.getDefaultCell().setBorder(0);
            float[] columnWidthsCliente = new float[]{50f, 25f, 25f};
            proveedor.setWidths(columnWidthsCliente);
            proveedor.setHorizontalAlignment(Element.ALIGN_LEFT);

            // Celdas para Nombre, Teléfono y Dirección del cliente
            PdfPCell cliNom = new PdfPCell(new Phrase("Nombre", fuenteNormal));
            PdfPCell cliTel = new PdfPCell(new Phrase("Télefono", fuenteNormal));
            PdfPCell cliDir = new PdfPCell(new Phrase("Dirección", fuenteNormal));
            cliNom.setBorder(Rectangle.NO_BORDER);
            cliTel.setBorder(Rectangle.NO_BORDER);
            cliDir.setBorder(Rectangle.NO_BORDER);
            proveedor.addCell(cliNom);
            proveedor.addCell(cliTel);
            proveedor.addCell(cliDir);

            // Consulta de datos del cliente
            String clienteQuery = "SELECT * FROM clientes WHERE Id_C = ?";
            try {
                System.out.println("ID del cliente a buscar: " + cliente); // Agregado para depuración
                ps = con.prepareStatement(clienteQuery);
                ps.setInt(1, cliente);
                rs = ps.executeQuery();
                if (rs.next()) {
                    proveedor.addCell(new Phrase(rs.getString("Nombre"), fuenteNormal));
                    proveedor.addCell(new Phrase(rs.getString("Telefono"), fuenteNormal));
                    proveedor.addCell(new Phrase(rs.getString("Direccion") + "\n\n", fuenteNormal));
                } else {
                    System.out.println("Cliente no encontrado para el ID: " + cliente); // Agregado para depuración
                    // Cliente no encontrado en la tabla "clientes", buscar en la tabla "eliminado"
                    String clienteEliminadoQuery = "SELECT * FROM eliminadosc WHERE Id = ?";
                    ps = con.prepareStatement(clienteEliminadoQuery);
                    ps.setInt(1, cliente);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        // Cliente encontrado en la tabla "eliminado", imprimir en el documento
                        proveedor.addCell(new Phrase(rs.getString("Nombre"), fuenteNormal));
                        proveedor.addCell(new Phrase(rs.getString("Telefono"), fuenteNormal));
                        proveedor.addCell(new Phrase(rs.getString("Direccion") + "\n\n", fuenteNormal));
                    } else {
                        System.out.println("Cliente no encontrado en la tabla eliminado para el ID: " + cliente); // Agregado para depuración
                        // Cliente no encontrado en ninguna tabla, imprimir en el documento
                        proveedor.addCell(new Phrase("Cliente no encontrado", fuenteNormal));
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error SQL: " + e.toString()); // Modificado para mostrar el error SQL
            } finally {
                // Asegúrate de cerrar los recursos y agregar la tabla al documento
                if (ps != null) try {
                    ps.close();
                } catch (Exception e) {
                }
                if (rs != null) try {
                    rs.close();
                } catch (Exception e) {
                }
                doc.add(proveedor);
            }

            // Tabla de productos
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.getDefaultCell().setBorder(0);
            float[] columnWidths = new float[]{25f, 35f, 20f, 20f};
            tabla.setWidths(columnWidths);
            tabla.setHorizontalAlignment(Element.ALIGN_LEFT);

            // Celdas para Cantidad, Descripción, Precio unitario y Precio Total
            PdfPCell c1 = new PdfPCell(new Phrase("Cantidad", fuenteNormal));
            PdfPCell c2 = new PdfPCell(new Phrase("Descripción", fuenteNormal));
            PdfPCell c3 = new PdfPCell(new Phrase("Precio unt.", fuenteNormal));
            PdfPCell c4 = new PdfPCell(new Phrase("Precio Total", fuenteNormal));
            c1.setBorder(Rectangle.NO_BORDER);
            c2.setBorder(Rectangle.NO_BORDER);
            c3.setBorder(Rectangle.NO_BORDER);
            c4.setBorder(Rectangle.NO_BORDER);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c2.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c3.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c4.setBackgroundColor(BaseColor.LIGHT_GRAY);
            tabla.addCell(c1);
            tabla.addCell(c2);
            tabla.addCell(c3);
            tabla.addCell(c4);

            // Consulta de productos
String product = "SELECT p.Precio_U, p.cantidad, p.Codigo FROM detalle_venta p JOIN venta v ON p.Id_Ventas = v.Id_Ventas WHERE v.Id_Ventas = ?";
try {
    ps = con.prepareStatement(product);
    ps.setInt(1, id_ventas);
    rs = ps.executeQuery();
    while (rs.next()) {
        double subTotal = rs.getInt("Cantidad") * rs.getDouble("Precio_U");

        // Obtén el nombre del producto basado en el código
        String codigoProducto = rs.getString("Codigo");
        String nombreProducto = obtenerNombreProducto(codigoProducto);  // Función auxiliar para obtener el nombre

        tabla.addCell(new Phrase(rs.getString("Cantidad"), fuenteNormal));
        tabla.addCell(new Phrase(nombreProducto, fuenteNormal));  // Utiliza el nombre obtenido
        tabla.addCell(new Phrase(rs.getString("Precio_U"), fuenteNormal));
        tabla.addCell(new Phrase(String.valueOf(subTotal), fuenteNormal));
    }
} catch (SQLException e) {
    System.out.println(e.toString());
}
doc.add(tabla);


            // Total
            Paragraph info = new Paragraph();
            info.setFont(fuenteNormal);
            info.add(Chunk.NEWLINE);
            info.add("Total MXN: " + total);
            info.setAlignment(Element.ALIGN_RIGHT);
            doc.add(info);

            // Firma
            Paragraph firma = new Paragraph();
            firma.add(Chunk.NEWLINE);
            firma.setAlignment(Element.ALIGN_CENTER);
            doc.add(firma);

            // Otras secciones del ticket
            Paragraph gr = new Paragraph();
            gr.add(Chunk.NEWLINE);
            gr.setAlignment(Element.ALIGN_CENTER);
            doc.add(gr);

            doc.close();
            archivo.close();
            Desktop.getDesktop().open(salida);
        } catch (DocumentException | IOException e) {
            System.out.println(e.toString());
        }
    }
    
private String obtenerNombreProducto(String codigoProducto) {
    String nombreProducto = "Nombre no encontrado";  // Valor predeterminado si no se encuentra

    // Busca el nombre en la tabla de productos
    String queryProductos = "SELECT Nombre FROM producto WHERE Clavealterna = ?";
    try {
        PreparedStatement psProductos = con.prepareStatement(queryProductos);
        psProductos.setString(1, codigoProducto);
        ResultSet rsProductos = psProductos.executeQuery();
        if (rsProductos.next()) {
            nombreProducto = rsProductos.getString("Nombre");
        } else {
            // Si no se encuentra en productos, busca en subproductos
            String querySubproductos = "SELECT Nombre FROM subproducto WHERE ClaveAlterna = ?";
            PreparedStatement psSubproductos = con.prepareStatement(querySubproductos);
            psSubproductos.setString(1, codigoProducto);
            ResultSet rsSubproductos = psSubproductos.executeQuery();
            if (rsSubproductos.next()) {
                nombreProducto = rsSubproductos.getString("Nombre");
            }
        }

        // Limitar la longitud del nombre a un máximo de 15 letras
        if (nombreProducto.length() > 15) {
            nombreProducto = nombreProducto.substring(0, 15);
        }
    } catch (SQLException e) {
        System.out.println(e.toString());
    }

    return nombreProducto;
}


    
    
}
