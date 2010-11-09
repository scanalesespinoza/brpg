package extensiones;
import jgame.*;

/** A standard object that allows the player to move in 8 directions around a
 * tile-based dungeon, which may be maze-like or contain open spaces.  Typical
 * application: gauntlet, and a variety of others.  The player moves in 8
 * directions along tile-aligned positions.  It can be made to occupy a space
 * on the tile map, blocking other objects.  The occupation is automatically
 * removed if the object is removed. It is assumed the object is 1x1 tile in
 * size, and the tileBBox is set to this.

 * <p>The public fields are the configuration fields which can be changed
 * at will during the object's lifetime.

 * <p>You can make movement stop by setting the stop_moving flag.  The
 * player will move on to the next aligned position, then halt until the flag
 * is cleared.  In some games, the player should stop when the movement keys
 * are used for firing or doing actions.

 * <p>This class only defines the move() method; hit() and hit_bg() are not
 * used.  Subclass it to customize.  To customize move(), call super.move()
 * somewhere inside your move method.  While in general you shouldn't touch
 * the object's position, direction, or speed in order to ensure correct
 * behaviour, you can set any of the configuration variables in the move()
 * method or somewhere else.

 */
public class StdDungeonPlayerV2 extends JGObject {
	/* config */
        private boolean bUpkey=false, bDownkey=false, bLeftkey=false, bRightkey=false;
	/** null indicates non-directional graphic */
	public String gfx_prefix=null;
	/** don't stop animating when player stops moving */
	public boolean continuous_anim;
	/** cid mask of tiles that block */
	public int block_mask;
	/** cid mask that object should use to indicate occupation */
	public int occupy_mask;
	//public double speed;
	/** Movement keys. */
	public boolean  pkey_up,pkey_down,pkey_left,pkey_right;
	//public int key_upleft,key_downleft,key_upright,key_downright;
	/** Set to true to disable movement (for example, for firing). */
	public boolean stop_moving;
	/* state */
	public JGPoint occupied=null;
	/** When initialised, the object will snap to grid to the nearest free
	 * position.  The object's graphic can be made
	 * directional by setting is_directional.  This will add the suffix
	 * "ul", "u", "ur", "dl", "d", "dr", "l", or "r" to the graphic string
	 * to indicate the direction of movement.  It is possible to define keys
	 * for the diagonal directions, but you can pass 0 as keycode if you don't
	 * want this.
	* @param x  position in pixels
	* @param y  position in pixels
	* @param graphic  prefix of graphic
	* @param is_directional  true = add direction suffix to graphic
	* @param continuous_anim  don't stop animating when player stops moving
	* @param block_mask  cid mask of tiles that block
	* @param occupy_mask  cid mask that object should use to indicate occupation
	*/
        public StdDungeonPlayerV2(String name, double x,double y, int cid,
        String graphic, boolean is_directional, boolean continuous_anim,
        int block_mask, int occupy_mask, double speed) {
                super(name,false, x,y, cid, graphic);
                setTileBBox(16,16,0,0);
                setDir(0,0);
                if (is_directional) gfx_prefix=graphic;
                this.continuous_anim=continuous_anim;
                this.pkey_up=false;
                this.pkey_down=false;
                this.pkey_left=false;
                this.pkey_right=false;
                /*
		this.key_upleft=key_upleft;
		this.key_downleft=key_downleft;
		this.key_upright=key_upright;
		this.key_downright=key_downright;
                 */
                occupy_mask=block_mask;
		this.block_mask=block_mask;
		this.occupy_mask=occupy_mask;
		setSpeed(speed,speed);
	}
	/** Moves the object around, and ensures it occupies
	 * space.  If you override this, be sure to call super.move() and don't
	 * touch x, y, xspeed, yspeed, xdir, ydir. */
    @Override
	public void move() {
                //coincidir direccion grafica con logica
                if (xdir < 0){
                    setGraphic("human_l");
                } else if (xdir > 0){
                    setGraphic("human_r");
                } else if (ydir < 0){
                    setGraphic("human_u");
                } else if (ydir > 0){
                    setGraphic("human_d");
                }

		if ( occupied==null 
		||   (xdir==0 && ydir==0 && (!isXAligned() || !isYAligned()) )  ) {
			// make sure we're occupying space and are on the grid when we're
			// not moving
			if (occupied!=null)
				eng.andTileCid(occupied.x,occupied.y,~occupy_mask);
			snapToGrid(eng.tileWidth(),eng.tileHeight());
			occupied = getCenterTile();
			eng.orTileCid(occupied.x,occupied.y,occupy_mask);
		} else if (!isXAligned() || !isYAligned()) {
			// move until tile aligned
			if (!continuous_anim) startAnim();
		} else { // tile aligned -> ready to change direction
			int prevxdir=xdir, prevydir=ydir;
			snapToGrid();
			JGPoint cen = getCenterTile();
			// determine direction
			setDir(0,0);
			if (!stop_moving) {
				if (bLeftkey&& (x- xdir)+ eng.tileWidth()>0 )  xdir = -1;
				if (bRightkey&& x + xdir<eng.pfWidth()) xdir = 1;
				if (bUpkey && y- ydir>0 )    ydir = -1;
				if (bDownkey && (y + ydir)+ eng.tileHeight()< eng.pfHeight())  ydir = 1;
			}
			StdDungeonMonster.checkIfBlocked(this,block_mask,prevxdir,prevydir);
			// occupy new tile, or same tile if we didn't move
			if (occupied!=null)
				eng.andTileCid(occupied.x,occupied.y,~occupy_mask);
			occupied = new JGPoint(cen.x+xdir,cen.y+ydir);
			eng.orTileCid(occupied.x,occupied.y,occupy_mask);
			if (!continuous_anim) {
				if (xdir!=0 || ydir!=0) startAnim();
				else                    stopAnim();
			}
		}
	}
	/** Removes object and object's occupation. */
	public void destroy() {
		// remove occupation
		if (occupied!=null)
			eng.andTileCid(occupied.x,occupied.y,~occupy_mask);
	}

    /**
     * @param bUpkey the bUpkey to set
     */
    public void setbUpkey(boolean bUpkey) {
        this.bUpkey = bUpkey;
    }

    /**
     * @param bDownkey the bDownkey to set
     */
    public void setbDownkey(boolean bDownkey) {
        this.bDownkey = bDownkey;
    }

    /**
     * @param bLeftkey the bLeftkey to set
     */
    public void setbLeftkey(boolean bLeftkey) {
        this.bLeftkey = bLeftkey;
    }

    /**
     * @param bRightkey the bRightkey to set
     */
    public void setbRightkey(boolean bRightkey) {
        this.bRightkey = bRightkey;
    }
}
