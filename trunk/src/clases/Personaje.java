/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

//import jgame.JGPoint;
import java.sql.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jgame.JGRectangle;
import jgame.JGTimer;

/**
 *
 * @author gerald
 */
public class Personaje extends extensiones.StdDungeon {
    //Variables de juego de Personaje
    private short idPersonaje;
    private String nombre;
    private short nivel;
    private short tipo;
    protected dbDelegate conexion;
    public JGRectangle rClick;
    public boolean estadoClick = false;
    double mouseX;
    double mouseY;
    private double xAnt;
    private double yAnt;
    private double xSpeedAnt;
    private double ySpeedAnt;
    //factor es para convertir el punto del mouse de 800*600 a 1280*960
    //proporcionalmente para que se desplace de forma correcta
    double factor = 1.6;
    private Inventario inventario;
    private ContrincanteHabilidad habilidades;
    private Encargo misiones;
    private Boolean bloqueo;
    private int cidAnt;

    public Personaje(String name, boolean unique_id, double x, double y, int cid, String graphic, int occupy_mask,dbDelegate conn) {
        super(name, unique_id, x, y, cid, graphic, occupy_mask );
        this.inventario = new Inventario(conn);
        this.habilidades = new ContrincanteHabilidad(conn);
        this.misiones = new Encargo(conn);
        this.bloqueo = false;
        this.conexion = conn;
    }

    public Personaje(double x, double y, double speed, short idPersonaje, String nombre, String graf, short nivel, short tipo, int cid,dbDelegate conn) {
        super(nombre, x, y, cid, graf, true, false,
                16, 32, speed);
        stopAnim();
        this.idPersonaje = idPersonaje;
        this.nombre = nombre;
        this.nivel = nivel;
        this.tipo = tipo;
        this.inventario = new Inventario(conn);
        this.habilidades = new ContrincanteHabilidad(conn);
        this.misiones = new Encargo(conn);
        this.bloqueo = false;
        this.conexion = conn;
    }

    public Personaje(dbDelegate conn) {
        super("player", 0, 0, 2, "human_", true, false,
                16, 32, 2.3);
        this.inventario = new Inventario(conn);
        this.habilidades = new ContrincanteHabilidad(conn);
        this.misiones = new Encargo(conn);
        this.bloqueo = false;
        this.conexion = conn;

    }
    /**
     * Nuevo constructor para NPC OPTIMIZACION BD
     * 
     * @param posicionX
     * @param posicionY
     * @param speed
     * @param graf
     * @param idPersonaje
     * @param nombre
     * @param nivel
     * @param tipo
     * @param cid
     */
    public Personaje( double speed, String graf, int cid,short idPersonaje, String nombre, short nivel, double posicionX, double posicionY, short tipo, dbDelegate conn) {
        super(nombre, posicionX, posicionY, cid, graf, true, false,
                16, 32, speed);
        stopAnim();
        this.idPersonaje = idPersonaje;
        this.nombre = nombre;
        this.nivel = nivel;
        this.tipo = tipo;
        this.inventario = new Inventario(conn);
        this.habilidades = new ContrincanteHabilidad(conn);
        this.misiones = new Encargo(conn);
        this.bloqueo = false;
    }


    public ContrincanteHabilidad getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(ContrincanteHabilidad habilidades) {
        this.habilidades = habilidades;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public void setInventario(Inventario inventario) {
        this.inventario = inventario;
    }

    public Encargo getMisiones() {
        return misiones;
    }

    public void setMisiones(Encargo misiones) {
        this.misiones = misiones;
    }

    public short getIdPersonaje() {
        return idPersonaje;
    }

    public void setIdPersonaje(short idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    public short getNivel() {
        return nivel;
    }

    public void setNivel(short nivel) {
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public short getTipo() {
        return tipo;
    }

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }
    /*
     * Carga los datos del personaje leyendo de las base de datos
     * Los datos a cargar son los siquientes
     * -Misiones asociadas
     * -Objetos que posee
     * -Habilidades que posee
     */

//    public void cargarDatos(Short id) {
//        this.cargarPersonaje(id);
//        this.getInventario().cargarInventario(id);
//        this.getMisiones().cargarMisiones(id);
//        this.getHabilidades().cargarHabilidades(id);
//
//
//    }
    /**
     * Carga los datos del personaje
     * @param id
     */
    public void cargarDatos(HashMap<Short,Objeto> objetos ,HashMap<Short, Habilidad> habilidades, HashMap<Short,Mision> misiones) {
        this.getInventario().cargarInventario(this.getIdPersonaje(),objetos);
        this.getMisiones().cargarMisiones(this.getIdPersonaje(),misiones);
        this.getHabilidades().cargarHabilidades(this.getIdPersonaje(),habilidades);
    }

    /*
     * Carga los datos del personaje desde la base de datos
     */

    public void cargarPersonaje(Short id) {
//        this.conexion = new dbDelegate();
        String StrSql = "SELECT  pjuno.id id, pjuno.nombre nombre, pjuno.nivel nivel, "
                + " pjuno.posicionX posX, pjuno.posicionY posY,pjuno.tipo tipo FROM personaje pjuno "
                + "WHERE pjuno.id=" + id;
        try {
            ResultSet res = conexion.Consulta(StrSql);
            if (res.next()) {
                this.setIdPersonaje(res.getShort("id"));
//                System.out.println("ID------------>"+this.getIdPersonaje());
                this.setNombre(res.getString("nombre"));
                this.setNivel(res.getShort("nivel"));
                this.setPos(res.getInt("posx"), res.getInt("posy"));
                this.setTipo(res.getShort("tipo"));
            }
//            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            System.out.println("Problemas en: clase->personaje , método->cargarPersonaje() " + ex);
        }
        

    }

    /**
     * Bloquea al personaje según el tiempo indicado
     * @param tiempo en segundos
     */
    public void bloquear(int tiempo) {
        this.setBloqueo(true);
        new JGTimer((int) (eng.getFrameRate() * tiempo), true) {

            @Override
            public void alarm() {
                desbloquear();

            }
        };
    }

    public void bloquear() {
        this.setBloqueo(true);

    }

    public void setBloqueo(Boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public Boolean getBloqueo() {
        return bloqueo;
    }

    public Boolean isBlocked() {
        return bloqueo;
    }

    public void desbloquear() {
        this.setBloqueo(false);
    }

    public double getPosicionX() {
        return this.x;
    }

    public double getPosicionY() {
        return this.y;
    }

    public void salvarDatos() {
        salvarPersonaje();
        this.getInventario().salvarInventario();
        this.getHabilidades().salvarHabilidades();
        this.getMisiones().salvarEncargo();
    }

    private void salvarPersonaje() {
        try {
            //seccion de misiones contenidas en el hashmap(misiones vigentes)
//            this.conexion = new dbDelegate();
            String StrSql = "UPDATE Personaje" + "   SET posicionx = " + this.x + "," + "       posiciony  = " + this.y + "," + "       nivel = " + this.getNivel() + "," + "       posiciony  = " + this.y + " WHERE personaje_id = " + this.getIdPersonaje();
            conexion.Ejecutar(StrSql);
//            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            Logger.getLogger(Personaje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
