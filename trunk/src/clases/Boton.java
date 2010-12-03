/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import jgame.JGObject;

public class Boton extends JGObject {
    /**
     * Tipo de boton
     * 0 = cerrar
     * 1 = sumar
     * 2 = usar
     * 3 = ver
     * 4 = abandonar
     * 5 = siguiente dialogo
     */
    private int tipo_boton;
    private boolean apretado;
    private double xAnt;
    private double yAnt;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boton(String nombre, String graf, double x, double y, int cid, int tipo_boton, int id) {
        super(nombre, false, x, y, (int) Math.pow(2, 5), graf);
        this.tipo_boton = tipo_boton;
        xAnt = x;
        yAnt = y;
        this.id = id;
        this.resume_in_view = false;
    }

    @Override
    public void hit(JGObject obj) {
       
        if ((obj.colid == (int) Math.pow(2, 4)) && (!eng.getMouseButton(3)) && (obj.getGraphic().equals("grilla npc"))) {
            System.out.println("Has vendido item");

        }
        if ((obj.colid == (int) Math.pow(2, 4)) && (!eng.getMouseButton(3)) && (obj.getGraphic().equals("grilla pj"))) {
            System.out.println("Has comprado item");

        }
    }

    public boolean isApretado() {
        return apretado;
    }

    public void setApretado(boolean apretado) {
        this.apretado = apretado;
    }

    @Override
    public void move(){
        x = xAnt + eng.viewXOfs();
        y = yAnt + eng.viewYOfs();
        
    }

    public void pintar(){
        eng.drawImage(xAnt, yAnt, this.getGraphic(),false);
        

    }

    public int getTipo_boton() {
        return tipo_boton;
    }

    public void setTipo_boton(int tipo_boton) {
        this.tipo_boton = tipo_boton;
    }

    public void setxAnt(double xAnt) {
        this.xAnt = xAnt;
    }

    public void setyAnt(double yAnt) {
        this.yAnt = yAnt;
    }


}
