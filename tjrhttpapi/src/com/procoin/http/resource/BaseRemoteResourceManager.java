package com.procoin.http.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.procoin.http.common.CommonConst;

/**
 * 专门对所有图片作一个文件imag操作
 *
 * @author Administrator
 */
public class BaseRemoteResourceManager extends Observable {
    private static final String TAG = "RemoteResourceManager";
    private static final boolean DEBUG = CommonConst.DEBUG;
    private Map<String, SoftReference<Bitmap>> mCache;
    private DiskCache mDiskCache;
    private BaseRemoteResourceFetcher mRemoteResourceFetcher;
    private FetcherObserver mFetcherObserver = new FetcherObserver();

    public BaseRemoteResourceManager(String cacheName) {
        this(new BaseDiskCache("procoin", cacheName));
    }

    public void removeNoMedia() {
        if (mDiskCache instanceof BaseDiskCache) {
            ((BaseDiskCache) mDiskCache).removeNoMedia();
        }

    }

    public BaseRemoteResourceManager(DiskCache cache) {
        mDiskCache = cache;
        mCache = new HashMap<String, SoftReference<Bitmap>>();
        mRemoteResourceFetcher = new BaseRemoteResourceFetcher(mDiskCache);
        mRemoteResourceFetcher.addObserver(mFetcherObserver);
    }

    public boolean exists(String key) {
        return mDiskCache.exists(key);
    }

    public boolean exists(Uri uri) {
        return mDiskCache.exists(uri.toString());
    }

    public File getFile(Uri uri) {
        if (DEBUG) Log.d(TAG, " getFile(): " + uri);
        return mDiskCache.getFile(uri.toString());
    }

    public InputStream getInputStream(Uri uri) throws IOException {
        if (DEBUG) Log.d(TAG, "从sd卡取出文件 getInputStream(): " + uri);
        return mDiskCache.getInputStream(uri.toString());
    }

    /**
     * 保存文件
     *
     * @param bitmap
     * @throws IOException
     */
    public void writeFile(File file, Bitmap bitmap) throws IOException {
        mDiskCache.writeFile(file, bitmap);
    }

    /**
     * 从缓存器中读取文件,如果没有就从SD卡中读取，再放入缓存器 这个是针对行情的图像使用的
     * <p>
     * file URL/file PATH
     *
     * @throws IOException
     */
    public Bitmap getnoOption(Uri uri) throws IOException {
        SoftReference<Bitmap> ref;
        Bitmap bitmap = null;
        // Look in memory first.
        synchronized (this) {
            ref = mCache.get(uri.toString());
        }
        if (ref != null) {
            bitmap = ref.get();
            if (bitmap != null) {
                if (DEBUG) Log.d(TAG, "从缓存中取出文件 mCache: " + uri);
                return bitmap;
            }
        }
        // Now try SD file.
        try {
            bitmap = BitmapFactory.decodeStream(getInputStream(uri));
            if (DEBUG) Log.d(TAG, "image bitmap: " + bitmap);
            if (bitmap != null) {
                synchronized (this) {
                    mCache.put(uri.toString(), new SoftReference<Bitmap>(bitmap));
                }
                return bitmap;
            }
        } catch (OutOfMemoryError e) {
        }
        // TODO: 如果都没有就返回null
        return null;
    }

    /**
     * 这个提供给头像使用的 从缓存器中读取文件,如果没有就从SD卡中读取，再放入缓存器
     *
     * @throws IOException
     */
    public Bitmap get(Uri uri) throws IOException {
        SoftReference<Bitmap> ref;
        Bitmap bitmap = null;
        // Look in memory first.
        synchronized (this) {
            ref = mCache.get(uri.toString());
        }
        if (ref != null) {
            bitmap = ref.get();
            if (bitmap != null) {
                if (DEBUG) Log.d(TAG, "从缓存中取出文件 mCache: " + uri);
                return bitmap;
            }
        }
        // Now try SD file.
        try {
            bitmap = BitmapFactory.decodeStream(getInputStream(uri));
            if (DEBUG) Log.d(TAG, "image bitmap: " + bitmap);
            if (bitmap != null) {
                synchronized (this) {
                    mCache.put(uri.toString(), new SoftReference<Bitmap>(bitmap));
                }
                return bitmap;
            }
        } catch (OutOfMemoryError e) {
        }
        // TODO: 如果都没有就返回null
        return null;
    }

