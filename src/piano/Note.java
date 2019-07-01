package piano;

/**
 * Note
 */
public class Note extends MusicSymbol {

    private char c;

    public Note(char cc, Duration dd) {
        super(dd);
        c = cc;
    }

    public char getC(){
        return c;
    }


    @Override
    public String toString() {
        return "Note: " + c +" "+super.toString();
    }

}