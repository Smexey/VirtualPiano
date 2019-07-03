package piano;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Text
 */
public class Text implements FileFormatter {
    public boolean openedeightnotesection = false;
    private BufferedWriter writer = null;
    public boolean first = true;

    @Override
    public void printto(Composition comp, String path) {
        try {
            writer = new BufferedWriter(new FileWriter(path));
            ArrayList<MusicSymbol> red = comp.getPartCopy();
            red.stream().forEach(l -> {
                try {
                    if (openedeightnotesection)
                        writer.append(' ');
                        
                    if (l instanceof Note && l.getDur().equals(MusicSymbol.Duration.EIGHT)) {
                        // pise quick note
                        if (!openedeightnotesection) {
                            writer.append('[');
                            openedeightnotesection = true;
                        }

                    } else if (openedeightnotesection) {
                        writer.append(']');
                        openedeightnotesection = false;
                    }
                    
                    writer.append(l.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (openedeightnotesection)
                    writer.append(']');
                if (writer != null)
                    writer.close();
                writer = null;
                first = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}