package gui;

import java.awt.Dimension;

import javax.swing.SwingUtilities;





/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Mainframe frame = new Mainframe("Muziza");
                frame.setSize(new Dimension(800,600));

                frame.setVisible(true);
            }
        });

    }
}