package clases;

import java.sql.Date;
import java.sql.SQLException;
import jgame.*;

/**
 *
 * @author gerald
 */
public class Jugador extends Personaje {

    private short idJugador;

    public Jugador(double x, double y, double speed, short idPj, String nombrePj, String graf, short nivelPj, short tipoPj, int cid) throws SQLException {
        super(x, y, speed, idPj, nombrePj, graf, nivelPj, tipoPj, cid);
        this.idJugador = idPj;
    }
    private short vitalidad;
    private short destreza;
    private short sabiduria;
    private short fuerza;
    private short totalPuntosHabilidad;
    private short totalPuntosEstadistica;
    private int limiteSuperiorExperiencia;
    private int experiencia;
    private int pesoSoportado;
    private Date fechaCreacion;
    private boolean esBaneado;
    //Determina si un npc esta activo (se ha colisionado con el desencadenando un dialogo con el jugador)
    private boolean interactuarNpc = false;
    private dbDelegate conect = new dbDelegate();
    public Npc npcInterac;

    public Jugador() {
    }
    /* Aumenta la todas las  cosas inherentes al subir de nivel
     * 
     */

    public void subirNivel() {
        this.aumentarNivel();
        this.aumentarStats();
        this.setExperiencia(0);
        this.setLimiteSuperiorExperiencia((int) this.calcularLimiteExperiencia());
    }
    /*
     * aumente el nivel en una unidad
     */

    public void aumentarNivel() {
        this.setNivel((short) (this.getNivel() + 1));
    }

    private double calcularLimiteExperiencia() {
        int limite = this.getLimiteSuperiorExperiencia();
        //hay que fijar en el trabajo (domcumento word) la formula que emplearemos.
        //aca usare que aumente el 37%

        return limite * 1.35;
    }
    /*aumenta los stats
     * del jugador
     */

    private void aumentarStats() {
        short aumento = 3;
        this.setVitalidad((short) (this.getVitalidad() + aumento));
        this.setDestreza((short) (this.getDestreza() + aumento));
        this.setFuerza((short) (this.getFuerza() + aumento));
        this.setSabiduria((short) (this.getSabiduria() + aumento));
        this.aumentarPuntos();
        this.aumentarPesoSoportado();
    }

    public void aumentarExperiencia(short exp) {
        this.setExperiencia(this.getExperiencia() + exp);
    }

    private void aumentarPuntos() {
        this.aumentaPuntosEstadistica();
        this.aumentaPuntosHabilidad();
    }

    private void aumentaPuntosHabilidad() {
        this.setTotalPuntosHabilidad((short) (this.getTotalPuntosHabilidad() + 1));
    }

    private void aumentaPuntosEstadistica() {
        this.setTotalPuntosEstadistica((short) (this.getTotalPuntosEstadistica() + 1));
    }

    private void aumentarPesoSoportado() {
        //hay que determinar en cuando sube el peso soportado
        this.setPesoSoportado(this.getPesoSoportado() + 50);
    }

    @Override
    public void move() {
        /*limpio todas las  teclas que han sido presionadas*/
        setbDownkey(false);
        setbLeftkey(false);
        setbRightkey(false);
        setbUpkey(false);

        int entorno = 16; //variable para formar cuadrados a partir de un punto.
        if (eng.getMouseButton(1)) {
            //Obtengo posicion del mouse
            mouseX = eng.getMouseX() + eng.viewXOfs();
            mouseY = eng.getMouseY() + eng.viewYOfs();

            if (interactuarNpc) {
                return;
            }

            //creo objeto JGRectangle para ver si llegó al punto deseado
            rClick = new JGRectangle((int) mouseX, (int) mouseY, entorno, entorno);

            this.estadoClick = true;
            eng.clearMouseButton(1);
        }


        if (estadoClick) {

            if (this.x < mouseX - entorno) {
                setbRightkey(true);
            }
            if (this.x > mouseX + entorno) {
                setbLeftkey(true);
            }
            if (this.y < mouseY - entorno) {
                setbDownkey(true);
            }
            if (this.y > mouseY + entorno) {
                setbUpkey(true);
            }

            if (rClick.intersects(this.getTileBBox()) || eng.getMouseButton(3) || eng.getKey(eng.KeyLeft)
                    || eng.getKey(eng.KeyDown) || eng.getKey(eng.KeyUp) || eng.getKey(eng.KeyRight)) {
                setbDownkey(false);
                setbLeftkey(false);
                setbRightkey(false);
                setbUpkey(false);
                eng.clearMouseButton(3);
                estadoClick = false;
            }

        }
        if (!estadoClick) {
            setbDownkey(false);
            setbLeftkey(false);
            setbRightkey(false);
            setbUpkey(false);
            if (eng.getKey(eng.KeyUp)) {
                setbUpkey(true);
            }   //else {eng.clearKey(eng.KeyUp);}
            if (eng.getKey(eng.KeyDown)) {
                setbDownkey(true);
            } //else {eng.clearKey(eng.KeyDown);}
            if (eng.getKey(eng.KeyLeft)) {
                setbLeftkey(true);
            } //else {eng.clearKey(eng.KeyLeft);}
            if (eng.getKey(eng.KeyRight)) {
                setbRightkey(true);
            }//else {eng.clearKey(eng.KeyRight);}

        }
        super.desplazar();
    }

