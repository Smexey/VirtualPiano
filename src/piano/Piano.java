package piano;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
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
    public static final int DEFAULT_INSTRUMENT = 1;
    public static final String DEFAULT_MAP_PATH = "map.csv";

    private Map<Character, Pair<Boolean,Pair<String, Integer>>> mapa;
    private ArrayList<Composition> comparr = new ArrayList<Composition>();

    private MidiChannel channel;

    public Piano(int instrument) throws MidiUnavailableException {
        setBackground(Color.cyan);
        channel = getChannel(instrument);
    }

    public Piano() throws MidiUnavailableException {
        this(DEFAULT_INSTRUMENT);
    }

    private static MidiChannel getChannel(int instrument) throws MidiUnavailableException {
        Synthesizer synthesizer = MidiSystem.getSynthesizer();
        synthesizer.open();
        return synthesizer.getChannels()[instrument];
    }

    public Piano(String path) throws MidiUnavailableException {
        this();
        loadmap(path);
    }

    public Piano(Map<Character, Pair<Boolean, Pair<String, Integer>>> m) throws MidiUnavailableException {
        this();
        mapa = m;
    }

    Map<Character, Pair<Boolean, Pair<String, Integer>>> getMap() {
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
                mapa.put(vals[0].charAt(0), new Pair<Boolean, Pair<String, Integer>>(false,new Pair<String, Integer>(vals[1], Integer.parseInt(vals[2]))));
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

    public void play(final int note) {
        channel.noteOn(note, 50);
    }

    public void release(final int note) {
        channel.noteOff(note, 50);
    }

    public void play(final int note, final long length) throws InterruptedException {
        play(note);
        Thread.sleep(length);
        release(note);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //wont play
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Pair<String, Integer> p = mapa.get(e.getKeyChar()).getSecond();
        if (p != null ){
            if(!(mapa.get(e.getKeyChar()).getFirst())) play(p.getSecond());
            mapa.get(e.getKeyChar()).setFirst(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Pair<String, Integer> p = mapa.get(e.getKeyChar()).getSecond();
        if (p != null){
            mapa.get(e.getKeyChar()).setFirst(false);
            release(p.getSecond());
        }
    }

}