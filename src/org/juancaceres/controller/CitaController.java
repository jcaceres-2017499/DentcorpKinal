/*
    Nombre: Juan Pablo Caceres Enriquez
    Codigo Técnico: IN5BM
    Carné: 2017499
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
import org.juancaceres.bean.Cita;
import org.juancaceres.bean.Doctor;
import org.juancaceres.bean.Paciente;
import org.juancaceres.db.Conexion;
import org.juancaceres.report.GenerarReporte;
import org.juancaceres.system.Principal;


public class CitaController implements Initializable{
        private Principal escenarioPrincipal;
        private enum operaciones{NUEVO,ELIMINAR,EDITAR,GUARDAR,ACTUALIZAR,CANCELAR,NINGUNO};
        private operaciones tipoDeOperacion = operaciones.NINGUNO;
        private ObservableList<Cita>listaCita;
        private ObservableList<Paciente>listaPaciente;
        private ObservableList<Doctor>listaDoctor;
        private DatePicker fCita;
        @FXML private Button btnNuevo;
        @FXML private Button btnEliminar;
        @FXML private Button btnEditar;
        @FXML private Button btnReporte;
        @FXML private ImageView imgNuevo;
        @FXML private ImageView imgEliminar;
        @FXML private ImageView imgEditar;
        @FXML private ImageView imgReporte;
        @FXML private TextField txtCodigoCita;
        @FXML private TextField txtHoraCita;
        @FXML private TextField txtTratamiento;
        @FXML private TextField txtCondicionActual;
        @FXML private ComboBox cmbCodigoPaciente;
        @FXML private ComboBox cmbNumeroColegiado;
        @FXML private GridPane grpFechas;
        @FXML private TableView tblCitas;
        @FXML private TableColumn colCodigoCita;
        @FXML private TableColumn colFechaCita;
        @FXML private TableColumn colHoraCita;
        @FXML private TableColumn colTratamiento;
        @FXML private TableColumn colCondicionActual;
        @FXML private TableColumn colCodigoPaciente;
        @FXML private TableColumn colNumeroColegiado;
        @FXML Label lblConteo;
        int numero;
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fCita = new DatePicker(Locale.ENGLISH);
        fCita.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fCita.getCalendarView().todayButtonTextProperty().set("Today");
        fCita.getCalendarView().setShowWeeks(false);
        grpFechas.add(fCita, 3, 0); //Cuidado
        fCita.getStylesheets().add("/org/juancaceres/resource/DatePicker.css");
        cmbNumeroColegiado.setItems(getDoctor());
        cmbCodigoPaciente.setItems(getPaciente());
        conteoLabel();
    }
    
    private void conteoLabel() {
        String tamaño = " " + numero;
        System.out.println("Numero de datos: " + numero);
        lblConteo.setText(tamaño);
    }
    
    public void cargarDatos(){
        tblCitas.setItems(getCita());
        colCodigoCita.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoCita"));
        colFechaCita.setCellValueFactory(new PropertyValueFactory<Cita, Date>("fechaCita"));
        colHoraCita.setCellValueFactory(new PropertyValueFactory<Cita, String>("horaCita"));
        colTratamiento.setCellValueFactory(new PropertyValueFactory<Cita, String>("tratamiento"));
        colCondicionActual.setCellValueFactory(new PropertyValueFactory<Cita, String>("desripCondActual"));
        colCodigoPaciente.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("codigoPaciente"));
        colNumeroColegiado.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("numeroColegiado"));
        conteoLabel();
    }
    
    public void seleccionarElemento(){
      if(tblCitas.getSelectionModel().getSelectedItem() != null){
        txtCodigoCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita()));
        fCita.selectedDateProperty().set(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getFechaCita());
        txtHoraCita.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getHoraCita()));
        txtTratamiento.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getTratamiento()));
        txtCondicionActual.setText(String.valueOf(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getDesripCondActual()));
        cmbNumeroColegiado.getSelectionModel().select(buscarDoctor(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getNumeroColegiado()));
       cmbCodigoPaciente.getSelectionModel().select(buscarPaciente(((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoPaciente()));
    }  
       else{
          JOptionPane.showMessageDialog(null, "No hay ningun registro en el lugar seleccionado");
        }
    } 
    
    public Paciente buscarPaciente (int codigoPaciente){
        Paciente resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarPaciente(?)}");
            procedimiento.setInt(1, codigoPaciente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Paciente(registro.getInt("codigoPaciente"),
                                            registro.getString("nombresPaciente"),
                                            registro.getString("apellidosPaciente"),
                                            registro.getString("sexo"),
                                            registro.getDate("fechaNacimiento"),
                                            registro.getString("direccionPaciente"),
                                            registro.getString("telefonoPersonal"),
                                            registro.getDate("fechaPrimeraVisita"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
               
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
    
    public ObservableList<Cita> getCita(){
        ArrayList<Cita> lista = new ArrayList<Cita>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarCitas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Cita(resultado.getInt("codigoCita"),
                        resultado.getDate("fechaCita"),
                        resultado.getString("horaCita"),
                        resultado.getString("tratamiento"),
                        resultado.getString("desripCondActual"),
                        resultado.getInt("codigoPaciente"),
                        resultado.getInt("numeroColegiado")));
                numero = lista.size();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaCita = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Paciente> getPaciente(){
        ArrayList<Paciente> lista = new ArrayList<Paciente>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPacientes()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Paciente(resultado.getInt("codigoPaciente"),
                        resultado.getString("nombresPaciente"),
                        resultado.getString("apellidosPaciente"),
                        resultado.getString("sexo"),
                        resultado.getDate("fechaNacimiento"),
                        resultado.getString("direccionPaciente"),
                        resultado.getString("telefonoPersonal"),
                        resultado.getDate("fechaPrimeraVisita")
                ));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPaciente = FXCollections.observableArrayList(lista);
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
        Cita registro = new Cita();
        registro.setFechaCita(fCita.getSelectedDate());
        registro.setHoraCita(txtHoraCita.getText());
        registro.setTratamiento(txtTratamiento.getText());
        registro.setDesripCondActual(txtCondicionActual.getText());
        registro.setCodigoPaciente(((Paciente)cmbCodigoPaciente.getSelectionModel().getSelectedItem()).getCodigoPaciente());
        registro.setNumeroColegiado(((Doctor)cmbNumeroColegiado.getSelectionModel().getSelectedItem()).getNumeroColegiado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarCita(?, ?, ?, ?, ?, ?)}");
            procedimiento.setDate(1, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setString(2, registro.getHoraCita());
            procedimiento.setString(3, registro.getTratamiento());
            procedimiento.setString(4, registro.getDesripCondActual());
            procedimiento.setInt(5, registro.getCodigoPaciente());
            procedimiento.setInt(6, registro.getNumeroColegiado());
            procedimiento.execute();
            listaCita.add(registro);
            listaCita.add(registro);
                        numero = listaCita.size();
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
                if(tblCitas.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Cita",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarCita(?)}");
                            procedimiento.setInt(1, ((Cita)tblCitas.getSelectionModel().getSelectedItem()).getCodigoCita());
                            procedimiento.execute();
                            listaCita.remove(tblCitas.getSelectionModel().getSelectedIndex());
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
                 if(tblCitas.getSelectionModel().getSelectedItem() != null){
                     btnEditar.setText("Actualizar");
                     btnReporte.setText("Cancelar");
                     btnNuevo.setDisable(true);
                     btnEliminar.setDisable(true);
                     imgEditar.setImage(new Image("/org/juancaceres/image/Actualizar 96.png"));
                     imgReporte.setImage(new Image("/org/juancaceres/image/Cancelar.png"));
                     activarControles();
                     txtCodigoCita.setEditable(false);
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
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarCita(?, ?, ?, ?, ?)}");
           Cita registro = (Cita)tblCitas.getSelectionModel().getSelectedItem();
           registro.setFechaCita(fCita.getSelectedDate());
           registro.setHoraCita(txtHoraCita.getText());
           registro.setTratamiento(txtTratamiento.getText());
           registro.setDesripCondActual(txtCondicionActual.getText());
           procedimiento.setInt(1, registro.getCodigoCita());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaCita().getTime()));
            procedimiento.setString(3, registro.getHoraCita());
            procedimiento.setString(4, registro.getTratamiento());
            procedimiento.setString(5, registro.getDesripCondActual());
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
    
    public void imprimirReporte() {
        Map parametros = new HashMap();
        parametros.put("codigoCita", null);
        GenerarReporte.mostrarReporte("ReporteCitas.jasper", "Reporte de citas", parametros);
    } 
    
    public void desactivarControles(){
        txtCodigoCita.setEditable(false);
        txtHoraCita.setEditable(false);
        txtTratamiento.setEditable(false);
        txtCondicionActual.setEditable(false);
        cmbCodigoPaciente.setDisable(true);
        cmbNumeroColegiado.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoCita.setEditable(true);
        txtHoraCita.setEditable(true);
        txtTratamiento.setEditable(true);
        txtCondicionActual.setEditable(true);
        cmbCodigoPaciente.setDisable(false);
        cmbNumeroColegiado.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoCita.clear();
        txtHoraCita.clear();
        txtTratamiento.clear();
        txtCondicionActual.clear();
        cmbCodigoPaciente.getSelectionModel().clearSelection();
        cmbNumeroColegiado.getSelectionModel().clearSelection();
        tblCitas.getSelectionModel().clearSelection();
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

