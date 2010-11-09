package clases;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
/**
 *
 * @author Gerald
 */
public class Conexion {
    private Connection cn;
    private Statement St;
    private ResultSet reg;
    public  Conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/db_trabajo_titulo";
            cn = DriverManager.getConnection(url, "root", "gwdesarrollo");
            System.out.println("Conexion ´Conexion´ correcta");
        } catch (Exception ee) {
            System.out.println("Error:" + ee.getMessage());
        }
    }

    public String Ejecutar(String sql) {
        String error = "";
        try {
            St = cn.createStatement();
            St.execute(sql);
        } catch (Exception ex) {
            error = ex.getMessage();
        }
        return (error);
    }

    public ResultSet Consulta(String sql) {
        String error = "";
        try {
            St = cn.createStatement();
            reg = St.executeQuery(sql);
        } catch (Exception ee) {
            error = ee.getMessage();
        }
        return (reg);
    }
}
