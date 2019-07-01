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
public class Composition {
    // private static final long serialVersionUID = -6969907616327729934L;

    private ArrayList<MusicSymbol> part = new ArrayList<>();

    public Composition(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        List<String> allMatches = new ArrayList<String>();
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;
            while ((text = reader.readLine()) != null) {

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

        //ubacivanje nota u arraylist
        makepart(allMatches);
    }

    private void makepart(List<String> allMatches){
        for (String str : allMatches) {
            if (str.length() == 1) {
                if (str.equals(" ")) {
                    // pauza 1/4
                    part.add(new Pause(MusicSymbol.Duration.EIGHT));
                } else if (str.equals("|")) {
                    // pauza 1/8
                    part.add(new Pause(MusicSymbol.Duration.QUART));
                } else {
                    // regularna nota 1/4
                    part.add(new Note(str.charAt(0),MusicSymbol.Duration.QUART));
                }
            } else {
                //chord ili brzi
                if (!(str.charAt(2)!= ' ')) {
                    //brzi
                    //dodaj sve kao zasebne osmina note
                    for (int i = 1; i < str.length(); i+=2) 
                        part.add(new Note(str.charAt(i),MusicSymbol.Duration.EIGHT));

                }else{
                    //chord
                    ArrayList<Character> arr = new ArrayList<>();
                    for (int i = 1; i < str.length()-1; i++) {
                        arr.add(str.charAt(i));
                    }
                    part.add(new Chord(MusicSymbol.Duration.QUART, arr));
                }
            }
        }
    }

    public static void main(String[] args) {
        new Composition("C:\\Users\\Pyo\\Desktop\\VirtualPiano\\input\\test.txt");
    }

}