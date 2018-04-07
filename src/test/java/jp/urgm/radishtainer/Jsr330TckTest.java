package jp.urgm.radishtainer;

import java.lang.annotation.Annotation;
import javax.inject.Named;
import junit.framework.Test;
import org.atinject.tck.Tck;
import org.atinject.tck.auto.Car;
import org.atinject.tck.auto.Convertible;
import org.atinject.tck.auto.Drivers;
import org.atinject.tck.auto.DriversSeat;
import org.atinject.tck.auto.Engine;
import org.atinject.tck.auto.FuelTank;
import org.atinject.tck.auto.Seat;
import org.atinject.tck.auto.Tire;
import org.atinject.tck.auto.V8Engine;
import org.atinject.tck.auto.accessories.Cupholder;
import org.atinject.tck.auto.accessories.SpareTire;

import jp.urgm.radishtainer.Container;

public class Jsr330TckTest {

    @Drivers
    Object drivers;

    @Named("spare")
    Object spare;

    private static Annotation qualifier(String name) throws NoSuchFieldException {
        return Jsr330TckTest.class.getDeclaredField(name).getAnnotations()[0];
    }

    public static Test suite() throws Exception {
        Container c = new Container();
        c.addClass(Car.class, null, Convertible.class);
        c.addClass(Seat.class, qualifier("drivers"), DriversSeat.class);
        c.addClass(Seat.class, null, null);
        c.addClass(Tire.class, null, null);
        c.addClass(Engine.class, null, V8Engine.class);
        c.addClass(Tire.class, qualifier("spare"), SpareTire.class);

        c.addClass(Cupholder.class, null, null);
        c.addClass(SpareTire.class, null, null);
        c.addClass(FuelTank.class, null, null);

        Car car = c.getInstance(Car.class, null);
        boolean supportsStatic = false;
        boolean supportsPrivate = true;
        return Tck.testsFor(car, supportsStatic, supportsPrivate);
    }
}
