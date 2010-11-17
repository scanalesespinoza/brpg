/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jgame.JGColor;
import jgame.JGFont;
import jgame.JGObject;



/**
 *
 * @author GetWay
 */
public class menuJuego extends JGObject {

    private Jugador pj;
    private boolean teclaEscape=false;
    private boolean teclaEnter=false;

    public menuJuego(String string, boolean bln, double d, double d1, int i, String string1, Jugador pj) {
        super(string, bln, d, d1, i, string1);
        this.pj=pj;

    }
    




    public void menuActual(int menu) {
            //Fondo blanco del menu
//            eng.setColor(JGColor.white);
//            eng.drawRect(eng.viewXOfs()+550,eng.viewYOfs(),100,eng.viewHeight(), true, false);
//            eng.setColor(JGColor.black);
//            eng.drawRect(eng.viewXOfs(),eng.viewYOfs()+400,eng.viewWidth()-90,100, true, false);
//            eng.setColor(JGColor.white);
//            eng.drawRect(eng.viewXOfs()+5,eng.viewYOfs()+405,eng.viewWidth()-100,70, true, false);
            //Cuadros de menu
            eng.setColor(JGColor.black);
            eng.drawRect(eng.viewXOfs()+550,eng.viewYOfs(),100,300, true, false);
            eng.setColor(JGColor.white);
            eng.drawRect(eng.viewXOfs()+555,eng.viewYOfs()+5,80,290, true, false);

            eng.setColor(JGColor.black);
            eng.drawRect(eng.viewXOfs()+550,eng.viewYOfs()+300,100,180, true, false);
            eng.setColor(JGColor.white);
            eng.drawRect(eng.viewXOfs()+555,eng.viewYOfs()+305,80,170, true, false);




            eng.setColor(JGColor.black);
            eng.setFont(new JGFont("Arial",0,20));

            //eng.drawString("Coordenada X: "+pj.x+" Coordenada Y: "+pj.y, eng.viewWidth()/2, 420, 0);
////            eng.drawString("Bienvenido al Mundo de BRPG(Demo)", eng.viewWidth()/2, 420, 0);
////            eng.setFont(new JGFont("Arial",0,10));
////            eng.drawString("Presiona Escape para salir del Juego ", eng.viewWidth()/2, 450, 0);


            switch(menu){
                case 0/*"main"*/:
                    eng.setFont(new JGFont("Arial",0,14));//fuente titulo
                    eng.drawString("General", eng.viewWidth()-45, 10, 0);
                    eng.setFont(new JGFont("Arial",0,10));//fuente parrafo
                    eng.drawString("Nombre: "+pj.getNombre().toString(), eng.viewWidth()-45, 30, 0);
                    eng.drawString("Nivel: "+pj.getNivel(), eng.viewWidth()-45, 40, 0);

                    break;
                case 1/*"habilidad"*/:
                    eng.setFont(new JGFont("Arial",0,14));//fuente titulo
                    eng.drawString("Habilidades", eng.viewWidth()-45, 10, 0);
                    eng.setFont(new JGFont("Arial",0,10));//fuente parrafo
                    eng.drawString("Ataque Basico"+" (+)", eng.viewWidth()-45, 30, 0);
                    eng.drawString("Curaciones"+" (+)", eng.viewWidth()-45, 40, 0);
                    eng.drawString("Reparar"+" (+)", eng.viewWidth()-45, 50, 0);
                    eng.drawString("Abrir cerraduras"+" (+)", eng.viewWidth()-45, 60, 0);
                    eng.drawString("Vista aguda"+" (+)", eng.viewWidth()-45, 70, 0);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("Adherir puntos", eng.viewWidth()-45, 100, 0);
                    eng.drawString("(+)", eng.viewWidth()-45, 120, 0);
                    break;
                case 2/*"mision"*/:
                    eng.setFont(new JGFont("Arial",0,14));//fuente titulo
                    eng.drawString("Misiones", eng.viewWidth()-45, 10, 0);
                    eng.setFont(new JGFont("Arial",0,10));//fuente parrafo
                    eng.drawString("La aventura       ", eng.viewWidth()-45, 30, 0);
                    eng.drawString("Despejar la plaza ", eng.viewWidth()-45, 40, 0);
                    eng.drawString("La torre          ", eng.viewWidth()-45, 50, 0);
                    eng.drawString("El Alcalde        ", eng.viewWidth()-45, 60, 0);
                    eng.drawString("Las provisiones   ", eng.viewWidth()-45, 70, 0);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("[-Descripción:    ]", eng.viewWidth()-45, 100, 0);
                    eng.drawString("[-Eliminar mision:]", eng.viewWidth()-45, 120, 0);
                    break;
                case 3/*"inventario"*/:
                    /*Inventario[] inventario=pj.getInv();
                    String equipado  = "";
                    eng.setFont(new JGFont("Arial",0,14));
                    eng.drawString("Inventario", eng.viewWidth()-45, 10, 0);
                    eng.setFont(new JGFont("Arial",0,10));
                    int cont=1;
                    for(int i=0;i<inventario.length;i++){
                        cont+=i;
                        if(inventario[i].getEstaEquipado()==0){equipado="No";}else{equipado="Si";}
                        eng.drawString("-N°: "+cont, eng.viewWidth()-45, 30+i*50, 0);
                        eng.drawString("-Nombre: "+inventario[i].getNombre(), eng.viewWidth()-45, 40+i*50, 0);
                        eng.drawString("-Equipado: "+equipado, eng.viewWidth()-45, 50+i*50, 0);
                        eng.drawString("-Cantidad: "+inventario[i].getCantidad(), eng.viewWidth()-45, 60+i*50, 0);
                        eng.drawString("--------------------", eng.viewWidth()-45, 70+i*50, 0);
                    }
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("[-Descripción:    ]", eng.viewWidth()-45, 270, 0);
                    eng.drawString("[-Eliminar objeto:]", eng.viewWidth()-45, 280, 0);*/
                    break;
                case 4/*"estadistica"*/:
                    eng.setFont(new JGFont("Arial",0,14));
                    eng.drawString("Estadisticas", eng.viewWidth()-45, 10, 0);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("Fuerza:    "+pj.getFuerza()+" (+)", eng.viewWidth()-45, 30, 0);
                    eng.drawString("Destreza:  "+pj.getDestreza()+" (+)", eng.viewWidth()-45, 40, 0);
                    eng.drawString("Sabiduria: "+pj.getSabiduria()+" (+)", eng.viewWidth()-45, 50, 0);
                    eng.drawString("Vitalidad: "+pj.getVitalidad()+" (+)", eng.viewWidth()-45, 60, 0);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("Adherir puntos", eng.viewWidth()-45, 100, 0);
                    eng.drawString("(+)", eng.viewWidth()-45, 120, 0);
                    break;
                case 5/*"Opciones"*/:
                    eng.setFont(new JGFont("Arial",0,14));
                    eng.drawString("Opciones", eng.viewWidth()-45, 10, 0);
                    eng.setFont(new JGFont("Arial",0,10));
                    eng.drawString("Teclas", eng.viewWidth()-45, 30, 0);
                    eng.drawString("Musica", eng.viewWidth()-45, 40, 0);
                    break;


            }
            eng.setFont(new JGFont("Arial",0,14));
            eng.drawString("Menu[Tecla]", eng.viewWidth()-45, 320, 0);
            eng.setFont(new JGFont("Arial",0,10));
            eng.drawString("General     ", eng.viewWidth()-45, 340, 0);
            eng.drawString("Habilidades [H]", eng.viewWidth()-45, 350, 0);
            eng.drawString("Misiones     [M]", eng.viewWidth()-45, 360, 0);
            eng.drawString("Inventario    [ I ]  ", eng.viewWidth()-45, 370, 0);
            eng.drawString("Estadisticas[E]", eng.viewWidth()-45, 380, 0);
            eng.drawString("Opciones    [O]", eng.viewWidth()-45, 390, 0);
            eng.setFont(new JGFont("Arial",0,10));
            eng.drawString("Salir del Juego", eng.viewWidth()-45, 430, 0);
            eng.drawString("[Escape]", eng.viewWidth()-45, 450, 0);



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

}
