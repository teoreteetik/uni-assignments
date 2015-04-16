package ee.ut.jf2014.homework9;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class FibCached {

    static class MyCache {
        private Map<Long, SoftReference<BigInteger>> cache = new HashMap<>();
        private long putCount;
        private long getCount;
        private long hitCount;
        private ReferenceQueue<BigInteger> queue = new ReferenceQueue<>();

        public BigInteger get(Long n) {
            getCount++;
            SoftReference<BigInteger> result = cache.get(n);
            if (result != null) {
                BigInteger i = result.get();
                if (i != null) {
                    hitCount++;
                    return i;
                }
            }
            return null;
        }

        public void put(Long n, BigInteger result) {
            putCount++;
            cache.put(n, new SoftReference<>(result, queue));
        }

        public long collectedCount() {
            long count = 0;
            while (queue.poll() != null) count++;
            return count;
        }

        public long putCount() {
            return putCount;
        }

        public long getCount() {
            return getCount;
        }

        public long hitCount() {
            return hitCount;
        }
    }

    static MyCache cache = new MyCache();

    public static BigInteger fib(long n) {
        if (n == 0 || n == 1) {
            return BigInteger.ONE;
        }
        BigInteger cached = cache.get(n);
        if (cached != null) {
            return cached;
        }

        BigInteger result = fib(n - 1).add(fib(n - 2));
        cache.put(n, result);
        return result;
    }

    public void calculate() {
        int i = 0;
        BigInteger result = null;
        for (i = 0; i < 100000; i++) {
            // long start = System.currentTimeMillis();
            result = fib(i);
            // long end = System.currentTimeMillis();
            // System.out.println(i + " " + result + " " + (end - start));
        }
        System.out.println("done");

        // report statistics
        System.out.println("putCount=" + cache.putCount()); // number of calls to cache put method
        System.out.println("getCount=" + cache.getCount()); // number of calls to cache get method
        System.out.println("hitCount=" + cache.hitCount()); // number of calls to cache get method that found a value from cache
        System.out.println("missCount=" + (cache.getCount() - cache.hitCount())); // number of calls to cache get method that did not find a value from cache
        System.out.println("hitRatio=" + ((double) cache.hitCount()) / cache.getCount());
        System.out.println("collectedCount=" + cache.collectedCount()); // number of elements that were put to cache and collected by gc
    }

}
