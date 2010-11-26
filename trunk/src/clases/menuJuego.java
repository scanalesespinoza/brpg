/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.HashMap;
import java.awt.Color;
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

    private boolean teclaEscape=false;
    private boolean teclaEnter=false;
    private Habilidad hab = new Habilidad();
    private Objeto obj = new Objeto();
    private Jugador pjTest;
    private HashMap<Integer, Icono> hmPj = new HashMap<Integer, Icono>();
    private HashMap<Integer, Icono> hmNpc = new HashMap<Integer, Icono>();
    private int pos_inicial_x, pos_inicial_y;
    private JGPoint recorrido;
    private int tabla_inicial_x, tabla_inicial_y;
    private JGPoint tabla;


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
        this.pj=pj;

    }
    




    public void menuActual(int menu, Jugador pj) {
            //Fondo blanco del menu
//            eng.setColor(JGColor.white);
//            eng.drawRect(eng.viewXOfs()+550,eng.viewYOfs(),100,eng.viewHeight(), true, false);
//            eng.setColor(JGColor.black);
//            eng.drawRect(eng.viewXOfs(),eng.viewYOfs()+400,eng.viewWidth()-90,100, true, false);
//            eng.setColor(JGColor.white);
//            eng.drawRect(eng.viewXOfs()+5,eng.viewYOfs()+405,eng.viewWidth()-100,70, true, false);
            //Cuadros de menu
//            eng.setColor(JGColor.black);
//            eng.drawRect(eng.viewXOfs()+550,eng.viewYOfs(),100,300, true, false);
//            eng.setColor(JGColor.white);
//            eng.drawRect(eng.viewXOfs()+555,eng.viewYOfs()+5,80,290, true, false);
//
//            eng.setColor(JGColor.black);
//            eng.drawRect(eng.viewXOfs()+550,eng.viewYOfs()+300,100,180, true, false);
//            eng.setColor(JGColor.white);
//            eng.drawRect(eng.viewXOfs()+555,eng.viewYOfs()+305,80,170, true, false);
//
//


            eng.setColor(JGColor.white);
            eng.setFont(new JGFont("Arial",2,20));

            //eng.drawString("Coordenada X: "+pj.x+" Coordenada Y: "+pj.y, eng.viewWidth()/2, 420, 0);
////            eng.drawString("Bienvenido al Mundo de BRPG(Demo)", eng.viewWidth()/2, 420, 0);
////            eng.setFont(new JGFont("Arial",0,10));
////            eng.drawString("Presiona Escape para salir del Juego ", eng.viewWidth()/2, 450, 0);


            switch(menu){
                case 0/*"main"*/:
                    eng.setFont(new JGFont("Arial",1,14));//fuente titulo
                    pjTest = (Jugador) eng.getObject("player");

                    eng.drawString("General", eng.viewWidth()-80, 10, -1);
                    eng.setFont(new JGFont("Arial",0,10));//fuente parrafo
                    eng.drawString("Nombre: "+pjTest.getNombre(), eng.viewWidth()-75, 30, -1);
                    eng.drawString("Nivel: "+pjTest.getNivel(), eng.viewWidth()-75, 40, -1);
                    eng.drawString("Experiencia :"+pjTest.getExperiencia(), eng.viewWidth()-75, 50, -1);
                    eng.drawRect(eng.viewWidth()-45 + eng.viewXOfs(), 60 + eng.viewYOfs(), (float) (pj.getExperiencia() * 100 / pj.getLimiteSuperiorExperiencia()), 10, true, false, 0, JGColor.orange );
                    eng.setColor(JGColor.white);
                    removerIconos();
                    break;
                case 1/*"habilidad"*/:
                    eng.setFont(new JGFont("Arial",1,14));//fuente titulo
                    eng.setColor(JGColor.yellow);
                    eng.drawString("Habilidades", eng.viewWidth()-75, 10, -1);
                    eng.setColor(JGColor.white);
                    eng.setFont(new JGFont("Arial",0,10));//fuente parrafo
                    eng.drawString("Ataque Basico"+" (+)", eng.viewWidth()-75, 30, -1);
                    eng.drawString("Curaciones"+" (+)", eng.viewWidth()-75, 40, -1);
                    eng.drawString("Reparar"+" (+)", eng.viewWidth()-75, 50, -1);
                    eng.drawString("Abrir cerraduras"+" (+)", eng.viewWidth()-75, 60, -1);
                    eng.drawString("Vista aguda"+" (+)", eng.viewWidth()-75, 70, -1);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("Adherir puntos", eng.viewWidth()-755, 100, -1);
                    eng.drawString("(+)", eng.viewWidth()-75, 120, -1);
                    break;
                case 2/*"mision"*/:
                    eng.setFont(new JGFont("Arial",1,14));//fuente titulo
                    eng.setColor(JGColor.yellow);
                    eng.drawString("Misiones", eng.viewWidth()-75, 10, -1);
                    eng.setColor(JGColor.white);
                    eng.setFont(new JGFont("Arial",0,10));//fuente parrafo
                    eng.drawString("La aventura       ", eng.viewWidth()-75, 30, -1);
                    eng.drawString("Despejar la plaza ", eng.viewWidth()-75, 40, -1);
                    eng.drawString("La torre          ", eng.viewWidth()-75, 50, -1);
                    eng.drawString("El Alcalde        ", eng.viewWidth()-75, 60, -1);
                    eng.drawString("Las provisiones   ", eng.viewWidth()-75, 70, -1);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("[-Descripción:    ]", eng.viewWidth()-75, 100, -1);
                    eng.drawString("[-Eliminar mision:]", eng.viewWidth()-75, 120, -1);
                    break;
//                case 3/*"inventario"*/:
//                    /*
//                     * Es llamada desde manager y pintada en menuJuego.paintB
//                     */
//
//                    break;
                case 4/*"estadistica"*/:
                    eng.setFont(new JGFont("Arial",1,14));
                    eng.setColor(JGColor.yellow);
                    eng.drawString("Estadisticas", eng.viewWidth()-75, 10, 0);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.setColor(JGColor.white);
                    eng.drawString("Fuerza:    "+pj.getFuerza()+" (+)", eng.viewWidth()-75, 30, -1);
                    eng.drawString("Destreza:  "+pj.getDestreza()+" (+)", eng.viewWidth()-75, 40, -1);
                    eng.drawString("Sabiduria: "+pj.getSabiduria()+" (+)", eng.viewWidth()-75, 50, -1);
                    eng.drawString("Vitalidad: "+pj.getVitalidad()+" (+)", eng.viewWidth()-75, 60, -1);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("Adherir puntos", eng.viewWidth()-75, 100, -1);
                    eng.drawString("(+)", eng.viewWidth()-75, 120, -1);
                    break;
                case 5/*"Opciones"*/:
                    eng.setFont(new JGFont("Arial",1,14));
                    eng.setColor(JGColor.yellow);
                    eng.drawString("Opciones", eng.viewWidth()-75, 10, -1);
                    eng.setColor(JGColor.white);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("Teclas", eng.viewWidth()-75, 30, -1);
                    eng.drawString("Musica", eng.viewWidth()-75, 40, -1);
                    break;


            }
            eng.setFont(new JGFont("Arial",1,14));
            eng.drawString("Menu[Tecla]", eng.viewWidth()-80, 320, -1);
            eng.setFont(new JGFont("Arial",0,10));
            eng.drawString("General     ", eng.viewWidth()-75, 340, -1);
            eng.drawString("Habilidades [H]", eng.viewWidth()-75, 350, -1);
            eng.drawString("Misiones     [M]", eng.viewWidth()-75, 360, -1);
            eng.drawString("Inventario    [ I ]  ", eng.viewWidth()-75, 370, -1);
            eng.drawString("Estadisticas[E]", eng.viewWidth()-75, 380, -1);
            eng.drawString("Opciones    [O]", eng.viewWidth()-75, 390, -1);
            eng.setFont(new JGFont("Arial",0,10));
            eng.drawString("Salir del Juego", eng.viewWidth()-75, 430, -1);
            eng.drawString("[Escape]", eng.viewWidth()-75, 450, -1);



    }

