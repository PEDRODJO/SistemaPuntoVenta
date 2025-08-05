/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Login;
import Vista.DashBoardAdmin; // Asegúrate que el nombre de tu ventana principal sea este
import Vista.VistaLogin;
import javax.swing.JOptionPane;

public class ControladorVistaLogin {
    //Atributos
    private VistaLogin vista;
    private Login modelo;
    
    //Constructor
    public ControladorVistaLogin() {
        this.vista = new VistaLogin();
        this.modelo = new Login();
        manejadorEventos();
    }
    
    //Metodo para el manejador de evento
    public void manejadorEventos(){
        // Verifica que el botón en tu vista se llame 'btnIniciar'
        this.vista.btnIniciar.addActionListener(e -> iniciarSesion());
    }
    
    //Metodo para iniciar Sesión
    public void iniciarSesion(){
        // Obtiene los datos de los campos de la vista
        String user = this.vista.TxtUsuario.getText(); // Verifica que el campo se llame 'TxtUsuario'
        String pass = String.valueOf(this.vista.TxtPassword.getPassword()); // Verifica que el campo se llame 'TxtPassword'
        
        // Asigna los datos al modelo para que los pueda usar en la validación
        this.modelo.setNombreLogin(user);
        this.modelo.setContraseniaLogin(pass);
        
        // Llama al método del modelo que ahora se conecta a la BD
        if (this.modelo.validarLogin()) {
            // Si el login fue exitoso, obtiene el nombre que el modelo recuperó de la BD
            String nombreBienvenida = this.modelo.getUsuario().getNombreUsuario();
            JOptionPane.showMessageDialog(this.vista, "¡Bienvenido, " + nombreBienvenida + "!");
            
            // Abre el Dashboard principal
            DashBoardAdmin dashBoard = new DashBoardAdmin();
            dashBoard.setVisible(true);
            dashBoard.setLocationRelativeTo(null);
            
            // Cierra la ventana de login
            this.vista.dispose();
        } else {
            JOptionPane.showMessageDialog(this.vista, "Usuario y/o Contraseña Incorrectos");
        }
    }
    
    //Punto de entrada para iniciar la aplicación
    public static void main(String[] args) {
        ControladorVistaLogin controlador = new ControladorVistaLogin();
        controlador.vista.setVisible(true);
        controlador.vista.setLocationRelativeTo(null);
    }
}