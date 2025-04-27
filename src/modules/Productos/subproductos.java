package modules.Productos;

public class subproductos {
    private String Codigo;
    private String Nombre;
    private String ClaveAlterna;
    private Double precio;
    private String unidad;
    private String granel;
    private byte[] imagen;

    public subproductos() {
    }

    public subproductos(String Nombre, String ClaveAlterna, Double precio, String unidad) {
        this.Nombre = Nombre;
        this.ClaveAlterna = ClaveAlterna;
        this.precio = precio;
        this.unidad = unidad;
    }


    
    
    public subproductos(String Codigo, String Nombre, Double precio, String unidad, String granel, byte[] imagen) {
        this.Codigo = Codigo;
        this.Nombre = Nombre;
        this.precio = precio;
        this.unidad = unidad;
        this.granel = granel;
        this.imagen = imagen;
    }
    
    

    public subproductos(String Codigo, String Nombre, String ClaveAlterna, Double precio, String unidad, String granel, byte[] imagen) {
        this.Codigo = Codigo;
        this.Nombre = Nombre;
        this.ClaveAlterna = ClaveAlterna;
        this.precio = precio;
        this.unidad = unidad;
        this.granel = granel;
        this.imagen = imagen;
    }

    public String getGranel() {
        return granel;
    }

    public void setGranel(String granel) {
        this.granel = granel;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getClaveAlterna() {
        return ClaveAlterna;
    }

    public void setClaveAlterna(String ClaveAlterna) {
        this.ClaveAlterna = ClaveAlterna;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

}
