package net.hogedriven.backpaper0.radishtainer;

import net.hogedriven.backpaper0.radishtainer.test.Aaa;
import net.hogedriven.backpaper0.radishtainer.test.Bbb;
import net.hogedriven.backpaper0.radishtainer.test.Ccc2;
import net.hogedriven.backpaper0.radishtainer.test.Ddd2;
import net.hogedriven.backpaper0.radishtainer.test.Eee2;
import net.hogedriven.backpaper0.radishtainer.test.Fff;
import net.hogedriven.backpaper0.radishtainer.test.Ggg;
import net.hogedriven.backpaper0.radishtainer.test.sub.Eee3;
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

    @Test
    public void test_inject_inheritance() throws Exception {
        Container c = newContainer();
        Ddd2 target = new Ddd2();
        c.inject(target);
        assertThat("super @Inject", target.withAtInjectSuper, is(false));
        assertThat("super no annotation", target.noAtInjectSuper, is(false));
        assertThat("sub @Inject", target.withAtInjectSub, is(true));
        assertThat("sub no annotation", target.noAtInjectSub, is(false));
    }

    @Test
    public void test_inject_no_override() throws Exception {
        Container c = newContainer();
        Eee2 target = new Eee2();
        c.inject(target);
        assertThat("private super", target.privateMethodSuper, is(true));
        assertThat("package private super", target.packagePrivateMethodSuper, is(false));
        assertThat("protected super", target.protectedMethodSuper, is(false));
        assertThat("public super", target.publicMethodSuper, is(false));
        assertThat("private sub", target.privateMethodSub, is(false));
        assertThat("package private sub", target.packagePrivateMethodSub, is(false));
        assertThat("protected sub", target.protectedMethodSub, is(false));
        assertThat("public sub", target.publicMethodSub, is(false));
    }

    @Test
    public void test_inject_no_override_diff_package() throws Exception {
        Container c = newContainer();
        Eee3 target = new Eee3();
        c.inject(target);
        assertThat("private super", target.privateMethodSuper, is(true));
        assertThat("package private super", target.packagePrivateMethodSuper, is(true));
        assertThat("protected super", target.protectedMethodSuper, is(false));
        assertThat("public super", target.publicMethodSuper, is(false));
        assertThat("private sub", target.privateMethodSub, is(false));
        assertThat("package private sub", target.packagePrivateMethodSub, is(false));
        assertThat("protected sub", target.protectedMethodSub, is(false));
        assertThat("public sub", target.publicMethodSub, is(false));
    }

    @Test
    public void test_getInstance_andinject() throws Exception {
        Container c = newContainer();
        c.add(Aaa.class);
        c.add(Fff.class);
        Fff instance = c.getInstance(Fff.class);
        assertThat("instance", instance, notNullValue());
        assertThat("field injection", instance.getField(), not(sameInstance(Aaa.INSTANCE)));
        assertThat("method injection", instance.getMethod(), not(sameInstance(Aaa.INSTANCE)));
    }

    @Test
    public void test_constructor_injection() throws Exception {
        Container c = newContainer();
        c.add(Aaa.class);
        c.add(Ggg.class);
        Ggg instance = c.getInstance(Ggg.class);
        assertThat("instance", instance, notNullValue());
        assertThat("constructor injection", instance.aaa, not(sameInstance(Aaa.INSTANCE)));
    }

    protected Container newContainer() {
        return new Container();
    }
}
