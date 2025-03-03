/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;


public class productos {
    
   private String codigo;
   private String clavealterna;
   private String nombre;
   private Double existencia;
   private String marca;
   private Double precio;
   private String descripcion;
   private String Granel;
   private String Unidad;
   private byte[] imagen;
   private String Categoria;
   private String UnidadC;
   private int ExtMin;
   private int ExtMax;

    public productos() {
    }

    public productos(String clavealterna, String nombre, Double existencia, String marca, Double precio, String descripcion, String Granel, String Unidad, byte[] imagen, String Categoria, String UnidadC, int ExtMin, int ExtMax) {
        this.clavealterna = clavealterna;
        this.nombre = nombre;
        this.existencia = existencia;
        this.marca = marca;
        this.precio = precio;
        this.descripcion = descripcion;
        this.Granel = Granel;
        this.Unidad = Unidad;
        this.imagen = imagen;
        this.Categoria = Categoria;
        this.UnidadC = UnidadC;
        this.ExtMin = ExtMin;
        this.ExtMax = ExtMax;
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


    
    

    public String getUnidadC() {
        return UnidadC;
    }

    public void setUnidadC(String UnidadC) {
        this.UnidadC = UnidadC;
    }
    
    
    

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getClavealterna() {
        return clavealterna;
    }

    public void setClavealterna(String clavealterna) {
        this.clavealterna = clavealterna;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getExistencia() {
        return existencia;
    }

    public void setExistencia(Double existencia) {
        this.existencia = existencia;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getGranel() {
        return Granel;
    }

    public void setGranel(String Granel) {
        this.Granel = Granel;
    }

    public String getUnidad() {
        return Unidad;
    }

    public void setUnidad(String Unidad) {
        this.Unidad = Unidad;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }
    
    

}