    @Override
    public void hit(JGObject obj) {
        try {
            conect.actualizaPosicionJugador(this.getIdPersonaje(), (int) this.getLastX(), (int) this.getLastY() + 5);
        } catch (Exception ex) {
            System.out.println("Error al conectar la DB #Jugador.Hit: " + ex);
        }

        remove();
        this.setInteractuarNpc(true);
        System.out.println("Nombre del objeto colisionador: " + getGraphic() + getName());

        this.npcInterac = (Npc) obj;
    }

    public short getDestreza() {
        return destreza;
    }

    public void setDestreza(short destreza) {
        this.destreza = destreza;
    }

    public boolean isEsBaneado() {
        return esBaneado;
    }

    public void setEsBaneado(boolean esBaneado) {
        this.esBaneado = esBaneado;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public short getFuerza() {
        return fuerza;
    }

    public void setFuerza(short fuerza) {
        this.fuerza = fuerza;
    }

    public int getLimiteSuperiorExperiencia() {
        return limiteSuperiorExperiencia;
    }

    public void setLimiteSuperiorExperiencia(int limiteSuperiorExperiencia) {
        this.limiteSuperiorExperiencia = limiteSuperiorExperiencia;
    }

    public int getPesoSoportado() {
        return pesoSoportado;
    }

    public void setPesoSoportado(int pesoSoportado) {
        this.pesoSoportado = pesoSoportado;
    }

    public short getSabiduria() {
        return sabiduria;
    }

    public void setSabiduria(short sabiduria) {
        this.sabiduria = sabiduria;
    }

    public short getTotalPuntosEstadistica() {
        return totalPuntosEstadistica;
    }

    public void setTotalPuntosEstadistica(short totalPuntosEstadistica) {
        this.totalPuntosEstadistica = totalPuntosEstadistica;
    }

    public short getTotalPuntosHabilidad() {
        return totalPuntosHabilidad;
    }

    public void setTotalPuntosHabilidad(short totalPuntosHabilidad) {
        this.totalPuntosHabilidad = totalPuntosHabilidad;
    }

    public short getVitalidad() {
        return vitalidad;
    }

    public void setVitalidad(short vitalidad) {
        this.vitalidad = vitalidad;
    }

    public boolean isInteractuarNpc() {
        return interactuarNpc;
    }

    public void setInteractuarNpc(boolean interactuarNpc) {
        this.interactuarNpc = interactuarNpc;
    }

    public short getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(short idJugador) {
        this.idJugador = idJugador;
    }

    public int getPesoDisponible() {
        return this.getPesoSoportado() - this.getInventario().getPesoUsado();
    }

    public void agregarItem(short idItem, short cantidad) {
        Objeto objt = new Objeto();
        objt.setObjeto(idItem);
        if (puedellevarItem(idItem, cantidad)) {
            this.getInventario().agregarItem(idItem, cantidad);
        }
    }

    public void agregarItem(short idItem) {
        Objeto objt = new Objeto();
        objt.setObjeto(idItem);
        if (puedellevarItem(idItem, (short)1)) {
            this.getInventario().agregarItem(idItem, (short)1);
        }
    }

    private boolean puedellevarItem(short idItem, short cantidad ){
        Objeto objt = new Objeto();
        objt.setObjeto(idItem);
        return cantidad > 0 && objt != null && objt.getPeso() * cantidad <= this.getPesoDisponible();
    }

}
