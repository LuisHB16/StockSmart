package codigo;

public class ProductoModel {
    private String claveAlterna = "";
    private String nombre = "";
    private String existencia = "";
    private String precio = "";
    private String descripcion = "";
    private String unidad = "";
    private byte[] imagen;
    private String unidadC = "";
    private int ExtMin;
   private int ExtMax;

    public String getUnidadC() {
        return unidadC;
    }

    public int getExtMin() {
        return ExtMin;
    }

    public void setExtMin(int ExtMin) {
        this.ExtMin = ExtMin;
    }

    public int getExtMax() {
        return ExtMax;
    }

    public void setExtMax(int ExtMax) {
        this.ExtMax = ExtMax;
    }

    // Getters y setters
    public void setUnidadC(String unidadC) {
        this.unidadC = unidadC;
    }

    public String getClaveAlterna() {
        return claveAlterna;
    }

    public void setClaveAlterna(String claveAlterna) {
        this.claveAlterna = claveAlterna;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getExistencia() {
        return existencia;
    }

    public void setExistencia(String existencia) {
        this.existencia = existencia;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
}

