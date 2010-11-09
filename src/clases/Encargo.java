/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author gerald
 */
public class Encargo {

    private short idPersonaje;
    private dbDelegate conexion;
    private HashMap<Short, UnEncargo> misiones_activas;//contiene las misiones del personaje
    private UnEncargo[] misiones_finalizadas;

    public Encargo(short idPj) {
        this.idPersonaje = idPj;
    }

    public Encargo() {
    }

    public short getIdPersonaje() {
        return idPersonaje;
    }

    public void setIdPersonaje(short idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    private HashMap<Short, UnEncargo> getMisiones() {
        return misiones_activas;
    }

    /**
     * Carga las misiones desde la base de datos asociados al personaje
     * las deja en el arreglo "objetos"
     * @param id
     */
    public void cargarMisiones(Short id) {
        cargarMisionesFinalizadas(id);
        cargarMisionesVigentes(id);
    }

    public void agregarMision(short idMision, short rol) {
        if (!this.isHaciendoMision(idMision) && !this.isHizoMision(idMision)) {
            UnEncargo mision = new UnEncargo();
            mision.setIdMision(idMision);
            mision.setRolPersonaje(rol);
            mision.setFechaFin(null);
            mision.setFechaComienzo(Date.valueOf(getFecha()));
            mision.setNewEncargo(true);
            this.getMisiones().put(mision.getIdMision(), mision);
        }
    }

    /**
     * Elimina una mision, se setean los datos para 
     * que sea eliminada en al base de datos
     * @param idMision
     */
    public void abandonaMision(short idMision) {
        this.getMisiones().get(idMision).setRolPersonaje((short) -1);
    }

    /**
     * completa una mision, dejando
     * @param idMision
     */
    public void completarMision(short idMision) {
        this.misiones_finalizadas[this.misiones_finalizadas.length] = this.getMisiones().get(idMision);
        this.misiones_finalizadas[this.misiones_finalizadas.length].setFechaFin(Date.valueOf(getFecha()));
        this.getMisiones().remove(idMision);
    }

    /**
     * Verifica si el personaje tiene una mision
     * @param idMision
     * @return
     */
    public boolean isHaciendoMision(short idMision) {
        return this.getMisiones().containsKey(idMision)
                && this.getMisiones().get(idMision).getRolPersonaje() != -1;
    }

    public String getFecha() {
        Calendar c1 = Calendar.getInstance();
        String dia = Integer.toString(c1.get(Calendar.DATE));
        String mes = Integer.toString(c1.get(Calendar.MONTH));
        String annio = Integer.toString(c1.get(Calendar.YEAR));
        String hour = Integer.toString(Calendar.HOUR);
        String second = Integer.toString(Calendar.SECOND);
        String minutos = Integer.toString(Calendar.MINUTE);
        String fecha = annio + "/" + mes + "/" + dia + " " + hour + ":" + minutos + ":" + second;
        return fecha;
    }

    /**
     * Deja las msiones colcuidas en un array para mejorar el performance
     * @param id
     */
    private void cargarMisionesFinalizadas(Short id) {
        this.conexion = new dbDelegate();
        String StrSql = "SELECT * FROM encargo "
                + " WHERE personaje_id = " + id
                + " AND updated_at IS NOT NULL";
        System.out.println(StrSql);
        try {
            ResultSet res = conexion.Consulta(StrSql);
            byte i = 0;
            while (res.next()) {
                UnEncargo mision = new UnEncargo();
                mision.setIdMision(res.getShort("mision_id"));
                mision.setIdPersonaje(res.getShort("personaje_id"));
                mision.setFechaComienzo(res.getDate("created_at"));
                mision.setRolPersonaje(res.getShort("rolpersonaje"));
                mision.setFechaFin(res.getDate("fechaFin"));
                mision.setNewEncargo(false);
                misiones_finalizadas[i] = mision;
                i += 1;
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->Encargo , método->cargarMisionesFinalizadas() " + ex);
        }
    }

    /**
     * Deja las misiones sin fecha de termino en un hashmap
     * para obtener mejor performace
     * @param id
     */
    private void cargarMisionesVigentes(Short id) {
        this.conexion = new dbDelegate();
        String StrSql = "SELECT * FROM encargo "
                + " WHERE personaje_id = " + id
                + " AND updated_at IS NULL";
        System.out.println(StrSql);
        try {
            ResultSet res = conexion.Consulta(StrSql);
            while (res.next()) {
                UnEncargo mision = new UnEncargo();
                mision.setIdMision(res.getShort("mision_id"));
                mision.setIdPersonaje(res.getShort("personaje_id"));
                mision.setFechaComienzo(res.getDate("created_at"));
                mision.setRolPersonaje(res.getShort("rolpersonaje"));
                mision.setFechaFin(null);
                mision.setNewEncargo(false);
                this.misiones_activas.put(mision.getIdMision(), mision);
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->Encargo , método->cargarMisionesVigentes() " + ex);
        }


    }

    private boolean isHizoMision(short idMision) {
        byte i = 0;
        while (i < this.misiones_finalizadas.length) {
            if (this.misiones_finalizadas[i].getIdMision() == idMision) {
                Mision mision = new Mision();
                mision.setMision(idMision);
                if (mision.isRepetible()) {
                    return false;
                } else {
                    return true;
                }
            }

            i++;
        }
        return false;
    }

    /**
     * Realiza las gestiones necesarias para
     * grabar los registros en a base de datos
     *
     */
    public void salvarEncargo() {
        bdUpdates();
        bdInsert();
        bdDelete();
    }

    private void bdUpdates() {
        //seccion de misiones contenidas en el hashmap(misiones vigentes)
        this.conexion = new dbDelegate();
        Iterator it = this.getMisiones().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            UnEncargo mision = (UnEncargo) e.getValue();
            if (!mision.isNewEncargo() && mision.getRolPersonaje() != -1) {
                String StrSql = "UPDATE Encargo"
                        + "   SET rolpersonaje = " + mision.getRolPersonaje() + ","
                        + "       updated_At = " + mision.getFechaFin()
                        + " WHERE personaje_id = " + this.getIdPersonaje()
                        + "   AND mision_id = " + mision.getIdMision()
                        + "    AND created_at = '" + mision.getFechaComienzo() + "'";
                conexion.Ejecutar(StrSql);
            }
        }
        //Seccion de misioens contenidas en el arreglo que representan las misiones
        //que el jugador ya ha hecho
        byte i = 0;
        while (i < misiones_finalizadas.length) {
            if (!misiones_finalizadas[i].isNewEncargo()) {
                String StrSql = "UPDATE Encargo"
                        + "   SET rolpersonaje = " + misiones_finalizadas[i].getRolPersonaje() + ","
                        + "       updated_At = " + misiones_finalizadas[i].getFechaFin()
                        + " WHERE personaje_id = " + this.getIdPersonaje()
                        + "   AND mision_id = " + misiones_finalizadas[i].getIdMision()
                        + "   AND created_at = '" + misiones_finalizadas[i].getFechaComienzo() + "'";
                conexion.Ejecutar(StrSql);
            }
            i++;
        }
    }

    private void bdInsert() {
        //seccion de misiones contenidas en el hashmap(misiones vigentes)
        this.conexion = new dbDelegate();
        Iterator it = this.getMisiones().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            UnEncargo mision = (UnEncargo) e.getValue();
            if (mision.isNewEncargo() && mision.getRolPersonaje() != -1) {
                String StrSql = "INSERT INTO Encargo "
                        + "   VALUES (" + this.getIdPersonaje() + ","
                        + mision.getIdMision() + ","
                        + mision.getFechaComienzo() + ","
                        + mision.getRolPersonaje() + ","
                        + mision.getFechaFin()
                        + ")";
                conexion.Ejecutar(StrSql);
            }
        }
        //Seccion de misioens contenidas en el arreglo que representan las misiones
        //que el jugador ya ha hecho
        byte i = 0;
        while (i
                < misiones_finalizadas.length) {
            if (misiones_finalizadas[i].isNewEncargo()) {
               String StrSql = "INSERT INTO Encargo "
                        + "   VALUES (" + this.getIdPersonaje() + ","
                        + misiones_finalizadas[i].getIdMision() + ","
                        + misiones_finalizadas[i].getFechaComienzo() + ","
                        + misiones_finalizadas[i].getRolPersonaje() + ","
                        + misiones_finalizadas[i].getFechaFin()
                        + ")";
                conexion.Ejecutar(StrSql);
            }
            i++;
        }
    }

    private void bdDelete(){
        //seccion de misiones contenidas en el hashmap(misiones vigentes)
        this.conexion = new dbDelegate();
        Iterator it = this.getMisiones().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            UnEncargo mision = (UnEncargo) e.getValue();
            if (!mision.isNewEncargo() && mision.getRolPersonaje() == -1) {
                String StrSql = "DELETE FROM Encargo"
                        + " WHERE personaje_id = " + this.getIdPersonaje()
                        + "   AND mision_id = " + mision.getIdMision()
                        + "   AND created_at = '" + mision.getFechaComienzo() + "'";
                conexion.Ejecutar(StrSql);
            }
        }
    }
    private class UnEncargo {

        private short idPersonaje;
        private short idMision;
        private Date fechaComienzo;
        private short rolPersonaje;
        private Date fechaFin;
        private boolean newEncargo;

        public boolean isNewEncargo() {
            return newEncargo;
        }

        public void setNewEncargo(boolean newEncargo) {
            this.newEncargo = newEncargo;
        }

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

        public short getRolPersonaje() {
            return rolPersonaje;
        }

        public void setRolPersonaje(short rolPersonaje) {
            this.rolPersonaje = rolPersonaje;
        }
    }
}
