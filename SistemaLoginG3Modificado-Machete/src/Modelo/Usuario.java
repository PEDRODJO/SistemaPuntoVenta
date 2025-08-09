/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Este método va DENTRO de tu clase Modelo/Usuario.java

package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author LuisE
 */
public class Usuario {
    //Atributos
    private int idUsuario;
    private String nombreUsuario;
    private String apellidoParternoUsuario;
    private String apellidoMaternoUsuario;
    private String emailUsuario;
    private String telefonoCleluarUSuario;
    
    //Constructores
    public Usuario() {
    }

    public Usuario(int idUsuario, String nombreUsuario, String apellidoParternoUsuario, String apellidoMaternoUsuario, String emailUsuario, String telefonoCleluarUSuario) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoParternoUsuario = apellidoParternoUsuario;
        this.apellidoMaternoUsuario = apellidoMaternoUsuario;
        this.emailUsuario = emailUsuario;
        this.telefonoCleluarUSuario = telefonoCleluarUSuario;
    }
    
    //Métodos Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getApellidoParternoUsuario() { return apellidoParternoUsuario; }
    public void setApellidoParternoUsuario(String apellidoParternoUsuario) { this.apellidoParternoUsuario = apellidoParternoUsuario; }
    public String getApellidoMaternoUsuario() { return apellidoMaternoUsuario; }
    public void setApellidoMaternoUsuario(String apellidoMaternoUsuario) { this.apellidoMaternoUsuario = apellidoMaternoUsuario; }
    public String getEmailUsuario() { return emailUsuario; }
    public void setEmailUsuario(String emailUsuario) { this.emailUsuario = emailUsuario; }
    public String getTelefonoCleluarUSuario() { return telefonoCleluarUSuario; }
    public void setTelefonoCleluarUSuario(String telefonoCleluarUSuario) { this.telefonoCleluarUSuario = telefonoCleluarUSuario; }
    
    // --- MÉTODOS PARA INTERACTUAR CON LA BASE DE DATOS ---

    public boolean registrarUsuario() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "INSERT INTO usuario (nombreUsuario, apPaternoUsuario, apMaternoUsuario, emailUsuario, telefonoUsuario) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, this.getNombreUsuario());
            ps.setString(2, this.getApellidoParternoUsuario());
            ps.setString(3, this.getApellidoMaternoUsuario());
            ps.setString(4, this.getEmailUsuario());
            ps.setString(5, this.getTelefonoCleluarUSuario());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: " + e.getMessage());
            return false;
        } finally {
            conector.desconectar();
        }
    }

    public boolean editarUsuario() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "UPDATE usuario SET nombreUsuario=?, apPaternoUsuario=?, apMaternoUsuario=?, emailUsuario=?, telefonoUsuario=? WHERE idUsuario=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, this.getNombreUsuario());
            ps.setString(2, this.getApellidoParternoUsuario());
            ps.setString(3, this.getApellidoMaternoUsuario());
            ps.setString(4, this.getEmailUsuario());
            ps.setString(5, this.getTelefonoCleluarUSuario());
            ps.setInt(6, this.getIdUsuario());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al editar usuario: " + e.getMessage());
            return false;
        } finally {
            conector.desconectar();
        }
    }

    public boolean eliminarUsuario() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "DELETE FROM usuario WHERE idUsuario=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, this.getIdUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar usuario: " + e.getMessage());
            return false;
        } finally {
            conector.desconectar();
        }
    }
    
    public boolean buscarUsuarioPorId(int id) {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    this.setIdUsuario(rs.getInt("idUsuario"));
                    this.setNombreUsuario(rs.getString("nombreUsuario"));
                    this.setApellidoParternoUsuario(rs.getString("apPaternoUsuario"));
                    this.setApellidoMaternoUsuario(rs.getString("apMaternoUsuario"));
                    this.setEmailUsuario(rs.getString("emailUsuario"));
                    this.setTelefonoCleluarUSuario(rs.getString("telefonoUsuario"));
                    return true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar usuario: " + e.getMessage());
        } finally {
            conector.desconectar();
        }
        return false;
    }

    public DefaultTableModel listarUsuarios() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        DefaultTableModel modeloTabla = new DefaultTableModel();
        
        String[] columnas = {"ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Email", "Teléfono"};
        modeloTabla.setColumnIdentifiers(columnas);

        String sql = "SELECT idUsuario, nombreUsuario, apPaternoUsuario, apMaternoUsuario, emailUsuario, telefonoUsuario FROM usuario";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("idUsuario"),
                    rs.getString("nombreUsuario"),
                    rs.getString("apPaternoUsuario"),
                    rs.getString("apMaternoUsuario"),
                    rs.getString("emailUsuario"),
                    rs.getString("telefonoUsuario")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar usuarios: " + e.getMessage());
        } finally {
            conector.desconectar();
        }
        return modeloTabla;
    }
    
    public DefaultTableModel listarUnUsuario(int id) {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        DefaultTableModel modeloTabla = new DefaultTableModel();
        
        String[] columnas = {"ID", "Nombre", "Ap. Paterno", "Ap. Materno", "Email", "Teléfono"};
        modeloTabla.setColumnIdentifiers(columnas);

        String sql = "SELECT idUsuario, nombreUsuario, apPaternoUsuario, apMaternoUsuario, emailUsuario, telefonoUsuario FROM usuario WHERE idUsuario = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object[] fila = {
                        rs.getInt("idUsuario"),
                        rs.getString("nombreUsuario"),
                        rs.getString("apPaternoUsuario"),
                        rs.getString("apMaternoUsuario"),
                        rs.getString("emailUsuario"),
                        rs.getString("telefonoUsuario")
                    };
                    modeloTabla.addRow(fila);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar usuario: " + e.getMessage());
        } finally {
            conector.desconectar();
        }
        return modeloTabla;
    }
}