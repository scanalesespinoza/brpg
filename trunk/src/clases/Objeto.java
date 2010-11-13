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
public class Objeto {
private Short idObjeto;

    private String nombre;

    private String descripcion;

    private boolean tipo;

    private short peso;

    private short valorDinero;

    private boolean usoCombate;

    private String nombreGrafico;


    private dbDelegate conexion;


    /*
     * Busca los valores en la base de datos y los mapea para que queden disponible
     * de manera objetual para el sistema
     */
    public void setObjeto(short id){
        this.conexion = new dbDelegate();
        System.out.println("Inicio obtiene datos personaje");
        String StrSql = "SELECT * FROM objeto "+
                        "WHERE id = "+ id;
        System.out.println(StrSql);
        try {
            ResultSet res = conexion.Consulta(StrSql);
            if (res.next()) {
                this.setDescripcion(res.getString("descripcion"));
                this.setNombre(res.getString("nombre"));
                this.setIdObjeto(res.getShort("id"));
                this.setPeso(res.getShort("peso"));
                this.setTipo(Boolean.valueOf(res.getString("tipo")));
                this.setUsoCombate(Boolean.parseBoolean(res.getString("usocombate")));
                this.setValorDinero(res.getShort("valordinero"));
                this.setNombreGrafico(res.getString("nom_grafico"));
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->Objeto , método->setObjeto() " + ex);
        }
    }

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
    public String getNombreGrafico() {
        return nombreGrafico;
    }

    public void setNombreGrafico(String nombreGrafico) {
        this.nombreGrafico = nombreGrafico;
    }

}
