/*
    Nombre: Juan Pablo Caceres Enriquez
    Codigo Técnico: IN5BM
    Carné: 2017499
 */
package org.juancaceres.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.juancaceres.bean.Medicamento;
import org.juancaceres.db.Conexion;
import org.juancaceres.report.GenerarReporte;
import org.juancaceres.system.Principal;


public class MedicamentoController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,ELIMINAR,EDITAR,GUARDAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Medicamento>listaMedicamentos;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TableView tblMedicamentos;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML private TableColumn colDescripcion;
    @FXML private TextField txtCodigoMedicamento;
    @FXML private TextField txtDescripcion;
    @FXML Label lblConteo;
    int numero;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       conteoLabel();
    }
    
    private void conteoLabel() {
        String tamaño = " " + numero;
        System.out.println("Numero de datos: " + numero);
        lblConteo.setText(tamaño);
    }
    
    public void cargarDatos(){
        tblMedicamentos.setItems(getMedicamento());
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, Integer>("codigoMedicamento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("descripcion"));
        conteoLabel();
    }
    
    public void seleccionarElemento(){
       if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
        txtCodigoMedicamento.setText(String.valueOf(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
        txtDescripcion.setText(((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getDescripcion());
    }
      else{
          JOptionPane.showMessageDialog(null, "No hay ningun registro en el lugar seleccionado");
        }
    }  
    
    public ObservableList<Medicamento> getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"),
                        resultado.getString("descripcion")));
                numero = lista.size();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicamentos = FXCollections.observableArrayList(lista);
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
                break;
            
        }    
    }
    
     public void guardar(){
        Medicamento registro = new Medicamento();
        registro.setDescripcion(txtDescripcion.getText());
        
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarMedicamento(?)}");
            procedimiento.setString(1, registro.getDescripcion());
            procedimiento.execute();
            listaMedicamentos.add(registro);
            listaMedicamentos.add(registro);
                        numero = listaMedicamentos.size();
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
                if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Medicamento",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarMedicamento(?)}");
                            procedimiento.setInt(1, ((Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
                            procedimiento.execute();
                            listaMedicamentos.remove(tblMedicamentos.getSelectionModel().getSelectedIndex());
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
                 if(tblMedicamentos.getSelectionModel().getSelectedItem() != null){
                     btnEditar.setText("Actualizar");
                     btnReporte.setText("Cancelar");
                     btnNuevo.setDisable(true);
                     btnEliminar.setDisable(true);
                     imgEditar.setImage(new Image("/org/juancaceres/image/Actualizar 96.png"));
                     imgReporte.setImage(new Image("/org/juancaceres/image/Cancelar.png"));
                     activarControles();
                     txtCodigoMedicamento.setEditable(false);
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
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EditarMedicamento(?, ?)}");
           Medicamento registro = (Medicamento)tblMedicamentos.getSelectionModel().getSelectedItem();
           registro.setDescripcion(txtDescripcion.getText());
           procedimiento.setInt(1, registro.getCodigoMedicamento());
            procedimiento.setString(2, registro.getDescripcion());
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
        parametros.put("codigoMedicamento", null);
        GenerarReporte.mostrarReporte("ReporteMedicamentos.jasper", "Reporte de medicamentos", parametros);
    } 
    
    public void desactivarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtDescripcion.setEditable(false);
    }
    
    public void activarControles(){
        txtCodigoMedicamento.setEditable(false);
        txtDescripcion.setEditable(true);
    }
    
    public void limpiarControles(){
        txtCodigoMedicamento.clear();
        txtDescripcion.clear();
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