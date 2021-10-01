package com.procoin.util;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.procoin.R;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有用到这个类的activity都应该重写onRequestPermissionsResult，然后使用DynamicPermission.onRequestPermissionsResult
 * 缺点:一次只能申请一个权限
 * <p>
 * 使用步骤：
 * ①new DynamicPermission(),并且构造器中传入callback
 * ②使用DynamicPermission.checkSelfPermission()方法
 * ③所属activity实现onRequestPermissionsResult，然后回调到DynamicPermission中来， DynamicPermission.onRequestPermissionsResult
 * Created by zhengmj on 18-8-17.
 */

public class DynamicPermission {

    private AppCompatActivity activity;
    private TjrBaseDialog permissionDialog;
    private int requestCode;
    private RequestPermissionsCallBack requestPermissionsCallBack;


    public DynamicPermission(AppCompatActivity activity, RequestPermissionsCallBack requestPermissionsCallBack) {
        this.activity = activity;
        this.requestPermissionsCallBack = requestPermissionsCallBack;
    }

    public void checkSelfPermission(String[] permissions,int requestCode) {
        this.requestCode=requestCode;
        if (permissions.length > 1) {//多个权限
            List<String> needPermission = new ArrayList<>();//记录需要申请的权限
            for (String per : permissions) {
                if (ContextCompat.checkSelfPermission(activity, per) != PackageManager.PERMISSION_GRANTED) {
                    needPermission.add(per);
                }
            }
            if (needPermission.size() > 0) {
                ActivityCompat.requestPermissions(activity, needPermission.toArray(new String[needPermission.size()]), requestCode);
            } else {
                if (requestPermissionsCallBack != null) {
                    requestPermissionsCallBack.onRequestSuccess(permissions,requestCode);
                }
            }
        } else {//单个权限
            if (ContextCompat.checkSelfPermission(activity, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, permissions, requestCode);
            } else {
                if (requestPermissionsCallBack != null) {
                    requestPermissionsCallBack.onRequestSuccess(permissions,requestCode);
                }
            }
        }

    }

    /**
     * 请求权限的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.requestCode) {
            List<String> refusePermission = new ArrayList<>();//记录拒绝的哪些权限
            for (int i = 0, m = permissions.length; i < m; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    refusePermission.add(permissions[i]);
                }
            }
            if (refusePermission.isEmpty()) {//用户点击始终允许,或者点击全部允许
                if (requestPermissionsCallBack != null) {
                    requestPermissionsCallBack.onRequestSuccess(permissions,requestCode);
                }
            } else {//用户点击不允许,或者没有点击全部允许
                if (requestPermissionsCallBack != null) {
                    requestPermissionsCallBack.onRequestFail(permissions,requestCode);
                }
                showPermissionDialog(refusePermission);
//                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions)) {//并且用户点了不在询问，就告诉用户去设置目录可以设置，这个可以不用判断也可以
//                    showPermissionDialog();
//                } else {
//                    CommonUtil.showmessage(showFailMessageByPermission(), activity);
//                }


            }
        }
    }

//    private String showFailMessageByPermission() {
//        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission)) {
//            return "你已拒绝访问存储空间";
//        } else if (Manifest.permission.CAMERA.equals(permission)) {
//            return "你已拒绝使用摄像头";
//        }
//        return "您已拒绝使用该功能";
//    }


    private String getShowDialogMessage(List<String> refusePermission) {
        StringBuilder stringBuilder = new StringBuilder();
        if (refusePermission != null) {
            for (int i = 0, m = refusePermission.size(); i < m; i++) {
                //这里其他的请自行添加
                if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(refusePermission.get(i))) {
                    if(stringBuilder.length()>0){
                        stringBuilder.append("、");
                    }
                    stringBuilder.append("访问存储空间");
                } else if (Manifest.permission.CAMERA.equals(refusePermission.get(i))) {
                    if(stringBuilder.length()>0){
                        stringBuilder.append("、");
                    }
                    stringBuilder.append("使用摄像头");
                }else if (Manifest.permission.RECORD_AUDIO.equals(refusePermission.get(i))) {
                    if(stringBuilder.length()>0){
                        stringBuilder.append("、");
                    }
                    stringBuilder.append("录音");
                }else{
//                    stringBuilder.append("相对应");
                }

            }
        }
        if (stringBuilder != null) {
            return stringBuilder.toString();
        }
        return "相对应";
    }

    private void showPermissionDialog(List<String> refusePermission) {
        if (permissionDialog == null) {//
            permissionDialog = new TjrBaseDialog(activity) {
                @Override
                public void setDownProgress(int progress) {
                }

                @Override
                public void onclickOk() {
                    permissionDialog.dismiss();
                    NotificationsUtils.notificationSetting(activity);
                }

                @Override
                public void onclickClose() {
                    permissionDialog.dismiss();
                }
            };
        }
        permissionDialog.setTvTitle("权限申请");
        permissionDialog.setMessage("在设置-应用-" + activity.getString(R.string.app_name) + "-应用权限中开启" + getShowDialogMessage(refusePermission) + "的权限");//""
        permissionDialog.setBtnColseText("取消");
        permissionDialog.setBtnOkText("去设置");
        if (activity != null && !activity.isFinishing() && !permissionDialog.isShowing())
            permissionDialog.show();
    }


//    /**
//     * 需要申请的权限，因为可能申请的时候可能是权限组之类的
//     *
//     * @return
//     */
//    private String[] requestPermissions() {
//        if (permission.length == 1) {
//            if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission[0])) {
//                return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//            }
//        }
//        return permission;
//    }


    public interface RequestPermissionsCallBack {
        void onRequestSuccess(String[] permissions, int requestCode);//权限申请成功的回调（点击允许使用）,多个权限申请用requestCode判断

        void onRequestFail(String[] permissions, int requestCode);//权限申请失败的回调（点击不允许使用），一般情况下用不到这个方法，目前只是网页用到
    }

}
