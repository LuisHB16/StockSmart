
package codigo;

/**
 *
 * @author truea
 */
public class proveedores {
    
    private String marca;
    private String telefono;
    private String direccion;
    private String correo;

    public proveedores(String marca, String telefono, String direccion, String correo) {
        this.marca = marca;
        this.telefono = telefono;
        this.direccion = direccion;
        this.correo = correo;
    }

    public proveedores() {
    }
    

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    
}
