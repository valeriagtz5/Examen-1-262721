/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package interfaces;

import java.util.List;
import modelo.Servicio;

/**
 *
 * @author valeria
 */
public interface IServicio {
    List<Servicio> buscarPorNumero(String numeroServicio);
    Servicio obtenerDatosConsumo(int idServicio);
}
