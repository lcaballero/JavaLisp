package JavaLisp;


import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

/**
 * A SourceFileReader provides a simplified reader interface over a files.  It produces chars
 * and tracks the current offset.
 */
public class SourceFileReader implements AutoCloseable {

    private enum State {
        Unopened,
        Opened,
        Peeking,
        Reading
    }

    public final int EOF_SENTINEL = -1;
    public final int UNOPENED_SENTINEL = -2;
    public final Path path;

    private int currentInt;
    private Reader reader = null;
    private int offset = 0;
    private State state = State.Unopened;

    public SourceFileReader(Path p) {

        if (p == null) {
            throw new IllegalArgumentException("Path cannot be null");
        }

        if (!p.toFile().exists()) {
            throw new IllegalArgumentException("File provided as path doesn't exist");
        }

        this.path = p;
        this.currentInt = UNOPENED_SENTINEL;
    }

    /**
     * Opens the a BufferedStream over the held Path.
     * @throws IOException
     */
    public void open() throws IOException {
        this.reader = Files.asCharSource(this.path.toFile(), Charsets.UTF_8).openBufferedStream();
        this.state = State.Opened;
    }

    /**
     * Peeks at the next character, and determines if it exists and returns true
     * iff the next character can be obtained from the stream.
     *
     * @return true iff the next character can be obtained from the stream.
     * @throws IOException
     */
    public boolean hasNext() throws IOException {
        if (this.state == State.Unopened) {
            throw new IllegalStateException("Reader is not yet opened");
        }
        return peekInt() != EOF_SENTINEL;
    }

    /**
     * Sets the internal state to peeking so that it can read a single char from the stream.
     * While in a Peeking state it will return only the currentInt and not allow progress
     * over the stream.
     *
     * @return either the current char, or the next char depending on the current state.
     * @throws IOException
     */
    protected int peekInt() throws IOException {
        if (this.state != State.Peeking) {
            return readInt();
        } else {
            return this.currentInt;
        }
    }

    protected int readInt() throws IOException {
        if (this.state == State.Unopened) {
            throw new IllegalStateException("Reader is not yet opened");
        }

        // Use the peeking char if we are peeking, else read a new char
        // and advance the mark to the next char
        if (this.state == State.Peeking) {
            // Change state so that future calls will proceed to read single chars
            // up to the end of the file.
            this.state = State.Reading;
            return this.currentInt;
        } else {
            this.offset++;
            return this.currentInt = this.reader.read();
        }
    }

    /**
     * Return the current char, and move the cursor forward.
     * @return the next read char.
     * @throws IOException
     */
    public char read() throws IOException { return (char)readInt(); }

    /**
     * Look at the current char without moving the cursor forward.
     * @return the current char
     */
    public char peek() { return (char)this.currentInt; }

    /**
     * The offset into the file at which the reader is currently reading.
     *
     * @return the offset into the file at which the reader is currently reading.
     */
    public int getOffset() { return offset; }

    /**
     * Closes the underlying BufferedStreamReader.  Useful in for the
     * java try resource facilities.
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        this.reader.close();
    }
}
