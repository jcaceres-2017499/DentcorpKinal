/*
    Nombre: Juan Pablo Caceres Enriquez
    Codigo Técnico: IN5BM
    Carné: 2017499
    Fecha de creación: 19-07-2022
 */
package org.juancaceres.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.juancaceres.bean.Doctor;
import org.juancaceres.bean.Receta;
import org.juancaceres.db.Conexion;
import org.juancaceres.report.GenerarReporte;
import org.juancaceres.system.Principal;


public class RecetaController implements Initializable {    
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,ELIMINAR,EDITAR,GUARDAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Receta> listaReceta;
    private ObservableList<Doctor>listaDoctor;
    private DatePicker fR;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML TableColumn colCodigoReceta;
    @FXML TableColumn colFechaReceta;
    @FXML TableColumn colNumeroColegiado;
    @FXML TextField txtCodigoReceta;
    @FXML TableView tblRecetas;
    @FXML GridPane grpFechas;
    @FXML ComboBox cmbNumeroColegiado;
    @FXML Label lblConteo;
    int numero;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fR = new DatePicker(Locale.ENGLISH);
        fR.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fR.getCalendarView().todayButtonTextProperty().set("Today");
        fR.getCalendarView().setShowWeeks(false);
        grpFechas.add(fR, 1, 1);
        fR.getStylesheets().add("/org/juancaceres/resource/DatePicker.css");
        cmbNumeroColegiado.setItems(getDoctor());
        conteoLabel();
    }
    
    private void conteoLabel() {
        String tamaño = " " + numero;
        System.out.println("Numero de datos: " + numero);
        lblConteo.setText(tamaño);
    }
    
    public void cargarDatos(){
        tblRecetas.setItems(getReceta());
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("codigoReceta"));
        colFechaReceta.setCellValueFactory(new PropertyValueFactory<Receta, Date>("fechaReceta"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Receta, Integer>("numeroColegiado"));
        conteoLabel();
    }
    
    public void seleccionarElemento(){
      if(tblRecetas.getSelectionModel().getSelectedItem() != null){
        txtCodigoReceta.setText(String.valueOf(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
        fR.selectedDateProperty().set(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getFechaReceta());
        cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
    }
      else{
          JOptionPane.showMessageDialog(null, "No hay ningun registro en el lugar seleccionado");
        }
    }  
    
    public ObservableList<Receta> getReceta(){
        ArrayList<Receta> lista = new ArrayList<Receta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Receta(resultado.getInt("codigoReceta"),
                        resultado.getDate("fechaReceta"),
                        resultado.getInt("numeroColegiado")));
                numero = lista.size();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaReceta = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Doctor> getDoctor(){
        ArrayList<Doctor> lista = new ArrayList<Doctor>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarDoctores()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Doctor(resultado.getInt("numeroColegiado"),
                                    resultado.getString("nombresDoctor"),
                                    resultado.getString("apellidosDoctor"),
                                    resultado.getString("telefonoContacto"),
                                    resultado.getInt("codigoEspecialidad")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDoctor = FXCollections.observableArrayList(lista);
    }
    
    public Doctor buscarDoctor (int numeroColegiado){
        Doctor resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarDoctor(?)}");
            procedimiento.setInt(1, numeroColegiado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Doctor(registro.getInt("numeroColegiado"),
                                            registro.getString("nombresDoctor"),
                                            registro.getString("apellidosDoctor"),
                                            registro.getString("telefonoContacto"),
                                            registro.getInt("codigoEspecialidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
               
    }
    
    public void nuevo (){
        switch(tipoDeOperacion){
            case NINGUNO:
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                imgNuevo.setImage(new Image("/org/juancaceres/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/juancaceres/image/Cancelar.png"));
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/juancaceres/image/guardar.png"));
                imgEliminar.setImage(new Image("/org/juancaceres/image/eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                conteoLabel();           
        }    
    }
    
     public void guardar(){
        Receta registro = new Receta();
        registro.setFechaReceta(fR.getSelectedDate());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarReceta(?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.setInt(2, registro.getNumeroColegiado());
            procedimiento.execute();
            listaReceta.add(registro);
            listaReceta.add(registro);
                        numero = listaReceta.size();
            cargarDatos();
            conteoLabel();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     
     public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                imgNuevo.setImage(new Image("/org/juancaceres/image/nuevo 96.png"));
                imgEliminar.setImage(new Image("/org/juancaceres/image/eliminar.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Receta",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarReceta(?)}");
                            procedimiento.setInt(1, ((Receta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                            procedimiento.execute();
                            listaReceta.remove(tblRecetas.getSelectionModel().getSelectedIndex());
                            numero = numero -1;
                            cargarDatos();
                            conteoLabel();
                            limpiarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else
                    JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento");
        }
    }
     
     public void editar(){
         switch(tipoDeOperacion){
             case NINGUNO:
                 if(tblRecetas.getSelectionModel().getSelectedItem() != null){
                     btnEditar.setText("Actualizar");
                     btnReporte.setText("Cancelar");
                     btnNuevo.setDisable(true);
                     btnEliminar.setDisable(true);
                     imgEditar.setImage(new Image("/org/juancaceres/image/Actualizar 96.png"));
                     imgReporte.setImage(new Image("/org/juancaceres/image/Cancelar.png"));
                     activarControles();
                     txtCodigoReceta.setEditable(false);
                     tipoDeOperacion = operaciones.ACTUALIZAR;                 
                 }else
                     JOptionPane.showMessageDialog(null, "Debe seleccionar un elemento");
                 break;
             case ACTUALIZAR:
                 actualizar();
                 btnEditar.setText("Editar");
                     btnReporte.setText("Reporte");
                     btnNuevo.setDisable(false);
                     btnEliminar.setDisable(false);
                     imgEditar.setImage(new Image("/org/juancaceres/image/Actualizar 96.png"));
                     imgReporte.setImage(new Image("/org/juancaceres/image/Reporte.png"));
                     desactivarControles();
                     limpiarControles();
                     tipoDeOperacion = operaciones.NINGUNO;
                     cargarDatos();
                     break;
         }
     }
     
     public void actualizar(){
         try{
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarReceta(?, ?)}");
           Receta registro = (Receta)tblRecetas.getSelectionModel().getSelectedItem();
           registro.setFechaReceta(fR.getSelectedDate());
           procedimiento.setInt(1, registro.getCodigoReceta());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaReceta().getTime()));
            procedimiento.execute();
         }catch(Exception e){
             e.printStackTrace();
         }
     }
     
     public void reporte(){
         switch(tipoDeOperacion){
             case NINGUNO:
                imprimirReporte();
             break;   
             case ACTUALIZAR:
                desactivarControles();
                limpiarControles();
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                imgEditar.setImage(new Image("/org/juancaceres/image/Reporte.png"));
                imgReporte.setImage(new Image("/org/juancaceres/image/Recetas.png"));
                tipoDeOperacion = operaciones.NINGUNO;
                break;
         }
    }
     
    public void generarReporte() {
        switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
                break;
        }
    }
    
    public void imprimirReporte() {
        Map parametros = new HashMap();
        if(tblRecetas.getSelectionModel().getSelectedItem() != null){
        int codReceta = ((Receta) tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta();
        parametros.put("codigoReceta", null);
        GenerarReporte.mostrarReporte("ReporteRecetas.jasper", "Reporte de recetas", parametros);
        }else
            JOptionPane.showMessageDialog(null, "Debe Seleccionar un elemento");
    } 
     
    public void desactivarControles(){
        txtCodigoReceta.setEditable(false);
        cmbNumeroColegiado.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoReceta.setEditable(false);
        cmbNumeroColegiado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoReceta.clear();
        cmbNumeroColegiado.getSelectionModel().clearSelection();
        tblRecetas.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
             
}
