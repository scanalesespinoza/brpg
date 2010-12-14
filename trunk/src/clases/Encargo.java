/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author gerald
 */
public class Encargo {

    private short idPersonaje;
    private dbDelegate conexion;
    private HashMap<Short, UnEncargo> misiones_activas = new HashMap<Short, UnEncargo>();//contiene las misiones del personaje
    private ArrayList<UnEncargo> misiones_finalizadas = new ArrayList<UnEncargo>();
    private ArrayList<UnEncargo> misiones_borrar = new ArrayList<UnEncargo>();

    public Encargo(short idPj) {
        this.idPersonaje = idPj;
    }

    public Encargo(dbDelegate con) {
        this.conexion = con;
    }

    public short getIdPersonaje() {
        return idPersonaje;
    }

    public void setIdPersonaje(short idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    public HashMap<Short, UnEncargo> getMisiones() {
        return misiones_activas;
    }

    public void agregarMision(short idMision, short rol, Mision mis) {
//        if (!this.isHaciendoMision(idMision) && !this.isHizoMision(idMision)) {
        UnEncargo mision = new UnEncargo();
        mision.setIdMision(idMision);
        mision.setRolPersonaje(rol);
        mision.setFechaFin(null);
        mision.setFechaComienzo(getFecha());
        mision.setNewEncargo(true);
        mision.setMision(mis);
        this.getMisiones().put(mision.getIdMision(), mision);

//        }
    }

    /**
     * Elimina una mision, se setean los datos para 
     * que sea eliminada en al base de datos...rol personaje = -1 (invalido)
     * @param idMision
     */
    public void abandonaMision(short idMision) {
        this.getMisiones().get(idMision).setRolPersonaje((short) -1);
        this.misiones_borrar.add(this.getMisiones().get(idMision));
        this.getMisiones().remove(idMision);
    }


    /**
     * completa una mision, dejando
     * @param idMision
     */
    public void completarMision(short idMision) {
        this.misiones_finalizadas.add(this.misiones_finalizadas.size(), this.getMisiones().get(idMision));
        this.misiones_finalizadas.get(this.misiones_finalizadas.size() - 1).setFechaFin(getFecha());
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
        Date c2 = new Date();

        String dia = Integer.toString(c1.get(Calendar.DATE));
        String mes = Integer.toString(c1.get(Calendar.MONTH));
        String annio = Integer.toString(c1.get(Calendar.YEAR));
        String hour = Integer.toString(c2.getHours());
        String minutos = Integer.toString(c2.getMinutes());
        String second = Integer.toString(c2.getSeconds());
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        if (minutos.length() == 1) {
            minutos = "0" + minutos;
        }
        if (second.length() == 1) {
            second = "0" + second;
        }
        String fecha = annio + "/" + mes + "/" + dia + " " + hour + ":" + minutos + ":" + second;
        return fecha;
    }

    /**
     * Deja las msiones colcuidas en un array para mejorar el performance
     * @param id
     */
    private void cargarMisionesFinalizadas(Short id) {
//        this.conexion = new dbDelegate();
        String StrSql = "SELECT * FROM encargo "
                + " WHERE personaje_id = " + id
                + " AND updated_at IS NOT NULL";
        misiones_finalizadas = new ArrayList<UnEncargo>();
        try {
            ResultSet res = conexion.Consulta(StrSql);
            byte i = 0;
            while (res.next()) {
                UnEncargo mision = new UnEncargo();
                mision.setIdMision(res.getShort("mision_id"));
                mision.setIdPersonaje(res.getShort("personaje_id"));
                mision.setFechaComienzo(res.getString("created_at"));
                mision.setRolPersonaje(res.getShort("rolpersonaje"));
                mision.setFechaFin(res.getString("updated_at"));
                mision.setNewEncargo(false);
                mision.setRolPersonaje((short) -1);
                misiones_finalizadas.add(i, mision);
                i += 1;
            }
//            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            System.out.println("Problemas en: clase->Encargo , método->cargarMisionesFinalizadas() " + ex);
        }
    }

    public boolean isHizoMision(short idMision, Mision mision) {
        byte i = 0;
        while (i < this.misiones_finalizadas.size()) {
            if (this.misiones_finalizadas.get(i).getIdMision() == idMision) {
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
        String msj;
        try {
            //seccion de misiones contenidas en el hashmap(misiones vigentes)
//            Iterator it = this.getMisiones().entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry e = (Map.Entry) it.next();
//                UnEncargo mision = (UnEncargo) e.getValue();
//                if (!mision.isNewEncargo() && mision.getRolPersonaje() != -1) {
//                    if (mision.getFechaFin() == null){
//                        msj = "NULL";
//                    } else msj = "'"+mision.getFechaFin()+"'";
//                    //ACA VAN LAS MISIONES
//                    String StrSql = "UPDATE encargo" + "   SET rolpersonaje = " + mision.getRolPersonaje() + "," + "       updated_at = " +msj+ " WHERE personaje_id = " + this.getIdPersonaje() + "   AND mision_id = " + mision.getIdMision() + "    AND created_at = '" + mision.getFechaComienzo() + "'";
//                    System.out.println(StrSql);
//                    mision.setNewEncargo(false);
//                    conexion.Ejecutar(StrSql);
//                    System.out.println("*******************************");
//                }
//            }
            //Seccion de misioens contenidas en el arreglo que representan las misiones
            //que el jugador ya ha hecho
            byte i = 0;
            while (i < misiones_finalizadas.size()) {
                if (!misiones_finalizadas.get(i).isNewEncargo() && misiones_finalizadas.get(i).getRolPersonaje() != -1) {
                    if (misiones_finalizadas.get(i).getFechaFin() == null) {
                        msj = "NULL";
                    } else {
                        msj = "'" + misiones_finalizadas.get(i).getFechaFin() + "'";
                    }
                    //ACA VAN LAS MISIONES QUE NO HAYA TOMADO EN LA SESION Y PERO QUE SI LAS HAYA FINALIZADO
                    String StrSql = "UPDATE encargo" + "   SET rolpersonaje = " + misiones_finalizadas.get(i).getRolPersonaje() + "," + "       updated_At = " + msj + " WHERE personaje_id = " + this.getIdPersonaje() + "   AND mision_id = " + misiones_finalizadas.get(i).getIdMision() + "   AND created_at = '" + misiones_finalizadas.get(i).getFechaComienzo() + "'";
                    conexion.Ejecutar(StrSql);
                    misiones_finalizadas.get(i).setRolPersonaje((short) -1);
                    misiones_finalizadas.get(i).setNewEncargo(false);
                }
                i++;
            }
        } catch (Exception ex) {
            Logger.getLogger(Encargo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void bdInsert() {
        String msj;
        try {
            //seccion de misiones contenidas en el hashmap(misiones vigentes)
            Iterator it = this.getMisiones().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                UnEncargo mision = (UnEncargo) e.getValue();
                if (mision.isNewEncargo() && mision.getRolPersonaje() != -1) {
                    if (mision.getFechaFin() == null) {
                        msj = "NULL";
                    } else {
                        msj = "'" + mision.getFechaFin() + "'";
                    }
                    //ACA VAN LAS MISIONES QUE ...TOME EN LA ULTIMA SESIÓN Y NO HAYAN SIDO TERMINADAS
                    String StrSql = "INSERT INTO encargo " + "   VALUES (" + this.getIdPersonaje() + "," + mision.getIdMision() + ",'" + mision.getFechaComienzo() + "'," + mision.getRolPersonaje() + "," + msj + ")";
                    mision.setNewEncargo(false);
                    conexion.Ejecutar(StrSql);

                }
            }
            //Seccion de misioens contenidas en el arreglo que representan las misiones
            //que el jugador ya ha hecho
            byte i = 0;
            while (i < misiones_finalizadas.size()) {
                if (misiones_finalizadas.get(i).isNewEncargo()) {
                    if (misiones_finalizadas.get(i).getFechaFin() == null) {
                        msj = "NULL";
                    } else {
                        msj = "'" + misiones_finalizadas.get(i).getFechaFin() + "'";
                    }
                    //ACA VAN LAS MISIONES QUE HAYA TOMADO Y TERMINADO DURANTE LA ULTIMA SESIÓN
                    String StrSql = "INSERT INTO encargo " + "   VALUES (" + this.getIdPersonaje() + "," + misiones_finalizadas.get(i).getIdMision() + ",'" + misiones_finalizadas.get(i).getFechaComienzo() + "'," + misiones_finalizadas.get(i).getRolPersonaje() + "," + msj + ")";
                    conexion.Ejecutar(StrSql);
                    misiones_finalizadas.get(i).setRolPersonaje((short) -1);
                    misiones_finalizadas.get(i).setNewEncargo(false);
                }
                i++;
            }
        } catch (Exception ex) {
            Logger.getLogger(Encargo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void bdDelete() {
        try {
            //seccion de misiones contenidas en el hashmap(misiones vigentes)
            for (int i = 0; i < misiones_borrar.size(); i++) {
                UnEncargo mision = (UnEncargo) this.misiones_borrar.get(i);
                //ACA VAN LAS MISIONES QUE NO HAYA TOMADO EN LA ULTIMA SESIÓN
                //PERO SI LA HAYA ELIMINADO (SIN TERMINARLA)
                String StrSql = "DELETE FROM encargo" + " WHERE personaje_id = " + this.getIdPersonaje() + "   AND mision_id = " + mision.getIdMision() + "   AND created_at = '" + mision.getFechaComienzo() + "'";
                conexion.Ejecutar(StrSql);
            }
            this.misiones_borrar = new ArrayList<UnEncargo>();

//            Iterator it = this.getMisiones().entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry e = (Map.Entry) it.next();
//                UnEncargo mision = (UnEncargo) e.getValue();
//                System.out.println("MISION :" + mision.getMision().getNombre());
//                if (!mision.isNewEncargo() && mision.getRolPersonaje() == -1) {
//                    //ACA VAN LAS MISIONES QUE NO HAYA TOMADO EN LA ULTIMA SESIÓN
//                    //PERO SI LA HAYA ELIMINADO (SIN TERMINARLA)
//                    String StrSql = "DELETE FROM encargo" + " WHERE personaje_id = " + this.getIdPersonaje() + "   AND mision_id = " + mision.getIdMision() + "   AND created_at = '" + mision.getFechaComienzo() + "'";
//                    System.out.println(StrSql);
//                    conexion.Ejecutar(StrSql);
//                    System.out.println("222222222222222222222222222222222222222222222222222");
//                    getMisiones().remove((Short) (e.getKey()));
//                }
//            }
        } catch (Exception ex) {
            Logger.getLogger(Encargo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void cargarMisiones(short id, HashMap<Short, Mision> misiones) {
        this.setIdPersonaje(id);
        cargarMisionesFinalizadas(id);
        cargarMisionesVigentes(id, misiones);
        this.misiones_borrar = new ArrayList<UnEncargo>();
    }

    private void cargarMisionesVigentes(short id, HashMap<Short, Mision> misiones) {
//        this.conexion = new dbDelegate();
        String StrSql = "SELECT * FROM encargo "
                + " WHERE personaje_id = " + id
                + " AND updated_at IS NULL";

        this.misiones_activas = new HashMap<Short, UnEncargo>();
        try {
            ResultSet res = conexion.Consulta(StrSql);
            while (res.next()) {
                UnEncargo mision = new UnEncargo();
                mision.setIdMision(res.getShort("mision_id"));
                mision.setIdPersonaje(res.getShort("personaje_id"));
                mision.setFechaComienzo(res.getString("created_at"));
                mision.setRolPersonaje(res.getShort("rolpersonaje"));
                mision.setFechaFin(null);
                mision.setNewEncargo(false);
                mision.setMision(misiones.get(mision.getIdMision()));
                this.misiones_activas.put(mision.getIdMision(), mision);

            }
//            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            System.out.println("Problemas en: clase->Encargo , método->cargarMisionesVigentes() " + ex);
        }
    }

    public class UnEncargo {

        private short idPersonaje;
        private short idMision;
        private String fechaComienzo;
        private short rolPersonaje;
        private String fechaFin;
        private boolean newEncargo;
        private Mision mision = new Mision(conexion);

        public Mision getMision() {
            return mision;
        }

        public void setMision(Mision mision) {
            this.mision = mision;
        }

        public boolean isNewEncargo() {
            return newEncargo;
        }

        public void setNewEncargo(boolean newEncargo) {
            this.newEncargo = newEncargo;
        }

        public String getFechaComienzo() {
            return fechaComienzo;
        }

        public void setFechaComienzo(String fechaComienzo) {
            this.fechaComienzo = fechaComienzo;
        }

        public String getFechaFin() {
            return fechaFin;
        }

        public void setFechaFin(String fechaFin) {
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
