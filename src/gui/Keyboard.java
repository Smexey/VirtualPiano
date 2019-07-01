package gui;

import java.awt.*;

import javax.swing.JPanel;

/**
 * Keyboard
 */
public class Keyboard extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int NUMOFWHITEKEYS = 5 * 7;

    private static final char[] keychars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't',
            'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };

    private boolean labelsflag = true;

    public Keyboard() {
        setPreferredSize(new Dimension(800, 200));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        double x = 0, y = 0;
        double keywidth = getWidth() / (double) NUMOFWHITEKEYS;
        double keyheight = getHeight();

        double blackkeywidth = keywidth / 2.0;
        double blackkeyheight = keyheight / 2.0;
        g.setColor(Color.black);
        g.drawLine(0, 0, getWidth(), 0);
        int fontsize = (int) (blackkeywidth);

        g.setFont(new Font("Franklin Gothic", Font.BOLD, fontsize));
        for (int i = 0; i < NUMOFWHITEKEYS; i++) {
            g.setColor(Color.black);
            g.drawLine((int) x, (int) y, (int) x, (int) keyheight);

            if (labelsflag) {
                g.drawChars(keychars, i, 1, (int) (x + keywidth / 2.0 - fontsize * 0.3),
                        (int) (y + keyheight * 3 / 4.0));
            }
            x += keywidth;
            if (((i % 7) != 2) && ((i % 7) != 6) && i != NUMOFWHITEKEYS) {
                g.fillRect((int) (x - blackkeywidth / 2.0 + 1), (int) y, (int) blackkeywidth, (int) blackkeyheight);
                if (labelsflag) {
                    g.setColor(Color.white);
                    g.drawChars(keychars, i, 1, (int) (x-blackkeywidth/5.0), (int) (y + blackkeyheight / 2.0));
                    g.drawString("↑", (int) (x-blackkeywidth/5.0), (int) (y + blackkeyheight / 2.0 + fontsize));
                }
            }
        }

    }

    public void setLabels(boolean flag) {
        labelsflag = flag;
    }

}