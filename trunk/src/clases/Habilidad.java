/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author gerald
 */
public class Habilidad {
    private Short idHabilidad;

    private String nombre;

    private String descripcion;

    private short danoBeneficio;

    private short costoBasico;

    private short nivelMaximo;
    private dbDelegate conexion;

     /*
     * Busca los valores en la base de datos y los mapea para que queden disponible
     * de manera objetual para el sistema
     */
    public void setHabilidad(short id){
        this.conexion = new dbDelegate();
        System.out.println("Inicio obtiene datos personaje");
        String StrSql = "SELECT * FROM Habilidad "+
                        "WHERE id = "+ id;
        System.out.println(StrSql);
        try {
            ResultSet res = conexion.Consulta(StrSql);
            if (res.next()) {
                this.setDescripcion(res.getString("descripcion"));
                this.setNombre(res.getString("nombre"));
                this.setIdHabilidad(res.getShort("id"));
                this.setDanoBeneficio(res.getShort("dañobeneficio"));
                this.setNivelMaximo(res.getShort("nivelMaximo"));
                this.setCostoBasico(res.getShort("costoBasico"));

            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->habilidades , método->setHabilidad() " + ex);
        }
    }

    public short getCostoBasico() {
        return costoBasico;
    }

    public void setCostoBasico(short costoBasico) {
        this.costoBasico = costoBasico;
    }

    public short getDanoBeneficio() {
        return danoBeneficio;
    }

    public void setDanoBeneficio(short danoBeneficio) {
        this.danoBeneficio = danoBeneficio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getIdHabilidad() {
        return idHabilidad;
    }

    public void setIdHabilidad(Short idHabilidad) {
        this.idHabilidad = idHabilidad;
    }

    public short getNivelMaximo() {
        return nivelMaximo;
    }

    public void setNivelMaximo(short nivelMaximo) {
        this.nivelMaximo = nivelMaximo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
}
