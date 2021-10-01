package com.procoin.social.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.http.model.User;
import com.procoin.social.R;
import com.procoin.social.baseui.AbstractBaseActivity;
import com.procoin.social.common.TjrSocialShareConfig;
import com.procoin.social.util.CommentWatcher;
import com.procoin.social.util.CommentWatcher.CommentWatcherInterface;
import com.procoin.social.util.CommentWatcher.SurplusNum;
import com.procoin.social.util.CommonUtil;
import com.procoin.social.util.ResizeLayout;
import com.procoin.social.util.SmileyParser;
import com.procoin.social.view.TjrsocialCanvasActivity;
import com.procoin.social.view.TjrsocialStockMenuActivity;
import com.procoin.social.view.TjrsocialchoiceActivity;
import com.procoin.task.BaseAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

//import com.cropyme.social.weibo.TjrSocialShareWeiboAtActivity;

/**
 * 这个类是要 分享时候的统一样式,如果 themeId is 样式的id ,现在就可以
 *
 * @author zhengmj
 */
public class ShareUI implements ResizeLayout.showOrHideViewListen, CommentWatcherInterface, SurplusNum {
    private AbstractBaseActivity activity;
    private View view;
    private SmileyParser smileyParser; // 表情的解析
    private CommentWatcher watcher; // 這個是

    private Button btndel;
    private Button btnFace; // 表情按钮
    private RelativeLayout rlImage;
    private LinearLayout listques;
    private LinearLayout gvFace;
    private ScrollView srolview;
    private GridView gvimage;
    private TextView toptext;// 顶部的标题，估计会有不同的名字
    private String surplusNum120;
    private String surplusNumPass120;
    private InputMethodManager imm;
    private CheckBox cbXuan;
    private Button btnAt;
    private Button btnImage;
    private TextView tvNum;
    private EditText edWeibo;
    private ImageView buImg;

    // 页面传递的参数
    private Bundle bundle;// 页面传递进来的
    private Bitmap bmp; // 图片
    private ArrayList<String> arraylist;// 问题的列表
    private String question;// 是否已经有选项
    // private String filename, fileUrl, jsonStr;// 图片的文件名,jsonstr是每个组件的json数据
    // 图片编辑
    private static final int IMAGE_SELECT = 1;
    private static final int PHOTOHRAPH = 2;
    public final int PHOTORESOULT = 3;// 结果
    public final String IMAGE_UNSPECIFIED = "image/*";
    private EditImageTask editImageTask;

    private ShareUICallBack callBack;
    // private String gotoActivity; //要去到的頁面，有時跳轉到cavas頁面會用到
    private String textContent;// 微薄的内容
    private User user;
    // 页面的配置项
    private boolean isAt; // 需要at@
    private boolean isface;// 是不是需要表情
    private boolean isOtherImg;// 是不是要可以選其他的圖片
    private boolean isQuestion; // 是不是需要選問題
    private String themeType;

    private WeiboShareFace shareFace;

    public User getUser() {
        return user;
    }

    public void setAt(boolean isAt) {
        this.isAt = isAt;
        if (!this.isAt) btnAt.setVisibility(View.GONE);
    }

    public void setIsface(boolean isface) {
        this.isface = isface;
        if (!this.isface) btnFace.setVisibility(View.GONE);
    }

    public void setOtherImg(boolean isOtherImg) {
        this.isOtherImg = isOtherImg;
        if (!this.isOtherImg) btnImage.setVisibility(View.GONE);
    }

    public void setQuestion(boolean isQuestion) {
        this.isQuestion = isQuestion;
        if (!this.isQuestion) cbXuan.setVisibility(View.INVISIBLE);
    }

    public void setCallBack(ShareUICallBack callBack) {
        this.callBack = callBack;
    }

