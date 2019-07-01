package piano;


/**
 * Pause
 */
public class Pause extends MusicSymbol {


    public Pause(Duration dd){
        super(dd);
    }

    @Override
    public String toString() {
        return "Pause: "+super.toString();
    }
    
}