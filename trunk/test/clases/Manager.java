/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import jgame.JGColor;
import jgame.JGFont;
import jgame.JGPoint;
import jgame.platform.JGEngine;


/**
 *
 * @author gerald
 */
public class Manager extends JGEngine {
    public Personaje pj;
    public static void main(String[] args) {
        new Manager(new JGPoint(800, 768));
    }

    /** Application constructor. */
    public Manager(JGPoint size) {
        initEngine(size.x, size.y);
    }

    /** Applet constructor. */
    public Manager() {
        initEngineApplet();
    }

    public void initCanvas() {
        // we set the background colour to same colour as the splash background
        setCanvasSettings(40, 30, 16, 16, JGColor.black, new JGColor(255, 246, 199), null);

    }

    public void initGame() {
        setFrameRate(60, 2);
        

        defineMedia("dungeons_of_hack.tbl");
        //setBGImage("mybackground");
        setMsgFont(new JGFont("Helvetica",0,32));

        /* Busca Personaje segun idPersonaje*/
       
        pj = new Personaje(pfWidth()/2, pfHeight()/2, 1, KeyUp, KeyDown, KeyLeft, KeyRight);
       

        // create some tiles. "#" is our marble tile, "." is an empty space.
        setTiles(
                2, // tile x index
                2, // tile y index
                new String[]{"#####", "#", "#", "#"} // A series of tiles. Each String represents a line of tiles.
                );

        setTiles(13, 2, new String[]{"#####", "....#", "....#", "....#"});
        setTiles(13, 9, new String[]{"....#", "....#", "....#", "#####"});

        setTiles(2, 9, new String[]{"#....", "#....", "#....", "#####"});
        // define the off-playfield tiles
        setTileSettings(
                "#", // tile that is found out of the playfield bounds
                2, // tile cid found out of playfield bounds
                0 // which cids to preserve when setting a tile (not used here).
                );

    }

    public void doFrame() {

        moveObjects(null, 0);
        // check object-object collision
        checkCollision(
                1, // cids of objects that our objects should collide with
                1 // cids of the objects whose hit() should be called
                );
        // check object-tile collision
        checkBGCollision(
                1 + 2, // collide with the marble and border tiles
                1 // cids of our objects
                );
    }

    public void paintFrame() {
       // drawString("click ("+getMouseX()+", "+getMouseY()+")", pfWidth()/2, 5, 0);
        drawString("pj: ("+pj.x+", "+pj.y+")", pfWidth()/2, 5, 0);
        drawRect(pfWidth()-100, 0, 100, pfHeight(), false, false);
        drawRect(0, pfHeight()-90, pfWidth(), 100, false, false);
       
    }

    
}

