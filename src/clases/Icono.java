/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import jgame.JGColor;
import jgame.JGFont;
import jgame.JGObject;

/**
 *
 * @author gerald
 */
public class Icono extends JGObject {

    private short idObjeto;
    private short tipo;
    private double xAnt;
    private double yAnt;
    private short pertenece;
    private int cantidad = 0;
    private String nombreLogico;
    private Objeto item = new Objeto();
    private Habilidad habilidad = new Habilidad();

    public Habilidad getHabilidad() {
        return habilidad;
    }

    public void setHabilidad(Habilidad habilidad) {
        this.habilidad = habilidad;
    }

    

    public Objeto getItem() {
        return item;
    }

    public void setItem(Objeto item) {
        this.item = item;
    }

    
    public String getNombreLogico() {
        return nombreLogico;
    }

    public void setNombreLogico(String nombreLogico) {
        this.nombreLogico = nombreLogico;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public short getIdObjeto() {
        return idObjeto;
    }

    public void setIdObjeto(short idObjeto) {
        this.idObjeto = idObjeto;
    }

    public short getTipo() {
        return tipo;
    }

    public void setTipo(short tipo) {
        this.tipo = tipo;
    }

    public Icono(String nombre, double x, double y, String graf, short id, short tipoIcono, int cantidad, short pertenece, String nombreLogico, Habilidad habilidad) {
        //tipo icono :
        // 0 = habilidades (habilidad)
        // 1 = inventario (objeto)
        super(nombre, true, x, y, (int) Math.pow(2, 4), graf);
        this.setIdObjeto(id);
        this.setTipo(tipoIcono);
        xAnt = x;
        yAnt = y;
        this.pertenece = pertenece;
        this.cantidad = cantidad;
        this.nombreLogico = nombreLogico;
        this.habilidad=habilidad;
    }

    public Icono(String nombre, double x, double y, String graf, short id, short tipoIcono, int cantidad, short pertenece, String nombreLogico, Objeto item) {
        //tipo icono :
        // 0 = habilidades (habilidad)
        // 1 = inventario (objeto)
        super(nombre, true, x, y, (int) Math.pow(2, 4), graf);
        this.setIdObjeto(id);
        this.setTipo(tipoIcono);
        xAnt = x;
        yAnt = y;
        this.pertenece = pertenece;
        this.cantidad = cantidad;
        this.nombreLogico = nombreLogico;
        this.item=item;
    }

    /**
     * Constructor de iconos para Items
     * @param nombre : Nombre JGObject
     * @param x : Posición x
     * @param y : Posición y
     * @param graf : Nombre del archivo grafico descrito en tbl
     * @param cantidad : Cantidad de ese item
     * @param pertenece : Referencia para el caso comerciar donde se muestra los item del Jugador y Npc
     * @param item : Objeto que representa al item respectivo con todos sus datos.
     */
    public Icono(String nombre, double x, double y, String graf, int cantidad, short pertenece, Objeto item) {
        //tipo icono :
        // 0 = habilidades (habilidad)
        // 1 = inventario (objeto)
        super(nombre, true, x, y, (int) Math.pow(2, 4), graf);
        this.item = item;
        this.tipo= 0;
        this.cantidad = cantidad;

        xAnt = x;
        yAnt = y;
    }


    /**
     * Contructor para habilidades
     * @param nombre
     * @param x
     * @param y
     * @param graf
     * @param cantidad
     * @param pertenece
     * @param habilidad
     */
    public Icono(String nombre, double x, double y, String graf, int cantidad, short pertenece, Habilidad habilidad) {
        //tipo icono :
        // 0 = habilidades (habilidad)
        // 1 = inventario (objeto)
        super(nombre, true, x, y, (int) Math.pow(2, 4), graf);
        this.habilidad = habilidad;
        this.tipo= 1;
        this.cantidad = cantidad;
        xAnt = x;
        yAnt = y;
    }


    public boolean belongTo(short tipo) {
        if (this.pertenece == tipo) {
            return true;
        }
        return false;
    }

//    @Override
//    public void paint() {
//        x = xAnt + eng.viewXOfs();
//        y = yAnt + eng.viewYOfs();
//        eng.setFont(new JGFont("Arial", 0, 24));
//        eng.setColor(JGColor.blue);
//        eng.drawString("" + this.cantidad, xAnt+10, yAnt+10, 0);
//
//    }
    public void paintB() {
        x = xAnt + eng.viewXOfs();
        y = yAnt + eng.viewYOfs();

        eng.setFont(new JGFont("Arial", 0, 24));
        eng.setTextOutline(1, JGColor.black);
        eng.setColor(JGColor.white);
        eng.drawString("" + this.cantidad, xAnt+10, yAnt+10, 0);

    }
   public void paintEquipo() {
        x = xAnt + eng.viewXOfs();
        y = yAnt + eng.viewYOfs();

    }
}
