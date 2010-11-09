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
public class Mision {

    private Short idMision;

    private String nombre;

    private String descripcion;

    private short nivelRequerido;

    private short recompensaExp;

    private boolean repetible;

    private short idPersonajeConcluyeMision;

    private dbDelegate conexion;

    public void Mision(){

    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getIdMision() {
        return idMision;
    }

    public void setIdMision(Short idMision) {
        this.idMision = idMision;
    }

    public short getIdPersonajeConcluyeMision() {
        return idPersonajeConcluyeMision;
    }

    public void setIdPersonajeConcluyeMision(short idPersonajeConcluyeMision) {
        this.idPersonajeConcluyeMision = idPersonajeConcluyeMision;
    }

    public short getNivelRequerido() {
        return nivelRequerido;
    }

    public void setNivelRequerido(short nivelRequerido) {
        this.nivelRequerido = nivelRequerido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getRecompensaExp() {
        return recompensaExp;
    }

    public void setRecompensaExp(short recompensaExp) {
        this.recompensaExp = recompensaExp;
    }

    public boolean isRepetible() {
        return repetible;
    }

    public void setRepetible(boolean repetible) {
        this.repetible = repetible;
    }

    public void setMision(short id){
        this.conexion = new dbDelegate();
        System.out.println("Inicio obtiene datos personaje");
        String StrSql = "SELECT * FROM mision "+
                        "WHERE id = "+ id;
        System.out.println(StrSql);
        try {
            ResultSet res = conexion.Consulta(StrSql);
            if (res.next()) {
                this.setDescripcion(res.getString("descripcion"));
                this.setNombre(res.getString("nombre"));
                this.setIdMision(res.getShort("id"));
                this.setIdPersonajeConcluyeMision(res.getShort("personaje_id"));
                this.setNivelRequerido(res.getShort("nivelrequerido"));
                this.setRepetible(Boolean.parseBoolean(res.getString("repetible")));
                this.setRecompensaExp(res.getShort("recompensaexp"));
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->Objeto , método->setObjeto() " + ex);
        }
    }

    
}
