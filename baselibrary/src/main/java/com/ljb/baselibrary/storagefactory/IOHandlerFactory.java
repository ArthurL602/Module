package com.ljb.baselibrary.storagefactory;


/**
 * Author      :ljb
 * Date        :2018/8/2
 * Description :
 */
public class IOHandlerFactory {
public  static  volatile IOHandlerFactory sIOHandlerFactory;

private IOHandler mMemoryIoHandler,mDiskLruIOHandler;

    public static IOHandlerFactory get(){
        if(sIOHandlerFactory==null){
            synchronized (IOHandlerFactory.class){
                if(sIOHandlerFactory==null){
                    sIOHandlerFactory = new IOHandlerFactory();
                }
            }
        }
        return sIOHandlerFactory;
    }

    private   IOHandler create(Class<? extends IOHandler> ioHandlerClass) {
        try {
            return ioHandlerClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 内存储存
     * @return
     */
    public IOHandler createMemoryIOHandler(){
        if (mMemoryIoHandler==null) {
            mMemoryIoHandler=create(MemoryIOHandler.class);
        }
        return mMemoryIoHandler;
    }

    /**
     * 磁盘存储
     * @return
     */
    public IOHandler createDiskIOHandler(){
        if (mDiskLruIOHandler==null) {
            mDiskLruIOHandler=create(DiskLruIOHandler.class);
        }
        return mDiskLruIOHandler;
    }

    /**
     * 默认存储
     * @return
     */
    public IOHandler creaetDefaultIOHandler(){
        if (mMemoryIoHandler==null) {
            mMemoryIoHandler=create(MemoryIOHandler.class);
        }
        return mMemoryIoHandler;
    }
}
