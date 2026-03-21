package extensiones;

import jgame.*;

/**
 * A class for displaying an animated piece of text. The text is displayed
 * in the supplied font and can be made to colour-cycle.
 */
public class StdScoring extends JGObject {

    public String msg;
    JGColor[] cols;
    public int cycletimer = 0, cyclespeed;
    JGFont font;
    private boolean pf_relative = true;

    /**
     * Create animated piece of text.
     * 
     * @param name       name prefix (unique id is always added)
     * @param x          center of text (text is centered)
     * @param y          top of text
     * @param expiry     the expiry value: # ticks, -1=never, or -2=die offscreen
     * @param colors     colors through which to cycle
     * @param cyclespeed number of ticks before next colour in the colour cycle
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
        super("score", true, 0, 0, (int) Math.pow(2, 7), null, 0.0, 0.0, 0);
    }

    public StdScoring(String name, double x, double y, double xspeed, double yspeed,
            int expiry, String message, JGFont font, JGColor[] colors, int cyclespeed, boolean pf_relative) {
        super(name, true, x, y, (int) Math.pow(2, 7), null, xspeed, yspeed, expiry);
        msg = message;
        this.font = font;
        cols = colors;
        this.cyclespeed = cyclespeed;
        this.pf_relative = pf_relative;
    }

    // Color Presets
    public static final JGColor[] COLOR_DAMAGE = { JGColor.red, JGColor.orange, JGColor.yellow };
    public static final JGColor[] COLOR_CRIT = { JGColor.yellow, JGColor.red, JGColor.white };
    public static final JGColor[] COLOR_HEAL = { JGColor.green, JGColor.white };
    public static final JGColor[] COLOR_MANA = { JGColor.blue, JGColor.cyan };
    public static final JGColor[] COLOR_EXP = { JGColor.white, JGColor.yellow };

    private int originalAlpha = 255;

    /** Paints the message. */
    public void paint() {
        eng.setFont(font);
        // Fade out effect
        double lifePercent = 1.0;
        if (expiry > 0) {
            lifePercent = 1.0 - ((double) cycletimer / (double) expiry);
            if (lifePercent < 0)
                lifePercent = 0;
        }

        JGColor baseCol = cols[(cycletimer / cyclespeed) % cols.length];
        // Note: JGame might not support dynamic alpha on standard JGColor without
        // creating new objects
        // For efficiency, we might skip alpha or implement it if performance allows.
        // Assuming simple color cycling for now as strict alpha requires JGame 3.5+
        // features or custom drawing.

        eng.setColor(baseCol);
        eng.drawString(msg, (int) x, (int) y, 0, pf_relative);
        cycletimer++;
    }

    public void paintB() {
        if (cycletimer <= expiry || expiry == -1) {
            eng.setFont(font);
            eng.setColor(cols[(cycletimer / cyclespeed) % cols.length]);
            eng.drawString(msg, (int) x, (int) y, 0, pf_relative);
            cycletimer++;
        } else {
            remove();
        }
    }

    public void paintC() {
        eng.setFont(font);
        eng.setColor(cols[(cycletimer / cyclespeed) % cols.length]);
        eng.drawString(msg, (int) x, (int) y, 0, pf_relative);
        cycletimer++;
    }
}
