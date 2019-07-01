package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.sound.midi.MidiUnavailableException;
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
        setSize(new Dimension(300, 200));
        setLayout(new BorderLayout());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        keyboard = new Keyboard();
        try {
            piano = new Piano();
        } catch (MidiUnavailableException e) {
            System.out.println(e);
        }

        piano.loadmap(Piano.DEFAULT_MAP_PATH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                repaint();
            }
        });

        add(piano, BorderLayout.CENTER);
        addKeyListener(piano);
        add(keyboard, BorderLayout.SOUTH);

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // for (Component var : getComponents()) {
        //     var.repaint();
        // }
    }

    public static void main(String[] args) {
        Mainframe frame = new Mainframe("Mužiža");

        frame.setSize(new Dimension(1000, 500));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        frame.setVisible(true);
    }

}
