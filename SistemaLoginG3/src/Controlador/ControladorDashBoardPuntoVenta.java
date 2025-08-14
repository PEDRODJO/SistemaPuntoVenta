package Controlador;

import Modelo.Producto;
import Modelo.Venta;
import Modelo.DetalleVenta;
import Vista.DashBoardPuntoVenta;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author TuNombre
 */
public class ControladorDashBoardPuntoVenta {
    private DashBoardPuntoVenta vista;
    private Producto productoModelo; // Para buscar productos
    private int idUsuarioActivo; // Para saber qué cajero realiza la venta

    // El constructor ahora recibe el ID del usuario que inició sesión
    public ControladorDashBoardPuntoVenta(int idUsuario) {
        this.vista = new DashBoardPuntoVenta();
        this.productoModelo = new Producto();
        this.idUsuarioActivo = idUsuario;
        manejadorEventos();
    }

    public DashBoardPuntoVenta getVista() {
        return vista;
    }

    private void manejadorEventos() {
        this.vista.btnAgregarProducto.addActionListener(e -> agregarProducto());
        this.vista.btnFinalizarVenta.addActionListener(e -> finalizarVenta());
        this.vista.btnCancelarVenta.addActionListener(e -> cancelarVenta());
    }

    private void agregarProducto() {
        // Validación de entrada
        if (vista.txtIdProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese el ID de un producto.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int idProducto = Integer.parseInt(vista.txtIdProducto.getText());
            int cantidad = (int) vista.txtCantidad.getValue();

            // Buscamos el producto en la base de datos
            if (productoModelo.buscarProductoPorId(idProducto)) {
                // Verificamos si hay stock suficiente
                if (productoModelo.getStock() >= cantidad) {
                    DefaultTableModel tablaModelo = (DefaultTableModel) vista.tablaVenta.getModel();
                    double precio = productoModelo.getPrecioVenta();
                    double subtotal = cantidad * precio;
                    
                    // Creamos la fila para agregar a la tabla
                    Object[] fila = {
                        productoModelo.getIdProducto(),
                        productoModelo.getNombreProducto(),
                        cantidad,
                        precio,
                        subtotal
                    };
                    tablaModelo.addRow(fila);
                    actualizarTotales();
                    limpiarCamposProducto();
                } else {
                    JOptionPane.showMessageDialog(vista, "No hay stock suficiente. Stock actual: " + productoModelo.getStock(), "Stock Insuficiente", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Producto no encontrado.", "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El ID del producto debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizarVenta() {
        if (vista.tablaVenta.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "No hay productos en la venta para finalizar.", "Venta Vacía", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. Crear y registrar la cabecera de la Venta
        Venta venta = new Venta();
        venta.setIdUsuario(this.idUsuarioActivo);
        // Obtenemos los totales desde los labels, quitando el símbolo "$" y las comas
        venta.setTotalVenta(Double.parseDouble(vista.lblTotal.getText().replace("$", "").replace(",", "")));
        venta.setIvaVenta(Double.parseDouble(vista.lblIva.getText().replace("$", "").replace(",", "")));
        // Aquí podrías agregar un campo para buscar y asignar un cliente. Por ahora, es genérico.
        venta.setIdCliente(0); 

        int idVentaGenerada = venta.registrarVenta();

        if (idVentaGenerada != -1) {
            // 2. Registrar cada producto de la tabla como un Detalle de Venta
            DefaultTableModel tablaModelo = (DefaultTableModel) vista.tablaVenta.getModel();
            for (int i = 0; i < tablaModelo.getRowCount(); i++) {
                DetalleVenta detalle = new DetalleVenta();
                detalle.setIdVenta(idVentaGenerada);
                detalle.setIdProducto((int) tablaModelo.getValueAt(i, 0));
                detalle.setCantidad((int) tablaModelo.getValueAt(i, 2));
                detalle.setPrecioUnitario((double) tablaModelo.getValueAt(i, 3));
                detalle.setSubtotal((double) tablaModelo.getValueAt(i, 4));
                detalle.registrarDetalle();
                // Tarea pendiente: Aquí también deberías actualizar el stock del producto en la BD
            }
            
            // 3. Generar el Ticket
            generarTicket(idVentaGenerada);
            
            // 4. Limpiar la interfaz para la siguiente venta
            cancelarVenta(); 
        } else {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error y no se pudo registrar la venta.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelarVenta() {
        DefaultTableModel modelo = (DefaultTableModel) vista.tablaVenta.getModel();
        modelo.setRowCount(0); // Borra todas las filas
        actualizarTotales();
        limpiarCamposProducto();
    }
    
    private void actualizarTotales() {
        DefaultTableModel tablaModelo = (DefaultTableModel) vista.tablaVenta.getModel();
        double subtotalGeneral = 0.0;
        for (int i = 0; i < tablaModelo.getRowCount(); i++) {
            subtotalGeneral += (double) tablaModelo.getValueAt(i, 4);
        }
        
        double iva = subtotalGeneral * 0.16;
        double total = subtotalGeneral + iva;
        
        DecimalFormat df = new DecimalFormat("#,##0.00"); // Formato para dos decimales con separador de miles
        vista.lblSubtotal.setText("$" + df.format(subtotalGeneral));
        vista.lblIva.setText("$" + df.format(iva));
        vista.lblTotal.setText("$" + df.format(total));
    }
    
    private void limpiarCamposProducto() {
        vista.txtIdProducto.setText("");
        vista.txtCantidad.setValue(1);
        vista.txtIdProducto.requestFocusInWindow(); // Pone el cursor en el campo ID
    }
    
    private void generarTicket(int idVenta) {
        StringBuilder ticketTexto = new StringBuilder();
        ticketTexto.append("========================================\n");
        ticketTexto.append("           TICKET DE VENTA\n");
        ticketTexto.append("========================================\n");
        ticketTexto.append("Folio de Venta: ").append(idVenta).append("\n");
        ticketTexto.append("Fecha: ").append(new java.util.Date()).append("\n");
        ticketTexto.append("Atendido por: Cajero ID ").append(this.idUsuarioActivo).append("\n");
        ticketTexto.append("----------------------------------------\n");
        ticketTexto.append(String.format("%-16s %-5s %-8s %-9s\n", "Producto", "Cant.", "Precio", "Subtotal"));
        ticketTexto.append("----------------------------------------\n");

        DefaultTableModel tablaModelo = (DefaultTableModel) vista.tablaVenta.getModel();
        for (int i = 0; i < tablaModelo.getRowCount(); i++) {
            String nombre = (String) tablaModelo.getValueAt(i, 1);
            int cant = (int) tablaModelo.getValueAt(i, 2);
            double precio = (double) tablaModelo.getValueAt(i, 3);
            double sub = (double) tablaModelo.getValueAt(i, 4);
            // Acortamos el nombre si es muy largo para que quepa
            String nombreCorto = nombre.length() > 15 ? nombre.substring(0, 15) + "." : nombre;
            ticketTexto.append(String.format("%-16s %-5d $%7.2f $%8.2f\n", nombreCorto, cant, precio, sub));
        }
        
        ticketTexto.append("----------------------------------------\n");
        ticketTexto.append(String.format("%28s %s\n", "Subtotal:", vista.lblSubtotal.getText()));
        ticketTexto.append(String.format("%28s %s\n", "IVA (16%):", vista.lblIva.getText()));
        ticketTexto.append(String.format("%28s %s\n", "TOTAL:", vista.lblTotal.getText()));
        ticketTexto.append("\n\n          ¡GRACIAS POR SU COMPRA!\n");
        ticketTexto.append("========================================\n");

        // Usamos un JTextArea para que el texto se vea bien formateado
        JTextArea areaTicket = new JTextArea(ticketTexto.toString());
        areaTicket.setEditable(false);
        areaTicket.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        
        JOptionPane.showMessageDialog(vista, new javax.swing.JScrollPane(areaTicket), "Ticket de Venta", JOptionPane.INFORMATION_MESSAGE);
    }
}