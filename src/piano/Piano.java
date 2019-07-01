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

/**
 * Piano
 */
public class Piano extends JPanel implements KeyListener {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_INSTRUMENT = 1;
    public static final String DEFAULT_MAP_PATH = "map.csv";
    public static final int DEFAULT_VELOCITY = 60;

    private static class MidiNoteInfo{
        private boolean isplaying=false;
        private String name;
        private int midival;
        private long t_start;
        public MidiNoteInfo(String n,int midivall){
            midival = midivall;
            name = n;
        }

    }

    private Map<Character, MidiNoteInfo> mapa;
    private Composition comp;

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

    public Piano(Map<Character, MidiNoteInfo> m) throws MidiUnavailableException {
        this();
        mapa = m;
    }

    Map<Character, MidiNoteInfo> getMap() {
        return mapa;
    }

    public void loadComp(String path) {
        comp = new Composition(path);
    }

    public Composition getComp() {
        return comp;
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
                mapa.put(vals[0].charAt(0), new MidiNoteInfo(vals[1], Integer.parseInt(vals[2])));
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

    public void play(MidiNoteInfo m) {
        channel.noteOn(m.midival, DEFAULT_VELOCITY);
        m.t_start = System.currentTimeMillis();
        m.isplaying = true;
    }

    public void release(MidiNoteInfo m) {
        channel.noteOff(m.midival, DEFAULT_VELOCITY);
        m.isplaying = false;
        //logika pomeranja udesno composicije
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //nista
    }

    @Override
    public void keyPressed(KeyEvent e) {
        MidiNoteInfo p = mapa.get(e.getKeyChar());
        if (p != null ){
            if(!p.isplaying) play(p);
            p.isplaying = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        MidiNoteInfo p = mapa.get(e.getKeyChar());
        if (p != null){
            release(p);
        }
    }

}