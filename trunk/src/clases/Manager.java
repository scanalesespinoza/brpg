package clases;

import java.util.logging.Level;
import java.util.logging.Logger;
import jgame.JGColor;
import jgame.JGFont;
import jgame.JGPoint;
import jgame.platform.*;
import java.util.HashMap;
import jgame.JGTimer;
import jgame.JGObject;

/**
 *
 * @author gerald
 */
public class Manager extends JGEngine {
    /*
     *@param idJugador Esta variable idetificador corresponde a la clave
     * principal del personaje que ha seleccionado el usuario para jugar.
     * Permite en una misma sesion de juego recuperar, actualizar y desconectar al personaje Jugador.
     */
    private short idJugador = 1;//Valor en duro, debiera recibirse como parametro desde el sitio web
    private int interactuar = 0;//0=Jugador presente en el juego/1=Jugador ausente e interactuando con Npc/>0 Ejecutando dialogo y acciones de Npc
    private String nomNpcInteractuar;
    public int pausa = 0;// Modo de evitar que se ejecuten acciones por los 60 frames que ocurren por segundo
    //Personajes del juego
    public Jugador pj;
    public Mob mob;
    public Npc casa1;
    public Npc casa1Npc;
    public Npc alcaldia;
    public Npc alcalde;
    public Npc casa2;
    public Npc casa2Npc;
    public Npc casa3;
    public Npc casa3Npc;
    public Npc casa4;
    public Npc casa4Npc;
    public Npc casa5;
    public Npc casa5Npc;
    public Npc pasto1;
    public Npc pasto2;
    public Npc arbol1;
    public Npc arbol2;
    public Npc pileta;
    public Npc vendedor;

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
    private Ventana asd;
    public String[] textoPrueba;

    public static void main(String[] args) {
        new Manager(new JGPoint(800, 540));

    }

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
        setFrameRate(60, 2);
        try {
            defineMedia("/media/rpg-basico.tbl");
            setBGImage("bgimage");
            playAudio("music", "musicaciudad", true);
            setMouseCursor(pj);
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
        setPFSize(80, 60);//menuJuego de juego

        //Objeto cursor, imágen que sigue las coordenadas del mouse
        cursor = new Cursor();

        //cargaJugador(0,0); reemplazamos por el metodo nuevo
        this.pj = new Jugador();
        this.pj.cargarDatos(this.idJugador);
        setCursor(null);
        inicializarTeclas();
        try {


            menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);

            casa1 = new Npc(680, 660, "casa1", "casa3", 2, 0, (short) 100,
                    new String[]{"Hola amigo",
                        "Miguel: Como estas, espero mejor que yo",
                        "Me doy cuenta que no eres de estos lados",
                        "lamento no poder atenderte,",
                        "pero me siento muy debil,",
                        "tanto asi que no he podido salir por una cura.",
                        "Estoy seguro que con una buena cantidad de hongos",
                        "tendria para cuidarme por unos dias,",
                        "te agradeceria mucho si los traes por mi.",
                        "20 serán suficientes, en la cuidad suelen",
                        "crecer en la humedad de las rocas."
                    });//casa superior
            casa2 = new Npc(80, 400, "casa2", "casa2", 8, 0, (short) 101, new String[]{"Casa 2"});
            casa3 = new Npc(350, 448, "casa3", "casa4", 8, 0, (short) 102, new String[]{"Casa 3"});
            casa4 = new Npc(80, 634, "casa3", "casa3", 8, 0, (short) 103, new String[]{"Casa 3"});
            casa5 = new Npc(350, 682, "casa3", "casa5", 8, 0, (short) 104, new String[]{"Casa 3"});
            alcaldia = new Npc(700, 75, "alcaldia", "casa4", 8, 0, (short) 105, new String[]{ "Alcalde: Hola forastero,", "actualemente la cuidad", "tiene muchos problemas,", "por favor ve y ayuda a la gente.", "Usualmente se mantienen", "en sus casas, temerosos", "de salir."});//casa superior
            //pasto1 = new Npc(192,128,"pasto","pasto",4,0,new String[]{"Hola amiguirijillo","soy pastillo1"});//pasto
            arbol1 = new Npc(352, 64, "arbol1", "arbol", 4, 0, (short) 106, new String[]{"Hola amiguirijillo", "soy Don Arbol, cuidame"});//
            arbol2 = new Npc(288, 32, "arbol2", "arbol", 4, 0, (short) 107, new String[]{"Hola amiguirijillo", "soy Don Arbol, cuidame"});//
            pileta = new Npc(128, 64, "arbol2", "pileta", 4, 0, (short) 108, new String[]{"Hola amiguirijillo", "soy la fuente magica"});//
            vendedor = new Npc(1040, 416, "vendedor", "vendedor", 2^5, 0, (short) 0, new String[]{"Hola amiguirijillo", "soy el vendedorsillo"});//
            
            
            //instancia mob y define como objeto home a pj
            this.mob = new Mob(100, 300, 1.5, (short) 100, "Mario", "mario", (short) 10, (short) 2, pj, false, 0.9, 192);



        } catch (Exception ex) {
            System.out.println("Extrae datos del HashMapsssssssssssssssss: " + ex);
        }
        /*
         * Mapa completo de tiles que definen el campo de juego.
         * Simbologia presente en el archivo TBL.
         */

