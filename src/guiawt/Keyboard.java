package guiawt;

import java.awt.*;

import javax.swing.JLabel;
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
    private JLabel[] labelsarr;

    public Keyboard() {
        setPreferredSize(new Dimension(800, 200));
        labelsarr = new JLabel[keychars.length];
        for (int i = 0; i < keychars.length; i++) {
            labelsarr[i] = new JLabel(Character.toString(keychars[i]));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        double x = 0, y = 0;
        double keywidth = getWidth() / NUMOFWHITEKEYS;
        double keyheight = getHeight();

        double blackkeywidth = keywidth / 2;
        double blackkeyheight = keyheight / 2;
        g.setColor(Color.black);
        g.drawLine(0, 0, getWidth(), 0);
        int fontsize = (int) (blackkeywidth);

        g.setFont(new Font("FranklinGothic", Font.BOLD, fontsize));
        for (int i = 0; i <= NUMOFWHITEKEYS; i++) {
            g.setColor(Color.black);
            g.drawLine((int) x, (int) y, (int) x, (int) keyheight);

            if (labelsflag) {
                g.drawChars(keychars, i, 1, (int) (x + keywidth / 2 - fontsize * 0.3), (int) (y + keyheight * 3 / 4));
            }
            x += keywidth;
            if (((i % 7) != 2) && ((i % 7) != 6)) {
                g.fillRect((int) (x - keywidth / 3 / 2), (int) y, (int) blackkeywidth, (int) blackkeyheight);
                if (labelsflag) {
                    g.setColor(Color.white);
                    g.drawChars(keychars, i, 1, (int) ((x - keywidth / 3 / 2) + blackkeywidth / 2 - fontsize * 0.3),
                            (int) (y + blackkeyheight / 2));

                    g.drawString("↑", (int) ((x - keywidth / 3 / 2) + blackkeywidth / 2 - fontsize * 0.3),
                            (int) (y + blackkeyheight / 2 + fontsize));
                }
            }
        }

    }

    public void setLabels(boolean flag) {
        labelsflag = flag;
    }

}