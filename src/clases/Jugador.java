package clases;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jgame.*;

/**
 *
 * @author gerald
 */
public class Jugador extends Personaje {

    private short idJugador;
    private HashMap<Short, Integer> preCompra = new HashMap<Short, Integer>();
    private Mob enemigo;
    private boolean bloquear_uso = false;
    private boolean muriendo = false, golpeado = false, atacando = false, esperando = false;
    public int quieto = 0;
    public String anim_muerte, anim_golpeado, anim_atacar, anim_parado, anim_inworld;
    public int frames_anim_muerte, frames_anim_golpeado, frames_anim_atacar, frames_anim_parado;
    private int frames_anim_concurrente;
    private String anim_concurrente;
    private menuJuego menu;

    public Jugador(double x, double y, double speed, short idPj, String nombrePj, String graf, short nivelPj, short tipoPj, int cid, dbDelegate con) throws SQLException {
        super(x, y, speed, idPj, nombrePj, graf, nivelPj, tipoPj, cid, con);
        this.idJugador = idPj;

    }

    public Jugador(String graf, String anim_muerte, int frames_muerte, String anim_golpeado, int frames_golpeado, String anim_atacar, int frames_atacar, String anim_parado, int frames_parado, double speed, short idPersonaje, dbDelegate con) {
        super(con);
        this.anim_atacar = anim_atacar;
        this.anim_golpeado = anim_golpeado;
        this.anim_muerte = anim_muerte;
        this.anim_parado = anim_parado;
        this.frames_anim_atacar = frames_atacar;
        this.frames_anim_golpeado = frames_golpeado;
        this.frames_anim_parado = frames_parado;
        this.frames_anim_muerte = frames_muerte;
        this.anim_inworld = graf;
        setTileBBox(16, 50, 64, 16);

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
    private int dinero;
    private boolean haComprado;
    //Determina si un npc esta activo (se ha colisionado con el desencadenando un dialogo con el jugador)
    private boolean interactuarNpc = false;
    public Npc npcInterac;
    private short idProximoAtaque = -1, idProximoItem = -1;
    private Integer hp, mp, defensa, ataque, hpMax, mpMax;

    @Override
    public void cargarDatos(HashMap<Short, Objeto> objetos, HashMap<Short, Habilidad> habilidades, HashMap<Short, Mision> misiones) {
        super.cargarDatos(objetos, habilidades, misiones);

    }

    @Override
    public void cargarPersonaje(Short id) {
        String StrSql = "SELECT  pjuno.id id, pjuno.nombre nombre, pjuno.nivel nivel, "
                + " pjuno.posicionX posX, pjuno.posicionY posY,pjuno.tipo tipo, pjdos.vitalidad vit,"
                + " pjdos.destreza des, pjdos.sabiduria sab, pjdos.fuerza fue,"
                + " pjdos.totalPuntosHabilidad ptosHab, pjdos.totalPuntosEstadistica ptosEst,"
                + " pjdos.limiteSuperiorExperiencia limExp, pjdos.experiencia experiencia,"
                + " pjdos.pesoSoportado peso, pjdos.fechaCreacion, pjdos.estaBaneado ban,"
                + " pjdos.Cuenta_id cuenta, pjdos.dinero dinero FROM personaje pjuno, jugador pjdos "
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
                this.setDinero(res.getInt("dinero"));
            }
        } catch (Exception ex) {
            System.out.println("Problemas en: clase->Jugador , método->cargarPersonaje() " + ex);
        }

