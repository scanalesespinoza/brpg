/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;
import jgame.JGObject;
import jgame.JGPoint;

/**
 *
 * @author gerald
 */
public class Mob extends Personaje {

    public double tiempo_espera_original;
    /* tiempo de espera oeiginal para volver a moverse */
    public double tiempo_espera;
    /* tiempo de espera oeiginal para volver a moverse */
    private short idProximoAtaque;
    private Integer hp, mp, defensa, ataque;
    private short vitalidad;
    private short sabiduria;
    private short fuerza;
    private short destreza;
    private short experiencia;
    private short dinero;
    private int hpMax;
    private int mpMax;
    private boolean muriendo = false, golpeado = false, atacando = false, esperando = false;
    public int quieto = 0;
    public String anim_muerte, anim_golpeado, anim_atacar, anim_parado, anim_inworld;
    public int frames_anim_muerte, frames_anim_golpeado, frames_anim_atacar, frames_anim_parado;
    private int frames_anim_concurrente;
    private String anim_concurrente;
    public int getFrames_anim_concurrente() {
        return frames_anim_concurrente;
    }

    public Mob(dbDelegate con) {
        super(con);
    }

    public Mob(String graf, String anim_muerte, int frames_muerte, String anim_golpeado, int frames_golpeado, String anim_atacar, int frames_atacar, String anim_parado, int frames_parado, double speed, short idPersonaje, String nombre, short nivel, double posicionX, double posicionY, short tipo, JGObject home_in, boolean avoid, double random_proportion, short vitalidad, short destreza, short sabiduria, short fuerza, short experiencia, short dinero, dbDelegate con) {
        super(posicionX, posicionY, speed, idPersonaje, nombre, graf, nivel, tipo, (int) Math.pow(2, 2), con);
        this.home_in = home_in;
        this.avoid = avoid;
        this.random_proportion = random_proportion;
        this.vitalidad = vitalidad;
        this.sabiduria = sabiduria;
        this.fuerza = fuerza;
        this.destreza = destreza;
        this.experiencia = experiencia;
        this.anim_atacar = anim_atacar;
        this.anim_golpeado = anim_golpeado;
        this.anim_muerte = anim_muerte;
        this.anim_parado = anim_parado;
        this.frames_anim_atacar = frames_atacar;
        this.frames_anim_golpeado = frames_golpeado;
        this.frames_anim_parado = frames_parado;
        this.frames_anim_muerte = frames_muerte;
        this.anim_inworld = graf;
        this.dinero=dinero;
        this.setHp();
        this.setMp();
    }

    public void cargarInventario() {
    }

