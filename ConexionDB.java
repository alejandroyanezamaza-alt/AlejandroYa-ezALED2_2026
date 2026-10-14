package com.clubdeportivo.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Clase para conectarse a la base de datos club_deportivo
public class ConexionDB {

    static String url = "jdbc:mysql://localhost:3306/club_deportivo";
    static String usuario = "root";
    static String password = ""; // si tu mysql tiene clave, ponerla aca

    public static Connection conectar() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("No se encontro el driver: " + e.getMessage());
        }
        return DriverManager.getConnection(url, usuario, password);
    }
}