        setTiles(
                0, // tile x index
                0, // tile y index
                new String[]{"+++++++++++++++++++++++++++++++++++|||||++++++++++++++++++++|||||+++++++++++++++",//1
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
                    "+++++++++++++++++++++++++++++++++++|||||++++++++++++++++++++++++++++++++++++++||",});
        textoPrueba = new String[]{"hola", "como estas?", "necesito dinero", "enserio", "piola"};
    }
    /** View offset. */
    int xofs = 0, yofs = 0;

    @Override
    public void doFrame() {
        capturarTeclas();
        
        if (((pj.isInteractuarNpc()) && ((getMouseButton(1)) || (getKey(KeyDown)))) || (interactuar > casa1.obtieneDialogo().length)) {
            pj = new Jugador();
            pj.cargarPersonaje((short) 1);
            removeObjects(getNomNpcInteractuar(), 51);
            pj.setInteractuarNpc(false);
            setInteractuar(0);
        } else if ((pj.isInteractuarNpc()) && (interactuar == 0)) {
            System.out.println(pj.npcInterac.getNomNpc() + "Npc");
            try {
                casa1 = new Npc(viewXOfs() + 380, viewYOfs() + 120, pj.npcInterac.getNomNpc() + "Npc", pj.npcInterac.getNomNpc() + "Npc", 51, 51, (short) (pj.npcInterac.getIdNpc()), pj.npcInterac.obtieneDialogo());
                // casa1.realizaTarea(pj);
                asd = new Ventana(300, 300, casa1.obtieneDialogo());//casa superior);
            } catch (Exception ex) {
                System.out.println("Extrae datos del HashMap: fsdfsdfsd" + ex);
            }
            setNomNpcInteractuar(pj.npcInterac.getNomNpc() + "Npc");
            setInteractuar(1);
        } else if ((interactuar > 0) && (interactuar < casa1.obtieneDialogo().length) && (getKey(KeyEnter))) {

            if (pausa == 10) {
                interactuar += 3;
            } else if (pausa > 10) {
                pausa = 0;
            }
            pausa += 1;
            //System.out.println("+3 interactuar");

        }
        moveObjects(null, 0);
        // llamada al metodo de colision entre objetos con las siguientes id de colision

        checkCollision(
                4 + 1, // cids of objects that our objects should collide with
                1 // cids of the objects whose hit() should be called
                );
        checkCollision(
                2 + 1, // cids of objects that our objects should collide with
                1 // cids of the objects whose hit() should be called
                );
        checkCollision(
                8 + 1, // cids of objects that our objects should collide with
                1 // cids of the objects whose hit() should be called
                );
        checkCollision(
                192 + 256, // cids of objects that our objects should collide with
                256 // cids of the objects whose hit() should be called
                );
        checkCollision(//vendedor
                2^5 + 256, // cids of objects that our objects should collide with
                256 // cids of the objects whose hit() should be called
                );
        checkCollision(//vendedor + pj
                1 + 2^5, // cids of objects that our objects should collide with
                2^5 // cids of the objects whose hit() should be called
                );
        checkCollision(//boton cerrar
                2^6 + 256, // cids of objects that our objects should collide with
                256 // cids of the objects whose hit() should be called
                );
        checkCollision(//boton cerrar + pj
                1 + 2^6, // cids of objects that our objects should collide with
                2^6 // cids of the objects whose hit() should be called
                );
        checkCollision(//ventana trade
                2^7 + 256, // cids of objects that our objects should collide with
                256 // cids of the objects whose hit() should be called
                );
        checkCollision(//ventana trade + pj
                1 + 2^7, // cids of objects that our objects should collide with
                2^7 // cids of the objects whose hit() should be called
                );
        // llamada al metodo de colision entre objeto y escenario con las siguientes id de colision
        checkBGCollision(
                1 + 11 + 13, // collide with the marble and border tiles
                1 // cids of our objects
                );

        int posX = (int) pj.x;
        int posY = (int) pj.y;

        xofs = posX;
        yofs = posY;
        setViewOffset(
                xofs, yofs,
                true);


            if(cursor.getVentana()==1){
                ventanaTrade = new Boton("ventana trade", "ventana trade",viewXOfs(),viewYOfs(),0);
                cerrar = new Boton("cerrar", "cerrar",viewXOfs()+300,viewYOfs()+150,2^6);
                cursor.setVentana((byte)2);
            }else if(cursor.getVentana()==3){
                removeObjects("ventana trade", 0);
                removeObjects("cerrar", 2^6);
                cursor.setVentana((byte)0);
            }


        /*
         * Comprueba si debe cerrarse la aplicacion
         */
        if (isSalir()) {
            System.exit(0);
        }
    }

    @Override
    public void paintFrame() {

        //panel basico
        menu.menuActual(getTeclaMenu());

        drawString("SEGUNDOS: " + seg, viewXOfs() + 200 / 2, viewHeight() / 2, 1);
        drawRect(viewXOfs() + 700, viewYOfs(), 100, viewHeight(), true, false);
        drawImage(cursor.x,cursor.y, "cursor");

        if (getKey(KeyEsc)) {
            menu.ventanaSalida();

            pj.bloquear(60);
            if (getKey(KeyEnter)) {
                menu.setTeclaEscape(false);
                setSalir(true);
            }
        }

        if (interactuar > 0) {
            // ventanaDialogo(pj.npcInterac.obtieneDialogo());
            asd.avanzarTexto();

        }

        //prueba de captura de teclas

        if (isPresionada(KeyCtrl)) {
            new Ventana("presionaste crtl");
        }
        /*
         * Detecta si has encontrado un hongo para la mision
         */

        if ((pj.x == 880.0) && (pj.y == 400.0) && (!hongo1)) {
            hongo1 = true;
            tiempoMensaje = 120;
            try {
                //agrego el hongo al inventario del jugador
                //Inventario[] tempInv = pj.getInv();
                pj.getInventario().agregarItem(pj.getIdPersonaje(), (short) 1);
                //tempInv[0].agregarItem(pj.getIdJugador(),(short)1000,(short)1);
                //pj.cargaInventario(pj.getIdJugador());
                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
            } catch (Exception ex) {
                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ((pj.x == 928) && (pj.y == 144) && (!hongo2)) {
            hongo2 = true;
            tiempoMensaje = 120;
            try {
                //agrego el hongo al inventario del jugador
                //Inventario[] tempInv = pj.getInv();
                pj.getInventario().agregarItem((short) 1000, (short) 1);
                //tempInv[0].agregarItem(pj.getIdJugador(),(short)1000,(short)1);
                //pj.cargaInventario(pj.getIdJugador());
                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
            } catch (Exception ex) {
                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ((pj.x == 80) && (pj.y == 48) && (!hongo3)) {
            hongo3 = true;
            tiempoMensaje = 120;
            try {
                //agrego el hongo al inventario del jugador
                //Inventario[] tempInv = pj.getInv();
                pj.getInventario().agregarItem(pj.getIdPersonaje(), (short) 1);
                //tempInv[0].agregarItem(pj.getIdJugador(),(short)1000,(short)1);
                //pj.cargaInventario(pj.getIdJugador());
                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
            } catch (Exception ex) {
                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ((pj.x == 416) && (pj.y == 624) && (!hongo4)) {
            hongo4 = true;
            tiempoMensaje = 120;
            try {
                //agrego el hongo al inventario del jugador
                //Inventario[] tempInv = pj.getInv();
                pj.getInventario().agregarItem(pj.getIdPersonaje(), (short) 1);
                //tempInv[0].agregarItem(pj.getIdJugador(),(short)1000,(short)1);
                //pj.cargaInventario(pj.getIdJugador());
                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
            } catch (Exception ex) {
                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ((pj.x == 64) && (pj.y == 800) && (!hongo5)) {
            hongo5 = true;
            tiempoMensaje = 120;
            try {
                //agrego el hongo al inventario del jugador
                //Inventario[] tempInv = pj.getInv();
                pj.getInventario().agregarItem(pj.getIdPersonaje(), (short) 1);
                //tempInv[0].agregarItem(pj.getIdJugador(),(short)1000,(short)1);
                //pj.cargaInventario(pj.getIdJugador());
                menu = new menuJuego(null, true, xofs, xofs, xofs, null, pj);
            } catch (Exception ex) {
                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if ((hongo1) && (hongo2) && (hongo3) && (hongo4) && (hongo5) && (!finHongos)) {
            finHongos = true;
        }
        if (tiempoMensaje > 0) {
            new Ventana("wena choro pillaste una callapampa!");
            // new Ventana(300, 300, textoPrueba);
        }
        tiempoMensaje--;


        if (cursor.getMensaje().length()>0){
            new Ventana(cursor.getMensaje());
            cursor.setMensaje("");
        }


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
            System.out.println(teclas.get(tecla) + "##############################################################");
            return true;
        }
        return false;
    }

    public void ventanaDialogo(String[] texto) {
        //System.out.println("Ventana dialogo: "+texto[0]);
        setColor(JGColor.blue);
        //drawRect(viewXOfs()+430,viewYOfs()+160,100,viewHeight()/2, true, false);
        drawRect(viewXOfs() + 250, viewYOfs() + 300, viewWidth() / 2 - 50, 81, true, false);
        setColor(JGColor.white);
        //drawRect(viewXOfs()+435,viewYOfs()+165,90,viewHeight()/2-10, true, false);
        drawRect(viewXOfs() + 255, viewYOfs() + 305, viewWidth() / 2 - 60, 71, true, false);

        setColor(JGColor.black);
        setFont(new JGFont("Arial", 0, 10));

        //System.out.println("Ventana dialogo pintada ");
        try {
            int cont = 0;
            while (cont < 3) {
                //System.out.println("cont: "+cont);
                //System.out.println("casa1.obtieneDialogo().length: "+casa1.obtieneDialogo().length);
                if ((interactuar + cont) < casa1.obtieneDialogo().length) {
                    final int i = 320;
                    drawString(texto[interactuar + cont], viewWidth() - 250, i + (cont + 1) * 10, 0);

                }
                cont += 1;
            }
        } catch (Exception ex) {
            System.out.println("Error al cargar ventana de dialogo: " + ex);
        }
        setColor(JGColor.red);
        drawString("Presiona [ENTER]", viewWidth() - 180, 365, 0);

        //System.out.println("Se cargaron 3 filas de dialogo");
        //System.out.println("Interactuar: "+interactuar);

    }

    public int getTeclaMenu() {
        int teclaPres = 0;
        if ((getKey(105)) || (getKey(73))) {
            teclaPres = 3;
        } else if ((getKey(101)) || (getKey(69))) {
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
            this.desplegarVentana();
            this.desplegarMensaje(mensaje);
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
            System.out.println("Ventanas: " + this.ventanas);
            System.out.println("Ventana_actual: " + this.ventana_actual);

        }

        /**
         * despliega una lista de mensajes en fragmetos de 3 líneas
         * @param mensajes
         * @param x coordenada X donde se ubica la ventana
         * @param y coordenada Y donde se ubica la ventana
         */
        public void desplegarMensaje(double x, double y, String[] mensajes) {
            byte separadorLinea = 16;
            desplegarVentana(x, y);
            setColor(JGColor.black);
            setFont(new JGFont("Arial", 0, 10));
            int linea = (ventana_actual - 1) * LINEAS;
            System.out.println("linea :----------------------- " + linea + " ----------------------------");
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
                System.out.println("''''''''''''''''''''''''''''''''''''''''''''''''''''''");
                espera = new JGTimer(60 * 1, true) {
                    @Override
                    public void alarm() {
                        setWait(false);
                        System.out.println("mMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMmmmmm");
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
            System.out.println("Ventanas: " + this.ventanas);
            System.out.println("Ventana_actual: " + this.ventana_actual);
            if (ventana_actual <= ventanas) {
                if (getKey(KeyEnter) && !isWait() ) {
                    setWait(true);
                    espera = null;
                    System.out.println("00000000000000000000000000000000000000000000000000000000000");
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
            //eng.setColor(JGColor.red);
            //eng.setFont(new JGFont("Arial", 0, 16));
            drawString(mensaje, x, y + 45, 0);

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

    }

        public class Boton extends JGObject {
            public Boton(String nombre,String graf,double x, double y,int cid){
                super(nombre,false,x,y,cid,graf);
            }
        @Override
            public void hit(JGObject obj){

            }
        }

        public class Item extends JGObject {
            public Item(String nombre,String graf,double x, double y,int cid){
                super(nombre,false,x,y,cid,graf);
            }
        }


	public class Cursor extends JGObject {
            private String mensaje = new String();
            private double ejex = eng.getMouseX() + eng.viewXOfs();
            private double ejey = eng.getMouseY() + eng.viewYOfs();
            private byte ventana = 0;

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
                    super("cursor",false,0,0,256,"cursor");
            }
            int oldmousex=0,oldmousey=0;
            boolean scissors_c=false;
            @Override
            public void move() {
                    // only follow mouse if it moves
                    int mx = eng.getMouseX() + eng.viewXOfs();
                    int my = eng.getMouseY() + eng.viewYOfs();
                    if (mx!=oldmousex) x=mx;
                    if (my!=oldmousey) y=my;
                    oldmousex=mx;
                    oldmousey=my;
            }

        @Override
            public void hit(JGObject obj) {

                System.out.println(obj.getGraphic());


                if(obj.getGraphic().equals("mario")){
                        setMensaje("Soy "+obj.getGraphic());
                }
                if(obj.getGraphic().equals("vendedor")){
                        setMensaje("Vendedor: Hola "+pj.getNombre()+", deseas hacer un trato?");
                        if(getMouseButton(3)){
                        setVentana((byte)1);
                        }
                }
                if((obj.getGraphic().equals("cerrar"))&&(getMouseButton(3))){
                        System.out.println("----------cerrar trade");
                        setMensaje("");
                        setVentana((byte)3);
                        System.out.println("getVentana: "+getVentana());
                        System.out.println(getMensaje()+"Nada");
                        System.out.println(getMouseButton(3)+" boton derecho del mouse");
                        

                }

                    
            }
    }


}
