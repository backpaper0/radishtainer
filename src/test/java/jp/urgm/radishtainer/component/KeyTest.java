package jp.urgm.radishtainer.component;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import jakarta.inject.Named;

import org.junit.Test;

import jp.urgm.radishtainer.NamedImpl;

public class KeyTest {

	@Test
	public void getAliases() throws Exception {
		final Key key = new Key(Aaa5.class, new Annotation[0]);
		final Set<Key> aliases = key.getAliases();

		final Set<Key> expected = Stream.of(Aaa1.class, Aaa2.class, Aaa3.class, Aaa4.class)
			.map(Key::new)
			.collect(Collectors.toSet());

		assertEquals(expected, aliases);
	}

	@Test
	public void qualifier() throws Exception {
		final Key key = new Key(Bbb2.class, new NamedImpl("foo"));
		final Set<Key> aliases = key.getAliases();

		final Set<Key> expected = Stream
			.of(new Key(Bbb2.class), new Key(Bbb1.class), new Key(Bbb1.class, new NamedImpl("foo")))
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

	private interface Bbb1 {

	}

	@Named("foo")
	private static class Bbb2 implements Bbb1 {

	}

}
