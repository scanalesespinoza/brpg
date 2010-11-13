package clases;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private short idProximoAtaque, idProximoItem;
    private Integer hp, mp, defensa, ataque, hpMax, mpMax;

    @Override
    public void cargarPersonaje(Short id) {
        conexion = new dbDelegate();
        System.out.println("Inicio obtiene datos Jugador");
        String StrSql = "SELECT  pjuno.id id, pjuno.nombre nombre, pjuno.nivel nivel, "
                + " pjuno.posicionX posX, pjuno.posicionY posY,pjuno.tipo tipo, pjdos.vitalidad vit,"
                + " pjdos.destreza des, pjdos.sabiduria sab, pjdos.fuerza fue,"
                + " pjdos.totalPuntosHabilidad ptosHab, pjdos.totalPuntosEstadistica ptosEst,"
                + " pjdos.limiteSuperiorExperiencia limExp, pjdos.experiencia experiencia,"
                + " pjdos.pesoSoportado peso, pjdos.fechaCreacion, pjdos.estaBaneado ban,"
                + " pjdos.Cuenta_id cuenta FROM personaje pjuno, jugador pjdos "
                + "WHERE pjuno.id=" + id
                + "  AND pjdos.Personaje_id=" + id;

        try {

            ResultSet res = conexion.Consulta(StrSql);

            if (res.next()) {
                this.setIdPersonaje(res.getShort("id"));
                this.setNombre(res.getString("nombre"));
                this.setNivel(res.getShort("nivel"));
                this.setPos(res.getInt("posx"), res.getInt("posy"));
                this.setTipo(res.getShort("tipo"));
                this.setVitalidad(res.getShort("vit"));
                this.setTotalPuntosHabilidad(res.getShort("ptosHab"));
                this.setTotalPuntosEstadistica(res.getShort("ptosEst"));
                this.setDestreza(res.getShort("des"));
                this.setFuerza(res.getShort("fue"));
                this.setSabiduria(res.getShort("sab"));
                this.setLimiteSuperiorExperiencia(res.getInt("limExp"));
                this.setExperiencia(res.getInt("experiencia"));
                this.setPesoSoportado(res.getInt("peso"));
                this.setFechaCreacion(res.getDate("fechaCreacion"));
                this.setEsBaneado(res.getBoolean("ban"));

            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->Jugador , método->cargarPersonaje() " + ex);
        }
        try {
            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            Logger.getLogger(Personaje.class.getName()).log(Level.SEVERE, null, ex);
        }
        setHp();
        setMp();
    }

    public Integer getAtaque() {
        return getFuerza() * 2 + getFuerza() / 5 + getNivel(); //+ dañoArma
    }

    public Integer getDefensa() {
        return getVitalidad() / 10; // + defensa equipo
    }

    public Integer getHp() {
        return hp;
    }

    public Integer getMp() {
        return mp;
    }

    private void setHp() {
        this.hp = (getVitalidad() / 5 * 100) + (getVitalidad() * 20) + (getNivel() * 10) + getNivel();
        this.hpMax = this.hp;
        System.out.println("hp: "+this.hp);
    }

    private void setMp() {
        this.mp = (((getSabiduria() / 7) * 50) + (getSabiduria() / 30) + getNivel() * 10 + getNivel());
        this.mpMax = this.mp;
    }

    public short getIdProximoAtaque() {
        return idProximoAtaque;
    }

    public void setIdProximoAtaque(short idProximoAtaque) {
        this.idProximoAtaque = idProximoAtaque;
    }

    public short getIdProximoItem() {
        return idProximoItem;
    }

    public void setIdProximoItem(short idProximoItem) {
        this.idProximoItem = idProximoItem;
    }

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

    /**
     * Aumenta los stats
     * del jugador
     *
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
        if (puedellevarItem(idItem, (short) 1)) {
            this.getInventario().agregarItem(idItem, (short) 1);
        }
    }

    private boolean puedellevarItem(short idItem, short cantidad) {
        Objeto objt = new Objeto();
        objt.setObjeto(idItem);
        return cantidad > 0 && objt != null && objt.getPeso() * cantidad <= this.getPesoDisponible();
    }

    /**
     * Retoma el boton de habilidad
     */
    public void setProximoAtaque(Short idHabilidad) {
        if (eng.inGameState("InCombat")) {
            if (!isBlocked()) {
                this.setIdProximoAtaque(idHabilidad);
                this.utilizarHabilidad();
            } else {
                this.setIdProximoAtaque((short) -1);
            }
        } else {
            this.setIdProximoAtaque((short) -1);
        }
    }

    public void setProximoItem(Short idItem) {
        if (eng.inGameState("InCombat")) {
            if (!isBlocked()) {
                this.setIdProximoItem(idItem);
                this.utilizarItem();
            } else {
                this.setIdProximoItem((short) -1);
            }
        }
    }

    /**
     * actualiza al jugador en cuanto a uso de poderes se refiere
     */
    public void utilizarHabilidad() {
        if (this.getIdProximoAtaque() != -1) {
            //quitar mp
            this.mp -= this.getHabilidades().getCosto(this.getIdProximoAtaque());

            //bloqueo segun tiempo de espera
            this.bloquear(this.getHabilidades().getTiempoEspera(this.getIdProximoAtaque()));
        }

    }

    public void utilizarItem() {
        if (this.getIdProximoItem() != -1) {
            //bloqueo 1 tiempo de espera
            this.bloquear(2);
        }

    }

    public void recibirDañoBeneficio(int daño) {
        if (this.hpMax >= this.getHp() + daño && this.getHp() + daño > 0) {
            this.hp += daño;
        } else if (this.hpMax <= this.getHp() + daño) {
            this.hp = this.hpMax;
        } else {
            this.hp = 0;
            muerte();
        }
    }

    public void muerte() {
        eng.setGameState("InDeath");
    }

    @Override
    public void paint() {
        if (eng.inGameState("InCombat")) {
            //borde externo
            eng.setColor(JGColor.red);
            eng.drawRect(eng.viewXOfs() + 200, eng.viewYOfs() + 250, 300, 100, true, false);
            //interior

        }else if (eng.inGameState("InDeath")){

            eng.setColor(JGColor.white);
            eng.drawRect(eng.viewXOfs() + 205, eng.viewYOfs() + 255, 290, 90, true, false);
            eng.setColor(JGColor.red);
            eng.setFont(new JGFont("Arial", 0, 16));
            eng.drawString("HP: "+this.getHp(), eng.viewWidth() / 2, eng.viewHeight() / 2 + 45, 0);
        }
    }
}
