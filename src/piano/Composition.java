package piano;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Composition
 */
public class Composition extends ArrayList<Measure> {
    private static final long serialVersionUID = -6969907616327729934L;

    public Composition(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                List<String> allMatches = new ArrayList<String>();
                Matcher m = Pattern.compile("(\\[[^\\]]*\\]|[^\\[])").matcher(text);
                while (m.find()) {
                  allMatches.add(m.group());
                }

            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }

    }

    public static void main(String[] args) {
        new Composition("C:\\Users\\Pyo\\Desktop\\VirtualPiano\\input\\test.txt");
    }

}