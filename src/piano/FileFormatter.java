package piano;

import piano.Composition;

/**
 * FileFormatter
 */
public interface FileFormatter {

    public void printto(Composition comp, String path);
}