package com.clubdeportivo.modelo;

// Clase que representa una reserva de la tabla reservas
public class Reserva {

    private int idReserva;
    private String cancha;
    private String cliente;
    private String fecha; // dd/mm/aaaa
    private String hora;  // HH:mm

    public Reserva(int idReserva, String cancha, String cliente, String fecha, String hora) {
        this.idReserva = idReserva;
        this.cancha = cancha;
        this.cliente = cliente;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public String getCancha() {
        return cancha;
    }

    public String getCliente() {
        return cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }
}
