package piano;

import java.util.ArrayList;

/**
 * Chord
 */
public class Chord extends MusicSymbol {
    public ArrayList<Character> arr;

    public Chord(Duration dd,ArrayList<Character> arrr){
        super(dd);
        arr = arrr;
    }

    @Override
    public String toString() {
        
        StringBuilder s = new StringBuilder();
        s.append("[");

        arr.stream().forEach(x->s.append(x));
        s.append("]");

        return s.toString();
    }

}