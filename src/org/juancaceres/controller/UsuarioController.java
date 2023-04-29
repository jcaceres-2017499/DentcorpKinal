/*
    Nombre: Juan Pablo Caceres Enriquez
    Codigo Técnico: IN5BM
    Carné: 2017499
    Fecha de creación: 19-07-2022
 */
package org.juancaceres.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;
import org.juancaceres.bean.Usuario;
import org.juancaceres.bean.RolController;
import org.juancaceres.db.Conexion;
import org.juancaceres.system.Principal;

public class UsuarioController implements Initializable {
    private Principal escenarioPrincipal;
    private enum operaciones{NINGUNO, GUARDAR};
    private operaciones tipodeOperacion = operaciones.NINGUNO;
    private ObservableList<RolController> listaRol;
    @FXML private TextField txtCodigoUsuario;
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtApellidoUsuario;
    @FXML private TextField txtUsuario;
    @FXML private TextField txtPassword;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private ImageView imgNuevo;
    @FXML private ImageView imgCancelar;
    @FXML private ComboBox  cmbCodigoRol;	
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnEliminar.setDisable(true);
    }
    
    public void desactivarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(false);
        txtApellidoUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
        btnEliminar.setDisable(true);
    }
    
    public void activarControles(){
        txtCodigoUsuario.setEditable(false);
        txtNombreUsuario.setEditable(true);
        txtApellidoUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
        btnEliminar.setDisable(false);
    }
    
    public void limpiarControles(){
        txtCodigoUsuario.clear();
        txtNombreUsuario.clear();
        txtApellidoUsuario.clear();
        txtUsuario.clear();
        txtPassword.clear();
    }
    
    public void nuevo (){
        switch(tipodeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/josecifuentes/image/guardar.png"));
                tipodeOperacion= operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Cancelar");
                imgNuevo.setImage(new Image("/org/josecifuentes/image/new.png"));
                tipodeOperacion = operaciones.NINGUNO;
                break;            
        }    
    }
    
    public void guardar(){
        Usuario registros = new Usuario();
        registros.setNombreUsuario(txtNombreUsuario.getText());
        registros.setApellidoUsuario(txtApellidoUsuario.getText());
        registros.setUsuarioLogin(txtUsuario.getText());
        registros.setContrasena(txtPassword.getText());
        registros.setCodigoRol(((RolController) cmbCodigoRol.getSelectionModel().getSelectedItem()).getCodigoRol());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarUsuario(?, ?, ?, ?,?)}");
            procedimiento.setString(1, registros.getNombreUsuario());
            procedimiento.setString(2, registros.getApellidoUsuario());
            procedimiento.setString(3, registros.getUsuarioLogin());
            procedimiento.setString(4, registros.getContrasena());
	    procedimiento.setInt(5, registros.getCodigoRol());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "Datos Ingresados exitosamente");
            ventanaLogin();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public RolController buscarRol(int codigoRol){
        RolController resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{Call sp_BuscarRol(?)}");
            procedimiento.setInt(1, codigoRol);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new RolController(registro.getInt("codigoRol"),
                                            registro.getString("tipoRol"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
               
    }
    
    public ObservableList<RolController> getRol(){
        ArrayList<RolController> lista = new ArrayList<RolController>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarRoles()}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new RolController(resultado.getInt("codigoRol"),
                        resultado.getString("tipoRol")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaRol = FXCollections.observableArrayList(lista);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaLogin(){
        escenarioPrincipal.ventanaLogin();
    }
}
