package com.procoin.http.retrofitservice;

import android.text.TextUtils;
import android.util.Log;

//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.procoin.http.tjrcpt.VHttpServiceManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 18-9-12.
 */

public class UploadFileUtils {


    private static final String FILE_KEY_IMGFILE = "files";
    private static final String FILE_KEY_OTHERFILE = "imageFiles";
    private static final String FILE_KEY_VIDEOFILE = "videoFiles";



    /**
     * 上传都用这个，这个方法是上传资源后，后台在把资源uri返回给前端
     *
     * @param url
     * @param dir  文件目录，后台给
     * @param map  文件 key的值为 files(普通文件，后台不处理) imageFiles(后台需要处理，比如压缩之类) videoFiles(视频) 目前只有这3种
     *
        通用文件上传
        type=file
        文件key用files
        dir=common

        图片上传
        type=imageRetOriginal // 上传图片，返回原图链接
        type=imageRetCompressed // 上传图片，返回压缩图片链接
        type=imageRetOrgAndCpr // 上传图片，返回原图和压缩图片链接
        文件key用imageFiles
        dir=...（后台给）

        视频上传
        type=videoRetOriginal // 上传视频，返回原视频链接
        type=videoRetCompressed // 上传视频，返回压缩视频链接（暂未可用）
        imageFiles=...（上传封面，不上传则自动截取视频第一帧作为封面）
        文件key用videoFiles
        dir=video
     */
    public static Call<ResponseBody> uploadFiles(String url, String type,String dir, Map<String, List<String>> map) {
        List<MultipartBody.Part> parts = null;
        Log.d("startUpdateUserInfo", "files==" + map.size());
        if (map != null && map.size() > 0) {
            parts = new ArrayList<>();
            for (String key : map.keySet()) {
                List<String> list = map.get(key);
                for (String f : list) {
                    if (!TextUtils.isEmpty(f)) {
                        File file = new File(f);
                        if (file.exists()) {
                            RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
                            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
                            parts.add(part);
                        }
                    }
                }
            }
        }
        return VHttpServiceManager.getInstance().getFileUploadService().uploadFiles(url, getUploadTypeParams(type,dir), parts);
    }
    public static Map<String, RequestBody> getUploadTypeParams(String type,String dir) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("dir", str2RequestBody(dir));
        map.put("type", str2RequestBody(type));
        return map;
    }


    /**
     * 上传都用这个，这个方法是上传资源后，后台在把资源uri返回给前端 ,此方法已弃用
     *
     * @param url
     * @param uploadType 1图片，2视频，3音频
     * @param multType   0(符合普通上传，支持单个文件key:imgFile)
     *                   1(符合支持多文件上传，key:imgFile)
     *                   2(支持多文件上传(返回大小文件,2套图)，key:imgFile)
     *                   3(支持文件Key上传，其中imgFile是单文件，otherFile是多文件，key:imgFile，key:otherFile,key:videoFile)
     *                   4(支持文件Key上传，其中imgFile是单文件，otherFile是单文件，videoFile是单文件，key:imgFile，key:otherFile，key:videoFile)
     *                   5实名认证图片专用，需要加水印
     * @param fileZone   这个是决定后台把资源放在哪个目录
     *                   FILE_COMMON(0, "/imredz/common/", "公用路径"), //
     *                   VIDEO(1, "/imredz/video/", "视频"), //
     *                   VOICE(2, "/imredz/voice/", "音频"), //
     *                   IMAGE_HEAD(3, "/imredz/head/", "图片->头像"), //
     *                   IMAGE_CHAT(4, "/imredz/chat/", "图片->私聊图片"), //
     *                   IMAGE_DYNAMIC(5, "/imredz/dynamic/", "图片->动态路径"), //
     *                   IMAGE_TOKA(6, "/imredz/toka/", "图片->通卡路径"), //
     *                   IMAGE_PROJECT(7, "/imredz/project/", "图片->项目路径"), //
     *                   IMAGE_IDENTITY(8, "/imredz/identity/", "图片->实名认证"), //
     *                   IMAGE_BANNER(9, "/imredz/banner/", "图片->banner"), //
     *                   IMAGE_OTC_APPEAL(10, "/imredz/otc/appeal/", "图片->OTC申诉"); //
     * @param map        文件 key的值为 imgFile otherFile videoFile(视频) 目前只有这3种
     * @return
     */
    public static Call<ResponseBody> uploadFiles(String url, int uploadType, int multType, int fileZone, Map<String, List<String>> map) {
        List<MultipartBody.Part> parts = null;
        Log.d("startUpdateUserInfo", "files==" + map.size());
        if (map != null && map.size() > 0) {
            parts = new ArrayList<>();
            for (String key : map.keySet()) {
                List<String> list = map.get(key);
                for (String f : list) {
                    if (!TextUtils.isEmpty(f)) {
                        File file = new File(f);
                        if (file.exists()) {
                            RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
                            MultipartBody.Part part = MultipartBody.Part.createFormData(key, file.getName(), requestFile);
                            parts.add(part);
                        }
                    }
                }
            }
        }
        return VHttpServiceManager.getInstance().getFileUploadService().uploadFiles(url, getUploadTypeParams(uploadType, multType, fileZone), parts);
    }

    //这个是单独发图片
    public static Call<ResponseBody> uploadFile(String url, File file) {
        return VHttpServiceManager.getInstance().getFileUploadService().uploadFile(url, MultipartBody.Part.createFormData("imgFile", file.getName(), RequestBody.create(MultipartBody.FORM, file)));
    }


    public static Map<String, RequestBody> getUploadTypeParams(int uploadType, int multType, int fileZone) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("uploadType", str2RequestBody(String.valueOf(uploadType)));
        map.put("multType", str2RequestBody(String.valueOf(multType)));
        map.put("fileZone", str2RequestBody(String.valueOf(fileZone)));
        return map;
    }


    //发布短文.长文或者发布提问用到
    public static Map<String, RequestBody> getPublishParams(long coin_id, String title, double question_coin, String content, String anwser_users, int type) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("coin_id", str2RequestBody(String.valueOf(coin_id)));
        map.put("title", str2RequestBody(title));
        map.put("question_coin", str2RequestBody(String.valueOf(question_coin)));
        map.put("content", str2RequestBody(content));
        map.put("answer_users", str2RequestBody(anwser_users));
        map.put("article_type", str2RequestBody(String.valueOf(type)));
        return map;
    }

    /**
     * 私聊发图片或者发语音
     */

    public static Map<String, RequestBody> privateChatsend(String chat_topic, String type, int second, int pic_length, int pic_width, String verifi) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("chat_topic", str2RequestBody(chat_topic));
        map.put("type", str2RequestBody(type));
        map.put("second", str2RequestBody(String.valueOf(second)));
        map.put("pic_length", str2RequestBody(String.valueOf(pic_length)));
        map.put("pic_width", str2RequestBody(String.valueOf(pic_width)));
        map.put("verifi", str2RequestBody(verifi));
        return map;
    }

    //更新用户信息  参数
    public static Map<String, RequestBody> updateUserInfo(String userName, String sex, String describes, String birthday) {
        Map<String, RequestBody> map = new HashMap<>();
        map.put("userName", str2RequestBody(String.valueOf(userName)));
        map.put("sex", str2RequestBody(sex));
        map.put("birthday", str2RequestBody(String.valueOf(birthday)));
        map.put("describes", str2RequestBody(describes));
        return map;
    }


    /**
     * 一张图片(或其他文件)适用
     * <p>
     * 多张图片(或多个文件)但是key都一样也适用
     *
     * @param picPaths
     * @return
     */
    public static Map<String, List<String>> getFilesMap(String... picPaths) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> files = new ArrayList<>();
        for (String filePath : picPaths) {
            files.add(filePath);
            Log.d("getFilesMap","filePath=="+filePath);
        }
        map.put(FILE_KEY_IMGFILE, files);
        return map;
    }

    /**
     * 生成key为imageFiles的map
     * @param picPaths
     * @return
     */
    public static Map<String, List<String>> getImageFilesMap(String... picPaths) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> files = new ArrayList<>();
        for (String filePath : picPaths) {
            files.add(filePath);
            Log.d("getFilesMap","filePath=="+filePath);
        }
        map.put(FILE_KEY_OTHERFILE, files);
        return map;
    }

    /**
     * 实名认证上传用到
     *
     * @return
     */
    public static Map<String, List<String>> getIdentityAuthen(String frontImgFile, String backImgFile, String holdImgFile) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> files1 = new ArrayList<>();
        files1.add(frontImgFile);
        map.put(FILE_KEY_IMGFILE, files1);

        List<String> files2 = new ArrayList<>();
        files2.add(backImgFile);
        map.put(FILE_KEY_OTHERFILE, files2);

        List<String> files3 = new ArrayList<>();
        files3.add(holdImgFile);
        map.put(FILE_KEY_VIDEOFILE, files3);
        return map;
    }

    public static Map<String, List<String>> video2Map(String videoPath,String videoBg) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> files = new ArrayList<>();
        files.add(videoPath);

        List<String> files2 = new ArrayList<>();
        files2.add(videoBg);

        map.put(FILE_KEY_VIDEOFILE, files);
        map.put(FILE_KEY_IMGFILE, files2);
        return map;
    }


    /**
     * 创建项目
     *
     * @param conver 封面图片
     * @param files  详情图片
     * @return
     */
    public static Map<String, List<String>> createProject(String conver, List<String> files) {
        Map<String, List<String>> map = new HashMap<>();
        List<String> converList = new ArrayList<>();
        converList.add(conver);
        map.put(FILE_KEY_IMGFILE, converList);
        map.put(FILE_KEY_OTHERFILE, files);
        return map;
    }


    public static RequestBody str2RequestBody(String str) {
        return RequestBody.create(MediaType.parse("text/plain"), TextUtils.isEmpty(str) ? "" : str);
    }
}
