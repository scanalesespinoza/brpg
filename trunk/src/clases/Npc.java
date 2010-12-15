/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/*import extensiones.StdDungeonMonster;
import java.sql.ResultSet;*/
import clases.Encargo.UnEncargo;
import java.sql.SQLException;
import java.util.Calendar;
//import java.util.HashMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import jgame.*;

/**
 *
 * @author Iarwain
 */
public class Npc extends Personaje {

    private dialogo_personaje dialogo;
    private short idNpc;
    private String grafNpc;
    private String nomNpc;
    private int colId;
    public Calendar c1;
    public boolean cargado = false;
    

    public Npc(double x, double y, String name, String mediaName, int colId, int tamano, short idNpc,dbDelegate conn, String[] dialogo) throws SQLException {
        super(name, true, x, y, colId, mediaName, tamano,null);
        this.idNpc = idNpc;
        this.grafNpc = mediaName;
        this.nomNpc = name;
        this.colId = colId;
        this.dialogo = new dialogo_personaje(conn);
        
    }

    public Npc(double x, double y, String name, String mediaName, int tamano, short idNpc, short nivel, short tipo,  dbDelegate conn,String[] dialogo) throws SQLException {
        super(x, y, 1, idNpc, name, mediaName, nivel, tipo, (int) Math.pow(2, 3),conn);
        this.dialogo = new dialogo_personaje(conn);
        this.idNpc = idNpc;
        this.grafNpc = mediaName;
        this.nomNpc = name;
    }

    public Npc(String graf, short idPersonaje, String nombre, short nivel, double posicionX, double posicionY, short tipo,  dbDelegate conn) {
        super(posicionX, posicionY, 1, idPersonaje, nombre, graf, nivel, tipo, (int) Math.pow(2, 3),conn);
        this.dialogo = new dialogo_personaje(conn);
    }

    public Npc(dbDelegate con) {
        super(con);
    }

    public String obtieneDialogo() {
        return dialogo.getParrafo();
    }

    public short getIdNpc() {
        return idNpc;
    }

    public void setIdNpc(short idNpc) {
        this.idNpc = idNpc;
    }

    public String getGrafNpc() {
        return grafNpc;
    }

    public void setGrafNpc(String grafNpc) {
        this.grafNpc = grafNpc;
    }

    public String getNomNpc() {
        return nomNpc;
    }

    public void setNomNpc(String nomNpc) {
        this.nomNpc = nomNpc;
    }

    @Override
    public void move() {
    }

    @Override
    public void cargarDatos(HashMap<Short,Objeto> objetos ,HashMap<Short, Habilidad> habilidades, HashMap<Short,Mision> misiones) {
        super.cargarDatos(objetos,habilidades,misiones);
        dialogo.cargarDialogos(this.getIdPersonaje());
    }

    public dialogo_personaje getDialogo() {
        return dialogo;
    }

    public void setDialogo(dialogo_personaje dialogo) {
        this.dialogo = dialogo;
    }

    /**
     * Solo para personajes del tipo mision
     * Retorna si hay misiones asignadas
     * @return
     */
    public boolean tieneMisiones() {
        if (getTipo() == 2) {
            if (this.getMisiones().getMisiones().size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    @Override
    public void hit(JGObject obj){

    }
}
