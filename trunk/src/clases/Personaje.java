/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

//import jgame.JGPoint;
import java.sql.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jgame.JGRectangle;
import jgame.JGTimer;

/**
 *
 * @author gerald
 */
public class Personaje extends extensiones.StdDungeonPlayerV2 {
    // object cids: 1=player 2=monster 4=bullet 8=monsterbullet

    public static final int WALL_T = 1;
    public static final int SHWALL_T = 2; // shootable wall
    public static final int DOOR_T = 4;
    public static final int PLAYER_T = 16;
    public static final int MONSTER_T = 32;
    public static final int GEN_T = 64; // generator
    public static final int BONUS_T = 128;
    public static final int KEY_T = 256;
    public static final int HEALTH_T = 512;
    public static final int PLAYERBLOCK_T =
            WALL_T | SHWALL_T | DOOR_T | GEN_T;
    public static final int MONSTERBLOCK_T =
            WALL_T | SHWALL_T | DOOR_T | BONUS_T | MONSTER_T | GEN_T | BONUS_T | KEY_T | HEALTH_T;
    public static final int BULLETBLOCK_T =
            WALL_T | DOOR_T | BONUS_T | BONUS_T | KEY_T | HEALTH_T;
    //Variables de juego de Personaje
    private short idPersonaje;
    private String nombre;
    private short nivel;
    private short tipo;
    protected  dbDelegate conexion;
    public JGRectangle rClick;
    public boolean estadoClick = false;
    double mouseX;
    double mouseY;
    //factor es para convertir el punto del mouse de 800*600 a 1280*960
    //proporcionalmente para que se desplace de forma correcta
    double factor = 1.6;
    private Inventario inventario;
    private ContrincanteHabilidad habilidades;
    private Encargo misiones;
    private Boolean bloqueo;

    public Personaje(double x, double y, double speed, short idPersonaje, String nombre, String graf, short nivel, short tipo, int cid) {
        super(nombre, x, y, cid, graf, true, false,
                16, 32, speed);
        stopAnim();
        this.idPersonaje = idPersonaje;
        this.nombre = nombre;
        this.nivel = nivel;
        this.tipo = tipo;
        this.inventario = new Inventario();
        this.habilidades = new ContrincanteHabilidad();
        this.misiones = new Encargo();
        this.bloqueo = false;
    }

    public Personaje() {
        super("player", 0, 0, 1, "human_", true, false,
                16, 32, 2.3);
        this.inventario = new Inventario();
        this.habilidades = new ContrincanteHabilidad();
        this.misiones = new Encargo();
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

    /**
     * Este método es un puente al move de la clase StdDungeonPlayerV2
     */
    public void desplazar() {
        super.move();
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

    public void cargarDatos(Short id) {
        this.cargarPersonaje(id);
        this.getInventario().cargarInventario(id);
        this.getMisiones().cargarMisiones(id);
        this.getHabilidades().cargarHabilidades(id);


    }
    /*
     * Carga los datos del personaje desde la base de datos
     */

    public void cargarPersonaje(Short id) {
        this.conexion = new dbDelegate();
        System.out.println("Inicio obtiene datos personaje");
        String StrSql = "SELECT  pjuno.id id, pjuno.nombre nombre, pjuno.nivel nivel, "
                + " pjuno.posicionX posX, pjuno.posicionY posY,pjuno.tipo tipo FROM personaje pjuno "
                + "WHERE pjuno.id=" + id;

        try {

            ResultSet res = conexion.Consulta(StrSql);

            if (res.next()) {
                this.setIdPersonaje(res.getShort("id"));
                this.setNombre(res.getString("nombre"));
                this.setNivel(res.getShort("nivel"));
                this.setPos(res.getInt("posx"), res.getInt("posy"));
                this.setTipo(res.getShort("tipo"));

            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->personaje , método->cargarPersonaje() " + ex);
        }
        try {
            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            Logger.getLogger(Personaje.class.getName()).log(Level.SEVERE, null, ex);
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

    public void setBloqueo(Boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public Boolean getBloqueo() {
        return bloqueo;
    }

    public Boolean isBlocked() {
        return bloqueo;
    }

    private void desbloquear() {
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
        //seccion de misiones contenidas en el hashmap(misiones vigentes)
        this.conexion = new dbDelegate();
        String StrSql = "UPDATE Personaje"
                + "   SET posicionx = " + this.x + ","
                + "       posiciony  = " + this.y +","
                + "       nivel = " + this.getNivel() + ","
                + "       posiciony  = " + this.y
                + " WHERE personaje_id = " + this.getIdPersonaje();
        conexion.Ejecutar(StrSql);

    }
}
