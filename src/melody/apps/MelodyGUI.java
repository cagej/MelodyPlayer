package melody.apps;

import melody.audio.StdAudio;
import melody.gui.GuiUtils;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import melody.audio.*;
import ADTs.*;
import DataStructures.*;

public class MelodyGUI implements ActionListener, StdAudio.AudioEventListener {
    // constants for important directories used by the program
    private static final String RESOURCE_FOLDER = "res";
    private static final String ICONS_FOLDER = "icons";
    private static final String MELODY_FOLDER = "melodies";
    private static final String TITLE = "Melody Player";

    // fields
    private Melody melody;
    private boolean playing; // whether a song is currently playing
    private JFrame frame;
    private Container overallLayout;
    private JLabel statusLabel;
    private JButton playButton; // buttons in the GUI
    private JButton pauseButton;
    private JButton stopButton;
    private JButton loadButton;
    private JButton appendButton;
    private JButton reverseButton;
    private JButton changeTempoButton;
    private JButton octaveUpButton;
    private JButton octaveDownButton;
    private JFileChooser fileChooser; // for loading files
    private JFileChooser fileChooser4Append; // for appending files 
    private JSlider currentTimeSlider; // displays current time in the song
    private JLabel currentTimeLabel;
    private JLabel totalTimeLabel;
    private JSpinner changeTempoSpinner; // numeric duration field

    /*
     * Creates the melody player GUI window and graphical components.
     */
    public MelodyGUI() {
            melody = null;
            createComponents();
            doLayout();
            StdAudio.addAudioEventListener(this);
            frame.setVisible(true);
    }

