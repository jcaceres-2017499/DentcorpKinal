/*
    Nombre: Juan Pablo Caceres Enriquez
    Codigo Técnico: IN5AV
    Carné: 2017499
    Fecha de creación: 05-04-2022
    Modificaciones: 17/06/2022
 */
package org.juancaceres.system;

import java.io.IOException;
import java.io.InputStream;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.juancaceres.controller.CitaController;
import org.juancaceres.controller.DetalleRecetaController;
import org.juancaceres.controller.DoctorController;
import org.juancaceres.controller.EspecialidadController;
import org.juancaceres.controller.LoginController;
import org.juancaceres.controller.MedicamentoController;
import org.juancaceres.controller.MenuPrincipalController;
import org.juancaceres.controller.PacienteController;
import org.juancaceres.controller.ProgramadorController;
import org.juancaceres.controller.RecetaController;
import org.juancaceres.controller.UsuarioController;

/**
 *
 * @author pjcae
 */
public class Principal extends Application {
    private Stage escenarioPrincipal;
    private Scene escena;
    private final String PAQUETE_VISTA= "/org/juancaceres/view/";
    
    @Override
    public void start(Stage escenarioPrincipal) throws IOException {
        this.escenarioPrincipal = escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Dentcorp Kinal");
        escenarioPrincipal.getIcons().add(new Image("/org/juancaceres/image/Icono.jpg"));
//        Parent root = FXMLLoader.load(getClass().getResource("/org/juancaceres/view/MenuPrincipalView.fxml"));
//        Scene escena = new Scene (root);
//        escenarioPrincipal.setScene(escena);
        ventanaLogin();
        escenarioPrincipal.show();
    }
    public void menuPrincipal(){
        try{
           MenuPrincipalController ventanaMenu = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml",557,392);
           ventanaMenu.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML = new FXMLLoader();
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA+fxml));
        escena = new Scene((Pane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable)cargadorFXML.getController();
        return resultado;
    }
    
    public void ventanaProgramador(){
        try{
           ProgramadorController vistaProgramador = (ProgramadorController)cambiarEscena("ProgramadorView.fxml",600,400);
           vistaProgramador.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void ventanaPaciente(){
        try{
           PacienteController vistaPacientes = (PacienteController)cambiarEscena("PacienteView.fxml",1070,400);
           vistaPacientes.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaMedicamento(){
        try{
            MedicamentoController vistaMedicamento= (MedicamentoController)cambiarEscena("MedicamentoView.fxml",600,400);
            vistaMedicamento.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    public void ventanaEspecialidades(){
        try{
            EspecialidadController vistaEspecialidad= (EspecialidadController)cambiarEscena("EspecialidadView.fxml",600,400);
            vistaEspecialidad.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
            
        }
    }
    public void ventanaDoctores(){
        try{
            DoctorController vistaDoctor= (DoctorController)cambiarEscena("DoctorView.fxml",1070,400);
            vistaDoctor.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaRecetas(){
        try{
            RecetaController vistaReceta= (RecetaController)cambiarEscena("RecetasView.fxml",620,400);
            vistaReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaDetalleRecetas(){
        try{
            DetalleRecetaController vistaDetalleReceta= (DetalleRecetaController)cambiarEscena("DetalleRecetaView.fxml",1000,400);
            vistaDetalleReceta.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaCitas(){
        try{
            CitaController vistaCita= (CitaController)cambiarEscena("CitasView.fxml",1070,400);
            vistaCita.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaLogin(){
        try{
            LoginController login = (LoginController)cambiarEscena("LoginView.fxml",600,400);
            login.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void ventanaUsuario(){
        try{
            UsuarioController usuario= (UsuarioController)cambiarEscena("UsuarioView.fxml",600,400);
            usuario.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}