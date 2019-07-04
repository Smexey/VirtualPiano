package piano;

/**
 * NoteOutputer
 */
public interface NoteOutputer {

    public void press(char c);
    public void release(char c);
}