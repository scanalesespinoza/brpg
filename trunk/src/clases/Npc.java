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

    private short idNpc;
    private String grafNpc;
    private String nomNpc;
    private int colId;
    public String[] dialogo;
    private dbDelegate conect = new dbDelegate();
    public Calendar c1;
    public boolean cargado = false;
    private Inventario[] inv;

    public Npc(double x, double y, String name, String mediaName, int colId, int tamano, short idNpc, String[] dialogo) throws SQLException {
        super(name, true, x, y, colId, mediaName, tamano);
        this.dialogo = dialogo;
        this.idNpc = idNpc;
        this.grafNpc = mediaName;
        this.nomNpc = name;
        this.colId = colId;
        System.out.println("contruye NPC");
        //cargaInventario(idNpc);
        System.out.println("Inventario Npc Cargado");
    }

    public Npc(double x, double y, String name, String mediaName, int tamano, short idNpc, short nivel, short tipo, String[] dialogo) throws SQLException {
        super(x, y, 1, idNpc, name, mediaName, nivel, tipo, (int) Math.pow(2, 3));
        this.dialogo = dialogo;
        this.idNpc = idNpc;
        this.grafNpc = mediaName;
        this.nomNpc = name;
        System.out.println("contruye NPC " + name);
    }

    public Npc() {
    }

    public String[] obtieneDialogo() {
        /*
        String[] dialogo = {"Hola amigo",
        "como estas",
        "Me doy cuenta que no eres de estos lados",
        "te advierto que es muy peligroso salir de la cuidad",
        "hay muchas criaturas peligrosas",
        "si no tienes donde ir puedes quedarte",
        "mientras ayudes a mantener la cuidad en pie",
        "todos te aceptaran sin problema",
        "pensadolo hay mucho trabajo que hacer",
        "acomapañame a la plaza y danos una mano",
        "podras conocer al resto de la gente."
        };
         * 
         */

        return dialogo;
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
    public void move(){
       
    }

}
