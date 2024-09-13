package melody.audio;

import java.applet.*;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.sound.sampled.*;

public final class StdAudio {
	/**
	 * The sample rate - 44,100 Hz for CD quality audio.
	 */
	public static final int SAMPLE_RATE = 44100;

	private static final int BYTES_PER_SAMPLE = 2; // 16-bit audio
	private static final int BITS_PER_SAMPLE = 16; // 16-bit audio
	private static final double MAX_16_BIT = Short.MAX_VALUE; // 32,767
	private static final int SAMPLE_BUFFER_SIZE = 4096;

	private static SourceDataLine line; // to play the sound
	private static byte[] buffer; // our internal buffer
	private static int bufferSize = 0; // number of samples currently in internal buffer
									
	private static boolean muted = false;
	private static boolean paused = false;
	private static Set<AudioEventListener> listeners;

	// static initializer
	static {
            init();
	}
	
	public static class AudioEvent {
		public static enum Type { PLAY, LOOP, PAUSE, UNPAUSE, STOP, MUTE, UNMUTE }
		
		private Type type;
		private Note note;
		private double duration;
		
		public AudioEvent(Type type) {
			this(type, 0.0);
		}
		
		public AudioEvent(Type type, double duration) {
			this.type = type;
			this.duration = duration;
		}
		
		public AudioEvent(Type type, Note note, double duration) {
			this.type = type;
			this.note = note;
			this.duration = duration;
		}
		
		public double getDuration() {
			return duration;
		}
		
		public Note getNote() {
			return note;
		}
		
		public Type getType() {
			return type;
		}
		
		public String toString() {
			return "AudioEvent{Type=" + type
					+ (note == null ? "" : (", note=" + note))
					+ (duration == 0.0 ? "" : (", duration=" + duration)) + "}";
		}
	}
	
	public static interface AudioEventListener {
		void onAudioEvent(AudioEvent event);
	}
	
	public static void addAudioEventListener(AudioEventListener listener) {
            listeners.add(listener);
	}
	
	// open up an audio stream
	private static void init() {
            try {
                // 44,100 samples per second, 16-bit audio, mono, signed PCM, little
                // Endian
                AudioFormat format = new AudioFormat((float) SAMPLE_RATE, BITS_PER_SAMPLE, 1, true,
                                false);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(format, SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE);
                buffer = new byte[SAMPLE_BUFFER_SIZE * BYTES_PER_SAMPLE / 3];
                listeners = new HashSet<AudioEventListener>();
            } catch (Exception e) {
                System.err.println("Error initializing StdAudio audio system:");
                e.printStackTrace();
                System.exit(1);
            }
            // no sound gets made before this call
            line.start();
	}
	
	/**
	 * Removes all audio event listeners from being notified of future audio events
	 */
	public static void clearAudioEventListeners() {
            listeners.clear();
	}
	
	/**
	 * Close standard audio
	 */
	public static void close() {
            line.drain();
            line.stop();
	}

	/**
	 * Returns whether the audio system is currently muted
	 */
	public static boolean isMuted() {
            return muted;
	}

	/**
	 * Returns whether the audio system is currently paused
	 */
	public static boolean isPaused() {
            return paused;
	}

	/**
	 * Loop a sound file (in .wav or .au format) in a background thread
	 */
	public static void loop(String filename) {
            if (muted) {
                return;
            }
            URL url = null;
            try {
                File file = new File(filename);
                if (file.canRead())
                    url = file.toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url == null)
                throw new RuntimeException("audio " + filename + " not found");
            AudioClip clip = Applet.newAudioClip(url);
            clip.loop();
            notifyListeners(new AudioEvent(AudioEvent.Type.LOOP));
	}
        
	public static double[] note(double hz, double duration, double amplitude) {
            int N = (int) (StdAudio.SAMPLE_RATE * duration);
            double[] a = new double[N + 1];
            for (int i = 0; i <= N; i++)
                    a[i] = amplitude * Math.sin(2 * Math.PI * i * hz / StdAudio.SAMPLE_RATE);
            return a;
	}

	public static void play(double in) {
            if (muted) {
                return;
            }

            // clip if outside [-1, +1]
            if (in < -1.0) {
                in = -1.0;
            }
            if (in > +1.0) {
                in = +1.0;
            }

            // convert to bytes
            short s = (short) (MAX_16_BIT * in);
            buffer[bufferSize++] = (byte) s;
            buffer[bufferSize++] = (byte) (s >> 8); // little Endian

            // send to sound card if buffer is full
            if (bufferSize >= buffer.length) {
                line.write(buffer, 0, buffer.length);
                bufferSize = 0;
            }
	}
        
	public static void play(double[] input) {
            prePlay();
            for (int i = 0; i < input.length; i++) {
                play(input[i]);
            }
	}

	public static void play(double[] input, double duration) {
            play(input);
            notifyListeners(new AudioEvent(AudioEvent.Type.PLAY, duration));
	}

