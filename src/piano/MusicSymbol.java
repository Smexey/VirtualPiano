
package piano;

/**
 * MusicSymbol
 */
public abstract class MusicSymbol {

    private Duration d;

    public static enum Duration {
        EIGHT(1), QUART(2);
        private final int number;
        private Duration(int number) {
            this.number = number;
        }
        public int getEightNum() {
            return number;
        }
    }

    public MusicSymbol(Duration dd) {
        d = dd;
    }

    public Duration getDur(){
        return d;
    }

    @Override
    public String toString() {
        return d.name();
    }


}