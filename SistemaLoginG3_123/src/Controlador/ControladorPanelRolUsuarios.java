/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.RolUsuario;
import Vista.PanelRolUsuarios;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ControladorPanelRolUsuarios {
    private PanelRolUsuarios vista;
    private RolUsuario modelo;
    
    public ControladorPanelRolUsuarios(){
        this.vista = new PanelRolUsuarios();
        this.modelo = new RolUsuario();
        manejadorEventos();
        
        // ---> CAMBIO AQUÍ <---
        // La tabla ahora se carga con todos los datos al iniciar.
        listarTodos();
    }
    
    public PanelRolUsuarios getVista() { return vista; }
    
    public void manejadorEventos(){
        this.vista.btnRolRegistrar.addActionListener(e -> registrar());
        this.vista.btnRolEditar.addActionListener(e -> editar());
        this.vista.btnRolEliminar.addActionListener(e -> eliminar());
        this.vista.btnRolNuevo.addActionListener(e -> limpiarYRecargar());
        this.vista.btnRolBuscar.addActionListener(e -> buscar());
        this.vista.btnRolSalir.addActionListener(e -> salir());
        
        this.vista.jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila(e);
            }
        });
    }

    private void listarTodos() {
        this.vista.jTable1.setModel(modelo.listarRoles());
    }
    
    // ---> MÉTODO BUSCAR MODIFICADO <---
    private void buscar() {
        if (vista.txtRolUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese un ID de rol para buscar.", "Campo ID vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            int id = Integer.parseInt(vista.txtRolUsuario.getText());
            // Llama al método que devuelve una tabla con un solo rol
            DefaultTableModel tablaModelo = modelo.listarUnRol(id);
            
            if (tablaModelo.getRowCount() > 0) {
                // Si se encontró, actualiza la tabla solo con ese registro
                this.vista.jTable1.setModel(tablaModelo);
                limpiarCampos(); // Limpia el formulario
            } else {
                JOptionPane.showMessageDialog(vista, "Rol no encontrado.", "Búsqueda fallida", JOptionPane.INFORMATION_MESSAGE);
                // No se limpia la tabla principal si la búsqueda falla
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(vista, "Por favor, ingrese un ID válido (solo números).", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---> MÉTODO SALIR MODIFICADO <---
    private void salir() {
        // Oculta el panel actual
        this.vista.setVisible(false);
        
        // Elimina el panel del contenedor principal para que el dashboard quede vacío
        if (this.vista.getParent() != null) {
            this.vista.getParent().removeAll();
            this.vista.getParent().revalidate();
            this.vista.getParent().repaint();
        }
    }

    private void registrar() {
        if (vista.TxtRolNombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El campo 'Nombre' es obligatorio.", "Campo vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modelo.setNombreRolUsuario(vista.TxtRolNombre.getText());
        modelo.setDescripcionRolusuario(vista.TxtRolObservaciones.getText());
        
        if (modelo.registrarRol()) {
            JOptionPane.showMessageDialog(vista, "Rol registrado con éxito.");
            listarTodos();
            limpiarCampos();
        }
    }
    
    private void editar() {
        if (vista.txtRolUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un rol de la tabla para editar.", "Ningún rol seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        modelo.setIdRolUsuario(Integer.parseInt(vista.txtRolUsuario.getText()));
        modelo.setNombreRolUsuario(vista.TxtRolNombre.getText());
        modelo.setDescripcionRolusuario(vista.TxtRolObservaciones.getText());
        
        if (modelo.editarRol()) {
            JOptionPane.showMessageDialog(vista, "Rol editado con éxito.");
            listarTodos();
            limpiarCampos();
        }
    }
    
    private void eliminar() {
        if (vista.txtRolUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Seleccione un rol de la tabla para eliminar.", "Ningún rol seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Seguro que quieres eliminar este rol?", "Confirmar", JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            modelo.setIdRolUsuario(Integer.parseInt(vista.txtRolUsuario.getText()));
            if (modelo.eliminarRol()) {
                JOptionPane.showMessageDialog(vista, "Rol eliminado con éxito.");
                listarTodos();
                limpiarCampos();
            }
        }
    }
    
    private void seleccionarFila(MouseEvent e) {
        JTable tabla = (JTable) e.getSource();
        int fila = tabla.rowAtPoint(e.getPoint());
        if (fila > -1) {
            vista.txtRolUsuario.setText(String.valueOf(tabla.getValueAt(fila, 0)));
            vista.TxtRolNombre.setText(String.valueOf(tabla.getValueAt(fila, 1)));
            vista.TxtRolObservaciones.setText(String.valueOf(tabla.getValueAt(fila, 2)));
        }
    }

    private void limpiarCampos() {
        vista.txtRolUsuario.setText("");
        vista.TxtRolNombre.setText("");
        vista.TxtRolObservaciones.setText("");
    }

    private void limpiarYRecargar() {
        limpiarCampos();
        listarTodos();
    }
}
