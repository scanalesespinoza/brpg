/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map.Entry;
import jgame.JGColor;
import jgame.JGFont;
import jgame.JGObject;
import java.util.Iterator;
import java.util.Map;
import jgame.JGPoint;

/**
 *
 * @author GetWay
 */
public class menuJuego extends JGObject {

    private Jugador pj;
    private boolean teclaEscape = false;
    private boolean teclaEnter = false;
    private Habilidad hab = new Habilidad();
    private Objeto obj = new Objeto();
    private Jugador pjTest;
    private HashMap<Integer, Icono> hmPj = new HashMap<Integer, Icono>();
    private HashMap<Integer, Icono> hmNpc = new HashMap<Integer, Icono>();
    private int pos_inicial_x, pos_inicial_y;
    private JGPoint recorrido;
    private int tabla_inicial_x, tabla_inicial_y;
    private JGPoint tabla;
    private HashMap<Short, Boton> botones_estadistica_aumentar,botones_estadistica_ver;
    private dbDelegate conexion;
    private HashMap<Short, Boton> botones_habilidad_aumentar, botones_habilidad_ver;
    private HashMap<Short, Habilidad> habilidades;
    private HashMap<Short, Boton> botones_mision_ver, botones_mision_abandonar;
    private int menuActual = 0;

    public HashMap<Integer, Icono> getHmNpc() {
        return hmNpc;
    }

