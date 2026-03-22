/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author gerald
 */
public class Mision {

    @JsonProperty("id")
    private Short idMision;
    @JsonProperty("nombre")
    private String nombre;
    @JsonProperty("descripcion")
    private String descripcion;
    @JsonProperty("nivelrequerido")
    private short nivelRequerido;
    @JsonProperty("recompensaexp")
    private short recompensaExp;
    @JsonProperty("recompensadinero")
    private short recompensaDinero;
    @JsonProperty("repetible")
    private boolean repetible;
    @JsonProperty("personaje_id")
    private short idPersonajeConcluyeMision;
    @JsonIgnore
    private dbDelegate conexion;
    @JsonIgnore
    private dialogo_mision dialogo;
    @JsonIgnore
    private ObjetoMision requerimientos;
    @JsonIgnore
    private boolean bool;

    public Mision() {
    }

    public void init(dbDelegate con) {
        this.conexion = con;
        dialogo = new dialogo_mision(con);
        requerimientos = new ObjetoMision(con);
    }

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
