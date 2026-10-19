package vista;

import controlador.ControlMenu;
import controlador.ControlPago;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.DefaultListModel;
import modelo.ComprobantePago;
import modelo.Servicio;

/**
 * @author valeria
 */
public class PantallaPago extends javax.swing.JFrame {

    private ControlPago controlPago;
    private ControlMenu controlMenu;
    private DefaultListModel<Servicio> modeloClientes = new DefaultListModel<>();
    private boolean seleccionandoCliente;
    private String numeroBuscado = "";

    public PantallaPago() {
        initComponents();
        limpiarDatos();
        setLocationRelativeTo(null);
    }

    public void setControlPago(ControlPago controlPago) {
        this.controlPago = controlPago;
    }

    public void setControlMenu(ControlMenu controlMenu) {
        this.controlMenu = controlMenu;
    }

    private void btnPagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagarActionPerformed
        double monto = controlPago.getServicioSeleccionado().getMontoPagar();
        ingresarDatosTarjeta(txtTarjeta.getText(), monto);
    }//GEN-LAST:event_btnPagarActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        volver();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void txtTarjetaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTarjetaActionPerformed
        btnPagar.doClick();
    }//GEN-LAST:event_txtTarjetaActionPerformed

    private void txtNoServicioCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_txtNoServicioCaretUpdate
        String numero = txtNoServicio.getText();
        // Busca solo si cambió el texto, no al mover el cursor.
        if (!seleccionandoCliente && !numero.equals(numeroBuscado)) {
            numeroBuscado = numero;
            teclearNumeroServicio(numero);
        }
    }//GEN-LAST:event_txtNoServicioCaretUpdate

    private void lstClientesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstClientesValueChanged
        if (!seleccionandoCliente && !evt.getValueIsAdjusting()) {
            Servicio servicio = lstClientes.getSelectedValue();
            if (servicio != null) {
                seleccionarCliente(servicio.getCliente().getIdCliente());
            }
        }
    }//GEN-LAST:event_lstClientesValueChanged

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        volver();
    }//GEN-LAST:event_formWindowClosing

    public void mostrarPantallaPago() {
        setVisible(true);
        txtNoServicio.requestFocusInWindow();
    }

    public void teclearNumeroServicio(String numeroServicio) {
        limpiarDatos();
        controlPago.buscarSimilares(numeroServicio);
    }

    public void mostrarListaClientes(List<Servicio> clientes) {
        modeloClientes.clear();
        for (Servicio servicio : clientes) {
            modeloClientes.addElement(servicio);
        }
        if (txtNoServicio.getText().trim().isEmpty()) {
            lblTextoClientes.setText("Aún no hay datos de clientes.");
        } else {
            lblTextoClientes.setText("No se encontraron clientes.");
        }
        lblTextoClientes.setVisible(clientes.isEmpty());
    }

    public void seleccionarCliente(int idCliente) {
        Servicio servicio = lstClientes.getSelectedValue();
        if (servicio == null || servicio.getCliente().getIdCliente() != idCliente) {
            return;
        }
        seleccionandoCliente = true;
        txtNoServicio.setText(servicio.getNumeroServicio());
        numeroBuscado = servicio.getNumeroServicio();
        modeloClientes.clear();
        modeloClientes.addElement(servicio);
        lstClientes.setSelectedIndex(0);
        seleccionandoCliente = false;
        controlPago.obtenerDatosConsumo(servicio.getIdServicio());
    }

    private void limpiarDatos() {
        lblDatos.setText("Datos de Consumo");
        txtDatos.setName("txtDatos");
        txtDatos.setText("Aún no hay datos de consumo por mostrar.");
        txtTarjeta.setText("");
        lblMensajePago.setText(" ");
        lblTarjeta.setVisible(false);
        txtTarjeta.setVisible(false);
        btnPagar.setVisible(false);
    }

    public void mostrarDatosConsumo(Servicio servicio) {
        if (servicio.estaPagado()) {
            List<ComprobantePago> pagos = servicio.getComprobantes();
            mostrarRecibo(pagos.get(pagos.size() - 1));
            return;
        }
        txtDatos.setText("Cliente: " + servicio.getCliente().getNombreCompleto()
                + "\nNo. de servicio: " + servicio.getNumeroServicio()
                + "\nPeriodo: " + periodo(servicio)
                + "\nkWh consumidos: " + servicio.getKwhConsumidos()
                + "\nMonto total: " + String.format("$%.2f MXN", servicio.getMontoPagar()));
        lblTarjeta.setVisible(true);
        txtTarjeta.setVisible(true);
        btnPagar.setVisible(true);
    }

    public void ingresarDatosTarjeta(String noTarjeta, double monto) {
        try {
            controlPago.procesarPago(monto, noTarjeta);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            lblMensajePago.setText(ex.getMessage());
        }
    }

    public void mostrarRecibo(ComprobantePago comprobante) {
        Servicio servicio = controlPago.getServicioSeleccionado();
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        limpiarDatos();
        lblDatos.setText("Pago");
        txtDatos.setName("comprobantePago");
        txtDatos.setText("COMPROBANTE DE PAGO\n\n"
                + "Folio: " + comprobante.getFolio()
                + "\nFecha y hora: " + fecha.format(comprobante.getFechaHora())
                + "\nCliente: " + servicio.getCliente().getNombreCompleto()
                + "\nNo. de servicio: " + servicio.getNumeroServicio()
                + "\nPeriodo: " + periodo(servicio)
                + "\nkWh consumidos: " + servicio.getKwhConsumidos()
                + "\nMonto pagado: " + String.format("$%.2f MXN", comprobante.getMontoPagado())
                + "\n\nMétodo de pago: Tarjeta de débito/crédito"
                + "\nNo. de tarjeta: " + comprobante.getNoTarjeta()
                + "\nAutorización: " + comprobante.getAutorizacion()
                + "\nEstatus: " + comprobante.getEstatus()
                + "\n\n¡Gracias por realizar su pago a tiempo!");
        txtDatos.setCaretPosition(0);
        txtNoServicio.setEditable(false);
        lstClientes.setEnabled(false);
    }

    private String periodo(Servicio servicio) {
        SimpleDateFormat fecha = new SimpleDateFormat("dd/MM/yyyy");
        return fecha.format(servicio.getFechaInicio()) + " al " + fecha.format(servicio.getFechaFin());
    }

    private void volver() {
        txtTarjeta.setText("");
        dispose();
        controlMenu.mostrarMenu();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlPrincipal = new javax.swing.JPanel();
        pnlBase = new javax.swing.JPanel();
        PNLTITULO = new javax.swing.JPanel();
        lblTutilo = new javax.swing.JLabel();
        pnlNoServicio = new javax.swing.JPanel();
        pnlTitiloNoServicio = new javax.swing.JPanel();
        lblNoServicio = new javax.swing.JLabel();
        lblTextoNoServicio = new javax.swing.JLabel();
        txtNoServicio = new javax.swing.JTextField();
        pnlClientes = new javax.swing.JPanel();
        pnlTituloClientes = new javax.swing.JPanel();
        lblClientes = new javax.swing.JLabel();
        srlClientes = new javax.swing.JScrollPane();
        lstClientes = new javax.swing.JList<>();
        lblTextoClientes = new javax.swing.JLabel();
        pnlDatos = new javax.swing.JPanel();
        pnlTituloDatos = new javax.swing.JPanel();
        lblDatos = new javax.swing.JLabel();
        srlDatos = new javax.swing.JScrollPane();
        txtDatos = new javax.swing.JTextArea();
        pnlBotones = new javax.swing.JPanel();
        lblTarjeta = new javax.swing.JLabel();
        txtTarjeta = new javax.swing.JTextField();
        btnPagar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        lblMensajePago = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Pagar Servicio de Luz");
        setName("pantallaPago");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        pnlPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        pnlBase.setBackground(new java.awt.Color(240, 240, 240));
        pnlBase.setForeground(new java.awt.Color(255, 255, 204));
        pnlBase.setPreferredSize(new java.awt.Dimension(724, 640));

        PNLTITULO.setBackground(new java.awt.Color(0, 42, 101));

        lblTutilo.setFont(new java.awt.Font("Kailasa", 1, 13)); // NOI18N
        lblTutilo.setForeground(new java.awt.Color(255, 255, 255));
        lblTutilo.setText("Pagar Servicio de Luz");

        javax.swing.GroupLayout PNLTITULOLayout = new javax.swing.GroupLayout(PNLTITULO);
        PNLTITULO.setLayout(PNLTITULOLayout);
        PNLTITULOLayout.setHorizontalGroup(
            PNLTITULOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PNLTITULOLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTutilo)
                .addGap(274, 274, 274))
        );
        PNLTITULOLayout.setVerticalGroup(
            PNLTITULOLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PNLTITULOLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(lblTutilo, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnlNoServicio.setBackground(new java.awt.Color(240, 240, 240));
        pnlNoServicio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        pnlTitiloNoServicio.setBackground(new java.awt.Color(121, 159, 212));

        lblNoServicio.setBackground(new java.awt.Color(255, 255, 255));
        lblNoServicio.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblNoServicio.setForeground(new java.awt.Color(255, 255, 255));
        lblNoServicio.setText("Número de Servicio");

        javax.swing.GroupLayout pnlTitiloNoServicioLayout = new javax.swing.GroupLayout(pnlTitiloNoServicio);
        pnlTitiloNoServicio.setLayout(pnlTitiloNoServicioLayout);
        pnlTitiloNoServicioLayout.setHorizontalGroup(
            pnlTitiloNoServicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitiloNoServicioLayout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(lblNoServicio)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTitiloNoServicioLayout.setVerticalGroup(
            pnlTitiloNoServicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitiloNoServicioLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblNoServicio)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        lblTextoNoServicio.setText("Ingresa tu Número de Servicio:");

        txtNoServicio.setName("txtNoServicio");
        txtNoServicio.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                txtNoServicioCaretUpdate(evt);
            }
        });

        lblTextoNoServicio.setToolTipText("");

        javax.swing.GroupLayout pnlNoServicioLayout = new javax.swing.GroupLayout(pnlNoServicio);
        pnlNoServicio.setLayout(pnlNoServicioLayout);
        pnlNoServicioLayout.setHorizontalGroup(
            pnlNoServicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTitiloNoServicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNoServicioLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblTextoNoServicio)
                .addGap(81, 81, 81))
            .addGroup(pnlNoServicioLayout.createSequentialGroup()
                .addGap(68, 68, 68)
                .addComponent(txtNoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(54, Short.MAX_VALUE))
        );
        pnlNoServicioLayout.setVerticalGroup(
            pnlNoServicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNoServicioLayout.createSequentialGroup()
                .addComponent(pnlTitiloNoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTextoNoServicio)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtNoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 101, Short.MAX_VALUE))
        );

        pnlClientes.setPreferredSize(new java.awt.Dimension(360, 212));
        pnlClientes.setBackground(new java.awt.Color(240, 240, 240));
        pnlClientes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        pnlTituloClientes.setBackground(new java.awt.Color(121, 159, 212));

        lblClientes.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblClientes.setForeground(new java.awt.Color(255, 255, 255));
        lblClientes.setText("Clientes Encontrados");

        javax.swing.GroupLayout pnlTituloClientesLayout = new javax.swing.GroupLayout(pnlTituloClientes);
        pnlTituloClientes.setLayout(pnlTituloClientesLayout);
        pnlTituloClientesLayout.setHorizontalGroup(
            pnlTituloClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTituloClientesLayout.createSequentialGroup()
                .addGap(107, 107, 107)
                .addComponent(lblClientes)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTituloClientesLayout.setVerticalGroup(
            pnlTituloClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTituloClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblClientes)
                .addContainerGap(11, Short.MAX_VALUE))
        );

        srlClientes.setBackground(new java.awt.Color(240, 240, 240));
        lstClientes.setModel(modeloClientes);
        lstClientes.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstClientes.setName("lstClientes");
        lstClientes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstClientesValueChanged(evt);
            }
        });
        srlClientes.setViewportView(lstClientes);

        lblTextoClientes.setText("Aún no hay datos de clientes.");

        javax.swing.GroupLayout pnlClientesLayout = new javax.swing.GroupLayout(pnlClientes);
        pnlClientes.setLayout(pnlClientesLayout);
        pnlClientesLayout.setHorizontalGroup(
            pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTituloClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlClientesLayout.createSequentialGroup()
                .addGap(84, 84, 84)
                .addComponent(lblTextoClientes)
                .addContainerGap(94, Short.MAX_VALUE))
            .addComponent(srlClientes)
        );
        pnlClientesLayout.setVerticalGroup(
            pnlClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlClientesLayout.createSequentialGroup()
                .addComponent(pnlTituloClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTextoClientes)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(srlClientes)
                .addContainerGap())
        );

        pnlDatos.setBackground(new java.awt.Color(240, 240, 240));
        pnlDatos.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        pnlTituloDatos.setBackground(new java.awt.Color(121, 159, 212));

        lblDatos.setFont(new java.awt.Font("Helvetica Neue", 1, 13)); // NOI18N
        lblDatos.setForeground(new java.awt.Color(255, 255, 255));
        lblDatos.setText("Datos de Consumo");

        javax.swing.GroupLayout pnlTituloDatosLayout = new javax.swing.GroupLayout(pnlTituloDatos);
        pnlTituloDatos.setLayout(pnlTituloDatosLayout);
        pnlTituloDatosLayout.setHorizontalGroup(
            pnlTituloDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTituloDatosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDatos)
                .addGap(288, 288, 288))
        );
        pnlTituloDatosLayout.setVerticalGroup(
            pnlTituloDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTituloDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDatos)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        txtDatos.setEditable(false);
        txtDatos.setColumns(45);
        txtDatos.setRows(19);
        txtDatos.setLineWrap(true);
        txtDatos.setWrapStyleWord(true);
        txtDatos.setName("txtDatos");
        srlDatos.setViewportView(txtDatos);

        lblTarjeta.setText("Número de tarjeta:");
        lblTarjeta.setName("lblTarjeta");
        pnlBotones.add(lblTarjeta);
        txtTarjeta.setColumns(20);
        txtTarjeta.setName("txtTarjeta");
        txtTarjeta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTarjetaActionPerformed(evt);
            }
        });
        pnlBotones.add(txtTarjeta);
        btnPagar.setText("Pagar");
        btnPagar.setName("btnPagar");
        btnPagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagarActionPerformed(evt);
            }
        });
        pnlBotones.add(btnPagar);
        btnVolver.setText("Volver");
        btnVolver.setName("btnVolver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });
        pnlBotones.add(btnVolver);
        lblMensajePago.setText(" ");
        lblMensajePago.setForeground(java.awt.Color.RED);
        lblMensajePago.setName("lblMensajePago");

        javax.swing.GroupLayout pnlDatosLayout = new javax.swing.GroupLayout(pnlDatos);
        pnlDatos.setLayout(pnlDatosLayout);
        pnlDatosLayout.setHorizontalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTituloDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(srlDatos)
            .addComponent(pnlBotones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblMensajePago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlDatosLayout.setVerticalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addComponent(pnlTituloDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(srlDatos)
                .addComponent(pnlBotones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(lblMensajePago))
        );

        javax.swing.GroupLayout pnlBaseLayout = new javax.swing.GroupLayout(pnlBase);
        pnlBase.setLayout(pnlBaseLayout);
        pnlBaseLayout.setHorizontalGroup(
            pnlBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBaseLayout.createSequentialGroup()
                .addComponent(pnlNoServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(pnlDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(PNLTITULO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlBaseLayout.setVerticalGroup(
            pnlBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBaseLayout.createSequentialGroup()
                .addComponent(PNLTITULO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBaseLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlClientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlNoServicio, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap(65, Short.MAX_VALUE)
                .addComponent(pnlBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPrincipalLayout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(pnlBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String[] args) {
        main.Examen_1_262721.main(args);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PNLTITULO;
    private javax.swing.JLabel lblClientes;
    private javax.swing.JLabel lblDatos;
    private javax.swing.JLabel lblNoServicio;
    private javax.swing.JLabel lblTextoClientes;
    private javax.swing.JLabel lblTarjeta;
    private javax.swing.JLabel lblMensajePago;
    private javax.swing.JList<Servicio> lstClientes;
    private javax.swing.JTextArea txtDatos;
    private javax.swing.JTextField txtTarjeta;
    private javax.swing.JButton btnPagar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JPanel pnlBotones;
    private javax.swing.JScrollPane srlDatos;
    private javax.swing.JLabel lblTextoNoServicio;
    private javax.swing.JLabel lblTutilo;
    private javax.swing.JPanel pnlBase;
    private javax.swing.JPanel pnlClientes;
    private javax.swing.JPanel pnlDatos;
    private javax.swing.JPanel pnlNoServicio;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JPanel pnlTitiloNoServicio;
    private javax.swing.JPanel pnlTituloClientes;
    private javax.swing.JPanel pnlTituloDatos;
    private javax.swing.JScrollPane srlClientes;
    private javax.swing.JTextField txtNoServicio;
    // End of variables declaration//GEN-END:variables
}
