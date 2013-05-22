package net.hogedriven.backpaper0.radishtainer;

import net.hogedriven.backpaper0.radishtainer.test.Aaa;
import net.hogedriven.backpaper0.radishtainer.test.Bbb;
import net.hogedriven.backpaper0.radishtainer.test.Ccc1;
import net.hogedriven.backpaper0.radishtainer.test.Ccc2;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContainerTest {

    @Test
    public void test_getInstance() throws Exception {
        Container c = newContainer();
        c.add(Aaa.class);
        Aaa instance = c.getInstance(Aaa.class);
        assertThat(instance, is(notNullValue()));
    }

    @Test
    public void test_inject() throws Exception {
        Container c = newContainer();
        c.add(Aaa.class);
        Bbb target = new Bbb();
        c.inject(target);
        assertThat("private field", target.getPrivateField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("package private field", target.getPackagePrivateField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("protected field", target.getProtectedField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("public field", target.getPublicField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("private method", target.getPrivateMethod(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("package private method", target.getPackagePrivateMethod(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("protected method", target.getProtectedMethod(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("public method", target.getPublicMethod(), not(sameInstance(Aaa.INSTANCE)));
    }

    @Test
    public void test_not_inject() throws Exception {
        Container c = newContainer();
        c.add(Aaa.class);
        Ccc2 target = new Ccc2();
        c.inject(target);
        assertThat("super field", target.getSuperField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("super method", target.getSuperMethod(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("sub field", target.getSubField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("sub method", target.getSubMethod(), not(sameInstance(Aaa.INSTANCE)));

        assertTrue("super field < sub field", target.getSuperField().count < target.getSubField().count);
        assertTrue("super method < sub method", target.getSuperMethod().count < target.getSubMethod().count);
        assertTrue("super method < sub field", target.getSuperMethod().count < target.getSubField().count);
    }

    protected Container newContainer() {
        return new Container();
    }
}
