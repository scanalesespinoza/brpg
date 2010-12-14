/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import extensiones.StdScoring;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
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
    private Jugador pjTest;
    private HashMap<Integer, Icono> hmPjItem = new HashMap<Integer, Icono>();
    private HashMap<Integer, Icono> hmPjHabilidades = new HashMap<Integer, Icono>();
    private HashMap<Integer, Icono> hmNpc = new HashMap<Integer, Icono>();
    private int pos_inicial_x, pos_inicial_y;
    private JGPoint recorrido;
    private int tabla_inicial_x, tabla_inicial_y;
    private JGPoint tabla;
    private HashMap<Short, Boton> botones_estadistica_aumentar, botones_estadistica_ver;
    private dbDelegate conexion;
    private HashMap<Short, Boton> botones_habilidad_aumentar, botones_habilidad_ver;
    private HashMap<Short, Habilidad> habilidades;
    private HashMap<Short, Boton> botones_mision_ver, botones_mision_abandonar;
    private int menuActual = 0;
    private StdScoring stdScorePj;
    private StdScoring stdScoreNpc;
    private final HashMap<Short, Boton> botones_objetos_abandonar;
//    private final HashMap<Short, Boton> botones_objetos_usar;
    private final HashMap<Short, Boton> botones_objetos_ver;
    public int filtrar = 0;
    private boolean continua_anim_mob = false;
    public String anim_mob, anim_mob_flag, anim_pj, anim_pj_flag;
    public int frames_mob, frames_pj;
    public int frame_mob;
    private int tick_go_mob;
    private int tick_actual_mob = 0;
    private boolean continua_anim_pj = false;
    public int frame_pj;
    private double tick_go_pj;
    private double tick_actual_pj = 0;
    public boolean termineAnimacionMuerte = false;
    public boolean dibujandoAnimacionMuerte = false;
	public boolean vestir = false;
    public StdScoring getStdScoreNpc() {
        return stdScoreNpc;
    }

    public void setStdScoreNpc(StdScoring stdScoreNpc) {
        this.stdScoreNpc = stdScoreNpc;
    }

    public StdScoring getStdScorePj() {
        return stdScorePj;
    }

    public void setStdScorePj(StdScoring stdScorePj) {
        this.stdScorePj = stdScorePj;
    }

    public HashMap<Integer, Icono> getHmPjHabilidades() {
        return hmPjHabilidades;
    }

    public void setHmPjHabilidades(HashMap<Integer, Icono> hmPjHabilidades) {
        this.hmPjHabilidades = hmPjHabilidades;
    }

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
        return hmPjItem;
    }

    public void setHm(HashMap<Integer, Icono> hm) {
        this.hmPjItem = hm;
    }

    public menuJuego(String string, boolean bln, double d, double d1, int i, String string1, Jugador pj, dbDelegate con) {
        super(string, bln, d, d1, i, string1);
        this.conexion = con;
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
        this.botones_estadistica_aumentar.put((short) 1, new Boton("fuerza" + "_aumentar", "suma", eng.viewWidth() - 88, 30, (int) Math.pow(2, 5), 1, 1));
        this.botones_estadistica_aumentar.put((short) 2, new Boton("destreza" + "_aumentar", "suma", eng.viewWidth() - 88, 45, (int) Math.pow(2, 5), 1, 2));
        this.botones_estadistica_aumentar.put((short) 3, new Boton("sabiduria" + "_aumentar", "suma", eng.viewWidth() - 88, 60, (int) Math.pow(2, 5), 1, 3));
        this.botones_estadistica_aumentar.put((short) 4, new Boton("vitalidad" + "_aumentar", "suma", eng.viewWidth() - 88, 75, (int) Math.pow(2, 5), 1, 4));
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

        String StrSql = "SELECT * FROM habilidad ";
        this.botones_habilidad_aumentar = new HashMap<Short, Boton>();
        this.botones_habilidad_ver = new HashMap<Short, Boton>();
        this.habilidades = new HashMap<Short, Habilidad>();
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
                Boton btn = new Boton(habi.getNombre() + "_aumentar", "suma", eng.viewWidth() - 88, linea, (int) Math.pow(2, 5), 1, habi.getIdHabilidad());
                btn.suspend();
                this.botones_habilidad_aumentar.put(habi.getIdHabilidad(), btn);
                btn = new Boton(habi.getNombre() + "_ver", "ver", eng.viewWidth() - 8, linea, (int) Math.pow(2, 5), 3, habi.getIdHabilidad());
                btn.suspend();
                this.botones_habilidad_ver.put(habi.getIdHabilidad(), btn);
                linea += 15;
            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->habilidades , método->setHabilidad() " + ex);
        }

        this.botones_mision_ver = new HashMap<Short, Boton>();
        this.botones_mision_abandonar = new HashMap<Short, Boton>();
        this.botones_objetos_abandonar = new HashMap<Short, Boton>();
