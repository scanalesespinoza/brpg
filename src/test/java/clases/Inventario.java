/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

/**
 *
 * @author gerald
 */
public class Inventario {

    private short idPersonaje;
    private short idObjeto;
    private short cantidad;
    private boolean estaEquipado;

    public short getCantidad() {
        return cantidad;
    }

    public void setCantidad(short cantidad) {
        this.cantidad = cantidad;
    }

    public boolean isEstaEquipado() {
        return estaEquipado;
    }

    public void setEstaEquipado(boolean estaEquipado) {
        this.estaEquipado = estaEquipado;
    }

    public short getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(short idObjeto) {
        this.idObjeto = idObjeto;
    }

    public short getIdPersonaje() {
        return idPersonaje;
    }

    public void setIdPersonaje(short idPersonaje) {
        this.idPersonaje = idPersonaje;
    }


}
