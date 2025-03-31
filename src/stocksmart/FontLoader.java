package stocksmart;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class FontLoader {
    
    private static final String FONT_PATH = "src/assests/fonts/Inter,Lusitana/Inter/Inter-VariableFont_opsz,wght.ttf";
    public static Font customFont;
    public static Font customFontBold;
    public static Font customFontBold2;
    public static Font customFontBold3;

    // Bloque estático para inicializar las fuentes
    static {
        
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(FONT_PATH)).deriveFont(18f);
            customFontBold = customFont.deriveFont(Font.BOLD, 30f);
            customFontBold2 = customFont.deriveFont(Font.PLAIN, 14f);
            customFontBold3 = customFont.deriveFont(Font.PLAIN, 22f);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la fuente.");
            customFont = new Font("Serif", Font.PLAIN, 18); // Fuente predeterminada en caso de error
            customFontBold = customFont.deriveFont(Font.BOLD, 30f);
            customFontBold2 = customFont.deriveFont(Font.PLAIN, 14f);
        }
        
    }
    
}
