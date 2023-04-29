package org.juancaceres.bean;

import java.util.Date;

public class Paciente {
    private int codigoPaciente;
    private String nombresPaciente;
    private String apellidosPaciente;
    private String sexo;
    private Date fechaNaciemiento;
    private String direccionPaciente;
    private String telefonoPersonal;
    private Date fechaPrimeraVisita;

    public Paciente() {
    }

    public Paciente(int codigoPaciente, String nombresPaciente, String apellidosPaciente, String sexo, Date fechaNaciemiento, String direccionPaciente, String telefonoPersonal, Date fechaPrimeraVisita) {
        this.codigoPaciente = codigoPaciente;
        this.nombresPaciente = nombresPaciente;
        this.apellidosPaciente = apellidosPaciente;
        this.sexo = sexo;
        this.fechaNaciemiento = fechaNaciemiento;
        this.direccionPaciente = direccionPaciente;
        this.telefonoPersonal = telefonoPersonal;
        this.fechaPrimeraVisita = fechaPrimeraVisita;
    }

    public int getCodigoPaciente() {
        return codigoPaciente;
    }

    public void setCodigoPaciente(int codigoPaciente) {
        this.codigoPaciente = codigoPaciente;
    }

    public String getNombresPaciente() {
        return nombresPaciente;
    }

    public void setNombresPaciente(String nombresPaciente) {
        this.nombresPaciente = nombresPaciente;
    }

    public String getApellidosPaciente() {
        return apellidosPaciente;
    }

    public void setApellidosPaciente(String apellidosPaciente) {
        this.apellidosPaciente = apellidosPaciente;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Date getFechaNaciemiento() {
        return fechaNaciemiento;
    }

    public void setFechaNaciemiento(Date fechaNaciemiento) {
        this.fechaNaciemiento = fechaNaciemiento;
    }

    public String getDireccionPaciente() {
        return direccionPaciente;
    }

    public void setDireccionPaciente(String direccionPaciente) {
        this.direccionPaciente = direccionPaciente;
    }

    public String getTelefonoPersonal() {
        return telefonoPersonal;
    }

    public void setTelefonoPersonal(String telefonoPersonal) {
        this.telefonoPersonal = telefonoPersonal;
    }

    public Date getFechaPrimeraVisita() {
        return fechaPrimeraVisita;
    }

    public void setFechaPrimeraVisita(Date fechaPrimeraVisita) {
        this.fechaPrimeraVisita = fechaPrimeraVisita;
    }

    @Override
    public String toString() {
        return getCodigoPaciente() + "| "+ getNombresPaciente();
    } 
}

