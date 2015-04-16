package ee.ut.jf2014.homework11;

import java.io.IOException;

public class ProxifierTestClasses {
    
    public static interface I {
        @Deprecated void methodDeprecatedOnlyInInterface();
        void methodDeprecatedOnlyInObj();
        void methodWithMoreRestrictiveAccessModifierInObj();
        void methodWithMoreRestrictiveExceptionInObj() throws IOException;
        void methodWithDifferentReturnTypeInObj();
        String methodWithMoreGeneralReturnTypeInObj();
        void methodWithDifferentArgCountInObj(String arg1);
        void methodWithDifferentArgInObj(CharSequence arg1);
        
        String methodWithSameReturnType();
        CharSequence methodWithMoreSpecificReturnTypeInObj();
        void methodWithSameParameters(String arg1, String arg2);
        void methodWithLessRestrictiveExceptionInObj1() throws Exception;
        void methodWithLessRestrictiveExceptionInObj2();
        
        int getX();
        int getx();
        String getURL();
        int getxIndex();
        void setX(int x);
        void setURL(String URL);
        void setxIndex(int xIndex);
        void setxindex(int xIndex);
        String getPerson();
    }
    
    public static class A {
        public boolean methodCalled = false;
        
        private String URL = "http://neti.ee";
        private int x = 20;
        private int xIndex = 10;
        private boolean open;
        

        public void methodDeprecatedOnlyInInterface() {
            // exception
        }

        @Deprecated public void methodDeprecatedOnlyInObj() {
            // exception
        }

        protected void methodWithMoreRestrictiveAccessModifierInObj() {
            // exception
        }

        public void methodWithMoreRestrictiveExceptionInObj() throws Exception {
            // exception
        }

        public int methodWithDifferentReturnTypeInObj() {
            // exception
            return 0;
        }

        public CharSequence methodWithMoreGeneralReturnTypeInObj() {
            // exception
            return null;
        }

        public void methodWithDifferentArgCountInObj(String arg1, int arg2) {
            // exception
        }

        public void methodWithDifferentArgInObj(String arg1) {
            // exception
        }

        public String methodWithSameReturnType() {
            return "called_methodWithSameReturnType";
        }

        public String methodWithMoreSpecificReturnTypeInObj() {
            return "called_methodWithMoreSpecificReturnTypeInObj";
        }

        public void methodWithSameParameters(String arg1, String arg2) {
            methodCalled = true;
        }

        public void methodWithLessRestrictiveExceptionInObj1() throws IOException {
            methodCalled = true;
        }

        public void methodWithLessRestrictiveExceptionInObj2() throws RuntimeException {
            methodCalled = true;
        }
        
//        public int getX() {
//            return x;
//        }
//        public int getx() {
//            //throws exception
//            return 0;
//        }
//        public String getURL() {
//            return URL;
//        }
//        public int getxIndex() {
//            return xIndex;
//        }
//        
//        public void setX(int x) {
//            this.x = x;
//        }
//        
//        public void setURL(String URL) {
//            this.URL = URL;
//        }
//        public void setxIndex(int xIndex) {
//            this.xIndex = xIndex;
//        }
//        public void setxindex(int xIndex) {
//            //throws exception
//        }
//        public String getPerson() {
//            //throws exception, person doesn't exist
//            return null;
//        }
//        public boolean isOpen() {
//            return open;
//        }
    }
}