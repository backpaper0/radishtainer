package jp.urgm.radishtainer.component;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class KeyTest {

    @Test
    public void getAliases() throws Exception {
        final Key key = new Key(Aaa5.class);
        final Set<Key> aliases = key.getAliases();

        final Set<Key> expected = Stream.of(Aaa1.class, Aaa2.class, Aaa3.class, Aaa4.class)
                .map(Key::new)
                .collect(Collectors.toSet());

        assertEquals(expected, aliases);
    }

    private interface Aaa1 {
    }

    private static class Aaa2 implements Aaa1 {
    }

    private interface Aaa3 {
    }

    private interface Aaa4 extends Aaa3 {
    }

    private static class Aaa5 extends Aaa2 implements Aaa4 {
    }
}