    @Override
    public void cargarPersonaje(Short id) {
//        conexion = new dbDelegate();
        String StrSql = "SELECT  pjuno.id id, pjuno.nombre nombre, pjuno.nivel nivel, "
                + " pjuno.posicionX posX, pjuno.posicionY posY,pjuno.tipo tipo, pjdos.vitalidad vit,"
                + " pjdos.destreza des, pjdos.sabiduria sab, pjdos.fuerza fue,"
                + " pjdos.experiencia experiencia, pjdos.dinero dinero "
                + " FROM personaje pjuno, mob pjdos "
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
                this.setDestreza(res.getShort("des"));
                this.setFuerza(res.getShort("fue"));
                this.setSabiduria(res.getShort("sab"));
                this.setExperiencia(res.getShort("experiencia"));
                this.setDinero(res.getShort("dinero"));

            }
//            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            System.out.println("Problemas en: clase->MOB , método->cargarPersonaje() " + ex);
        }


    }

    public short getDinero() {
        return dinero;
    }

    public void setDinero(short dinero) {
        this.dinero = dinero;
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

    @Override
    public void move() {
        dirGrafica(this.getIdPersonaje());
        if (occupied == null
                || (xspeed == 0 && yspeed == 0)
                || (xdir == 0 && ydir == 0 && (!isXAligned() || !isYAligned()))) {
            // make sure we're occupying space and are on the grid when we're
            // not moving
            if (occupied != null) {
                eng.andTileCid(occupied.x, occupied.y, ~occupy_mask);
            }
            snapToGrid(eng.tileWidth(), eng.tileHeight());
            occupied = getCenterTile();
            eng.orTileCid(occupied.x, occupied.y, occupy_mask);
        } else if (!isXAligned() || !isYAligned()) {
            // we're moving and not aligned -> move until tile aligned
            if (!continuous_anim) {
                startAnim();
            }
        } else { // tile aligned -> ready to change direction
            int prevxdir = xdir, prevydir = ydir;
            snapToGrid();
            JGPoint cen = getCenterTile();
            // determine direction
            setDir(0, 0);
            int newxdir = 0, newydir = 0;
//            boolean xdir_any = false, ydir_any = false; // alternate directions
            if (eng.random(0.01, 0.99) > 0.50) {
                quieto = 5;
            }
            if (quieto > 0) {
                quieto--;
            }
            if ((home_in != null) && (quieto > 0)) {
                newxdir = 0;
                newydir = 0;
            } else { // random
                newxdir = eng.random(-1, 1, 1);
                newydir = eng.random(-1, 1, 1);
            }

            if (newxdir == -1) {
                if (x + newxdir * 16 <= 32) {
                    newxdir *= -1;
                }
            } else {
                if (x + newxdir * 16 >= eng.pfWidth() - 100) {
                    newxdir *= -1;
                }
            }
            if (newydir == -1) {
                if (y + newydir * 16 <= 32) {
                    newydir *= -1;
                }
            } else {
                if (y + newydir * 16 >= eng.pfHeight() - 100) {
                    newydir *= -1;
                }
            }

            // check if we can go this way
            xdir = newxdir;
            ydir = newydir;
            checkIfBlocked(this, block_mask, prevxdir, prevydir);
            // occupy new tile, or same tile if we didn't move
            if (occupied != null) {
                eng.andTileCid(occupied.x, occupied.y, ~occupy_mask);
            }
            occupied = new JGPoint(cen.x + xdir, cen.y + ydir);
            eng.orTileCid(occupied.x, occupied.y, occupy_mask);
            if (!continuous_anim) {
                startAnim();

            }
        }
    }

    public void dirGrafica(short id) {
        /*
         * Especificacion grafica segun el mob y la direccion que seguirá
         */
        switch (id) {
            case 6:
                if (xdir < 0) {
                    setGraphic("goblin_walk_l");
                } else if (xdir > 0) {
                    setGraphic("goblin_walk_r");
                } else if (ydir > 0) {
                    setGraphic("goblin_walk_l");
                } else if (ydir < 0) {
                    setGraphic("goblin_walk_r");
                } else if (ydir == 0 && xdir == 0) {
                    if (this.getGraphic().equals("goblin_walk_l")) {
                        setGraphic("goblin_stand_l");
                    } else if (this.getGraphic().equals("goblin_walk_r")) {
                        setGraphic("goblin_stand_r");
                    }
                }
                break;
            case 7:
                if (xdir < 0) {
                    setGraphic("goblin2_walk_l");
                } else if (xdir > 0) {
                    setGraphic("goblin2_walk_r");
                } else if (ydir > 0) {
                    setGraphic("goblin2_walk_l");
                } else if (ydir < 0) {
                    setGraphic("goblin2_walk_r");
                } else if (ydir == 0 && xdir == 0) {
                    if (this.getGraphic().equals("goblin2_walk_l")) {
                        setGraphic("goblin2_stand_l");
                    } else if (this.getGraphic().equals("goblin2_walk_r")) {
                        setGraphic("goblin2_stand_r");
                    }
                }
                break;
            case 8:
                if (xdir < 0) {
                    setGraphic("tana_walk_l");
                } else if (xdir > 0) {
                    setGraphic("tana_walk_r");
                } else if (ydir > 0) {
                    setGraphic("tana_walk_l");
                } else if (ydir < 0) {
                    setGraphic("tana_walk_r");
                } else if (ydir == 0 && xdir == 0) {
                    if (this.getGraphic().equals("tana_walk_l")) {
                        setGraphic("tana_stand_l");
                    } else if (this.getGraphic().equals("tana_walk_r")) {
                        setGraphic("tana_stand_r");
                    }
                }
                break;
            case 9:
                if (xdir < 0) {
                    setGraphic("mutant_walk_l");
                } else if (xdir > 0) {
                    setGraphic("mutant_walk_r");
                } else if (ydir > 0) {
                    setGraphic("mutant_walk_l");
                } else if (ydir < 0) {
                    setGraphic("mutant_walk_r");
                } else if (ydir == 0 && xdir == 0) {
                    if (this.getGraphic().equals("mutant_walk_l")) {
                        setGraphic("mutant_stand_l");
                    } else if (this.getGraphic().equals("mutant_walk_r")) {
                        setGraphic("mutant_stand_r");
                    }
                }
                break;
            case 10:
                if (xdir < 0) {
                    setGraphic("grif_walk_l");
                } else if (xdir > 0) {
                    setGraphic("grif_walk_r");
                } else if (ydir > 0) {
                    setGraphic("grif_walk_l");
                } else if (ydir < 0) {
                    setGraphic("grif_walk_r");
                } else if (ydir == 0 && xdir == 0) {
                    if (this.getGraphic().equals("grif_walk_l")) {
                        setGraphic("grif_walk_l");
                    } else if (this.getGraphic().equals("grif_walk_r")) {
                        setGraphic("grif_walk_r");
                    }
                }
                startAnim();
                
//                System.out.println(eng.getAnimation(this.getAnimId()).);
                break;
            case 12:
                if (xdir < 0) {
                    setGraphic("boss_stand_l");
                } else if (xdir > 0) {
                    setGraphic("boss_stand_r");
                } else if (ydir > 0) {
                    setGraphic("boss_stand_l");
                } else if (ydir < 0) {
                    setGraphic("boss_stand_r");
                } else if (ydir == 0 && xdir == 0) {
                    if (this.getGraphic().equals("boss_stand_l")) {
                        setGraphic("boss_stand_l");
                    } else if (this.getGraphic().equals("boss_stand_r")) {
                        setGraphic("boss_stand_r");
                    }
                }
                startAnim();
                break;
        }


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

    /** Removes object and object's occupation. */
    @Override
    public void destroy() {
        if (occupied != null) {
            eng.andTileCid(occupied.x, occupied.y, ~occupy_mask);
        }
    }

    /** Check if we aren't blocked in the xdir,ydir direction we're currently
     * going, and change direction if we're partially blocked.
     * This is a static method that can be applied to any JGObject.  It
     * modifies the xdir and ydir appropriately.
     * @param prevxdir  direction we were going previously (such as, last frame)
     * @param prevydir  direction we were going previously (such as, last frame)
     */
    public static void checkIfBlocked(JGObject o, int block_mask,
            int prevxdir, int prevydir) {
        JGPoint cen = o.getCenterTile();
        boolean can_go_h = !and(o.eng.getTileCid(cen, o.xdir, 0), block_mask);
        boolean can_go_v = !and(o.eng.getTileCid(cen, 0, o.ydir), block_mask);
        if (o.xdir != 0 && o.ydir != 0
                && and(o.eng.getTileCid(cen, o.xdir, o.ydir), block_mask)) {
            // blocked diagonally -> see if we can move horiz. or vert.
            if (can_go_h && can_go_v) {
                // alternate direction
                if (prevxdir != 0) {
                    o.xdir = 0;
                } else if (prevydir != 0) {
                    o.ydir = 0;
                } else {
                    o.xdir = 0; // arbitrary choice
                }
            } else if (can_go_h) {
                o.ydir = 0;
            } else if (can_go_v) {
                o.xdir = 0;
            } else {
                o.xdir = 0;
                o.ydir = 0;
            }
        }
        if (o.xdir != 0 && !can_go_h) {
            o.xdir = 0;
        }
        if (o.ydir != 0 && !can_go_v) {
            o.ydir = 0;
        }
    }

    public short getDestreza() {
        return destreza;
    }

    public void setDestreza(short destreza) {
        this.destreza = destreza;
    }

    public short getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(short experiencia) {
        this.experiencia = experiencia;
    }

    public short getFuerza() {
        return fuerza;
    }

    public void setFuerza(short fuerza) {
        this.fuerza = fuerza;
    }

    public short getSabiduria() {
        return sabiduria;
    }

    public void setSabiduria(short sabiduria) {
        this.sabiduria = sabiduria;
    }

    public short getVitalidad() {
        return vitalidad;
    }

    public void setVitalidad(short vitalidad) {
        this.vitalidad = vitalidad;
    }

    /**
     * Retoma el boton de habilidad
     */
    public void generarProximoAtaque() {
        if (eng.inGameState("InCombat")) {
            if (!isBlocked()) {
                //Genero una habilidad al azar
                this.setIdProximoAtaque(this.getHabilidades().getHabilidadAlAzar());
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
                this.bloquear(this.getHabilidades().getTiempoEspera(this.getIdProximoAtaque()));
            } else {
                this.setIdProximoAtaque((short) -1);
            }
        }

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
        this.hp = ((getVitalidad() / 5 * 100) + (getVitalidad() * 20) + (getNivel() * 100) + getNivel());
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
        if (this.hp <= 0){
            setMuriendo(true);
        }else setMuriendo(false);
    }

    public void muerte() {
        // si por algún motivo el mob muere, debe ser redireccionado
        // a UNA ventana en donde se lea el inventario del mob (mob.getInventario()
        // y se ofrezca los item al jugador, seleccionandoel icono correspondiente

        this.setMuerto(true);
        //eng.setGameState("InReward");


    }

    private void setMuerto(boolean b) {
        this.muriendo = b;

    }

    public boolean isMuerto() {
        return muriendo;
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
}
