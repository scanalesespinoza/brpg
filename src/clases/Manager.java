package clases;

import extensiones.StdScoring;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import jgame.JGColor;
import jgame.JGFont;
import jgame.JGPoint;
import jgame.platform.*;
import java.util.HashMap;
import jgame.JGTimer;
import jgame.JGObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import jgame.JGRectangle;

/**
 *
 * @author gerald
 */
public class Manager extends JGEngine {

    private static final int CIUDAD_X = 200;
    private static final int CIUDAD_Y = 200;

    /*
     *@param idJugador Esta variable idetificador corresponde a la clave
     * principal del personaje que ha seleccionado el usuario para jugar.
     * Permite en una misma sesion de juego recuperar, actualizar y desconectar al personaje Jugador.
     */
    private short idJugador = 13;//Valor en duro, debiera recibirse como parametro desde el sitio web
    private int interactuar = 0;//0=Jugador presente en el juego/1=Jugador ausente e interactuando con Npc/>0 Ejecutando dialogo y acciones de Npc
    private String nomNpcInteractuar;
    public int pausa = 0;// Modo de evitar que se ejecuten acciones por los 60 frames que ocurren por segundo
    //Personajes del juego
    public Jugador pj;
    public Mob mob_concurrente;
    public Npc vendedor_concurrente;
    public Npc npc_concurrente;
    public Personaje personaje_concurrente;
    public Npc npc_vendedor_1; // id = 1
    public Npc npc_vendedor_2; // id = 2
    public Npc npc_encargo_1; // id = 3
    public Npc npc_encargo_2; // id = 4
    public Npc npc_encargo_3; // id = 5
    public Mob mob_facil_1; // id = 6
    public Mob mob_facil_2; // id = 7
    public Mob mob_medio_1; // id = 8
    public Mob mob_medio_2; // id = 9
    public Mob mob_dificil_1; // id = 10
    public Mob mob_dificil_2; // id = 11
    public Mob mob_jefe_final; // id = 12
    public Seccion seccion = new Seccion();
    public Seccion seccionNpc = new Seccion();
    public Ventana ventanaManager;
    public boolean entregarRecompensa = false;
    public int mostrarVestir = 0;
    /*
     * Equipo: 5 Objetos para cada una de las partes donde puede equipar un item
     */
    private Icono equipo1;
    private Icono equipo2;
    private Icono equipo3;
    private Icono equipo4;
    private Icono equipo5;
    /*
    /*
     * Objetos de combate
     */
    private Icono icon;
    private boolean unJGTimer = false;
    private JGTimer tiempoRegenerar, respawn_mob, respawn_pj;
    private HashMap<Short, Habilidad> habilidades;
    private HashMap<Short, Mision> misiones;
    private Npc npc_interaccion;
    private StdScoring std_mob_daño, std_mob_sanacion, std_pj_daño, std_pj_sanacion, std_mob_habilidad, std_pj_mana;
    private HashMap<Short, Objeto> objetos;
    private boolean terminar_combate;

    public Icono getIcon() {
        return icon;
    }

    public void setIcon(Icono icon) {
        this.icon = icon;
    }
    /*
     * Objetos menú
     */
    public Cursor cursor;
    public Boton ventanaTrade;
    public Boton cerrar;
    /*
     * Variables para probar funcionalidad "Realizar Mision"
     */
    public boolean hongo1 = false;
    public boolean hongo2 = false;
    public boolean hongo3 = false;
    public boolean hongo4 = false;
    public boolean hongo5 = false;
    public boolean finHongos = false;
    public int tiempoMensaje = 0;
    private HashMap hsDatosPersonaje = new HashMap();//Mapa de datos utilizado para cargar un Jugador cuando su objeto no ha sido instanciado o fue removido
    private dbDelegate conect = new dbDelegate();//Conexion a base de datos
    private menuJuego menu;
    //Valor para determinar si la aplicacion debe cerrarse
    private boolean salir = false;
    private int seg = 0;
    private HashMap<Integer, Boolean> teclas;
    private HashMap<Integer, Icono> hmIconoItem = new HashMap<Integer, Icono>();
    private HashMap<Integer, Icono> hmIconoHabilidades = new HashMap<Integer, Icono>();
    public String[] textoPrueba;
    private boolean salirInInteracting = false;
    public int filtro = 0;

    public static void main(String[] args) {
        new Manager(new JGPoint(800, 540));

    }
//    public Boton grillaNpc;
//    public Boton grillaPj;

    /** Application constructor. */
    public Manager(JGPoint size) {
        initEngine(size.x, size.y);
    }

    /** Applet constructor. */
    public Manager() {
        initEngineApplet();

    }

    @Override
    public void initCanvas() {
        // we set the background colour to same colour as the splash background
        setCanvasSettings(40, 30, 16, 16, JGColor.black, new JGColor(255, 246, 199), null);
    }

    @Override
    public void initGame() {
        setProgressMessage("Iniciando la aplicación, espere...");
        setAuthorMessage("Sergio Canales Espinoza ||                    -JGame-                    || Gerald Schmidt Padilla");
        if (isApplet()) {
            idJugador = Short.parseShort(getParameter("id_personaje"));
        }
        setFrameRate(60, 2);
        dbgShowGameState(true);
        dbgShowBoundingBox(false);
        try {
            defineMedia("/media/rpg-basico_2.tbl");
            setBGImage("bgimage");

            /**
             * Definicion de canales para el audio
             * Canal    : Ambiental     --Musica de estados de combate
             * Canal    : Evento       --Sonidos de golpes, completar/tomar misiones, matar mobs, subir de nivel
             * Canal    : Menu          --Click, accion permitida, accion no permitida
             */
//            setMouseCursor(pj);
        } catch (Exception ex) {
            System.out.println("Error al cargar medios: " + ex);
        }
        new JGTimer(60, false) {

            @Override
            public void alarm() {
                seg++;
                inicializarTeclas();

            }
        };
        //setMsgFont(new JGFont("Helvetica",0,32));
        setFont(new JGFont("Arial", 0, 20));
        setPFSize(160, 120);//menuJuego de juego
        //playAudio("ambiental", "ciudad", true);
        //Objeto cursor, imágen que sigue las coordenadas del mouse
        cursor = new Cursor();
        //cargaJugador(0,0); reemplazamos por el metodo nuevo

//        setCursor(null);
        inicializarTeclas();
        try {
////            mob_facil_1 = new Mob(140 * 16, 110 * 16, 1.5, (short) 6, "mob_facil_1", "mob_1", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 6
////            mob_facil_2 = new Mob(140 * 16, 110 * 16, 1.5, (short) 7, "mob_facil_2", "orc_stand_r", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 7
////
////            mob_medio_1 = new Mob(140 * 16, 110 * 16, 1.5, (short) 8, "mob_medio_1", "mob_3", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 8
////            mob_medio_2 = new Mob(140 * 16, 110 * 16, 1.5, (short) 9, "mob_medio_2", "mob_4", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 9
////
////            mob_dificil_1 = new Mob(140 * 16, 110 * 16, 1.5, (short) 10, "mob_dificil_1", "grif_walk_r", (short) 10, (short) 3, mob_jefe_final, false, 0.9, (int) Math.pow(2, 2)); // id = 10
////            mob_dificil_2 = new Mob(140 * 16, 110 * 16, 0, (short) 11, "mob_dificil_2", "mob_6", (short) 10, (short) 3, mob_jefe_final, false, 0.9, (int) Math.pow(2, 2)); // id = 11
////
////            mob_jefe_final = new Mob(140 * 16, 110 * 16, 0, (short) 12, "mob_jefe_final", "boss_1", (short) 10, (short) 2, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 12
////
//            mob_facil_1.cargarDatos((short) 6);
//            mob_facil_2.cargarDatos((short) 7);
//            mob_medio_1.cargarDatos((short) 8);
//            mob_medio_2.cargarDatos((short) 9);
//            mob_dificil_1.cargarDatos((short) 10);
//            mob_dificil_2.cargarDatos((short) 11);
//            mob_jefe_final.cargarDatos((short) 12);
//
//            mob_facil_1.resume_in_view = false;
//            mob_facil_2.resume_in_view = false;
//            mob_medio_1.resume_in_view = false;
//            mob_medio_2.resume_in_view = false;
//            mob_dificil_1.resume_in_view = false;
//            mob_dificil_2.resume_in_view = false;
//            mob_jefe_final.resume_in_view = false;

            dibujarObjetosEscenario();
            menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj, conect);

//            npc_vendedor_1 = new Npc(1040, 416, "npc_vendedor_1", "vendedor", 0, (short) 22, (short) 1, (short) 1, new String[]{"Hola amiguirijillo", "soy el vendedorsillo"});//
//            npc_vendedor_1.cargarDatos((short) 1);
//            npc_vendedor_2 = new Npc(1040, 416, "npc_vendedor_2", "vendedor", 0, (short) 22, (short) 1, (short) 1, new String[]{"Hola amiguirijillo", "soy el vendedorsillo"});//
//            npc_vendedor_2.cargarDatos((short) 2);
//            npc_encargo_1 = new Npc(700, 75, "npc_encargo_1", "people", (int) Math.pow(2, 3), 0, (short) 3, new String[]{"Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior; // id = 3
//            npc_encargo_1.cargarDatos((short) 3);
//            npc_encargo_2 = new Npc(730, 75, "npc_encargo_2", "people2", (int) Math.pow(2, 3), 0, (short) 4, new String[]{"Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior; // id = 4
//            npc_encargo_2.cargarDatos((short) 4);
//            npc_encargo_3 = new Npc(760, 75, "npc_encargo_3", "people3", (int) Math.pow(2, 3), 0, (short) 5, new String[]{"Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior;
//            npc_encargo_3.cargarDatos((short) 5);

        } catch (Exception ex) {
            System.out.println("Extrae datos del HashMapsssssssssssssssss: " + ex);
        }

        definirEscenario();

