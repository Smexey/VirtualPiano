package guiawt;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * Mainframe
 */
public class Mainframe extends JFrame {

    private static final long serialVersionUID = 1L;
    private Keyboard keyboard;

    public Mainframe(String name) {
        super(name);
        setSize(new Dimension(300,200));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        
        add(keyboard = new Keyboard(),BorderLayout.SOUTH);
    }

}