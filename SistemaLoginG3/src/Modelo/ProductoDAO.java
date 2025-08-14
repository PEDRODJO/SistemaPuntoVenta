/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductoDAO {
    ConexionMySQL cn = new ConexionMySQL();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public Producto buscarPorId(int id) {
        Producto producto = null;
        String sql = "SELECT * FROM producto WHERE idProducto = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                producto = new Producto();
                producto.setIdProducto(rs.getInt("idProducto"));
                producto.setNombreProducto(rs.getString("nombreProducto"));
                producto.setPrecioVenta(rs.getBigDecimal("precioVenta"));
                producto.setStock(rs.getInt("stock"));
            }
        } catch (Exception e) {
            System.err.println("Error al buscar producto: " + e);
        } finally {
            // Es buena práctica cerrar la conexión
            try { if (con != null) con.close(); } catch (Exception e) { /* Ignorar */ }
        }
        return producto;
    }
}