package modules.Movimientos;

public class movimiento {
    private String Movimiento;
    private String Fehca;
    private Double Cantidad;
    private String Coidgo;
    private Double inventario_antes;
    private Double Formato; 

    public movimiento(String Movimiento, String Fehca, Double Cantidad, String Coidgo, Double inventario_antes) {
        this.Movimiento = Movimiento;
        this.Fehca = Fehca;
        this.Cantidad = Cantidad;
        this.Coidgo = Coidgo;
        this.inventario_antes = inventario_antes;
    }

    public movimiento(String Movimiento, String Fehca, Double Cantidad, String Coidgo, Double inventario_antes, Double Formato) {
        this.Movimiento = Movimiento;
        this.Fehca = Fehca;
        this.Cantidad = Cantidad;
        this.Coidgo = Coidgo;
        this.inventario_antes = inventario_antes;
        this.Formato = Formato;
    }
    
    

    public movimiento() {
    }

    public Double getFormato() {
        return Formato;
    }

    public void setFormato(Double Formato) {
        this.Formato = Formato;
    }
    
    
    

    public String getMovimiento() {
        return Movimiento;
    }

    public void setMovimiento(String Movimiento) {
        this.Movimiento = Movimiento;
    }

    public String getFehca() {
        return Fehca;
    }

    public void setFehca(String Fehca) {
        this.Fehca = Fehca;
    }

    public Double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Double Cantidad) {
        this.Cantidad = Cantidad;
    }

    public String getCoidgo() {
        return Coidgo;
    }

    public void setCoidgo(String Coidgo) {
        this.Coidgo = Coidgo;
    }

    public Double getInventario_antes() {
        return inventario_antes;
    }

    public void setInventario_antes(Double inventario_antes) {
        this.inventario_antes = inventario_antes;
    }
    
    
}