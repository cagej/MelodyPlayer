package melody.audio;


import melody.audio.Accidental;

public class Note {
    /**
     * Constant for the minimum octave value
     */
    public static final int OCTAVE_MIN = 1;

    /**
     * Constant for the maximum octave value
     */
    public static final int OCTAVE_MAX = 10;
    private Pitch note;
    private double duration;
    private int octave;
    private Accidental accidental;
    private boolean repeat;

    // duration > 0 and 0 <= octave <= 10, otherwise throw IllegalArgumentException
    public Note(double duration, Pitch note, int octave, Accidental accidental, boolean repeat) {
       if(duration <= 0 || octave < 0 || octave > 10) {
          throw new IllegalArgumentException();
       }
       this.note = note;
       this.duration = duration;
       this.octave = octave;
       this.accidental = accidental;
       this.repeat = repeat;
    }
   
    // duration > 0, otherwise throw IllegalArgumentException
    public Note(double duration, Pitch note, boolean repeat) {
       this(duration, note, 0, Accidental.NATURAL, repeat);
    }
    public Note(double duration, boolean repeat) {
        this(duration, Pitch.R, OCTAVE_MIN + 1, Accidental.NATURAL, repeat);
    }
   // returns the length of the note in seconds
   public double getDuration() {
      return duration;
   }
   
   // returns the accidental value of the note
   public Accidental getAccidental() {
      return accidental;
   }
   
   // returns the octave of the note
   public int getOctave() {
      return octave;
   }
   
   // returns the pitch of the note 
   public Pitch getPitch() {
      return note;
   }
   
   // d must be greater than 0 otherwise throw IllegalArgumentException
   // sets the duration of the note to be the given time
   public void setDuration(double d) {
      if(d <= 0) {
         throw new IllegalArgumentException();    
      }
      duration = d;
   }
   
   public void setAccidental(Accidental a) {
      accidental = a;
   }
   
   public void setOctave(int octave) {
      if(octave < 0 || octave > 10) {
         throw new IllegalArgumentException();    
      }
      this.octave = octave;
   }
   
   public void setPitch(Pitch pitch) {
      note = pitch;
   }
   
   public void setRepeat(boolean repeat) {
      this.repeat = repeat;
   }
   
   public boolean isRepeat() {
      return repeat;
   }
   
    public void play() {
        if (note == Pitch.R) {
            StdAudio.play(this, StdAudio.note(0, duration, 0.5), duration);
        } else {
            char noteChar = note.toString().charAt(0);
            int steps = (noteChar - 'A') * 2;
            
            if (noteChar == 'C' || noteChar == 'D' || noteChar == 'E') {
                    steps -= 1;
            } else if (noteChar == 'F' || noteChar == 'G') {
                    steps -= 2;
            }
            
            if (octave > 4 || (octave == 4 && noteChar <= 'B')) {
                    steps += (octave - 4) * 12;
            } else {
                    steps -= (4 - octave) * 12;
            }

            if (noteChar != 'A' && noteChar != 'B') {
                steps -= 12;
            }

            if (accidental.equals(Accidental.SHARP)) {
                steps += 1;
            } else if (accidental.equals(Accidental.FLAT)) {
                steps -= 1;
            }

            double hz = 440.0 * Math.pow(2, steps / 12.0);
            StdAudio.play(this, StdAudio.note(hz, duration, 0.5), duration);
        }
    }

   public String toString() {
      if(note.equals(Pitch.R)) {
         return duration + " " + note + " " + repeat;
      } else {
         return duration + " " + note + " " + octave + " " + accidental + " " + repeat;
      }
   }
}