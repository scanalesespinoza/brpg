package extensiones;

import jgame.*;

/** A class for displaying an animated piece of text.  The text is displayed
 * in the supplied font and can be made to colour-cycle. */
public class StdScoring extends JGObject {

    public String msg;
    JGColor[] cols;
    public int cycletimer = 0, cyclespeed;
    JGFont font;
    private boolean pf_relative = true;

    /** Create animated piece of text.
     * @param name  name prefix (unique id is always added)
     * @param x  center of text (text is centered)
     * @param y  top of text
     * @param expiry  the expiry value: # ticks, -1=never, or -2=die offscreen
     * @param colors  colors through which to cycle
     * @param cyclespeed  number of ticks before next colour in the colour cycle
     */
    public StdScoring(String name, double x, double y, double xspeed, double yspeed,
            int expiry, String message, JGFont font, JGColor[] colors, int cyclespeed) {
        super(name, true, x, y, (int) Math.pow(2, 7), null, xspeed, yspeed, expiry);
        msg = message;
        this.font = font;
        cols = colors;
        this.cyclespeed = cyclespeed;
    }

    public StdScoring() {
        super("score", true, 0, 0, (int) Math.pow(2, 7), null, 0, 0, 0);
    }

    public StdScoring(String name, double x, double y, double xspeed, double yspeed,
            int expiry, String message, JGFont font, JGColor[] colors, int cyclespeed,boolean pf_relative) {
        super(name, true, x, y, (int) Math.pow(2, 7), null, xspeed, yspeed, expiry);
        msg = message;
        this.font = font;
        cols = colors;
        this.cyclespeed = cyclespeed;
        this.pf_relative = pf_relative;
    }

    /** Paints the message. */
    public void paint() {
        eng.setFont(font);
        eng.setColor(cols[(cycletimer / cyclespeed) % cols.length]);
        eng.drawString(msg, (int) x, (int) y, 0, pf_relative);
        cycletimer++;
    }

    public void paintB() {
        if (cycletimer <=  expiry) {
            eng.setFont(font);
            eng.setColor(cols[(cycletimer / cyclespeed) % cols.length]);
            eng.drawString(msg, (int) x, (int) y, 0, pf_relative);
            cycletimer++;
        }
    }
}
