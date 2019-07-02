package piano;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JPanel;

import gui.Keyboard;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Piano
 */
public class Piano extends JPanel {
    private static final long serialVersionUID = 1L;
    public static final int DEFAULT_INSTRUMENT = 1;
    public static final String DEFAULT_MAP_PATH = "map.csv";
    public static final int DEFAULT_VELOCITY = 60;

    private Keyboard keyboard;

    public static enum Modes {
        GAME, AUTOPLAY,RECORD;
    }

    private Modes mode = Modes.AUTOPLAY;

    public void setmode(Modes m) {
        if (mode.equals(m))
            return;

        if (m.equals(Modes.GAME)) {
            swaptogame();
        } else if(m.equals(Modes.AUTOPLAY)) {
            swaptoautoplay();
        }else{
            swaptorecord();
        }
    }

    public void setkeyboard(Keyboard k){
        keyboard = k;
    }

    private void swaptogame() {
        player.reset();
        mode = Modes.GAME;
    }

    private void swaptoautoplay() {
        comp.reset();
        mode = Modes.AUTOPLAY;
    }

    private void swaptorecord() {
        player.reset();
        remove(comp);
        comp = new Composition();
        add(comp);
        comp.setPiano(this);
        
        mode = Modes.RECORD;
        revalidate();
        repaint();
    }

    private static class MidiNoteInfo {
        private boolean isplaying = false;
        private String name;
        private int midival;
        private long t_start;

        public MidiNoteInfo(String n, int midivall) {
            midival = midivall;
            name = n;
        }

    }

    private Map<Character, MidiNoteInfo> mapa;
    private Composition comp;

    private MidiChannel channel;

    private Player player = new Player();

    public String mapChartoStr(char c) {
        MidiNoteInfo s;
        if ((s = mapa.get(c)) != null)
            return s.name;
        return null;
    }

    public Piano(int instrument) throws MidiUnavailableException {
        setBackground(Color.cyan);
        channel = getChannel(instrument);
        setLayout(new GridLayout(0, 1));
        player.start();
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
        if (comp != null)
            remove(comp);
        comp = new Composition(path);
        add(comp);
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

    public void playcomp() {
        player.playcomp();
    }

    public void play(char c) {
        handlepress(c);
    }

    public void release(char c) {
        handlerelease(c);
    }

    public void handlepress(char c) {
        MidiNoteInfo p = mapa.get(c);
        if (p != null) {
            if (!p.isplaying)
                play(p);
            p.isplaying = true;
        }
    }

    public void handlerelease(char c) {
        MidiNoteInfo p = mapa.get(c);
        if (p != null) {
            release(p);
        }
    }

    private void play(MidiNoteInfo m) {
        channel.noteOn(m.midival, DEFAULT_VELOCITY);
        m.t_start = System.currentTimeMillis();
        m.isplaying = true;
    }

    private void release(MidiNoteInfo m) {
        channel.noteOff(m.midival, DEFAULT_VELOCITY);
        m.isplaying = false;
        // logika pomeranja udesno composicije
    }

    public void stop() {
        channel.allNotesOff();
        player.interrupt();
    }

    synchronized public void pause() {
        player.pause();
    }

    synchronized public void unpause() {
        player.unpause();
    }

    synchronized public void reset() {
        player.reset();
    }

    private class Player extends Thread {

        private boolean working = false;
        private boolean paused = false;
        private static final int COEFFICIENT = 1;
        private static final long EIGHTPLAYTIME = 1000 / 8 * COEFFICIENT;

        public Player() {
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    synchronized (this) {
                        while (!working)
                            wait();
                        // unutar synchronized jer ne sme da se playcomp dva komada?
                    }
                    var part = comp.getPartCopy();

                    for (MusicSymbol sym : part) {
                        synchronized (this) {
                            while (paused) {
                                wait();
                            }
                            if (!working){
                                break;
                            }
                            
                        }

                        if (sym instanceof Chord) {
                            Chord chord = (Chord) sym;
                            for (char c : chord.arr) {
                                channel.noteOn(mapa.get(c).midival, DEFAULT_VELOCITY);
                                if(keyboard!=null){
                                    keyboard.press(c);
                                }
                            }
                            sleep(sym.getDur().getEightNum() * EIGHTPLAYTIME);
                            for (char c : chord.arr) {
                                channel.noteOff(mapa.get(c).midival, DEFAULT_VELOCITY);
                                if(keyboard!=null){
                                    keyboard.release(c);
                                }
                            }
                        } else if (sym instanceof Pause) {
                            sleep(sym.getDur().getEightNum() * EIGHTPLAYTIME);
                        } else {
                            Note note = (Note) sym;
                            channel.noteOn(mapa.get(note.getC()).midival, DEFAULT_VELOCITY);
                            if(keyboard!=null){
                                keyboard.press(note.getC());
                            }
                            sleep(sym.getDur().getEightNum() * EIGHTPLAYTIME);
                            channel.noteOff(mapa.get(note.getC()).midival, DEFAULT_VELOCITY);
                            if(keyboard!=null){
                                keyboard.release(note.getC());
                            }
                        }

                        synchronized (this) {
                            if(working) {
                                comp.movebyone();
                            }
                        }
                    }
                    synchronized (this) {
                        working = false;
                        notifyAll();
                    }

                }
            } catch (InterruptedException e) {
            }

        }

        synchronized public void playcomp() {
            if (working && !paused) {
                return;
            } else if (working && paused) {
                unpause();
                return;
            } else {
                working = true;
                paused = false;
                notifyAll();
            }
        }

        synchronized public void pause() {
            paused = true;
            notifyAll();
        }

        synchronized public void unpause() {
            paused = false;
            notifyAll();
        }

        synchronized public void reset() {
            working = false;
            unpause();
            comp.reset();
        }
        @Override
        public void interrupt() {
            working = false;
            super.interrupt();
        }

    }

}