	/**
	 * If a sample is outside -1 -> 1, it will be clipped
	 */
	public static void play(Note note, double[] input, double duration) {
            play(input);
            notifyListeners(new AudioEvent(AudioEvent.Type.PLAY, note, duration));
	}
        
	public static void play(String filename) {
            prePlay();
            URL url = null;
            try {
                File file = new File(filename);
                if (file.canRead())
                        url = file.toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (url == null)
                throw new RuntimeException("audio " + filename + " not found");
            AudioClip clip = Applet.newAudioClip(url);
            clip.play();
	}

	public static double[] read(String filename) {
            byte[] data = readByte(filename);
            int N = data.length;
            double[] d = new double[N / 2];
            for (int i = 0; i < N / 2; i++) {
                d[i] = ((short) (((data[2 * i + 1] & 0xFF) << 8) + (data[2 * i] & 0xFF)))
                                / ((double) MAX_16_BIT);
            }
            return d;
	}

	public static void removeAudioEventListener(AudioEventListener listener) {
            listeners.remove(listener);
	}
	
	public static void save(String filename, double[] input) {
            AudioFormat format = new AudioFormat(SAMPLE_RATE, 16, 1, true, false);
            byte[] data = new byte[2 * input.length];
            for (int i = 0; i < input.length; i++) {
                int temp = (short) (input[i] * MAX_16_BIT);
                data[2 * i + 0] = (byte) temp;
                data[2 * i + 1] = (byte) (temp >> 8);
            }

            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                AudioInputStream ais = new AudioInputStream(bais, format, input.length);
                if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
                } else if (filename.endsWith(".au") || filename.endsWith(".AU")) {
                    AudioSystem.write(ais, AudioFileFormat.Type.AU, new File(filename));
                } else {
                    throw new RuntimeException("File format not supported: " + filename);
                }
            } catch (Exception e) {
                System.out.println(e);
                System.exit(1);
            }
	}

	public static void setMute(boolean mute) {
            muted = mute;
            notifyListeners(new AudioEvent(mute ? AudioEvent.Type.MUTE : AudioEvent.Type.UNMUTE));
	}

	public static void setPaused(boolean pause) {
            paused = pause;
            notifyListeners(new AudioEvent(pause ? AudioEvent.Type.PAUSE : AudioEvent.Type.UNPAUSE));
	}
        
	private static void notifyListeners(AudioEvent event) {
            for (AudioEventListener listener : listeners) {
                listener.onAudioEvent(event);
            }
	}
   
    public static void play(double duration, Pitch n, int octave, Accidental accidental) {
    	// play no sound if the note is a rest
    	if(n.equals(Pitch.R)) {
            StdAudio.play(note(0, duration, 0.5));
      	} else {
            char note = n.toString().charAt(0);					
            int steps = (note - 'A') * 2;

            // adjust for sharps/flats
            if (note == 'C' || note == 'D' || note == 'E') {
                steps -= 1;
            } else if (note == 'F' || note == 'G') {
                steps -= 2;
            }

            if( octave > 4 || (octave == 4 && note <= 'B')) {
                steps += (octave - 4) * 12;
            } else {
                steps -= (4 - octave) * 12;
            }

            if (note != 'A' && note != 'B') {
                steps -= 12;
            }

            if(accidental.equals(Accidental.SHARP)) {
                steps += 1;
            } else if (accidental.equals(Accidental.FLAT)) {
                steps -= 1;
            }

            double hz = 440.0 * Math.pow(2, steps / 12.0);
            StdAudio.play(note(hz, duration, 0.5));
      	}
    }

    private static void prePlay() {
        if (muted) {
            return;
        }
        while (paused) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    /*
     * Return data as a byte array.
     */
    private static byte[] readByte(String filename) {
        byte[] data = null;
        AudioInputStream ais = null;
        try {
            // try to read from file
            File file = new File(filename);
            if (file.exists()) {
                ais = AudioSystem.getAudioInputStream(file);
                data = new byte[ais.available()];
                ais.read(data);
            }

            // try to read from URL
            else {
                URL url = StdAudio.class.getResource(filename);
                ais = AudioSystem.getAudioInputStream(url);
                data = new byte[ais.available()];
                ais.read(data);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not read " + filename);
        }

        return data;
    }

    public static void main(String[] args) {
        // 440 Hz for 1 sec
        double freq = 440.0;
        for (int i = 0; i <= StdAudio.SAMPLE_RATE; i++) {
            StdAudio.play(0.5 * Math.sin(2 * Math.PI * freq * i / StdAudio.SAMPLE_RATE));
        }

        // scale increments
        int[] steps = { 0, 2, 4, 5, 7, 9, 11, 12 };
        for (int i = 0; i < steps.length; i++) {
            double hz = 440.0 * Math.pow(2, steps[i] / 12.0);
            StdAudio.play(note(hz, 1.0, 0.5));
        }

        StdAudio.close();
        System.exit(0);
    }

    private StdAudio() {
        
    }
}
