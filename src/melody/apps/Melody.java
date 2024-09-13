package melody.apps;

import melody.audio.Note;
import java.io.*;
import ADTs.*;
import DataStructures.*;
import melody.audio.Pitch;

public class Melody {
    QueueADT<Note> song;
    String songTitle;
    String songComposers;
    int numOfLines;
    double totalDuration;
    final static int MIN_OCTAVE = 1;
    final static int MAX_OCTAVE = 10;
    final static int MAX_REPEATS = 2;
    final static int ARRAY_BLOCKING_QUEUE_INIT_CAPACITY = 100;
    
    public Melody(QueueADT<Note> song, 
    String title, String artists, int lineNum) {
        this.song = song;
        this.totalDuration = 0;
        this.songTitle = title;
        this.numOfLines = lineNum;
        this.songComposers = artists;
    }

    public void changeTempo(double ratio) {
        if (this.song == null) return;
        int numOfNotes = this.song.size();
        Note note;
        for (int i = 0; i < numOfNotes; i++){
            note = this.song.poll();
            note.setDuration(ratio * note.getDuration());
            this.song.add(note);
        }
        this.totalDuration *= ratio;
    }

    public double getTotalDuration() {
        if (this.song == null) return 0;
        if (this.totalDuration == 0){
            boolean isRepeated = false;
            int repeatCount = 0;
            int numOfNotes = this.song.size();
            QueueADT<Note> repeatQueue = null;
            Note note;
            QueueADT<Note> tmpQueue = this.song;
            for (int i = 0; i < numOfNotes; i++){
                note = tmpQueue.poll();
                this.totalDuration += note.getDuration();
                tmpQueue.add(note);
                if (repeatCount != 0)
                    numOfNotes++;
                if ((! isRepeated) && note.isRepeat()){
                    repeatQueue = new ArrayCircularQueue<Note>();
                    isRepeated = !isRepeated;
                    repeatQueue.add(note);
                }else if (isRepeated && note.isRepeat()){
                    repeatCount++;
                    if (repeatCount >= Melody.MAX_REPEATS){
                        tmpQueue = this.song;
                        isRepeated = false;
                        repeatCount = 0;
                    }
                }else if (isRepeated && (! note.isRepeat()))
                    repeatQueue.add(note);
            }
        }
        return this.totalDuration;
    }

    public boolean octaveDown() {
        if (this.song == null) return false;
        Note note;
        boolean isInvalid = false;
        int numOfNotes = this.song.size();
        for (int i = 0; i < numOfNotes; i++){
            note = this.song.poll();
            if (note.getPitch() != Pitch.R && note.getOctave() == Melody.MIN_OCTAVE)
                isInvalid = true;
            this.song.add(note);
        }
        if (isInvalid) return false;
        
        for (int i = 0; i < numOfNotes; i++){
            note = this.song.poll();
            if (note.getPitch() != Pitch.R)
                note.setOctave(note.getOctave()-1);
            this.song.add(note);
        }
        return true;
    }

    public boolean octaveUp() {
        if (this.song == null) return false;
        Note note;
        boolean isInvalid = false;
        int numOfNotes = this.song.size();

        for (int i = 0; i < numOfNotes; i++){
            note = this.song.poll();
            if (note.getPitch() != Pitch.R && note.getOctave() == Melody.MAX_OCTAVE)
                isInvalid = true;
            this.song.add(note);
        }
        if (isInvalid) return false;
        
        for (int i = 0; i < numOfNotes; i++){
            note = this.song.poll();
            if (note.getPitch() != Pitch.R)
                note.setOctave(note.getOctave()+1);
            this.song.add(note);
        }
        return true;
    }
    
    public void play() {
        if (this.song == null) return;
        boolean isRepeated = false;
        int repeatCount = 0;
        int numOfNotes = this.song.size();
        QueueADT<Note> repeatQueue = null;
        Note note;
        QueueADT<Note> tmpQueue = this.song;
        for (int i = 0; i < numOfNotes; i++){
            note = tmpQueue.poll();
            note.play();
            tmpQueue.add(note);
            if (repeatCount != 0)
                numOfNotes++;
            if ((! isRepeated) && note.isRepeat()){
                repeatQueue = new ArrayCircularQueue<Note>();
                isRepeated = !isRepeated;
                repeatQueue.add(note);
            }else if (isRepeated && note.isRepeat()){
                repeatCount++;
                if (repeatCount >= Melody.MAX_REPEATS){
                    tmpQueue = this.song;
                    isRepeated = false;
                    repeatCount = 0;
                }
            }else if (isRepeated && (! note.isRepeat()))
                repeatQueue.add(note);
        }
        return ;
    }

    /**
     * Adds all notes from the given other song to the end of song
     */
    public void append(Melody other){
        if (other == null) return ;
        if (this.song == null)
            this.song = new LinkedQueue<Note>();
        
        Note note;
        boolean isInvalid = false;
        int numOfNotes = other.song.size();
        for (int i = 0; i < numOfNotes; i++){
            note = other.song.poll();
            this.song.add(note);
            other.song.add(note);
        }
        this.totalDuration += other.getTotalDuration();
    }
    /**
     * Reverses the order of notes in the song
     */
    public void reverse() {
        if (this.song == null) return;
        int numOfNotes = this.song.size();
        StackADT<Note> songStack = new ArrayStack<Note>();
        Note note = null;
        while (! this.song.isEmpty()){
            note = this.song.poll();
            if (note != null)
                songStack.push(note);
        }
        
        try{
            while (! songStack.isEmpty()){
                note = songStack.pop();
                this.song.add(note);
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String getTitle() {
        if (this.song == null) return null;
        return this.songTitle;
    }
    
    public String getArtist() {
        if (this.song == null) return null;
        return this.songComposers;
    }    
    
    public String toString() {
        if (this.song == null) return "";
        int numOfNotes = this.song.size();
        Note[] notes = new Note[numOfNotes];
        Note note;
        for (int i = 0; i < numOfNotes; i++){
            note = this.song.poll();
            notes[i] = note;
            this.song.add(note);
        }
        return java.util.Arrays.toString(notes);
    }
}

