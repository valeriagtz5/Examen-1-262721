package modelo;

import java.util.Date;

public class ComprobantePago {
    private static int siguienteId = 1;
    private int idComprobantePago;
    private String folio;
    private Date fechaHora;
    private double montoPagado;
    private String noTarjeta;
    private String autorizacion;
    private EstatusPago estatus;

    public ComprobantePago(int idComprobantePago, String folio, Date fechaHora,
            double montoPagado, String noTarjeta, String autorizacion, EstatusPago estatus) {
        this.idComprobantePago = idComprobantePago;
        this.folio = folio;
        this.fechaHora = new Date(fechaHora.getTime());
        this.montoPagado = montoPagado;
        this.noTarjeta = ocultarNumeroTarjeta(noTarjeta);
        this.autorizacion = autorizacion;
        this.estatus = estatus;
    }

    public static ComprobantePago generarComprobante(double monto, String noTarjeta) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor que cero.");
        }

        String tarjeta = quitarEspaciosYGuiones(noTarjeta);
        if (tarjeta.length() != 16) {
            throw new IllegalArgumentException("Ingresa un número de tarjeta de 16 dígitos.");
        }
        for (int i = 0; i < tarjeta.length(); i++) {
            char caracter = tarjeta.charAt(i);
            if (caracter < '0' || caracter > '9') {
                throw new IllegalArgumentException("Ingresa un número de tarjeta de 16 dígitos.");
            }
        }

        
        int id = siguienteId;
        siguienteId++;
        String folio = "FOLIO-" + System.currentTimeMillis() + "-" + id;
        int numeroAutorizacion = (int) (Math.random() * 900000) + 100000;
        String autorizacion = "AUT-" + numeroAutorizacion;

        ComprobantePago comprobante = new ComprobantePago(id, folio, new Date(),
                monto, tarjeta, autorizacion, EstatusPago.APROBADO);
        return comprobante;
    }

    private static String quitarEspaciosYGuiones(String tarjeta) {
        if (tarjeta == null) {
            return "";
        }
        String numero = "";
        for (int i = 0; i < tarjeta.length(); i++) {
            char caracter = tarjeta.charAt(i);
            if (caracter != '-' && !Character.isWhitespace(caracter)) {
                numero = numero + caracter;
            }
        }
        return numero;
    }

    private static String ocultarNumeroTarjeta(String tarjeta) {
        String numero = quitarEspaciosYGuiones(tarjeta);
        if (numero.length() < 4) {
            throw new IllegalArgumentException("El número de tarjeta está incompleto.");
        }
        String ultimosDigitos = numero.substring(numero.length() - 4);
        return "XXXX XXXX XXXX " + ultimosDigitos;
    }

    public int getIdComprobantePago() {
        return idComprobantePago;
    }

    public String getFolio() {
        return folio;
    }

    public Date getFechaHora() {
        return new Date(fechaHora.getTime());
    }

    public double getMontoPagado() {
        return montoPagado;
    }

    public String getNoTarjeta() {
        return noTarjeta;
    }

    public String getAutorizacion() {
        return autorizacion;
    }

    public EstatusPago getEstatus() {
        return estatus;
    }
}