    public ShareUI(AbstractBaseActivity activity, String themeType) {
        this.activity = activity;
        this.themeType = themeType;
        int resId = R.style.ShareTheme_Default;
        // switch (themeId) {//因为页面其他组件使用会出问题，先去掉
        // case 0:
        // resId = R.style.ShareTheme_Default;
        // break;
        // case 1:
        // resId = R.style.ShareTheme_Kline;
        // break;
        //
        // default:
        // resId = R.style.ShareTheme_Default;
        // break;
        // }
        activity.setTheme(resId);
        initView();
    }

    /**
     * 頂上的標題
     *
     * @param str
     */
    public void SetTopText(String str) {
        toptext.setText(str);
    }

    /**
     * @param mbundle
     */
    public void getData(Bundle mbundle) {
        this.bundle = mbundle;
        if (mbundle != null) {

            if (mbundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_BITMAP))
                bmp = CommonUtil.BytesToBitmap(mbundle.getByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP));
            if (mbundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT)) {
                textContent = mbundle.getString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT);
            }
            // if
            // (mbundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT))
            // textContent =
            // mbundle.getString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT);
            if (mbundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USERID)) {
                user = new User();
                user.setUserId(mbundle.getLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, 0l));
            }
            if (user == null || user.getUserId() == 0) {
                CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
                activity.goback();
                return;
            }
            if (mbundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USERNAME)) {
                user.setUserName(mbundle.getString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME));
            }
            if (mbundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL)) {
                user.setHeadUrl(mbundle.getString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL));
            }
        }

        if (question != null) {
            cbXuan.setChecked(true);
        }
        if (textContent != null) {
            edWeibo.setText(smileyParser.replace(textContent));
        }

        if (bmp != null) {
            rlImage.setVisibility(View.VISIBLE);
            CommonUtil.recycleBitmap(buImg);
            btnImage.setVisibility(View.GONE);
            buImg.setImageBitmap(bmp);
        }
    }

    public View getView() {
        return view;
    }

    public void initView() {
        OnClick onClick = new OnClick();
        view = activity.inflateView(R.layout.tjr_social_main);
        Button btnBack = (Button) view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(onClick);
        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(onClick);
        ResizeLayout rlAll = (ResizeLayout) view.findViewById(R.id.rlAlltest);
        rlAll.setListen(this);
        smileyParser = new SmileyParser(activity);
        watcher = new CommentWatcher(120, this);
        watcher.setWatcherInterface(this);
        btndel = (Button) view.findViewById(R.id.btndel);
        rlImage = (RelativeLayout) view.findViewById(R.id.rlImage);
        listques = (LinearLayout) view.findViewById(R.id.lysd);
        gvFace = (LinearLayout) view.findViewById(R.id.llFace);
        gvimage = (GridView) view.findViewById(R.id.gvtheimage);
        srolview = (ScrollView) view.findViewById(R.id.srolview);
        toptext = (TextView) view.findViewById(R.id.tvTitle);
        gvimage.setAdapter(new ArrayAdapter<String>(activity, R.layout.tjr_social_share_chat_kline_item, new String[]{"大盘分时", "股票分时", "大盘 K 线", "股票 K 线", "拍 照", "图库选取"}));
        gvimage.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (arg2) {
                    case 4:

                        if (!CommonUtil.isSdCard()) {
                            CommonUtil.showToast(activity, "SD卡不存在,请检查...", Gravity.BOTTOM);
                            return;
                        }
                        try {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
                            activity.startActivityForResult(intent, PHOTOHRAPH);
                        } catch (Exception e) {
                            CommonUtil.showToast(activity, "请确认是否有摄相功能!", Gravity.BOTTOM);
                        }

                        break;
                    case 5:
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                        activity.startActivityForResult(intent, IMAGE_SELECT);
                        break;
                    default:
                        if (!CommonUtil.isSdCard()) {
                            CommonUtil.showToast(activity, "SD卡不存在,请检查...", Gravity.BOTTOM);
                            return;
                        }
                        if (bundle != null) {
                            bundle.putInt(TjrSocialShareConfig.KEY_EXTRAS_TYPE, arg2);
                            CommonUtil.pageJumpToData(activity, TjrsocialStockMenuActivity.class, false, false, bundle);
                        }
                        break;
                }
            }
        });
        shareFace = new WeiboShareFace(activity);
        gvFace.addView(shareFace);
        shareFace.setClickFace(new WeiboShareFace.ClickFace() {

            @Override
            public void onClickItemFace(String face) {
                int index = edWeibo.getSelectionStart();// 获取光标所在位置
                Editable edit = edWeibo.getEditableText();// 获取EditText的文字
                if (index < 0 || index >= edit.length()) {
                    edit.append(smileyParser.replace(face));
                } else {
                    edit.insert(index, smileyParser.replace(face));// 光标所在位置插入文字
                }
            }
        });
        // gvFace.setAdapter(faceAdapter);
        // gvFace.setOnItemClickListener(new OnItemClickListener() {
        // @Override
        // public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        // long arg3) {
        // int index = edWeibo.getSelectionStart();// 获取光标所在位置
        // Editable edit = edWeibo.getEditableText();// 获取EditText的文字
        // if (index < 0 || index >= edit.length()) {
        // edit.append(smileyParser.replace(mSmileyTexts[arg2]));
        // } else {
        // edit.insert(index, smileyParser.replace(mSmileyTexts[arg2]));//
        // 光标所在位置插入文字
        // }
        // }
        // });
        surplusNum120 = activity.getString(R.string.surplusNum120);
        surplusNumPass120 = activity.getString(R.string.surplusNumPass120);
        imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        cbXuan = (CheckBox) view.findViewById(R.id.cbXuan);
        btnAt = (Button) view.findViewById(R.id.btnAt);
        btnFace = (Button) view.findViewById(R.id.btnExpression);
        btnImage = (Button) view.findViewById(R.id.btnImage);
        btnFace.setOnClickListener(onClick);

        tvNum = (TextView) view.findViewById(R.id.tvNum);
        edWeibo = (EditText) view.findViewById(R.id.etWeico);
        edWeibo.addTextChangedListener(watcher);
        tvNum.setText(String.format(surplusNum120, 120));// 限制的最大字数
        buImg = (ImageView) view.findViewById(R.id.ivImg);
        if (onClick != null) {
            btnAt.setOnClickListener(onClick);
            btnImage.setOnClickListener(onClick);
            buImg.setOnClickListener(onClick);
            cbXuan.setOnClickListener(onClick);
            btndel.setOnClickListener(onClick);
            listques.setOnClickListener(onClick);
        }
    }

    @Override
    public void showOrHideView(boolean show) {
        if (!show) {
            gvFace.setVisibility(View.GONE);
            gvimage.setVisibility(View.GONE);
            Viewsetvisible(false);
        } else {
            if (!(listques.getChildCount() == 0)) {
                Viewsetvisible(true);
            }
        }

    }

    /**
     * 是否让问题选项隐藏
     */
    private void Viewsetvisible(boolean visible) {
        if (visible) {
            srolview.setVisibility(View.VISIBLE);
            listques.setVisibility(View.VISIBLE);
        } else {
            srolview.setVisibility(View.GONE);
            listques.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub

    }

    @Override
    public void callSurplusNum(int num) {
        tvNum.setText(Html.fromHtml(String.format(num < 0 ? surplusNumPass120 : surplusNum120, num)));
//        if (num < 0) CommonUtil.showToast(activity, "输入内容超过120个中文字符(240个英文字符)!", Gravity.CENTER);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btnBack) {
                activity.goback();
            } else if (id == R.id.btnAdd) {
                String content = edWeibo.getText().toString();
                if (content != null && content.length() > 0) {
                    if (!watcher.getNumber()) {
                        CommonUtil.showToast(activity, "内容字数超过120个中文字符,或240个英文字符!", Gravity.CENTER);
                        return;
                    }
                } else if (content.length() <= 0) {
                    CommonUtil.showToast(activity, "内容不能为空", Gravity.BOTTOM);
                    return;
                }
                JSONArray option = new JSONArray();
                String voteTitle = null;
                int weiboType = 2;// 无投票
                int voteType = 1;
                if (arraylist != null && arraylist.size() > 0) {
                    for (int i = 0; i < arraylist.size() - 1; i++) {
                        try {
                            option.put(i, new JSONObject().put("content", arraylist.get(i)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    weiboType = 3;// 有投票
                    voteTitle = arraylist.get(arraylist.size() - 1);
                    voteType = Integer.parseInt(question);
                }

                if (callBack != null)
                    callBack.sendWeibo(weiboType, content.trim(), bmp, voteTitle, voteType, option.toString());
            } else if (id == R.id.btnAt) {
//                Intent intent = new Intent(activity, TjrSocialShareWeiboAtActivity.class);
//                intent.putExtras(bundle);
//                activity.startActivityForResult(intent, 555);
            } else if (id == R.id.btnExpression) {
                if (gvFace.getVisibility() == View.GONE) {
                    gvFace.setVisibility(View.VISIBLE);
                    gvimage.setVisibility(View.GONE);
                    if (imm.isActive()) imm.hideSoftInputFromWindow(edWeibo.getWindowToken(), 0);
                } else {
                    gvFace.setVisibility(View.GONE);
                }
            } else if (id == R.id.ivImg) {
                startEditImageTask();

//                switch (themeId) {
//                    case 1:// k綫不能編輯
//
//                        break;
//
//                    default:
//                        startEditImageTask();
//                        break;
//                }

            } else if (id == R.id.btnImage) {
                if (gvimage.getVisibility() == View.GONE) {
                    gvimage.setVisibility(View.VISIBLE);
                    gvFace.setVisibility(View.GONE);
                    if (imm.isActive()) imm.hideSoftInputFromWindow(edWeibo.getWindowToken(), 0);
                } else {
                    gvimage.setVisibility(View.GONE);
                }
            } else if (id == R.id.cbXuan) {
                if (!cbXuan.isChecked()) {
                    cbXuan.setChecked(false);
                    Viewsetvisible(false);
                    listques.removeAllViewsInLayout();
                    bundle.remove(TjrSocialShareConfig.KEY_EXTRAS_QUESTION);
                    bundle.remove(TjrSocialShareConfig.KEY_EXTRAS_QUESTION_ARRAY);
                    question = null;
                    arraylist = null;
                } else {
                    cbXuan.setChecked(false);
                    Intent myintent = new Intent(activity, TjrsocialchoiceActivity.class);
                    myintent.putExtras(bundle);
                    activity.startActivityForResult(myintent, 554);
                }
            } else if (id == R.id.lysd) {
                Intent myintent = new Intent(activity, TjrsocialchoiceActivity.class);
                myintent.putExtras(bundle);
                activity.startActivityForResult(myintent, 554);
            } else if (id == R.id.btndel) {
                onActivityResult(553, Activity.RESULT_OK, activity.getIntent());
            }
        }
    }

    public void startEditImageTask() {
        CommonUtil.cancelAsyncTask(editImageTask);
        editImageTask = (EditImageTask) new EditImageTask().executeParams();
    }

    private class EditImageTask extends BaseAsyncTask<Void, Void, Void> {
        private Bundle ibundle;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity.showProgressDialog(activity.getResources().getString(R.string.loading_date_message));
        }

        @Override
        protected Void doInBackground(Void... params) {
            ibundle = bundle;
            ibundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, CommonUtil.bitmapToBytes(bmp));
            // ibundle.putParcelable(TjrSocialShareConfig.KEY_EXTRAS_USER,
            // user);
            ibundle.putInt(TjrSocialShareConfig.KEY_EXTRAS_TYPE, 4);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            activity.dismissProgressDialog();
            CommonUtil.pageJumpToData(activity, TjrsocialCanvasActivity.class, false, false, ibundle);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null && requestCode != PHOTOHRAPH) {
            return;
        }
        switch (requestCode) {
            case PHOTORESOULT:
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    if (photo != null) {
                        CommonUtil.recycleBitmap(buImg);
                        bmp = photo;
                        ispicture("");
                        buImg.setImageBitmap(photo);
                    } else {
                        CommonUtil.showToast(activity, "图片生成出错", Gravity.BOTTOM);
                    }
                } else {
                    CommonUtil.showToast(activity, "系统图片生成出错", Gravity.BOTTOM);
                }
                break;
            case PHOTOHRAPH:
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    // 设置文件保存路径这里放在跟目录下
                    CommonUtil.LogLa(2, "===拍照==");
                    File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
                    startPhotoZoom(Uri.fromFile(picture));
                }
                break;
            case IMAGE_SELECT:
                CommonUtil.LogLa(2, "===图库选取照片==" + data.getData().getPath());
                startPhotoZoom(data.getData());
                break;
            // 获取到@的人
            case 555:
                if (resultCode == 556) {
                    // this.update(data.getExtras());
                } else {
                    edWeibo.append(" @" + data.getExtras().getCharSequence("friends_name").toString());
                }
                break;

            case 554:// 提交问题
                Bundle thebundle = data.getExtras();
                if (thebundle != null) {
                    if (thebundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_QUESTION))
                        question = thebundle.getString(TjrSocialShareConfig.KEY_EXTRAS_QUESTION);
                    if (thebundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_QUESTION_ARRAY))
                        arraylist = thebundle.getStringArrayList(TjrSocialShareConfig.KEY_EXTRAS_QUESTION_ARRAY);
                    bundle.putStringArrayList(TjrSocialShareConfig.KEY_EXTRAS_QUESTION_ARRAY, arraylist);
                    bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_QUESTION, question);
                }
                if (arraylist != null) {
                    listques.removeAllViewsInLayout();
                    createView(arraylist);
                }
                if (imm.isActive()) Viewsetvisible(false);
                cbXuan.setChecked(true);
                break;
            case 553:
                // filename = null;
                // fileUrl = null;
                rlImage.setVisibility(View.GONE);
                buImg.setImageBitmap(null);
                if (bmp != null && !bmp.isRecycled()) {
                    System.gc();
                    bmp.isRecycled();
                    bmp = null;
                }
                if (!isOtherImg) {
                } else {
                    btnImage.setVisibility(View.VISIBLE);
                }
                // CommonUtil.LogLa(2, " isOtherImg "+isOtherImg +" "+
                // btnImage.getVisibility());

                break;
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 256);
        intent.putExtra("outputY", 256);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, PHOTORESOULT);
    }

    /**
     * 创建问题布局
     *
     * @param arraylist
     */
    private void createView(ArrayList<String> arraylist) {
        Viewsetvisible(true);
        View hoot = activity.inflateView(R.layout.tjr_social_amount_listview_head);
        listques.addView(hoot);
        TextView tvquestion = (TextView) hoot.findViewById(R.id.tvQuestion);
        for (int i = 0; i < arraylist.size() - 1; i++) {
            View item = activity.inflateView(R.layout.tjr_social_amount_listview_item);
            TextView tvTag = (TextView) item.findViewById(R.id.tvTag);
            TextView tvCount = (TextView) item.findViewById(R.id.tvCount);
            char s = (char) (65 + i);
            tvTag.setText(s + ".");
            tvCount.setText(arraylist.get(i));
            listques.addView(item);
        }
        tvquestion.setText("问题: " + arraylist.get(arraylist.size() - 1));
    }

    private void ispicture(String filepath) {
        gvimage.setVisibility(View.GONE);
        rlImage.setVisibility(View.VISIBLE);
        btnImage.setVisibility(View.GONE);
    }

    public interface ShareUICallBack {
        /**
         * 分享weibo
         *
         * @param weiboType 3 组件分享 + 投票;2 组件分享;1纯微博
         * @param text      输入框内容
         * @param voteTitle 投票问题
         * @param voteType  1单选;2多选
         * @param option    选项json格式
         * @param bmp
         */
        public void sendWeibo(int weiboType, String text, Bitmap bmp, String voteTitle, int voteType, String option);
    }

    public EditText getEdWeibo() {
        return edWeibo;
    }

}
