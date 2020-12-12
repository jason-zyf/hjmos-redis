package com.pci.hjmos.cache.localCache;

/**
 * @author zyting
 * @sinne 2020-12-12
 */
public class CleanTimeOutThread implements Runnable{

    @Override
    public void run() {
        ConcurrentHashMapCacheUtils.setCleanThreadRun();
        while (true) {
            System.out.println("clean thread run ");
            ConcurrentHashMapCacheUtils.deleteTimeOut();
            try {
                Thread.sleep(ConcurrentHashMapCacheUtils.ONE_MINUTE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
