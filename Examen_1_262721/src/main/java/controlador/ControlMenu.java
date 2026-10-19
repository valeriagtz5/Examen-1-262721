package controlador;

import modelo.Servicio;
import vista.PantallaMenu;
import vista.PantallaPago;

public class ControlMenu {
    private Servicio servicios;
    private PantallaMenu pantallaMenu;

    public ControlMenu() {
        servicios = new Servicio();
        pantallaMenu = new PantallaMenu();
        pantallaMenu.setControlMenu(this);
    }

    public void mostrarMenu() {
        pantallaMenu.mostrarMenu();
    }

    public void solicitarPantallaPago() {
        ControlPago controlPago = new ControlPago(servicios);
        PantallaPago pantallaPago = new PantallaPago();

        // Conecta la pantalla de pago con sus controles.
        pantallaPago.setControlMenu(this);
        pantallaPago.setControlPago(controlPago);
        controlPago.setPantallaPago(pantallaPago);

        pantallaMenu.setVisible(false);
        pantallaPago.mostrarPantallaPago();
    }
}
