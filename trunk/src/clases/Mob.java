/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jgame.JGObject;
import jgame.JGPoint;
import jgame.JGTimer;

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
    private int hpMax;
    private int mpMax;
    private boolean muerto;

    public Mob(double x, double y, double speed, short idPj, String nombrePj, String graf, short nivelPj, short tipoPj, JGObject home_in, boolean avoid, double random_proportion, int cid) /*throws SQLException*/ {
        super(x, y, speed, idPj, nombrePj, graf, nivelPj, tipoPj, cid);
        this.setIdPersonaje(idPj);
        this.home_in = home_in;
        this.avoid = avoid;
        this.random_proportion = random_proportion;
        this.setHp();
        this.setMp();
    }

    public Mob() {
    }

    @Override
    public void cargarPersonaje(Short id) {
        conexion = new dbDelegate();
        System.out.println("Inicio obtiene datos Mob");
        String StrSql = "SELECT  pjuno.id id, pjuno.nombre nombre, pjuno.nivel nivel, "
                + " pjuno.posicionX posX, pjuno.posicionY posY,pjuno.tipo tipo, pjdos.vitalidad vit,"
                + " pjdos.destreza des, pjdos.sabiduria sab, pjdos.fuerza fue,"
                + " pjdos.experiencia experiencia "
                + " FROM personaje pjuno, mob pjdos "
                + "WHERE pjuno.id=" + id
                + "  AND pjdos.Personaje_id=" + id;
        System.out.println(StrSql);
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


            }
        } catch (SQLException ex) {
            System.out.println("Problemas en: clase->MOB , método->cargarPersonaje() " + ex);
        }
        try {
            this.conexion.cierraDbCon();
        } catch (Exception ex) {
            Logger.getLogger(Personaje.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setHp();
        this.setMp();
    }

    @Override
    public void move() {

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
            boolean xdir_any = false, ydir_any = false; // alternate directions
            if (home_in != null && eng.random(0.0001, 0.9999) > random_proportion) {
                int basedir = avoid ? -1 : 1;
                if (home_in.x < x) {
                    newxdir = -basedir;
                }
                if (home_in.x > x) {
                    newxdir = basedir;
                }
                if (home_in.y < y) {
                    newydir = -basedir;
                }
                if (home_in.y > y) {
                    newydir = basedir;
                }
                if (Math.abs(home_in.x - x) > Math.abs(home_in.y - y)) {
                    ydir_any = true;
                } else {
                    xdir_any = true;
                }
            } else { // random
                newxdir = eng.random(-1, 1, 1);
                newydir = eng.random(-1, 1, 1);
                xdir_any = true;
                ydir_any = true;
            }
            // check if we can go this way
            xdir = newxdir;
            ydir = newydir;
            checkIfBlocked(this, block_mask, prevxdir, prevydir);
            if (xdir == 0 && ydir == 0) {
                // if not, try an alternate direction
                if (xdir_any) {
                    if (newxdir != 0) {
                        xdir = -newxdir;
                    } else {
                        xdir = eng.random(-1, 1, 2);
                    }
                    ydir = newydir;
                    checkIfBlocked(this, block_mask, prevxdir, prevydir);
                } else if (ydir_any) {
                    xdir = newxdir;
                    if (newydir != 0) {
                        ydir = -newydir;
                    } else {
                        ydir = eng.random(-1, 1, 2);
                    }
                    checkIfBlocked(this, block_mask, prevxdir, prevydir);
                }
            }
            // occupy new tile, or same tile if we didn't move
            if (occupied != null) {
                eng.andTileCid(occupied.x, occupied.y, ~occupy_mask);
            }
            occupied = new JGPoint(cen.x + xdir, cen.y + ydir);
            eng.orTileCid(occupied.x, occupied.y, occupy_mask);
            if (!continuous_anim) {
                if (xdir != 0 || ydir != 0) {
                    startAnim();
                } else {
                    stopAnim();
                }
            }
//                        if (gfx_prefix!=null) {
//                                if (ydir <  0 && xdir <  0) setGraphic(gfx_prefix+"ul");
//                                if (ydir <  0 && xdir == 0) setGraphic(gfx_prefix+"u");
//                                if (ydir <  0 && xdir >  0) setGraphic(gfx_prefix+"ur");
//                                if (ydir == 0 && xdir <  0) setGraphic(gfx_prefix+"l");
//                                if (ydir == 0 && xdir >  0) setGraphic(gfx_prefix+"r");
//                                if (ydir >  0 && xdir <  0) setGraphic(gfx_prefix+"dl");
//                                if (ydir >  0 && xdir == 0) setGraphic(gfx_prefix+"d");
//                                if (ydir >  0 && xdir >  0) setGraphic(gfx_prefix+"dr");
//                        }
        }


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
                System.out.println("CAMBIO DE HABILIDAD a " + getIdProximoAtaque());
                this.utilizarHabilidad();
            } else {
                this.setIdProximoAtaque((short) -1);
            }
        } else {
            this.setIdProximoAtaque((short) -1);
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
            System.out.println("MAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaaa : " + this.mp);
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
        // si por algún motivo el mob muere, debe ser redireccionado
        // a UNA ventana en donde se lea el inventario del mob (mob.getInventario()
        // y se ofrezca los item al jugador, seleccionandoel icono correspondiente
        new JGTimer((int) (eng.getFrameRate() * 5 * 1), true) {

                @Override
                public void alarm() {
                    System.out.println("ENTRE ACA WEON2");
                    resume();

                }
            };
        this.setMuerto(true);
        eng.setGameState("InReward");
    }

    private void setMuerto(boolean b) {
        this.muerto = b;
    }

    public boolean isMuerto() {
        return muerto;
    }

    public void regenerarMp(int porcentaje, int seg) {
        if (seg % 3 == 0) {
            aumentarDisminuirMp((int) (mpMax * ((float) (porcentaje / 100.0))));
        }
    }

    public void aumentarDisminuirMp(int cant) {
        System.out.println("VALORES: ");
        System.out.println("this.mpMax : " + this.mpMax);
        System.out.println("this.getMp(): " + this.getMp());
        System.out.println("cant: " + cant);

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
