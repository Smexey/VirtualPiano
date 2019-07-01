package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import piano.Piano;



/**
 * Mainframe
 */
public class Mainframe extends JFrame {

    private static final long serialVersionUID = 1L;
    private Keyboard keyboard;
    private Piano piano;

    public Mainframe(String name) {
        super(name);
        setSize(new Dimension(300,200));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        keyboard = new Keyboard();
        piano = new Piano();

        add(piano,BorderLayout.CENTER);
        addKeyListener(piano);

        add(keyboard,BorderLayout.SOUTH);
    }

}