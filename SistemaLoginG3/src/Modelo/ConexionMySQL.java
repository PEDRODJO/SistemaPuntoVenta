/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo; // Tu clase ya está en este paquete, ¡perfecto!

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author pedro
 */
public class ConexionMySQL {

    private Connection conexion = null;
    
    // El driver ya está en tu proyecto, este es su nombre
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Tu URL va bien, solo la ponemos en su propia variable
    private static final String URL = "jdbc:mysql://localhost:3306/bd_sistema_login";
    
    // Define tu usuario y contraseña de MySQL
    private static final String USER = "root";
    private static final String PASSWORD = "29062006"; // <-- ¡CAMBIA ESTO POR TU CONTRASEÑA!  de my SQL PERSONAL DEL SISTEMA 

    /**
     * Método para conectar con la base de datos.
     * @return La conexión o null si falla.
     */
    public Connection conectar() {
        try {
            // 1. Usar el nombre del driver que ya tienes
            Class.forName(DRIVER);
            
            // 2. Usar los datos para establecer la conexión
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // System.exit(0); // Descomenta si quieres que el programa se cierre si no hay conexión
        }
        return conexion;
    }

    /**
     * Método para cerrar la conexión.
     */
    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cerrar la conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}