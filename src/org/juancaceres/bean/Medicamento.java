
package org.juancaceres.bean;


public class Medicamento {
    private int codigoMedicamento;
    private String descripcion;

    public Medicamento() {
    }

    public Medicamento(int codigoMedicamento, String descripcion) {
        this.codigoMedicamento = codigoMedicamento;
        this.descripcion = descripcion;
    }

    public int getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(int codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return getCodigoMedicamento()+ "| "+ getDescripcion();
    }
    
    
}
