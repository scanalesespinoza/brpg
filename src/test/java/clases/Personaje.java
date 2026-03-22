/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import extensiones.StdDungeonPlayer;
import jgame.*;

/**
 *
 * @author gerald
 */
public class Personaje extends StdDungeonPlayer {
    // object cids: 1=player 2=monster 4=bullet 8=monsterbullet
	public static final int WALL_T=1;
	public static final int SHWALL_T=2; // shootable wall
	public static final int DOOR_T=4;
	public static final int PLAYER_T=16;
	public static final int MONSTER_T=32;
	public static final int GEN_T=64; // generator
	public static final int BONUS_T=128;
	public static final int KEY_T=256;
	public static final int HEALTH_T=512;
	public static final int PLAYERBLOCK_T =
		WALL_T|SHWALL_T|DOOR_T|GEN_T;
	public static final int MONSTERBLOCK_T =
		WALL_T|SHWALL_T|DOOR_T|BONUS_T|MONSTER_T|GEN_T|BONUS_T|KEY_T|HEALTH_T;
	public static final int BULLETBLOCK_T =
		WALL_T|DOOR_T|BONUS_T|BONUS_T|KEY_T|HEALTH_T;
    private short idPersonaje;
    private String nombre;
    private short nivel;
    private short posicionX;
    private short posicionY;
    private String tipo;

    public Personaje(double x, double y, double speed, int upkey, int downkey,int leftkey, int rightkey) {
        super("player", x, y, 1, "human_l", false, false,
                PLAYERBLOCK_T, PLAYER_T, 2.3,
                upkey, downkey, leftkey, rightkey);
        stopAnim();
    }

    /**
     * @return the idPersonaje
     */
    public short getIdPersonaje() {
        return idPersonaje;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @return the nivel
     */
    public short getNivel() {
        return nivel;
    }

    /**
     * @return the posicionX
     */
    public short getPosicionX() {
        return posicionX;
    }

    /**
     * @return the posicionY
     */
    public short getPosicionY() {
        return posicionY;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param idPersonaje the idPersonaje to set
     */
    public void setIdPersonaje(short idPersonaje) {
        this.idPersonaje = idPersonaje;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(short nivel) {
        this.nivel = nivel;
    }

    /**
     * @param posicionX the posicionX to set
     */
    public void setPosicionX(short posicionX) {
        this.posicionX = posicionX;
    }

    /**
     * @param posicionY the posicionY to set
     */
    public void setPosicionY(short posicionY) {
        this.posicionY = posicionY;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    int prevxdir=1,prevydir=0;
    @Override
    public void move() {
        super.move();
    }
}