        //CARGA DE HABILIDADES*************************************************/
        String StrSql = "SELECT * FROM habilidad ";
        System.out.println("*******************************************************************");
        this.habilidades = new HashMap<Short, Habilidad>();
        double linea = 30;
        try {
            ResultSet res = conect.Consulta(StrSql);
            while (res.next()) {
                Habilidad habi = new Habilidad();
                habi.setDescripcion(res.getString("descripcion"));
                habi.setNombre(res.getString("nombre"));
                habi.setIdHabilidad(res.getShort("id"));
                habi.setDanoBeneficio(res.getShort("danoBeneficio"));
                habi.setNivelMaximo(res.getShort("nivelMaximo"));
                habi.setCostoBasico(res.getShort("costoBasico"));
                habi.setNombreGrafico(res.getString("nom_grafico"));
                habi.setTiempoEspera(res.getInt("tiempoEspera"));
                this.habilidades.put(habi.getIdHabilidad(), habi);
                System.out.println("HABILIDAD " + habi.getNombre());
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->habilidades , método->setHabilidad() " + ex);
        }
        System.out.println("*******************************************************************");
        //CARGA DE MISIONES****************************************************/
        System.out.println("Inicio obtiene misiones");
        StrSql = "SELECT * FROM mision ";

        this.misiones = new HashMap<Short, Mision>();
        System.out.println("Inicio obtiene datos personaje");
        boolean bool;
        try {
            ResultSet res = conect.Consulta(StrSql);
            while (res.next()) {
                Mision mis = new Mision(conect);
                mis.setDescripcion(res.getString("descripcion"));
                mis.setNombre(res.getString("nombre"));
                mis.setIdMision(res.getShort("id"));
                mis.setIdPersonajeConcluyeMision(res.getShort("personaje_id"));
                mis.setNivelRequerido(res.getShort("nivelrequerido"));
                if (res.getShort("repetible") == 0) {
                    bool = false;
                } else {
                    bool = true;
                }
                mis.setRepetible(bool);
                mis.setRecompensaExp(res.getShort("recompensaexp"));
                mis.cargarDatos();
                this.misiones.put(mis.getIdMision(), mis);
                System.out.println("MISION :" + mis.getNombre());
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->manager , método-Linea 255 " + ex);
        }
        System.out.println("*******************************************************************");
        //CARGA DE OBJETOS****************************************************/
        System.out.println("Inicio obtiene misiones");
        StrSql = "SELECT * FROM objeto ";

        this.objetos = new HashMap<Short, Objeto>();
        System.out.println("Inicio obtiene datos personaje");
        try {
            ResultSet res = conect.Consulta(StrSql);
            while (res.next()) {
                Objeto obj = new Objeto();
                obj.setDescripcion(res.getString("descripcion"));
                obj.setNombre(res.getString("nombre"));
                obj.setIdObjeto(res.getShort("id"));
                obj.setNombreGrafico(res.getString("nom_grafico"));
                obj.setBeneficio(res.getShort("beneficio"));
                if (res.getInt("usocombate") == 0) {
                    obj.setUsoCombate(false);
                } else {
                    obj.setUsoCombate(true);
                }
                obj.setTipo(res.getShort("tipo"));
                obj.setPeso(res.getShort("peso"));
                obj.setValorDinero(res.getShort("valordinero"));

                this.objetos.put(obj.getIdObjeto(), obj);
                System.out.println("OBJETO: " + obj.getNombre());
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->manager , método-Linea 255 " + ex);
        }
//CARGO PERSONAJES*************************************************************/
        //CARGO PERSONAJES-NPC*************************************************/
        StrSql = "SELECT * FROM personaje p "
                + " WHERE tipo = 2 OR tipo= 1 ";

        System.out.println("**********PERSAONJEASDasda************************************************");
        try {
            ResultSet res = conect.Consulta(StrSql);
            while (res.next()) {
                switch (res.getShort("id")) {
                    //Pongo los constructores
                    case 1:
//                        npc_vendedor_1 = new Npc(1040, 416, "npc_vendedor_1", "vendedor", 0, (short) 22, (short) 1, (short) 1, new String[]{"Hola amiguirijillo", "soy el vendedorsillo"});//
//                        npc_vendedor_1.cargarDatos((short) 1);
                        npc_vendedor_1 = new Npc("vendedor_1", res.getShort("id"), res.getString("nombre"), res.getShort("nivel"), res.getDouble("posicionx"), res.getDouble("posiciony"), res.getShort("tipo"), conect);
                        break;
                    case 2:
//                        npc_vendedor_2 = new Npc(1040, 416, "npc_vendedor_2", "vendedor", 0, (short) 22, (short) 1, (short) 1, new String[]{"Hola amiguirijillo", "soy el vendedorsillo"});//
//                        npc_vendedor_2.cargarDatos((short) 2);
                        npc_vendedor_2 = new Npc("vendedor_2", res.getShort("id"), res.getString("nombre"), res.getShort("nivel"), res.getDouble("posicionx"), res.getDouble("posiciony"), res.getShort("tipo"), conect);
                        break;
                    case 3:
//                        npc_encargo_1 = new Npc(700, 75, "npc_encargo_1", "people", (int) Math.pow(2, 3), 0, (short) 3, new String[]{"Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior; // id = 3
//                        npc_encargo_1.cargarDatos((short) 3);
                        npc_encargo_1 = new Npc("mision_1", res.getShort("id"), res.getString("nombre"), res.getShort("nivel"), res.getDouble("posicionx"), res.getDouble("posiciony"), res.getShort("tipo"), conect);
                        break;
                    case 4:
//                        npc_encargo_2 = new Npc(730, 75, "npc_encargo_2", "people2", (int) Math.pow(2, 3), 0, (short) 4, new String[]{"Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior; // id = 4
//                        npc_encargo_2.cargarDatos((short) 4);
                        npc_encargo_2 = new Npc("mision_2", res.getShort("id"), res.getString("nombre"), res.getShort("nivel"), res.getDouble("posicionx"), res.getDouble("posiciony"), res.getShort("tipo"), conect);
                        break;
                    case 5:
//                        npc_encargo_3 = new Npc(760, 75, "npc_encargo_3", "people3", (int) Math.pow(2, 3), 0, (short) 5, new String[]{"Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior;
//                        npc_encargo_3.cargarDatos((short) 5);
                        npc_encargo_3 = new Npc("mision_3", res.getShort("id"), res.getString("nombre"), res.getShort("nivel"), res.getDouble("posicionx"), res.getDouble("posiciony"), res.getShort("tipo"), conect);
                        break;
                }
                System.out.println("PERSONAJE NPC :" + res.getString("nombre"));

            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->habilidades , método->setHabilidad() " + ex);
        }
        System.out.println("*******************************************************************");

        //CARGO PERSONAJES-MOB*************************************************/
        StrSql = "SELECT * FROM personaje p,mob m "
                + " WHERE p.id = m.personaje_id ";

        System.out.println("**********************************************************");
        try {
            ResultSet res = conect.Consulta(StrSql);
            while (res.next()) {
                switch (res.getShort("id")) {
                    //Pongo los constructores
                    case 6:
                        mob_facil_1 = new Mob("goblin_stand_r","goblin_dying_l",10, "goblin_hit_l", 5, "goblin_attack_l", 13, "goblin_stand2_l", 11,0.3, res.getShort("Id"), res.getString("nombre"), res.getShort("nivel"), res.getShort("posicionx"), res.getShort("posiciony"), res.getShort("tipo"), pj, false, 0.9, res.getShort("vitalidad"), res.getShort("destreza"), res.getShort("sabiduria"), res.getShort("fuerza"), res.getShort("experiencia"), res.getShort("dinero"),conect);
                        break;
                    case 7:
                        mob_facil_2 = new Mob("goblin2_stand_r","goblin2_dying_l",24, "goblin2_hit_l", 6, "goblin2_attack_l", 13, "goblin2_stand2_l", 8,0.3, res.getShort("Id"), res.getString("nombre"), res.getShort("nivel"), res.getShort("posicionx"), res.getShort("posiciony"), res.getShort("tipo"), pj, false, 0.9, res.getShort("vitalidad"), res.getShort("destreza"), res.getShort("sabiduria"), res.getShort("fuerza"), res.getShort("experiencia"), res.getShort("dinero"),conect);
                        break;
                    case 8:
                        mob_medio_1 = new Mob("tana_stand_r","tana_dying_l",20, "tana_hit_l", 5, "tana_attack_l", 11, "tana_stand2_l", 6  ,0.3, res.getShort("Id"), res.getString("nombre"), res.getShort("nivel"), res.getShort("posicionx"), res.getShort("posiciony"), res.getShort("tipo"), pj, false, 0.9, res.getShort("vitalidad"), res.getShort("destreza"), res.getShort("sabiduria"), res.getShort("fuerza"), res.getShort("experiencia"), res.getShort("dinero"),conect);
                        break;
                    case 9:
                        mob_medio_2 = new Mob("mutant_stand_r","mutant_dying_l",60, "mutant_hit_l", 5, "mutant_attack_l", 18, "mutant_stand2_l", 8 ,0.3, res.getShort("Id"), res.getString("nombre"), res.getShort("nivel"), res.getShort("posicionx"), res.getShort("posiciony"), res.getShort("tipo"), pj, false, 0.9, res.getShort("vitalidad"), res.getShort("destreza"), res.getShort("sabiduria"), res.getShort("fuerza"), res.getShort("experiencia"), res.getShort("dinero"),conect);
                        break;
                    case 10:
                        mob_dificil_1 = new Mob("grif_walk_l", "grif_dying_l",46, "grif_hit_l", 6, "grif_attack_l", 14, "grif_stand2_l", 8 , 0, res.getShort("Id"), res.getString("nombre"), res.getShort("nivel"), res.getShort("posicionx"), res.getShort("posiciony"), res.getShort("tipo"), pj, false, 0.9, res.getShort("vitalidad"), res.getShort("destreza"), res.getShort("sabiduria"), res.getShort("fuerza"), res.getShort("experiencia"), res.getShort("dinero"),conect);
                        break;
                    case 11:
                        mob_dificil_2 = new Mob("wolverine","wolverine_dying_l",5, "wolverine_hit_l", 6, "wolverine_attack_l", 7, "wolverine_stand2_l", 1  , 0.0,res.getShort("Id"), res.getString("nombre"), res.getShort("nivel"), res.getShort("posicionx"), res.getShort("posiciony"), res.getShort("tipo"), pj, false, 0.9, res.getShort("vitalidad"), res.getShort("destreza"), res.getShort("sabiduria"), res.getShort("fuerza"), res.getShort("experiencia"), res.getShort("dinero"),conect);
                        break;
                    case 12:
                        mob_jefe_final = new Mob("boss_stand_l", "boss_dying_l",20, "boss_hit_l", 5, "boss_attack_l", 14, "boss_stand2_l", 10, 0, res.getShort("Id"), res.getString("nombre"), res.getShort("nivel"), res.getShort("posicionx"), res.getShort("posiciony"), res.getShort("tipo"), pj, false, 0.9, res.getShort("vitalidad"), res.getShort("destreza"), res.getShort("sabiduria"), res.getShort("fuerza"), res.getShort("experiencia"), res.getShort("dinero"), conect);
                        break;
                }
                System.out.println("PERSONAJE NPC :" + res.getString("nombre"));
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->habilidades , método->setHabilidad() " + ex);
        }

        mob_facil_1.cargarDatos(objetos, habilidades, misiones);
        mob_facil_2.cargarDatos(objetos, habilidades, misiones);
        mob_medio_1.cargarDatos(objetos, habilidades, misiones);
        mob_medio_2.cargarDatos(objetos, habilidades, misiones);
        mob_dificil_1.cargarDatos(objetos, habilidades, misiones);
        mob_dificil_2.cargarDatos(objetos, habilidades, misiones);
        mob_jefe_final.cargarDatos(objetos, habilidades, misiones);
        npc_vendedor_1.cargarDatos(objetos, habilidades, misiones);
        npc_vendedor_2.cargarDatos(objetos, habilidades, misiones);
        npc_encargo_1.cargarDatos(objetos, habilidades, misiones);
        npc_encargo_2.cargarDatos(objetos, habilidades, misiones);
        npc_encargo_3.cargarDatos(objetos, habilidades, misiones);

        mob_facil_1.resume_in_view = false;
        mob_facil_2.resume_in_view = false;
        mob_medio_1.resume_in_view = false;
        mob_medio_2.resume_in_view = false;
        mob_dificil_1.resume_in_view = false;
        mob_dificil_2.resume_in_view = false;
        mob_jefe_final.resume_in_view = false;
        this.pj = new Jugador("human_", "pj_stand_r", 6, "pj_stand_r", 6, "pj_attack1_r", 3, "pj_stand_r", 6, 1, idJugador, conect);
        this.pj.cargarPersonaje(idJugador);
        this.pj.cargarDatos(objetos, habilidades, misiones);
        this.pj.setLinkMenu(menu);
        System.out.println("*******************************************************************");
        //setTileSettings("!", 20,0);
        ventanaManager = new Ventana();
        setGameState("Title");
        new JGTimer((int) (getFrameRate() * 10), false) {

            @Override
            public void alarm() {
                if (!inGameState("InCombat")) {
                    pj.regenerarMp(5);
                    new StdScoring("scoring_pj_mp", ((viewWidth() * 8) / 100), (double) 312, -0.1, -0.5, 160, " +" + pj.regenerarMp(5) + " MP ", new JGFont("arial", 1, 10), new JGColor[]{JGColor.blue}, 5, false);
                    pj.recibirDañoBeneficio((int) (pj.getHpMax() * 3 / 100));
                    new StdScoring("scoring_pj", ((viewWidth() * 10) / 100), (double) 302, -0.09, -0.5, 160, "" + (pj.getHpMax() * 3 / 100) + " HP", new JGFont("arial", 1, 13), new JGColor[]{JGColor.green}, 5, false);
                }
            }
        };
        //Guarda en la base de datos el jugador
        new JGTimer((int) (getFrameRate() * 30), false) {

            @Override
            public void alarm() {
                pj.salvarDatos();
                
            }
        };
    }
    
    /** View offset. */
    int xofs = 0, yofs = 0;

    public Icono getIconoPresionado() {
        return this.icon;
    }

    public void paintFrameTitle() {
        drawString("Trabajo de título", 100, 100, 0);
    }

    public void doFrameTitle() {
        if (getMouseButton(1)) { // start game
            clearMouseButton(1);
            setGameState("InWorld");

        } else if (getMouseButton(2)) { // start game
            clearMouseButton(1);
            seccion.removerIconos();
            setGameState("InDeath");
        }
    }

    public void doFrameInWorld() {
        if (cursor.isLimpiarIconos()) {
            seccion.removerIconos();
        }

        pj.desbloquear();
        if (pj.isSuspended()) {
            pj.setResumeMode(true);
        }
        if (pj.x < viewWidth() * 2 && pj.y < viewHeight() * 2) {
            //esta en la ciudad
            //playAudio("ambiental", "ciudad", true);
        } else if (pj.x < viewWidth() * 3 && pj.y < viewHeight() * 3) {
            //esta en la afuera de la ciudad
            //playAudio("ambiental", "ciudad2", true);
        } else {
            //Esta en pantano
            // playAudio("ambiental", "pantano", true);
        }
        capturarTeclas();
        if (isPresionada(KeyShift) && isPresionada(KeyCtrl)) {
            setGameState("InCombat");
        } else if (isPresionada(KeyShift) && isPresionada(KeyTab)) {
            initGame();
        }
//       
        moveObjects(null, 0);
        // llamada al metodo de colision entre objetos con las siguientes id de colision

//        checkCollision(
//                (int) Math.pow(2, 3) + (int) Math.pow(2, 1), // Colisión entre Npc + Jugador
//                (int) Math.pow(2, 1) // ejecuta hit Jugador
//                );
        checkCollision(
                (int) Math.pow(2, 3) + (int) Math.pow(2, 0), // Colisión entre Npc + Cursor
                (int) Math.pow(2, 0) // ejecuta hit Cursor
                );
        checkCollision(
                (int) Math.pow(2, 2) + (int) Math.pow(2, 1), // Colisión entre Mob + Jugador
                (int) Math.pow(2, 1) // ejecuta hit Jugador
                );
        checkCollision(
                (int) Math.pow(2, 4) + (int) Math.pow(2, 5), // Colisión entre Iconos + botones
                (int) Math.pow(2, 5) // ejecuta hit botones
                );
        checkCollision(
                (int) Math.pow(2, 4) + (int) Math.pow(2, 0), // Colisión entre Iconos + cursor
                (int) Math.pow(2, 0) // ejecuta hit cursor
                );
        checkCollision(
                (int) Math.pow(2, 5) + (int) Math.pow(2, 0), // Colisión entre botones  + cursor
                (int) Math.pow(2, 0) // ejecuta hit cursor
                );
        /*checkCollision(//boton cerrar
        2 ^ 6 + 256, // cids of objects that our objects should collide with
        256 // cids of the objects whose hit() should be called
        );
        checkCollision(//boton cerrar + pj
        1 + 2 ^ 6, // cids of objects that our objects should collide with
        2 ^ 6 // cids of the objects whose hit() should be called
        );
        checkCollision(//ventana trade
        2 ^ 7 + 256, // cids of objects that our objects should collide with
        256 // cids of the objects whose hit() should be called
        );
        checkCollision(//ventana trade + pj
        1 + 2 ^ 7, // cids of objects that our objects should collide with
        2 ^ 7 // cids of the objects whose hit() should be called
        );*/
        // llamada al metodo de colision entre objeto y escenario con las siguientes id de colision
        checkBGCollision(
                1 + 11 + 13, // collide with the marble and border tiles
                (int) Math.pow(2, 1) // cids of our objects
                );

        /*checkCollision(//vendedor + pj
        1500 + 2 ^ 12, // cids of objects that our objects should collide with
        2 ^ 12 // cids of the objects whose hit() should be called
        );
        checkCollision(//vendedor + pj
        1600 + 2 ^ 11, // cids of objects that our objects should collide with
        2 ^ 11 // cids of the objects whose hit() should be called
        );*/

        int posX = (int) pj.x;
        int posY = (int) pj.y;

        xofs = posX;
        yofs = posY;
        setViewOffset(
                xofs, yofs,
                true);

        if (cursor.getVentana() == 1) {
            setGameState("InCommerce");
            mostrarVestir = 0;
            menu.vestir = false;
            seccion.setSeccion(new JGPoint(25, 25), new JGPoint(2, 4));
            seccionNpc.setSeccion(new JGPoint(250, 25), new JGPoint(3, 4));
        }
        if (isSalir()) {
            System.exit(0);
        }
        if (checkCollision((int) Math.pow(2, 2), pj) == Math.pow(2, 2)) {
            mob_concurrente = pj.getEnemigo();
            setGameState("InCombat");
            removerEquipo();
            mostrarVestir=0;
            menu.restablecerDinamicaCombate();
            menu.anim_mob_flag = "Parado";
            menu.anim_mob = mob_concurrente.anim_parado;
            menu.frames_mob = mob_concurrente.frames_anim_parado;
            terminar_combate = false;
            
            //playAudio("ambiental", "combate", true);
            filtro = 0;
            seccion.setWorking(false);
            seccion.removerIconos();
        }
    }

    public void paintFrameInWorld() {
//        tiempoMensaje--;
//        if (cursor.getMensaje().length() > 0) {
//            new Ventana(cursor.getMensaje());
//            cursor.setMensaje("");
//        }
    }

    @Override
    public void paintFrame() {
        pj.getMisiones().getFecha();
        //Dibujo barra vida y mana del jugador
        setFont(new JGFont("Arial", 0, 15));

        setColor(JGColor.black);
        // aca graficar todas las wes hermosas y lindas de la warifaifa
        drawString(pj.getNombre() + " Nivel " + pj.getNivel(), ((viewWidth() * 10) / 100), (double) 302, 0);

        drawRect(viewWidth() * 10 / 100 + viewXOfs(), 322 + viewYOfs(), (float) (pj.getHp() * 100 / pj.getHpMax()), 10, true, false, 0, JGColor.green);
        drawRect(viewWidth() * 10 / 100 + viewXOfs(), 337 + viewYOfs(), (float) (pj.getMp() * 100 / pj.getMpMax()), 10, true, false, 0, JGColor.blue);
        setColor(JGColor.blue);
        setFont(new JGFont("Arial", 0, 10));
        drawString((pj.getHp() * 100 / pj.getHpMax()) + "%", ((viewWidth() * 17) / 100) + 9, 322, 0, false);
        setColor(JGColor.green);
        drawString((pj.getMp() * 100 / pj.getMpMax()) + "%", ((viewWidth() * 17) / 100) + 9, 337, 0, false);


        if (mostrarVestir == 1) {

            if (equipo1 != null) {
                if (pj.getInventario().getEquipo().get(equipo1.getItem().getIdObjeto()).getEquipado() == 1) {
                    equipo1.paintEquipo();
                }
            }
            if (equipo2 != null) {
                if (pj.getInventario().getEquipo().get(equipo2.getItem().getIdObjeto()).getEquipado() == 1) {
                    equipo2.paintEquipo();
                }
            }
            if (equipo3 != null) {
                if (pj.getInventario().getEquipo().get(equipo3.getItem().getIdObjeto()).getEquipado() == 1) {
                    equipo3.paintEquipo();
                }
            }
            if (equipo4 != null) {
                if (pj.getInventario().getEquipo().get(equipo4.getItem().getIdObjeto()).getEquipado() == 1) {
                    equipo4.paintEquipo();
                }
            }
            if (equipo5 != null) {
                if (pj.getInventario().getEquipo().get(equipo5.getItem().getIdObjeto()).getEquipado() == 1) {
                    equipo5.paintEquipo();
                }
            }

        } else {
            removerEquipo();
            if (mostrarVestir == -1) {
                mostrarVestir = 1;
            }

        }
        seccion.setSeccion(new JGPoint(110, 435), new JGPoint(12, 1));
        seccion.generaSeccion(pj, 0);
        menu.recibeHm(hmIconoHabilidades, 2, filtro);

        seccion.setSeccion(new JGPoint(110, 400), new JGPoint(12, 1));
        seccion.generaSeccion(pj, 1);
        menu.recibeHm(hmIconoItem, 1, filtro);

//        seccion.removerIconos();
//        seccion.setWorking(false);
//        seccion.setSeccion(new JGPoint(110, 400), new JGPoint(12, 1));
//        seccion.generaSeccion(pj, 1);
//        menu.recibeHm(hmIconoItem, 1, filtro);

        seccion.setWorking(true);

        cursor.desplegarInformacion();

        moveObjects(null, 1);

        menu.paintB(pj);
        menu.menuActual(getTeclaMenu(), pj);

    }

    public void removerEquipo(){
            if (equipo1 != null) {
//                equipo1.x = 200+ viewXOfs();
//                equipo1.y = 200 + viewYOfs();
                equipo1.remove();
            }
            if (equipo2 != null) {
//                equipo2.x = 200+ viewXOfs();
//                equipo2.y = 250 + viewYOfs();
                equipo2.remove();
            }
            if (equipo3 != null) {
//                equipo3.x = 200+ viewXOfs();
//                equipo3.y = 300 + viewYOfs();
                equipo3.remove();
            }
            if (equipo4 != null) {
//                equipo4.x = 200+ viewXOfs();
//                equipo4.y = 350 + viewYOfs();
                equipo4.remove();
            }
            if (equipo5 != null) {
//                equipo5.x = 200+ viewXOfs();
//                equipo5.y = 400 + viewYOfs();
                equipo5.remove();
            }
    }

    @Override
    public void doFrame() {
        moveObjects(null, (int) Math.pow(2, 5)); //muevo los botones que estan en el menu
//        if ((inGameStateNextFrame("InWorld") && !inGameState("InWorld"))) {
//            //seccion.removerIconos();
//            //removeObjects("icono", (int) Math.pow(2, 4));
//        }
        //el cursor no choca contra el icono
        if (checkCollision((int) Math.pow(2, 4), cursor) != Math.pow(2, 4)) {
//            cursor.setMensajeIcon(null);
            ventanaManager.mostrarDatoFreak("");
        }
        //el cursor no choca contra un boton del tipo ver
        if (checkCollision((int) Math.pow(2, 5), cursor) != Math.pow(2, 5)) {
            ventanaManager.mostrarDatoFreak("");
//            cursor.limpiarInformacion();
        }
        //el cursor no choca contra un npc
        if (checkCollision((int) Math.pow(2, 3), cursor) != Math.pow(2, 3)) {
            ventanaManager.mostrarDatoFreak("");
        }
        moveObjects(null, (int) Math.pow(2, 7));
        if (getKey(27)) {
            cursor.limpiarInformacion();
        }

    }

    public void paintFrameInDeath() {
        //Avisar de que el jugador perdio y debe recuperarse terriblemente
        new Ventana("Te encuentras exahusto despues del combate, vé a la ciudad para recuperarte");
    }

    public void doFrameInDeath() {
        if (respawn_pj == null || !respawn_pj.running) {
            respawn_pj = new JGTimer((int) (0), true) {

                @Override
                public void alarm() {
                    //playAudio("ambiental", "muerte", true);
                    pj.setPos(CIUDAD_X, CIUDAD_Y);
                    pj.setDir(0, 0);
                    pj.suspend();
                    respawn_pj = null;
                }
            };
        }


        //personaje es enviado a la ciudad, poner con cara de muerto, o alguna seña que lo está
        ventanaManager.mostrarDatoFreak("Pulse ENTER para continuar");
        if (getKey(KeyEnter)|| getMouseButton(1)) {
            clearMouseButton(1);
            new JGTimer(60 * 3, true) {

                @Override
                public void alarm() {
                    seccion.removerIconos();
                    pj.aumentarDisminuirMp(pj.getMpMax() / 2);
                    pj.recibirDañoBeneficio(pj.getHpMax() / 2);
                    setGameState("InWorld");
                    pj.resume();
                }
            };
        }
    }

    public void paintFrameInCombat() {
        mostrarVestir=0;
        removerEquipo();
        menu.vestir(false);

        if (std_mob_daño != null) {
            std_mob_daño.paintB();
        }
        if (std_pj_daño != null) {
            std_pj_daño.paintB();
        }
        if (std_pj_sanacion != null) {
            std_pj_sanacion.paintB();
        }
        if (std_mob_sanacion != null) {
            std_mob_sanacion.paintB();
        }

        if (std_mob_habilidad != null) {
            std_mob_habilidad.paintB();
        }

        if (std_pj_mana != null) {
            std_pj_mana.paintB();
        }

        //Dibujo barra vida y mana del jugador
        setFont(new JGFont("Arial", 0, 15));

        setColor(JGColor.black);
        // aca graficar todas las wes hermosas y lindas de la warifaifa
        drawString(pj.getNombre() + " Nivel " + pj.getNivel(), ((viewWidth() * 10) / 100), (double) 302, 0);

        drawRect(viewWidth() * 10 / 100 + viewXOfs(), 322 + viewYOfs(), (float) (pj.getHp() * 100 / pj.getHpMax()), 10, true, false, 0, JGColor.green);
        drawRect(viewWidth() * 10 / 100 + viewXOfs(), 337 + viewYOfs(), (float) (pj.getMp() * 100 / pj.getMpMax()), 10, true, false, 0, JGColor.blue);

        setColor(JGColor.black);
        drawString(mob_concurrente.getNombre() + " Nivel " + mob_concurrente.getNivel(), ((viewWidth() * 70) / 100), (double) 302, 0);
        drawRect(viewWidth() * 70 / 100 + viewXOfs(), 322 + viewYOfs(), (float) (mob_concurrente.getHp() * 100 / mob_concurrente.getHpMax()), 10, true, false, 400, JGColor.green);
        drawRect(viewWidth() * 70 / 100 + viewXOfs(), 337 + viewYOfs(), (float) (mob_concurrente.getMp() * 100 / mob_concurrente.getMpMax()), 10, true, false, 400, JGColor.blue);

        setFont(new JGFont("Arial", 0, 10));
        drawString((pj.getHp() * 100 / pj.getHpMax()) + "%", ((viewWidth() * 17) / 100) + 9, 322, 0, false);
        drawString((mob_concurrente.getHp() * 100 / mob_concurrente.getHpMax()) + "%", ((viewWidth() * 77) / 100) + 9, 322, 0, false);
        setColor(JGColor.green);
        drawString((pj.getMp() * 100 / pj.getMpMax()) + "%", ((viewWidth() * 17) / 100) + 9, 337, 0, false);
        drawString((mob_concurrente.getMp() * 100 / mob_concurrente.getMpMax()) + "%", ((viewWidth() * 77) / 100) + 9, 337, 0, false);

        if (mob_concurrente.getIdProximoAtaque() != -1) {
            std_mob_habilidad = new StdScoring("nombreAtaque", ((viewWidth() * 70) / 100) + viewXOfs(), 315 + viewYOfs(), 0, 0, 80, mob_concurrente.getHabilidades().getHabilidad(mob_concurrente.getIdProximoAtaque()).getHabilidad().getNombre(), new JGFont("Arial", 0, 12), new JGColor[]{JGColor.black, JGColor.white}, 10);
        }
    }

    public void doFrameInCombat() {

        moveObjects(null, (int) Math.pow(2, 7));
        int dañoBeneficio = 0;
        checkCollision(
                (int) Math.pow(2, 4) + (int) Math.pow(2, 0), // Colisión entre Iconos + cursor
                (int) Math.pow(2, 0) // ejecuta hit cursor
                );
        if (!terminar_combate) {
            //PESCO LOS ICONOS QUE FUERON PRESIONADOS POR EL JUGADOR Y LE DIGO AL OBJETO JUGADOR
            //QUE ESA HABILIDAD SE VA A OCUPAR, LE ENTREGO COMO PARAMETRO LA HABILIDAD DEL ICONO
            //PONER EN VARIABLES AL WEON CON CUAL EL QLIO DEL JUGADOR PELEA (nombre = Enemigo)
            /**************************PERSONAJE**********************************/
            if (this.getIconoPresionado() != null && this.getIconoPresionado().getTipo() == 0) {
                //personaje utilizara una habilidad
                pj.setProximoAtaque(this.getIconoPresionado().getIdObjeto());
                if (pj.getIdProximoAtaque() != -1) {
                    //el personaje puede atacar por que no está bloqueado
                    dañoBeneficio = pj.getHabilidades().getDañoBeneficio(pj.getIdProximoAtaque());
                    if (dañoBeneficio < 0) {
                        dañoBeneficio -= ((pj.getAtaque()) * (100 - mob_concurrente.getDefensa())) / 50 - pj.getAtaque();
                        //se convierte en daño hacia el enemigo
                        mob_concurrente.recibirDañoBeneficio(-(mob_concurrente.getHpMax() / 4));
                        //si no es beneficio al jugador
                        std_mob_daño = new StdScoring("scoring_mob", ((viewWidth() * 78) / 100) + viewXOfs(), (double) 302 + viewYOfs(), 0.09, -1, 160, "" + dañoBeneficio + " HP", new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.red, JGColor.orange, JGColor.yellow}, 5);
                        // playAudio("evento_combate", "golpe", false);
//                    menu.recibeScore(null, new StdScoring("scoring", mob_concurrente.x, mob_concurrente.y, 0, -2, 80, "" + dañoBeneficio, new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.red, JGColor.orange, JGColor.yellow}, 5));
                    } else {
                        pj.recibirDañoBeneficio(dañoBeneficio);
                        //playAudio("evento_combate", "heal", false);
                        std_pj_sanacion = new StdScoring("scoring_pj", ((viewWidth() * 10) / 100) + viewXOfs(), (double) 302 + viewYOfs(), -0.09, -1, 160, "" + dañoBeneficio + " HP", new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green, JGColor.yellow}, 5);
//                    menu.recibeScore(new StdScoring("scoring", pj.x, pj.y, 0, -2, 80, "" + dañoBeneficio, new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green, JGColor.yellow}, 5), null);
                    }
                }
                setIcon(null);
            } else if (this.getIconoPresionado() != null && this.getIconoPresionado().getTipo() == 1) {
                //personaje ha utilizado algun tipo de objeto...validar que sea para uso en combate
                Objeto obje = icon.getItem();//pj.getInventario().getItem(this.getIconoPresionado().getIdObjeto()).getObjeto();
                if (obje.isUsoCombate()) {
                    pj.setProximoItem(obje);
                    seccion.removerIconos();
                    seccion.setWorking(false);
                }

                if (pj.getIdProximoItem() != -1) {
                    //el personaje puede usar un item
                    dañoBeneficio = pj.getInventario().getItem(pj.getIdProximoItem()).getObjeto().getBeneficio();
                    pj.recibirDañoBeneficio(dañoBeneficio);
                    //playAudio("evento_combate", "heal2", false);
                    std_pj_sanacion = new StdScoring("scoring_pj", ((viewWidth() * 10) / 100) + viewXOfs(), (double) 302 + viewYOfs(), -0.09, -1, 160, "" + dañoBeneficio + " HP", new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green, JGColor.yellow}, 5);
                    pj.setIdProximoItem((short) -1);
                }
                setIcon(null);
            }


            dañoBeneficio = 0;
            /**************************ENEMIGO MOB*********************************/
            //MOB utilizara una habilidad
            mob_concurrente.generarProximoAtaque();
            if (mob_concurrente.getIdProximoAtaque() != -1) {
                //el MOB puede atacar por que no está bloqueado
                dañoBeneficio = mob_concurrente.getHabilidades().getDañoBeneficio(mob_concurrente.getIdProximoAtaque());
                if (dañoBeneficio < 0) {
                    dañoBeneficio -= ((mob_concurrente.getAtaque()) * (50 - pj.getDefensa())) / 50 - mob_concurrente.getAtaque();
                    //se convierte en daño hacia el jugador
                    pj.recibirDañoBeneficio(dañoBeneficio);//dañoBeneficio
                    //playAudio("evento_combate", "golpe2", false);
                    std_pj_daño = new StdScoring("scoring_pj", ((viewWidth() * 10) / 100) + viewXOfs(), (double) 302 + viewYOfs(), -0.09, -1, 160, "" + dañoBeneficio + " HP", new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.red, JGColor.orange, JGColor.yellow}, 5);
                    //si no es beneficio al MOB
                } else {
                    mob_concurrente.recibirDañoBeneficio(dañoBeneficio);
                    std_mob_sanacion = new StdScoring("scoring_mob", ((viewWidth() * 78) / 100) + viewXOfs(), (double) 302 + viewYOfs(), 0.09, -1, 160, "" + dañoBeneficio + " HP", new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green, JGColor.yellow}, 5);
                }
            }
            //regenero mana cada 4 segundos y que desaparezca cuando no esté en estado INCOMBAT
            if (tiempoRegenerar == null || !tiempoRegenerar.running) {
                tiempoRegenerar = new JGTimer((int) (getFrameRate() * 4), false, "InCombat") {

                    @Override
                    public void alarm() {
                        mob_concurrente.regenerarMp(3);
                        std_pj_mana = new StdScoring("scoring_pj_mp", ((viewWidth() * 8) / 100) + viewXOfs(), (double) 312 + viewYOfs(), -0.1, -0.5, 160, " +" + pj.regenerarMp(5) + " MP ", new JGFont("helvetica", 1, 14), new JGColor[]{JGColor.blue}, 5);
                    }
                };
            }
        }
        if (mob_concurrente.getHp() <= 0) {
            terminar_combate = true;
            final Mob enemigo_procesar = (Mob) getObject(mob_concurrente.getName());
            if (menu.isTermineAnimacionMuerte()) {
                if (respawn_mob == null) {
                    respawn_mob = new JGTimer((int) (getFrameRate() * 30 * 1), true) {

                        @Override
                        public void alarm() {
                            enemigo_procesar.resume();
                            enemigo_procesar.aumentarDisminuirMp(enemigo_procesar.getMpMax());
                            enemigo_procesar.recibirDañoBeneficio(enemigo_procesar.getHpMax());
                            mob_concurrente.getInventario().restablecerInventario();
                        }
                    };
                    respawn_mob = null;
                }

                mob_concurrente.suspend();
                personaje_concurrente = (Personaje) enemigo_procesar;
                seccion.removerIconos();
                cursor.setVentana((byte) (1));
                setGameState("InReward");
                int nivel = pj.getNivel();
                pj.aumentarExperiencia(mob_concurrente.getExperiencia());
                String mjs;
                if (nivel != pj.getNivel()) {
                    mjs = "¡ Has alcanzado el nivel " + pj.getNivel() + " !";
                } else {
                    mjs = "¡ +" + mob_concurrente.getExperiencia() + " de experiencia !";
                    playAudio("evento", "hallar_algo", false);
                }
                new StdScoring("pj_exp", pj.x, pj.y + 100, 0, -2, 120, mjs, new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green}, 10);
//                menu.restablecerDinamicaCombate();
            }
        }
    }

    public void paintFrameInInteraction() {
        if (!salirInInteracting) {
            Boton btn1;
            //dibujo las lineas del parrafo
            ventanaManager.mostrarTexto();
            if (ventanaManager.terminoDialogo) {//genero el boton siguiente
                btn1 = new Boton("cerrar", "cerrar", 430, 330, (int) (Math.pow(2, 5)), 0, 0);
                btn1.pintar();
            } else {//termino el dialogo //aun podria estar leyendo el ultimo parrafo = new Boton("cerrar", "suma", 430, 330, (int) (Math.pow(2, 5)), 0, 0);
                btn1 = new Boton("siguiente", "ver", 430, 330, (int) (Math.pow(2, 5)), 5, 0);
                btn1.pintar();
            }
        }
    }
    //avanzo al siguiente dialogo
