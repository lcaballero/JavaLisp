package JavaLisp;


import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.junit.Assert.assertThat;

public class SourceFileReaderTests {

    @Before
    public void setup() {}

    @Test (expected = IllegalArgumentException.class)
    public void ctor_should_not_accept_null_path() {
        new SourceFileReader(null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ctor_should_not_accept_path_that_doesnt_exist() {
        new SourceFileReader(Paths.get("not-a-file.txt"));
    }

    @Test
    public void ctor_should_find_file_that_exists() {
        new SourceFileReader(Paths.get("./files/file-exists.txt"));
    }

    @Test (expected = IllegalStateException.class)
    public void should_throw_exception_if_hasnext_called_before_opening_the_reader() throws IOException {
        SourceFileReader r = new SourceFileReader(Paths.get("./files/file-exists.txt"));
        r.hasNext();
    }

    @Test (expected = IllegalStateException.class)
    public void should_throw_exception_if_read_called_before_opening_the_reader() throws IOException {
        SourceFileReader r = new SourceFileReader(Paths.get("./files/file-exists.txt"));
        r.read();
    }

    @Test
    public void should_not_have_next_for_newly_opened_empty_file() throws IOException {
        SourceFileReader r = new SourceFileReader(Paths.get("./files/file-exists.txt"));
        r.open();
        assertThat(r.hasNext(), is(false));
    }

    @Test
    public void ctor_should_find_and_have_next() throws IOException {
        SourceFileReader r = new SourceFileReader(Paths.get("./files/has-source.txt"));
        r.open();
        boolean hasNext = r.hasNext();
        char curr = r.peek();
        assertThat("current char: " + curr, hasNext, is(true));
    }

    @Test
    public void initial_reader_offset_should_be_zero() throws IOException {
        SourceFileReader r = new SourceFileReader(Paths.get("./files/has-source.txt"));
        r.open();
        assertThat(r.getOffset(), is(0));
    }

    @Test
    public void should_progress_through_the_file() throws IOException {
        Path path = Paths.get("./files/has-source.txt");
        SourceFileReader r = new SourceFileReader(path);
        String text = Files.toString(path.toFile(), Charsets.UTF_8);
        r.open();

        assertThat(text.length(), greaterThan(0));

        for (int i = 0, n = text.length(); i < n; i++) {
            assertThat(r.read(), is(text.charAt(i)));
        }

        assertThat(r.getOffset(), is(text.length()));
    }

    @Test
    public void should_be_able_to_make_string_from_chars_read_that_match_contents_of_file() throws IOException {
        Path path = Paths.get("./files/has-source.txt");
        SourceFileReader r = new SourceFileReader(path);
        String text = Files.toString(path.toFile(), Charsets.UTF_8);
        r.open();

        char[] chars = new char[text.length()];

        for (int i = 0, n = text.length(); i < n; i++) {
            chars[i] = r.read();
        }

        assertThat(new String(chars), is(text));
    }

    @Test
    public void should_progress_the_offset_through_the_file() throws IOException {
        Path path = Paths.get("./files/has-source.txt");
        SourceFileReader r = new SourceFileReader(path);
        String text = Files.toString(path.toFile(), Charsets.UTF_8);
        r.open();

        for (int i = 0, n = text.length(); i < n; i++) {
            assertThat(r.getOffset(), is(i));
            r.read();
        }
    }


}
