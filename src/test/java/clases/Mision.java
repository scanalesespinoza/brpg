/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

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

    
}
