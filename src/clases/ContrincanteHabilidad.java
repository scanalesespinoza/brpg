/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author gerald
 */
public class ContrincanteHabilidad {

    private short idPersonaje;
    private HashMap<Short, UnaHabilidad> habilidades;//contiene las habilidades del personaje
    private dbDelegate conexion;

    public short getIdPersonaje() {
        return idPersonaje;
    }

    public void setIdPersonaje(short idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    /*
     * Carga las habilidades desde la base de datos asociadas al personaje
     * las deja en el arreglo "habilidades"
     */
    public void cargarHabilidades(Short id) {
        this.conexion = new dbDelegate();
        System.out.println("Inicio obtiene datos personaje");
        String StrSql = "SELECT * FROM contrincante_habilidad "
                + "WHERE personaje_id = " + id;
        try {
            ResultSet res = conexion.Consulta(StrSql);
            System.out.println(StrSql);
            byte i = 0;
            while (res.next()) {
                UnaHabilidad habilidad = new UnaHabilidad();
                habilidad.setIdHabilidad(res.getShort("habilidad_id"));
                habilidad.setIdPersonaje(res.getShort("personaje_id"));
                habilidad.setNivelHabilidad(res.getShort("nivelhabilidad"));
                habilidad.setNewHabilidad(false);
                this.getHabilidades().put(habilidad.getIdHabilidad(), habilidad);
                i += 1;
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->ContrincanteHabilidad , método->cargarHabilidades() " + ex);
        }

    }

    /**
     * elimina, inserta y actualiza en la base de datos según los datos que tenga
     * en el hashmap
     */
    public void salvarHabilidades() {
        bdUpdates();
        bdInserts();
    }

    /**
     * actualiza en la base de datos si newHabilidad es false
     */
    private void bdUpdates() {
        this.conexion = new dbDelegate();
        Iterator it = this.getHabilidades().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            UnaHabilidad hab = this.getHabilidad(Short.parseShort(e.getKey().toString()));
            if (!hab.isNewHabilidad()) {
                String StrSql = "UPDATE contrincante_habilidad"
                        + " SET nivelhabilidad= " + hab.getNivelHabilidad() + ","
                        + " WHERE personaje_id = " + this.getIdPersonaje()
                        + "   AND objeto_id = " + hab.getIdHabilidad();
                conexion.Ejecutar(StrSql);
            }
        }
    }

    /**
     * se ingresa a la base de datos si newHabilidad == false
     */
    private void bdInserts() {
        this.conexion = new dbDelegate();
        Iterator it = this.getHabilidades().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            UnaHabilidad hab = this.getHabilidad(Short.parseShort(e.getKey().toString()));
            if (hab.isNewHabilidad()) {
                String StrSql = "INSERT INTO contrincante_habilidad VALUES("
                        + this.getIdPersonaje() + ","
                        + hab.getIdHabilidad() + ","
                        + hab.getNivelHabilidad() + ")";
                conexion.Ejecutar(StrSql);
            }
        }
    }
//    /*
//     * Aumenta el nivel de la habilidad del personaje
//     */
//
//    public void aumentaNivelHabilidad(short idHabilidad) {
//        this.conexion = new dbDelegate();
//        System.out.println("Inicio aumentaHabilidad");
//        String StrSql = "Update contrincante_habilidad "
//                + "SET nivelHabilidad = nivelHabilidad+1 "
//                + "WHERE personaje_id = " + idPersonaje + "AND"
//                + "habilidad_id = " + idHabilidad;
//        conexion.Ejecutar(StrSql);
//    }
    /**
     * Agrega una habilidad al personaje, para considerarse activa debe tener un
     * nivel  mayor que 0
     * @param idhabilidad
     */
    public void agregaHabilidad(short idhabilidad) {
        if (!tieneHabilidad(idhabilidad)){
            UnaHabilidad hab  = new UnaHabilidad(idhabilidad, (short) 1, true);
            this.getHabilidades().put(idhabilidad, hab);
        }
    }
    /*
     * Aumenta el nivel de una habilidad
     */
    public void aumentarNivel(short idHabilidad) {
        if (tieneHabilidad(idHabilidad)) {
            aumentarNivel(idHabilidad);
        }
    }

    public boolean tieneHabilidad(short idHabilidad) {
        return this.getHabilidades().containsKey(idHabilidad);
    }
//    /*
//     * Agrega nuevas habilidades al personaje segun el nivel adquirido
//     */
//
//    public void agregaNuevaHabilidad(short nivelPersonaje) {
//        this.conexion = new dbDelegate();
//        String StrSqlUno = new String();
//        String StrSqlDos = new String();
//        if (nivelPersonaje == 3) {
//            StrSqlUno = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",3,1)";
//            StrSqlDos = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",4,1)";
//        } else if (nivelPersonaje == 7) {
//            StrSqlUno = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",5,1)";
//            StrSqlDos = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",6,1)";
//        } else if (nivelPersonaje == 12) {
//            StrSqlUno = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",7,1)";
//            StrSqlDos = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",8,1)";
//        } else if (nivelPersonaje == 18) {
//            StrSqlUno = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",9,1)";
//            StrSqlDos = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",10,1)";
//        } else if (nivelPersonaje == 25) {
//            StrSqlUno = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",3,1)";
//            StrSqlDos = "INSERT INTO contrincante_habilidad VALUES (" + idPersonaje + ",4,1)";
//        }
//        conexion.Ejecutar(StrSqlUno);
//        conexion.Ejecutar(StrSqlDos);
//    }

    private HashMap<Short, UnaHabilidad> getHabilidades() {
        return habilidades;
    }

    private UnaHabilidad getHabilidad(short idHabilidad) {
        return this.getHabilidades().get(idHabilidad);
    }

    private class UnaHabilidad {

        private short idPersonaje;
        private short idHabilidad;
        private short nivelHabilidad;
        private boolean newHabilidad;

        public UnaHabilidad(short idHabilidad, short nivelHabilidad, boolean newHabilidad) {
            this.idHabilidad = idHabilidad;
            this.nivelHabilidad = nivelHabilidad;
            this.newHabilidad = newHabilidad;
        }

        public UnaHabilidad() {
            
        }

        public short getIdHabilidad() {
            return idHabilidad;
        }

        public void setIdHabilidad(short idHabilidad) {
            this.idHabilidad = idHabilidad;
        }

        public short getIdPersonaje() {
            return idPersonaje;
        }

        public void setIdPersonaje(short idPersonaje) {
            this.idPersonaje = idPersonaje;
        }

        public short getNivelHabilidad() {
            return nivelHabilidad;
        }

        public void setNivelHabilidad(short nivelHabilidad) {
            this.nivelHabilidad = nivelHabilidad;
        }

        public boolean isNewHabilidad() {
            return newHabilidad;
        }

        public void setNewHabilidad(boolean newhabilidad) {
            this.newHabilidad = newhabilidad;
        }

        public void aumentarNivel() {
            if (puedeAumentar()) {
                setNivelHabilidad((short) (getNivelHabilidad() + 1));
            }
        }

        private boolean puedeAumentar() {
            Habilidad hab = new Habilidad();
            hab.setHabilidad(this.idHabilidad);
            if (hab.getNivelMaximo() < this.getNivelHabilidad()) {
                return true;
            } else {
                return false;
            }
        }
    }
}
