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
public class Jugador {
private Short idPersonaje;

    private short vitalidad;

    private short destreza;

    private short sabiduria;

    private short fuerza;

    private short totalPuntosHabilidad;

    private short totalPuntosEstadistica;

    private int limiteSuperiorExperiencia;

    private int experiencia;

    private int pesoSoportado;

    private Date fechaCreacion;

    private boolean esBaneado;

    private short idCuenta;

    public short getDestreza() {
        return destreza;
    }

    public void setDestreza(short destreza) {
        this.destreza = destreza;
    }

    public boolean isEsBaneado() {
        return esBaneado;
    }

    public void setEsBaneado(boolean esBaneado) {
        this.esBaneado = esBaneado;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public short getFuerza() {
        return fuerza;
    }

    public void setFuerza(short fuerza) {
        this.fuerza = fuerza;
    }

    public short getIdCuenta() {
        return idCuenta;
    }

    public void setIdCuenta(short idCuenta) {
        this.idCuenta = idCuenta;
    }

    public Short getIdPersonaje() {
        return idPersonaje;
    }

    public void setIdPersonaje(Short idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    public int getLimiteSuperiorExperiencia() {
        return limiteSuperiorExperiencia;
    }

    public void setLimiteSuperiorExperiencia(int limiteSuperiorExperiencia) {
        this.limiteSuperiorExperiencia = limiteSuperiorExperiencia;
    }

    public int getPesoSoportado() {
        return pesoSoportado;
    }

    public void setPesoSoportado(int pesoSoportado) {
        this.pesoSoportado = pesoSoportado;
    }

    public short getSabiduria() {
        return sabiduria;
    }

    public void setSabiduria(short sabiduria) {
        this.sabiduria = sabiduria;
    }

    public short getTotalPuntosEstadistica() {
        return totalPuntosEstadistica;
    }

    public void setTotalPuntosEstadistica(short totalPuntosEstadistica) {
        this.totalPuntosEstadistica = totalPuntosEstadistica;
    }

    public short getTotalPuntosHabilidad() {
        return totalPuntosHabilidad;
    }

    public void setTotalPuntosHabilidad(short totalPuntosHabilidad) {
        this.totalPuntosHabilidad = totalPuntosHabilidad;
    }

    public short getVitalidad() {
        return vitalidad;
    }

    public void setVitalidad(short vitalidad) {
        this.vitalidad = vitalidad;
    }

}
