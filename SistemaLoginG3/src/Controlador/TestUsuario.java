/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Usuario;

/**
 *
 * @author LuisE
 */
public class TestUsuario {
    public static void main(String[] args) {
        //Crear objeto usuario
        Usuario usuario=new Usuario(1, "pedro", "juarez", "olguin", "pedro123@gmail.com", "5663557970");
        System.out.println(usuario);
        
        //Crear objeto usuario 2
    
        Usuario usuario2=new Usuario();
        usuario2.setIdUsuario(2);
        usuario2.setNombreUsuario("Dani");
        usuario2.setApellidoParternoUsuario("Ceron");
        usuario2.setApellidoMaternoUsuario("Cruz");
        usuario2.setEmailUsuario("Daniel.pendejo@gmail.com");
        usuario2.setTelefonoCleluarUSuario("5623158694");
    
        System.out.println("");
    
        System.out.println("usuario2");
        
        //Crear objeto usuario3
        Usuario usuario3=new Usuario();
        usuario3.setNombreUsuario("Brandon");
        usuario3.setApellidoParternoUsuario("Godinez");
        usuario3.setApellidoMaternoUsuario("Levario");
        usuario3.setEmailUsuario("Brandon.wey10@gmail.com");
        usuario3.setTelefonoCleluarUSuario("5560405075");
        
        System.out.println("");
        System.out.println("ID \t Nombre \t Apellido Paterno \t Apellido Materno \t Email \t Telefono \n");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------\n");
        System.out.println(usuario.getIdUsuario() + "\t" + usuario.getNombreUsuario() + "\t" + usuario.getApellidoParternoUsuario() + "\t" + usuario.getApellidoMaternoUsuario() + "\t" + usuario.getEmailUsuario() + "\t" + usuario.getTelefonoCleluarUSuario() + "\n");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------\n");
        System.out.println(usuario2.getIdUsuario() + "\t" + usuario2.getNombreUsuario() + "\t" + usuario2.getApellidoParternoUsuario() + "\t" + usuario2.getApellidoMaternoUsuario() + "\t" + usuario2.getEmailUsuario() + "\t" + usuario2.getTelefonoCleluarUSuario() + "\n");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------\n");
        System.out.println(usuario3.getIdUsuario() + "\t" + usuario3.getNombreUsuario() + "\t" + usuario3.getApellidoParternoUsuario() + "\t" + usuario3.getApellidoMaternoUsuario() + "\t" + usuario3.getEmailUsuario() + "\t" + usuario3.getTelefonoCleluarUSuario() + "\n");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------\n");
    }  
    
}
