package piano;

import java.util.ArrayList;

/**
 * Chord
 */
public class Chord extends MusicSymbol {
    private ArrayList<Character> arr;

    public Chord(Duration dd,ArrayList<Character> arrr){
        super(dd);
        arr = arrr;
    }

    @Override
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        s.append("Chord: ");
        for (var x : arr) {
            s.append(x);
            s.append(" ");
        }
        s.append(super.toString());

        return s.toString();
    }
}