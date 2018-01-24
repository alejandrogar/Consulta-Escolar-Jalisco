package mx.gob.jalisco.edu.consultaescolar.objects;


public class CCT {

    private String clavecct;
    private String domicilio;
    private String nombrect;
    private String n_municipi;

    public CCT(){}

    public CCT(String clavecct, String domicilio, String nombrect, String n_municipi){
        this.clavecct = clavecct;
        this.domicilio = domicilio;
        this.nombrect = nombrect;
        this.n_municipi = n_municipi;
    }

    public String getClavecct() {
        return clavecct;
    }

    public void setClavecct(String clavecct) {
        this.clavecct = clavecct;
    }

    public String getNombrect() {
        return nombrect;
    }

    public void setNombrect(String nombrect) {
        this.nombrect = nombrect;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getN_municipi() {
        return n_municipi;
    }

    public void setN_municipi(String n_municipi) {
        this.n_municipi = n_municipi;
    }
}
