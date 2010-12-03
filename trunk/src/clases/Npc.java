/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

/*import extensiones.StdDungeonMonster;
import java.sql.ResultSet;*/
import java.sql.SQLException;
import java.util.Calendar;
//import java.util.HashMap;
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
    private dbDelegate conect = new dbDelegate();
    public Calendar c1;
    public boolean cargado = false;
    private Inventario[] inv;

    public Npc(double x, double y, String name, String mediaName, int colId, int tamano, short idNpc, String[] dialogo) throws SQLException {
        super(name, true, x, y, colId, mediaName, tamano);
        this.idNpc = idNpc;
        this.grafNpc = mediaName;
        this.nomNpc = name;
        this.colId = colId;
        this.dialogo = new dialogo_personaje();
    }

    public Npc(double x, double y, String name, String mediaName, int tamano, short idNpc, short nivel, short tipo, String[] dialogo) throws SQLException {
        super(x, y, 1, idNpc, name, mediaName, nivel, tipo, (int) Math.pow(2, 3));
        this.dialogo = new dialogo_personaje();
        this.idNpc = idNpc;
        this.grafNpc = mediaName;
        this.nomNpc = name;
    }

    public Npc() {
        this.dialogo = new dialogo_personaje();
    }

    public String obtieneDialogo() {
        return dialogo.getParrafo() +  " y mi nombre eh "+this.getNombre();
    }
    public void comerciar() {
    }

    public void misionar() {
    }

    public void dialogar() {
    }

    @Override
    public void hit(JGObject obj) {
        //System.out.println("Hit NPC!"+obj.getName());
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
    public void cargarDatos(Short id) {
        super.cargarDatos(id);
        dialogo.cargarDialogos(this.getIdPersonaje());
    }

    public dialogo_personaje getDialogo() {
        return dialogo;
    }

    public void setDialogo(dialogo_personaje dialogo) {
        this.dialogo = dialogo;
    }


}
