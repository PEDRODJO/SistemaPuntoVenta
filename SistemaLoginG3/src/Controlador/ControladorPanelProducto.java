/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Producto;
import Vista.PanelProducto;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;

public class ControladorPanelProducto {
    private PanelProducto vista;
    private Producto modelo;

    public ControladorPanelProducto() {
        this.vista = new PanelProducto();
        this.modelo = new Producto();
        manejadorEventos();
        
        // Carga la tabla de productos al iniciar el panel
        listarTodos();
    }
    
    public PanelProducto getVista() {
        return vista;
    }

    private void manejadorEventos() {
        this.vista.btnRegistrar.addActionListener(e -> registrar());
        this.vista.btnEditar.addActionListener(e -> editar());
        this.vista.btnEliminar.addActionListener(e -> eliminar());
        this.vista.btnNuevo.addActionListener(e -> limpiarYRecargar());
        
        this.vista.tablaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila(e);
            }
        });
    }

    private void listarTodos() {
        this.vista.tablaProductos.setModel(modelo.listarProductos());
    }

    private void registrar() {
        // Validación de campos
        if (vista.txtNombre.getText().isEmpty() || vista.txtPrecio.getText().isEmpty() || vista.txtStock.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Los campos Nombre, Precio y Stock son obligatorios.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            modelo.setNombreProducto(vista.txtNombre.getText());
            modelo.setDescripcionProducto(vista.txtDescripcion.getText());
            modelo.setPrecioVenta(Double.parseDouble(vista.txtPrecio.getText()));
            modelo.setStock(Integer.parseInt(vista.txtStock.getText()));
            
            if (modelo.registrarProducto()) {
                JOptionPane.showMessageDialog(vista, "Producto registrado con éxito.", "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                listarTodos();
                limpiarCampos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El precio y el stock deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (vista.txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla para editar.", "Ningún Producto Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            modelo.setIdProducto(Integer.parseInt(vista.txtID.getText()));
            modelo.setNombreProducto(vista.txtNombre.getText());
            modelo.setDescripcionProducto(vista.txtDescripcion.getText());
            modelo.setPrecioVenta(Double.parseDouble(vista.txtPrecio.getText()));
            modelo.setStock(Integer.parseInt(vista.txtStock.getText()));

            if (modelo.editarProducto()) {
                JOptionPane.showMessageDialog(vista, "Producto editado con éxito.", "Edición Exitosa", JOptionPane.INFORMATION_MESSAGE);
                listarTodos();
                limpiarCampos();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "El precio y el stock deben ser números válidos.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminar() {
        if (vista.txtID.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla para eliminar.", "Ningún Producto Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de que desea eliminar este producto?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            modelo.setIdProducto(Integer.parseInt(vista.txtID.getText()));
            if (modelo.eliminarProducto()) {
                JOptionPane.showMessageDialog(vista, "Producto eliminado con éxito.", "Eliminación Exitosa", JOptionPane.INFORMATION_MESSAGE);
                listarTodos();
                limpiarCampos();
            }
        }
    }

    private void seleccionarFila(MouseEvent e) {
        JTable tabla = (JTable) e.getSource();
        int fila = tabla.rowAtPoint(e.getPoint());
        if (fila > -1) {
            int idProducto = (int) tabla.getValueAt(fila, 0);
            // Usamos el método buscar para obtener todos los datos, incluida la descripción
            if (modelo.buscarProductoPorId(idProducto)) {
                vista.txtID.setText(String.valueOf(modelo.getIdProducto()));
                vista.txtNombre.setText(modelo.getNombreProducto());
                vista.txtDescripcion.setText(modelo.getDescripcionProducto());
                vista.txtPrecio.setText(String.valueOf(modelo.getPrecioVenta()));
                vista.txtStock.setText(String.valueOf(modelo.getStock()));
            }
        }
    }

    private void limpiarCampos() {
        vista.txtID.setText("");
        vista.txtNombre.setText("");
        vista.txtDescripcion.setText("");
        vista.txtPrecio.setText("");
        vista.txtStock.setText("");
    }
    
    private void limpiarYRecargar() {
        limpiarCampos();
        listarTodos();
    }
}