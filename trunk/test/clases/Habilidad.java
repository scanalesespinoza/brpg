/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clases;

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
