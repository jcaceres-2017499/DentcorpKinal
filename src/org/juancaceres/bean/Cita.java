package org.juancaceres.bean;

import java.util.Date;


public class Cita {
    private int codigoCita; 
    private Date fechaCita; 
    private String horaCita; 
    private String tratamiento; 
    private String desripCondActual; 
    private int codigoPaciente; 
    private int numeroColegiado;

    public Cita() {
    }

    public Cita(int codigoCita, Date fechaCita, String horaCita, String tratamiento, String desripCondActual, int codigoPaciente, int numeroColegiado) {
        this.codigoCita = codigoCita;
        this.fechaCita = fechaCita;
        this.horaCita = horaCita;
        this.tratamiento = tratamiento;
        this.desripCondActual = desripCondActual;
        this.codigoPaciente = codigoPaciente;
        this.numeroColegiado = numeroColegiado;
    }

    public int getCodigoCita() {
        return codigoCita;
    }

    public void setCodigoCita(int codigoCita) {
        this.codigoCita = codigoCita;
    }

    public Date getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(Date fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public String getDesripCondActual() {
        return desripCondActual;
    }

    public void setDesripCondActual(String desripCondActual) {
        this.desripCondActual = desripCondActual;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public int getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(int numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }
    
    
}
