package ee.ut.jf2014.homework11;

import java.lang.reflect.UndeclaredThrowableException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ee.ut.jf2014.homework11.ProxifierTestClasses.A;
import ee.ut.jf2014.homework11.ProxifierTestClasses.I;

public class ProxifierTest {

    private I proxy;
    private A obj;

    @Before
    public void setUp() {
        obj = new A();
        proxy = (I) Proxifier.proxify(I.class, obj);
    }

    @Test(expected = NoSuchMethodException.class)
    public void methodDeprecatedOnlyInInterface() throws Throwable {
        try {
            proxy.methodDeprecatedOnlyInInterface();
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void methodDeprecatedOnlyInObj() throws Throwable {
        try {
            proxy.methodDeprecatedOnlyInObj();
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void methodWithMoreRestrictiveAccessModifierInObj() throws Throwable {
        try {
            proxy.methodWithMoreRestrictiveAccessModifierInObj();
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void methodWithMoreRestrictiveExceptionInObj() throws Throwable {
        try {
            proxy.methodWithMoreRestrictiveExceptionInObj();
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void methodWithDifferentReturnTypeInObj() throws Throwable {
        try {
            proxy.methodWithDifferentReturnTypeInObj();
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void methodWithMoreGeneralReturnTypeInObj() throws Throwable {
        try {
            proxy.methodWithMoreGeneralReturnTypeInObj();
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void methodWithDifferentArgCountInObj() throws Throwable {
        try {
            proxy.methodWithDifferentArgCountInObj("string");
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }

    @Test(expected = NoSuchMethodException.class)
    public void methodWithMoreSpecificArgInObj() throws Throwable {
        try {
            CharSequence arg = "charsequence";
            proxy.methodWithDifferentArgInObj(arg);
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }

    @Test
    public void methodWithSameOrLessGeneralReturnType() {
        Assert.assertEquals("called_methodWithSameReturnType",
                proxy.methodWithSameReturnType());
        Assert.assertEquals("called_methodWithMoreSpecificReturnTypeInObj",
                proxy.methodWithMoreSpecificReturnTypeInObj());
    }

    @Test
    public void methodWithSameParameters() {
        obj.methodCalled = false;
        proxy.methodWithSameParameters("string", "string");
        Assert.assertTrue(obj.methodCalled);
    }

    @Test
    public void methodWithLessRestrictiveExceptionInObj1() throws Exception {
        obj.methodCalled = false;
        proxy.methodWithLessRestrictiveExceptionInObj1();
        Assert.assertTrue(obj.methodCalled);
    }

    @Test
    public void methodWithLessRestrictiveExceptionInObj2() {
        obj.methodCalled = false;
        proxy.methodWithLessRestrictiveExceptionInObj2();
        Assert.assertTrue(obj.methodCalled);
    }
    
    @Test
    public void getX() {
        proxy.setX(500);
        Assert.assertEquals(500, proxy.getX());
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void getx() throws Throwable {
        try {
            proxy.getx();
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }
    
    @Test
    public void getURL() {
        proxy.setURL("jou");
        Assert.assertEquals("jou", proxy.getURL());
    }
    
    @Test
    public void getxIndex() {
        proxy.setxIndex(50);
        Assert.assertEquals(50, proxy.getxIndex());
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void setxindex() throws Throwable {
        try {
            proxy.setxindex(20);
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }
    
    @Test(expected = NoSuchMethodException.class)
    public void getPerson() throws Throwable {
        try {
            proxy.getPerson();
        } catch (UndeclaredThrowableException e) {
            throw e.getCause();
        }
    }   
}