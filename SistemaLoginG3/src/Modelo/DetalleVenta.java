package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Representa la tabla 'detalle_venta' de la base de datos.
 * Guarda la información de un producto específico dentro de una venta.
 */
public class DetalleVenta {
    // Atributos
    private int idDetalleVenta;
    private int idVenta;
    private int idProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    // Constructor
    public DetalleVenta() {}

    // Getters y Setters
    public int getIdDetalleVenta() { return idDetalleVenta; }
    public void setIdDetalleVenta(int idDetalleVenta) { this.idDetalleVenta = idDetalleVenta; }
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    /**
     * Inserta un nuevo detalle (producto) en la base de datos.
     * @return true si el registro fue exitoso, false en caso contrario.
     */
    public boolean registrarDetalle() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        String sql = "INSERT INTO detalle_venta (idVenta, idProducto, cantidad, precioUnitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.setInt(1, this.getIdVenta());
            ps.setInt(2, this.getIdProducto());
            ps.setInt(3, this.getCantidad());
            ps.setDouble(4, this.getPrecioUnitario());
            ps.setDouble(5, this.getSubtotal());
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar el detalle de la venta: " + e.getMessage());
            return false;
        } finally {
            conector.desconectar();
        }
    }
}
