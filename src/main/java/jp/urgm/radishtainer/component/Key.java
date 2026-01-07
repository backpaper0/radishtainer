package jp.urgm.radishtainer.component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Key {

	private final Class<?> clazz;

	private final Annotation[] qualifiers;

	public Key(final Class<?> clazz, final Annotation... qualifiers) {
		this.clazz = clazz;
		this.qualifiers = qualifiers;
	}

	public Set<Key> getAliases() {
		final Set<Key> aliases = new HashSet<>();
		collect(clazz, aliases);
		return aliases;
	}

	private void collect(final Class<?> c, final Set<Key> aliases) {
		if (c != null && c != Object.class) {
			if (qualifiers.length > 0) {
				aliases.add(new Key(c));
			}
			if (c != clazz) {
				aliases.add(new Key(c, qualifiers));
			}
			collect(c.getSuperclass(), aliases);
			for (final Class<?> i : c.getInterfaces()) {
				collect(i, aliases);
			}
		}
	}

	@Override
	public int hashCode() {
		final Object[] a = new Object[qualifiers.length + 1];
		a[0] = clazz;
		System.arraycopy(qualifiers, 0, a, 1, qualifiers.length);
		return Arrays.hashCode(a);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Key) {
			final Key other = (Key) obj;
			return clazz.equals(other.clazz) && Arrays.equals(qualifiers, other.qualifiers);
		}
		return false;
	}

	@Override
	public String toString() {
		if (qualifiers.length == 0) {
			return clazz.getSimpleName();
		}
		return Arrays.stream(qualifiers)
			.map(Annotation::toString)
			.collect(Collectors.joining(", ", clazz.getSimpleName() + " { ", " }"));
	}

}
