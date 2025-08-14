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

public class VentaDAO {
    ConexionMySQL cn = new ConexionMySQL();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    public boolean registrarVenta(Venta venta) {
        String sqlVenta = "INSERT INTO venta (totalVenta, ivaVenta, idUsuario, idCliente) VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_venta (idVenta, idProducto, cantidad, precioUnitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlActualizarStock = "UPDATE producto SET stock = stock - ? WHERE idProducto = ?";
        
        try {
            con = cn.getConnection();
            // Iniciar transacción para asegurar que todo se ejecute o nada lo haga
            con.setAutoCommit(false);
            
            // 1. Insertar en la tabla 'venta'
            ps = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            ps.setBigDecimal(1, venta.getTotalVenta());
            ps.setBigDecimal(2, venta.getIvaVenta());
            ps.setInt(3, venta.getIdUsuario());
            ps.setObject(4, venta.getIdCliente() == 0 ? null : venta.getIdCliente());
            ps.executeUpdate();
            
            // 2. Obtener el ID de la venta que acabamos de crear
            rs = ps.getGeneratedKeys();
            int idVentaGenerado = 0;
            if (rs.next()) {
                idVentaGenerado = rs.getInt(1);
            }
            
            // 3. Insertar cada producto en 'detalle_venta' y actualizar su stock
            for (DetalleVenta detalle : venta.getDetalles()) {
                ps = con.prepareStatement(sqlDetalle);
                ps.setInt(1, idVentaGenerado);
                ps.setInt(2, detalle.getIdProducto());
                ps.setInt(3, detalle.getCantidad());
                ps.setBigDecimal(4, detalle.getPrecioUnitario());
                ps.setBigDecimal(5, detalle.getSubtotal());
                ps.executeUpdate();
                
                ps = con.prepareStatement(sqlActualizarStock);
                ps.setInt(1, detalle.getCantidad());
                ps.setInt(2, detalle.getIdProducto());
                ps.executeUpdate();
            }
            
            con.commit(); // Confirmar todos los cambios en la base de datos
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al registrar venta: " + e);
            try {
                con.rollback(); // Si algo falla, deshacer todos los cambios
            } catch (SQLException ex) {
                System.err.println("Error al hacer rollback: " + ex);
            }
            return false;
        } finally {
            try { if (con != null) con.close(); } catch (Exception e) { /* Ignorar */ }
        }
    }
}
