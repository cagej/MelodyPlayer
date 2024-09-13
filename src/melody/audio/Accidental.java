package melody.audio;

public enum Accidental {	
    SHARP, NATURAL, FLAT;
    public static Accidental getValueOf(String s) {
        s = s.intern();
        if (s == "sharp") {
            return Accidental.SHARP;
        }
        if (s == "flat") {
            return Accidental.FLAT;
        }
        if (s == "natural") {
            return Accidental.NATURAL;
        }
        return null;
    }
}