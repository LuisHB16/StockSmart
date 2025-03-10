package codigo;

public class Unidad {
    private String Unidad;
    private Double Formato;

    public Unidad(String Unidad, Double Formato) {
        this.Unidad = Unidad;
        this.Formato = Formato;
    }

    public Unidad(String Unidad) {
        this.Unidad = Unidad;
    }

    public String getUnidad() {
        return Unidad;
    }

    public void setUnidad(String Unidad) {
        this.Unidad = Unidad;
    }

    public Double getFormato() {
        return Formato;
    }

    public void setFormato(Double Formato) {
        this.Formato = Formato;
    }
}
