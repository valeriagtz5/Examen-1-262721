package modelo;

import interfaces.IServicio;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Servicio implements IServicio {
    private int idServicio;
    private String numeroServicio;
    private Date fechaInicio;
    private Date fechaFin;
    private Double kwhConsumidos;
    private Double montoPagar;
    private Cliente cliente;
    private List<ComprobantePago> comprobantes = new ArrayList<>();
    private List<Servicio> listaServicios;

    // Estos son los clientes y servicios que se usan para probar el programa.
    public Servicio() {
        listaServicios = new ArrayList<>();
        Date inicio = crearFecha(2026, 9, 1);
        Date fin = crearFecha(2026, 10, 31);

        Cliente cliente1 = new Cliente(1, "María Elisa", "Sánchez", "");
        Cliente cliente2 = new Cliente(2, "Emilio", "Gómez", "");
        Cliente cliente3 = new Cliente(3, "Lucía Gabriela", "Ávila", "");

        listaServicios.add(new Servicio(101, "12345678", inicio, fin, 250.0, 650.00, cliente1));
        listaServicios.add(new Servicio(102, "12347865", inicio, fin, 280.0, 720.00, cliente2));
        listaServicios.add(new Servicio(103, "12348885", inicio, fin, 350.0, 850.00, cliente3));
    }

    public Servicio(int idServicio, String numeroServicio, Date fechaInicio,
            Date fechaFin, Double kwhConsumidos, Double montoPagar, Cliente cliente) {
        this.idServicio = idServicio;
        this.numeroServicio = numeroServicio;
        this.fechaInicio = copiarFecha(fechaInicio);
        this.fechaFin = copiarFecha(fechaFin);
        this.kwhConsumidos = kwhConsumidos;
        this.montoPagar = montoPagar;
        this.cliente = cliente;
    }

    private Date crearFecha(int anio, int mes, int dia) {
        Calendar fecha = Calendar.getInstance();
        fecha.clear();
        fecha.set(anio, mes - 1, dia);
        return fecha.getTime();
    }

    private Date copiarFecha(Date fecha) {
        if (fecha == null) {
            return null;
        }
        return new Date(fecha.getTime());
    }

    public int getIdServicio() {
        return idServicio;
    }

    public String getNumeroServicio() {
        return numeroServicio;
    }

    public Date getFechaInicio() {
        return copiarFecha(fechaInicio);
    }

    public Date getFechaFin() {
        return copiarFecha(fechaFin);
    }

    public Double getMontoPagar() {
        return montoPagar;
    }

    public Double getKwhConsumidos() {
        return kwhConsumidos;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<ComprobantePago> getComprobantes() {
        return new ArrayList<>(comprobantes);
    }

    @Override
    public List<Servicio> buscarPorNumero(String numeroServicio) {
        List<Servicio> encontrados = new ArrayList<>();
        if (numeroServicio == null || numeroServicio.trim().isEmpty()) {
            return encontrados;
        }

        String numero = numeroServicio.trim();
        if (listaServicios == null) {
            if (this.numeroServicio.contains(numero)) {
                encontrados.add(this);
            }
        } else {
            // Revisa uno por uno los números de servicio.
            for (Servicio servicio : listaServicios) {
                if (servicio.getNumeroServicio().contains(numero)) {
                    encontrados.add(servicio);
                }
            }
        }
        return encontrados;
    }

    @Override
    public Servicio obtenerDatosConsumo(int idServicio) {
        if (listaServicios == null) {
            if (this.idServicio == idServicio) {
                return this;
            }
        } else {
            for (Servicio servicio : listaServicios) {
                if (servicio.getIdServicio() == idServicio) {
                    return servicio;
                }
            }
        }
        return null;
    }

    public boolean estaPagado() {
        for (ComprobantePago comprobante : comprobantes) {
            if (comprobante.getEstatus() == EstatusPago.APROBADO) {
                return true;
            }
        }
        return false;
    }

    public void registrarComprobante(ComprobantePago comprobante) {
        if (estaPagado()) {
            throw new IllegalStateException("Este periodo de consumo ya está pagado.");
        }
        if (comprobante == null || comprobante.getMontoPagado() != montoPagar) {
            throw new IllegalArgumentException("El comprobante debe corresponder al monto del servicio.");
        }
        comprobantes.add(comprobante);
    }

    // Este texto es el que aparece en la lista de clientes de la pantalla.
    @Override
    public String toString() {
        return numeroServicio + " - " + cliente.getNombreCompleto();
    }
}
