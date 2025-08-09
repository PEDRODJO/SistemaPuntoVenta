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
    
    //Metodo para iniciar Sesión machete
    public void iniciarSesion(){
String user = this.vista.TxtUsuario.getText();
    String pass = String.valueOf(this.vista.TxtPassword.getPassword());

    this.modelo.setNombreLogin(user);
    this.modelo.setContraseniaLogin(pass);

    if (this.modelo.validarLogin()) {
        String nombreBienvenida = this.modelo.getUsuario().getNombreUsuario();
        String rol = this.modelo.getRolUsuario().getNombreRolUsuario(); // Obtenemos el rol

        JOptionPane.showMessageDialog(this.vista, "¡Bienvenido, " + nombreBienvenida + "!");

        // ---> LÓGICA DE DECISIÓN <---
        if (rol.equalsIgnoreCase("admin")) {
            // Si es admin, abre el dashboard de administrador
            ControladorDashBoardAdmin dashAdmin = new ControladorDashBoardAdmin();
            dashAdmin.getVista().setVisible(true);
            dashAdmin.getVista().setLocationRelativeTo(null);
        } else { // Asumimos que cualquier otro rol es "cajero"
            // Si es cajero, abre el dashboard de punto de venta
            // Esto dará error hasta que creemos la clase
            ControladorDashBoardPuntoVenta dashCajero = new ControladorDashBoardPuntoVenta();
            dashCajero.getVista().setVisible(true);
            dashCajero.getVista().setLocationRelativeTo(null);
        }

        this.vista.dispose(); // Cierra la ventana de login
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