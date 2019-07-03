package piano;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JPanel;

/**
 * Composition
 */
public class Composition extends JPanel {
    private static final long serialVersionUID = -6969907616327729934L;

    private ArrayList<MusicSymbol> part = new ArrayList<>();
    private ArrayList<MusicSymbol> mainpart;
    private int sumdura = 0;
    private boolean symbolstringprint = false;
    protected Piano mypiano;

    private static final Color QUARTNOTECOLOR = Color.red, QUARTPAUSECOLOR = new Color(128,0,0),
            EIGHTNOTECOLOR = new Color(127, 255, 0), EIGHTPAUSECOLOR = new Color(0,100,0);


    public Composition() {

    }

    synchronized public MusicSymbol getfirst(){
        return part.get(0);
    }

    synchronized public void reset(){
        if(mainpart!=null) part = new ArrayList<>(mainpart);
        repaint();
    }

    synchronized public void save(){
        mainpart = new ArrayList<>(part);
        repaint();
    }

    synchronized public ArrayList<MusicSymbol> getPartCopy(){
        return new ArrayList<MusicSymbol>(part);
    }

    public void setPiano(Piano p){
        mypiano = p;
    }

    public void printStrings(boolean f) {
        symbolstringprint = f;
        repaint();
    }

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
        // ubacivanje nota u arraylist
        makepart(allMatches);
    }

    synchronized public void insert(MusicSymbol m){
        part.add(m);
        sumdura += m.getDur().getEightNum();
    }

    synchronized public void insert(String str) {
        if (str.length() == 1) {
            if (str.equals(" ")) {
                // pauza 1/4
                part.add(new Pause(MusicSymbol.Duration.EIGHT));
                sumdura += MusicSymbol.Duration.EIGHT.getEightNum();
            } else if (str.equals("|")) {
                // pauza 1/8
                part.add(new Pause(MusicSymbol.Duration.QUART));
                sumdura += MusicSymbol.Duration.QUART.getEightNum();
            } else {
                // regularna nota 1/4
                part.add(new Note(str.charAt(0), MusicSymbol.Duration.QUART));
                sumdura += MusicSymbol.Duration.QUART.getEightNum();
            }
        } else {
            // chord ili brzi
            if (!(str.charAt(2) != ' ')) {
                // brzi
                // dodaj sve kao zasebne osmina note
                for (int i = 1; i < str.length(); i += 2) {
                    part.add(new Note(str.charAt(i), MusicSymbol.Duration.EIGHT));
                    sumdura += MusicSymbol.Duration.EIGHT.getEightNum();
                }

            } else {
                // chord
                ArrayList<Character> arr = new ArrayList<>();
                for (int i = 1; i < str.length() - 1; i++) {
                    arr.add(str.charAt(i));
                }
                part.add(new Chord(MusicSymbol.Duration.QUART, arr));
                sumdura += MusicSymbol.Duration.QUART.getEightNum();
            }
        }
    }

    synchronized public void movebyone(){
        //oduzmem sumdura ovde da se konvergira na kraju?
        part.remove(0);
        if(part.size()==0){
            reset();
        }
        repaint();
    }

    synchronized private void makepart(List<String> allMatches) {
        for (String str : allMatches) {
            insert(str);
        }
        mainpart = new ArrayList<MusicSymbol> (part);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        double width = getWidth();
        double height = getHeight();
        double eightwidth = width / (double) (sumdura<16?sumdura:16);
        double symheight = height / 8.0;
        double y = height / 2.0 - symheight / 2.0;
        double x = 0;
        double maxy = y+symheight;

        int fontsize = (int) (eightwidth*0.3);

        g.setFont(new Font("Franklin Gothic", Font.BOLD, fontsize));

        for (MusicSymbol symbol : part) {
            MusicSymbol.Duration dur = symbol.getDur();
            g.setColor(dur.equals(MusicSymbol.Duration.EIGHT) ? EIGHTNOTECOLOR : QUARTNOTECOLOR);

            if (symbol instanceof Note) {
                g.fillRect((int) x, (int) y, (int) (dur.getEightNum() * eightwidth), (int) symheight);
                Note sym = (Note) symbol;
                g.setColor(Color.black);

                g.drawString((symbolstringprint?mypiano.mapChartoStr(sym.getC()):Character.toString(sym.getC())), (int)(x+dur.getEightNum()*eightwidth/2.0),(int) (y+symheight/2.0));
            } else if (symbol instanceof Pause) {
                g.setColor(dur.equals(MusicSymbol.Duration.EIGHT) ? EIGHTPAUSECOLOR : QUARTPAUSECOLOR);
                g.fillRect((int) x, (int) y, (int) (dur.getEightNum() * eightwidth), (int) symheight);

            } else if (symbol instanceof Chord) {
                Chord sym = (Chord) symbol;
                double chordheight = sym.arr.size() * symheight;
                double chordy = y+symheight/2.0-chordheight/2.0;

                for (int i = 0; i < sym.arr.size(); i++) {
                    g.setColor(QUARTNOTECOLOR);
                    g.fillRect((int) x, (int)chordy, (int) (dur.getEightNum() * eightwidth), (int) symheight);
                    g.setColor(Color.black);
                    g.drawString((symbolstringprint?mypiano.mapChartoStr(sym.arr.get(i)):Character.toString(sym.arr.get(i))), (int)(x+dur.getEightNum()*eightwidth/2.0),(int) (chordy+symheight/2.0));
                    chordy+=symheight;
                    if(chordy>maxy) maxy = chordy;
                }
            }
            x+=dur.getEightNum()*eightwidth;
            if(x>=16*eightwidth)break;
        }

        //docrtati linije za cetvrtine
        x=0;
        // System.out.println("comp width" + width);
        // System.out.println("eightwidth" + eightwidth);
        y = maxy;
        while(x<width){
            g.drawLine((int)x, (int)maxy, (int)x, getHeight());
            x+=2*eightwidth;
        }
    }

    public static void main(String[] args) {
        new Composition("C:\\Users\\Pyo\\Desktop\\VirtualPiano\\input\\test.txt");
    }

}