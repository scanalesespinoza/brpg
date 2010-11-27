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
import java.util.Random;

/**
 *
 * @author gerald
 */
public class ContrincanteHabilidad {

    private short idPersonaje;
    private HashMap<Short, UnaHabilidad> habilidades;//contiene las habilidades del personaje
    private dbDelegate conexion;


    public ContrincanteHabilidad() {
        this.habilidades = new HashMap<Short, UnaHabilidad>();
       
    }

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
        System.out.println(StrSql);
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
                Habilidad hab = new Habilidad();
                hab.setHabilidad(habilidad.getIdHabilidad());
                
                habilidad.setHabilidad(hab);
                
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
            UnaHabilidad habi = this.getHabilidad(Short.parseShort(e.getKey().toString()));
            if (!habi.isNewHabilidad()) {
                String StrSql = "UPDATE contrincante_habilidad"
                        + " SET nivelhabilidad= " + habi.getNivelHabilidad() + ","
                        + " WHERE personaje_id = " + this.getIdPersonaje()
                        + "   AND objeto_id = " + habi.getIdHabilidad();
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
            UnaHabilidad habi = this.getHabilidad(Short.parseShort(e.getKey().toString()));
            if (habi.isNewHabilidad()) {
                String StrSql = "INSERT INTO contrincante_habilidad VALUES("
                        + this.getIdPersonaje() + ","
                        + habi.getIdHabilidad() + ","
                        + habi.getNivelHabilidad() + ")";
                conexion.Ejecutar(StrSql);
            }
        }
    }

    /**
     * Agrega una habilidad al personaje, para considerarse activa debe tener un
     * nivel  mayor que 0
     * @param idhabilidad
     */
    public void agregaHabilidad(short idhabilidad) {
        if (!tieneHabilidad(idhabilidad)) {
            UnaHabilidad habi = new UnaHabilidad(idhabilidad, (short) 1, true);
            this.getHabilidades().put(idhabilidad, habi);
        }
    }
    /*
     * Aumenta el nivel de una habilidad
     */

    public void aumentarNivel(short idHabilidad) {
        if (tieneHabilidad(idHabilidad)) {
            this.habilidades.get(idHabilidad).aumentarNivel();
            
        }
    }

    public boolean tieneHabilidad(short idHabilidad) {
        return this.getHabilidades().containsKey(idHabilidad);
    }

    public HashMap<Short, UnaHabilidad> getHabilidades() {
        return habilidades;
    }

    public UnaHabilidad getHabilidad(short idHabilidad) {
        return this.getHabilidades().get(idHabilidad);
    }

    public Integer getCosto(short idHabilidad) {
        Short nivel = this.getHabilidades().get(idHabilidad).getNivelHabilidad();
        return (nivel * getHabilidad(idHabilidad).getHabilidad().getCostoBasico()) / 2;
    }

    public Integer getDañoBeneficio(short idHabilidad) {
        Short nivel = this.getHabilidades().get(idHabilidad).getNivelHabilidad();
        return Math.round((int) (getHabilidad(idHabilidad).getHabilidad().getDanoBeneficio() * (1 + (0.1 * nivel))));
    }

    public int getTiempoEspera(short idHabilidad) {
        return getHabilidad(idHabilidad).getHabilidad().getTiempoEspera();
    }

    public short getHabilidadAlAzar() {
        short result = -1;
        if (this.getHabilidades().size() > 0) {
            System.out.println("ENTRE A SACAR HABILIDAD ");
            Random tope = new Random();
            int num = tope.nextInt(this.getHabilidades().size()+1);
            int i = 0;
            Iterator it = this.getHabilidades().entrySet().iterator();
            while (it.hasNext() && i < num) {
                Map.Entry e = (Map.Entry) it.next();
                i++;
                result = Short.parseShort(e.getKey().toString());
            }
        }
        return result;
    }

    public class UnaHabilidad {

        private short idPersonaje;
        private short idHabilidad;
        private short nivelHabilidad;
        private boolean newHabilidad;
        private Habilidad habilidad = new Habilidad();

        public UnaHabilidad(short idHabilidad, short nivelHabilidad, boolean newHabilidad) {
            this.idHabilidad = idHabilidad;
            this.nivelHabilidad = nivelHabilidad;
            this.newHabilidad = newHabilidad;
            this.habilidad.setHabilidad(idHabilidad);
        }

        public Habilidad getHabilidad() {
            return habilidad;
        }

        public void setHabilidad(Habilidad habilidad) {
            this.habilidad = habilidad;
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

        public boolean puedeAumentar() {
            Habilidad hab = this.getHabilidad();
            if (hab.getNivelMaximo() > this.getNivelHabilidad()) {
                System.out.println("SI puede agregar");
                return true;
            } else {
                System.out.println("no puede agregar");
                return false;
            }
        }

    }
}
