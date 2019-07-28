package jp.urgm.radishtainer;

import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.junit.Ignore;

import junit.framework.Test;

@Ignore
public class JSR330TckTest {

    public static Test suite() {
        final Car car = null; //TODO
        final boolean supportsStatic = false;
        final boolean supportsPrivate = true;
        return Tck.testsFor(car, supportsStatic, supportsPrivate);
    }
}