    public void setHmNpc(HashMap<Integer, Icono> hmNpc) {
        this.hmNpc = hmNpc;
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

    public HashMap<Integer, Icono> getHm() {
        return hmPj;
    }

    public void setHm(HashMap<Integer, Icono> hm) {
        this.hmPj = hm;
    }

    public menuJuego(String string, boolean bln, double d, double d1, int i, String string1, Jugador pj) {
        super(string, bln, d, d1, i, string1);
        this.pj = pj;
        this.botones_estadistica_aumentar = new HashMap<Short, Boton>();
        this.botones_estadistica_ver = new HashMap<Short, Boton>();
        /**
         * ids para estadisticas
         * 1 = fuerza
         * 2 = destreza
         * 3 = Sabiduria
         * 4 = vitalidad
         */
        this.botones_estadistica_aumentar.put((short) 1, new Boton("fuerza"+"_aumentar", "suma", eng.viewWidth() - 88, 30, (int) Math.pow(2, 5), 1, 1));
        this.botones_estadistica_aumentar.put((short) 2, new Boton("destreza"+"_aumentar", "suma", eng.viewWidth() - 88, 45, (int) Math.pow(2, 5), 1, 2));
        this.botones_estadistica_aumentar.put((short) 3, new Boton("sabiduria"+"_aumentar", "suma", eng.viewWidth() - 88, 60, (int) Math.pow(2, 5), 1, 3));
        this.botones_estadistica_aumentar.put((short) 4, new Boton("vitalidad"+"_aumentar", "suma", eng.viewWidth() - 88, 75, (int) Math.pow(2, 5), 1, 4));
        this.botones_estadistica_aumentar.get((short) 1).suspend();
        this.botones_estadistica_aumentar.get((short) 2).suspend();
        this.botones_estadistica_aumentar.get((short) 3).suspend();
        this.botones_estadistica_aumentar.get((short) 4).suspend();
        this.botones_estadistica_ver.put((short) 1, new Boton("fuerza_ver", "ver", eng.viewWidth() - 8, 30, (int) Math.pow(2, 5), 3, 1));
        this.botones_estadistica_ver.put((short) 2, new Boton("destreza_ver", "ver", eng.viewWidth() - 8, 45, (int) Math.pow(2, 5), 3, 2));
        this.botones_estadistica_ver.put((short) 3, new Boton("sabiduria_ver", "ver", eng.viewWidth() - 8, 60, (int) Math.pow(2, 5), 3, 3));
        this.botones_estadistica_ver.put((short) 4, new Boton("vitalidad_ver", "ver", eng.viewWidth() - 8, 75, (int) Math.pow(2, 5), 3, 4));
        this.botones_estadistica_ver.get((short) 1).suspend();
        this.botones_estadistica_ver.get((short) 2).suspend();
        this.botones_estadistica_ver.get((short) 3).suspend();
        this.botones_estadistica_ver.get((short) 4).suspend();
        this.conexion = new dbDelegate();
        System.out.println("Inicio obtiene datos personaje");
        String StrSql = "SELECT * FROM habilidad ";
        this.botones_habilidad_aumentar = new HashMap<Short, Boton>();
        this.botones_habilidad_ver = new HashMap<Short, Boton>();
        this.habilidades = new HashMap<Short, Habilidad>();
        System.out.println(StrSql);
        double linea = 30;
        try {
            ResultSet res = conexion.Consulta(StrSql);
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
                Boton btn = new Boton(habi.getNombre()+"_aumentar", "suma", eng.viewWidth() - 88, linea, (int) Math.pow(2, 5), 1, habi.getIdHabilidad());
                btn.suspend();
                this.botones_habilidad_aumentar.put(habi.getIdHabilidad(), btn);
                btn = new Boton(habi.getNombre()+"_ver", "ver", eng.viewWidth() - 8, linea, (int) Math.pow(2, 5), 3, habi.getIdHabilidad());
                btn.suspend();
                this.botones_habilidad_ver.put(habi.getIdHabilidad(), btn);
                linea += 15;
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->habilidades , método->setHabilidad() " + ex);
        }

        this.botones_mision_ver = new HashMap<Short, Boton>();
        this.botones_mision_abandonar = new HashMap<Short, Boton>();
    }

    public void menuActual(int menu, Jugador pj) {
        eng.setColor(JGColor.white);
        eng.setFont(new JGFont("Arial", 2, 20));
        int linea;

        switch (menu) {
            case 0/*"main"*/:
                menuActual = 0;
                eng.setFont(new JGFont("Arial", 1, 14));//fuente titulo
                pjTest = (Jugador) eng.getObject("player");
                eng.drawString("General", eng.viewWidth() - 80, 10, -1);
                eng.setFont(new JGFont("Arial", 0, 10));//fuente parrafo
                eng.drawString("Nombre: " + pjTest.getNombre(), eng.viewWidth() - 75, 30, -1);
                eng.drawString("Nivel: " + pjTest.getNivel(), eng.viewWidth() - 75, 40, -1);
                eng.drawString("Experiencia :" + pjTest.getExperiencia(), eng.viewWidth() - 75, 50, -1);
                eng.drawRect(eng.viewWidth() - 75 + eng.viewXOfs(), 60 + eng.viewYOfs(), (float) (pj.getExperiencia() * 100 / pj.getLimiteSuperiorExperiencia()), 10, true, false, 0, JGColor.orange);
                eng.setColor(JGColor.white);
                eng.drawString("Dinero :" + pjTest.getDinero(), eng.viewWidth() - 75, 70, -1);
                eng.setColor(JGColor.white);
                removerIconos();
                suspenderBotones(1);
                suspenderBotones(4);
                suspenderBotones(2);
                break;
            case 1/*"habilidad"*/:
                menuActual = 1;
                eng.setFont(new JGFont("Arial", 1, 14));//fuente titulo
                eng.setColor(JGColor.yellow);
                eng.drawString("Habilidades", eng.viewWidth() - 75, 10, -1);
                eng.setColor(JGColor.white);
                eng.setFont(new JGFont("Arial", 0, 10));//fuente parrafo
                //Dibujo todas las habilidades que el sistema posee
                //si el personaje no tiene la habilidad o no la tiene al maximo nivel.. se muestra
                //el boton =)
                if (pj.getTotalPuntosHabilidad() > 0) {
                    eng.drawString("Ptos. Restantes:" + pj.getTotalPuntosHabilidad(), eng.viewWidth() - 75, 20, -1);
                }
                String linea_menu = "";
                Iterator it = this.botones_habilidad_aumentar.entrySet().iterator();
                linea = 30;
                while (it.hasNext()) {
                    Map.Entry e = (Map.Entry) it.next();
                    Boton boton = (Boton) e.getValue();
                    linea_menu = habilidades.get(Short.parseShort(e.getKey().toString())).getNombre();
                    if (pj.getTotalPuntosHabilidad() > 0) {
                        if (!pj.getHabilidades().tieneHabilidad(Short.parseShort(e.getKey().toString()))
                                || ((pj.getHabilidades().tieneHabilidad(Short.parseShort(e.getKey().toString()))) && pj.getHabilidades().getHabilidad(Short.parseShort(e.getKey().toString())).getNivelHabilidad() < pj.getHabilidades().getHabilidad(Short.parseShort(e.getKey().toString())).getHabilidad().getNivelMaximo())) {
                            boton.resume();
                            boton.pintar();

                        }
                    }
                    linea_menu += " N: ";
                    if (pj.getHabilidades().tieneHabilidad(Short.parseShort(e.getKey().toString()))) {
                        linea_menu += pj.getHabilidades().getHabilidad(Short.parseShort(e.getKey().toString())).getNivelHabilidad();
                    } else {
                        linea_menu += "0";
                    }
                    this.botones_habilidad_ver.get((short)boton.getId()).resume();
                    this.botones_habilidad_ver.get((short)boton.getId()).pintar();
                    eng.drawString(linea_menu, eng.viewWidth() - 75, linea, -1);
                    linea += 15;
                }

                break;
            case 2/*"mision"*/:
                menuActual = 2;
                eng.setFont(new JGFont("Arial", 1, 14));//fuente titulo
                eng.setColor(JGColor.yellow);
                eng.drawString("Misiones", eng.viewWidth() - 75, 10, -1);
                eng.setColor(JGColor.white);
                eng.setFont(new JGFont("Arial", 0, 10));//fuente parrafo
                Iterator it1 = pj.getMisiones().getMisiones().entrySet().iterator();
                linea = 30;
                while (it1.hasNext()) {
                    Map.Entry e = (Map.Entry) it1.next();
                    if (pj.getMisiones().getMisiones().get(Short.parseShort(e.getKey().toString())).getRolPersonaje() != -1) {
                        //La mision no está borrada lógicamente, se procede a dibujar
                        Mision mi = pj.getMisiones().getMisiones().get(Short.parseShort(e.getKey().toString())).getMision();
                        if (this.botones_mision_ver.containsKey(mi.getIdMision())) {
                            this.botones_mision_ver.get(mi.getIdMision()).resume();
                            this.botones_mision_ver.get(mi.getIdMision()).setyAnt(linea);
                            this.botones_mision_ver.get(mi.getIdMision()).pintar();
                            this.botones_mision_abandonar.get(mi.getIdMision()).resume();
                            this.botones_mision_abandonar.get(mi.getIdMision()).setyAnt(linea);
                            this.botones_mision_abandonar.get(mi.getIdMision()).pintar();
                        } else {
                            Boton btn = new Boton(mi.getNombre() + "_ver", "ver", eng.viewWidth() - 8, linea, (int) Math.pow(2, 5), 3, mi.getIdMision());
                            btn.suspend();
                            this.botones_mision_ver.put(mi.getIdMision(), btn);
                            btn = new Boton(mi.getNombre() + "_abandonar", "abandonar", eng.viewWidth() - 88, linea, (int) Math.pow(2, 5), 4, mi.getIdMision());
                            btn.suspend();
                            this.botones_mision_abandonar.put(mi.getIdMision(), btn);
                        }
                        eng.drawString(mi.getNombre(), eng.viewWidth() - 75, linea, -1);
                        linea += 15;
                    }
                }

                break;
//                case 3/*"inventario"*/:
//                    /*
//                     * Es llamada desde manager y pintada en menuJuego.paintB
//                     */
//
//                    break;
            case 4/*"estadistica"*/:
                eng.setFont(new JGFont("Arial", 1, 14));
                eng.setColor(JGColor.yellow);
                eng.drawString("Estadisticas", eng.viewWidth() - 75, 10, -1);
                eng.setFont(new JGFont("Arial", 0, 10));
                menuActual = 4;
                if (pj.getTotalPuntosEstadistica() > 0) {
                    eng.drawString("Ptos. Restantes:" + pj.getTotalPuntosEstadistica(), eng.viewWidth() - 75, 20, -1);
                    this.botones_estadistica_aumentar.get((short) 1).resume();
                    this.botones_estadistica_aumentar.get((short) 2).resume();
                    this.botones_estadistica_aumentar.get((short) 3).resume();
                    this.botones_estadistica_aumentar.get((short) 4).resume();
                    this.botones_estadistica_aumentar.get((short) 1).pintar();
                    this.botones_estadistica_aumentar.get((short) 2).pintar();
                    this.botones_estadistica_aumentar.get((short) 3).pintar();
                    this.botones_estadistica_aumentar.get((short) 4).pintar();
                    
                } else {
                    this.suspenderBotones(4);
                }
                this.botones_estadistica_ver.get((short) 1).resume();
                    this.botones_estadistica_ver.get((short) 2).resume();
                    this.botones_estadistica_ver.get((short) 3).resume();
                    this.botones_estadistica_ver.get((short) 4).resume();
                    this.botones_estadistica_ver.get((short) 1).pintar();
                    this.botones_estadistica_ver.get((short) 2).pintar();
                    this.botones_estadistica_ver.get((short) 3).pintar();
                    this.botones_estadistica_ver.get((short) 4).pintar();
                eng.setColor(JGColor.white);
                eng.drawString("Fuerza:    " + pj.getFuerza(), eng.viewWidth() - 75, 30, -1);
                eng.drawString("Destreza:  " + pj.getDestreza(), eng.viewWidth() - 75, 45, -1);
                eng.drawString("Sabiduria: " + pj.getSabiduria(), eng.viewWidth() - 75, 60, -1);
                eng.drawString("Vitalidad: " + pj.getVitalidad(), eng.viewWidth() - 75, 75, -1);
                eng.setFont(new JGFont("Arial", 0, 10));
                eng.drawString("Adherir puntos", eng.viewWidth() - 75, 100, -1);
                eng.drawString("(+)", eng.viewWidth() - 75, 120, -1);
                break;
            case 5/*"Opciones"*/:
                menuActual = 5;
                eng.setFont(new JGFont("Arial", 1, 14));
                eng.setColor(JGColor.yellow);
                eng.drawString("Opciones", eng.viewWidth() - 75, 10, -1);
                eng.setColor(JGColor.white);
                eng.setFont(new JGFont("Arial", 0, 10));
                eng.drawString("Teclas", eng.viewWidth() - 75, 30, -1);
                eng.drawString("Musica", eng.viewWidth() - 75, 40, -1);
                break;


        }
        eng.setFont(new JGFont("Arial", 1, 14));
        eng.drawString("Menu[Tecla]", eng.viewWidth() - 80, 320, -1);
        eng.setFont(new JGFont("Arial", 0, 10));
        eng.drawString("General     ", eng.viewWidth() - 75, 340, -1);
        eng.drawString("Habilidades [H]", eng.viewWidth() - 75, 350, -1);
        eng.drawString("Misiones     [M]", eng.viewWidth() - 75, 360, -1);
        eng.drawString("Inventario    [ I ]  ", eng.viewWidth() - 75, 370, -1);
        eng.drawString("Estadisticas[E]", eng.viewWidth() - 75, 380, -1);
        eng.drawString("Opciones    [O]", eng.viewWidth() - 75, 390, -1);
        eng.setFont(new JGFont("Arial", 0, 10));
        eng.drawString("Salir del Juego", eng.viewWidth() - 75, 430, -1);
        eng.drawString("[Escape]", eng.viewWidth() - 75, 450, -1);



    }

    private void suspenderBotones(int i) {
        HashMap<Short, Boton> boton = new HashMap<Short, Boton>();
        if (i == 4) {
            boton = this.botones_estadistica_aumentar;
        } else if (i == 1) {
            boton = this.botones_habilidad_aumentar;
        } else if (i == 2) {
            boton = this.botones_mision_ver;
        }
        Iterator it = boton.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            boton.get(Short.parseShort(e.getKey().toString())).suspend();
        }
    }

