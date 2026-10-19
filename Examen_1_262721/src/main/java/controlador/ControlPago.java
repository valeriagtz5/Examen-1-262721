package controlador;

import interfaces.IServicio;
import java.util.List;
import modelo.ComprobantePago;
import modelo.Servicio;
import vista.PantallaPago;

public class ControlPago {
    private IServicio modeloServicio;
    private PantallaPago pantallaPago;
    private Servicio servicioSeleccionado;
    private ComprobantePago comprobanteActual;

    public ControlPago(IServicio modeloServicio) {
        this.modeloServicio = modeloServicio;
    }

    public void setPantallaPago(PantallaPago pantallaPago) {
        this.pantallaPago = pantallaPago;
    }

    public List<Servicio> buscarSimilares(String numeroServicio) {
        // Al hacer otra búsqueda se borra la selección anterior.
        servicioSeleccionado = null;
        comprobanteActual = null;
        List<Servicio> encontrados = modeloServicio.buscarPorNumero(numeroServicio);

        if (pantallaPago != null) {
            pantallaPago.mostrarListaClientes(encontrados);
        }
        return encontrados;
    }

    public void obtenerDatosConsumo(int idServicio) {
        servicioSeleccionado = modeloServicio.obtenerDatosConsumo(idServicio);
        comprobanteActual = null;

        if (servicioSeleccionado == null) {
            throw new IllegalArgumentException("No se encontró el servicio seleccionado.");
        }
        if (pantallaPago != null) {
            pantallaPago.mostrarDatosConsumo(servicioSeleccionado);
        }
    }

    public void procesarPago(double monto, String noTarjeta) {
        if (servicioSeleccionado == null) {
            throw new IllegalStateException("Selecciona primero un cliente de la lista.");
        }

        // Crea el comprobante y lo guarda en el servicio seleccionado.
        ComprobantePago comprobante = ComprobantePago.generarComprobante(monto, noTarjeta);
        servicioSeleccionado.registrarComprobante(comprobante);
        comprobanteActual = comprobante;

        if (pantallaPago != null) {
            pantallaPago.mostrarRecibo(comprobante);
        }
    }

    public Servicio getServicioSeleccionado() {
        return servicioSeleccionado;
    }

    public ComprobantePago getComprobanteActual() {
        return comprobanteActual;
    }
}
