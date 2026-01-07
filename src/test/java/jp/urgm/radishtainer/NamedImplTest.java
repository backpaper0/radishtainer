package jp.urgm.radishtainer;

import static org.junit.Assert.*;

import jakarta.inject.Named;

import org.junit.Test;

public class NamedImplTest {

	@Named("foo")
	Object foo;

	@Named("bar")
	Object bar;

	@Test
	public void foo() throws Exception {
		final Named expected = getClass().getDeclaredField("foo").getAnnotation(Named.class);
		final Named actual = new NamedImpl("foo");
		assertEquals(expected, actual);
	}

	@Test
	public void bar() throws Exception {
		final Named expected = getClass().getDeclaredField("bar").getAnnotation(Named.class);
		final Named actual = new NamedImpl("bar");
		assertEquals(expected, actual);
	}

}
