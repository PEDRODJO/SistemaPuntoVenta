package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 * Representa la tabla 'venta' de la base de datos.
 * Guarda la información general de una transacción.
 */
public class Venta {
    // Atributos que coinciden con las columnas de la tabla 'venta'
    private int idVenta;
    private Date fechaVenta;
    private double totalVenta;
    private double ivaVenta;
    private int idUsuario;
    private int idCliente;

    // Constructor
    public Venta() {}

    // Getters y Setters
    public int getIdVenta() { return idVenta; }
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public Date getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(Date fechaVenta) { this.fechaVenta = fechaVenta; }
    public double getTotalVenta() { return totalVenta; }
    public void setTotalVenta(double totalVenta) { this.totalVenta = totalVenta; }
    public double getIvaVenta() { return ivaVenta; }
    public void setIvaVenta(double ivaVenta) { this.ivaVenta = ivaVenta; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    /**
     * Inserta una nueva venta en la base de datos y devuelve el ID generado.
     * Es crucial para poder enlazar los detalles de la venta.
     * @return El ID de la venta recién creada, o -1 si hubo un error.
     */
    public int registrarVenta() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        // Usamos Statement.RETURN_GENERATED_KEYS para poder recuperar el ID
        String sql = "INSERT INTO venta (totalVenta, ivaVenta, idUsuario, idCliente) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setDouble(1, this.getTotalVenta());
            ps.setDouble(2, this.getIvaVenta());
            ps.setInt(3, this.getIdUsuario());
            
            // El cliente puede ser opcional (nulo)
            if (this.getIdCliente() > 0) {
                ps.setInt(4, this.getIdCliente());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }

            if (ps.executeUpdate() > 0) {
                // Recuperamos el ID que se autogeneró
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Devuelve el ID de la venta
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar la venta: " + e.getMessage());
        } finally {
            conector.desconectar();
        }
        return -1; // Retorna -1 si algo salió mal
    }
}

