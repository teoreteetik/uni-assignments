package ee.ut.jf2014.homework11;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class Proxifier {

    public static Object proxify(Class<?> iface, Object obj) {
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                               new Class [] { iface },
                               new MyProxy(obj));
    }
    
    private static class MyProxy implements InvocationHandler {
        Object obj;
        
        public MyProxy(Object obj) {
            this.obj = obj;
        }
           
        
        @Override
        public Object invoke(Object proxy, Method iMethod, Object[] args) throws Throwable {
            
            Method[] objMethods = obj.getClass().getMethods();
            for (Method objMethod : objMethods) {
                if (objMethodSatisfiesInterfaceMethod(objMethod, iMethod)) { 
                    return objMethod.invoke(this.obj, args);
                }
            }
            
            try {
                return setOrGetFieldIfMethodNameSatisfiesJBNamingAndFieldExists(iMethod, args);              
            } catch (NoSuchFieldException | 
                     NotJBCompliantAccessorMethodException | 
                     IllegalArgumentException | 
                     IllegalAccessException e) {
                throw new NoSuchMethodException();
            }
            
        }
        
        private boolean objMethodSatisfiesInterfaceMethod(Method objMethod, Method iMethod) {
            return iMethod.getName().equals(objMethod.getName()) &&
                    Modifier.isPublic(objMethod.getModifiers()) && 
                    isReturnTypeCompatible(iMethod, objMethod) &&
                    areParametersCompatible(iMethod, objMethod) &&
                    areExceptionsCompatible(iMethod, objMethod) &&
                    !isDeprecated(objMethod) && !isDeprecated(iMethod);
        }
        
        private boolean isReturnTypeCompatible(Method iMethod, Method objMethod) {
            return iMethod.getReturnType().isAssignableFrom(objMethod.getReturnType());
        }
        
        private boolean areExceptionsCompatible(Method iMethod, Method objMethod) {
            for (Class<?> objEx : objMethod.getExceptionTypes()) {
                if (objEx.isAssignableFrom(Exception.class)) {
                    boolean objExceptionIsMoreRestrictive = true;
                    for (Class<?> iEx : iMethod.getExceptionTypes()) {
                        if (iEx.isAssignableFrom(objEx)) {
                            objExceptionIsMoreRestrictive = false;
                            break;
                        }
                    }
                    if (objExceptionIsMoreRestrictive) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        private boolean isDeprecated(Method method) {
            for (Annotation a : method.getAnnotations()) {
                if (a.annotationType().getCanonicalName().equals(Deprecated.class.getCanonicalName())) {
                    return true;
                }
            }
            return false;
        }
        
        private boolean areParametersCompatible(Method iMethod, Method objMethod) {
            Class<?>[] iMethodTypes = iMethod.getParameterTypes();
            Class<?>[] objMethodTypes = objMethod.getParameterTypes();

            if (iMethodTypes.length != objMethodTypes.length) {
                return false;
            }
            for (int i = 0; i < iMethodTypes.length; i++) {
                if (!objMethodTypes[i].getCanonicalName().equals(iMethodTypes[i].getCanonicalName())) {
                    return false;
                }
            }
            return true;
        }   
        
        private Object setOrGetFieldIfMethodNameSatisfiesJBNamingAndFieldExists(Method method, Object[] args) throws NotJBCompliantAccessorMethodException,
                                                                                                                     NoSuchFieldException, 
                                                                                                                     IllegalArgumentException,
                                                                                                                     IllegalAccessException {
            JBAccessor accessor = getAccessorFromJBAccessorMethodName(method.getName());
            Field field = obj.getClass().getDeclaredField(accessor.fieldName);
            field.setAccessible(true);
            if (accessor.type == JBAccessor.AccessorType.GETTER) {
                if (args != null) {
                    throw new NotJBCompliantAccessorMethodException();                    
                }
                return field.get(obj);
            } else {
                if (args.length != 1) {
                    throw new NotJBCompliantAccessorMethodException();                    
                }
                field.set(obj, args[0]);
                return null;
            }
        }

        private class NotJBCompliantAccessorMethodException extends Exception {}
        
        private static class JBAccessor {
            private enum AccessorType {
                GETTER,
                SETTER
            }
            public final AccessorType type;
            public final String fieldName;
            
            public JBAccessor(AccessorType type, String fieldName) {
                this.type = type;
                this.fieldName = fieldName;
            }
        }
        
        private JBAccessor getAccessorFromJBAccessorMethodName(String methodName) throws NotJBCompliantAccessorMethodException {
            if (!methodName.matches("(set|get|is).+")) {
                throw new NotJBCompliantAccessorMethodException();            
            }
            String suffix = methodName.startsWith("is") ? methodName.substring(2) : methodName.substring(3);
            String fieldName = getFieldNameOrNullFromJBAccessorSuffix(suffix);
            
            if (fieldName == null) {
                throw new NotJBCompliantAccessorMethodException();
            }
            
            if (methodName.startsWith("set")) {
                return new JBAccessor(JBAccessor.AccessorType.SETTER, fieldName);
            } else {
                return new JBAccessor(JBAccessor.AccessorType.GETTER, fieldName);
            }
        }
        
        private String getFieldNameOrNullFromJBAccessorSuffix(String fieldName) {
            char firstChar = fieldName.charAt(0);    
            if (fieldName.length() == 1) {                                   //getX or getx
                if (Character.isUpperCase(firstChar)) {                          //getX
                    return String.valueOf(Character.toLowerCase(firstChar));         //x
                } else {                                                         //getx
                    return null;                                                     //not jb
                } 
            } else {
                char secondChar = fieldName.charAt(1);
                if (Character.isUpperCase(firstChar)) {                                 //getURL or getName
                    if (Character.isUpperCase(secondChar)) {                                  //getURL
                        return fieldName;                                                           //URL
                    } else {                                                                  //getName
                        return Character.toLowerCase(firstChar) + fieldName.substring(1);           //name
                    }
                } else {                                                                //getaindex or getaIndex
                    if (Character.isLowerCase(secondChar)) {                                  //getaindex        
                        return null;                                                                //not jb
                    } else {                                                                  //getaIndex
                        return fieldName;                                                           //aIndex
                    }
                }
            } 
        }
    }
}