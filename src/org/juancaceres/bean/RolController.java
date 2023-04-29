
package org.juancaceres.bean;

public class RolController {
    private  int codigoRol;
    private String tipoRol;

    public RolController() {
    }

    public RolController(int codigoRol, String tipoRol) {
        this.codigoRol = codigoRol;
        this.tipoRol = tipoRol;
    }

    public int getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(int codigoRol) {
        this.codigoRol = codigoRol;
    }

    public String getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(String tipoRol) {
        this.tipoRol = tipoRol;
    }
    
    
    
}
