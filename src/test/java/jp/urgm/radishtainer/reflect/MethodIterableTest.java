package jp.urgm.radishtainer.reflect;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.Test;

import com.example.Override1;
import com.example.Override2;
import com.example.subpackage.Override3;

public class MethodIterableTest {

	@Test
	public void samePackage() throws Exception {
		final MethodIterable iterable = new MethodIterable(Override2.class);

		final Set<Method> methods = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toSet());

		final Set<Method> expected = Stream
			.of(Override2.class.getDeclaredMethod("method1"), Override2.class.getDeclaredMethod("method2"),
					Override2.class.getDeclaredMethod("method3"), Override2.class.getDeclaredMethod("method4"),
					Override2.class.getDeclaredMethod("method5"), Override1.class.getDeclaredMethod("method1"),
					Override1.class.getDeclaredMethod("method5"))
			.collect(Collectors.toSet());

		assertEquals(expected, methods);
	}

	@Test
	public void subPackage() throws Exception {
		final MethodIterable iterable = new MethodIterable(Override3.class);

		final Set<Method> methods = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toSet());

		final Set<Method> expected = Stream
			.of(Override3.class.getDeclaredMethod("method1"), Override3.class.getDeclaredMethod("method2"),
					Override3.class.getDeclaredMethod("method3"), Override3.class.getDeclaredMethod("method4"),
					Override3.class.getDeclaredMethod("method5"), Override1.class.getDeclaredMethod("method1"),
					Override1.class.getDeclaredMethod("method2"), Override1.class.getDeclaredMethod("method5"))
			.collect(Collectors.toSet());

		assertEquals(expected, methods);
	}

	@Test
	public void ignoreBridgeMethod() throws Exception {
		final MethodIterable iterable = new MethodIterable(Aaa2.class);

		final Set<Method> methods = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toSet());

		final Set<Method> expected = Arrays.stream(Aaa2.class.getDeclaredMethods())
			.filter(a -> a.getName().equals("method") && a.getReturnType() == String.class
					&& a.getParameterCount() == 0)
			.collect(Collectors.toSet());

		assertEquals(expected, methods);
	}

	@Test
	public void ignoreSyntheticMethod() throws Exception {
		new Aaa3().method(); // syntheticメソッドを生成するために呼び出す

		final MethodIterable iterable = new MethodIterable(Aaa3.class);

		final Set<Method> methods = StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toSet());

		final Set<Method> expected = Arrays.stream(Aaa3.class.getDeclaredMethods())
			.filter(a -> a.getName().equals("method") && a.getReturnType() == Void.TYPE && a.getParameterCount() == 0)
			.collect(Collectors.toSet());

		assertEquals(expected, methods);
	}

	private static class Aaa1 {

		Object method() {
			return null;
		}

	}

	private static class Aaa2 extends Aaa1 {

		@Override
		String method() {
			return null;
		}

	}

	private static class Aaa3 {

		private void method() {
		}

	}

}