        setHp();
        setMp();
    }

    public Mob getEnemigo() {
        return enemigo;
    }

    public void setEnemigo(Mob enemigo) {
        this.enemigo = enemigo;
    }

    public int getDinero() {
        return dinero;
    }

    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public Integer getAtaque() {
        return (getFuerza() * 2 + getFuerza() / 5 + getNivel());
    }

    public Integer getDefensa() {
        return (getVitalidad() / 10);
    }

    public Integer getHp() {
        return hp;
    }

    public Integer getMp() {
        return mp;
    }

    private void setHp() {
        this.hp = (getVitalidad() / 5 * 100) + (getVitalidad() * 20) + (getNivel() * 100) + getNivel();
        this.hpMax = this.hp;
    }

    private void setMp() {
        this.mp = (((getSabiduria() / 7) * 50) + (getSabiduria() / 30) + getNivel() * 100 + getNivel());
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

    public Jugador(dbDelegate con) {
        super(con);

    }
    /* Aumenta la todas las  cosas inherentes al subir de nivel
     * 
     */

    public void subirNivel() {
//        eng.playAudio("evento", "subir_nivel", false);
        this.aumentarNivel();
        this.aumentarStats();
        this.setExperiencia(0);
        this.setLimiteSuperiorExperiencia((int) this.calcularLimiteExperiencia());
    }
    /*
     * aumente el nivel en una unidad
     */

    public void aumentarNivel() {
        if (!(this.getNivel() + 1 >= 100)) {
            this.setNivel((short) (this.getNivel() + 1));
        }
    }

    private double calcularLimiteExperiencia() {
        int limite = this.getLimiteSuperiorExperiencia();
        //hay que fijar en el trabajo (documento word) la formula que emplearemos.
        //aca usare que aumente el 17%
        return limite * 1.17;
    }

    public boolean haComprado() {
        return haComprado;
    }

    public void setHaComprado(boolean haComprado) {
        this.haComprado = haComprado;
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
        if (this.getExperiencia() + exp >= this.getLimiteSuperiorExperiencia()) {//personaje subió de nivel
            short resto = (short) ((this.getExperiencia() + exp) - (this.getLimiteSuperiorExperiencia()));
            subirNivel();
            this.aumentarExperiencia(resto);
        } else {
            this.setExperiencia(this.getExperiencia() + exp);
        }

    }

    public boolean isAtacando() {
        return atacando;
    }

    public void setAtacando(boolean atacando) {
        this.atacando = atacando;
    }

    public boolean isEsperando() {
        return esperando;
    }

    public void setEsperando(boolean esperando) {
        this.esperando = esperando;
    }

    public boolean isGolpeado() {
        return golpeado;
    }

    public void setGolpeado(boolean golpeado) {
        this.golpeado = golpeado;
    }

    public boolean isMuriendo() {
        return muriendo;
    }

    public void setMuriendo(boolean muriendo) {
        this.muriendo = muriendo;
    }

    public String getGraficaEstado() {
        if (eng.inGameState("InCombat")) {
            if (isAtacando()) {
                setAtacando(false);
                anim_concurrente = "Atacando";
                this.frames_anim_concurrente = frames_anim_atacar;
                return anim_atacar;
            } else if (isGolpeado()) {
                this.frames_anim_concurrente = frames_anim_golpeado;
                setGolpeado(false);
                anim_concurrente = "Golpeado";
                return anim_golpeado;
            } //else if (isMuriendo()) {
//                this.frames_anim_concurrente =frames_anim_muerte;
//                anim_concurrente = "Muriendo";
//                setMuriendo(false);
//                return anim_muerte;
//            }
            setAtacando(false);
            setGolpeado(false);
            setMuriendo(false);
            anim_concurrente = "Parado";
            this.frames_anim_concurrente = frames_anim_parado;
            return anim_parado;
        }
        return anim_inworld;
    }

    public String getAnim_concurrente() {
        return anim_concurrente;
    }

    public void setAnim_concurrente(String anim_concurrente) {
        this.anim_concurrente = anim_concurrente;
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

    public void usarItem(Objeto objeto) {
        if (objeto.getTipo() == 0) {
            this.getInventario().getItem(objeto.getIdObjeto()).restarCantidad((short) 1);
            this.bloquearUsarItemHabilidad(1);
        }
    }

    @Override
    public void move() {
        /*limpio todas las  teclas que han sido presionadas*/
        setbDownkey(false);
        setbLeftkey(false);
        setbRightkey(false);
        setbUpkey(false);

        if (!isBlocked()) {
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
            player_move();
        }
    }

    @Override
    public void hit(JGObject obj) {
        switch (obj.colid) {
            case 8:
                ydir = 0;
                bloquear();
                suspend();
                this.setInteractuarNpc(true);
                this.npcInterac = (Npc) obj;
                break;
            case 4:
                this.enemigo = (Mob) obj;
                break;

            default:
                break;
        }
    }

    public int getDestreza() {
        int totalDes = 0;
        totalDes = this.destreza + this.getInventario().getDestreza();
        return totalDes;
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

    public int getFuerza() {
        int totalFue = 0;
        totalFue = this.fuerza + this.getInventario().getFuerza();
        return totalFue;
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

    public int getSabiduria() {
        int totalSab = 0;
        totalSab = this.sabiduria + this.getInventario().getSabiduria();
        return totalSab;
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

    /**
     * devuelve la vitalidad del jugador mas su equipo
     * @return
     */
    public int getVitalidad() {
        int totalVit = 0;
        totalVit = this.vitalidad + this.getInventario().getVitalidad();
        return totalVit;
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

    public void agregarItem(short idItem, short cantidad, Objeto objt) {
        if (puedellevarItem(idItem, cantidad, objt)) {
            this.getInventario().agregarItem(idItem, cantidad, objt);
        }
    }

    public void agregarItem(short idItem, Objeto objt) {
        if (puedellevarItem(idItem, (short) 1, objt)) {
            this.getInventario().agregarItem(idItem, (short) 1, objt);
        }
    }

    public boolean validarDinero(int dinerorequerido) {
        boolean tiene = false;
        if (dinero >= dinerorequerido) {
            tiene = true;
        }
        return tiene;
    }

    public boolean puedellevarItem(short idItem, short cantidad, Objeto objt) {
        return cantidad > 0 && objt != null && objt.getPeso() * cantidad <= this.getPesoDisponible();
    }

    public HashMap<Short, Integer> getPreCompra() {
        return preCompra;
    }

    public void setPreCompra(HashMap<Short, Integer> preCompra) {
        this.preCompra = preCompra;
    }

    /**
     * Retoma el boton de habilidad
     */
    public void setProximoAtaque(Short idHabilidad) {
        if (eng.inGameState("InCombat")) {
            if (!isBloquearUso()) {
                this.setIdProximoAtaque(idHabilidad);
                this.utilizarHabilidad();
                setAtacando(true);
            } else {
                this.setIdProximoAtaque((short) -1);
                setAtacando(false);
            }
        } else {
            this.setIdProximoAtaque((short) -1);
            setAtacando(false);
        }
    }

    public void setProximoItem(Objeto obj) {
//        if (eng.inGameState("InCombat")) {
        if (!isBloquearUso()) {
            this.setIdProximoItem(obj.getIdObjeto());
            this.usarItem(obj);
        } else {
            this.setIdProximoItem((short) -1);
        }
//        }
    }

    /**
     * actualiza al jugador en cuanto a uso de poderes se refiere
     */
    public void utilizarHabilidad() {
        if (this.getIdProximoAtaque() != -1) {
            int costo = this.getHabilidades().getCosto(this.getIdProximoAtaque());
            if (this.mp >= costo) {
                //tiene mp suficiente para ejecutar la habilidad
                //quitar mp
                aumentarDisminuirMp(-costo);
                //bloqueo segun tiempo de espera
                this.bloquearUsarItemHabilidad(this.getHabilidades().getTiempoEspera(this.getIdProximoAtaque()));
            } else {
                this.setIdProximoAtaque((short) -1);
            }
        }
    }

    public void utilizarItem() {
        if (this.getIdProximoItem() != -1) {

            //bloqueo 1 tiempo de espera
            this.bloquear(2);
        }

    }

    public void recibirDañoBeneficio(int daño) {
        if (daño < 0) {
            setGolpeado(true);
        }
        if (this.hpMax >= this.getHp() + daño && this.getHp() + daño > 0) {
            this.hp += daño;
        } else if (this.hpMax <= this.getHp() + daño) {
            this.hp = this.hpMax;
        } else {
            this.hp = 0;
        }
    }

    public void muerte() {
        if (menu.termineAnimacionMuerte) {
            this.aumentarDisminuirDinero((int) -(this.getDinero() * ((float) (0.2))));
            eng.setGameState("InDeath");
//            eng.playAudio("ambiental", "muerte", true);
        }
    }

    public int getFrames_anim_concurrente() {
        return frames_anim_concurrente;
    }

    @Override
    public void paint() {
    }

    public int regenerarMp(int porcentaje) {
        aumentarDisminuirMp((int) (mpMax * ((float) (porcentaje / 100.0))));
        return (int) (mpMax * ((float) (porcentaje / 100.0)));
    }

    public void aumentarDisminuirMp(int cant) {
        if (this.mpMax >= this.getMp() + cant && this.getMp() + cant > 0) {
            this.mp += cant;
        } else if (this.mpMax <= this.getMp() + cant) {
            this.mp = this.mpMax;
        } else {
            this.mp = 0;
        }
    }

    public Integer getHpMax() {
        return hpMax;
    }

    public Integer getMpMax() {
        return mpMax;
    }

    public void aumentarDisminuirDinero(int dinero) {
        if (this.getDinero() + dinero < 0) {
            this.setDinero(0);
        } else {
            this.setDinero(this.getDinero() + dinero);
        }
    }

    public void aumentarFuerza(int i) {
        this.setFuerza((short) (getFuerza() + 1));
    }

    public void aumentarDestreza(int i) {
        this.setDestreza((short) (getDestreza() + 1));
    }

    void aumentarSabiduria(int i) {
        this.setSabiduria((short) (getSabiduria() + 1));
    }

    void aumentarVitalidad(int i) {
        this.setVitalidad((short) (getVitalidad() + 1));
    }

    void gastarPuntoEstadistica() {
        this.setTotalPuntosEstadistica((short) (getTotalPuntosEstadistica() - 1));
    }

    void gastarPuntosHabilidad() {
        this.setTotalPuntosHabilidad((short) (getTotalPuntosHabilidad() - 1));
    }

    private void bloquearUsarItemHabilidad(int i) {
        this.setBloquear_uso(true);
        new JGTimer((int) (eng.getFrameRate() * i), true) {

            @Override
            public void alarm() {
                desbloquear_uso();
            }
        };
    }

    private void desbloquear_uso() {
        this.setBloquear_uso(false);
    }

    /**
     * Verifica si el Jugador esta habilitado para usar items o habilidades
     * @return
     */
    public boolean isBloquearUso() {
        return this.bloquear_uso;
    }

    public void setBloquear_uso(boolean bloquear_uso) {
        this.bloquear_uso = bloquear_uso;
    }

    public void setLinkMenu(menuJuego menu) {
        this.menu = menu;
    }

    @Override
    public void salvarPersonaje() {
        super.salvarPersonaje();
        try {
            String StrSql = "UPDATE jugador "
                    + "   SET `vitalidad` = "+this.vitalidad+", `destreza` = "+this.destreza+", `sabiduria`= "+this.sabiduria+", `fuerza` = "+this.fuerza+", `totalpuntoshabilidad`= "+this.totalPuntosHabilidad+", `totalpuntosestadistica` = "+this.totalPuntosEstadistica+", `limitesuperiorexperiencia`= "+this.limiteSuperiorExperiencia+", `experiencia` = "+this.experiencia+", `pesosoportado` = "+this.pesoSoportado+", `dinero` = "+this.dinero+"  WHERE personaje_id = " + this.getIdPersonaje();
            conexion.Ejecutar(StrSql);
        } catch (Exception ex) {
            Logger.getLogger(Personaje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