    /*
     * Called when the user interacts with graphical components, such as
     * clicking on a button.
     */
    public void actionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand().intern();
        if (cmd == "Play") {
            playMelody();
        } else if (cmd == "Pause") {
            StdAudio.setPaused(!StdAudio.isPaused());
        } else if (cmd == "Stop") {
            StdAudio.setMute(true);
            StdAudio.setPaused(false);
        } else if (cmd == "Load") {
            try {
                loadFile();
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(frame, "I/O error: " + ioe.getMessage(), "I/O error",
                JOptionPane.ERROR_MESSAGE);
            }
        } else if (cmd == "Append") {
            try {
                appendFile();
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(frame, "I/O error: " + ioe.getMessage(), "I/O error",
                JOptionPane.ERROR_MESSAGE);
            }
        } else if (cmd == "Reverse") {
            if (melody != null) {
                System.out.println("Reversing.");
                melody.reverse();
                System.out.println("Melody: " + melody);
            }
        } else if (cmd == "Up") {
            if (melody != null) {
                System.out.println("Octave up.");
                if (!melody.octaveUp()) {
                    JOptionPane.showMessageDialog(frame,
                                    "Can't go up an octave; maximum octave reached.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
                }
                System.out.println("Melody: " + melody);
            }
        } else if (cmd == "Down") {
            if (melody != null) {
                System.out.println("Octave down.");
                if (!melody.octaveDown()) {
                    JOptionPane.showMessageDialog(frame,
                                    "Can't go down an octave; minimum octave reached.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
                }
                System.out.println("Melody: " + melody);
            }
        } else if (cmd == "Change Tempo") {
            if (melody != null) {
                try {
                    double ratio = ((Double) changeTempoSpinner.getValue()).doubleValue();
                    System.out.println("Change tempo by " + ratio + ".");
                    melody.changeTempo(ratio);
                    updateTotalTime();
                    System.out.println("Melody: " + melody);
                } catch (NumberFormatException nfe) {
                            // empty
                }
            }
        }
    }

    public void onAudioEvent(StdAudio.AudioEvent event) {
        // update current time
        if (event.getType() == StdAudio.AudioEvent.Type.PLAY
                || event.getType() == StdAudio.AudioEvent.Type.STOP) {
            setCurrentTime(getCurrentTime() + event.getDuration());
        }
    }

    /*
     * Sets up the graphical components in the window and event listeners.
     */
    private void createComponents() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
                
        }
        frame = new JFrame(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        String currentDir = System.getProperty("user.dir") + File.separator + RESOURCE_FOLDER
                        + File.separator + MELODY_FOLDER;
        if (!(new File(currentDir).exists())) {
                currentDir = System.getProperty("user.dir");
        }
        fileChooser = new JFileChooser(currentDir);
        fileChooser.setFileFilter(GuiUtils.getExtensionFileFilter("Text files (*.txt)", "txt"));
        fileChooser4Append = new JFileChooser(currentDir);
        fileChooser4Append.setFileFilter(GuiUtils.getExtensionFileFilter("Text files (*.txt)", "txt"));

        statusLabel = new JLabel("Welcome to the Melody Player!");

        String icon = RESOURCE_FOLDER + File.separator + ICONS_FOLDER + File.separator;
        playButton = GuiUtils.createButton("Play", icon + "play.gif", 'P', this);
        pauseButton = GuiUtils.createButton("Pause", icon + "pause.gif", 'a', this);
        stopButton = GuiUtils.createButton("Stop", icon + "stop.gif", 'S', this);
        loadButton = GuiUtils.createButton("Load", icon + "load.gif", 'L', this);
        appendButton = GuiUtils.createButton("Append", icon + "append.gif", 'L', this);

        reverseButton = GuiUtils.createButton("Reverse", icon + "reverse.gif", 'R', this);
        octaveUpButton = GuiUtils.createButton("Up", icon + "up.gif", 'U', this);
        octaveDownButton = GuiUtils.createButton("Down", icon + "down.gif", 'D', this);
        changeTempoButton = GuiUtils.createButton("Change", icon + "lightning.gif", 'h', this);
        changeTempoButton.setActionCommand("Change Tempo");
        changeTempoSpinner = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 9.9, 0.1));

        currentTimeSlider = new JSlider(/* min */0, /* max */100);
        currentTimeSlider.setValue(0);
        currentTimeSlider.setMajorTickSpacing(10);
        currentTimeSlider.setMinorTickSpacing(5);
        currentTimeSlider.setPaintLabels(false);
        currentTimeSlider.setPaintTicks(true);
        currentTimeSlider.setSnapToTicks(false);
        currentTimeSlider.setPreferredSize(new Dimension(300,
        currentTimeSlider.getPreferredSize().height));
        currentTimeLabel = new JLabel("000000.0 /");
        totalTimeLabel = new JLabel("000000.0 sec");

        JSpinner.NumberEditor editor = (JSpinner.NumberEditor) changeTempoSpinner.getEditor();
        DecimalFormat format = editor.getFormat();
        format.setMinimumFractionDigits(1);
        changeTempoSpinner.setValue(1.0);
        doEnabling();
    }

    /*
     * Sets whether every button, slider, spinner, etc. should be currently
     * enabled, based on the current state of whether a song has been loaded and
     * whether or not it is currently playing. This is done to prevent the user
     * from doing actions at inappropriate times such as clicking play while the
     * song is already playing, etc.
     */
    private void doEnabling() {
        playButton.setEnabled(melody != null && !playing);
        pauseButton.setEnabled(melody != null && playing);
        stopButton.setEnabled(melody != null && playing);
        loadButton.setEnabled(!playing);
        appendButton.setEnabled(melody != null && !playing);
        currentTimeSlider.setEnabled(false);
        reverseButton.setEnabled(melody != null && !playing);
        octaveUpButton.setEnabled(melody != null && !playing);
        octaveDownButton.setEnabled(melody != null && !playing);
        changeTempoButton.setEnabled(melody != null && !playing);
        changeTempoSpinner.setEnabled(melody != null && !playing);
    }

    /*
     * Performs layout of the components within the graphical window. Also
     * resizes the window snugly and centers it on the screen.
     */
    private void doLayout() {
        overallLayout = Box.createVerticalBox();
        ImageIcon logoIcon = new ImageIcon(RESOURCE_FOLDER
                        + File.separator + ICONS_FOLDER + File.separator + "notes.gif");
        overallLayout.add(GuiUtils.createPanel(new JLabel(logoIcon)));
        overallLayout.add(GuiUtils.createPanel(statusLabel));
        overallLayout.add(GuiUtils.createPanel(currentTimeSlider,
                        GuiUtils.createPanel(new GridLayout(2, 1), currentTimeLabel, totalTimeLabel)));
        overallLayout.add(GuiUtils.createPanel(playButton, stopButton, loadButton, appendButton));
        overallLayout.add(GuiUtils.createPanel(reverseButton, octaveUpButton, octaveDownButton));
        overallLayout.add(GuiUtils.createPanel(new JLabel("Tempo: "), changeTempoSpinner,
                        changeTempoButton));
        frame.setContentPane(overallLayout);
        frame.pack();
        GuiUtils.centerWindow(frame);
    }

    /*
     * Returns the estimated current time within the overall song, in seconds.
     */
    private double getCurrentTime() {
        String timeStr = currentTimeLabel.getText();
        timeStr = timeStr.replace(" /", "");
        try {
            return Double.parseDouble(timeStr);
        } catch (NumberFormatException nfe) {
            return 0.0;
        }
    }

    /*
     * Pops up a file-choosing window for the user to select a song file to be
     * loaded. If the user chooses a file, a Melody object is created and used
     * to represent that song.
     */
    private void loadFile() throws IOException {
        if (fileChooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selected = fileChooser.getSelectedFile();
        if (selected == null) {
            return;
        }
        statusLabel.setText("Current song: " + selected.getName());
        String filename = selected.getAbsolutePath();
        System.out.println("Loading melody from " + selected.getName() + " ...");
        QueueADT<Note> queue = MelodyMain.read(filename);
        melody = new Melody(queue, "","", queue.size());
        changeTempoSpinner.setValue(1.0);
        setCurrentTime(0.0);
        updateTotalTime();
        System.out.println("Loading complete.");
        System.out.println("Melody: " + melody);
        doEnabling();
    }

    /*
     * Pops up a file-choosing window for the user to select a song file to be
     * loaded. If the user chooses a file, a Melody object is created and used
     * to represent that song.
     */
    private void appendFile() throws IOException {
        if (fileChooser4Append.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
                return;
        }
        File selected = fileChooser4Append.getSelectedFile();
        if (selected == null) {
            return;
        }
        statusLabel.setText("Current song: " + selected.getName());
        String filename = selected.getAbsolutePath();
        System.out.println("Loading melody from " + selected.getName() + " ...");
        QueueADT<Note> queue = MelodyMain.read(filename);
        Melody melody2 = new Melody(queue, "","", queue.size());
        melody.append(melody2);
        changeTempoSpinner.setValue(1.0);
        setCurrentTime(0.0);
        updateTotalTime();
        System.out.println("Appending complete.");
        System.out.println("Melody: " + melody);
        doEnabling();
    }
    
    /*
     * Initiates the playing of the current song Melody in a separate thread
     */
    private void playMelody() {
        if (melody != null) {
            setCurrentTime(0.0);
            Thread playThread = new Thread(new Runnable() {
                public void run() {
                    StdAudio.setMute(false);
                    playing = true;
                    doEnabling();
                    String title = melody.getTitle();
                    String artist = melody.getArtist();
                    double duration = melody.getTotalDuration();
                    System.out.println("Playing \"" + title
                                    + "\", by " + artist + " (" + duration + " sec)");
                    melody.play();
                    System.out.println("Playing complete.");
                    playing = false;
                    doEnabling();
                }
            });
            playThread.start();
        }
    }

    /*
     * Sets the current time display slider/label to show the given time in
     * seconds. Bounded to the song's total duration as reported by Melody.
     */
    private void setCurrentTime(double time) {
        double total = melody.getTotalDuration();
        time = Math.max(0, Math.min(total, time));
        currentTimeLabel.setText(String.format("%08.2f /", time));
        currentTimeSlider.setValue((int) (100 * time / total));
    }

    /*
     * Updates the total time label on the screen to the current total duration.
     */
    private void updateTotalTime() {
        totalTimeLabel.setText(String.format("%08.2f sec", melody.getTotalDuration()));
    }
}