    public int getMenuActual() {
        return menuActual;
    }

    public void setMenuActual(int menuActual) {
        this.menuActual = menuActual;
    }

    public void paintB() {
        if (eng.inGameState("InCombat")) {
            eng.drawImage(0, 0, "combate", false);
        }

//            eng.drawString("Ancho: "+eng.viewWidth()+" Alto: "+eng.viewHeight(), eng.viewWidth()/2, eng.viewHeight()/2, 0);
        eng.drawImage(0, eng.viewHeight() - 90, "monitor", false);
        eng.drawImage(eng.viewWidth() - 90, 0, "lateral", false);
        eng.drawImage(eng.viewWidth() - 90, 315, "titulo", false);
        eng.drawImage(eng.viewWidth() - 90, 5, "titulo", false);

        if (eng.inGameState("InCombat")) {
            setSeccion(new JGPoint(110, 330), new JGPoint(12, 1));
            generaSeccion(1);

        }
        if (eng.inGameState("InCommerce")) {

            eng.drawImage(20, 20, "trade", false);
            eng.drawImage(200, 20, "trade", false);
            setSeccion(new JGPoint(25, 25), new JGPoint(2, 4));
            generaSeccion(1);
            setSeccion(new JGPoint(250, 25), new JGPoint(3, 4));
            generaSeccion(0);

        }
        if ((eng.inGameState("InWorld")) && ((eng.getKey(73)) || (eng.getKey(105)))) {
            eng.setFont(new JGFont("Arial", 1, 14));
            eng.setColor(JGColor.yellow);
            eng.drawString("Inventario", eng.viewWidth() - 60, 10, 0);
            eng.setFont(new JGFont("Arial", 0, 10));
            eng.setColor(JGColor.white);

            setSeccion(new JGPoint(eng.viewWidth() - 80, 40), new JGPoint(2, 10));
            generaSeccion(1);
        }

        if (eng.inGameState("InReward")) {
            removerIconos();
            setSeccion(new JGPoint(eng.viewWidth() / 2 - 100, eng.viewHeight() / 2 - 45), new JGPoint(4, 4));
            generaSeccion(0);
        }


    }

