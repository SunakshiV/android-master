package com.procoin.http.resource;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class BaseDiskCache implements DiskCache {
    private static final String TAG = "BaseDiskCache";
    private static final String NOMEDIA = ".nomedia";
    private static final int MIN_FILE_SIZE_IN_BYTES = 100;

    // private long time = 15 * 24 * 60 * 60 * 1000;// 半个月前不用的文件

    private File mStorageDirectory;

    BaseDiskCache(String dirPath, String name) {
        // Lets make sure we can actually cache things!
        File baseDirectory = new File(Environment.getExternalStorageDirectory(), dirPath);
        File storageDirectory = new File(baseDirectory, name);
        createDirectory(storageDirectory);
        mStorageDirectory = storageDirectory;
        // cleanup(); // Remove invalid files that may have shown up.
        cleanupSimple();
    }

    public void removeNoMedia() {
        File nomediaFile = new File(mStorageDirectory, NOMEDIA);
        if (nomediaFile.exists()) {
            nomediaFile.delete();
        }
    }

    @Override
    public boolean exists(String key) {
        return getFile(key).exists();
    }

    /**
     * This is silly, but our content provider *has* to serve content: URIs as
     * File/FileDescriptors using ContentProvider.openAssetFile, this is a
     * limitation of the StreamLoader that is used by the WebView. So, we handle
     * this by writing the file to disk, and returning a File pointer to it.
     *
     * @param hash
     * @return
     */
    public File getFile(String hash) {
        File file = new File(mStorageDirectory.toString() + File.separator + Uri.encode(hash));
        return file;
    }

    public InputStream getInputStream(String hash) throws IOException {
        return (InputStream) new FileInputStream(getFile(hash));
    }

    /**
     * 这个是保存的文件的方法，需要加入一個type類型
     *
     * @param type 0 是 头像 , 1是 私聊图片 2是 私聊语音
     */
    public boolean store(String key, InputStream is) {
        Log.d(TAG, "store: " + key);
        is = new BufferedInputStream(is);

        try {
            OutputStream os = new BufferedOutputStream(new FileOutputStream(getFile(key)));
            byte[] b = new byte[2048];
            int count;

            while ((count = is.read(b)) > 0) {
                os.write(b, 0, count);
            }
            os.close();
            Log.d(TAG, "store complete: " + key);
            return true;
        } catch (IOException e) {
            Log.d(TAG, "store failed to store: " + key, e);
            return false;
        }
    }

    @Override
    public void writeFile(File file, Bitmap bitmap, boolean recycle) throws Exception {
        if (bitmap == null || file == null) {
            return;
        }
        // if (bitmap.getHeight() > 576 || bitmap.getWidth() > 480) {
        // bitmap = Bitmap.createScaledBitmap(bitmap, 480, 576, false);
        // }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(cropBitmap(bitmap));
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // PNG
        } catch (Exception ioe) {
            throw ioe;
        } finally {
            try {
                if (fos != null) {
                    if (recycle) bitmap.recycle();
                    fos.flush();
                    fos.close();
                }
                // bitmap.recycle();
            } catch (IOException e) {
                throw e;
            }
        }
    }

    /**
     * 将Bitmap写入本地缓存文件.
     *
     * @param hash   名称
     * @param bitmap
     */
    public void writeFile(File file, Bitmap bitmap) {
        if (bitmap == null || file == null) {
            return;
        }
        // if (bitmap.getHeight() > 576 || bitmap.getWidth() > 480) {
        // bitmap = Bitmap.createScaledBitmap(bitmap, 480, 576, false);
        // }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(cropBitmap(bitmap));
        } catch (Exception ioe) {

        } finally {
            try {
                if (fos != null) {
                    bitmap.recycle();
                    fos.flush();
                    fos.close();
                }
                // bitmap.recycle();
            } catch (IOException e) {
            }
        }
    }

    public void invalidate(String key) {
        getFile(key).delete();
    }

    public void cleanup() {
        // removes files that are too small to be valid. Cheap and cheater way
        // to remove files that
        // were corrupted during download.
        String[] children = mStorageDirectory.list();
        if (children != null) { // children will be null if hte directyr does
            // not exist.
            for (int i = 0; i < children.length; i++) {
                File child = new File(mStorageDirectory, children[i]);
                if (!child.equals(new File(mStorageDirectory, NOMEDIA)) && child.length() <= MIN_FILE_SIZE_IN_BYTES) {
                    Log.d(TAG, "Deleting: " + child);
                    child.delete();
                }
            }
        }
    }

    /**
     * Temporary fix until we rework this disk cache. We delete the first 50
     * youngest files if we find the cache has more than 1000 images in it.
     */
    public void cleanupSimple() {
        final int maxNumFiles = 1000;
        final int numFilesToDelete = 50;

        String[] children = mStorageDirectory.list();
        // for(String fileurl:children){
        // File child = new File(mStorageDirectory, fileurl);
        // }
        if (children != null) {
            Log.d(TAG, "Found disk cache length to be: " + children.length);
            if (children.length > maxNumFiles) {
//                Log.d(TAG, "Disk cache found to : " + children);
                for (int i = children.length - 1, m = i - numFilesToDelete; i > m; i--) {
                    File child = new File(mStorageDirectory, children[i]);
                    Log.d(TAG, "  deleting: " + child.getName());
                    child.delete();
                }
            }
        }
    }

    public void clear() {
        // Clear the whole cache. Coolness.
        String[] children = mStorageDirectory.list();
        if (children != null) { // children will be null if hte directyr does
            // not exist.
            for (int i = 0; i < children.length; i++) {
                File child = new File(mStorageDirectory, children[i]);
                if (!child.equals(new File(mStorageDirectory, NOMEDIA))) {
                    Log.d(TAG, "Deleting: " + child);
                    child.delete();
                }
            }
        }
        mStorageDirectory.delete();
    }

    private static final void createDirectory(File storageDirectory) {
        if (!storageDirectory.exists()) {
            Log.d(TAG, "Trying to create storageDirectory: " + String.valueOf(storageDirectory.mkdirs()));

            Log.d(TAG, "Exists: " + storageDirectory + " " + String.valueOf(storageDirectory.exists()));
            Log.d(TAG, "State: " + Environment.getExternalStorageState());
            Log.d(TAG, "Isdir: " + storageDirectory + " " + String.valueOf(storageDirectory.isDirectory()));
            Log.d(TAG, "Readable: " + storageDirectory + " " + String.valueOf(storageDirectory.canRead()));
            Log.d(TAG, "Writable: " + storageDirectory + " " + String.valueOf(storageDirectory.canWrite()));
            File tmp = storageDirectory.getParentFile();
            Log.d(TAG, "Exists: " + tmp + " " + String.valueOf(tmp.exists()));
            Log.d(TAG, "Isdir: " + tmp + " " + String.valueOf(tmp.isDirectory()));
            Log.d(TAG, "Readable: " + tmp + " " + String.valueOf(tmp.canRead()));
            Log.d(TAG, "Writable: " + tmp + " " + String.valueOf(tmp.canWrite()));
            tmp = tmp.getParentFile();
            Log.d(TAG, "Exists: " + tmp + " " + String.valueOf(tmp.exists()));
            Log.d(TAG, "Isdir: " + tmp + " " + String.valueOf(tmp.isDirectory()));
            Log.d(TAG, "Readable: " + tmp + " " + String.valueOf(tmp.canRead()));
            Log.d(TAG, "Writable: " + tmp + " " + String.valueOf(tmp.canWrite()));
        }

        File nomediaFile = new File(storageDirectory, NOMEDIA);
        if (!nomediaFile.exists()) {
            try {
                Log.d(TAG, "Created file: " + nomediaFile + " " + String.valueOf(nomediaFile.createNewFile()));
            } catch (IOException e) {
                Log.d(TAG, "Unable to create .nomedia file for some reason.", e);
//TODO				throw new IllegalStateException("Unable to create nomedia file.");
            }
        }

        // After we best-effort try to create the file-structure we need,
        // lets make sure it worked.
        if (!(storageDirectory.isDirectory() && nomediaFile.exists())) {
//TODO			throw new RuntimeException("Unable to create storage directory and nomedia file.");
        }
    }

    // 这个就是压缩图片
    private byte[] compressImage(Bitmap image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] resultbtye = null;
        try {
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 80;
            // 这句log可能会内存溢出
            while (baos.toByteArray().length / 1024 > 50 && options > 1) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
                if (options <= 0) {
                    options -= 1;
                }
            }
            resultbtye = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (baos != null) baos.close();
        }
        return resultbtye;
    }

    public byte[] cropBitmap(Bitmap image) {
        try {
            int w = image.getWidth();
            int h = image.getHeight();
            // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
            float hh = 640f;// 这里设置高度为800f
            float ww = 480f;// 这里设置宽度为480f
            // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
            int be = 1;// be=1表示不缩放
            if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
                be = (int) (w / ww);
            } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
                be = (int) (h / hh);
            }
            if (be <= 0) be = 1;
            if (be > 1) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                if (baos.toByteArray().length / 1024 > 128) {//
                    // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
                    baos.reset();// 重置baos即清空baos
                    image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//
                    // 这里压缩50%，把压缩后的数据存放到baos中
                }
                // ByteArrayInputStream isBm = new
                // ByteArrayInputStream(baos.toByteArray());

                // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
                // newOpts.inJustDecodeBounds = true;
                // Bitmap bitmap = BitmapFactory.decodeStream(isBm, null,
                // newOpts);
                // newOpts.inJustDecodeBounds = false;

                BitmapFactory.Options newOpts = new BitmapFactory.Options();
                newOpts.inSampleSize = be;// 设置缩放比例
                // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
                ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
                Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
                baos.close();
                isBm.close();
                // TODO 这里需要
                byte[] resultbtye = compressImage(bitmap);
                if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
                return resultbtye;
            } else {
                return compressImage(image);
            }
            // 压缩好比例大小后再进行质量压缩
        } catch (OutOfMemoryError e) {
        } catch (Exception e) {

        }
        return null;
    }

}
