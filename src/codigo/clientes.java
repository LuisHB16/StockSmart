/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codigo;

public class clientes {
    
    
    private String Id_C;
    private String Nombre;
    private String Apellidop;
    private String Apellidom;
    private String Direccion;
    private String Telefono;
    private String correo;
    private String Contraseña;
    private String Status;

    public clientes(String Id_C, String Nombre, String Apellidop, String Apellidom, String Direccion, String Telefono, String correo, String Contraseña, String Status) {
        this.Id_C = Id_C;
        this.Nombre = Nombre;
        this.Apellidop = Apellidop;
        this.Apellidom = Apellidom;
        this.Direccion = Direccion;
        this.Telefono = Telefono;
        this.correo = correo;
        this.Contraseña = Contraseña;
        this.Status = Status;
    }

    public clientes(String Nombre, String Apellidop, String Apellidom, String Direccion, String Telefono, String correo, String Contraseña, String Status) {
        this.Nombre = Nombre;
        this.Apellidop = Apellidop;
        this.Apellidom = Apellidom;
        this.Direccion = Direccion;
        this.Telefono = Telefono;
        this.correo = correo;
        this.Contraseña = Contraseña;
        this.Status = Status;
    }

    public clientes(String Nombre, String Apellidop, String Apellidom, String Direccion, String Telefono) {
        this.Nombre = Nombre;
        this.Apellidop = Apellidop;
        this.Apellidom = Apellidom;
        this.Direccion = Direccion;
        this.Telefono = Telefono;
    }  

    public String getId_C() {
        return Id_C;
    }

    public void setId_C(String Id_C) {
        this.Id_C = Id_C;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellidop() {
        return Apellidop;
    }

    public void setApellidop(String Apellidop) {
        this.Apellidop = Apellidop;
    }

    public String getApellidom() {
        return Apellidom;
    }

    public void setApellidom(String Apellidom) {
        this.Apellidom = Apellidom;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return Contraseña;
    }

    public void setContraseña(String Contraseña) {
        this.Contraseña = Contraseña;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    
}
