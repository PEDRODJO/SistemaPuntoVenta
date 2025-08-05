/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Usuario;
import Vista.PanelUsuario;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author LuisE
 */
public class ControladorPanelUsuarios {
    //Atributos
    private PanelUsuario vista;
    private Usuario modelo;
    
    //Constructor
    public ControladorPanelUsuarios() {
        this.vista = new PanelUsuario();
        this.modelo = new Usuario();
        manejadorEventos();
        
        // Carga todos los usuarios en la tabla al iniciar el panel
        listarTodos();
    }
    
    //Métodos Getters y Setters
    public PanelUsuario getVista() { return vista; }
    public void setVista(PanelUsuario vista) { this.vista = vista; }
    public Usuario getModelo() { return modelo; }
    public void setModelo(Usuario modelo) { this.modelo = modelo; }
    
    //Método para dar funcionalidad a todos los botones y la tabla
    public void manejadorEventos(){
        this.vista.btnRegistrar.addActionListener(e -> registrar());
        this.vista.btnEditar.addActionListener(e -> editar());
        this.vista.btnEliminar.addActionListener(e -> eliminar());
        this.vista.btnBuscar.addActionListener(e -> buscar());
        this.vista.btnNuevo.addActionListener(e -> limpiarYRecargar());
        
        // Evento para cuando se hace clic en una fila de la tabla
        this.vista.tablaUsuario.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFila(e);
            }
        });
    }

    // Muestra todos los usuarios en la tabla
    private void listarTodos() {
        this.vista.tablaUsuario.setModel(modelo.listarUsuarios());
    }
    
    // Registra un nuevo usuario
    private void registrar() {
        if(vista.TxtNombre.getText().isEmpty() || vista.TxtApellidoPaterno.getText().isEmpty()){
            JOptionPane.showMessageDialog(vista, "Los campos Nombre y Apellido Paterno son obligatorios.", "Campos incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.modelo.setNombreUsuario(this.vista.TxtNombre.getText());
        this.modelo.setApellidoParternoUsuario(this.vista.TxtApellidoPaterno.getText());
        this.modelo.setApellidoMaternoUsuario(this.vista.TxtApellidoMaterno.getText());
        this.modelo.setEmailUsuario(this.vista.TxtEmail.getText());
        this.modelo.setTelefonoCleluarUSuario(this.vista.TxtTelefono.getText());

        if (this.modelo.registrarUsuario()) {
            JOptionPane.showMessageDialog(this.vista, "Usuario registrado con éxito.");
            listarTodos();
            limpiarCampos();
        }
    }
    
    // Edita un usuario existente
    private void editar() {
        if (this.vista.TxtUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this.vista, "Por favor, seleccione un usuario de la tabla para editar.", "Ningún usuario seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        this.modelo.setIdUsuario(Integer.parseInt(this.vista.TxtUsuario.getText()));
        this.modelo.setNombreUsuario(this.vista.TxtNombre.getText());
        this.modelo.setApellidoParternoUsuario(this.vista.TxtApellidoPaterno.getText());
        this.modelo.setApellidoMaternoUsuario(this.vista.TxtApellidoMaterno.getText());
        this.modelo.setEmailUsuario(this.vista.TxtEmail.getText());
        this.modelo.setTelefonoCleluarUSuario(this.vista.TxtTelefono.getText());

        if (this.modelo.editarUsuario()) {
            JOptionPane.showMessageDialog(this.vista, "Usuario editado con éxito.");
            listarTodos();
            limpiarCampos();
        }
    }
    
    // Elimina un usuario seleccionado
    private void eliminar() {
        if (this.vista.TxtUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this.vista, "Por favor, seleccione un usuario de la tabla para eliminar.", "Ningún usuario seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }
            
        int confirmacion = JOptionPane.showConfirmDialog(this.vista, "¿Seguro que quieres eliminar a este usuario?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            
        if (confirmacion == JOptionPane.YES_OPTION) {
            this.modelo.setIdUsuario(Integer.parseInt(this.vista.TxtUsuario.getText()));
            if (this.modelo.eliminarUsuario()) {
                JOptionPane.showMessageDialog(this.vista, "Usuario eliminado con éxito.");
                listarTodos();
                limpiarCampos();
            }
        }
    }

    // Busca un usuario por ID y lo muestra en la tabla
    private void buscar() {
        if (this.vista.TxtUsuario.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this.vista, "Por favor, ingrese un ID para buscar.", "Campo ID vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(this.vista.TxtUsuario.getText());
            this.vista.tablaUsuario.setModel(modelo.listarUnUsuario(id));
            limpiarCampos(); // Limpiamos los campos del formulario después de la búsqueda
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this.vista, "Por favor, ingrese un ID válido (solo números).", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Pasa los datos de la fila seleccionada a los campos del formulario
    private void seleccionarFila(MouseEvent e) {
        JTable tabla = (JTable) e.getSource();
        int fila = tabla.rowAtPoint(e.getPoint());
        if (fila > -1) {
            this.vista.TxtUsuario.setText(String.valueOf(tabla.getValueAt(fila, 0)));
            this.vista.TxtNombre.setText(String.valueOf(tabla.getValueAt(fila, 1)));
            this.vista.TxtApellidoPaterno.setText(String.valueOf(tabla.getValueAt(fila, 2)));
            this.vista.TxtApellidoMaterno.setText(String.valueOf(tabla.getValueAt(fila, 3)));
            this.vista.TxtEmail.setText(String.valueOf(tabla.getValueAt(fila, 4)));
            this.vista.TxtTelefono.setText(String.valueOf(tabla.getValueAt(fila, 5)));
        }
    }
    
    // Limpia los campos de texto
    private void limpiarCampos() {
        this.vista.TxtUsuario.setText("");
        this.vista.TxtNombre.setText("");
        this.vista.TxtApellidoPaterno.setText("");
        this.vista.TxtApellidoMaterno.setText("");
        this.vista.TxtEmail.setText("");
        this.vista.TxtTelefono.setText("");
    }
    
    // Limpia los campos y recarga la tabla completa
    private void limpiarYRecargar() {
        limpiarCampos();
        listarTodos();
    }
}