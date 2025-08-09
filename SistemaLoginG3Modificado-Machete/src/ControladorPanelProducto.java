/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Modelo.Producto;
import Vista.PanelProducto;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Thinkpad E14
 */
public class ControladorPanelProducto {
    
    private PanelProducto vista;
    private Producto modelo;

    public ControladorPanelProducto() {
        this.vista = new PanelProducto();
        this.modelo = new Producto();
        manejadorEventos();
        
        // Carga la tabla de productos al iniciar
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
        if (vista.txtNombreProducto.getText().isEmpty() || vista.txtPrecioVenta.getText().isEmpty() || vista.txtStock.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Los campos Nombre, Precio y Stock son obligatorios.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            modelo.setNombreProducto(vista.txtNombreProducto.getText());
            modelo.setDescripcionProducto(vista.txtDescripcionProducto.getText());
            modelo.setPrecioVenta(Double.parseDouble(vista.txtPrecioVenta.getText()));
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
        if (vista.txtIdProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla para editar.", "Ningún Producto Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            modelo.setIdProducto(Integer.parseInt(vista.txtIdProducto.getText()));
            modelo.setNombreProducto(vista.txtNombreProducto.getText());
            modelo.setDescripcionProducto(vista.txtDescripcionProducto.getText());
            modelo.setPrecioVenta(Double.parseDouble(vista.txtPrecioVenta.getText()));
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
        if (vista.txtIdProducto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un producto de la tabla para eliminar.", "Ningún Producto Seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de que desea eliminar este producto?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            modelo.setIdProducto(Integer.parseInt(vista.txtIdProducto.getText()));
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
            // Para obtener todos los datos, necesitamos hacer una consulta extra,
            // ya que la tabla solo muestra algunos.
            int idProducto = (int) tabla.getValueAt(fila, 0);
            // Aquí iría una llamada a un método `buscarProductoPorId` en el modelo
            // para obtener la descripción y luego llenar los campos.
            // Por simplicidad, por ahora solo llenamos los datos visibles.
            vista.txtIdProducto.setText(String.valueOf(tabla.getValueAt(fila, 0)));
            vista.txtNombreProducto.setText(String.valueOf(tabla.getValueAt(fila, 1)));
            vista.txtPrecioVenta.setText(String.valueOf(tabla.getValueAt(fila, 2)));
            vista.txtStock.setText(String.valueOf(tabla.getValueAt(fila, 3)));
            // La descripción quedaría vacía, se necesita un método `buscarPorId` para llenarla.
            vista.txtDescripcionProducto.setText("");
        }
    }

    private void limpiarCampos() {
        vista.txtIdProducto.setText("");
        vista.txtNombreProducto.setText("");
        vista.txtDescripcionProducto.setText("");
        vista.txtPrecioVenta.setText("");
        vista.txtStock.setText("");
    }
    
    private void limpiarYRecargar() {
        limpiarCampos();
        listarTodos();
    }
    
}
