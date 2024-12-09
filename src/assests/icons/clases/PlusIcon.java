/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package assests.icons.clases;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Icon;

/**
 *
 * @author alanm
 */
public class PlusIcon implements Icon{
     private int width;  // Ancho del ícono
    private int height; // Altura del ícono
    private Color color; // Color del símbolo "+"

    // Constructor para definir dimensiones y color del ícono
    public PlusIcon(int width, int height, Color color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g;

        // Activar antialiasing para suavizar el dibujo
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dibujar fondo transparente o blanco
        g2d.setColor(new Color(255, 255, 255, 0)); // Fondo transparente
        g2d.fillRect(x, y, width, height);

        // Configurar color del símbolo "+"
        g2d.setColor(color);

        // Calcular el grosor del trazo del "+" y sus dimensiones
        int lineThickness = Math.min(width, height) / 9; // Grosor proporcional al tamaño
        int centerX = x + width / 2;
        int centerY = y + height / 2;

        // Dibujar la línea vertical del "+"
        g2d.fillRect(centerX - lineThickness / 2, y + lineThickness, lineThickness, height - 2 * lineThickness);

        // Dibujar la línea horizontal del "+"
        g2d.fillRect(x + lineThickness, centerY - lineThickness / 2, width - 2 * lineThickness, lineThickness);
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }

}
