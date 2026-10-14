package com.clubdeportivo.dao;

import com.clubdeportivo.conexion.ConexionDB;
import com.clubdeportivo.modelo.Reserva;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// DAO con el CRUD completo de reservas (Alta, Baja, Modificacion, Listado)
public class ReservaDAO {

    // Devuelve true si esa cancha ya tiene una reserva para esa fecha y hora.
    // idExcluir sirve para cuando estamos modificando (no chocar con la misma reserva)
    public boolean estaOcupada(String cancha, String fecha, String hora, int idExcluir) throws SQLException {
        Connection con = ConexionDB.conectar();
        String sql = "SELECT * FROM reservas WHERE cancha = ? AND fecha = ? AND hora = ? AND idreserva <> ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, cancha);
        ps.setString(2, fecha);
        ps.setString(3, hora);
        ps.setInt(4, idExcluir);
        ResultSet rs = ps.executeQuery();

        boolean ocupada = rs.next();

        rs.close();
        ps.close();
        con.close();
        return ocupada;
    }

    // ALTA: agrega una reserva nueva
    public void agregar(String cancha, String cliente, String fecha, String hora) throws SQLException {
        Connection con = ConexionDB.conectar();
        String sql = "INSERT INTO reservas (cancha, cliente, fecha, hora) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, cancha);
        ps.setString(2, cliente);
        ps.setString(3, fecha);
        ps.setString(4, hora);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    // MODIFICACION: actualiza una reserva existente por su id
    public void modificar(int idReserva, String cancha, String cliente, String fecha, String hora) throws SQLException {
        Connection con = ConexionDB.conectar();
        String sql = "UPDATE reservas SET cancha = ?, cliente = ?, fecha = ?, hora = ? WHERE idreserva = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, cancha);
        ps.setString(2, cliente);
        ps.setString(3, fecha);
        ps.setString(4, hora);
        ps.setInt(5, idReserva);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    // BAJA: elimina una reserva por su id
    public void eliminar(int idReserva) throws SQLException {
        Connection con = ConexionDB.conectar();
        String sql = "DELETE FROM reservas WHERE idreserva = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idReserva);
        ps.executeUpdate();
        ps.close();
        con.close();
    }

    // LISTADO: trae todas las reservas
    public ArrayList<Reserva> listarTodas() throws SQLException {
        ArrayList<Reserva> lista = new ArrayList<>();
        Connection con = ConexionDB.conectar();
        String sql = "SELECT * FROM reservas ORDER BY fecha, hora";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            lista.add(new Reserva(
                    rs.getInt("idreserva"),
                    rs.getString("cancha"),
                    rs.getString("cliente"),
                    rs.getString("fecha"),
                    rs.getString("hora")
            ));
        }
        rs.close();
        ps.close();
        con.close();
        return lista;
    }

    // BUSQUEDA/FILTRO: filtra las reservas segun el criterio elegido
    // (cliente, cancha o fecha). Devuelve solo las que coinciden.
    public ArrayList<Reserva> filtrar(String criterio, String texto) throws SQLException {
        ArrayList<Reserva> lista = new ArrayList<>();
        Connection con = ConexionDB.conectar();

        String columna;
        switch (criterio) {
            case "Cancha":
                columna = "cancha";
                break;
            case "Fecha":
                columna = "fecha";
                break;
            default:
                columna = "cliente";
        }

        String sql = "SELECT * FROM reservas WHERE " + columna + " LIKE ? ORDER BY fecha, hora";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, "%" + texto + "%");
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            lista.add(new Reserva(
                    rs.getInt("idreserva"),
                    rs.getString("cancha"),
                    rs.getString("cliente"),
                    rs.getString("fecha"),
                    rs.getString("hora")
            ));
        }
        rs.close();
        ps.close();
        con.close();
        return lista;
    }
}
