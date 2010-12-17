/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;

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
    private short recompensaDinero;
    private boolean repetible;
    private short idPersonajeConcluyeMision;
    private dbDelegate conexion;
    private dialogo_mision dialogo;
    private ObjetoMision requerimientos;
    private boolean bool;

    public short getRecompensaDinero() {
        return recompensaDinero;
    }

    public void setRecompensaDinero(short recompensaDinero) {
        this.recompensaDinero = recompensaDinero;
    }


    
    public ObjetoMision getRequerimientos() {
        return requerimientos;
    }

    public void setRequerimientos(ObjetoMision requerimientos) {
        this.requerimientos = requerimientos;
    }

    
    public Mision(dbDelegate con) {
        this.conexion= con;
        dialogo = new dialogo_mision(con);
        requerimientos = new ObjetoMision(con);
        
    }

    public dialogo_mision getDialogo() {
        return dialogo;
    }

    public void setDialogo(dialogo_mision dialogo) {
        this.dialogo = dialogo;
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
    public void cargarDatos(){
        this.dialogo.cargarDialogos(this.idMision);
        this.requerimientos.cargarRequerimientos(this.idMision);
    }
//    public void setMision(short id) {
////        this.conexion = new dbDelegate();
//        String StrSql = "SELECT * FROM mision "
//                + "WHERE id = " + id;
//        try {
//            ResultSet res = conexion.Consulta(StrSql);
//            if (res.next()) {
//                this.setDescripcion(res.getString("descripcion"));
//                this.setNombre(res.getString("nombre"));
//                this.setIdMision(res.getShort("id"));
//                this.setIdPersonajeConcluyeMision(res.getShort("personaje_id"));
//                this.setNivelRequerido(res.getShort("nivelrequerido"));
//                if (res.getShort("repetible") == 0) bool = false;
//                else bool = true;
//                this.setRepetible(bool);
//                this.setRecompensaExp(res.getShort("recompensaexp"));
//
//            }
////            this.conexion.cierraDbCon();
//        } catch (Exception ex) {
//            System.out.println("Problemas en: clase->Mision, método->setMision() " + ex);
//        }
//        this.dialogo.cargarDialogos(this.idMision);
//        this.requerimientos.cargarRequerimientos(this.idMision);
//    }
}
