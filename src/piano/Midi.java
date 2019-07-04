
package piano;

import java.util.ArrayList;
import java.util.Map;

import javax.sound.midi.*;
import java.io.*;

/**
 * Midi
 */
public class Midi implements FileFormatter {

    private ShortMessage mm;
    private int current = 0;
    private MidiEvent event;

    private static final int SPEED = 48 * 3;

    @Override
    public void printto(Composition comp, String path) {
        try {
            Map<Character, Piano.MidiNoteInfo> mapa = comp.mypiano.mapa;
            // **** Create a new MIDI sequence with 24 ticks per beat ****
            Sequence s = new Sequence(javax.sound.midi.Sequence.PPQ, SPEED);

            // **** Obtain a MIDI track from the sequence ****
            Track t = s.createTrack();

            // **** General MIDI sysex -- turn on General MIDI sound set ****
            byte[] b = { (byte) 0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte) 0xF7 };
            SysexMessage sm = new SysexMessage();
            sm.setMessage(b, 6);
            MidiEvent me = new MidiEvent(sm, (long) 0);
            t.add(me);

            // **** set tempo (meta event) ****
            MetaMessage mt = new MetaMessage();
            byte[] bt = { 0x02, (byte) 0x00, 0x00 };
            mt.setMessage(0x51, bt, 3);
            me = new MidiEvent(mt, (long) 0);
            t.add(me);

            // **** set track name (meta event) ****
            mt = new MetaMessage();
            String TrackName = new String("midifile track");
            mt.setMessage(0x03, TrackName.getBytes(), TrackName.length());
            me = new MidiEvent(mt, (long) 0);
            t.add(me);

            // **** set omni on ****
            mm = new ShortMessage();
            mm.setMessage(0xB0, 0x7D, 0x00);
            me = new MidiEvent(mm, (long) 0);
            t.add(me);

            // **** set poly on ****
            mm = new ShortMessage();
            mm.setMessage(0xB0, 0x7F, 0x00);
            me = new MidiEvent(mm, (long) 0);
            t.add(me);

            // **** set instrument to Piano ****
            mm = new ShortMessage();
            mm.setMessage(0xC0, 0x00, 0x00);
            me = new MidiEvent(mm, (long) 0);
            t.add(me);

            ArrayList<MusicSymbol> red = comp.getPartCopy();
            red.stream().forEach(l -> {
                try {

                    if (l instanceof Note) {
                        Note nota = (Note) l;
                        mm = new ShortMessage();
                        mm.setMessage(0x90, mapa.get(nota.getC()).midival, 0x60);
                        event = new MidiEvent(mm, (long) current);
                        t.add(event);

                        current += nota.getDur().getEightNum() * Piano.EIGHTPLAYTIME;

                        mm = new ShortMessage();
                        mm.setMessage(0x80, mapa.get(nota.getC()).midival, 0x40);
                        event = new MidiEvent(mm, (long) current);
                        t.add(event);
                    } else if (l instanceof Chord) {
                        Chord akord = (Chord) l;

                        for (char c : akord.arr) {
                            mm = new ShortMessage();
                            mm.setMessage(0x90, mapa.get(c).midival, 0x60);
                            event = new MidiEvent(mm, (long) current);
                            t.add(event);
                        }
                        current += akord.getDur().getEightNum() * Piano.EIGHTPLAYTIME;

                        for (char c : akord.arr) {
                            mm = new ShortMessage();
                            mm.setMessage(0x80, mapa.get(c).midival, 0x40);
                            event = new MidiEvent(mm, (long) current);
                            t.add(event);
                        }

                    } else
                        current += l.getDur().getEightNum() * Piano.EIGHTPLAYTIME;

                } catch (Exception e) {
                }
            });

            // **** set end of track (meta event) 19 ticks later ****
            mt = new MetaMessage();
            byte[] bet = {}; // empty array
            mt.setMessage(0x2F, bet, 0);
            me = new MidiEvent(mt, (long) 140);
            t.add(me);

            // **** write the MIDI sequence to a MIDI file ****
            File f = new File(path);
            MidiSystem.write(s, 1, f);
        } // try
        catch (Exception e) {
            System.out.println(e);
        } // catch
    }

}