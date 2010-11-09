/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

import java.sql.Date;

/**
 *
 * @author gerald
 */
public class Encargo {
private short idPersonaje;

    private short idMision;

    private Date fechaComienzo;
    private boolean rolPersonaje;

    private Date fechaFin;

    public Date getFechaComienzo() {
        return fechaComienzo;
    }

    public void setFechaComienzo(Date fechaComienzo) {
        this.fechaComienzo = fechaComienzo;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public short getIdMision() {
        return idMision;
    }

    public void setIdMision(short idMision) {
        this.idMision = idMision;
    }

    public short getIdPersonaje() {
        return idPersonaje;
    }

    public void setIdPersonaje(short idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    public boolean isRolPersonaje() {
        return rolPersonaje;
    }

    public void setRolPersonaje(boolean rolPersonaje) {
        this.rolPersonaje = rolPersonaje;
    }

    
}