//        this.botones_objetos_usar = new HashMap<Short, Boton>();
        this.botones_objetos_ver = new HashMap<Short, Boton>();

    }

    public void menuActual(int menu, Jugador pj) {
        eng.setColor(JGColor.white);
        eng.setFont(new JGFont("Arial", 2, 20));
        int linea_y;
        int linea_x;

        switch (menu) {
            case 0/*"main"*/:
                menuActual = 0;
                eng.setFont(new JGFont("Arial", 1, 14));//fuente titulo
                pjTest = (Jugador) eng.getObject("player");
                eng.drawString("General", eng.viewWidth() - 80, 10, -1);
                eng.setFont(new JGFont("Arial", 0, 10));//fuente parrafo
                eng.drawString("Nombre: " + pjTest.getNombre(), eng.viewWidth() - 75, 30, -1);
                eng.drawString("Nivel: " + pjTest.getNivel(), eng.viewWidth() - 75, 40, -1);
                eng.drawString("Experiencia", eng.viewWidth() - 75, 50, -1);
                eng.drawString("Dinero :" + pjTest.getDinero(), eng.viewWidth() - 75, 83, -1);
                eng.drawString("Peso libre :" + pjTest.getPesoDisponible(), eng.viewWidth() - 75, 93, -1);
                eng.setColor(JGColor.red);
                eng.drawRect(eng.viewWidth() - 80, 70, 68, 10, false, false, false);
                eng.setColor(JGColor.yellow);
                eng.drawRect(eng.viewWidth() - 79, 71, (pj.getExperiencia() * 67) / pj.getLimiteSuperiorExperiencia(), 9, true, false, false, new JGColor[]{JGColor.blue, JGColor.orange, JGColor.green, JGColor.magenta, JGColor.white, JGColor.red});
                eng.setColor(JGColor.white);
                eng.drawString(pj.getExperiencia() + "/" + pj.getLimiteSuperiorExperiencia(), eng.viewWidth() - 50, 60, 0, false);

                eng.setColor(JGColor.orange);
//                eng.drawRect(eng.viewWidth() - 80, 263 , 70, 52.5, true, false, false);
                eng.drawImage(eng.viewWidth() - 80, 263, "mini_mapa", false);
                eng.setColor(JGColor.black);
                eng.drawOval(((pj.x * 70) / eng.pfWidth()) + (eng.viewWidth() - 80), ((pj.y * 52.5) / eng.pfHeight()) + (263), 4, 4, true, true, false);
                eng.setColor(JGColor.white);
                //Dibujar el personaje en el "mini..Mapa"
                removerIconos();
                suspenderBotones(1);
                suspenderBotones(4);
                suspenderBotones(2);
                suspenderBotones(3);
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

                String linea_menu = "";
                Iterator it = this.botones_habilidad_aumentar.entrySet().iterator();
                linea_y = 30;
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
                    this.botones_habilidad_ver.get((short) boton.getId()).resume();
                    this.botones_habilidad_ver.get((short) boton.getId()).pintar();
                    eng.drawString(linea_menu, eng.viewWidth() - 75, linea_y, -1);
                    linea_y += 15;
                }
                if (pj.getTotalPuntosHabilidad() > 0) {
                    eng.drawString("Ptos. Restantes:" + pj.getTotalPuntosHabilidad(), eng.viewWidth() - 75, 20, -1);
                } else {
                    suspenderBotones(1);
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
                linea_y = 30;
                while (it1.hasNext()) {
                    Map.Entry e = (Map.Entry) it1.next();
                    if (pj.getMisiones().getMisiones().get(Short.parseShort(e.getKey().toString())).getRolPersonaje() != -1) {
                        //La mision no está borrada lógicamente, se procede a dibujar
                        Mision mi = pj.getMisiones().getMisiones().get(Short.parseShort(e.getKey().toString())).getMision();
                        if (this.botones_mision_ver.containsKey(mi.getIdMision())) {
                            this.botones_mision_ver.get(mi.getIdMision()).resume();
                            this.botones_mision_ver.get(mi.getIdMision()).setyAnt(linea_y);
                            this.botones_mision_ver.get(mi.getIdMision()).pintar();
                            this.botones_mision_abandonar.get(mi.getIdMision()).resume();
                            this.botones_mision_abandonar.get(mi.getIdMision()).setyAnt(linea_y);
                            this.botones_mision_abandonar.get(mi.getIdMision()).pintar();
                        } else {
                            Boton btn = new Boton(mi.getNombre() + "_ver", "ver", eng.viewWidth() - 8, linea_y, (int) Math.pow(2, 5), 3, mi.getIdMision());
                            btn.suspend();
                            this.botones_mision_ver.put(mi.getIdMision(), btn);
                            btn = new Boton(mi.getNombre() + "_abandonar", "abandonar", eng.viewWidth() - 88, linea_y, (int) Math.pow(2, 5), 4, mi.getIdMision());
                            btn.suspend();
                            this.botones_mision_abandonar.put(mi.getIdMision(), btn);
                        }
                        eng.drawString(mi.getNombre(), eng.viewWidth() - 75, linea_y, -1);
                        linea_y += 15;
                    }
                }

                break;
//            case 3/*"inventario"*/:
//                eng.setFont(new JGFont("Arial", 1, 14));
//                eng.setColor(JGColor.yellow);
//                eng.drawString("Inventario", eng.viewWidth() - 75, 10, 0);
//                eng.setFont(new JGFont("Arial", 0, 10));
//                eng.setColor(JGColor.white);
//                menuActual = 3;
//                int cont = 0;
//                Iterator it3 = pj.getInventario().getObjetos().entrySet().iterator();
//                linea_y = 30;
//                linea_x = eng.viewWidth() - 80;
//                while (it3.hasNext()) {
//                    Map.Entry e = (Map.Entry) it3.next();
//                    //Si tiene almenos una cantidad de objetos lo dibujo
//                    if (pj.getInventario().tieneItem(Short.parseShort(e.getKey().toString()))) {
//                        Objeto ob = pj.getInventario().getObjetos().get(Short.parseShort(e.getKey().toString())).getObjeto();
//                        if (this.botones_objetos_ver.containsKey(ob.getIdObjeto())) {
//                            this.botones_objetos_ver.get(ob.getIdObjeto()).resume();
//                            this.botones_objetos_ver.get(ob.getIdObjeto()).setyAnt(linea_y + 32);
//                            this.botones_objetos_ver.get(ob.getIdObjeto()).setxAnt(linea_x + 8);
//                            this.botones_objetos_ver.get(ob.getIdObjeto()).pintar();
//                            this.botones_objetos_abandonar.get(ob.getIdObjeto()).resume();
//                            this.botones_objetos_abandonar.get(ob.getIdObjeto()).setyAnt(linea_y + 32);
//                            this.botones_objetos_abandonar.get(ob.getIdObjeto()).setxAnt(linea_x + 16);
//                            this.botones_objetos_abandonar.get(ob.getIdObjeto()).pintar();
//                            this.botones_objetos_usar.get(ob.getIdObjeto()).resume();
//                            this.botones_objetos_usar.get(ob.getIdObjeto()).setyAnt(linea_y);
//                            this.botones_objetos_usar.get(ob.getIdObjeto()).setxAnt(linea_x);
//                            this.botones_objetos_usar.get(ob.getIdObjeto()).pintar();
//                        } else {
//                            Boton btn = new Boton(ob.getNombre() + "_ver", "ver", linea_x + 8, linea_y + 32, (int) Math.pow(2, 5), 3, ob.getIdObjeto());
//                            btn.suspend();
//                            this.botones_objetos_ver.put(ob.getIdObjeto(), btn);
//
//                            btn = new Boton(ob.getNombre() + "_abandonar", "abandonar", linea_x + 16, linea_y + 32, (int) Math.pow(2, 5), 4, ob.getIdObjeto());
//                            btn.suspend();
//                            this.botones_objetos_abandonar.put(ob.getIdObjeto(), btn);
//
//                            btn = new Boton(ob.getNombre() + "_usar", ob.getNombreGrafico(), linea_x, linea_y, (int) Math.pow(2, 5), 2, ob.getIdObjeto());
//                            btn.suspend();
//                            this.botones_objetos_usar.put(ob.getIdObjeto(), btn);
//
//                        }
//                        linea_x += 37;
//                    }
//                    if (cont % 2 != 0) {//el numero es par se dibuja lo mas a la izquierda posible
//                        linea_x = eng.viewWidth() - 80;
//                        linea_y += 42;
//                    }
//
//                    if (linea_y > 42 * 6) {
//                        linea_y = 30;
//                    }
//                    cont++;
//                }

//                break;
            case 4/*"estadistica"*/:
                eng.setFont(new JGFont("Arial", 1, 14));
                eng.setColor(JGColor.yellow);
                eng.drawString("Estad aisticas", eng.viewWidth() - 75, 10, -1);
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
        /*
         * Inventario siempre dibujado
         */
//                menuActual = 3;
        int cont = 0;
        Iterator it3 = pj.getInventario().getObjetos().entrySet().iterator();
        linea_y = 400;
        linea_x = 110;
        while (it3.hasNext()) {
            Map.Entry e = (Map.Entry) it3.next();
            //Si tiene almenos una cantidad de objetos lo dibujo
            if (pj.getInventario().tieneItem(Short.parseShort(e.getKey().toString()))) {
                Objeto ob = pj.getInventario().getObjetos().get(Short.parseShort(e.getKey().toString())).getObjeto();

                if (this.botones_objetos_ver.containsKey(ob.getIdObjeto())) {

//                                this.botones_objetos_ver.get(ob.getIdObjeto()).resume();
                    this.botones_objetos_ver.get(ob.getIdObjeto()).setyAnt(linea_y + 32);
                    this.botones_objetos_ver.get(ob.getIdObjeto()).setxAnt(linea_x + 8);
//                                this.botones_objetos_ver.get(ob.getIdObjeto()).pintar();
//                                if ((eng.getKey(66)) || (eng.getKey(98))) {
//                                    this.botones_objetos_abandonar.get(ob.getIdObjeto()).resume();
                    this.botones_objetos_abandonar.get(ob.getIdObjeto()).setyAnt(linea_y + 32);
                    this.botones_objetos_abandonar.get(ob.getIdObjeto()).setxAnt(linea_x + 16);
//                                    this.botones_objetos_abandonar.get(ob.getIdObjeto()).pintar();
//                                }
                    //                            this.botones_objetos_usar.get(ob.getIdObjeto()).resume();
                    //                            this.botones_objetos_usar.get(ob.getIdObjeto()).setyAnt(linea_y);
                    //                            this.botones_objetos_usar.get(ob.getIdObjeto()).setxAnt(linea_x);
                    //                            this.botones_objetos_usar.get(ob.getIdObjeto()).pintar();
                } else {
                    Boton btn = new Boton(ob.getNombre() + "_ver", "ver", linea_x + 8, linea_y + 32, (int) Math.pow(2, 5), 3, ob.getIdObjeto());
                    btn.suspend();
                    this.botones_objetos_ver.put(ob.getIdObjeto(), btn);

                    btn = new Boton(ob.getNombre() + "_abandonar", "abandonar", linea_x + 16, linea_y + 32, (int) Math.pow(2, 5), 4, ob.getIdObjeto());
                    btn.suspend();
                    this.botones_objetos_abandonar.put(ob.getIdObjeto(), btn);
                    //
                    //                            btn = new Boton(ob.getNombre() + "_usar", ob.getNombreGrafico(), linea_x, linea_y, (int) Math.pow(2, 5), 2, ob.getIdObjeto());
                    //                            btn.suspend();
                    //                            this.botones_objetos_usar.put(ob.getIdObjeto(), btn);

                }


                if ((ob.getTipo() != filtrar)) {
                    this.botones_objetos_ver.get(ob.getIdObjeto()).suspend();
                    this.botones_objetos_abandonar.get(ob.getIdObjeto()).suspend();


                } else {

                    if ((eng.getKey(66)) || (eng.getKey(98))) {
                        this.botones_objetos_abandonar.get(ob.getIdObjeto()).resume();
                        this.botones_objetos_abandonar.get(ob.getIdObjeto()).pintar();
                    }
                    this.botones_objetos_ver.get(ob.getIdObjeto()).resume();
                    this.botones_objetos_ver.get(ob.getIdObjeto()).pintar();

                    linea_x += 37;
                }

            } else {
                this.botones_objetos_abandonar.get(((Inventario.Item) e.getValue()).getIdObjeto()).suspend();
                this.botones_objetos_ver.get(((Inventario.Item) e.getValue()).getIdObjeto()).suspend();

            }
//                    if (cont % 2 != 0) {//el numero es par se dibuja lo mas a la izquierda posible
//                        linea_x = eng.viewWidth() - 80;
//                        linea_y += 42;
//                    }

//                    if (linea_y > 42 * 6) {
//                        linea_y = 30;
//                    }

            cont++;
        }
    }

    private void suspenderBotones(int i) {
        HashMap<Short, Boton> boton = new HashMap<Short, Boton>(), boton2 = new HashMap<Short, Boton>();
        if (i == 4) {
            boton = this.botones_estadistica_aumentar;
            boton2 = this.botones_estadistica_ver;
        } else if (i == 1) {
            boton = this.botones_habilidad_aumentar;
            boton2 = this.botones_habilidad_ver;
        } else if (i == 2) {
            boton = this.botones_mision_ver;
            boton2 = this.botones_mision_abandonar;
        }
//        } else if (i == 3) {
//            boton = this.botones_objetos_abandonar;
//            boton2 = this.botones_objetos_ver;
//            Iterator it = boton.entrySet().iterator();
//            while (it.hasNext()) {
//                Map.Entry e = (Map.Entry) it.next();
////                this.botones_objetos_usar.get(Short.parseShort(e.getKey().toString())).suspend();
//            }
//        }
        Iterator it = boton.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            boton.get(Short.parseShort(e.getKey().toString())).suspend();
            boton2.get(Short.parseShort(e.getKey().toString())).suspend();
        }
    }

    public int getMenuActual() {
        return menuActual;
    }

    public void setMenuActual(int menuActual) {
        this.menuActual = menuActual;
    }

    public void paintB(Jugador pj) {
        String img = null, img1 = null;
        if ((eng.inGameState("InCombat"))) {
            eng.drawImage(0, 0, "combate", false);
            if (pj.isBloquearUso()){
                eng.drawString("Estás bloqueado, no puedes utilizar habilidades o items", eng.viewWidth()/2 - 100, eng.viewHeight() - 100, -1, false);
            }
            //dibujo los perfiles de los contrincantes
//            //personaje
//            String img1 = "personaje_combate";
//            if (pj.getImageName() != null) {
//                img1 = pj.getImageName();
//
//            }
//            eng.drawImage(10, 10, img1, false);
            //.....VARIABLES AUXILIARES PERSONAJE
            String anim_aux_pj = pj.getGraficaEstado();
            String anim_concu_aux_pj = pj.getAnim_concurrente();
            int frame_concu_aux_pj = pj.getFrames_anim_concurrente();
            //.....VARIABLES AUXILIARES ENEMIGO
            String anim_aux_mob = pj.getEnemigo().getGraficaEstado();
            String anim_concu_aux_mob = pj.getEnemigo().getAnim_concurrente();
            int frame_concu_aux_mob = pj.getEnemigo().getFrames_anim_concurrente();

            if ((pj.getEnemigo().getHp() > 0) && (pj.getHp() > 0)) {//la pelea continua
                //****************SECION ANIMACION PERSONAJE******************//
                if ((anim_aux_pj != null && !this.continua_anim_pj)) {
                    //No hay ninguno corriendo
                    anim_pj = anim_aux_pj;
                    frames_pj = frame_concu_aux_pj;
                    this.continua_anim_pj = true;
                    this.anim_pj_flag = anim_concu_aux_pj;
                    this.frame_pj = 0;
                    this.tick_actual_pj = 0;
                    if (this.frames_pj < 10) {
                        //Calculo cuantos ticks deben ocurrir para pasar al siguiente cuadro
                        tick_go_pj = (eng.getFrameRate() / frames_pj) - 2;
                    } else {
                        tick_go_pj = 6;
                    }

                } else if (continua_anim_pj && anim_pj_flag == "Parado" && anim_concu_aux_pj != "Parado") {
                    //cualquier imagen tiene prioridad sobre el flag "Parado"
                    anim_pj = anim_aux_pj;
                    frames_pj = frame_concu_aux_pj;
                    this.continua_anim_pj = true;
                    this.anim_pj_flag = anim_concu_aux_pj;
                    this.frame_pj = 0;
                    this.tick_actual_pj = 0;
                    if (this.frames_pj < 10) {
                        //Calculo cuantos ticks deben ocurrir para pasar al siguiente cuadro
                        tick_go_pj = (eng.getFrameRate() / frames_pj) - 2;
                    } else {
                        tick_go_pj = 6;
                    }

                } else if (continua_anim_pj && anim_pj_flag != "Parado" && anim_concu_aux_pj != "Parado") {
                } else if (continua_anim_pj && anim_pj_flag != "Parado" && anim_concu_aux_pj == "Parado") {
                } else if (continua_anim_pj && anim_pj_flag == "Parado" && anim_aux_pj == "Parado") {
                    anim_pj = anim_aux_pj;
                    frames_pj = frame_concu_aux_pj;
                    this.continua_anim_pj = true;
                    this.anim_pj_flag = anim_concu_aux_pj;
                    this.frame_pj = 0;
                    this.tick_actual_pj = 0;
                    if (this.frames_pj < 10) {
                        //Calculo cuantos ticks deben ocurrir para pasar al siguiente cuadro
                        tick_go_pj = (eng.getFrameRate() / frames_pj) - 2;
                    } else {
                        tick_go_pj = 6;
                    }
                    if (this.frame_pj > this.frames_pj - 2) {
                        this.frame_pj = 0;
                    }
                }
                //****************SECION ANIMACION MOB************************//
                if ((anim_aux_mob != null && !this.continua_anim_mob)) {
                    //No hay ninguno corriendo
                    anim_mob = anim_aux_mob;
                    frames_mob = frame_concu_aux_mob;
                    this.continua_anim_mob = true;
                    this.anim_mob_flag = anim_concu_aux_mob;
                    this.frame_mob = 0;
                    this.tick_actual_mob = 0;
                    if (this.frames_mob < 10) {
                        //Calculo cuantos ticks deben ocurrir para pasar al siguiente cuadro
                        tick_go_mob = (int) ((eng.getFrameRate() / frames_mob) - 2);
                    } else {
                        tick_go_mob = 6;
                    }

                } else if (continua_anim_mob && anim_mob_flag == "Parado" && anim_concu_aux_mob != "Parado") {
                    //cualquier imagen tiene prioridad sobre el flag "Parado"
                    anim_mob = anim_aux_mob;
                    frames_mob = frame_concu_aux_mob;
                    this.continua_anim_mob = true;
                    this.anim_mob_flag = anim_concu_aux_mob;
                    this.frame_mob = 0;
                    this.tick_actual_mob = 0;
                    if (this.frames_mob < 10) {
                        //Calculo cuantos ticks deben ocurrir para pasar al siguiente cuadro
                        tick_go_mob = (int) ((eng.getFrameRate() / frames_mob) - 2);
                    } else {
                        tick_go_mob = 6;
                    }

                } else if (continua_anim_mob && anim_mob_flag != "Parado" && anim_concu_aux_mob != "Parado") {
                } else if (continua_anim_mob && anim_mob_flag != "Parado" && anim_concu_aux_mob == "Parado") {
                } else if (continua_anim_mob && anim_mob_flag == "Parado" && anim_aux_mob == "Parado") {
                    anim_mob = anim_aux_mob;
                    frames_mob = frame_concu_aux_mob;
                    this.continua_anim_mob = true;
                    this.anim_mob_flag = anim_concu_aux_mob;
                    this.frame_mob = 0;
                    this.tick_actual_mob = 0;
                    if (this.frames_mob < 10) {
                        //Calculo cuantos ticks deben ocurrir para pasar al siguiente cuadro
                        tick_go_mob = (int) ((eng.getFrameRate() / frames_mob) - 2);
                    } else {
                        tick_go_mob = 6;
                    }
                    if (this.frame_mob > this.frames_mob - 2) {
                        this.frame_mob = 0;
                    }
                }
            } else {//la pelea acabó-...buscar quien murió
                if (pj.getHp() <= 0) {
                    //Aca setear animacion dying del pj y Standing del mob
                } else if (pj.getEnemigo().getHp() <= 0 && !dibujandoAnimacionMuerte) {//Mob Murió, muestro animación
                    anim_mob = pj.getEnemigo().anim_muerte;
                    frames_mob = pj.getEnemigo().frames_anim_muerte;
                    this.continua_anim_mob = true;
                    this.anim_mob_flag = "Muerte";
                    this.frame_mob = 0;
                    this.tick_actual_mob = 0;
                    if (this.frames_mob < 10) {
                        //Calculo cuantos ticks deben ocurrir para pasar al siguiente cuadro
                        tick_go_mob = (int) ((eng.getFrameRate() / frames_mob) - 2);
                    } else {
                        tick_go_mob = 6;
                    }
                    dibujandoAnimacionMuerte = true;
                    //Falta setear animacion del pj (Standing)
                }

            }
            //******************TRATO ANIMACION JUGADOR***********************//
            if (this.frame_pj < this.frames_pj) {
                //no ha terminado de reproducir
                img1 = anim_pj + frame_pj;
                if (tick_actual_pj == tick_go_pj) {
                    //si el tick se cumplio avanzo el cuadro
                    frame_pj++;
                    tick_actual_pj = 0;
                } else {
                    tick_actual_pj++;
                }

            } else {//termino de reproducir
                if (anim_pj_flag == "Muerte") {
                    //El mob está Muerto
                    this.termineAnimacionMuerte = true;
                } else {
                    this.continua_anim_pj = false;
                    img1 = pj.anim_parado + "0";
                }
            }
            if (termineAnimacionMuerte){
                img1 = "cuadro";
            }
            eng.drawImage(100 -(int) (eng.getImage(img1).getSize().x/2) , 200 -(int)(eng.getImage(img1).getSize().y/2), img1, false);
            
            //******************TRATO ANIMACION MOB***************************//
            if (this.frame_mob < this.frames_mob) {
                //no ha terminado de reproducir
                img = anim_mob + frame_mob;
                if (tick_actual_mob == tick_go_mob) {
                    //si el tick se cumplio avanzo el cuadro
                    frame_mob++;
                    tick_actual_mob = 0;
                } else {
                    tick_actual_mob++;
                }

            } else {//termino de reproducir
                if (anim_mob_flag == "Muerte") {
                    //El mob está Muerto
                    this.termineAnimacionMuerte = true;
                } else {
                    this.continua_anim_mob = false;
                    img = pj.getEnemigo().anim_parado + "0";
                }
            }
            if (termineAnimacionMuerte){
                img = "cuadro";
            }
//            eng.drawImage(eng.viewWidth() / 2, 10, img, false);
            eng.drawImage(eng.viewWidth() -200 -(int) (eng.getImage(img).getSize().x /2) , 200 -(int)(eng.getImage(img).getSize().y/2), img, false);
        }
//            eng.drawString("Ancho: "+eng.viewWidth()+" Alto: "+eng.viewHeight(), eng.viewWidth()/2, eng.viewHeight()/2, 0);
        eng.drawImage(0, eng.viewHeight() - 90, "monitor", false);
        eng.setFont(new JGFont("Arial", 1, 18));
        eng.setColor(JGColor.white);
        eng.drawString("Items", 20, eng.viewHeight() - 70, -1);
        eng.drawString("Habilidades", 20, eng.viewHeight() - 40, -1);
        eng.setFont(new JGFont("Arial", 1, 14));

        eng.drawImage(eng.viewWidth() - 110, 0, "lateral", false);
        eng.drawImage(eng.viewWidth() - 90, 315, "titulo", false);
        eng.drawImage(eng.viewWidth() - 90, 5, "titulo", false);

        /*
         * Equipo
         */
        HashMap<Short,String> hmEquipo = new HashMap<Short,String>(pj.getInventario().itemEquipados(pj.getInventario().getEquipo(),(Personaje) pj));


        if(eng.inGameState("InWorld")){
        if(this.vestir){
                eng.drawImage(50, 50, "vestimenta", false);
                eng.drawImage(115, 80, hmEquipo.get((short)1), false);
                eng.drawImage(110, 150, hmEquipo.get((short)2), false);
                eng.drawImage(70, 210, hmEquipo.get((short)3), false);
                eng.drawImage(140, 210, hmEquipo.get((short)4), false);
                eng.drawImage(110, 300, hmEquipo.get((short)5), false);
                }

        new Boton("vestir", "vestir", eng.viewWidth()-105, 191, (int) Math.pow(2, 5), 2, 31/*id boton*/);
            if (this.vestir) {
                eng.drawImage( eng.viewWidth()-100, 191, "vestir_select", false);
            } else {
                eng.drawImage( eng.viewWidth()-100, 191, "vestir", false);
            }
        }
        if (!eng.inGameState("InCombat")) {
            new Boton("usable", "tab_usable", 110, 370, (int) Math.pow(2, 5), 2, 30/*id boton*/);
            new Boton("equipo", "tab_equipo", 210, 370, (int) Math.pow(2, 5), 2, 30/*id boton*/);
            new Boton("colec", "tab_colec", 310, 370, (int) Math.pow(2, 5), 2, 30/*id boton*/);


            if (filtrar == 0) {
                eng.drawImage(110, 370, "tab_usable_dest", false);
                eng.drawImage(210, 370, "tab_equipo", false);
                eng.drawImage(310, 370, "tab_colec", false);
            } else if (filtrar == 1) {
                eng.drawImage(110, 370, "tab_usable", false);
                eng.drawImage(210, 370, "tab_equipo_dest", false);
                eng.drawImage(310, 370, "tab_colec", false);
            } else if (filtrar == 2) {
                eng.drawImage(110, 370, "tab_usable", false);
                eng.drawImage(210, 370, "tab_equipo", false);
                eng.drawImage(310, 370, "tab_colec_dest", false);
            }
        } else {
            eng.removeObjects("equipo", (int) Math.pow(2, 5));
            eng.removeObjects("colec", (int) Math.pow(2, 5));
            eng.drawImage(110, 370, "tab_usable", false);
        }

        setSeccion(new JGPoint(110, 435), new JGPoint(12, 1));
        generaSeccion(2);
        setSeccion(new JGPoint(110, 400), new JGPoint(12, 1));
        generaSeccion(1);
//        if (eng.inGameState("InCombat")) {
//            setSeccion(new JGPoint(110, 330), new JGPoint(12, 1));
//            generaSeccion(1);
//
//        }
        if (eng.inGameState("InCommerce")) {

//            eng.drawImage(20, 20, "trade", false);
            eng.drawImage(150, 200, "trade", false);
            eng.drawImage(230, 320, "cerrar", false);
//            setSeccion(new JGPoint(25, 25), new JGPoint(2, 4));
//            generaSeccion(1);
            setSeccion(new JGPoint(170, 220), new JGPoint(3, 4));
            generaSeccion(0);
        }
//        if ((eng.inGameState("InWorld")) && ((eng.getKey(73)) || (eng.getKey(105)))) {

//
//            setSeccion(new JGPoint(eng.viewWidth() - 80, 40), new JGPoint(2, 10));
//            generaSeccion(1);
//        }
        if (eng.inGameState("InReward")) {
            setSeccion(new JGPoint(200, 200), new JGPoint(4, 4));
            generaSeccion(0);
        }
    }

    public void restablecerDinamicaCombate() {
        this.dibujandoAnimacionMuerte = false;
        this.anim_mob = null;
        this.anim_mob_flag = "null";
        this.continua_anim_mob = false;
        this.termineAnimacionMuerte = false;
        
    }

    public boolean isTermineAnimacionMuerte() {
        return termineAnimacionMuerte;
    }

    public void setTermineAnimacionMuerte(boolean termineAnimacionMuerte) {
        this.termineAnimacionMuerte = termineAnimacionMuerte;
    }

    public void recibeHm(HashMap<Integer, Icono> hm, int tipo, int filtro) {
//        System.out.println("HM vacio: "+hm.isEmpty());
//        System.out.println("HM vacio: "+this.hm.isEmpty());
        if (tipo == 1) {
            setHm(hm);
        }
        if (tipo == 2) {
            setHmPjHabilidades(hm);
        }
        if (tipo == 0) {
            setHmNpc(hm);
        }
        this.filtrar = filtro;
//        System.out.println("HM vacio: "+this.hm.isEmpty());
    }

    public void vestir(boolean vestir){
        this.vestir=vestir;
    }

    /**
     * Pinta iconos
     * @param tipo Indica que HashMap debe cargarse 0 = NPC, 1 = Personaje
     */
    public void generaSeccion(int tipo) {

        switch (tipo) {

            case 1:
//                System.out.println("HM vacio: " + this.hmPjItem.isEmpty());
                if (!hmPjItem.isEmpty()) {
                    Iterator iter = hmPjItem.entrySet().iterator();
                    boolean fin = false;

                    while ((this.tabla.y > 0) && (!fin)) {
                        while ((this.tabla.x > 0) && (!fin)) {
                            if (iter.hasNext()) {
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
            case 2:
//                System.out.println("HM vacio: " + this.hmPjItem.isEmpty());
                if (!hmPjHabilidades.isEmpty()) {

                    Iterator iter = hmPjHabilidades.entrySet().iterator();
                    boolean fin = false;

                    while ((this.tabla.y > 0) && (!fin)) {
                        while ((this.tabla.x > 0) && (!fin)) {
                            if (iter.hasNext()) {
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
//                System.out.println("HM vacio: " + this.hmNpc.isEmpty());
                if (!hmNpc.isEmpty()) {

                    Iterator iter = hmNpc.entrySet().iterator();
                    boolean fin = false;

                    while ((this.tabla.y > 0) && (!fin)) {
                        while ((this.tabla.x > 0) && (!fin)) {
                            if (iter.hasNext()) {
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

    public void recibeScore(StdScoring stdScorePj, StdScoring stdScoreNpc) {
        stdScorePj = null;
        stdScoreNpc = null;
        setStdScorePj(stdScorePj);
        setStdScoreNpc(stdScoreNpc);
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
