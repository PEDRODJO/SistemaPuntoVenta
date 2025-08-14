/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.*;
import Vista.DashboardPuntoVenta;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControladorPuntoVenta implements ActionListener {

    // Atributos: El controlador conoce a la vista y a los modelos de datos (DAO)
    private final DashboardPuntoVenta vista;
    private final ProductoDAO productoDAO;
    private final VentaDAO ventaDAO;
    private final int idUsuarioActual; // El ID del cajero que inició sesión

    // Constructor: Aquí es donde se conectan todas las piezas
    public ControladorPuntoVenta(DashboardPuntoVenta vista, ProductoDAO pDAO, VentaDAO vDAO, int idUsuario) {
        this.vista = vista;
        this.productoDAO = pDAO;
        this.ventaDAO = vDAO;
        this.idUsuarioActual = idUsuario;

        // Se le dice a la vista que este controlador escuchará sus botones
        this.vista.getBtnAgregarProducto().addActionListener(this);
        this.vista.getTxtIdProducto().addActionListener(this); // Para que funcione con "Enter"
        this.vista.getBtnGenerarVenta().addActionListener(this);
        this.vista.getBtnNuevaVenta().addActionListener(this);
    }

    // Inicia la vista y la prepara
    public void iniciar() {
        vista.setTitle("Punto de Venta - Cajero ID: " + idUsuarioActual);
        vista.setLocationRelativeTo(null);
        configurarTabla(); // Prepara la tabla con sus columnas
        vista.setVisible(true);
    }
    
    // Método para ponerle los títulos a las columnas de la tabla
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("Producto");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Precio Unit.");
        modelo.addColumn("Subtotal");
        vista.getTblVenta().setModel(modelo);
    }

    // Este método se dispara cada vez que se presiona un botón
    @Override
    public void actionPerformed(ActionEvent e) {
        // Identifica qué botón se presionó y llama al método correspondiente
        if (e.getSource() == vista.getBtnAgregarProducto() || e.getSource() == vista.getTxtIdProducto()) {
            agregarProducto();
        } else if (e.getSource() == vista.getBtnGenerarVenta()) {
            generarVenta();
        } else if (e.getSource() == vista.getBtnNuevaVenta()) {
            vista.limpiarVista(); // Llama al método que creamos en la vista
        }
    }

    // Lógica para añadir un producto a la tabla
    private void agregarProducto() {
        try {
            int idProducto = Integer.parseInt(vista.getTxtIdProducto().getText());
            Producto p = productoDAO.buscarPorId(idProducto);

            if (p != null) { // Si el producto existe
                if (p.getStock() > 0) { // Y si hay stock
                    DefaultTableModel modelo = (DefaultTableModel) vista.getTblVenta().getModel();
                    Object[] fila = new Object[5];
                    fila[0] = p.getIdProducto();
                    fila[1] = p.getNombreProducto();
                    fila[2] = 1;
                    fila[3] = p.getPrecioVenta();
                    fila[4] = p.getPrecioVenta();
                    modelo.addRow(fila);
                    actualizarTotal();
                } else {
                    JOptionPane.showMessageDialog(vista, "Producto sin stock.");
                }
            } else {
                JOptionPane.showMessageDialog(vista, "Producto no encontrado.");
            }
            // Limpia el campo de texto para el siguiente producto
            vista.getTxtIdProducto().setText("");
            vista.getTxtIdProducto().requestFocus(); // Pone el cursor de nuevo en el campo
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese un ID de producto válido.");
        }
    }

    // Recalcula la suma total de la venta cada vez que se añade un producto
    private void actualizarTotal() {
        DefaultTableModel modelo = (DefaultTableModel) vista.getTblVenta().getModel();
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            BigDecimal subtotal = (BigDecimal) modelo.getValueAt(i, 4);
            total = total.add(subtotal);
        }
        vista.getLblTotal().setText(String.format("Total: %.2f", total));
    }

    // Lógica para finalizar la venta y guardarla en la BD
    private void generarVenta() {
        DefaultTableModel modeloTabla = (DefaultTableModel) vista.getTblVenta().getModel();
        if (modeloTabla.getRowCount() == 0) {
            JOptionPane.showMessageDialog(vista, "No hay productos en la venta.");
            return;
        }

        // 1. Crear el objeto Venta con los datos del "recibo"
        Venta v = new Venta();
        v.setIdUsuario(idUsuarioActual); // Asignamos el cajero
        v.setTotalVenta(new BigDecimal(vista.getLblTotal().getText().replace("Total: ", "")));
        v.setIvaVenta(BigDecimal.ZERO); // Puedes implementar el cálculo del IVA aquí

        // 2. Crear la lista de detalles de la venta
        List<DetalleVenta> detalles = new ArrayList<>();
        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            DetalleVenta d = new DetalleVenta();
            d.setIdProducto(Integer.parseInt(modeloTabla.getValueAt(i, 0).toString()));
            d.setCantidad(Integer.parseInt(modeloTabla.getValueAt(i, 2).toString()));
            d.setPrecioUnitario(new BigDecimal(modeloTabla.getValueAt(i, 3).toString()));
            d.setSubtotal(new BigDecimal(modeloTabla.getValueAt(i, 4).toString()));
            detalles.add(d);
        }
        v.setDetalles(detalles);

        // 3. Mandar el objeto Venta al DAO para que lo guarde
        if (ventaDAO.registrarVenta(v)) {
            JOptionPane.showMessageDialog(vista, "Venta generada con éxito.");
            vista.limpiarVista();
        } else {
            JOptionPane.showMessageDialog(vista, "Error al generar la venta.");
        }
    }
}