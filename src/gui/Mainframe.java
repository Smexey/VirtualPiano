package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;

import piano.Piano;

/**
 * Mainframe
 */
public class Mainframe extends JFrame {

    private static final long serialVersionUID = 1L;
    private Keyboard keyboard;

    private Piano piano;

    public Mainframe(String name) throws MidiUnavailableException {
        super(name);
        setSize(new Dimension(300, 200));
        setLayout(new BorderLayout());

        
        
        piano = new Piano();
        keyboard = new Keyboard(piano);
        piano.setkeyboard(keyboard);

        piano.loadmap(Piano.DEFAULT_MAP_PATH);
        piano.loadComp("C:\\Users\\Pyo\\Desktop\\VirtualPiano\\input\\fur_elise.txt");

        add(piano, BorderLayout.CENTER);

        add(keyboard, BorderLayout.SOUTH);
        addMouseListener(keyboard);
        addKeyListener(keyboard);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //saving files frame

                piano.stop();
                dispose();
            }
        });

        initmenu();
    }

    private void initmenu(){
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);


        JMenu options = new JMenu("Composition");
        menubar.add(options);

        JMenuItem menuitem = new JMenuItem("Play");
        menuitem.addActionListener(e->{
            piano.playcomp();
        });
        options.add(menuitem);

        menuitem = new JMenuItem("Pause");
        menuitem.addActionListener(e->{
            piano.pause();
        });
        options.add(menuitem);

        menuitem = new JMenuItem("Reset");
        menuitem.addActionListener(e->{
            piano.reset();
        });
        options.add(menuitem);

        options = new JMenu("Modes");

        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem menucheck = new JRadioButtonMenuItem("Autoplay",true);
        group.add(menucheck);
        options.add(menucheck);

        menucheck.addActionListener(e->{
            piano.setmode(Piano.Modes.AUTOPLAY);
        });

        menucheck = new JRadioButtonMenuItem("Game",false);
        group.add(menucheck);
        options.add(menucheck);

        menucheck.addActionListener(e->{
            piano.setmode(Piano.Modes.GAME);
        });

        menucheck = new JRadioButtonMenuItem("Record",false);
        group.add(menucheck);
        options.add(menucheck);

        menucheck.addActionListener(e->{
            piano.setmode(Piano.Modes.RECORD);
        });

        menubar.add(options);

        options = new JMenu("Options");
        JCheckBoxMenuItem chkboxmenuitem = new JCheckBoxMenuItem("Show notes",true);
        chkboxmenuitem.addActionListener(e->{
            keyboard.setLabels(chkboxmenuitem.getState());
        });
        options.add(chkboxmenuitem);

        menubar.add(options);
    }


    public static void main(String[] args) {
        try {
            Mainframe frame = new Mainframe("Mužiža");
            frame.setSize(new Dimension(1000, 500));
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        } catch (MidiUnavailableException e) {
            System.out.println(e);
        }

    }

}
