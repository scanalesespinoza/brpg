/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

/**
 *
 * @author gerald
 */
public class Objeto {
private Short idObjeto;

    private String nombre;

    private String descripcion;

    private boolean tipo;

    private short peso;

    private short valorDinero;

    private boolean usoCombate;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(Short idObjeto) {
        this.idObjeto = idObjeto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getPeso() {
        return peso;
    }

    public void setPeso(short peso) {
        this.peso = peso;
    }

    public boolean isTipo() {
        return tipo;
    }

    public void setTipo(boolean tipo) {
        this.tipo = tipo;
    }

    public boolean isUsoCombate() {
        return usoCombate;
    }

    public void setUsoCombate(boolean usoCombate) {
        this.usoCombate = usoCombate;
    }

    public short getValorDinero() {
        return valorDinero;
    }

    public void setValorDinero(short valorDinero) {
        this.valorDinero = valorDinero;
    }

    
}