    public File getFile(String key) {
        return mDiskCache.getFile(key);
    }

    /**
     * 专门针对大图而设计
     */
    public Bitmap get(String key) throws IOException {
        SoftReference<Bitmap> ref;
        Bitmap bitmap;
        // Look in memory first.
        synchronized (this) {
            ref = mCache.get(key);
        }
        if (ref != null) {
            bitmap = ref.get();
            if (bitmap != null) {
                if (DEBUG) Log.d(TAG, "从缓存中取出文件私聊图片 mCache: ");
                return bitmap;
            }
        }
        // Now try SD file.
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            // opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inSampleSize = 4;
            File file = getFile(key);
            // bitmap = BitmapFactory.decodeStream(getInputStream(key));
            if (file == null) return null;
            bitmap = BitmapFactory.decodeFile(file.getPath(), opt);
            if (bitmap != null) {
                synchronized (this) {
                    mCache.put(key, new SoftReference<Bitmap>(bitmap));
                }
                return bitmap;
            }
        } catch (OutOfMemoryError e) {
        }
        // TODO: 如果都没有就返回null
        return null;
    }

    public InputStream getInputStream(String key) throws IOException {
        return mDiskCache.getInputStream(key);
    }

    /**
     * 保存文件
     *
     * @param bitmap
     * @throws IOException
     */
    public void writeFile(File file, Bitmap bitmap, boolean reyle) throws Exception {
        mDiskCache.writeFile(file, bitmap, reyle);
    }

    /**
     * 保存文件
     *
     * @param is
     * @throws IOException
     */
    public void store(String key, InputStream is) throws IOException {
        mDiskCache.store(key, is);
    }

    /**
     * 读取文件,如果没有就从SD卡中读取，这个方法可以自己清空图片
     *
     * @throws IOException
     */
    public Bitmap getNoCache(String key) throws IOException {
        Bitmap bitmap = null;
        // Now try SD file.开始压缩图片
        try {
            bitmap = BitmapFactory.decodeStream(getInputStream(key));
            // if (bitmap != null) {
            // if (bitmap.getHeight() > 576 || bitmap.getWidth() > 480) bitmap =
            // Bitmap.createScaledBitmap(bitmap, 480, 576, false);
            // }
        } catch (OutOfMemoryError e) {
            // TODO: handle exception
        }
        if (DEBUG) Log.d(TAG, "image bitmap: " + bitmap);
        return bitmap;
    }

    public void request(final Uri uri) {
        if (DEBUG) Log.d(TAG, "request(): " + uri);
        mRemoteResourceFetcher.fetch(uri, Uri.encode(uri.toString()));
    }

    public void invalidate(Uri uri) {
        mDiskCache.invalidate(uri.toString());
    }

    public void shutdown() {
        mRemoteResourceFetcher.shutdown();
        mDiskCache.cleanup();
    }

    public void clear() {
        mRemoteResourceFetcher.shutdown();
        mDiskCache.clear();
        mCache.clear();
        if (mDiskCache instanceof BaseDiskCache) {
            ((BaseDiskCache) mDiskCache).clear();
        }
    }

    public static abstract class ResourceRequestObserver implements Observer {

        private Uri mRequestUri;

        abstract public void requestReceived(Observable observable, Uri uri);

        public ResourceRequestObserver(Uri requestUri) {
            mRequestUri = requestUri;
        }

        @Override
        public void update(Observable observable, Object data) {
            if (DEBUG) Log.d(TAG, "Recieved update: " + data);
            Uri dataUri = (Uri) data;
            if (dataUri == mRequestUri) {
                if (DEBUG) Log.d(TAG, "requestReceived: " + dataUri);
                requestReceived(observable, dataUri);
            }
        }
    }

    public class FetcherObserver implements Observer {

        @Override
        public void update(Observable observable, Object data) {
            setChanged();
            notifyObservers(data);
        }
    }

    public DiskCache getmDiskCache() {
        return mDiskCache;
    }

}
