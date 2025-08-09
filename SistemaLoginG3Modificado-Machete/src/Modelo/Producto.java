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
/**
 *
 * @author Thinkpad E14
 */
public class Producto {
     // Atributos
    private int idProducto;
    private String nombreProducto;
    private String descripcionProducto;
    private double precioVenta;
    private int stock;

    // Constructor
    public Producto() {}

    // Getters y Setters
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public String getDescripcionProducto() { return descripcionProducto; }
    public void setDescripcionProducto(String descripcionProducto) { this.descripcionProducto = descripcionProducto; }
    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { this.precioVenta = precioVenta; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    // --- MÉTODOS CRUD ---

    public boolean registrarProducto() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "INSERT INTO producto (nombreProducto, descripcionProducto, precioVenta, stock) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, this.getNombreProducto());
            ps.setString(2, this.getDescripcionProducto());
            ps.setDouble(3, this.getPrecioVenta());
            ps.setInt(4, this.getStock());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar producto: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            conector.desconectar();
        }
    }

    public boolean editarProducto() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "UPDATE producto SET nombreProducto=?, descripcionProducto=?, precioVenta=?, stock=? WHERE idProducto=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setString(1, this.getNombreProducto());
            ps.setString(2, this.getDescripcionProducto());
            ps.setDouble(3, this.getPrecioVenta());
            ps.setInt(4, this.getStock());
            ps.setInt(5, this.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al editar producto: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            conector.desconectar();
        }
    }

    public boolean eliminarProducto() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "DELETE FROM producto WHERE idProducto=?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, this.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al eliminar producto: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            conector.desconectar();
        }
    }

    public DefaultTableModel listarProductos() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        DefaultTableModel modeloTabla = new DefaultTableModel();
        
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Precio");
        modeloTabla.addColumn("Stock");

        String sql = "SELECT idProducto, nombreProducto, precioVenta, stock FROM producto";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] fila = {
                    rs.getInt("idProducto"),
                    rs.getString("nombreProducto"),
                    rs.getDouble("precioVenta"),
                    rs.getInt("stock")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al listar productos: " + e.getMessage(), "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        } finally {
            conector.desconectar();
        }
        return modeloTabla;
    }
}

    
    

