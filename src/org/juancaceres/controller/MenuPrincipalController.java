/*
    Nombre: Juan Pablo Caceres Enriquez
    Codigo Técnico: IN5BM
    Carné: 2017499
    Fecha de creación: 19-07-2022
 */
package org.juancaceres.controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.juancaceres.report.GenerarReporte;
import org.juancaceres.system.Principal;


public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    
    private final String PAQUETE_IMAGES = "org/juancaceres/image/";
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
     public void ventanaProgramador(){
        escenarioPrincipal.ventanaProgramador();
    }
     public void ventanaPaciente(){
        escenarioPrincipal.ventanaPaciente();
    }
     public void ventanaEspecialidades(){
         escenarioPrincipal.ventanaEspecialidades();
    }
     public void ventanaMedicamento(){
         escenarioPrincipal.ventanaMedicamento();
    }
     public void ventanaDoctores(){
         escenarioPrincipal.ventanaDoctores();
    }
     public void ventanaRecetas(){
         escenarioPrincipal.ventanaRecetas();
    }
     public void ventanaDetalleRecetas(){
         escenarioPrincipal.ventanaDetalleRecetas();
    }
     public void ventanaCitas(){
         escenarioPrincipal.ventanaCitas();
    }
     public void ventanaLogin(){
         escenarioPrincipal.ventanaLogin();
    }
      public void ventanaUsuario(){
         escenarioPrincipal.ventanaUsuario();
    }     
     public void cerrar(){
        System.exit(0);
    }
    
     public void imprimirReportePacientes() {
        Map parametros = new HashMap();
        parametros.put("codigoPaciente", null);
        GenerarReporte.mostrarReporte("ReportePacientes.jasper", "Reporte de pacientes", parametros);
    } 
     public void imprimirReporteEspecialidades() {
        Map parametros = new HashMap();
        parametros.put("codigoEspecialidad", null);
        GenerarReporte.mostrarReporte("ReporteEspecialidades.jasper", "Reporte de especialidades", parametros);
    } 
     public void imprimirReporteCitas() {
        Map parametros = new HashMap();
        parametros.put("codigoCita", null);
        GenerarReporte.mostrarReporte("ReporteCitas.jasper", "Reporte de citass", parametros);
    } 
     public void imprimirReporteMedicamentos() {
        Map parametros = new HashMap();
        parametros.put("codigoMedicamento", null);
        GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper", "Reporte de medicamentos", parametros);
    } 
     public void imprimirReporteDoctores() {
        Map parametros = new HashMap();
        parametros.put("numeroColegiado", null);
        GenerarReporte.mostrarReporte("ReporteDoctores.jasper", "Reporte de doctores", parametros);
    } 
     public void imprimirReporteRecetas() {
        Map parametros = new HashMap();
        parametros.put("codigoReceta", null);
        GenerarReporte.mostrarReporte("ReporteReceta.jasper", "Reporte de recetas", parametros);
    } 
     public void imprimirReporteDetalleReceta() {
        Map parametros = new HashMap();
        parametros.put("codigoDetalleReceta", null);
        GenerarReporte.mostrarReporte("ReporteDetalleReceta.jasper", "Reporte de detalle recetas", parametros);
        parametros.put("LOGO_HEADER", PAQUETE_IMAGES + "Icono.jpg");
        parametros.put("LOGO_FOOTER", PAQUETE_IMAGES + "Banner DentCorp.png");
        parametros.put("LOGO_BACKGROUND", PAQUETE_IMAGES + "Plantilla Reporte 4.png");
    } 
}
