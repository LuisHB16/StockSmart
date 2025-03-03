/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;


public class venta {
    private int Id_Ventas;
    private int Id_clientes;
    private String Fecha;
    private double Total;
    private String vendedor;
    private String repartidor;
    private String status;

    public venta(int Id_Ventas, int Id_clientes, String Fecha, double Total, String vendedor, String repartidor, String status) {
        this.Id_Ventas = Id_Ventas;
        this.Id_clientes = Id_clientes;
        this.Fecha = Fecha;
        this.Total = Total;
        this.vendedor = vendedor;
        this.repartidor = repartidor;
        this.status = status;
    }

    public venta() {
    }

    public venta(int Id_clientes, String Fecha, double Total, String vendedor, String repartidor, String status) {
        this.Id_clientes = Id_clientes;
        this.Fecha = Fecha;
        this.Total = Total;
        this.vendedor = vendedor;
        this.repartidor = repartidor;
        this.status = status;
    }

    public int getId_Ventas() {
        return Id_Ventas;
    }

    public void setId_Ventas(int Id_Ventas) {
        this.Id_Ventas = Id_Ventas;
    }

    public int getId_clientes() {
        return Id_clientes;
    }

    public void setId_clientes(int Id_clientes) {
        this.Id_clientes = Id_clientes;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String Fecha) {
        this.Fecha = Fecha;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double Total) {
        this.Total = Total;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public String getRepartidor() {
        return repartidor;
    }

    public void setRepartidor(String repartidor) {
        this.repartidor = repartidor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
