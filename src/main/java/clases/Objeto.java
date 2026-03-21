/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerald
 */
public class Objeto {

    private Short idObjeto;
    private String nombre;
    private String descripcion;

    private int tipo;

    private short peso;
    private short valorDinero;
    private boolean usoCombate;
    private int beneficio;

    private String nombreGrafico;
    private dbDelegate conexion;

    /**
     * Busca los valores en la base de datos y los mapea para que queden disponible
     * de manera objetual para el sistema
     * 
     */
//    public void setObjeto(short id) {
//        this.conexion = new dbDelegate();
//        String StrSql = "SELECT * FROM objeto obj"
//                + " WHERE obj.id = " + id;
//
//        try {
//            ResultSet res = conexion.Consulta(StrSql);
//            if (res.next()) {
//                this.setDescripcion(res.getString("descripcion"));
//                this.setNombre(res.getString("nombre"));
//                this.setIdObjeto(res.getShort("id"));
//                this.setPeso(res.getShort("peso"));
//                this.setTipo(res.getInt("tipo"));
//                this.setBeneficio(res.getInt("beneficio"));
//                if(res.getInt("usocombate")==0){
//                    this.setUsoCombate(false);
//                }else{
//                    this.setUsoCombate(true);
//                }
//
//                this.setValorDinero(res.getShort("valordinero"));
//                this.setNombreGrafico(res.getString("nom_grafico"));
//
//            }
//            this.conexion.cierraDbCon();
//        } catch (Exception ex) {
//            Logger.getLogger(Objeto.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("Problemas en: clase->Objeto , método->setObjeto() " + ex);
//        }
//
//    }

    public int getBeneficio() {
        return beneficio;
    }

    public void setBeneficio(int beneficio) {
        this.beneficio = beneficio;
    }

    
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
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
