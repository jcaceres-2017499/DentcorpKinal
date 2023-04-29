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
import org.juancaceres.bean.DetalleReceta;
import org.juancaceres.bean.Medicamento;
import org.juancaceres.bean.Receta;
import org.juancaceres.db.Conexion;
import org.juancaceres.report.GenerarReporte;
import org.juancaceres.system.Principal;


public class DetalleRecetaController implements Initializable{
    private Principal escenarioPrincipal;
    private enum operaciones{NUEVO,ELIMINAR,EDITAR,GUARDAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<DetalleReceta>listaDetalleReceta;
    private ObservableList<Receta>listaReceta;
    private ObservableList<Medicamento>listaMedicamento;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgEliminar;
    @FXML private ImageView imgEditar;
    @FXML private ImageView imgReporte;
    @FXML private TextField txtCodigoDetalleReceta;
    @FXML private TextField txtDosis;
    @FXML private ComboBox cmbCodigoReceta;
    @FXML private ComboBox cmbCodigoMedicamento;
    @FXML private TableView tblRecetas;
    @FXML private TableColumn colCodigoDetalleReceta;
    @FXML private TableColumn colDosis;
    @FXML private TableColumn colCodigoReceta;
    @FXML private TableColumn colCodigoMedicamento;
    @FXML Label lblConteo;
    int numero;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        cargarDatos();
        cmbCodigoReceta.setItems(getReceta());
        cmbCodigoMedicamento.setItems(getMedicamento());
        conteoLabel();
    }
    
    private void conteoLabel() {
        String tamaño = " " + numero;
        System.out.println("Numero de datos: " + numero);
        lblConteo.setText(tamaño);
    }
    
    public void cargarDatos(){
        tblRecetas.setItems(getDetalleReceta());
        colCodigoDetalleReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoDetalleReceta"));
        colDosis.setCellValueFactory(new PropertyValueFactory<DetalleReceta, String>("dosis"));
        colCodigoReceta.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoReceta"));
        colCodigoMedicamento.setCellValueFactory(new PropertyValueFactory<DetalleReceta, Integer>("codigoMedicamento"));
        conteoLabel();
    }
    
    public void seleccionarElemento(){
      if(tblRecetas.getSelectionModel().getSelectedItem() != null){
        txtCodigoDetalleReceta.setText(String.valueOf(((DetalleReceta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoDetalleReceta()));
        txtDosis.setText(((DetalleReceta)tblRecetas.getSelectionModel().getSelectedItem()).getDosis());
        cmbCodigoReceta.getSelectionModel().select(buscarReceta(((DetalleReceta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta()));
        cmbCodigoMedicamento.getSelectionModel().select(buscarMedicamento(((DetalleReceta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoMedicamento()));
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
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaReceta = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<DetalleReceta> getDetalleReceta(){
        ArrayList<DetalleReceta> lista = new ArrayList<DetalleReceta>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarDetalleRecetas()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new DetalleReceta(resultado.getInt("codigoDetalleReceta"),
                        resultado.getString("dosis"),
                        resultado.getInt("codigoReceta"),
                        resultado.getInt("codigoMedicamento")));
                numero = lista.size();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaDetalleReceta = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<Medicamento> getMedicamento(){
        ArrayList<Medicamento> lista = new ArrayList<Medicamento>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_ListarMedicamentos()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Medicamento(resultado.getInt("codigoMedicamento"),
                        resultado.getString("descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaMedicamento = FXCollections.observableArrayList(lista);
    }
    
    public Receta buscarReceta (int codigoReceta){
        Receta resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarReceta(?)}");
            procedimiento.setInt(1, codigoReceta);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Receta(registro.getInt("codigoReceta"),
                                            registro.getDate("fechaReceta"),
                                            registro.getInt("numeroColegiado"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
               
    }
    
    public Medicamento buscarMedicamento (int codigoMedicamento){
        Medicamento resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarMedicamento(?)}");
            procedimiento.setInt(1, codigoMedicamento);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Medicamento(registro.getInt("codigoMedicamento"),
                                            registro.getString("descripcion"));
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
                limpiarControles();
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
        DetalleReceta registro = new DetalleReceta();
        registro.setDosis(txtDosis.getText());
        registro.setCodigoReceta(((Receta)cmbCodigoReceta.getSelectionModel().getSelectedItem()).getCodigoReceta());
        registro.setCodigoMedicamento(((Medicamento)cmbCodigoMedicamento.getSelectionModel().getSelectedItem()).getCodigoMedicamento());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_AgregarDetalleReceta(?, ?, ?)}");
            procedimiento.setString(1, registro.getDosis());
            procedimiento.setInt(2, registro.getCodigoReceta());
            procedimiento.setInt(3, registro.getCodigoMedicamento());
            procedimiento.execute();
            listaDetalleReceta.add(registro);
            listaDetalleReceta.add(registro);
                        numero = listaDetalleReceta.size();
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
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Detalle Receta",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_NO_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EliminarDetalleReceta(?)}");
                            procedimiento.setInt(1, ((DetalleReceta)tblRecetas.getSelectionModel().getSelectedItem()).getCodigoReceta());
                            procedimiento.execute();
                            listaDetalleReceta.remove(tblRecetas.getSelectionModel().getSelectedIndex());
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
                     txtCodigoDetalleReceta.setEditable(false);
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
           PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_EditarDetalleReceta(?, ?, ?, ?)}");
           DetalleReceta registro = (DetalleReceta)tblRecetas.getSelectionModel().getSelectedItem();
           registro.setDosis(txtDosis.getText());
           procedimiento.setInt(1, registro.getCodigoDetalleReceta());
            procedimiento.setString(2, registro.getDosis());
            procedimiento.setInt(3, registro.getCodigoReceta());
            procedimiento.setInt(4, registro.getCodigoMedicamento());
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
        parametros.put("codigoDetalleReceta", null);
        GenerarReporte.mostrarReporte("ReporteDetalleRecetas.jasper", "Reporte de detalle recetas", parametros);
    } 
    
    public void desactivarControles(){
        txtCodigoDetalleReceta.setEditable(false);
	txtDosis.setEditable(false);
	cmbCodigoReceta.setDisable(true);
	cmbCodigoMedicamento.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoDetalleReceta.setEditable(false);
	txtDosis.setEditable(true);
	cmbCodigoReceta.setDisable(false);
	cmbCodigoMedicamento.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoDetalleReceta.clear();
	txtDosis.clear();
        tblRecetas.getSelectionModel().clearSelection();
	cmbCodigoReceta.getSelectionModel().clearSelection();
	cmbCodigoMedicamento.getSelectionModel().clearSelection();
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
