/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import jgame.JGObject;
import jgame.JGPoint;
/**
 *
 * @author gerald
 */
public class Mob extends Personaje{

    public JGObject home_in=null;
    /** true = avoid home_in position */
    public boolean avoid=false;
    /** chance that object moves randomly */
    public double random_proportion;
    /* state */
    public double tiempo_espera_original;
    /* tiempo de espera oeiginal para volver a moverse */
    public double tiempo_espera;
    /* tiempo de espera oeiginal para volver a moverse */



    private short vitalidad;

    private short sabiduria;

    private short fuerza;

    private short destreza;

    private short experiencia;


    public Mob(double x, double y, double speed, short idPj, String nombrePj, String graf, short nivelPj, short tipoPj,JGObject home_in,boolean avoid,double random_proportion,int cid) /*throws SQLException*/ {
        super(x, y, speed, idPj, nombrePj, graf, nivelPj, tipoPj, cid);
        this.setIdPersonaje(idPj);
        this.home_in=home_in;
        this.avoid=avoid;
        this.random_proportion=random_proportion;
    }

    public Mob(){
    }



    @Override
    public void move() {

                if (occupied==null
                ||  (xspeed==0 && yspeed==0)
                ||  ( xdir==0 && ydir==0 &&  (!isXAligned() || !isYAligned()) )  ) {
                        // make sure we're occupying space and are on the grid when we're
                        // not moving
                        if (occupied!=null)
                                eng.andTileCid(occupied.x,occupied.y,~occupy_mask);
                        snapToGrid(eng.tileWidth(),eng.tileHeight());
                        occupied = getCenterTile();
                        eng.orTileCid(occupied.x,occupied.y,occupy_mask);
                } else if (!isXAligned() || !isYAligned()) {
                        // we're moving and not aligned -> move until tile aligned
                        if (!continuous_anim) startAnim();
                } else { // tile aligned -> ready to change direction
                        int prevxdir=xdir, prevydir=ydir;
                        snapToGrid();
                        JGPoint cen = getCenterTile();
                        // determine direction
                        setDir(0,0);
                        int newxdir=0,newydir=0;
                        boolean xdir_any=false,ydir_any=false; // alternate directions
                        if (home_in!=null && eng.random(0.0001,0.9999)>random_proportion) {
                                int basedir = avoid ? -1 : 1;
                                if (home_in.x < x) newxdir = -basedir;
                                if (home_in.x > x) newxdir = basedir;
                                if (home_in.y < y) newydir = -basedir;
                                if (home_in.y > y) newydir = basedir;
                                if (Math.abs(home_in.x-x) > Math.abs(home_in.y-y)) {
                                        ydir_any=true;
                                } else {
                                        xdir_any=true;
                                }
                        } else { // random
                                newxdir = eng.random(-1,1,1);
                                newydir = eng.random(-1,1,1);
                                xdir_any=true;
                                ydir_any=true;
                        }
                        // check if we can go this way
                        xdir = newxdir;
                        ydir = newydir;
                        checkIfBlocked(this,block_mask,prevxdir,prevydir);
                        if (xdir==0 && ydir==0) {
                                // if not, try an alternate direction
                                if (xdir_any) {
                                        if (newxdir!=0) xdir = -newxdir;
                                        else            xdir = eng.random(-1,1,2);
                                        ydir = newydir;
                                        checkIfBlocked(this,block_mask,prevxdir,prevydir);
                                } else if (ydir_any) {
                                        xdir = newxdir;
                                        if (newydir!=0) ydir = -newydir;
                                        else            ydir = eng.random(-1,1,2);
                                        checkIfBlocked(this,block_mask,prevxdir,prevydir);
                                }
                        }
                        // occupy new tile, or same tile if we didn't move
                        if (occupied!=null)
                                eng.andTileCid(occupied.x,occupied.y,~occupy_mask);
                        occupied = new JGPoint(cen.x+xdir,cen.y+ydir);
                        eng.orTileCid(occupied.x,occupied.y,occupy_mask);
                        if (!continuous_anim) {
                                if (xdir!=0 || ydir!=0) startAnim();
                                else                    stopAnim();
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
                if (occupied!=null)
                        eng.andTileCid(occupied.x,occupied.y,~occupy_mask);
        }
        /** Check if we aren't blocked in the xdir,ydir direction we're currently
        * going, and change direction if we're partially blocked.
        * This is a static method that can be applied to any JGObject.  It
        * modifies the xdir and ydir appropriately.
        * @param prevxdir  direction we were going previously (such as, last frame)
        * @param prevydir  direction we were going previously (such as, last frame)
        */

        public static void checkIfBlocked(JGObject o,int block_mask,
        int prevxdir,int prevydir) {
                JGPoint cen = o.getCenterTile();
                boolean can_go_h=!and(o.eng.getTileCid(cen,o.xdir,0),block_mask);
                boolean can_go_v=!and(o.eng.getTileCid(cen,0,o.ydir),block_mask);
                if (o.xdir!=0 && o.ydir!=0
                &&  and(o.eng.getTileCid(cen,o.xdir,o.ydir),block_mask) ) {
                        // blocked diagonally -> see if we can move horiz. or vert.
                        if (can_go_h && can_go_v) {
                                // alternate direction
                                if (prevxdir!=0)      o.xdir=0;
                                else if (prevydir!=0) o.ydir=0;
                                else                  o.xdir=0; // arbitrary choice
                        } else if (can_go_h) {
                                o.ydir=0;
                        } else if (can_go_v) {
                                o.xdir=0;
                        } else {
                                o.xdir=0;
                                o.ydir=0;
                        }
                }
                if (o.xdir!=0 && !can_go_h) o.xdir=0;
                if (o.ydir!=0 && !can_go_v) o.ydir=0;
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

}
