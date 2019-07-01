package piano;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import lib.Pair;

/**
 * Piano
 */
public class Piano extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;

    private Map<Character, Pair<String, Integer>> mapa;
    private ArrayList<Composition> comparr = new ArrayList<Composition>();

    public Piano() {
        setBackground(Color.cyan);

    }

    public Piano(String path) {
        this();
        loadmap(path);
    }

    public Piano(Map<Character, Pair<String, Integer>> m) {
        this();
        mapa = m;
    }

    Map<Character, Pair<String, Integer>> getMap() {
        return mapa;
    }

    public void addNewComp(String path) {
        comparr.add(new Composition(path));
    }

    public ArrayList<Composition> getCompArr() {
        return comparr;
    }

    public void loadmap(String path) {
        mapa = new HashMap<>();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                // ubaci u mapu taj line csv
                String[] vals = text.split(",");
                mapa.put(vals[0].charAt(0), new Pair<String, Integer>(vals[1], Integer.parseInt(vals[2])));
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        //nista?
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("!" + e.getKeyChar());
    }

}