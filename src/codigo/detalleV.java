package codigo;

public class detalleV {
    
    private int Id_Ventas;
    private String Codigo;
    private double cantidad;
    private double Precio_U;
    private String unidad;
    private Double Total;

    public detalleV(int Id_Ventas, String Codigo, double cantidad, double Precio_U, String unidad) {
        this.Id_Ventas = Id_Ventas;
        this.Codigo = Codigo;
        this.cantidad = cantidad;
        this.Precio_U = Precio_U;
        this.unidad = unidad;
    }

    public detalleV(int Id_Ventas, String Codigo, double cantidad, double Precio_U, String unidad, Double Total) {
        this.Id_Ventas = Id_Ventas;
        this.Codigo = Codigo;
        this.cantidad = cantidad;
        this.Precio_U = Precio_U;
        this.unidad = unidad;
        this.Total = Total;
    }

    public Double getTotal() {
        return Total;
    }

    public void setTotal(Double Total) {
        this.Total = Total;
    }
    
    

    public detalleV() {
    }
    
    public int getId_Ventas() {
        return Id_Ventas;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public void setId_Ventas(int Id_Ventas) {
        this.Id_Ventas = Id_Ventas;
    }

    public String getCodigo() {
        return Codigo;
    }

    public void setCodigo(String Codigo) {
        this.Codigo = Codigo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio_U() {
        return Precio_U;
    }

    public void setPrecio_U(double Precio_U) {
        this.Precio_U = Precio_U;
    }
    
    
}
