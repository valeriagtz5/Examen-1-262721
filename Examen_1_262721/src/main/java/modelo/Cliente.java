/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author valeria
 */
public class Cliente {
    
    private int idCliente;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;

    public Cliente(int idCliente, String nombre, String apellidoPaterno, String apellidoMaterno) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
    }

    
    public int getIdCliente() {
        return idCliente; 
    }
    
    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente; 
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }
    
    public String getApellidoPaterno() { 
        return apellidoPaterno; 
    }
    
    public void setApellidoPaterno(String apellidoPaterno) { 
        this.apellidoPaterno = apellidoPaterno;
    }
    
    public String getApellidoMaterno() { 
        return apellidoMaterno; 
    }
    
    public void setApellidoMaterno(String apellidoMaterno) { 
        this.apellidoMaterno = apellidoMaterno; 
    }

    public String getNombreCompleto() {
        return (nombre + " " + apellidoPaterno + " " + apellidoMaterno).trim();
    }

}
