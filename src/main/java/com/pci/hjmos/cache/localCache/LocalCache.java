package com.pci.hjmos.cache.localCache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 本地缓存
 * @author zyting
 * @sinne 2020-12-11
 */
public class LocalCache {
    private static final Map<String, Object> map;
    private static final ScheduledExecutorService timerService;  //定时器

    /**
     * 默认有效时长
     */
    private static final long DEFAULT_TIMEOUT = 1000*5;
    private static final long SECOND_TIME = 1000;

    /**
     * 初始化块总是在构造器之前执行
     * 静态初始化块执行的优先级高于非静态初始化块，在对象装载到JVM中时执行一次，仅能初始化类成员变量，即static修饰的数据成员
     * 静态初始化块是类相关的，系统将在类加载时执行静态初始化块，而不是在创建对象时才执行，因此静态初始化块总是比非静态初始化块先执行
     */
    static {
        map = new LRUMap<>();
        timerService = new ScheduledThreadPoolExecutor(1, new LocalCache.DaemonThreadFactory());
    }

    /**
     * 工具类
     */
    private LocalCache() {
    }

    static class LRUMap<K, V> extends LinkedHashMap<K, V> {
        /**
         * 默认缓存大小
         */
        private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;

        /**
         * 默认最大缓存大小
         */
        private static final int DEFAULT_MAX_CAPACITY = 1 << 30;

        /**
         * 默认加载因子
         */
        private static final float DEFAULT_LOAD_FACTOR = 0.75f;

        /**
         * 读写锁
         */
        private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        private final Lock rLock = readWriteLock.readLock();
        private final Lock wLock = readWriteLock.writeLock();


        public LRUMap() {
            super(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
        }

        public LRUMap(int initialCapacity) {
            super(initialCapacity, DEFAULT_LOAD_FACTOR);
        }

        /**
         * 需要重写LinkedHashMap中removeEldestEntry方法;
         * 新增元素的时候,会判断当前map大小是否超过DEFAULT_MAX_CAPACITY,超过则移除map中最老的节点;
         * @param eldest
         * @return
         */
        protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
            return size() > DEFAULT_MAX_CAPACITY;
        }

        public V put(K k, V v) {
            wLock.lock();
            try {
                return super.put(k, v);
            } finally {
                wLock.unlock();
            }
        }

        public V get(String k) {
            rLock.lock();
            try {
                return super.get(k);
            } finally {
                rLock.unlock();
            }
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            wLock.lock();
            try {
                super.putAll(m);
            } finally {
                wLock.unlock();
            }
        }

        public V remove(Object k) {
            wLock.lock();
            try {
                return super.remove(k);
            } finally {
                wLock.unlock();
            }
        }


        public boolean containKey(K k) {
            rLock.lock();
            try {
                return super.containsKey(k);
            } finally {
                rLock.unlock();
            }
        }

        public int size() {
            rLock.lock();
            try {
                return super.size();
            } finally {
                rLock.unlock();
            }
        }


        public void clear() {
            wLock.lock();
            try {
                super.clear();
            } finally {
                wLock.unlock();
            }
        }
    }

    /**
     * 清除缓存的任务类
     */
    static class CleanWorkerTask implements Runnable {
        private String key;

        public CleanWorkerTask(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            LocalCache.remove(key);
        }
    }

    private static final class DaemonThreadFactory implements ThreadFactory {
        private AtomicInteger atomicInteger = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable runnable) {
            Thread thread = new Thread(runnable);
            thread.setName("schedule-pool-Thread-" + atomicInteger.getAndIncrement());
            thread.setDaemon(true);
            return null;
        }
    }


    /**
     * 增加缓存
     */
    public static void add(String key, Object value) {
        map.put(key, value);
        timerService.schedule(new CleanWorkerTask(key), DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    /**
     * 增加缓存
     * @param timeout  有效时长
     */
    public static void add(String key, Object value, int timeout) {
        map.put(key, value);
        timerService.schedule(new CleanWorkerTask(key), timeout * SECOND_TIME, TimeUnit.MILLISECONDS);
    }

    public static void putAll(Map<String, Object> m, int timeout) {
        map.putAll(m);
        for (String key : m.keySet()) {
            timerService.schedule(new CleanWorkerTask(key), timeout * SECOND_TIME, TimeUnit.MILLISECONDS);
        }
    }


    /**
     * 获取缓存
     */
    public static Object get(String key) {
        return map.get(key);
    }

    public static boolean containsKey(String key) {
        return map.containsKey(key);
    }

    /**
     *
     * @param key
     */
    public static void remove(String key) {
        map.remove(key);
    }

    public static int size() {
        return map.size();
    }

}