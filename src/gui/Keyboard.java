package gui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import piano.Piano;

/**
 * Keyboard
 */
public class Keyboard extends JPanel implements MouseListener, KeyListener {
    private static final long serialVersionUID = 1L;
    private static final int NUMOFWHITEKEYS = 5 * 7;

    private static final char[] keychars = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't',
            'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' };

    private static final char[] digittospecial = {'!','@','#','$','%','^','&','*','(',')'};

    private boolean labelsflag = true;

    private Piano piano;

    public Keyboard(Piano myp) {
        setPreferredSize(new Dimension(800, 200));
        piano = myp;
    }

    private Map<Character, Boolean> ischarred = new HashMap<>();

    public void press(char c) {
        if(ischarred.get(c)!=null){
            ischarred.replace(c, true);
        }else{
            ischarred.put(c, true);
        }
        repaint();
    }

    public void release(char c) {
        ischarred.replace(c, false);
        repaint();
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

            // crta beli
            g.setColor(Color.black);
            g.drawLine((int) x, (int) y, (int) x, (int) keyheight);

            if (labelsflag) {
                g.setColor(Color.black);
                Boolean b = ischarred.get(keychars[i]);
                if (b != null) {
                    if (b.equals(true)) {
                        g.setColor(Color.red);
                    }
                }

                g.drawChars(keychars, i, 1, (int) (x + keywidth / 2.0 - fontsize * 0.3),
                        (int) (y + keyheight * 3 / 4.0));
            }

            g.setColor(Color.black);
            x += keywidth;
            if (((i % 7) != 2) && ((i % 7) != 6) && i != NUMOFWHITEKEYS) {
                g.fillRect((int) (x - blackkeywidth / 2.0 + 1), (int) y, (int) blackkeywidth, (int) blackkeyheight);
                if (labelsflag) {
                    g.setColor(Color.white);
                    char c = keychars[i];
                    if(Character.isDigit(c)){
                        c = digittospecial[Character.getNumericValue(c)-1];
                        System.out.println("is digit");
                    }else{
                        c= Character.toUpperCase(c);
                    }
                    Boolean b = ischarred.get(c);
                    if (b != null) {
                        if (b.equals(true)) {
                            g.setColor(Color.red);
                        }
                    }
                    g.drawChars(keychars, i, 1, (int) (x - blackkeywidth / 5.0), (int) (y + blackkeyheight / 2.0));
                    g.drawString("↑", (int) (x - blackkeywidth / 5.0), (int) (y + blackkeyheight / 2.0 + fontsize));
                }
            }
        }

    }

    public void setLabels(boolean flag) {
        labelsflag = flag;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("pressed at" + e.getX());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        double keywidth = getWidth() / (double) NUMOFWHITEKEYS;
        int ind = (int) ((e.getX() / keywidth));
        System.out.println("mousex at: " + e.getX());
        System.out.println(ind);
        piano.play(keychars[ind]);
        try {
            Thread.sleep(150);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        piano.release(keychars[ind]);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // nista
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // nista
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // nista
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // System.out.println(e.getKeyChar());
        press(e.getKeyChar());
        piano.handlepress(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        release(e.getKeyChar());
        piano.handlerelease(e.getKeyChar());
    }

}