    public void recibeHm(HashMap<Integer, Icono> hm, int tipo) {
//        System.out.println("HM vacio: "+hm.isEmpty());
//        System.out.println("HM vacio: "+this.hm.isEmpty());
        if (tipo == 1) {
            setHm(hm);
        }
        if (tipo == 0) {
            setHmNpc(hm);
        }
//        System.out.println("HM vacio: "+this.hm.isEmpty());
    }

    public void generaSeccion(int tipo) {

        switch (tipo) {

            case 1:
                System.out.println("HM vacio: " + this.hmPj.isEmpty());
                if (!hmPj.isEmpty()) {

                    Iterator iter = hmPj.entrySet().iterator();
                    boolean fin = false;

                    while ((this.tabla.y > 0) && (!fin)) {
                        while ((this.tabla.x > 0) && (!fin)) {
                            System.out.println("dibuja tabla");
                            if (iter.hasNext()) {
                                System.out.println("Has next");
                                Map.Entry en = (Map.Entry) iter.next();
                                Icono drawIcon = (Icono) en.getValue();
                                eng.drawImage(this.recorrido.x, this.recorrido.y, drawIcon.getGraphic(), false);
                                drawIcon.paintB();
                                this.recorrido.x += 37;
                                this.tabla.x--;
                            } else {
                                fin = true;
                            }
                        }
                        this.recorrido.x = pos_inicial_x;
                        this.tabla.x = tabla_inicial_x;
                        this.tabla.y--;
                        this.recorrido.y += 37;
                    }


//                    Iterator iter = hm.entrySet().iterator();
//                    while (iter.hasNext()) {
//                        Map.Entry en = (Map.Entry) iter.next();
//                        Icono drawIcon = (Icono) en.getValue();
//
//                        eng.drawImage(drawIcon.x, drawIcon.y, drawIcon.getGraphic(), false);
//
//                    }

                }
                break;
            case 0:
                System.out.println("HM vacio: " + this.hmNpc.isEmpty());
                if (!hmNpc.isEmpty()) {

                    Iterator iter = hmNpc.entrySet().iterator();
                    boolean fin = false;

                    while ((this.tabla.y > 0) && (!fin)) {
                        while ((this.tabla.x > 0) && (!fin)) {
                            System.out.println("dibuja tabla");
                            if (iter.hasNext()) {
                                System.out.println("Has next");
                                Map.Entry en = (Map.Entry) iter.next();
                                Icono drawIcon = (Icono) en.getValue();
                                eng.drawImage(this.recorrido.x, this.recorrido.y, drawIcon.getGraphic(), false);
                                drawIcon.paintB();
                                this.recorrido.x += 37;
                                this.tabla.x--;
                            } else {
                                fin = true;
                            }
                        }
                        this.recorrido.x = pos_inicial_x;
                        this.tabla.x = tabla_inicial_x;
                        this.tabla.y--;
                        this.recorrido.y += 37;
                    }


//                    Iterator iter = hm.entrySet().iterator();
//                    while (iter.hasNext()) {
//                        Map.Entry en = (Map.Entry) iter.next();
//                        Icono drawIcon = (Icono) en.getValue();
//
//                        eng.drawImage(drawIcon.x, drawIcon.y, drawIcon.getGraphic(), false);
//
//                    }

                }
                break;
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

    public void ventanaSalida() {
        eng.setColor(JGColor.red);
        eng.drawRect(eng.viewXOfs() + 200, eng.viewYOfs() + 250, 300, 100, true, false);
        eng.setColor(JGColor.white);
        eng.drawRect(eng.viewXOfs() + 205, eng.viewYOfs() + 255, 290, 90, true, false);


        eng.setColor(JGColor.red);
        eng.setFont(new JGFont("Arial", 0, 16));
        eng.drawString("Deseas salir del Juego?", eng.viewWidth() / 2, eng.viewHeight() / 2 + 45, 0);
        eng.setFont(new JGFont("Arial", 0, 20));
        eng.drawString("Confirmar [ENTER]", eng.viewWidth() / 2, eng.viewHeight() / 2 + 65, 0);

    }

    public void ventanaAlert(String mensaje) {
        eng.setColor(JGColor.red);
        eng.drawRect(eng.viewXOfs() + 200, eng.viewYOfs() + 250, 300, 100, true, false);
        eng.setColor(JGColor.white);
        eng.drawRect(eng.viewXOfs() + 205, eng.viewYOfs() + 255, 290, 90, true, false);


        eng.setColor(JGColor.red);
        eng.setFont(new JGFont("Arial", 0, 16));
        eng.drawString(mensaje, eng.viewWidth() / 2, eng.viewHeight() / 2 + 45, 0);

    }

    public Jugador getPj() {
        return pj;
    }

    public void setPj(Jugador pj) {
        this.pj = pj;
    }

    public boolean isTeclaEnter() {
        return teclaEnter;
    }

    public void setTeclaEnter(boolean teclaEnter) {
        this.teclaEnter = teclaEnter;
    }

    public boolean isTeclaEscape() {
        return teclaEscape;
    }

    public void setTeclaEscape(boolean teclaEscape) {
        this.teclaEscape = teclaEscape;
    }

    public void removerIconos() {
        eng.removeObjects("iconoMenu", (int) Math.pow(2, 4));
    }
}
