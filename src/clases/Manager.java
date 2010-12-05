package clases;

import extensiones.StdScoring;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jgame.JGColor;
import jgame.JGFont;
import jgame.JGPoint;
import jgame.platform.*;
import java.util.HashMap;
import jgame.JGTimer;
import jgame.JGObject;
import java.util.Iterator;
import java.util.Map;
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
    /*
     * Objetos de combate
     */
    private Icono icon;
    private boolean unJGTimer = false;
    private JGTimer tiempoRegenerar, respawn_mob, respawn_pj;
    private HashMap<Short, Habilidad> habilidades;
    private HashMap<Short, Mision> misiones;
    private Npc npc_interaccion;
    private boolean salirInInteracting;

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
    private Ventana asd;
    public String[] textoPrueba;
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
            defineMedia("/media/rpg-basico.tbl");
            setBGImage("bgimage");
            // playAudio("ambiental", "muerte", true);

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

        //Objeto cursor, imágen que sigue las coordenadas del mouse
        cursor = new Cursor();
        //cargaJugador(0,0); reemplazamos por el metodo nuevo
        this.pj = new Jugador();
        this.pj.cargarDatos(this.idJugador);
//        setCursor(null);
        inicializarTeclas();
        try {
            mob_facil_1 = new Mob(140 * 16, 110 * 16, 1.5, (short) 6, "mob_facil_1", "mob_1", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 6
            mob_facil_2 = new Mob(140 * 16, 110 * 16, 1.5, (short) 7, "mob_facil_2", "orc_stand_r", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 7

            mob_medio_1 = new Mob(140 * 16, 110 * 16, 1.5, (short) 8, "mob_medio_1", "mob_3", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 8
            mob_medio_2 = new Mob(140 * 16, 110 * 16, 1.5, (short) 9, "mob_medio_2", "mob_4", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 9

            mob_dificil_1 = new Mob(140 * 16, 110 * 16, 1.5, (short) 10, "mob_dificil_1", "mob_5", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 10
            mob_dificil_2 = new Mob(140 * 16, 110 * 16, 1.5, (short) 11, "mob_dificil_2", "mob_6", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 11

            mob_jefe_final = new Mob(140 * 16, 110 * 16, 1.5, (short) 100, "mob_jefe_final", "boss_1", (short) 10, (short) 2, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 12

            mob_medio_1 = new Mob(140 * 16, 110 * 16, 1.5, (short) 8, "mob_medio_1", "mob_3", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 8
            mob_medio_2 = new Mob(140 * 16, 110 * 16, 1.5, (short) 9, "mob_medio_2", "mob_4", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 9

            mob_dificil_1 = new Mob(140 * 16, 110 * 16, 1.5, (short) 10, "mob_dificil_1", "mob_5", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 10
            mob_dificil_2 = new Mob(140 * 16, 110 * 16, 1.5, (short) 11, "mob_dificil_2", "mob_6", (short) 10, (short) 3, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 11

            mob_jefe_final = new Mob(140 * 16, 110 * 16, 1.5, (short) 100, "mob_jefe_final", "boss_1", (short) 10, (short) 2, pj, false, 0.9, (int) Math.pow(2, 2)); // id = 12

            mob_facil_1.cargarDatos((short) 6);
            mob_facil_2.cargarDatos((short) 7);
            mob_medio_1.cargarDatos((short) 8);
            mob_medio_2.cargarDatos((short) 9);
            mob_dificil_1.cargarDatos((short) 10);
            mob_dificil_2.cargarDatos((short) 11);
            mob_jefe_final.cargarDatos((short) 12);

            mob_facil_1.resume_in_view = false;
            mob_facil_2.resume_in_view = false;
            mob_medio_1.resume_in_view = false;
            mob_medio_2.resume_in_view = false;
            mob_dificil_1.resume_in_view = false;
            mob_dificil_2.resume_in_view = false;
            mob_jefe_final.resume_in_view = false;

            dibujarObjetosEscenario();
            menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);


            //pasto1 = new Npc(192,128,"pasto","pasto",4,0,new String[]{"Hola amiguirijillo","soy pastillo1"});//pasto

            npc_vendedor_1 = new Npc(1040, 416, "npc_vendedor_1", "vendedor", 0, (short) 22, (short) 1, (short) 1, new String[]{"Hola amiguirijillo", "soy el vendedorsillo"});//
            npc_vendedor_1.cargarDatos((short) 1);
            npc_vendedor_2 = new Npc(1040, 416, "npc_vendedor_2", "vendedor", 0, (short) 22, (short) 1, (short) 1, new String[]{"Hola amiguirijillo", "soy el vendedorsillo"});//
            npc_vendedor_2.cargarDatos((short) 2);
            npc_encargo_1 = new Npc(700, 75, "npc_encargo_1", "people", (int) Math.pow(2, 3), 0, (short) 3, new String[]{"Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior; // id = 3
            npc_encargo_1.cargarDatos((short) 3);
            npc_encargo_2 = new Npc(730, 75, "npc_encargo_2", "people2", (int) Math.pow(2, 3), 0, (short) 4, new String[]{"Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior; // id = 4
            npc_encargo_2.cargarDatos((short) 4);
            npc_encargo_3 = new Npc(760, 75, "npc_encargo_3", "people3", (int) Math.pow(2, 3), 0, (short) 5, new String[]{"Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior;
            npc_encargo_3.cargarDatos((short) 5);


        } catch (Exception ex) {
            System.out.println("Extrae datos del HashMapsssssssssssssssss: " + ex);
        }

        definirEscenario();
        System.out.println("Inicio obtiene habilidades");
        String StrSql = "SELECT * FROM habilidad ";

        this.habilidades = new HashMap<Short, Habilidad>();
        System.out.println(StrSql);
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

            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->habilidades , método->setHabilidad() " + ex);
        }
        System.out.println("Inicio obtiene misiones");
        StrSql = "SELECT * FROM mision ";

        this.misiones = new HashMap<Short, Mision>();
        System.out.println("Inicio obtiene datos personaje");
        try {
            ResultSet res = conect.Consulta(StrSql);
            if (res.next()) {
                Mision mis = new Mision();
                mis.setDescripcion(res.getString("descripcion"));
                mis.setNombre(res.getString("nombre"));
                mis.setIdMision(res.getShort("id"));
                mis.setIdPersonajeConcluyeMision(res.getShort("personaje_id"));
                mis.setNivelRequerido(res.getShort("nivelrequerido"));
                mis.setRepetible(Boolean.parseBoolean(res.getString("repetible")));
                mis.setRecompensaExp(res.getShort("recompensaexp"));
                this.misiones.put(mis.getIdMision(), mis);
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->manager , método-Linea 255 " + ex);
        }
        ventanaManager = new Ventana();
        setGameState("Title");
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
        capturarTeclas();
        if (isPresionada(KeyShift) && isPresionada(KeyCtrl)) {
            setGameState("InCombat");
        } else if (isPresionada(KeyShift) && isPresionada(KeyTab)) {
            initGame();
        }
//        if (((pj.isInteractuarNpc()) && ((getMouseButton(1)) || (getKey(KeyDown)))) || (interactuar > casa1.obtieneDialogo().length)) {
//            pj.y = pj.y + 10;
//            pj.desbloquear();
//            removeObjects(getNomNpcInteractuar(), (int) Math.pow(2, 3));
//            pj.setInteractuarNpc(false);
//            setInteractuar(0);
//        } else if ((pj.isInteractuarNpc()) && (interactuar == 0)) {
//            try {
//
//                // casa1.realizaTarea(pj);
//                //asd = new Ventana(300, 300, casa1.obtieneDialogo());//casa superior);
//            } catch (Exception ex) {
//                System.out.println("Extrae datos del HashMap: fsdfsdfsd" + ex);
//            }
//            setNomNpcInteractuar(pj.npcInterac.getNomNpc() + "Npc");
//            setInteractuar(1);
//        } else if ((interactuar > 0) && (interactuar < casa1.obtieneDialogo().length) && (getKey(KeyEnter))) {
//
//            if (pausa == 10) {
//                interactuar += 3;
//            } else if (pausa > 10) {
//                pausa = 0;
//            }
//            pausa += 1;
//            //System.out.println("+3 interactuar");
//
//        }
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
        if (checkCollision((int) Math.pow(2, 2), pj) == Math.pow(2, 2)) {
            mob_concurrente = pj.getEnemigo();
            setGameState("InCombat");
            mob_concurrente.getInventario().respaldarInventario();
        }
        int posX = (int) pj.x;
        int posY = (int) pj.y;

        xofs = posX;
        yofs = posY;
        setViewOffset(
                xofs, yofs,
                true);

        if (cursor.getVentana() == 1) {
            setGameState("InCommerce");
            seccion.setSeccion(new JGPoint(25, 25), new JGPoint(2, 4));
            seccionNpc.setSeccion(new JGPoint(250, 25), new JGPoint(3, 4));
        }

        if (isSalir()) {
            System.exit(0);
        }
    }

    public void paintFrameInWorld() {
//        //Genera seccion para inventario InWorld
////        if ((inGameState("InWorld")) && ((getKey(73)) || (getKey(105)))) {
////            seccion.setSeccion(new JGPoint(viewWidth() - 85, 20), new JGPoint(2, 10));
////            seccion.generaSeccion(pj, 1);
////            menu.recibeHm(hmIconoItem, 1);
////        } else {
////            seccion.removerIconos();
////        }
//
//        drawString("SEGUNDO: " + seg, +200 / 2, viewHeight() / 2, 1);
//        //drawRect(viewXOfs() + 700, viewYOfs(), 100, viewHeight(), true, false);
//
//        //drawImage(cursor.x, cursor.y, "cursor");
//        if (getKey(KeyEsc)) {
//            menu.ventanaSalida();
//            //playAudio("eventos","mensaje",false);
//            pj.bloquear(0);
//            if (getKey(KeyEnter)) {
//                menu.setTeclaEscape(false);
//                setSalir(true);
//            }
//        }
//        if (interactuar > 0) {
//            //asd.avanzarTexto();
//        }
//
//        /**
//         * Detecta si has encontrado un hongo para la mision
//         */
//        if ((pj.x == 880.0) && (pj.y == 400.0) && (!hongo1)) {
//            hongo1 = true;
//            tiempoMensaje = 120;
//            try {
//                //agrego el hongo al inventario del jugador
//                pj.getInventario().agregarItem(pj.getIdPersonaje(), (short) 1);
//                //pj.cargaInventario(pj.getIdJugador());
//                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
//            } catch (Exception ex) {
//                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        if ((pj.x == 928) && (pj.y == 144) && (!hongo2)) {
//            hongo2 = true;
//            tiempoMensaje = 120;
//            try {
//                //agrego el hongo al inventario del jugador
//                //Inventario[] tempInv = pj.getInv();
//                pj.getInventario().agregarItem((short) 1000, (short) 1);
//                //tempInv[0].agregarItem(pj.getIdJugador(),(short)1000,(short)1);
//                //pj.cargaInventario(pj.getIdJugador());
//                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
//            } catch (Exception ex) {
//                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        if ((pj.x == 80) && (pj.y == 48) && (!hongo3)) {
//            hongo3 = true;
//            tiempoMensaje = 120;
//            try {
//                //agrego el hongo al inventario del jugador
//                //Inventario[] tempInv = pj.getInv();
//                pj.getInventario().agregarItem(pj.getIdPersonaje(), (short) 1);
//                //tempInv[0].agregarItem(pj.getIdJugador(),(short)1000,(short)1);
//                //pj.cargaInventario(pj.getIdJugador());
//                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
//            } catch (Exception ex) {
//                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        if ((pj.x == 416) && (pj.y == 624) && (!hongo4)) {
//            hongo4 = true;
//            tiempoMensaje = 120;
//            try {
//                //agrego el hongo al inventario del jugador
//                //Inventario[] tempInv = pj.getInv();
//                pj.getInventario().agregarItem(pj.getIdPersonaje(), (short) 1);
//                //tempInv[0].agregarItem(pj.getIdJugador(),(short)1000,(short)1);
//                //pj.cargaInventario(pj.getIdJugador());
//                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
//            } catch (Exception ex) {
//                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        if ((pj.x == 64) && (pj.y == 800) && (!hongo5)) {
//            hongo5 = true;
//            tiempoMensaje = 120;
//            try {
//                //agrego el hongo al inventario del jugador
//                //Inventario[] tempInv = pj.getInv();
//                pj.getInventario().agregarItem(pj.getIdPersonaje(), (short) 1);
//                //tempInv[0].agregarItem(pj.getIdJugador(),(short)1000,(short)1);
//                //pj.cargaInventario(pj.getIdJugador());
//                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
//            } catch (Exception ex) {
//                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        if ((hongo1) && (hongo2) && (hongo3) && (hongo4) && (hongo5) && (!finHongos)) {
//            finHongos = true;
//        }
//        if (tiempoMensaje > 0) {
//            new Ventana("wena choro pillaste una callapampa!");
//        }
//        tiempoMensaje--;
        if (cursor.getMensaje().length() > 0) {
            new Ventana(cursor.getMensaje());
            cursor.setMensaje("");
        }
    }

    @Override
    public void paintFrame() {

        seccion.setSeccion(new JGPoint(110, 435), new JGPoint(12, 1));
        seccion.generaSeccion(pj, 0);
        menu.recibeHm(hmIconoHabilidades, 2,filtro);

        seccion.setSeccion(new JGPoint(110, 400), new JGPoint(12, 1));
        seccion.generaSeccion(pj, 1);
        menu.recibeHm(hmIconoItem, 1,filtro);

        seccion.setWorking(true);
        
        cursor.desplegarInformacion();

        moveObjects(null, 1);
        
        menu.paintB();
        menu.menuActual(getTeclaMenu(), pj);

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
            cursor.setMensajeIcon(null);
        }
        //el cursor no choca contra un boton del tipo ver
        if (checkCollision((int) Math.pow(2, 5), cursor) != Math.pow(2, 5)) {
            cursor.limpiarInformacion();
        }

        moveObjects(null, (int) Math.pow(2, 7));
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
                    pj.setPos(CIUDAD_X, CIUDAD_Y);
                    pj.setDir(0, 0);
                    pj.suspend();
                }
            };
        }


        //personaje es enviado a la ciudad, poner con cara de muerto, o alguna seña que lo está
        if (getMouseButton(1)) {
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

//(mpMax * ((float) (porcentaje / 100.0)))
        setFont(new JGFont("Arial", 0, 15));

        setColor(JGColor.orange);
        // aca graficar todas las wes hermosas y lindas de la warifaifa
        drawString(pj.getNombre() + " --- " + (float) (pj.getHp() * 100 / pj.getHpMax()), ((viewWidth() * 10) / 100), (double) 305, 0);
        drawRect(viewWidth() * 10 / 100 + viewXOfs(), 322 + viewYOfs(), (float) (pj.getHp() * 100 / pj.getHpMax()), 10, true, false, 400, JGColor.green);
        drawRect(viewWidth() * 10 / 100 + viewXOfs(), 337 + viewYOfs(), (float) (pj.getMp() * 100 / pj.getMpMax()), 10, true, false, 400, JGColor.blue);

        setColor(JGColor.yellow);
        drawString(mob_concurrente.getNombre() + "---" + (float) (mob_concurrente.getHp() * 100 / mob_concurrente.getHpMax()), ((viewWidth() * 70) / 100), (double) 305, 0);
        drawRect(viewWidth() * 70 / 100 + viewXOfs(), 322 + viewYOfs(), (float) (mob_concurrente.getHp() * 100 / mob_concurrente.getHpMax()), 10, true, false, 400, JGColor.green);
        drawRect(viewWidth() * 70 / 100 + viewXOfs(), 337 + viewYOfs(), (float) (mob_concurrente.getMp() * 100 / mob_concurrente.getMpMax()), 10, true, false, 400, JGColor.blue);
    }

    public void doFrameInCombat() {
        int dañoBeneficio = 0;
        checkCollision(
                (int) Math.pow(2, 4) + (int) Math.pow(2, 0), // Colisión entre Iconos + cursor
                (int) Math.pow(2, 0) // ejecuta hit cursor
                );
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
                    mob_concurrente.recibirDañoBeneficio(dañoBeneficio);

                    //si no es beneficio al jugador
                    menu.recibeScore(null, new StdScoring("scoring", mob_concurrente.x, mob_concurrente.y, 0, -2, 80, "" + dañoBeneficio, new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.red, JGColor.orange, JGColor.yellow}, 5));
                } else {
                    pj.recibirDañoBeneficio(dañoBeneficio);
                    menu.recibeScore(new StdScoring("scoring", pj.x, pj.y, 0, -2, 80, "" + dañoBeneficio, new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green, JGColor.yellow}, 5), null);
                }
            }
            setIcon(null);
        } else if (this.getIconoPresionado() != null && this.getIconoPresionado().getTipo() == 1) {
            //personaje ha utilizado algun tipo de objeto...validar que sea para uso en combate
            Objeto obje = icon.getItem();//pj.getInventario().getItem(this.getIconoPresionado().getIdObjeto()).getObjeto();
            System.out.println("Es--------->"+obje.getNombre());
            System.out.println("EsUsoCombate--------->"+obje.isUsoCombate());
            if (obje.isUsoCombate()) {
                pj.setProximoItem(obje);
                pj.getInventario().getItem(this.getIconoPresionado().getIdObjeto()).restarCantidad((short) 1);
                seccion.removerIconos();
                seccion.setWorking(false);
            }
            if (pj.getIdProximoItem() != -1) {
                //el personaje puede usar un item
                dañoBeneficio = random(pj.getNivel() * 2, pj.getNivel() * 5, 10);
                pj.recibirDañoBeneficio(dañoBeneficio);
                new StdScoring("scoring", pj.x, pj.y, 0, -2, 80, "" + dañoBeneficio, new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green, JGColor.yellow}, 5);
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
                new StdScoring("scoring", pj.x, pj.y, 0, -2, 80, "" + dañoBeneficio, new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.red, JGColor.orange, JGColor.yellow}, 5);
                //si no es beneficio al MOB
            } else {
                mob_concurrente.recibirDañoBeneficio(dañoBeneficio);
                new StdScoring("scoring", mob_concurrente.x, mob_concurrente.y, 0, -2, 80, "" + dañoBeneficio, new JGFont("helvetica", 1, 20), new JGColor[]{JGColor.green, JGColor.yellow}, 5);
            }
        }
        //regenero mana cada 4 segundos y que desaparezca cuando no esté en estado INCOMBAT
        if (tiempoRegenerar == null || !tiempoRegenerar.running) {
            tiempoRegenerar = new JGTimer((int) (getFrameRate() * 4), false, "InCombat") {

                @Override
                public void alarm() {
                    mob_concurrente.regenerarMp(3);
                    pj.regenerarMp(5);
                }
            };
        }

        if (mob_concurrente.getHp() <= 0) {
            final Mob enemigo_procesar = (Mob) getObject(mob_concurrente.getName());
            if (respawn_mob == null) {
                respawn_mob = new JGTimer((int) (getFrameRate() * 60 * 1), true) {

                    @Override
                    public void alarm() {
                        enemigo_procesar.resume();
                        enemigo_procesar.aumentarDisminuirMp(pj.getMpMax() / 2);
                        enemigo_procesar.recibirDañoBeneficio(pj.getHpMax() / 2);
                        mob_concurrente.getInventario().restablecerInventario();
                    }
                };
                respawn_mob = null;
            }
            mob_concurrente.suspend();
            seccion.removerIconos();
            cursor.setVentana((byte) (1));
            setGameState("InReward");
        }
    }

    public void paintFrameInInteraction() {
        if (!salirInInteracting) {
            System.out.print("inicio paint frame");
            Boton btn1;
            //dibujo las lineas del parrafo
            ventanaManager.mostrarTexto();
            if (ventanaManager.terminoDialogo) {//genero el boton siguiente
                btn1 = new Boton("cerrar", "cerrar", 430, 330, (int) (Math.pow(2, 5)), 0, 0);
                btn1.pintar();
            } else {//termino el dialogo //aun podria estar leyendo el ultimo parrafo = new Boton("cerrar", "suma", 430, 330, (int) (Math.pow(2, 5)), 0, 0);
                btn1 = new Boton("siguiente", "cerrar", 430, 330, (int) (Math.pow(2, 5)), 5, 0);
                btn1.pintar();
//            if (ventanaManager.terminoDialogo) {
//                new JGTimer((int) (this.getFrameRate() * 5), true, "InInteraction") {
//
//                    @Override
//                    public void alarm() {
//                        setGameState("InWorld");
//                    }
//                };
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
        if (getMouseButton(1)) {
            pj.aumentarExperiencia(mob_concurrente.getExperiencia());
            mob_concurrente.recibirDañoBeneficio(mob_concurrente.getHpMax());
            mob_concurrente.aumentarDisminuirMp(mob_concurrente.getMpMax());
            clearMouseButton(1);
            cursor.setLimpiarIconos(true);
            pj.bloquear();
            setGameState("InWorld");
        } else {

            interacVentana(mob_concurrente, "InReward");
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
                    "))))))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    ")))))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "))%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
                    "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",
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
            seccion.setWorking(false);
            seccionNpc.setWorking(false);

//            pj.colid = 0;
            seccion.generaSeccion(pj, 1);
            menu.recibeHm(hmIconoItem, 1,filtro);
            pj.bloquear();

            seccionNpc.setSeccion(new JGPoint(160, 210), new JGPoint(3, 4));
            seccionNpc.generaSeccion(vendedor, 1);
            menu.recibeHm(hmIconoItem, 0,filtro);

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
                System.out.println("setVentana antes InWorld:" + cursor.getVentana());

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

            //grillaNpc = new Boton("grilla npc", "grilla npc", viewXOfs() + 10, viewYOfs() + 10, (int) Math.pow(2, 5));
            //grillaPj = new Boton("grilla pj", "grilla pj", viewXOfs() + 200, viewYOfs() + 10, (int) Math.pow(2, 5));
            //ventanaTrade = new Boton("ventana trade", "ventana trade",viewXOfs(),viewYOfs(),0);
//            procesaItem(pj, grillaPj.x, grillaPj.y);
//            procesaItem(vendedor, grillaNpc.x, grillaNpc.y);
            //seccion.setSeccion(new JGPoint(10,10), new JGPoint(3,4));
            seccion.setWorking(false);
            seccionNpc.setWorking(false);

//            pj.colid = 0;
            seccion.generaSeccion(pj, 1);
            menu.recibeHm(hmIconoItem, 1,filtro);
            pj.bloquear();

            seccionNpc.setSeccion(new JGPoint(200, 200), new JGPoint(6, 1));
            seccionNpc.generaSeccion(mob, 1);
            seccionNpc.setWorking(true);
            menu.recibeHm(hmIconoItem, 0,filtro);

//            cerrar = new Boton("cerrar", "cerrar", 230, 320, (int) Math.pow(2, 5), 0, 0);
//            cerrar.pintar();

            cursor.setVentana((byte) 2);
        } else if ((cursor.getVentana() == 3) || (cursor.getVentana() == 4)) {
//            Remueve todos los objetos que forman la ventana de comerciar
//            removeObjects("ventana trade", 0);
//            removeObjects("grilla npc", (int) Math.pow(2, 5));
//            removeObjects("grilla pj", (int) Math.pow(2, 5));
//            removeObjects("cerrar", (int) Math.pow(2, 5));
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

    private void dibujarObjetosEscenario() throws SQLException {
        new Npc(700, 75, "alcaldia", "casa4", (int) Math.pow(2, 6), 0, (short) 105, new String[]{});
        new Npc(680, 660, "casa1", "casa3", (int) Math.pow(2, 6), 0, (short) 100, new String[]{});
        new Npc(80, 400, "casa2", "casa2", (int) Math.pow(2, 6), 0, (short) 101, new String[]{"Casa 2"});
        new Npc(350, 448, "casa3", "casa4", (int) Math.pow(2, 6), 0, (short) 102, new String[]{"Casa 3"});
        new Npc(80, 634, "casa3", "casa3", (int) Math.pow(2, 6), 0, (short) 103, new String[]{"Casa 3"});
        new Npc(350, 682, "casa3", "casa5", (int) Math.pow(2, 6), 0, (short) 104, new String[]{"Casa 3"});
        new Npc(352, 64, "arbol1", "arbol", (int) Math.pow(2, 6), 0, (short) 106, new String[]{"Hola amiguirijillo", "soy Don Arbol, cuidame"});//
        new Npc(288, 32, "arbol2", "arbol", (int) Math.pow(2, 6), 0, (short) 107, new String[]{"Hola amiguirijillo", "soy Don Arbol, cuidame"});//
        new Npc(128, 64, "arbol2", "pileta", (int) Math.pow(2, 6), 0, (short) 108, new String[]{"Hola amiguirijillo", "soy la fuente magica"});//
        new Npc(16 * 80, 16 * 12, "guardia", "guardia", (int) Math.pow(2, 6), (short) 4, (short) 20, new String[]{"Guardia: vé con cuidado"});
        new Npc(16 * 80, 16 * 21, "guardia", "guardia", (int) Math.pow(2, 6), (short) 4, (short) 20, new String[]{"Guardia: vé con cuidado"});
        new Npc(16 * 16, 16 * 12, "viajero", "viajero", (int) Math.pow(2, 6), (short) 4, (short) 20, new String[]{"Viajero: "});
        new Npc(16 * 10, 16 * 17, "mono", "mono", (int) Math.pow(2, 6), (short) 4, (short) 20, new String[]{"Mono: "});
        new Npc(16 * 10, 16 * 110, "perdido", "perdido", (int) Math.pow(2, 6), (short) 4, (short) 20, new String[]{"perdido: "});
        new Npc(16 * 80, 16 * 8, "escultura", "escultura", (int) Math.pow(2, 6), (short) 4, (short) 20, new String[]{"escultura: "});
        new Npc(16 * 80, 16 * 24, "escultura", "escultura", (int) Math.pow(2, 6), (short) 4, (short) 20, new String[]{"escultura: "});
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
        private String dialogo_restante, dialogo_mostrar, linea_1, linea_2, linea_3;
        private boolean terminoDialogo;

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
            new JGTimer(
                    (int) (getFrameRate() * this.getSegundos()), //calculo los frames
                    true // true indica que la alarma solo se dispara una vez
                    ) {
                //método que se debe redefinir para que ejecute una acción
                //esta vez, desaparecer el objeto ventana

                @Override
                public void alarm() {
                }
            };

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
            drawRect(viewXOfs() + 200, viewYOfs() + 250, 300, 100, true, false);
            //interior
            setColor(JGColor.white);
            drawRect(viewXOfs() + 205, viewYOfs() + 255, 290, 90, true, false);
        }

        private void desplegarVentana(double x, double y) {
            //borde externo
            setColor(JGColor.red);
            drawRect(viewXOfs() + x, viewYOfs() + y, 300, 100, true, false);
            //interior
            setColor(JGColor.white);
            drawRect(viewXOfs() + x + 5, viewYOfs() + y + 5, 290, 90, true, false);
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
                    String subStr2 = this.dialogo_restante.substring(1, indiceUltimaLetra);

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
    }

    public class Cursor extends JGObject {

        private String mensaje = new String();
        private double ejex = eng.getMouseX() + eng.viewXOfs();
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
            
            if ((obj.colid == (int) Math.pow(2, 4))) {//es icono
                Icono iconito = (Icono) obj;
                this.setMensajeIcon(iconito.getNombreLogico());
            }
            setMensaje("Soy " + obj.getName());
            if ((obj.getGraphic().equals("cerrar")) && (getMouseButton(3))) {
                setVentana((byte) 3);
            }
            //click en NPC
            if (obj.colid == (int) Math.pow(2, 3)) {
                Npc npc_procesar = (Npc) obj;
                switch (npc_procesar.getTipo()) {
                    case 2://npc de misiones
                        setMensaje("jajaja, agarrame el 1313... soy " + npc_procesar.getNombre() + " y que wea!");
                        if (getMouseButton(3)) {
                            npc_concurrente = npc_procesar;
                            ventanaManager.setDialogo(npc_concurrente.obtieneDialogo());
                            ventanaManager.activarParametrosFormateoTexto();
                            npc_concurrente.getDialogo().nextTexto();
                            setGameState("InInteraction");
                            salirInInteracting = false;
                        }
                        break;
                    case 1://npc vendedor
                        setMensaje("Vendedor: Hola " + pj.getNombre() + ", deseas hacer un trato     ?" + obj.colid);
                        if (getMouseButton(3)) {
                            vendedor_concurrente = (Npc) obj;
                            setVentana((byte) 1);
                        }
                }
            }
            //click en algun boton
            if (obj.colid == (int) Math.pow(2, 5)) {
                Boton boton = (Boton) obj;
                if (inGameState("InInteraction")) {
                    if (getMouseButton(3)) {
                        clearMouseButton(3);
                        Boton btn = (Boton) obj;
                        switch (btn.getTipo_boton()) {
                            case 0: //Presiono boton cerrar en un dialogo
                                salirInInteracting = true;
                                npc_concurrente = null;
                                setGameState("InWorld");
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
                    if (boton.getTipo_boton() != 3 && getMouseButton(3)) {
                        clearMouseButton(3);
                        //corresponde al menu y no es boton del tipo "ver"
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
                                break;
                            case 1://Menú está en Habilidad
                                if (!pj.getHabilidades().tieneHabilidad((short) boton.getId())) {
                                    pj.getHabilidades().agregaHabilidad((short) boton.getId());
                                } else if (pj.getHabilidades().getHabilidad((short) boton.getId()).puedeAumentar()) {
                                    pj.getHabilidades().aumentarNivel((short) boton.getId());
                                }
                                pj.gastarPuntosHabilidad();
                                seccion.setWorking(false);
                                setLimpiarIconos(true);
                                break;
                            case 0://Menú menu principal inventario
                                if (!pj.getHabilidades().tieneHabilidad((short) boton.getId())) {
                                    pj.getHabilidades().agregaHabilidad((short) boton.getId());
                                } else if (pj.getHabilidades().getHabilidad((short) boton.getId()).puedeAumentar()) {
                                    pj.getHabilidades().aumentarNivel((short) boton.getId());
                                }
                                pj.gastarPuntosHabilidad();
                                seccion.setWorking(false);
                                setLimpiarIconos(true);
                                System.out.println(pj.getHabilidades().getHabilidad((short) boton.getId()).getHabilidad().getNombre());
                                System.out.println("Nivel: " + pj.getHabilidades().getHabilidad((short) boton.getId()).getNivelHabilidad());
                                System.out.println("PTOS HAB " + pj.getTotalPuntosHabilidad());
                                break;
                            case 2://Menú está en misión
                                if (boton.getTipo_boton() == 4) {//boton corresponde al tipo abandonar
                                    pj.getMisiones().abandonaMision((short) boton.getId());
                                }
                                break;
                        }

                    } else if (boton.getTipo_boton() == 3) {
                        //corresponde al menu y es boton del tipo "ver"
                        //por comportamiento mouse over , se muestra información
                        //en el monitor
                        System.out.println("menuActual---------->"+menu.getMenuActual());
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
                                Mision mis = misiones.get((short) boton.getId());
                                setInformacionMision(mis.getNombre(), mis.getDescripcion(), String.valueOf(mis.getRecompensaExp()), String.valueOf(mis.getIdPersonajeConcluyeMision()));
                                break;

                        }
                    }
                }


                if(obj.y >= viewYOfs() + (viewHeight() - 180)){
                    if((getMouseButton(3)&&(getKey(66)))||(getMouseButton(3)&&(getKey(98)))){
                        seccion.removerIconos();
                        pj.getInventario().eliminarItem((short)boton.getId());
                        seccion.setWorking(false);
                    }
                    if (boton.getTipo_boton() == 3 && getMouseButton(3)) {
                            System.out.println("aquiiii->>>>>>>>>>");
                           Objeto item = pj.getInventario().getObjetos().get((short)boton.getId()).getObjeto();
                           setInformacionObjeto(item.getNombre(), item.getDescripcion(), String.valueOf(item.getValorDinero()), String.valueOf(item.getPeso()), String.valueOf(item.getTipo()), String.valueOf(item.isUsoCombate()));
                    }                    
                }
                
                if(obj.getName().equals("usable")&&(getMouseButton(3))){
                    System.out.println("Nombre del objeto-------->"+obj.getName());
//                    seccion.removerIconos();
                        cursor.setLimpiarIconos(true);
                    filtro=0;
                    seccion.setWorking(false);
                }else if(obj.getName().equals("equipo")&&(getMouseButton(3))){
                    System.out.println("Nombre del objeto-------->"+obj.getName());
//                    seccion.removerIconos();
                    cursor.setLimpiarIconos(true);
                    filtro=1;
                    seccion.setWorking(false);
                }else if(obj.getName().equals("colec")&&(getMouseButton(3))){
                    System.out.println("Nombre del objeto-------->"+obj.getName());
//                    seccion.removerIconos();
                    cursor.setLimpiarIconos(true);
                    filtro=2;
                    seccion.setWorking(false);
                }
            }
            if ((obj.colid == (int) Math.pow(2, 4)) && (getMouseButton(3)) & (inGameState("InCombat"))) {
                clearMouseButton(3);
                setIcon((Icono) obj);
            }
            if (obj.colid == (int) Math.pow(2, 4)) {
                Icono icon = (Icono) obj;
                if (inGameState("InCommerce")) {
                    if ((getMouseButton(3)) && icon.belongTo(vendedor_concurrente.getTipo())) {
                        clearMouseButton(3);
                        if (pj.validarDinero(icon.getItem().getValorDinero())) {
                            pj.getInventario().agregarItem(icon.getIdObjeto());
                            pj.setDinero(pj.getDinero() - icon.getItem().getValorDinero());
                            cursor.setVentana((byte) 4);
                        } else {
                            setMensaje("No tienes suficiente dinero");
                        }
                    }
                    if ((getMouseButton(3)) && icon.belongTo(pj.getTipo()) && icon.getItem().getTipo()==filtro) {
                        clearMouseButton(3);
                        pj.getInventario().eliminarItem(icon.getIdObjeto(), (short) 1);
                        pj.setDinero(pj.getDinero() + icon.getItem().getValorDinero());
                        cursor.setVentana((byte) 4);
                    }

                }
                if (inGameState("InReward")) {
                    if ((getMouseButton(3)) && icon.belongTo(mob_concurrente.getTipo())) {
                        clearMouseButton(3);
                        pj.getInventario().agregarItem(icon.getIdObjeto());
                        mob_concurrente.getInventario().eliminarItem(icon.getIdObjeto(), (short) 1);
                        cursor.setVentana((byte) 4);
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

                                    if (inv.tieneItem(obje.getIdObjeto())) {
                                        cantidad++;


                                        if(personaje.getTipo()==0){
                                            
                                            if(obje.getTipo()==filtro){
                                                System.out.println("Numero de filtro-------->"+filtro);
                                                hmIconoItem.put(cantidad, new Icono("icono", this.recorrido.x, this.recorrido.y, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje));

                                                setFont(new JGFont("Arial", 0, 24));
        //                                        drawString("Cantidad" + inv.contarItem(obje.getIdObjeto()), viewHeight() / 2, viewWidth() / 2, 0);
                                                System.out.println("ITEMMMMMMMMMMMMMMMMMMMMMMMM Filtrado");
                                                this.recorrido.x += 37;
                                                this.tabla.x--;
                                            }
                                        }else{
                                            hmIconoItem.put(cantidad, new Icono("icono", this.recorrido.x, this.recorrido.y, obje.getNombreGrafico(), obje.getIdObjeto(), (short) 1, inv.contarItem(obje.getIdObjeto()), personaje.getTipo(), obje.getNombre(), obje));

                                            setFont(new JGFont("Arial", 0, 24));
    //                                        drawString("Cantidad" + inv.contarItem(obje.getIdObjeto()), viewHeight() / 2, viewWidth() / 2, 0);
                                            System.out.println("ITEMMMMMMMMMMMMMMMMMMMMMMMMM Sin filtro");
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
