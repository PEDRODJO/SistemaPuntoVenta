/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author LuisE
 */
public class Login {
    //Atributos (se mantienen los tuyos)
    private int idLogin;
    private String nombreLogin;
    private Date fechaCreacionLogin;
    private boolean estatusLogin;
    private String contraseniaLogin;
    
    private Usuario usuario;
    private RolUsuario rolUsuario;
    
    //Constructor (se mantienen los tuyos)
    public Login() {
        //Crear objetos de usuario y su rol
        this.usuario = new Usuario();
        this.rolUsuario = new RolUsuario();
    }

    public Login(int idLogin, String nombreLogin, String contraseñaLogin) {
        this.idLogin = idLogin;
        this.nombreLogin = nombreLogin;
        this.contraseniaLogin = contraseñaLogin;
        this.usuario = new Usuario();
        this.rolUsuario = new RolUsuario();
    }
    
    //Metodos set y get (se mantienen los tuyos)
    public String getContraseniaLogin() {
        return contraseniaLogin;
    }

    public void setContraseniaLogin(String contraseniaLogin) {
        this.contraseniaLogin = contraseniaLogin;
    }
    
    public int getIdLogin() {
        return idLogin;
    }

    public void setIdLogin(int idLogin) {
        this.idLogin = idLogin;
    }

    public String getNombreLogin() {
        return nombreLogin;
    }

    public void setNombreLogin(String nombreLogin) {
        this.nombreLogin = nombreLogin;
    }

    public Date getFechaCreacionLogin() {
        return fechaCreacionLogin;
    }

    public void setFechaCreacionLogin(Date fechaCreacionLogin) {
        this.fechaCreacionLogin = fechaCreacionLogin;
    }

    public boolean isEstatusLogin() {
        return estatusLogin;
    }

    public void setEstatusLogin(boolean estatusLogin) {
        this.estatusLogin = estatusLogin;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public RolUsuario getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(RolUsuario rolUsuario) {
        this.rolUsuario = rolUsuario;
    }
    
    // --- MÉTODO DE VALIDACIÓN CONECTADO A LA BASE DE DATOS ---
    public boolean validarLogin() {
        ConexionMySQL conector = new ConexionMySQL();
        Connection conexion = conector.conectar();
        PreparedStatement ps = null;
        ResultSet rs = null;

        if (conexion != null) {
            try {
                // La consulta busca un login que coincida y obtiene el nombre real del usuario.
                String sql = "SELECT u.nombreUsuario FROM login l JOIN usuario u ON l.idUsuario = u.idUsuario WHERE l.nombreLogin = ? AND l.contraseniaLogin = ?";
                ps = conexion.prepareStatement(sql);
                ps.setString(1, this.nombreLogin); // Usa el nombre de login que el controlador asignó
                ps.setString(2, this.contraseniaLogin); // Usa la contraseña que el controlador asignó
                rs = ps.executeQuery();

                if (rs.next()) {
                    // Si encontró un resultado, el login es válido.
                    // Guardamos el nombre real del usuario en el objeto para usarlo en la bienvenida.
                    this.getUsuario().setNombreUsuario(rs.getString("nombreUsuario"));
                    return true;
                }
            } catch (SQLException e) {
                System.out.println("Error al validar login: " + e.getMessage());
            } finally {
                // Nos aseguramos de cerrar la conexión
                conector.desconectar();
            }
        }
        // Si no se encontró al usuario o hubo un error, el login falla.
        return false;
    }
    
    //Metodo toString (se mantiene el tuyo)
    @Override
    public String toString() {
        return "Login{" + "idLogin=" + idLogin + ", nombreLogin=" + nombreLogin + ", fechaCreacionLogin=" + fechaCreacionLogin + ", estatusLogin=" + estatusLogin + ", contraseniaLogin=" + contraseniaLogin + ", usuario=" + usuario + ", rolUsuario=" + rolUsuario + '}';
    }
}
