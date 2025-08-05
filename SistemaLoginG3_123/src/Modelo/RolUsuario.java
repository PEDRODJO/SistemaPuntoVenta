/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class RolUsuario {
    //Atributos
    private int idRolUsuario;
    private String nombreRolUsuario;
    private String descripcionRolusuario;

    //Constructor
    public RolUsuario() {}

    //Getters y Setters
    public int getIdRolUsuario() { return idRolUsuario; }
    public void setIdRolUsuario(int idRolUsuario) { this.idRolUsuario = idRolUsuario; }
    public String getNombreRolUsuario() { return nombreRolUsuario; }
    public void setNombreRolUsuario(String nombreRolUsuario) { this.nombreRolUsuario = nombreRolUsuario; }
    public String getDescripcionRolusuario() { return descripcionRolusuario; }
    public void setDescripcionRolusuario(String descripcionRolusuario) { this.descripcionRolusuario = descripcionRolusuario; }

    // --- MÉTODOS PARA INTERACTUAR CON LA BASE DE DATOS ---
    
    public boolean registrarRol() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "INSERT INTO rolusuario (nomRolUsuario, descripcionRolUsuario) VALUES (?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, this.getNombreRolUsuario());
            ps.setString(2, this.getDescripcionRolusuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el rol: " + e.getMessage());
            return false;
        } finally {
            conector.desconectar();
        }
    }

    public boolean editarRol() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "UPDATE rolusuario SET nomRolUsuario=?, descripcionRolUsuario=? WHERE idRolUsuario=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, this.getNombreRolUsuario());
            ps.setString(2, this.getDescripcionRolusuario());
            ps.setInt(3, this.getIdRolUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al editar el rol: " + e.getMessage());
            return false;
        } finally {
            conector.desconectar();
        }
    }

    public boolean eliminarRol() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "DELETE FROM rolusuario WHERE idRolUsuario=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, this.getIdRolUsuario());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar el rol: " + e.getMessage());
            return false;
        } finally {
            conector.desconectar();
        }
    }

    public boolean buscarRolPorId(int id) {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "SELECT * FROM rolusuario WHERE idRolUsuario = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    this.setIdRolUsuario(rs.getInt("idRolUsuario"));
                    this.setNombreRolUsuario(rs.getString("nomRolUsuario"));
                    this.setDescripcionRolusuario(rs.getString("descripcionRolUsuario"));
                    return true;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al buscar el rol: " + e.getMessage());
        } finally {
            conector.desconectar();
        }
        return false;
    }

    public DefaultTableModel listarRoles() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        DefaultTableModel modeloTabla = new DefaultTableModel();
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre Rol");
        modeloTabla.addColumn("Descripción");

        String sql = "SELECT idRolUsuario, nomRolUsuario, descripcionRolUsuario FROM rolusuario";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("idRolUsuario"),
                    rs.getString("nomRolUsuario"),
                    rs.getString("descripcionRolUsuario")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar roles: " + e.getMessage());
        } finally {
            conector.desconectar();
        }
        return modeloTabla;
    }
    
    // Este método devuelve una tabla con un solo registro
    public DefaultTableModel listarUnRol(int id) {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        DefaultTableModel modeloTabla = new DefaultTableModel();
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre Rol");
        modeloTabla.addColumn("Descripción");

        String sql = "SELECT idRolUsuario, nomRolUsuario, descripcionRolUsuario FROM rolusuario WHERE idRolUsuario = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Object[] fila = {
                        rs.getInt("idRolUsuario"),
                        rs.getString("nomRolUsuario"),
                        rs.getString("descripcionRolUsuario")
                    };
                    modeloTabla.addRow(fila);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar el rol: " + e.getMessage());
        } finally {
            conector.desconectar();
        }
        return modeloTabla;
    }
}