//            npc_concurrente.getDialogo().nextTexto();

    public void doFrameInInteraction() {
        moveObjects(null, (int) (Math.pow(2, 5)));
        checkCollision(
                (int) Math.pow(2, 5) + (int) Math.pow(2, 0), // Colisión entre botones  + cursor
                (int) Math.pow(2, 0) // ejecuta hit cursor
                );

    }

    public void doFrameInReward() {
        checkCollision(
                (int) Math.pow(2, 5) + (int) Math.pow(2, 0), // Colisión entre botones  + cursor
                (int) Math.pow(2, 0) // ejecuta hit cursor
                );
        checkCollision(
                (int) Math.pow(2, 4) + (int) Math.pow(2, 0), // Colisión entre Iconos + cursor
                (int) Math.pow(2, 0)); // ejecuta hit cursor
        ventanaManager.mostrarDatoFreak("Pulse ENTER para continuar");
        if (getKey(KeyEnter)|| getMouseButton(1)) {
        
//            mob_concurrente.recibirDañoBeneficio(mob_concurrente.getHpMax());
//            mob_concurrente.aumentarDisminuirMp(mob_concurrente.getMpMax());
            clearMouseButton(1);
//            cursor.setLimpiarIconos(true);
            seccion.removerIconos();
            seccion.setWorking(false);
            pj.bloquear();
            setGameState("InWorld");
            this.entregarRecompensa = false;
            personaje_concurrente.getInventario().restablecerInventario();
            this.personaje_concurrente = null;
        } else {
            interacVentana(personaje_concurrente, "InReward");
        }
    }

    public void paintFrameInReward() {
    }

    public void paintFrameInCommerce() {
        cursor.desplegarInformacion();
    }

    public void doFrameInCommerce() {
        checkCollision(
                (int) Math.pow(2, 5) + (int) Math.pow(2, 0), // Colisión entre botones  + cursor
                (int) Math.pow(2, 0) // ejecuta hit cursor
                );
        checkCollision(
                (int) Math.pow(2, 4) + (int) Math.pow(2, 0), // Colisión entre iconos  + cursor
                (int) Math.pow(2, 0) // ejecuta hit cursor
                );
        interacVentana(pj, vendedor_concurrente, "InWorld");
    }

    public void capturarTeclas() {
        if (getKey(KeyUp)) {
            teclas.put(KeyUp, true);
        }
        if (getKey(KeyDown)) {
            teclas.put(KeyDown, true);
        }
        if (getKey(KeyLeft)) {
            teclas.put(KeyLeft, true);
        }
        if (getKey(KeyRight)) {
            teclas.put(KeyRight, true);
        }
        if (getKey(KeyShift)) {
            teclas.put(KeyShift, true);
        }
        if (getKey(KeyCtrl)) {
            teclas.put(KeyCtrl, true);
        }
        if (getKey(KeyEsc)) {
            teclas.put(KeyEsc, true);
        }
        if (getKey(KeyFire)) {
            teclas.put(KeyFire, true);
        }
        if (getKey(KeyTab)) {
            teclas.put(KeyTab, true);
        }
        if (getKey(KeyMouse1)) {
            teclas.put(KeyMouse1, true);
        }
        if (getKey(KeyMouse2)) {
            teclas.put(KeyMouse2, true);
        }
        if (getKey(KeyMouse3)) {
            teclas.put(KeyMouse3, true);
        }
        if (getKey(KeyBackspace)) {
            teclas.put(KeyBackspace, true);
        }
        if (getKey(KeyEnter)) {
            teclas.put(KeyEnter, true);
        }
        if (getKey(KeyAlt)) {
            teclas.put(KeyAlt, true);
        }
    }

    public void inicializarTeclas() {
        teclas = new HashMap<Integer, Boolean>();
    }

    public boolean isPresionada(int tecla) {
        if (teclas.containsKey(tecla)) {
            teclas.put(tecla, false);
            return true;
        }
        return false;
    }

    public int getTeclaMenu() {
        int teclaPres = 0;
        if ((getKey(101)) || (getKey(69))) {
            teclaPres = 4;
        } else if ((getKey(109)) || (getKey(77))) {
            teclaPres = 2;
        } else if ((getKey(104)) || (getKey(72))) {
            teclaPres = 1;
        } else if ((getKey(111)) || (getKey(79))) {
            teclaPres = 5;
        }

        return teclaPres;
    }

    public short getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(short idJugador) {
        this.idJugador = idJugador;
    }

    public int getInteractuar() {
        return interactuar;
    }

    public void setInteractuar(int interactuar) {
        this.interactuar = interactuar;
    }

    public boolean isSalir() {
        return salir;
    }

    public void setSalir(boolean salir) {
        this.salir = salir;
    }

    public String getNomNpcInteractuar() {
        return nomNpcInteractuar;
    }

    public void setNomNpcInteractuar(String nomNpcInteractuar) {
        this.nomNpcInteractuar = nomNpcInteractuar;
    }

    private void definirEscenario() {
        /*
         * Mapa completo de tiles que definen la ciudad.
         * Simbologia presente en el archivo TBL.
         */

        setTiles(
                0, // tile x index
                0, // tile y index
                new String[]{"+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++",//1
                    "+!...............!!!!!!............*****^...................*****..............+",
                    "+!......!!!!!!!..!!!!!!!!!!........*****.^..................*****..............+",
                    "+!......!!!!!!!..!!!!!!!!!!........*****...!!!!!!!!!!!!!!...*****..............+",
                    "+!.....^!!!!!!!..!!!!!!!!!!........*****...!............!...*****..............+",//5
                    "+!......!!!!!!!..!!!!!!!!!!........*****...!............!...*****..............+",
                    "+!.%....!!!!!!!..!!!!!!!!!!........*****...!............!...*****..............+",
                    "+!.%%...!!!!!!!..!!!!!!!!!!....^...*****...!............!...*****..............+",
                    "+!.%%%..!!!!!!!..!!!!!!!!!!........*****...!............!...*****..............+",
                    "+!.%%%%..........!!!!!!!!!!........*****...!............!...*****..............+",//10
                    "+!.%%%%..........!!!!!!!!!!........*****...!............!.^.*****..............+",
                    "+!.%%%%%.............^!!!!!........*****...!............!...*****..............+",
                    "+!.%%%%%%..%.%.%.%.%..!!!!!........*****...!!!..!!!!!!!!!...*****..............+",
                    "+!..%%%%%%%%%%%%%%%%%%%%%%%%%%%....*****...!!!..!!!!!!!!!...*****.............^+",
                    "+!....%%%%%%%%%%%%%%^%%%%%%%%%%%...*****....................*****.............^+",//15
                    "+!^................................*****..^^^^^^............*****.......^^^^^^^+",
                    "+!*****************************************************************************|",
                    "+!*****************************************************************************|",
                    "+!*****************************************************************************|",
                    "+!*****************************************************************************|",//20
                    "+!*****************************************************************************|",
                    "+!.................................**********..................................+",
                    "+!.................................*****.*****.................................+",
                    "+!..!!!!!!!!!!!!!!....^............*****..*****................................+",
                    "+!..!!!!!!!!!!!!!!.................*****...*****.................!!!!!!........+",//25
                    "+!..!!!!!!!!!!!!!!.................*****....*****................!!!!!!........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****.....*****.....^.........!!!!!!........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****......*****..............!!!!!!........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****.......*****.............!!!!!!........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****........*****............!!!!!!........+",//30
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****.^.......*****...........!!!!!!........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****..........*****..........!!!!!!........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****...........*****.........!!!!!!........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****............*****........!!!!!!........+",
                    "+!...................!!!!!!!!!!!!!!*****.............*****.....................+",//35
                    "+!...................!!!!!!!!!!!!!!*****..............*****....................+",
                    "+!...................!!!!!!!!!!!!!!*****...............*****...................+",
                    "+!.................................*****................*****..................+",//38
                    "+!..!!!!!!!!!!!!!!.................*****.................*****.................+",
                    "+!..!!!!!!!!!!!!!!.................*****..!!!!!!!!!!!!!!..*****................+",
                    "+!..!!!!!!!!!!!!!!..........^......*****..!!!!!!!!!!!!!!...*****...............+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****..!............!....*****..............+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****..!............!.....*****.............+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****..!............!......*****............+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****..!............!.......*****...........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****..!............!........*****..........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****..!............!.........*****.........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****..!............!..........*****........+",
                    "+!..!!!!!!!!!!!!!!...!!!!!!!!!!!!!!*****..!............!...........*****.......+",
                    "+!............^^^....!!!!!!!!!!!!!!*****..!!!..!!!!!!!!!............*****......+",
                    "+!.............^.....!!!!!!!!!!!!!!*****.............................*****.....+",
                    "+!..^................!!!!!!!!!!!!!!*****..............................*****^^..+",
                    "+!.................................*****...............................*****^..+",
                    "+!.................................*****................^...............*****..+",
                    "+!.................................*****...............^^^...............*****.+",
                    "+!...^.............................*****..................................*****+",
                    "+!.......................^.........*****...................................****|",
                    "+!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*****!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!***|",
                    "+!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*****!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!**|",
                    "+++++++++++++++++++++++++++++++++++*****++++++++++++++++++++++++++++++++++++++||",});
        /**
         * Mapa completo de tiles que definen la entrada a la ciudad.
         * Simbologia presente en el archivo TBL.
         */
        setTiles(
                80, // tile x index
                0, // tile y index
                new String[]{"[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",//1
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",//5
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",//10
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[",//15
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "******[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "******[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "******[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "******[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "******[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",//25
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[)))))))))))",
                    "[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))",//35
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))",//38
                    "[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[^[[[[[[[[[[[[[[[[[[[[[[^[[[[)))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))^))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))%%",
                    "[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%",
                    "[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%",
                    "[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%",
                    "[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%",
                    "[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%",
                    "[[[[[^[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%",
                    "[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%",
                    "[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%",
                    "[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%",});
        /*
         * Mapa completo de tiles que definen las ruinas.
         * Simbologia presente en el archivo TBL.
         */
        setTiles(
                0, // tile x index
                60, // tile y index
                new String[]{"[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",//1
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[",//5
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[)",
                    "[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))",//10
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))",
                    "[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))",//15
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))",//25
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[^[[[[[[[[[[[[))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[^[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))",//35
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))",//38
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[^[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))",
                    "[[[[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%",
                    "[[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%",
                    "[[[[[[[[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%",
                    "[[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%",
                    "[[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%",
                    "[[[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%",
                    "[[[[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%",
                    "[[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%",
                    "[[[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%",});
        /**
         * Mapa completo de tiles que definen el pantano .
         * Simbologia presente en el archivo TBL.
         */
        setTiles(
                80, // tile x index
                60,// tile y index
                new String[]{"[[[[[[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%",//1
                    "[[[[[[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%",
                    "[[^[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%",
                    "[[[[)))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%",
                    "[[))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%",//%
                    "))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%",//10
                    ")))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%",//1%
                    "))))))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",//2%
                    ")))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",//30
                    ")))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",//3%
                    ")))))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",//38
                    ")))))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%==============%%%%%%%",
                    ")))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%=%%%%%%%",
                    "))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%=%%%%%%%%%%%======%%%%%%%",
                    ")))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%=%%%%%%%%%%%%%%%==%%%%%%%",
                    "))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%=%%%%%%%%%%%%%%%==%%%%%%%",
                    ")))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%=%%%%%%%%%%%%%%%==%%%%%%%",
                    ")))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%=%%%%%%%%%%%%%%%==%%%%%%%",
                    "))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%=%%%%%%%%%%%%%%%==%%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%==================%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",});
    }

    public void interacVentana(Jugador pj, Personaje vendedor, String estado) {
        clearKey(73);
        if (cursor.getVentana() == 1) {

            //grillaNpc = new Boton("grilla npc", "grilla npc", viewXOfs() + 10, viewYOfs() + 10, (int) Math.pow(2, 5));
            //grillaPj = new Boton("grilla pj", "grilla pj", viewXOfs() + 200, viewYOfs() + 10, (int) Math.pow(2, 5));
            //ventanaTrade = new Boton("ventana trade", "ventana trade",viewXOfs(),viewYOfs(),0);
//            procesaItem(pj, grillaPj.x, grillaPj.y);
//            procesaItem(vendedor, grillaNpc.x, grillaNpc.y);
            //seccion.setSeccion(new JGPoint(10,10), new JGPoint(3,4));
            seccion.removerIconos();
            seccion.setWorking(false);
            seccionNpc.setWorking(false);

//            pj.colid = 0;
            seccion.generaSeccion(pj, 1);
            menu.recibeHm(hmIconoItem, 1, filtro);
            pj.bloquear();

            seccionNpc.setSeccion(new JGPoint(160, 210), new JGPoint(3, 4));
            seccionNpc.generaSeccion(vendedor, 1);
            menu.recibeHm(hmIconoItem, 0, filtro);

            cerrar = new Boton("cerrar", "cerrar", 230, 320, (int) Math.pow(2, 5), 0, 0);
            cerrar.pintar();

            cursor.setVentana((byte) 2);
        } else if ((cursor.getVentana() == 3) || (cursor.getVentana() == 4)) {
//            Remueve todos los objetos que forman la ventana de comerciar
//            removeObjects("ventana trade", 0);
//            removeObjects("grilla npc", (int) Math.pow(2, 5));
//            removeObjects("grilla pj", (int) Math.pow(2, 5));
            removeObjects("cerrar", (int) Math.pow(2, 5));
//            for (int i = 0; i < 200; i++) {

            //Renueve todos los objetos item
            seccion.removerIconos();
//            seccionNpc.setWorking(false);



            pj.desbloquear();
            pj.colid = 2;


            if (cursor.getVentana() == 4) {
                cursor.setVentana((byte) 1);

            } else {
                cursor.setVentana((byte) 0);
            }
//            cursor.setVentana((byte) 0);
            setGameState(estado);
        }
    }

    public void interacVentana(Personaje mob, String estado) {
        clearKey(73);
        if (cursor.getVentana() == 1) {
            seccion.setWorking(false);
            seccionNpc.setWorking(false);
            seccion.setSeccion(new JGPoint(110 + viewXOfs(), 400 + viewYOfs()), new JGPoint(12, 1));
            seccion.generaSeccion(pj, 1);
            menu.recibeHm(hmIconoItem, 1, filtro);
            pj.bloquear();

            seccionNpc.setSeccion(new JGPoint(200, 200), new JGPoint(6, 1));
            seccionNpc.generaSeccion(mob, 1);
            seccionNpc.setWorking(true);
            menu.recibeHm(hmIconoItem, 0, filtro);

            cursor.setVentana((byte) 2);
        } else if ((cursor.getVentana() == 3) || (cursor.getVentana() == 4)) {

            //Remueve todos los objetos item
            seccion.removerIconos();
//            seccionNpc.setWorking(false);
            pj.desbloquear();
            pj.colid = 2;
            if (cursor.getVentana() == 4) {
                cursor.setVentana((byte) 1);

            } else {
                cursor.setVentana((byte) 0);
            }
//            cursor.setVentana((byte) 0);
            setGameState(estado);
        }

    }

    private void dibujarObjetosEscenario() throws SQLException {

        new Npc(700, 75, "alcaldia", "casa4", (int) Math.pow(2, 6), 0, (short) 105, conect, new String[]{});
        new Npc(680, 660, "casa1", "casa3", (int) Math.pow(2, 6), 0, (short) 100, conect, new String[]{});
        new Npc(80, 400, "casa2", "casa2", (int) Math.pow(2, 6), 0, (short) 101, conect, new String[]{"Casa 2"});
        new Npc(350, 448, "casa3", "casa4", (int) Math.pow(2, 6), 0, (short) 102, conect, new String[]{"Casa 3"});
        new Npc(80, 634, "casa3", "casa3", (int) Math.pow(2, 6), 0, (short) 103, conect, new String[]{"Casa 3"});
        new Npc(350, 682, "casa3", "casa5", (int) Math.pow(2, 6), 0, (short) 104, conect, new String[]{"Casa 3"});
        new Npc(352, 64, "arbol1", "arbol", (int) Math.pow(2, 6), 0, (short) 106, conect, new String[]{"Hola amiguirijillo", "soy Don Arbol, cuidame"});//
        new Npc(288, 32, "arbol2", "arbol", (int) Math.pow(2, 6), 0, (short) 107, conect, new String[]{"Hola amiguirijillo", "soy Don Arbol, cuidame"});//
        new Npc(128, 64, "arbol2", "pileta", (int) Math.pow(2, 6), 0, (short) 108, conect, new String[]{"Hola amiguirijillo", "soy la fuente magica"});//
        new Npc(16 * 80, 16 * 10, "guardia", "guard_stand_r", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"Guardia: vé con cuidado"});
        new Npc(16 * 80, 16 * 21, "guardia", "guard_stand_r", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"Guardia: vé con cuidado"});
        new Npc(16 * 16, 16 * 12, "viajero", "viajero", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"Viajero: "});
        new Npc(16 * 10, 16 * 17, "mono", "mono", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"Mono: "});
        new Npc(16 * 10, 16 * 110, "perdido", "perdido", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"perdido: "});
        new Npc(16 * 80, 16 * 8, "escultura", "escultura", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(16 * 80, 16 * 24, "escultura", "escultura", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(16 * 100, 16 * 35, "arbol_seco1", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(16 * 140, 16 * 100, "arbol_seco2", "arbol_seco", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(16 * 130, 16 * 110, "arbol_seco3", "arbol_seco", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(16 * 60, 16 * 100, "arbol_seco4", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(80, 1616, "arbol_seco5", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(144, 1616, "arbol_seco6", "casa_5", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(608, 1608, "arbol_seco7", "arbol_seco", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(880, 1184, "arbol_seco8", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(816, 1520, "arbol_seco9", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(752, 1136, "arbol_seco10", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(1072, 1408, "arbol_seco11", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(1168, 1248, "arbol_seco12", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(640, 960, "arbol_seco13", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(544, 960, "arbol_seco14", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(1564, 1232, "arbol_seco15", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(1632, 1040, "arbol_seco16", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(1840, 832, "arbol_seco17", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(2256, 848, "arbol_seco18", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(2352, 608, "arbol_seco19", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(2240, 336, "arbol_seco20", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(2016, 160, "arbol_seco21", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(1728, 160, "arbol_seco22", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        new Npc(1472, 96, "arbol_seco23", "arbol_seco2", (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
        //pongo ramas al azar fuera dela ciudad
        int cont = 1;
        for (int i = 0; i < 100; i++) {
            if (cont == 4) {
                cont = 1;
            }
            Random eje_x = new Random(), eje_y = new Random();
            int p = eje_x.nextInt((16 * 160));
            int q = eje_y.nextInt((16 * 120));
            if (p <= 1280 && q <= 960) {
                q += 960;
            }
            new Npc(p, q, "rama" + i, "rama" + cont, (int) Math.pow(2, 6), (short) 4, (short) 20, conect, new String[]{"escultura: "});
            cont++;
        }
    }

    public class Ventana {

        private String[] mensajes;
        private int segundos;
        private Boolean esAlerta;
        private String imagen = null;
        final static byte LINEAS = 3;
        private int x;
        private int y;
        private int ventanas = 1;
        private int ventana_actual;
        private boolean wait;
        private JGTimer espera;
        private String dialogo;
        private byte tamanoLinea;
        private int lineasTotal;
        private int linea_concurrente;
        private int separadorLinea;
        private int indiceUltimaLetra;
        private int indice_concurrente;
        private String dialogo_restante, dialogo_mostrar, linea_1, linea_2, linea_3, mensaje_flash = "";
        private boolean terminoDialogo;
        private StdScoring mensajito = new StdScoring("freak", 480, 380, 0, 0, -1, "", new JGFont("arial", 1, 10), new JGColor[]{JGColor.blue, JGColor.white}, 20, false);

        /**
         * Muestra una ventana del tipo alerta usada para mostrar mensajes flash
         *
         * @param mensaje
         */
        public Ventana(String mensaje) {
            this.x = 255;
            this.y = 305;
            this.esAlerta = true;
            this.segundos = 3;
            this.desplegarVentana(100, 100);
            this.desplegarMensaje(130, 130, mensaje);


        }

        public Ventana(double x, double y, String[] mensajes) {
            this.mensajes = mensajes;
            this.x = (int) x;
            this.y = (int) y;

            if (mensajes.length % 3 != 0) {
                this.ventanas = Math.abs(mensajes.length / 3) + 1;
            } else {
                this.ventanas = Math.abs(mensajes.length / 3);
            }
            this.ventanas++;
            this.ventana_actual = 1;
            //remove();

        }

        public void setDialogo(String dia) {
            this.dialogo = dia;
            this.dialogo_restante = dia;
        }

        private Ventana() {
        }

        /**
         * despliega una lista de mensajes en fragmetos de 3 líneas
         * @param mensajes
         * @param x coordenada X donde se ubica la ventana
         * @param y coordenada Y donde se ubica la ventana
         */
        public void desplegarMensaje(double x, double y, String[] mensajes) {
            separadorLinea = 16;
            desplegarVentana(x, y);
            setColor(JGColor.black);
            setFont(new JGFont("Arial", 0, 10));
            int linea = (ventana_actual - 1) * LINEAS;
            if (mensajes.length > linea) {
                desplegarMensaje(x + 60, y + separadorLinea * 1, mensajes[linea]);
                linea++;
            }
            if (mensajes.length > linea) {
                desplegarMensaje(x + 60, y + separadorLinea * 2, mensajes[linea]);
                linea++;
            }
            if (mensajes.length > linea) {
                desplegarMensaje(x + 60, y + separadorLinea * 3, mensajes[linea]);
                linea++;
            }
            if (espera == null || !espera.running) {
                espera = new JGTimer(60 * 1, true) {

                    @Override
                    public void alarm() {
                        setWait(false);
                    }
                };
            }

        }

        public boolean isWait() {
            return wait;
        }

        public void setWait(boolean wait) {
            this.wait = wait;
        }

        public void avanzarTexto() {
            if (ventana_actual <= ventanas) {
                if (getKey(KeyEnter) && !isWait()) {
                    setWait(true);
                    espera = null;
                    ventana_actual++;
                }
                desplegarMensaje(x, y, mensajes);
            }
        }

        private void limpiarVentana(double x, double y) {
            setColor(JGColor.yellow);
            drawRect(viewXOfs() + x + 5, viewYOfs() + y + 5, 290, 90, true, false);
        }

        /**
         *despliega mensaje en el centro de la pantalla
         * @param mensaje
         */
        public void desplegarMensaje(String mensaje) {
            setColor(JGColor.red);
            setFont(new JGFont("Arial", 0, 16));
            drawString(mensaje, viewWidth() / 2, viewHeight() / 2 + 45, 0);
        }

        /**
         * despliega mensaje en la posicion indicada
         * @param x
         * @param y
         * @param mensaje
         */
        public void desplegarMensaje(double x, double y, String mensaje) {
            setColor(JGColor.red);
            //eng.setFont(new JGFont("Arial", 0, 16));
            drawString(mensaje, x, y, 0);

        }

        private void desplegarVentana() {
            //borde externo
            setColor(JGColor.red);
            drawRect(200, 250, 300, 100, true, false, false);
            //interior
            setColor(JGColor.white);
            drawRect(205, 255, 290, 90, true, false, false);
        }

        private void desplegarVentana(double x, double y) {
            //borde externo
            setColor(JGColor.red);
            drawRect(x, y, 300, 100, true, false, false);
            //interior
            setColor(JGColor.white);
            drawRect(x + 5, y + 5, 290, 90, true, false, false);
        }

        //private void
        /**
         * Get the url value of imagen
         *
         * @return the url value of imagen
         */
        public String getImagen() {
            return imagen;
        }

        /**
         * Set the value of imagen
         *
         * @param url new url value of imagen
         */
        public void setImagen(String url) {
            this.imagen = url;
        }

        /**
         * Get the value of esAlerta
         *
         * @return the value of esAlerta
         */
        public Boolean getEsAlerta() {
            return esAlerta;
        }

        /**
         * Set the value of esAlerta
         *
         * @param esAlerta new value of esAlerta
         */
        public void setEsAlerta(Boolean esAlerta) {
            this.esAlerta = esAlerta;
        }

        /**
         * Get the value of segundos
         *
         * @return the value of segundos
         */
        public int getSegundos() {
            return segundos;
        }

        /**
         * Set the value of segundos
         *
         * @param segundos new value of segundos
         */
        public void setSegundos(int segundos) {
            this.segundos = segundos;
        }

        public String[] getMensajes() {
            return mensajes;
        }

        public void setMensajes(String[] mensajes) {
            this.mensajes = mensajes;
        }

        /**
         * Setea la cantidad de lineas totales del dialogo total
         * 
         * @param txt
         * @param ventaniwi
         */
        public void activarParametrosFormateoTexto(String txt, JGRectangle ventaniwi) {
            byte pixelLetra = 5;
            this.tamanoLinea = (byte) (ventaniwi.width / pixelLetra);
            this.lineasTotal = txt.length() / tamanoLinea;
            this.linea_concurrente = 1;
            terminoDialogo = false;
            linea_1 = this.generarLinea();
            linea_2 = this.generarLinea();
            linea_3 = this.generarLinea();
        }

        public void activarParametrosFormateoTexto() {
            byte pixelLetra = 5;
            this.tamanoLinea = (byte) (300 / pixelLetra);
            this.lineasTotal = dialogo.length() / tamanoLinea;
            this.linea_concurrente = 1;
            terminoDialogo = false;
            linea_1 = this.generarLinea();
            linea_2 = this.generarLinea();
            linea_3 = this.generarLinea();
        }

        private void mostrarTexto() {
            dibujarVentana();
            desplegarMensaje(350, 260 + 16, this.linea_1);
            desplegarMensaje(350, 260 + 32, this.linea_2);
            desplegarMensaje(350, 260 + 48, this.linea_3);
        }

        public void siguiente() {
            this.siguientesLineas();
        }

        private String generarLinea() {
            if (!terminoDialogo) {
                if (dialogo_restante.length() > tamanoLinea) {
                    //corto la cadena de la linea
                    String subStr = this.dialogo_restante.substring(1, tamanoLinea);

                    //busco el ultimo espacio dentro del subString
                    indiceUltimaLetra = subStr.lastIndexOf(" ");
                    //corto el String real hacia el ultimo espacio blanco
                    String subStr2 = this.dialogo_restante.substring(0, indiceUltimaLetra + 1);

                    //guardo las oraciones qlias ctm de la mierda
                    dialogo_mostrar = subStr2;
                    dialogo_restante = this.dialogo_restante.substring(indiceUltimaLetra + 1);
                    //aumenta la linea
                    linea_concurrente++;
                } else {
                    dialogo_mostrar = dialogo_restante;
                    linea_concurrente = lineasTotal;
                    this.terminoDialogo = true;
                }
                return dialogo_mostrar;
            } else {
                return "";
            }
        }

        public void siguientesLineas() {
            linea_1 = this.generarLinea();
            linea_2 = this.generarLinea();
            linea_3 = this.generarLinea();

        }

        private void dibujarVentana() {
            //borde externo
            setColor(JGColor.green);
            drawRect(viewXOfs() + 200, viewYOfs() + 250, 300, 100, true, false);
            //interior
            setColor(JGColor.white);
            drawRect(viewXOfs() + 205, viewYOfs() + 255, 290, 90, true, false);
        }

        public void mostrarMensajeValidacion(String txt) {

            new StdScoring("validacion", 480 + viewXOfs(), 370 + viewYOfs(), 0, 0, 120, txt, new JGFont("arial", 1, 10), new JGColor[]{JGColor.yellow, JGColor.black}, 20);
            this.mensaje_flash = txt;
        }

        public void mostrarDatoFreak(String txt) {
            this.mensajito.msg = txt;
//            if (!this.mensaje_flash.equals(txt) || this.mensajito.cycletimer >= this.mensajito.expiry ) {//cambio de mensaje
//                if (mensajito != null ) mensajito.suspend();
//
//                this.mensaje_flash = txt;
//            }else {
//                this.mensajito.paintB();
//            }
        }
    }

    public class Cursor extends JGObject {

        private String mensaje = new String();
        private double ejex =
                eng.getMouseX() + eng.viewXOfs();
        private double ejey = eng.getMouseY() + eng.viewYOfs();
        private byte ventana;
        private String mensajeIcon;
        private JGPoint puntos;
        private String nombre = null;
        private String descripcion = null;
        private String peso = null;
        private String valor = null;
        private String tipo = null;
        private String usoCombat = null;
        private String danoBeneficio = null;
        private String costo = null;
        private String lvl = null;
        private String maxLvl = null;
        private String recompensa = null;
        private String pjEntregar = null;
        private String nota = null;
        private boolean limpiarIconos = false;

        public boolean isLimpiarIconos() {
            return limpiarIconos;
        }

        public void setLimpiarIconos(boolean limpiarIconos) {
            this.limpiarIconos = limpiarIconos;
        }
//        private String nombre, descripcion,

        public JGPoint getPuntos() {
            return puntos;
        }

        public void setPuntos(JGPoint puntos) {
            this.puntos = puntos;
        }

        public byte getVentana() {
            return ventana;
        }

        public void setVentana(byte ventana) {
            this.ventana = ventana;
        }

        public double getEjex() {
            return ejex;
        }

        public void setEjex(double ejex) {
            this.ejex = ejex;
        }

        public double getEjey() {
            return ejey;
        }

        public void setEjey(double ejey) {
            this.ejey = ejey;
        }

        public String getMensaje() {
            return mensaje;
        }

        public void setMensaje(String mensaje) {
            this.mensaje = mensaje;
        }

        public Cursor() {
            super("cursor", false, 0, 0, (int) Math.pow(2, 0), "cursor");
            this.setTileBBox(0, 0, 2, 2);
            this.setBBox(0, 0, 2, 2);

        }
        int oldmousex = 0, oldmousey = 0;
        boolean scissors_c = false;

        @Override
        public void move() {
            // only follow mouse if it moves
            int mx = eng.getMouseX() + eng.viewXOfs();
            int my = eng.getMouseY() + eng.viewYOfs();
            if (mx != oldmousex) {
                x = mx;
            }
            if (my != oldmousey) {
                y = my;
            }
            oldmousex = mx;
            oldmousey = my;
        }

        @Override
        public void hit(JGObject obj) {
            if (obj instanceof Icono) {
                if (((Icono) obj).getTipo() == 0) {//habilidades{
                    ventanaManager.mostrarDatoFreak("Habilidad: " + ((Icono) obj).getHabilidad().getNombre());
                } else {
                    ventanaManager.mostrarDatoFreak("Ítem: " + ((Icono) obj).getItem().getNombre());
                }
            }
//                         ventanaManager.mostrarDatoFreak("Obj: "+obj.colid);
            if ((obj.colid == (int) Math.pow(2, 4))) {//es icono
                Icono iconito = (Icono) obj;
                this.setMensajeIcon(iconito.getNombreLogico());
            }
            setMensaje("Soy " + obj.getName());
            if ((obj.getGraphic().equals("cerrar")) && (getMouseButton(3))) {
                setVentana((byte) 3);
            }
            //click en NPC
            if (obj.colid == (int) Math.pow(2, 3) && obj.y < viewHeight() - 110 + viewYOfs() && obj.x < viewXOfs() + viewWidth() - 110) {
                Npc npc_procesar = (Npc) obj;
                switch (npc_procesar.getTipo()) {
                    case 2://npc de misiones
                        npc_concurrente = npc_procesar;
                        ventanaManager.mostrarDatoFreak("Personaje: " + npc_procesar.getNombre());
                        if (getMouseButton(3)) {
                            clearMouseButton(3);
                            //veo qué misión tiene el npc que el personaje no tenga (también veo el
                            Iterator it = npc_procesar.getMisiones().getMisiones().entrySet().iterator();
                            short mision_id = -1;
                            short accion = 0;
                            /**
                             * -1 = invalido
                             *  0 = agregar
                             *  1 = agregar
                             *  2 = comprobar
                             *
                             */
                            while (it.hasNext() && accion == 0) {
                                Map.Entry e = (Map.Entry) it.next();
                                Mision mis = npc_procesar.getMisiones().getMisiones().get(Short.parseShort(e.getKey().toString())).getMision();
                                if (!mis.isRepetible()) {//no es repetible
                                    if (!pj.getMisiones().isHizoMision(mis.getIdMision(), misiones.get(mis.getIdMision()))) {//nunca ha hecho la mision
                                        if (!pj.getMisiones().isHaciendoMision(mis.getIdMision())) {//no la esta desarrollando
                                            //si llega hasta acá, el pj nunca en su vida  ha tomado la misión
                                            accion = 1;//agregar
                                            //Validar que el personaje tenga nivel necesario

                                        } else {
                                            //comprobar si cumple la misión
                                            accion = 2;
                                        }
                                    }
                                } else {
                                    if (!pj.getMisiones().isHaciendoMision(mis.getIdMision())) {//no la esta desarrollando
                                        accion = 1;//agregar
                                    } else {
                                        //comprobar si cumple la misión
                                        accion = 2;
                                    }
                                }
                                mision_id = mis.getIdMision();
                                if (mis.getNivelRequerido() > pj.getNivel()) {
                                    accion = 0;
                                }
                            }
                            switch (accion) {
                                case 0://el personaje hizo todas las misiones o no tiene ninguna
                                    //aca se pone el dialogo normal
                                    ventanaManager.setDialogo(npc_concurrente.obtieneDialogo());

                                    break;
                                case 1: //el personaje no tiene la misión.. agregar

                                    pj.getMisiones().agregarMision(mision_id, (short) 1, misiones.get(mision_id));
                                    //pasamos al segundo dialogo (Comprobación)
                                    ventanaManager.setDialogo(npc_concurrente.getMisiones().getMisiones().get(mision_id).getMision().getDialogo().dialogoIniciarMision());
                                    break;
                                case 2://Comprobar Mision
                                    //Busco si el PJ tiene los objetos que la mision requiere
                                    //recorro el hashmap de la lista de objetos asociados A LA MISION
                                    /**Iterator **/
                                    it = npc_procesar.getMisiones().getMisiones().get(mision_id).getMision().getRequerimientos().getObjetos().entrySet().iterator();
                                    boolean fail = false;
                                    int cont = 0;
                                    while (it.hasNext() && !fail) {
                                        Map.Entry e = (Map.Entry) it.next();
                                        short objeto_id = Short.parseShort(e.getKey().toString());
                                        short cantidad = npc_procesar.getMisiones().getMisiones().get(mision_id).getMision().getRequerimientos().getObjetos().get(objeto_id).getCantidad();
                                        if (!pj.getInventario().tieneItem(objeto_id, cantidad)) {
                                            fail = true;
                                        }

                                        cont++;
                                    }
                                    String stringRelleno = "...                                                      ...";//30 espacios

                                    if (fail) {//no tiene los requerimientos para cumplir la mision
                                        ventanaManager.setDialogo(npc_concurrente.getMisiones().getMisiones().get(mision_id).getMision().getDialogo().dialogoComprobarMision() + stringRelleno + npc_concurrente.getMisiones().getMisiones().get(mision_id).getMision().getDialogo().dialogoFalloMision());
                                    } else {
                                        //cumplio los requerimientos...
                                        Mision misi = npc_procesar.getMisiones().getMisiones().get(mision_id).getMision();
                                        int nivel = pj.getNivel();
                                        //Se da la experiencia
                                        pj.aumentarExperiencia(misi.getRecompensaExp());
                                        String mjs;
                                        if (nivel != pj.getNivel()) {
                                            mjs = "¡ Has alcanzado el nivel " + pj.getNivel() + " !";
                                        } else {
                                            mjs = "¡ +" + misi.getRecompensaExp() + " de experiencia !";
                                            eng.playAudio("evento", "hallar_algo", false);
                                        }
                                        new StdScoring("pj_exp", pj.x, pj.y + 100, 0, -2, 120, mjs, new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green}, 10);


                                        interacVentana(npc_concurrente, "InInteraction");
                                        pj.getMisiones().completarMision(misi.getIdMision());
                                        //se da los item que tiene asociado el personaje que le dio la misión (NPC_CONCURRENTE)
                                        ventanaManager.setDialogo(npc_concurrente.getMisiones().getMisiones().get(mision_id).getMision().getDialogo().dialogoComprobarMision() + stringRelleno + stringRelleno + npc_concurrente.getMisiones().getMisiones().get(mision_id).getMision().getDialogo().dialogoCumplirMision());
                                        //se restan de su inventario
                                        for (Map.Entry id : misi.getRequerimientos().getObjetos().entrySet()) {
                                            short objeto_id = Short.parseShort(id.getKey().toString());
                                            short cantidad = misi.getRequerimientos().getObjetos().get(objeto_id).getCantidad();
                                            pj.getInventario().eliminarItem(objeto_id, cantidad);
                                        }
                                        //playAudio("evento", "hallar_algo", false);
                                        entregarRecompensa = true;
                                    }


                                    break;
                                default:
                                    break;
                            }

                            ventanaManager.activarParametrosFormateoTexto();
                            setGameState("InInteraction");
                            salirInInteracting = false;
                        }
                        break;
                    case 1://npc vendedor
                        ventanaManager.mostrarDatoFreak("Vendedor: " + ((Personaje) (obj)).getNombre());
                        //setMensaje("Vendedor: Hola " + pj.getNombre() + ", deseas hacer un trato     ?" + obj.colid);
                        if (getMouseButton(3)) {
                            vendedor_concurrente = (Npc) obj;
                            setVentana((byte) 1);
                            mostrarVestir = 0;
                        }
                    //playAudio("evento", "mensaje", false);

                }
            }
            if ((obj.colid == (int) Math.pow(2, 4)) && (getMouseButton(3)) & (inGameState("InCombat"))) {
                clearMouseButton(3);
                setIcon((Icono) obj);
            }
            //click en algun boton
            if (obj.colid == (int) Math.pow(2, 5)) {
                Boton boton = (Boton) obj;
                if (boton.getId() == 31 && getMouseButton(3)) {
                    clearMouseButton(3);

                    if (mostrarVestir == 1) {
                        mostrarVestir = 0;
                        menu.vestir(false);

                    } else if (mostrarVestir == 0) {
                        seccion.removerIconos();
                        seccion.setWorking(false);
                        mostrarVestir = 1;
                        menu.vestir(true);
                    }
                }
                if (inGameState("InInteraction")) {
                    if (getMouseButton(3)) {
                        clearMouseButton(3);
                        Boton btn = (Boton) obj;
                        //Comportamiento de los botones al momento de interactuar...
                        switch (btn.getTipo_boton()) {
                            case 0: //Presiono boton cerrar en un dialogo
                                salirInInteracting = true;
                                if (entregarRecompensa) {
                                    //seteo todo para entregar la recompensa
                                    personaje_concurrente = (Personaje) npc_concurrente;
//                                    seccion.removerIconos();
                                    cursor.setVentana((byte) (1));
                                    setGameState("InReward");
                                } else {
                                    setGameState("InWorld");
                                }
                                npc_concurrente.getDialogo().nextTexto();
                                npc_concurrente = null;


                                removeObject(obj);
                                break;
                            case 5: //presiono boton siguiente para las siguientes lineas del dialogo
                                ventanaManager.siguiente();
                                removeObject(obj);
                                break;
                        }
                    }
                }
                if (obj.x >= viewXOfs() + (viewWidth() - 180)) {// es del menu
                    if (boton.getTipo_boton() != 3) {
                        if (boton.getTipo_boton() == 4) {
                            ventanaManager.mostrarDatoFreak("Eliminar");
                        } else if (boton.getTipo_boton() == 1) {
                            ventanaManager.mostrarDatoFreak("Asignar puntos");
                        }
                        if (getMouseButton(3)) {
                            clearMouseButton(3);
                            //corresponde al menu y no es boton del tipo "ver"
                            switch (menu.getMenuActual()) {
                                case 4://Menú está en Estadísticas
                                    if (pj.getTotalPuntosEstadistica() > 0) {
                                        // playAudio("evento", "permitido", false);
                                        switch (boton.getId()) {
                                            /**
                                             * ids para estadisticas
                                             * 1 = fuerza
                                             * 2 = destreza
                                             * 3 = Sabiduria
                                             * 4 = vitalidad
                                             */
                                            case 1:
                                                pj.aumentarFuerza(1);
                                                pj.gastarPuntoEstadistica();
                                                break;//
                                            case 2:
                                                pj.aumentarDestreza(1);
                                                pj.gastarPuntoEstadistica();
                                                break;
                                            case 3:
                                                pj.aumentarSabiduria(1);
                                                pj.gastarPuntoEstadistica();
                                                break;
                                            case 4:
                                                pj.gastarPuntoEstadistica();
                                                pj.aumentarVitalidad(1);
                                                break;
                                        }
                                    }
                                    break;
                                case 1://Menú está en Habilidad
                                    if (pj.getTotalPuntosHabilidad() > 0) {
                                        //playAudio("evento", "permitido", false);
                                        if (!pj.getHabilidades().tieneHabilidad((short) boton.getId())) {
                                            pj.getHabilidades().agregaHabilidad((short) boton.getId(), habilidades.get((short) boton.getId()));
                                            pj.gastarPuntosHabilidad();
                                        } else if (pj.getHabilidades().getHabilidad((short) boton.getId()).puedeAumentar()) {
                                            pj.getHabilidades().aumentarNivel((short) boton.getId());
                                            pj.gastarPuntosHabilidad();
                                        }
                                        seccion.setWorking(false);
                                        setLimpiarIconos(true);
                                    }
                                    break;
                                case 2://Menú está en misión
                                    if (boton.getTipo_boton() == 4) {//boton corresponde al tipo abandonar
                                        pj.getMisiones().abandonaMision((short) boton.getId());
                                        //playAudio("evento", "no_permitido", false);
                                    }
                                    break;
                            }
                        }
                    } else if (boton.getTipo_boton() == 3) {
                        //corresponde al menu y es boton del tipo "ver"
                        //por comportamiento mouse over , se muestra información
                        //en el monitor
                        ventanaManager.mostrarDatoFreak("Ver descripción");
                        switch (menu.getMenuActual()) {
                            case 4://Menú está en Estadísticas
                                switch (boton.getId()) {
                                    /**
                                     * ids para estadisticas
                                     * 1 = fuerza
                                     * 2 = destreza
                                     * 3 = Sabiduria
                                     * 4 = vitalidad
                                     */
                                    case 1:
                                        setInformacionEstadistica("Fuerza", "Aumenta el daño que causas", "La distribución de puntos no tiene restricción");
                                        break;//
                                    case 2:
                                        setInformacionEstadistica("Destreza", "Aumenta el la sanación en ti", "La distribución de puntos no tiene restricción");
                                        break;
                                    case 3:
                                        setInformacionEstadistica("Sabiduría", "Reduce el daño que recibes", "La distribución de puntos no tiene restricción");
                                        break;
                                    case 4:
                                        setInformacionEstadistica("Vitalidad", "Aumenta el los puntos de vida que posees", "La distribución de puntos no tiene restricción");
                                        break;
                                }
                                break;
                            case 1://Menú está en Habilidad
                                Habilidad hab = habilidades.get((short) boton.getId());
                                setInformacionHabilidad(hab.getNombre(), hab.getDescripcion(), String.valueOf(hab.getDanoBeneficio()), String.valueOf(hab.getCostoBasico()), String.valueOf(hab.getNivelMaximo()));
                                break;
                            case 2://Menú está en misión
                                Mision mis = pj.getMisiones().getMisiones().get((short) boton.getId()).getMision();
                                setInformacionMision(mis.getNombre(), mis.getDescripcion(), String.valueOf(mis.getRecompensaExp()), String.valueOf(mis.getIdPersonajeConcluyeMision()));
                                break;
                        }
                    }
                }


                if (obj.y >= viewYOfs() + (viewHeight() - 180)) {
                    if ((getMouseButton(3) && (getKey(66))) || (getMouseButton(3) && (getKey(98)))) {
                        clearMouseButton(3);
                        seccion.removerIconos();
                        cursor.setVentana((byte) 4);
                        pj.getInventario().eliminarItem((short) boton.getId(), (short) 1);
                        seccion.setWorking(false);

                    }
                    if (boton.getTipo_boton() == 3 && getMouseButton(3)) {
                        Objeto item = pj.getInventario().getObjetos().get((short) boton.getId()).getObjeto();
                        setInformacionObjeto(item.getNombre(), item.getDescripcion(), String.valueOf(item.getValorDinero()), String.valueOf(item.getTipo()), String.valueOf(item.getPeso()), String.valueOf(item.isUsoCombate()));
                    }

                }

                if (obj.getName().equals("usable") && (getMouseButton(3))) {
                    seccion.removerIconos();
//                    cursor.setLimpiarIconos(true);
                    clearMouseButton(3);
                    filtro = 0;
                    seccion.setWorking(false);
                            if(mostrarVestir==1){
                            mostrarVestir = -1;
                            }else{
                                mostrarVestir=0;
                            }
                    if (inGameState("InCommerce")) {
                        cursor.setVentana((byte) 4);
                    }
                    if (inGameState("InReward")) {
                        cursor.setVentana((byte) 1);
                    }
                } else if (obj.getName().equals("equipo") && (getMouseButton(3))) {
                    seccion.removerIconos();
//                    cursor.setLimpiarIconos(true);
                    clearMouseButton(3);
                    filtro = 1;
                    seccion.setWorking(false);
                            if(mostrarVestir==1){
                            mostrarVestir = -1;
                            }else{
                                mostrarVestir=0;
                            }
//                    if (inGameState("InWorld")) {
//                        mostrarVestir = -1;
//                    }
                    if (inGameState("InCommerce")) {
                        cursor.setVentana((byte) 4);
                    }
                    if (inGameState("InReward")) {
                        cursor.setVentana((byte) 1);
                    }
                } else if (obj.getName().equals("colec") && (getMouseButton(3))) {
                    seccion.removerIconos();
//                    cursor.setLimpiarIconos(true);
                    clearMouseButton(3);
                    filtro = 2;
                    seccion.setWorking(false);
                            if(mostrarVestir==1){
                            mostrarVestir = -1;
                            }else{
                                mostrarVestir=0;
                            }
//                    if (inGameState("InWorld")) {
//                        mostrarVestir = -1;
//                    }
                    if (inGameState("InCommerce")) {
                        cursor.setVentana((byte) 4);
                    }
                    if (inGameState("InReward")) {
                        cursor.setVentana((byte) 1);
                    }
                }
            }

            //click en icono
            if (obj.colid == (int) Math.pow(2, 4)) {
                Icono icon = (Icono) obj;
                if (inGameState("InCommerce")) {
                    if ((getMouseButton(3)) && icon.belongTo(vendedor_concurrente.getTipo())) {
                        clearMouseButton(3);
                        if (pj.validarDinero(icon.getItem().getValorDinero()) && pj.puedellevarItem(icon.getItem().getIdObjeto(), (short) 1, objetos.get(icon.getItem().getIdObjeto()))) {
                            pj.getInventario().agregarItem(icon.getIdObjeto(), objetos.get(icon.getItem().getIdObjeto()));
                            pj.setDinero(pj.getDinero() - icon.getItem().getValorDinero());
                            cursor.setVentana((byte) 4);
                        } else {
                            if (!pj.validarDinero(icon.getItem().getValorDinero())) {
                                ventanaManager.mostrarMensajeValidacion("No tiene suficiente dinero");
                            } else {
                                ventanaManager.mostrarMensajeValidacion("No soportas mas peso");
                            }
                        }
                    }
                    if ((getMouseButton(3)) && icon.belongTo(pj.getTipo()) && icon.getItem().getTipo() == filtro) {
                        clearMouseButton(3);
                        pj.getInventario().eliminarItem(icon.getIdObjeto(), (short) 1);
                        pj.setDinero(pj.getDinero() + icon.getItem().getValorDinero());
                        cursor.setVentana((byte) 4);
                    }

                }
                if (inGameState("InReward")) {
                    if ((getMouseButton(3)) && icon.belongTo(personaje_concurrente.getTipo())) {
                        clearMouseButton(3);
                        if (pj.puedellevarItem(icon.getItem().getIdObjeto(), (short) 1, objetos.get(icon.getItem().getIdObjeto()))) {
                            pj.getInventario().agregarItem(icon.getIdObjeto(), objetos.get(icon.getItem().getIdObjeto()));
                            personaje_concurrente.getInventario().mod_despojar(icon.getIdObjeto());
                            cursor.setVentana((byte) 4);
                        } else {
                            ventanaManager.mostrarMensajeValidacion("No soportas mas peso");
                        }
                    }
                }
                if (inGameState("InWorld")) {
                    if (getMouseButton(3) && ((Icono) obj).getTipo() == 1 && ((Icono) obj).getItem().getTipo() == 0) {
                        clearMouseButton(3);
                        if (!pj.isBloquearUso()) {
                            //el personaje va a usar un item, por que el culiao es entero choro
                            pj.recibirDañoBeneficio(((Icono) obj).getItem().getBeneficio());
                            //playAudio("evento_combate", "heal2", false);
                            new StdScoring("scoring_pj", ((viewWidth() * 10) / 100) + viewXOfs(), (double) 302 + viewYOfs(), -0.09, -1, 160, "" + ((Icono) obj).getItem().getBeneficio() + " HP", new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green, JGColor.yellow}, 5);
                            pj.usarItem(((Icono) obj).getItem());
                        }
                        seccion.removerIconos();
                        seccion.setWorking(false);
                    }
                    if (getMouseButton(3) && ((Icono) obj).getTipo() == 1 && ((Icono) obj).getItem().getTipo() == 1) {
                        clearMouseButton(3);


                        if (obj.y < viewYOfs() + (viewHeight() - 100)) {
                            pj.getInventario().desequipar(((Icono) obj).getItem().getIdObjeto());

                            if(mostrarVestir==1){
                            mostrarVestir = -1;
                            }else{
                                mostrarVestir=0;
                            }

                        }
                        if (obj.y >= viewYOfs() + (viewHeight() - 100)) {
                            pj.getInventario().equipar(((Icono) obj).getItem().getIdObjeto());

                          if(mostrarVestir==1){
                            mostrarVestir = -1;
                            }else{
                                mostrarVestir=0;
                            }
                        }
                        seccion.removerIconos();
                        seccion.setWorking(false);
//                        clearKey(90);clearKey(122);
                    }
                }
            }
        }

        public void desplegarInformacion() {
            setFont(new JGFont("Arial", 0, 10));
            setColor(JGColor.white);
            if (nombre != null) {
                drawString("Nombre : " + nombre, 16, viewHeight() - 180, -1);
            }
            if (descripcion != null) {
                drawString("Descripción : " + descripcion, 16, viewHeight() - 170, -1);
            }
            if (valor != null) {
                drawString("Valor : " + valor, viewWidth() / 2, viewHeight() - 180, -1);
            }
            if (peso != null) {
                drawString("Peso : " + peso, viewWidth() / 2, viewHeight() - 170, -1);
            }
            if (tipo != null) {
                drawString("Tipo : " + tipo, viewWidth() / 2, viewHeight() - 160, -1);
            }
            if (usoCombat != null) {
                drawString("Uso Combate : " + usoCombat, viewWidth() / 2, viewHeight() - 150, -1);
            }
            if (danoBeneficio != null) {
                if (Integer.parseInt(danoBeneficio) > 0) {
                    drawString("Beneficio : " + danoBeneficio, viewWidth() / 2, viewHeight() - 180, -1);
                } else if (Integer.parseInt(danoBeneficio) < 0) {
                    drawString("Daño : " + danoBeneficio, viewWidth() / 2, viewHeight() - 180, -1);
                }
            }
            if (costo != null) {
                drawString("Costo mp : " + costo, viewWidth() / 2, viewHeight() - 170, -1);
            }
            if (maxLvl != null) {
                drawString("Nivel máx : " + maxLvl, viewWidth() / 2, viewHeight() - 160, -1);
            }
            if (recompensa != null) {
                drawString("Recompensa exp : " + recompensa, viewWidth() / 2, viewHeight() - 180, -1);
            }
            if (pjEntregar != null) {
                drawString("A quién entregar : " + pjEntregar, viewWidth() / 2, viewHeight() - 150, -1);
            }
            if (nota != null) {
                drawString("Nota : " + nota, viewWidth() / 2, viewHeight() - 110, -1);
            }
        }

        private void setInformacion(String nom, String desc, String val, String pes, String tip, String usoCom, String dano, String cos,
                String lvl, String maxLvl, String rec, String pjEnt, String not) {
            this.nombre = nom;
            this.descripcion = desc;
            this.valor = val;
            this.peso = pes;
            this.tipo = tip;
            this.usoCombat = usoCom;
            this.danoBeneficio = dano;
            this.costo = cos;
            this.lvl = lvl;
            this.maxLvl = maxLvl;
            this.recompensa = rec;
            this.pjEntregar = pjEnt;
            this.nota = not;

        }

        public void limpiarInformacion() {
            setInformacion(null, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        public void setInformacionHabilidad(String nom, String desc, String dano, String cos, String pmaxLvl) {
            setInformacion(nom, desc, null, null, null, null, dano, cos, null, pmaxLvl, null, null, null);
        }

        public void setInformacionObjeto(String nom, String desc, String val, String tip, String pes, String usoCom) {
            setInformacion(nom, desc, val, pes, tip, usoCom, null, null, null, null, null, null, null);
        }

        public void setInformacionMision(String nom, String desc, String rec, String pjEnt) {
            setInformacion(nom, desc, null, null, null, null, null, null, null, null, rec, pjEnt, null);
        }

        public void setInformacionEstadistica(String nom, String desc, String not) {
            setInformacion(nom, desc, null, null, null, null, null, null, null, null, null, null, not);
        }

        public String getMensajeIcon() {
            return mensajeIcon;
        }

        public void setMensajeIcon(String mensajeIcon) {
            this.mensajeIcon = mensajeIcon;
        }
    }

    public class Seccion {

        private int pos_inicial_x, pos_inicial_y;
        private JGPoint recorrido;
        private int tabla_inicial_x, tabla_inicial_y;
        private JGPoint tabla;
        private boolean working = false;

        public Seccion() {
        }

        public boolean isWorking() {
            return working;
        }

        public void setWorking(boolean working) {
            this.working = working;
        }

        public void generaSeccion(Personaje personaje, int tipo) {
            Iterator it;
            int cantidad = 0;
            if (!isWorking()) {
                switch (tipo) {
                    case 0:
                        hmIconoHabilidades = new HashMap<Integer, Icono>();
                        cantidad = 0;
                        ContrincanteHabilidad listHab = personaje.getHabilidades();
                        it = listHab.getHabilidades().entrySet().iterator();
                        while (this.tabla.y > 0) {
                            while (this.tabla.x > 0) {
                                if (it.hasNext()) {
                                    Map.Entry e = (Map.Entry) it.next();
                                    Habilidad hab = listHab.getHabilidad(Short.parseShort(e.getKey().toString())).getHabilidad();
                                    cantidad++;
                                    hmIconoHabilidades.put(cantidad, new Icono("icono", this.recorrido.x, this.recorrido.y, hab.getNombreGrafico(), hab.getIdHabilidad(), (short) 0, listHab.getHabilidad(hab.getIdHabilidad()).getNivelHabilidad(), personaje.getTipo(), hab.getNombre(), hab));
                                    if (inGameState("InReward")) {
                                    }
                                    this.recorrido.x += 37;
                                }
                                this.tabla.x--;
                            }
                            this.recorrido.x = pos_inicial_x;
                            this.tabla.x = tabla_inicial_x;
                            this.tabla.y--;
                            this.recorrido.y += 37;
                        }
                        break;
                    case 1:
                        hmIconoItem = new HashMap<Integer, Icono>();
                        Inventario inv = personaje.getInventario();
                        it = inv.getObjetos().entrySet().iterator();
                        boolean fin = false;
                        cantidad = 0;
                        while ((this.tabla.y > 0) && (!fin)) {
                            while ((this.tabla.x > 0) && (!fin)) {

                                if (it.hasNext()) {

                                    Map.Entry en = (Map.Entry) it.next();
                                    Objeto obje = inv.getItem(Short.parseShort(en.getKey().toString())).getObjeto();
                                    //Objeto obje = inv.getElObjeto(Short.parseShort(en.getKey().toString()));
                                    
                                        cantidad++;
                                        if (personaje.getTipo() == 0) {
                                            if (mostrarVestir == 1&& inGameState("InWorld") ) {

//                                            System.out.println("entra again"+inv.getEquipo().get(obje.getIdObjeto()).getEquipado());
                                                if (obje.getTipo() == 1 && inv.getEquipo().get(obje.getIdObjeto()).getEquipado() == 1) {
                                                    if (pj.getInventario().getEquipo().get(obje.getIdObjeto()).getEquipaEn() == 1) {
                                                        equipo1 = new Icono("1icono", 115, 80, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje);
//                                                    equipo1.suspend();
                                                    } else if (pj.getInventario().getEquipo().get(obje.getIdObjeto()).getEquipaEn() == 2) {
                                                        equipo2 = new Icono("2icono", 110, 150, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje);
//                                                    equipo2.suspend();
                                                    } else if (pj.getInventario().getEquipo().get(obje.getIdObjeto()).getEquipaEn() == 3) {
                                                        equipo3 = new Icono("3icono", 70, 210, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje);
//                                                    equipo3.suspend();
                                                    } else if (pj.getInventario().getEquipo().get(obje.getIdObjeto()).getEquipaEn() == 4) {
                                                        equipo4 = new Icono("4icono", 140, 210, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje);
//                                                    equipo4.suspend();
                                                    } else {
//                                                    new Icono("icono", this.recorrido.x, this.recorrido.y, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje);
                                                        equipo5 = new Icono("5icono", 110, 300, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje);
//                                                    equipo5.suspend();
                                                    }
                                                }
                                                
                                            }
                                            if (obje.getTipo() == filtro) {
                                                //if(obje.getTipo()==1){
//
//                                                    if(pj.getInventario().getEquipo().get(obje.getIdObjeto()).getEquipado()==1){
//                                                        pj.getInventario().eliminarItem(obje.getIdObjeto(), (short)1);
//                                                        System.out.println("Nombre Objeto-----"+obje.getNombre()+"---lugar que se equipa "+pj.getInventario().getEquipo().get(obje.getIdObjeto()).getEquipaEn());
//                                                    }
//                                                }
                                                if (inv.tieneItem(obje.getIdObjeto())) {
                                                    hmIconoItem.put(cantidad, new Icono("icono", this.recorrido.x, this.recorrido.y, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje));
                                                    setFont(new JGFont("Arial", 0, 24));
                                                    //                                        drawString("Cantidad" + inv.contarItem(obje.getIdObjeto()), viewHeight() / 2, viewWidth() / 2, 0);
                                                    this.recorrido.x += 37;
                                                    this.tabla.x--;
                                                }
                                                //                                                if(obje.getTipo()==1){
//
//                                                    if(pj.getInventario().getEquipo().get(obje.getIdObjeto()).getEquipado()==1){
//                                                        pj.getInventario().agregarItem(obje.getIdObjeto(), (short)1);
//                                                        System.out.println("Nombre Objeto-----"+obje.getNombre()+"---lugar que se equipa "+pj.getInventario().getEquipo().get(obje.getIdObjeto()).getEquipaEn());
//                                                    }
//                                                }
                                            }
                                        } else {
                                            if (inv.tieneItem(obje.getIdObjeto())) {
                                            System.out.println("Dibuje a este qlio 2: " + obje.getNombre() + " " + obje.getNombreGrafico());
                                            hmIconoItem.put(cantidad, new Icono("icono", this.recorrido.x, this.recorrido.y, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje));
                                            setFont(new JGFont("Arial", 0, 24));
                                            //                                        drawString("Cantidad" + inv.contarItem(obje.getIdObjeto()), viewHeight() / 2, viewWidth() / 2, 0);
                                            this.recorrido.x += 37;
                                            this.tabla.x--;
                                        }
                                        }
                                    

                                } else {
                                    fin = true;

                                }



                            }

                            this.recorrido.x = pos_inicial_x;
                            this.tabla.x = tabla_inicial_x;
                            this.tabla.y--;
                            this.recorrido.y += 37;

                        }

                        break;
                }
//                setWorking(true);
            }

        }

        public void setSeccion(JGPoint posicion, JGPoint tabla) {
            setRecorrido(posicion);
            setTabla(tabla);
            this.pos_inicial_x = posicion.x;
            this.pos_inicial_y = posicion.y;
            this.tabla_inicial_x = tabla.x;
            this.tabla_inicial_y = tabla.y;
        }

        public void removerIconos() {
            setWorking(false);
            removeObjects("icono", (int) Math.pow(2, 4));
        }

        public JGPoint getRecorrido() {
            return recorrido;
        }

        public void setRecorrido(JGPoint recorrido) {
            this.recorrido = recorrido;
        }

        public JGPoint getTabla() {
            return tabla;
        }

        public void setTabla(JGPoint tabla) {
            this.tabla = tabla;
        }
    }
}
