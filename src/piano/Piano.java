package piano;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.JPanel;

import gui.Keyboard;
import piano.MusicSymbol.Duration;

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
    protected static final long EIGHTPLAYTIME = 1000 / 8;

    private Keyboard keyboard;

    public static enum Modes {
        GAME, AUTOPLAY, RECORD;
    }

    private Modes mode = Modes.AUTOPLAY;

    public void setmode(Modes m) {
        if (mode.equals(m))
            return;

        if (m.equals(Modes.GAME)) {
            swaptogame();
        } else if (m.equals(Modes.AUTOPLAY)) {
            swaptoautoplay();
        } else {
            swaptorecord();
        }
    }

    public void setkeyboard(Keyboard k) {
        keyboard = k;
    }

    private void swaptogame() {
        player.reset();
        mode = Modes.GAME;
        game.reset();
    }

    private void swaptoautoplay() {
        comp.reset();
        mode = Modes.AUTOPLAY;
    }

    private void swaptorecord() {
        player.reset();
        mode = Modes.RECORD;

    }

    protected static class MidiNoteInfo {
        boolean isplaying = false;
        String name;
        int midival;
        long t_start;

        public MidiNoteInfo(String n, int midivall) {
            midival = midivall;
            name = n;
        }

    }

    protected Map<Character, MidiNoteInfo> mapa;
    private Composition comp;

    private MidiChannel channel;

    private Player player = new Player();
    private Game game;

    public String mapChartoStr(char c) {
        MidiNoteInfo s = mapa.get(c);
        if ((s = mapa.get(c)) != null)
            return s.name;
        return null;
    }

    public Piano(int instrument) throws MidiUnavailableException {
        setBackground(Color.cyan);
        channel = getChannel(instrument);
        setLayout(new GridLayout(0, 1));
        game = new Game();
        player.start();

        comp = new Composition();
        comp.printStrings(compstrings);
        add(comp);
        comp.setPiano(this);
        revalidate();

        repaint();
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
        comp.setPiano(this);
        add(comp);
        revalidate();
        repaint();
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
            if (mode.equals(Modes.RECORD)) {
                recordnote(c, p);
            }
        }
    }

    private void play(MidiNoteInfo m) {
        channel.noteOn(m.midival, DEFAULT_VELOCITY);
        m.t_start = System.currentTimeMillis();
        m.isplaying = true;
        if (mode.equals(Modes.GAME))
            game.play(m);
    }

    private void release(MidiNoteInfo m) {
        channel.noteOff(m.midival, DEFAULT_VELOCITY);
        m.isplaying = false;
        // logika pomeranja udesno composicije
        if (mode.equals(Modes.GAME))
            game.release(m);

    }

    private boolean compstrings = false;

    public void setCompStrings(boolean b) {
        compstrings = b;
        comp.printStrings(compstrings);
    }

    public void stop() {
        channel.allNotesOff();
        player.interrupt();
    }

    synchronized public void pause() {
        if (!mode.equals(Modes.AUTOPLAY))
            return;
        player.pause();
    }

    synchronized public void unpause() {
        if (!mode.equals(Modes.AUTOPLAY))
            return;
        player.unpause();
    }

    synchronized public void reset() {
        if (!mode.equals(Modes.AUTOPLAY))
            return;
        player.reset();
    }

    /////////////////////// RECORD///////////////////////////

    private boolean recording = true;
    private long lastt = 0;// krece od nule

    public void startrecord() {
        if (!mode.equals(Modes.RECORD))
            return;

        remove(comp);
        comp = new Composition();
        comp.printStrings(compstrings);
        add(comp);
        comp.setPiano(this);
        revalidate();
        repaint();
        recording = true;
    }

    public void endrecord() {
        if (!mode.equals(Modes.RECORD))
            return;
        if (recording != true)
            return;

        lastt = 0;
        recording = false;
        comp.save();
    }

    private void recordnote(char c, MidiNoteInfo m) {
        // if (System.currentTimeMillis() - m.t_start < EIGHTPLAYTIME)
        // return;
        //logika za chordove?

        if (lastt != 0) {
            // dodaj pauze
            long t = System.currentTimeMillis() - lastt;
            long n = t / EIGHTPLAYTIME;
            for (int i = 0; i < n / 2; i++) {
                // dodaj cetvrtine
                comp.insert(new Pause(Duration.QUART));
            }
            if (n % 2 == 1) {
                comp.insert(new Pause(Duration.EIGHT));
            }
        }
        // dodaj notu
        long t = System.currentTimeMillis() - m.t_start;

        long n = t / EIGHTPLAYTIME;
        System.out.println(t + " " + n);
        for (int i = 0; i < n / 2; i++) {
            // dodaj cetvrtine
            comp.insert(new Note(c, Duration.QUART));
        }
        if (n % 2 == 1 || n==0)
            comp.insert(new Note(c, Duration.EIGHT));
        comp.repaint();
        lastt = System.currentTimeMillis();
    }

    private class Game {
        private MusicSymbol currsym;
        private static final double ERRORPERCENT = 0.8;// +- delta

        public void play(MidiNoteInfo m) {

        }

        public void release(MidiNoteInfo m) {
            m.isplaying = false;
            if (currsym instanceof Note) {
                Note sym = (Note) currsym;
                if (checkchar(sym.getC(), m)) {
                    comp.movebyone();
                    getnextsym();
                }

            } else if (currsym instanceof Chord) {
                Chord sym = (Chord) currsym;
                boolean flag = true;
                for (char c : sym.arr) {
                    MidiNoteInfo inf = mapa.get(c);
                    if (!checkchar(c, inf)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    comp.movebyone();
                    getnextsym();
                }
            }
        }

        public void reset() {
            comp.reset();
            getnextsym();
        }

        private void getnextsym() {
            while (true) {
                MusicSymbol s = comp.getfirst();
                if (s == null) {
                    comp.reset();
                    currsym = comp.getfirst();
                    break;
                } else {
                    if (!(s instanceof Pause)) {
                        currsym = comp.getfirst();
                        break;
                    }
                    // ako je pauza ide ispocetka
                    comp.movebyone();
                }
            }
        }

        private boolean checkchar(char c, MidiNoteInfo m) {
            String name = mapChartoStr(c);
            if (m.name.equals(name)) {
                long t = System.currentTimeMillis() - m.t_start;
                long dur = currsym.getDur().getEightNum() * EIGHTPLAYTIME;

                if (t > (dur - dur * ERRORPERCENT) && t < (dur + dur * ERRORPERCENT)) {
                    // onda je u opsegu
                    return true;
                }
            }
            return false;
        }

    }

    private class Player extends Thread {

        private boolean working = false;
        private boolean paused = false;

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
                            if (!working) {
                                break;
                            }

                        }

                        if (sym instanceof Chord) {
                            Chord chord = (Chord) sym;
                            for (char c : chord.arr) {
                                channel.noteOn(mapa.get(c).midival, DEFAULT_VELOCITY);
                                if (keyboard != null) {
                                    keyboard.press(c);
                                }
                            }
                            sleep(sym.getDur().getEightNum() * EIGHTPLAYTIME);
                            for (char c : chord.arr) {
                                channel.noteOff(mapa.get(c).midival, DEFAULT_VELOCITY);
                                if (keyboard != null) {
                                    keyboard.release(c);
                                }
                            }
                        } else if (sym instanceof Pause) {
                            sleep(sym.getDur().getEightNum() * EIGHTPLAYTIME);
                        } else {
                            Note note = (Note) sym;
                            channel.noteOn(mapa.get(note.getC()).midival, DEFAULT_VELOCITY);
                            if (keyboard != null) {
                                keyboard.press(note.getC());
                            }
                            sleep(sym.getDur().getEightNum() * EIGHTPLAYTIME);
                            channel.noteOff(mapa.get(note.getC()).midival, DEFAULT_VELOCITY);
                            if (keyboard != null) {
                                keyboard.release(note.getC());
                            }
                        }

                        synchronized (this) {
                            if (working) {
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

    private Midi midifileformatter = new Midi();

    public void savetomidi(String name) {
        midifileformatter.printto(comp, name);
    }

    private Text textfileformatter = new Text();

    public void savetotxt(String name) {
        textfileformatter.printto(comp, name);
    }

}