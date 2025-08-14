package Controlador;

import Vista.DashBoardAdmin;

/**
 *
 * @author LuisE
 */
public class ControladorDashBoardAdmin {
    //Atributos
    private DashBoardAdmin vista;
    
    //Constructor
    public ControladorDashBoardAdmin() {
        this.vista = new DashBoardAdmin();
        manejadorEventos();
        
        // Muestra el panel de usuarios por defecto al iniciar.
        mostrarPanelUsuarios();
    }
    
    public DashBoardAdmin getVista() {
        return vista;
    }

    public void manejadorEventos(){
        // Se añaden las acciones a TODOS los botones del menú
        this.vista.btnUsuarios.addActionListener(e -> mostrarPanelUsuarios());
        this.vista.btnRolUsuarios.addActionListener(e -> mostrarPanelRolUsuarios());
        this.vista.btnProductos.addActionListener(e -> mostrarPanelProductos());
        
        // ---> ACCIÓN PARA EL NUEVO BOTÓN <---
        this.vista.btnPuntoVenta.addActionListener(e -> mostrarPanelPuntoVenta());
    }
    
    public void mostrarPanelRolUsuarios(){
        ControladorPanelRolUsuarios controladorHijo = new ControladorPanelRolUsuarios();
        this.vista.panelContenido.removeAll();
        this.vista.panelContenido.add(controladorHijo.getVista(), java.awt.BorderLayout.CENTER);
        this.vista.panelContenido.revalidate();
        this.vista.panelContenido.repaint();
    }
    
    public void mostrarPanelUsuarios(){
        ControladorPanelUsuarios controladorHijo = new ControladorPanelUsuarios();
        this.vista.panelContenido.removeAll();
        this.vista.panelContenido.add(controladorHijo.getVista(), java.awt.BorderLayout.CENTER);
        this.vista.panelContenido.revalidate();
        this.vista.panelContenido.repaint();
    }
     
    public void mostrarPanelProductos(){
        ControladorPanelProducto controladorHijo = new ControladorPanelProducto();
        this.vista.panelContenido.removeAll();
        this.vista.panelContenido.add(controladorHijo.getVista(), java.awt.BorderLayout.CENTER);
        this.vista.panelContenido.revalidate();
        this.vista.panelContenido.repaint();
    }
    
    // ---> MÉTODO NUEVO PARA MOSTRAR EL PUNTO DE VENTA <---
    public void mostrarPanelPuntoVenta() {
        // Creamos el controlador del punto de venta.
        // Como estamos probando, le pasamos un ID de usuario admin (ej: 1)
        ControladorDashBoardPuntoVenta controladorPOS = new ControladorDashBoardPuntoVenta(1);
        
        // OJO: Como el Punto de Venta es un JFrame (una ventana), no lo añadimos a un panel.
        // Lo que hacemos es ocultar el dashboard de admin y mostrar la ventana de ventas.
        this.vista.setVisible(false); // Oculta la ventana de admin
        controladorPOS.getVista().setVisible(true); // Muestra la ventana de ventas
        
        // Para regresar, tendrías que programar el botón de "salir" en la ventana de ventas
        // para que cierre esa ventana y vuelva a mostrar la de admin.
    }
    
    // ---> MÉTODO MAIN REACTIVADO <---
    // Te permite ejecutar este archivo con "Run File" para probar el dashboard directamente.
    public static void main(String[] args){
        ControladorDashBoardAdmin controlador = new ControladorDashBoardAdmin();
        controlador.vista.setVisible(true);
        controlador.vista.setLocationRelativeTo(null);
    }
}
