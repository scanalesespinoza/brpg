/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author gerald
 */
public class ObjetoMision {

    private dbDelegate conexion;
    private HashMap<Short, Item> objetos = new HashMap<Short, Item>();//contiene los objetos requeridos por la misión

    public HashMap<Short, Item> getObjetos() {
        return objetos;
    }

    public void setObjetos(HashMap<Short, Item> objetos) {
        this.objetos = objetos;
    }

    
    public void cargarRequerimientos(short id) {
        this.conexion = new dbDelegate();
        String StrSql = "SELECT  * "
                + "  FROM objeto_mision "
                + " WHERE mision_id=" + id;
        try {
            ResultSet res = conexion.Consulta(StrSql);
            byte i = 0;

            while (res.next()) {
                Item item = new Item();
                item.setIdObjeto(res.getShort("objeto_id"));
                item.setCantidad(res.getShort("cantidad"));
                this.objetos.put(item.getIdObjeto(), item);
                i += 1;
               
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->Inventario , método->cargarInventario() " + ex);
        }


    }

    public class Item {

        private short idObjeto;
        private short cantidad;

        public Item() {
        }

        public Item(short idObjeto, short cantidad) {
            this.idObjeto = idObjeto;
            this.cantidad = cantidad;
        }

        public short getCantidad() {
            return cantidad;
        }

        public void setCantidad(short cantidad) {
            this.cantidad = cantidad;
        }

        public short getIdObjeto() {
            return idObjeto;
        }

        public void setIdObjeto(short idObjeto) {
            this.idObjeto = idObjeto;
        }
    }
}
