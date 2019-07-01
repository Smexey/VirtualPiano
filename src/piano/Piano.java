package piano;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import lib.Pair;

/**
 * Piano
 */
public class Piano {
    private Map<Character, Pair<String, Integer>> mapa;
    private ArrayList<Composition> comparr = new ArrayList<Composition>();

    public Piano(String path) {
        loadmap(path);
    }

    public Piano(Map<Character, Pair<String, Integer>> m) {
        mapa = m;
    }

    Map<Character, Pair<String, Integer>> getMap() {
        return mapa;
    }

    public void addNewComp(String path) {
        comparr.add(new Composition(path));
    }

    public ArrayList<Composition> getCompArr() {
        return comparr;
    }

    public void loadmap(String path) {
        mapa = new HashMap<>();
        File file = new File(path);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = null;

            while ((text = reader.readLine()) != null) {
                // ubaci u mapu taj line csv
                String[] vals = text.split(",");
                mapa.put(vals[0].charAt(0), new Pair<String, Integer>(vals[1], Integer.parseInt(vals[2])));
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

}