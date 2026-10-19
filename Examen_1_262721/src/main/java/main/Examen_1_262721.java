package main;

import controlador.ControlMenu;

public class Examen_1_262721 {

    public static void main(String[] args) {
        // Abre el menú en el turno de Swing para mostrar las ventanas.
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ControlMenu controlMenu = new ControlMenu();
                controlMenu.mostrarMenu();
            }
        });
    }
}