//    @Override
//   public void paint(){
//
//            if(eng.inGameState("InCombat"))
//                eng.drawImage(0, 0, "combate", false);
//            if(eng.inGameState("InCommerce")){
//                eng.drawImage(20, 20, "trade", false);
//                eng.drawImage(200, 20, "trade", false);
//            }
//            eng.drawString("Ancho: "+eng.viewWidth()+" Alto: "+eng.viewHeight(), eng.viewWidth()/2, eng.viewHeight()/2, 0);
//            eng.drawImage(0, eng.viewHeight()-90, "monitor", false);
//            eng.drawImage(eng.viewWidth()-90, 0, "lateral", false);
//            eng.drawImage(eng.viewWidth()-90, 320, "titulo", false);
//            eng.drawImage(eng.viewWidth()-90, 10, "titulo", false);
//
//    }

    public void paintB(){
            if(eng.inGameState("InCombat")){
                eng.drawImage(0, 0, "combate", false);
            }
            
//            eng.drawString("Ancho: "+eng.viewWidth()+" Alto: "+eng.viewHeight(), eng.viewWidth()/2, eng.viewHeight()/2, 0);
            eng.drawImage(0, eng.viewHeight()-90, "monitor", false);
            eng.drawImage(eng.viewWidth()-90, 0, "lateral", false);
            eng.drawImage(eng.viewWidth()-90, 315, "titulo", false);
            eng.drawImage(eng.viewWidth()-90, 5, "titulo", false);

            if(eng.inGameState("InCombat")){
                setSeccion(new JGPoint(110, 330), new JGPoint(12, 1));
                generaSeccion(1);

            }
            if(eng.inGameState("InCommerce")){

                eng.drawImage(20, 20, "trade", false);
                eng.drawImage(200, 20, "trade", false);
                setSeccion(new JGPoint(25, 25), new JGPoint(2, 4));
                generaSeccion(1);
                setSeccion(new JGPoint(250, 25), new JGPoint(3, 4));
                generaSeccion(0);
                
            }
            if((eng.inGameState("InWorld"))&&((eng.getKey(73))||(eng.getKey(105)))){
                eng.setFont(new JGFont("Arial",1,14));
                eng.setColor(JGColor.yellow);
                eng.drawString("Inventario", eng.viewWidth()-60, 10, 0);
                eng.setFont(new JGFont("Arial",0,10));
                eng.setColor(JGColor.white);

                setSeccion(new JGPoint(eng.viewWidth()-80, 40), new JGPoint(2, 10));
                generaSeccion( 1);
            }

            if(eng.inGameState("InReward")){
                removerIconos();
                setSeccion(new JGPoint(eng.viewWidth()/2-100, eng.viewHeight()/2-45), new JGPoint(4, 4));
                generaSeccion(0);
            }

            
    }
    public void recibeHm(HashMap<Integer, Icono> hm, int tipo){
//        System.out.println("HM vacio: "+hm.isEmpty());
//        System.out.println("HM vacio: "+this.hm.isEmpty());
        if(tipo==1)
        setHm(hm);
        if(tipo==0)
            setHmNpc(hm);
//        System.out.println("HM vacio: "+this.hm.isEmpty());
    }

    public void generaSeccion(int tipo){

        switch(tipo){

            case 1:
                System.out.println("HM vacio: "+this.hmPj.isEmpty());
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
                System.out.println("HM vacio: "+this.hmNpc.isEmpty());
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

    public void ventanaSalida(){
            eng.setColor(JGColor.red);
            eng.drawRect(eng.viewXOfs()+200,eng.viewYOfs()+250,300,100, true, false);
            eng.setColor(JGColor.white);
            eng.drawRect(eng.viewXOfs()+205,eng.viewYOfs()+255,290,90, true, false);


            eng.setColor(JGColor.red);
            eng.setFont(new JGFont("Arial",0,16));
            eng.drawString("Deseas salir del Juego?", eng.viewWidth()/2,eng.viewHeight()/2+45, 0);
            eng.setFont(new JGFont("Arial",0,20));
            eng.drawString("Confirmar [ENTER]", eng.viewWidth()/2,eng.viewHeight()/2+65, 0);

    }

    public void ventanaAlert(String mensaje){
            eng.setColor(JGColor.red);
            eng.drawRect(eng.viewXOfs()+200,eng.viewYOfs()+250,300,100, true, false);
            eng.setColor(JGColor.white);
            eng.drawRect(eng.viewXOfs()+205,eng.viewYOfs()+255,290,90, true, false);


            eng.setColor(JGColor.red);
            eng.setFont(new JGFont("Arial",0,16));
            eng.drawString(mensaje, eng.viewWidth()/2,eng.viewHeight()/2+45, 0);

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
