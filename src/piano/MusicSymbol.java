
package piano;

/**
 * MusicSymbol
 */
public class MusicSymbol {

    private Duration d;

    public static enum Duration {
        QUART(1), HALF(2);
        private final int number;
        private Duration(int number) {
            this.number = number;
        }
        public int getQuarterNum() {
            return number;
        }
    }

    public MusicSymbol(Duration dd) {
        d = dd;
    }

    public Duration getDur(){
        return d;
    }

}