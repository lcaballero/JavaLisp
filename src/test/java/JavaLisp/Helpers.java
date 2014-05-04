package JavaLisp;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class Helpers {

    public SourceFileReader newSourceReader(String s) {
        StringReader reader = new StringReader(s);
        BufferedReader buff = new BufferedReader(reader);
        SourceFileReader src = new SourceFileReader(buff);

        return src;
    }

    public List<String> toNumbers(Path p) throws IOException {
        String text = toText(p);
        String[] lines = text.split("\n");
        return Arrays.asList(lines)
            .stream()
            .map((s) -> "(" + s + ")")
            .collect(Collectors.toList());
    }

    public Lexer newLexer(String s) {
        Lexer lex = new Lexer(newSourceReader(s));
        return  lex;
    }

    public Lexer newLexer(Path p) throws IOException {
        Lexer lex = new Lexer(newSourceReader(toText(p)));
        return  lex;
    }

    public String toText(Path p) throws IOException {
        return Files.toString(p.toFile(), Charsets.UTF_8);
    }
}
