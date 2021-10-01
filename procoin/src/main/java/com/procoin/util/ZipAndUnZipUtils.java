package com.procoin.util;

import android.util.Log;

import com.procoin.http.resource.BaseRemoteResourceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 这个有些方法一直没用
 */
public class ZipAndUnZipUtils {

//    /**
//     * 压缩文件,文件夹
//     *
//     * @param srcFileString 要压缩的文件/文件夹名字
//     * @param zipFileString 指定压缩的目的和名字
//     * @throws Exception
//     */
//    public static void ZipFolder(String srcFileString, String zipFileString) throws Exception {
//        android.util.Log.v("XZip", "ZipFolder(String, String)");
//
//        // 创建Zip包
//        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
//        outZip.setMethod(ZipOutputStream.DEFLATED);
//        outZip.setLevel(Deflater.BEST_COMPRESSION);
//
//        // 打开要输出的文件
//        File file = new File(srcFileString);
//
//        // 压缩
//        ZipFiles(file.getParent() + File.separator, file.getName(), outZip);
//
//        // 完成,关闭
//        outZip.finish();
//        outZip.close();
//
//    }

    /**
     * 直接压缩多个文件
     *
     * @param //            要压缩的文件/文件夹名字
     * @param zipFileString 指定压缩的目的和名字
     * @throws Exception
     */
    public static void ZipFolder(List<String> srcFileStrings, String zipFileString, BaseRemoteResourceManager remoteResourceManagerTalkie) throws Exception {
        android.util.Log.v("XZip", "ZipFolder(String, String)");

        // 创建Zip包
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        outZip.setMethod(ZipOutputStream.DEFLATED);
        outZip.setLevel(Deflater.BEST_COMPRESSION);
        File file = null;
        String fileString = null;
        ZipEntry zipEntry = null;
        FileInputStream inputStream = null;
        for (int i = 0; i < srcFileStrings.size(); i++) {
            fileString = srcFileStrings.get(i);
            file = remoteResourceManagerTalkie.getFile(fileString);
            Log.d("file", "存在==" + file.exists());
//			file = new File(fileString);
            // 判断是不是文件
            if (file.isFile()) {
                zipEntry = new ZipEntry(file.getName());
                inputStream = new FileInputStream(file);
                outZip.putNextEntry(zipEntry);
                int len;
                byte[] buffer = new byte[4096];
                while ((len = inputStream.read(buffer)) != -1) {
                    outZip.write(buffer, 0, len);
                }
                outZip.closeEntry();
                if (inputStream != null) inputStream.close();
            }
        }
        outZip.finish();
        outZip.close();

    }

//    //压缩文件
//    public static void ZipFolder(List<String> srcFileStrings, String zipFileString) throws Exception {
//        android.util.Log.v("XZip", "ZipFolder(String, String)");
//
//        // 创建Zip包
//        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
//        outZip.setMethod(ZipOutputStream.DEFLATED);
//        outZip.setLevel(Deflater.BEST_COMPRESSION);
//        File file = null;
//        String fileString = null;
//        ZipEntry zipEntry = null;
//        FileInputStream inputStream = null;
//        for (int i = 0; i < srcFileStrings.size(); i++) {
//            fileString = srcFileStrings.get(i);
//            file = new File(fileString);
//            Log.d("file", "存在==" + file.exists());
////			file = new File(fileString);
//            // 判断是不是文件
//            if (file.isFile()) {
//                zipEntry = new ZipEntry(file.getName());
//                inputStream = new FileInputStream(file);
//                outZip.putNextEntry(zipEntry);
//                int len;
//                byte[] buffer = new byte[4096];
//                while ((len = inputStream.read(buffer)) != -1) {
//                    outZip.write(buffer, 0, len);
//                }
//                outZip.closeEntry();
//                if (inputStream != null) inputStream.close();
//            }
//        }
//        outZip.finish();
//        outZip.close();
//
//    }

//    /**
//     * 压缩文件
//     *
//     * @param folderString
//     * @param fileString
//     * @param zipOutputSteam
//     * @throws Exception
//     */
//    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
//        Log.v("XZip", "ZipFiles(String, String, ZipOutputStream)");
//
//        if (zipOutputSteam == null) return;
//
//        File file = new File(folderString + fileString);
//
//        // 判断是不是文件
//        if (file.isFile()) {
//            ZipEntry zipEntry = new ZipEntry(fileString);
//            FileInputStream inputStream = new FileInputStream(file);
//            zipOutputSteam.putNextEntry(zipEntry);
//
//            int len;
//            byte[] buffer = new byte[4096];
//
//            while ((len = inputStream.read(buffer)) != -1) {
//                zipOutputSteam.write(buffer, 0, len);
//            }
//
//            zipOutputSteam.closeEntry();
//            inputStream.close();
//        } else {
//
//            // 文件夹的方式,获取文件夹下的子文件
//            String fileList[] = file.list();
//
//            // 如果没有子文件, 则添加进去即可
//            if (fileList.length <= 0) {
//                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
//                zipOutputSteam.putNextEntry(zipEntry);
//                zipOutputSteam.closeEntry();
//            }
//
//            // 如果有子文件, 遍历子文件
//            for (int i = 0; i < fileList.length; i++) {
//                ZipFiles(folderString, fileString + File.separator + fileList[i], zipOutputSteam);
//            }
//
//        }
//
//    }
//
//    public static void Unzip(String zipFile, String targetDir) {
//        int BUFFER = 4096; // 这里缓冲区我们使用4KB，
//        String strEntry; // 保存每个zip的条目名称
//
//        try {
//            BufferedOutputStream dest = null; // 缓冲输出流
//            FileInputStream fis = new FileInputStream(zipFile);
//            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
//            ZipEntry entry; // 每个zip条目的实例
//
//            while ((entry = zis.getNextEntry()) != null) {
//
//                try {
//                    Log.i("Unzip: ", "=" + entry);
//                    int count;
//                    byte data[] = new byte[BUFFER];
//                    strEntry = entry.getName();
//
//                    File entryFile = new File(targetDir + strEntry);
//                    File entryDir = new File(entryFile.getParent());
//                    if (!entryDir.exists()) {
//                        entryDir.mkdirs();
//                    }
//
//                    FileOutputStream fos = new FileOutputStream(entryFile);
//                    dest = new BufferedOutputStream(fos, BUFFER);
//                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
//                        dest.write(data, 0, count);
//                    }
//                    dest.flush();
//                    dest.close();
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//            zis.close();
//        } catch (Exception cwj) {
//            cwj.printStackTrace();
//        }
//    }

}
