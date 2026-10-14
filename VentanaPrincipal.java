package com.clubdeportivo.vista;

import com.clubdeportivo.dao.ReservaDAO;
import com.clubdeportivo.modelo.Reserva;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {

    private ReservaDAO dao = new ReservaDAO();

    // Celeste tipo Manchester City
    private final Color CELESTE = new Color(108, 171, 221);

    private JComboBox<String> comboCancha;
    private JComboBox<String> comboCriterio;
    private JTextField txtCliente, txtFecha, txtHora, txtFiltro;
    private JTable tabla;
    private DefaultTableModel modelo;
    private int idSeleccionado = -1; // -1 significa que no hay ninguna fila elegida

    public VentanaPrincipal() {
        super("Club Deportivo - Gestión de Reservas");
        initComponents();
        agregarEventos();
        cargarTabla();
    }

    private void initComponents() {
        setSize(750, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CELESTE);

        // Panel de arriba: el logo
        JPanel panelLogo = new JPanel();
        panelLogo.setBackground(CELESTE);
        JLabel lblLogo = new JLabel();
        URL urlLogo = getClass().getResource("/logo.png");
        if (urlLogo != null) {
            ImageIcon icono = new ImageIcon(urlLogo);
            Image imagen = icono.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            lblLogo.setIcon(new ImageIcon(imagen));
        }
        panelLogo.add(lblLogo);
        add(panelLogo, BorderLayout.NORTH);

        // Panel central: formulario + tabla
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setBackground(CELESTE);

        // Formulario
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        panelForm.setBackground(CELESTE);

        panelForm.add(new JLabel("Cancha:"));
        comboCancha = new JComboBox<>(new String[]{
                "Futbol 5", "Futbol 7", "Futbol 11", "Tenis", "Padel", "Basquet", "Rugby"
        });
        panelForm.add(comboCancha);

        panelForm.add(new JLabel("Cliente:"));
        txtCliente = new JTextField();
        panelForm.add(txtCliente);

        panelForm.add(new JLabel("Fecha (dd/mm/aaaa):"));
        txtFecha = new JTextField();
        panelForm.add(txtFecha);

        panelForm.add(new JLabel("Hora (HH:mm):"));
        txtHora = new JTextField();
        panelForm.add(txtHora);

        JButton btnAgregar = new JButton("Agregar");
        JButton btnModificar = new JButton("Modificar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");
        JButton btnDisponibilidad = new JButton("Consultar Disponibilidad");
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(CELESTE);
        panelBotones.add(btnAgregar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);
        panelBotones.add(btnDisponibilidad);
        panelForm.add(panelBotones);

        panelCentral.add(panelForm, BorderLayout.NORTH);

        // Panel de filtro (distinto al buscador del otro proyecto: esto
        // filtra lo que se ve en la tabla, no agrega nada)
        JPanel panelFiltro = new JPanel();
        panelFiltro.setBackground(CELESTE);
        panelFiltro.add(new JLabel("Filtrar por:"));
        comboCriterio = new JComboBox<>(new String[]{"Cliente", "Cancha", "Fecha"});
        panelFiltro.add(comboCriterio);
        txtFiltro = new JTextField(12);
        panelFiltro.add(txtFiltro);
        JButton btnFiltrar = new JButton("Filtrar");
        panelFiltro.add(btnFiltrar);
        JButton btnQuitarFiltro = new JButton("Quitar Filtro");
        panelFiltro.add(btnQuitarFiltro);

        // Tabla
        String[] columnas = {"Código", "Cancha", "Cliente", "Fecha", "Hora"};
        modelo = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(CELESTE);
        panelTabla.add(panelFiltro, BorderLayout.NORTH);
        panelTabla.add(scroll, BorderLayout.CENTER);

        panelCentral.add(panelTabla, BorderLayout.CENTER);
        add(panelCentral, BorderLayout.CENTER);

        // guardo referencias para usarlas en los eventos
        this.btnAgregar = btnAgregar;
        this.btnModificar = btnModificar;
        this.btnEliminar = btnEliminar;
        this.btnLimpiar = btnLimpiar;
        this.btnDisponibilidad = btnDisponibilidad;
        this.btnFiltrar = btnFiltrar;
        this.btnQuitarFiltro = btnQuitarFiltro;
    }

    // botones guardados como atributos para poder usarlos en agregarEventos()
    private JButton btnAgregar, btnModificar, btnEliminar, btnLimpiar, btnDisponibilidad, btnFiltrar, btnQuitarFiltro;

    private void agregarEventos() {
        btnAgregar.addActionListener(e -> agregarReserva());
        btnModificar.addActionListener(e -> modificarReserva());
        btnEliminar.addActionListener(e -> eliminarReserva());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnDisponibilidad.addActionListener(e -> consultarDisponibilidad());
        btnFiltrar.addActionListener(e -> aplicarFiltro());
        btnQuitarFiltro.addActionListener(e -> {
            txtFiltro.setText("");
            cargarTabla();
        });

        // al hacer click en una fila de la tabla, cargo los datos en el formulario
        tabla.getSelectionModel().addListSelectionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila >= 0) {
                idSeleccionado = (int) modelo.getValueAt(fila, 0);
                comboCancha.setSelectedItem(modelo.getValueAt(fila, 1));
                txtCliente.setText((String) modelo.getValueAt(fila, 2));
                txtFecha.setText((String) modelo.getValueAt(fila, 3));
                txtHora.setText((String) modelo.getValueAt(fila, 4));
            }
        });
    }

    // Valida los campos del formulario. Devuelve true si esta todo bien.
    private boolean validarCampos() {
        if (txtCliente.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del cliente no puede estar vacío");
            return false;
        }
        if (!txtFecha.getText().matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(this, "La fecha tiene que tener el formato dd/mm/aaaa");
            return false;
        }
        if (!txtHora.getText().matches("\\d{2}:\\d{2}")) {
            JOptionPane.showMessageDialog(this, "La hora tiene que tener el formato HH:mm");
            return false;
        }
        return true;
    }

    private void agregarReserva() {
        if (!validarCampos()) return;
        String cancha = (String) comboCancha.getSelectedItem();
        String fecha = txtFecha.getText();
        String hora = txtHora.getText();

        try {
            // Regla de negocio: no se puede reservar una cancha que ya esta reservada
            // en esa misma fecha y hora
            if (dao.estaOcupada(cancha, fecha, hora, -1)) {
                JOptionPane.showMessageDialog(this,
                        "Esa cancha ya está reservada ese día a esa hora.",
                        "No disponible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            dao.agregar(cancha, txtCliente.getText(), fecha, hora);
            JOptionPane.showMessageDialog(this, "Reserva agregada");
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private void modificarReserva() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Primero seleccioná una reserva de la tabla");
            return;
        }
        if (!validarCampos()) return;
        String cancha = (String) comboCancha.getSelectedItem();
        String fecha = txtFecha.getText();
        String hora = txtHora.getText();

        try {
            // idSeleccionado se excluye para no chocar contra si misma
            if (dao.estaOcupada(cancha, fecha, hora, idSeleccionado)) {
                JOptionPane.showMessageDialog(this,
                        "Esa cancha ya está reservada ese día a esa hora.",
                        "No disponible", JOptionPane.WARNING_MESSAGE);
                return;
            }

            dao.modificar(idSeleccionado, cancha, txtCliente.getText(), fecha, hora);
            JOptionPane.showMessageDialog(this, "Reserva modificada");
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    // Consulta si la cancha elegida esta libre u ocupada en la fecha y hora indicadas,
    // sin guardar nada todavia
    private void consultarDisponibilidad() {
        String cancha = (String) comboCancha.getSelectedItem();
        String fecha = txtFecha.getText();
        String hora = txtHora.getText();

        if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}") || !hora.matches("\\d{2}:\\d{2}")) {
            JOptionPane.showMessageDialog(this, "Completá fecha (dd/mm/aaaa) y hora (HH:mm) para consultar");
            return;
        }

        try {
            int idAExcluir = (idSeleccionado == -1) ? -1 : idSeleccionado;
            boolean ocupada = dao.estaOcupada(cancha, fecha, hora, idAExcluir);
            if (ocupada) {
                JOptionPane.showMessageDialog(this,
                        cancha + " está RESERVADA el " + fecha + " a las " + hora,
                        "Disponibilidad", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        cancha + " está DISPONIBLE el " + fecha + " a las " + hora,
                        "Disponibilidad", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private void eliminarReserva() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Primero seleccioná una reserva de la tabla");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(this, "¿Seguro que querés eliminar esta reserva?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        try {
            dao.eliminar(idSeleccionado);
            JOptionPane.showMessageDialog(this, "Reserva eliminada");
            limpiarFormulario();
            cargarTabla();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    private void limpiarFormulario() {
        idSeleccionado = -1;
        comboCancha.setSelectedIndex(0);
        txtCliente.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        tabla.clearSelection();
    }

    private void cargarTabla() {
        modelo.setRowCount(0);
        try {
            ArrayList<Reserva> lista = dao.listarTodas();
            for (Reserva r : lista) {
                modelo.addRow(new Object[]{r.getIdReserva(), r.getCancha(), r.getCliente(), r.getFecha(), r.getHora()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }

    // Filtra la tabla segun lo que se eligio en comboCriterio (Cliente/Cancha/Fecha)
    private void aplicarFiltro() {
        String texto = txtFiltro.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribí algo para filtrar");
            return;
        }
        String criterio = (String) comboCriterio.getSelectedItem();

        modelo.setRowCount(0);
        try {
            ArrayList<Reserva> lista = dao.filtrar(criterio, texto);
            if (lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron reservas con ese filtro");
            }
            for (Reserva r : lista) {
                modelo.addRow(new Object[]{r.getIdReserva(), r.getCancha(), r.getCliente(), r.getFecha(), r.getHora()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage());
        }
    }
}
