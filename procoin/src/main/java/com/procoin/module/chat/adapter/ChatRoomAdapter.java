package com.procoin.module.chat.adapter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.BasePlayVoiceAdapter;
import com.procoin.common.constant.CommonConst;
import com.procoin.data.db.TaoJinLuDatabase;
import com.procoin.http.base.Group;
import com.procoin.http.model.User;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.module.chat.util.ChatSmileyParser;
import com.procoin.module.circle.CircleChatViewPagerPhotoViewActivity;
import com.procoin.module.circle.entity.CircleChatEntity;
import com.procoin.module.circle.entity.CircleChatTypeEnum;
import com.procoin.module.circle.entity.VoiceEntity;
import com.procoin.module.circle.entity.parser.CircleKlineBoxParser;
import com.procoin.module.circle.ui.PlayVoiceUtilView;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.DensityUtil;
import com.procoin.util.InflaterUtils;
import com.procoin.util.MyClickableSpanUtil;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockTextUtils;
import com.procoin.util.TextViewFixTouchConsume;
import com.procoin.util.VeDate;
import com.procoin.widgets.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRoomAdapter extends BasePlayVoiceAdapter<CircleChatEntity> implements PlayVoiceUtilView.VoiceLoadAndPlay {
    private ChatRoomActivity context;
    private User user;
    private static final int MAXWIDTH = 130;//????????????????????????dip
    private static final int MAXHEIGHT = 150;//????????????????????????dip
    private static final int MINWIDTHHEIGHT = 100;//???????????????????????????????????????????????????????????????????????????????????????
    private static final int MINWIDTH = 50;//????????????????????????????????????????????????????????????????????????????????????
    private static final int MINHEIGHT = 50;//???????????????????????????????????????????????????????????????????????????????????????
    private int maxWidth;
    private int maxHeight;
    private int minWidthHeight;
    private int minWidth;
    private int minHeight;

//    private DisplayImageOptions optionsHead;//???????????????????????????????????????????????????????????????????????????????????????DisplayImageOptions

    private ChatSmileyParser chatSmileyParser;
    private TaoJinLuDatabase db;
    private TjrBaseDialog reSendMsgDialog;
    private CircleChatEntity reSendMsg;

    private final int[] itemLayouts;
    private int role = -1;


    private final SparseArray<CircleChatEntity> map;//???map??????????????????????????????????????????

    private CircleKlineBoxParser parser;
//    private BoxBeanCheckTask boxBeanCheckTask;

    @Override
    public void setGroup(Group<CircleChatEntity> g) {
        super.setGroup(g);
        if (map.size() > 0) map.clear();
        for (CircleChatEntity entity : g) {
            if (entity.chatMark != 0 && !g.contains(entity.chatMark)) {
                map.put(entity.chatMark, entity);
            }
        }
    }

    @Override
    public void addItemBefore(Group<CircleChatEntity> g) {
        for (CircleChatEntity entity : g) {
            if (entity.chatMark != 0 && !g.contains(entity.chatMark)) {
                map.put(entity.chatMark, entity);
            }
        }
        super.addItemBefore(g);
    }

    @Override
    public void addItem(CircleChatEntity g) {
        if (g == null) return;
        if (g.chatMark != 0) {//??????????????????
            if (map.get(g.chatMark) == null) {
                super.addItem(g);
                notifyDataSetChanged();
                map.put(g.chatMark, g);
            }
        } else {//????????????
            super.addItem(g);
            notifyDataSetChanged();
        }
    }

    @Override
    public void addItem(Group<CircleChatEntity> g) {
        if (g == null || g.size() == 0) return;
        for (CircleChatEntity entity : g) {
            if (entity.chatMark != 0) {//??????????????????
                if (map.get(entity.chatMark) == null) {
                    super.addItem(entity);
                    map.put(entity.chatMark, entity);
                }
            } else {//????????????
                super.addItem(entity);
            }
        }
        notifyDataSetChanged();
    }


    public ChatRoomAdapter(ChatRoomActivity context, User user, int role) {
        super(context, R.drawable.xml_bubbleview_default_src);
        this.context = context;
        this.user = user;
        this.role = role;
        map = new SparseArray<>();
        maxWidth = DensityUtil.dip2px(context, MAXWIDTH);
        maxHeight = DensityUtil.dip2px(context, MAXHEIGHT);
        minWidthHeight = DensityUtil.dip2px(context, MINWIDTHHEIGHT);
        minWidth = DensityUtil.dip2px(context, MINWIDTH);
        minHeight = DensityUtil.dip2px(context, MINHEIGHT);

//        optionsHead = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_head_default_photo).showImageOnFail(R.drawable.ic_head_default_photo).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(5)).bitmapConfig(Bitmap.Config.RGB_565) // default
        // Bitmap.Config.ARGB_8888
//				.showImageOnLoading(defaultRes)		//????????????listView??????????????????																																																														// ??????8888????????????????????????
//                .build();
        chatSmileyParser = ChatSmileyParser.getInstance(context);
        db = ((MainApplication) context.getApplicationContext()).getmDb();
        parser = new CircleKlineBoxParser();
        itemLayouts = new int[]{
                R.layout.circle_chat_item_text_left,//0
                R.layout.circle_chat_item_text_right,//1
                R.layout.circle_chat_item_img_left,//2
                R.layout.circle_chat_item_img_right,//3
                R.layout.circle_chat_item_voice_left,//4
                R.layout.circle_chat_itemt_voice_righ,//5
//                R.layout.circle_chat_item_stock_left,//6
//                R.layout.circle_chat_item_stock_right,//7
                R.layout.circle_chat_item_join_tips,//8  ???2?????????  1??????????????????   2??????????????????????????????????????????????????????????????????????????????????????????
//                R.layout.circle_chat_item_kline_box_left,//9
//                R.layout.circle_chat_item_kline_box_right,//10
                R.layout.circle_chat_item_unread_message,//11 ??????????????????
                R.layout.circle_chat_item_default_json_left, //12
                R.layout.circle_chat_item_default_json_right, //13
//                R.layout.circle_chat_item_kline_treasure_box_left,//14
//                R.layout.circle_chat_item_kline_treasure_box_right//15
        };
    }
//
//    public enum ChatTypeEunm {
//        tjr_text("[text]"), // ??????swith??????default?????????????????????
//        tjr_img("[img]"); // ??????
//        tjr_voice, // ??????
//        tjr_news, // ????????????
//        tjr_paper, // ??????
//        tjr_indexshare, // index??????
//        tjr_f10details, // F10??????
//        tjr_msgstock;// ????????????


//        public String text;
//
//        ChatTypeEunm(String text){
//            this.text=text;
//        }

//    }


    @Override
    public int getViewTypeCount() {
        return itemLayouts.length;
    }

    @Override
    public int getItemViewType(int position) {
        //CircleChatEntity cc???getItem(position);
        CircleChatEntity circleChatEntity = getItem(position);
        String say = circleChatEntity.say;
        Log.d("getItemViewType", "say==" + say);
        int type = 0;
        if (!TextUtils.isEmpty(say)) {
            if (say.startsWith(CircleChatTypeEnum.SAY_TEXT.type)) {
                if (circleChatEntity.userId != user.getUserId()) {
                    type = 0;
                } else {
                    type = 1;
                }

            } else if (say.startsWith(CircleChatTypeEnum.SAY_IMG.type)) {
                if (circleChatEntity.userId != user.getUserId()) {
                    type = 2;
                } else {
                    type = 3;
                }

            } else if (say.startsWith(CircleChatTypeEnum.SAY_VOICE.type)) {
                if (circleChatEntity.userId != user.getUserId()) {
                    type = 4;
                } else {
                    type = 5;
                }

            }
            else if (say.startsWith(CircleChatTypeEnum.SAY_TIP.type)) {
                type = 6;
            }
            else if (say.startsWith(CircleChatTypeEnum.SAY_UNREAD_MESSAGE.type)) {
                type = 7;
            }
            else {
                type = 6;//????????????????????? ???tip?????????????????????????????????????????????Tip??????
            }


        }
        Log.d("getItemViewType", "type==" + type + "  userId==" + circleChatEntity.userId + "   uid==" + user.getUserId());
        return type;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d("CircleChatAdapter", "getView//////////////");
        ViewHolderBase holder = null;
//        ViewHolderTextLeft holder0 = null;
//        ViewHolderTextRight holder1 = null;
//        ViewHolderImgLeft holder2 = null;
//        ViewHolderImgRight holder3 = null;

        int type = getItemViewType(position);
        Log.d("type", "type==" + type);
        if (convertView == null) {
            convertView = InflaterUtils.inflateView(context, itemLayouts[type]);
            switch (type) {
                case 0:
                    holder = new ViewHolderTextLeft(convertView);
                    convertView.setTag(holder);
                    break;
                case 1:
                    holder = new ViewHolderTextRight(convertView);
                    convertView.setTag(holder);
                    break;
                case 2:
                    holder = new ViewHolderImgLeft(convertView);
                    convertView.setTag(holder);
                    break;
                case 3:
                    holder = new ViewHolderImgRight(convertView);
                    convertView.setTag(holder);
                    break;
                case 4:
                    holder = new ViewHolderVoiceLeft(convertView);
                    convertView.setTag(holder);
                    break;
                case 5:
                    holder = new ViewHolderVoiceRight(convertView);
                    convertView.setTag(holder);
                    break;
                case 6://tip????????????????????????
                    holder = new ViewHolderTip(convertView);
                    convertView.setTag(holder);
                    break;
                case 7://?????????????????????????????????????????????
                    context.hideNews();
                    return convertView;

                default:
                    break;


            }
        } else {
            holder = (ViewHolderBase) convertView.getTag();
        }
        if (holder != null) holder.setData(position);
        return convertView;
    }


    public void changeSendState(CircleChatEntity entity) {
        if (getGroup() != null && getGroup().size() > 0) {
            Group<CircleChatEntity> group = getGroup();
            CircleChatEntity item = null;
            int index = -1;
            for (int i = group.size() - 1; i >= 0; i--) {//????????????????????????
                item = group.get(i);
                if (!TextUtils.isEmpty(entity.verify) && !TextUtils.isEmpty(item.verify) && entity.verify.equals(item.verify)) {
//                    item.state = state;
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                group.set(index, entity);
                notifyDataSetChanged();

            }

        }
    }

    public void changeSendState(Group<CircleChatEntity> entitylist) {
        if (entitylist != null && entitylist.size() > 0) {
            for (CircleChatEntity entity : entitylist) {
                if (getGroup() != null && getGroup().size() > 0) {
                    Group<CircleChatEntity> group = getGroup();
                    CircleChatEntity item = null;
                    int index = -1;
                    for (int i = group.size() - 1; i >= 0; i--) {//????????????????????????
                        item = group.get(i);
                        if (!TextUtils.isEmpty(entity.verify) && !TextUtils.isEmpty(item.verify) && entity.verify.equals(item.verify)) {
//                    item.state = state;
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        group.set(index, entity);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }

    class ViewHolderBase {
        CircleImageView ivHead;
        TextView tvTime;

        public ViewHolderBase(View convertView) {
            ivHead = (CircleImageView) convertView.findViewById(R.id.ivHead);

            tvTime = (TextView) convertView.findViewById(R.id.tvTime);
        }

        public void setData(int pos) {
            final CircleChatEntity item = getItem(pos);
            if (ivHead != null) {
                Log.d("headurl", "headurl==" + item.headurl + "  userId==" + item.userId + "  user_name==" + item.name);
                if (TextUtils.isEmpty(item.headurl)) {//??????????????????????????????????????????
                    ivHead.setImageResource(R.drawable.ic_default_head);
                } else {
                    displayImageForHead(item.headurl, ivHead);
                }


            }
//                displayAddVImage(ivHead, item.headurl, item.isVip, null, optionsHead);
//                Log.d("setData", " pos==" + pos + " ivHead==" + ivHead);

//            Log.d("ViewHolderBase", "time==" + item.createTime);

//            Log.d("154","showTime == "+item.showTime +" createTime == "+item.createTime);

            if (item.showTime || showTime(pos)) {
                item.showTime = true;//???????????????????????????????????????????????????????????????????????????
                tvTime.setVisibility(View.VISIBLE);
                tvTime.setText(DateUtils.getChatTimeFormat3(new Date(Long.parseLong(item.createTime)*1000)));
            } else {
                tvTime.setVisibility(View.GONE);
            }
            if (ivHead != null) {//??????tip????????????????????????????????????
                ivHead.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        PersonalHomepageActivity.pageJumpThis(context,item.userId);
                    }
                });
//                ivHead.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        context.addAt2EditText(item.userId, item.user_name);
//                        return true;
//                    }
//                });
            }

        }

        private boolean showTime(int currPos) {
            if (currPos == 0) return true;
            String lastTime = getItem(currPos - 1).createTime;
            String time = getItem(currPos).createTime;
            if (TextUtils.isEmpty(time)) return false;
            try {
                if (!TextUtils.isEmpty(lastTime)) {
                    Log.d("isShowTime", "currPos==" + currPos);
                    return VeDate.isShowTime(lastTime, time);
                }
            } catch (Exception e) {
            }
            return true;
        }
    }

    class ViewHolderTextLeft extends ViewHolderBase {
//        BubbleTextVew tvContent;

        TextView tvContent;

        TextView tvName;

//        TextView tvRoleName;

        public ViewHolderTextLeft(View convertView) {
            super(convertView);
            tvContent = (TextView) convertView.findViewById(R.id.tvContent);
            tvName = (TextView) convertView.findViewById(R.id.tvName);
//            tvRoleName = (TextView) convertView.findViewById(R.id.tvRoleName);
        }

        public void setData(int pos) {
            CircleChatEntity entity = getItem(pos);
            String say = entity.say;
            Log.d("setData", "say==" + say);
            if (!TextUtils.isEmpty(say))
                say = say.replace(CircleChatTypeEnum.SAY_TEXT.type, "");
            tvContent.setText(chatSmileyParser.replaceSamll(MyClickableSpanUtil.getCustomText(say, StockTextUtils.parserTestToStock(say), false, context), 0.5));
            tvContent.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());

            tvName.setText(entity.name);
//            if (tvRoleName != null) {
//                if (!TextUtils.isEmpty(entity.roleName)) {
//                    tvRoleName.setVisibility(View.VISIBLE);
//                    tvRoleName.setText(entity.roleName);
//                } else {
//                    tvRoleName.setVisibility(View.GONE);
//                }
//            }
            tvContent.setOnLongClickListener(new MyOnLongClickListener(entity));
            super.setData(pos);
        }
    }

    class ViewHolderTextRight extends ViewHolderBase {

        //        BubbleTextVew tvContent;
        TextView tvContent;
        ProgressBar pb;
        ImageView ivReSend;


        public ViewHolderTextRight(View convertView) {

            super(convertView);

            tvContent = (TextView) convertView.findViewById(R.id.tvContent);

            pb = (ProgressBar) convertView.findViewById(R.id.pb);
            ivReSend = (ImageView) convertView.findViewById(R.id.ivReSend);
        }

        public void setData(int pos) {
            final CircleChatEntity entity = getItem(pos);
            String say = entity.say;
            if (!TextUtils.isEmpty(say))
                say = say.replace(CircleChatTypeEnum.SAY_TEXT.type, "");
            tvContent.setText(chatSmileyParser.replaceSamll(MyClickableSpanUtil.getCustomText(say, StockTextUtils.parserTestToStock(say), false, context), 0.5));
            tvContent.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());

            if (entity.state == 1) {
                pb.setVisibility(View.VISIBLE);
                ivReSend.setVisibility(View.GONE);
            } else if (entity.state == 0) {
                pb.setVisibility(View.GONE);
                ivReSend.setVisibility(View.GONE);
            } else {
                pb.setVisibility(View.GONE);
                ivReSend.setVisibility(View.VISIBLE);
            }
            tvContent.setOnLongClickListener(new MyOnLongClickListener(entity));
            ivReSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReSendMsgDialog(entity);
                }
            });
            super.setData(pos);
        }
    }


    class ViewHolderImgLeft extends ViewHolderBase {
        ImageView bubbleImage;
        TextView tvName;

//        TextView tvRoleName;


        public ViewHolderImgLeft(View convertView) {

            super(convertView);
            bubbleImage = (ImageView) convertView.findViewById(R.id.bubbleImage);
            tvName = (TextView) convertView.findViewById(R.id.tvName);
//            tvRoleName = (TextView) convertView.findViewById(R.id.tvRoleName);


        }

        public void setData(int pos) {
            CircleChatEntity entity = getItem(pos);
            tvName.setText(entity.name);
            setImgaeWidthHeight(bubbleImage, entity.say);
            bubbleImage.setOnLongClickListener(new MyOnLongClickListener(entity));
            bubbleImage.setOnClickListener(new MyImgOnClick(entity));

//            if (tvRoleName != null) {
//                if (!TextUtils.isEmpty(entity.roleName)) {
//                    tvRoleName.setVisibility(View.VISIBLE);
//                    tvRoleName.setText(entity.roleName);
//                } else {
//                    tvRoleName.setVisibility(View.GONE);
//                }
//            }
            super.setData(pos);
        }
    }

    private void setImgaeWidthHeight(ImageView bubbleImage, String say) {
        if (TextUtils.isEmpty(say)) return;
        if (say.startsWith(CircleChatTypeEnum.SAY_IMG.type)) {
            say = say.replace(CircleChatTypeEnum.SAY_IMG.type, "");
        }
        String[] values = say.split(",");
        String imgUrl = "";
        int imgWidth = 0;
        int imgHeight = 0;
        if (values.length == 3) {
            imgUrl = values[0];
            imgHeight = Integer.parseInt(values[1]);
            imgWidth = Integer.parseInt(values[2]);
        } else {
            return;
        }
        if (!TextUtils.isEmpty(imgUrl)) {
            Log.d("bubbleImage", "imgUrl==" + imgUrl);
            displayImage(imgUrl, bubbleImage);
        }
        int imageViewWidth = bubbleImage.getWidth();
        int imageViewHeight = bubbleImage.getHeight();
        Log.d("bubbleImage", "maxWidth=" + maxWidth + " maxHeight=" + maxHeight + " imgWidth=" + imgWidth + " imgHeight=" + imgHeight);
        if (imgWidth != 0 && imgHeight != 0) {
            if (imgWidth > imgHeight) {//????????????????????? ???????????????????????????
                if (imgWidth < maxWidth) {//???????????????????????????????????????????????????????????????????????????????????????????????????
                    if (imgHeight < minHeight) {//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        imageViewHeight = minHeight;
                        imageViewWidth = Math.min(imageViewHeight * imgWidth / imgHeight, maxWidth);
//                        bubbleImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    } else {
                        imageViewWidth = imgWidth;
                        imageViewHeight = imgHeight;
//                        bubbleImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                } else if (imgWidth > maxWidth) {//?????????????????????????????????????????????????????????????????????????????????????????????????????????
                    imageViewWidth = maxWidth;
                    imageViewHeight = imgHeight * imageViewWidth / imgWidth;
//                    bubbleImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (imageViewHeight < minHeight) {//?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                        imageViewHeight = minHeight;
                        imageViewWidth = Math.min(imageViewHeight * imgWidth / imgHeight, maxWidth);//??????????????????
//                        bubbleImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }


                } else {//?????????????????????
                    if (imgWidth < minWidthHeight) {
                        imageViewWidth = imageViewHeight = minWidthHeight;
                    } else {
                        imageViewWidth = imageViewHeight = Math.min(imgWidth, maxWidth);
                    }
//                    bubbleImage.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            } else if (imgWidth < imgHeight) {
                if (imgHeight < maxHeight) {
                    if (imgWidth < minWidth) {
                        imageViewWidth = minWidth;
                        imageViewHeight = Math.min(imgHeight * imageViewWidth / imgWidth, maxHeight);
//                        bubbleImage.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    } else {
                        imageViewHeight = imgHeight;
                        imageViewWidth = imgWidth;
//                        bubbleImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    }

                } else if (imgHeight > maxHeight) {
                    imageViewHeight = maxHeight;
                    imageViewWidth = imgWidth * maxHeight / imgHeight;
//                    bubbleImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    if (imageViewWidth < minWidth) {
                        imageViewWidth = minWidth;
                        imageViewHeight = Math.min(imgHeight * imageViewWidth / imgWidth, maxHeight);
//                        bubbleImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }

                } else {
//                    Math.max(Math.min(imgWidth, maxWidth))
//                    imageViewWidth = imageViewHeight = Math.min(imgWidth, minWidthHeight);
                    if (imgWidth < minWidthHeight) {
                        imageViewWidth = imageViewHeight = minWidthHeight;
                    } else {
                        imageViewWidth = imageViewHeight = Math.min(imgWidth, maxWidth);
                    }
//                    bubbleImage.setScaleType(ImageView.ScaleType.FIT_XY);
                }

            } else {
//                imageViewWidth = imageViewHeight = Math.min(imgWidth, minWidthHeight);
                if (imgWidth < minWidthHeight) {
                    imageViewWidth = imageViewHeight = minWidthHeight;
                } else {
                    imageViewWidth = imageViewHeight = Math.min(imgWidth, maxWidth);
                }
//                bubbleImage.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
        Log.d("bubbleImage", "imageViewWidth=" + imageViewWidth + " imageViewHeight=" + imageViewHeight);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bubbleImage.getLayoutParams();
//        lp.width = imageViewWidth + (int) bubbleImage.getmArrowWidth();//????????????????????????????????????????????????????????????????????????
//        lp.height=(imageViewWidth+(int) bubbleImage.getmArrowWidth())*imageViewHeight/imageViewWidth;
        lp.height = imageViewHeight;
        lp.width = imageViewWidth;
        bubbleImage.setLayoutParams(lp);
        bubbleImage.postInvalidate();
    }

    class ViewHolderImgRight extends ViewHolderBase {
        ImageView bubbleImage;
        ProgressBar pb;
        ImageView ivReSend;


        public ViewHolderImgRight(View convertView) {
            super(convertView);
            bubbleImage = (ImageView) convertView.findViewById(R.id.bubbleImage);
            pb = (ProgressBar) convertView.findViewById(R.id.pb);
            ivReSend = (ImageView) convertView.findViewById(R.id.ivReSend);

        }

        public void setData(int pos) {
            final CircleChatEntity entity = getItem(pos);
            setImgaeWidthHeight(bubbleImage, entity.say);
            if (entity.state == 1) {
                pb.setVisibility(View.VISIBLE);
                ivReSend.setVisibility(View.GONE);
            } else if (entity.state == 0) {
                pb.setVisibility(View.GONE);
                ivReSend.setVisibility(View.GONE);
            } else {
                pb.setVisibility(View.GONE);
                ivReSend.setVisibility(View.VISIBLE);
            }
            bubbleImage.setOnLongClickListener(new MyOnLongClickListener(entity));
            bubbleImage.setOnClickListener(new MyImgOnClick(entity));
            ivReSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReSendMsgDialog(entity);
                }
            });
            super.setData(pos);
        }
    }

    @Override
    public void playNext(int tryPlayPos) {
        if (tryPlayPos >= getCount()) {
            return;
        }
        CircleChatEntity chat = getItem(tryPlayPos);
        if (CircleChatTypeEnum.isVoice(chat.say)) {
            if (chat.userId != user.getUserId()) {
                if (chat.voiceIsRed == 1) {
                    context.playNext(tryPlayPos);
                } else {
                    tryPlayPos++;
                    playNext(tryPlayPos);
                }
            } else {
                tryPlayPos++;
                playNext(tryPlayPos);
            }

        } else {
            tryPlayPos++;
            playNext(tryPlayPos);
        }

    }

    @Override
    public void play(File file, int pos, int followPos) {
        CircleChatEntity entity = getItem(pos);
        if (CircleChatTypeEnum.isVoice(entity.say)) {
            if (entity.voiceIsRed == 1) {
                entity.voiceIsRed = 0;
                notifyDataSetChanged();
                db.clearPrivateChatVoiceRed(user.getUserId(), entity.chatTopic, entity.chatMark);
            }
        }
        super.play(file, pos, followPos);
    }

    class ViewHolderVoiceLeft extends ViewHolderBase {
        TextView tvName;
        PlayVoiceUtilView rlVoice;
        ImageView ivVoiceNoRed;
//        TextView tvRoleName;


        public ViewHolderVoiceLeft(View convertView) {
            super(convertView);
            tvName = (TextView) convertView.findViewById(R.id.tvName);
            rlVoice = (PlayVoiceUtilView) convertView.findViewById(R.id.rlVoice);
            rlVoice.setBgAnimOnPalying(false);
            ivVoiceNoRed = (ImageView) convertView.findViewById(R.id.ivVoiceNoRed);

//            tvRoleName = (TextView) convertView.findViewById(R.id.tvRoleName);

//            int layout= R.layout.circle_chat_item_voice_left;
        }

        public void setData(int pos) {
            CircleChatEntity entity = getItem(pos);
            tvName.setText(entity.name);

//            if (tvRoleName != null) {
//                if (!TextUtils.isEmpty(entity.roleName)) {
//                    tvRoleName.setVisibility(View.VISIBLE);
//                    tvRoleName.setText(entity.roleName);
//                } else {
//                    tvRoleName.setVisibility(View.GONE);
//                }
//            }

            if (entity.voiceIsRed == 1) {
                ivVoiceNoRed.setVisibility(View.VISIBLE);
            } else {
                ivVoiceNoRed.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(entity.say)) {
                String say = entity.say;
                if (say.startsWith(CircleChatTypeEnum.SAY_VOICE.type)) {
                    say = say.replace(CircleChatTypeEnum.SAY_VOICE.type, "");
                }
                String[] values = say.split(",");
                String voiceUrl = "";
                int length = 0;
                if (values.length == 2) {
                    voiceUrl = values[0];
                    length = Integer.parseInt(values[1]);
                } else {
                    return;
                }

                rlVoice.setVoiceEntityAndCallBack(new VoiceEntity(voiceUrl, length), ChatRoomAdapter.this, pos, 0, playingPos, 0);
//                if(tryPlayPos==pos){
//                    rlVoice.performClick();
//                }
                rlVoice.setOnLongClickListener(new MyOnLongClickListener(entity));
            }
            super.setData(pos);
        }
    }

    class ViewHolderVoiceRight extends ViewHolderBase {
        PlayVoiceUtilView rlVoice;
        ProgressBar pb;
        ImageView ivReSend;

        public ViewHolderVoiceRight(View convertView) {
            super(convertView);
            rlVoice = (PlayVoiceUtilView) convertView.findViewById(R.id.rlVoice);
            rlVoice.setBgAnimOnPalying(false);
            pb = (ProgressBar) convertView.findViewById(R.id.sendingPb);
            ivReSend = (ImageView) convertView.findViewById(R.id.ivReSend);
        }

        public void setData(int pos) {
            final CircleChatEntity entity = getItem(pos);
            if (!TextUtils.isEmpty(entity.say)) {
                String say = entity.say;
                if (say.startsWith(CircleChatTypeEnum.SAY_VOICE.type)) {
                    say = say.replace(CircleChatTypeEnum.SAY_VOICE.type, "");
                }
                String[] values = say.split(",");
                String voiceUrl = "";
                int length = 0;
                if (values.length == 2) {
                    voiceUrl = values[0];
                    length = Integer.parseInt(values[1]);
                } else {
                    return;
                }

                rlVoice.setVoiceEntityAndCallBack(new VoiceEntity(voiceUrl, length), ChatRoomAdapter.this, pos, 0, playingPos, 0);
//                rlVoice.setOnLongClickListener();
//                setOnLongClick(rlVoice, entity);
                rlVoice.setOnLongClickListener(new MyOnLongClickListener(entity));
            }
            if (entity.state == 1) {
                pb.setVisibility(View.VISIBLE);
                ivReSend.setVisibility(View.GONE);
            } else if (entity.state == 0) {
                pb.setVisibility(View.GONE);
                ivReSend.setVisibility(View.GONE);
            } else {
                pb.setVisibility(View.GONE);
                ivReSend.setVisibility(View.VISIBLE);
            }
            ivReSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReSendMsgDialog(entity);
                }
            });
            super.setData(pos);
        }
    }

//    class ViewHolderFdmLeft extends ViewHolderBase {
////        int s = R.layout.circle_chat_item_stock_right;
//
//        private TextView tvName;
//        private TextView tvRoleName;
//
//        private TextView tvJc;
//        private TextView tvFullcode;
//        private TextView tvCurrPrice;
//        private TextView tvRate;
//        private TextView tvStockTime;
//
//        private LinearLayout llStock;
//
//        public ViewHolderFdmLeft(View convertView) {
//            super(convertView);
//            tvName = (TextView) convertView.findViewById(R.id.tvName);
//            tvRoleName = (TextView) convertView.findViewById(R.id.tvRoleName);
//            tvJc = (TextView) convertView.findViewById(R.id.tvJc);
//            tvFullcode = (TextView) convertView.findViewById(R.id.tvFullcode);
//            tvCurrPrice = (TextView) convertView.findViewById(R.id.tvCurrPrice);
//            tvRate = (TextView) convertView.findViewById(R.id.tvRate);
//            tvStockTime = (TextView) convertView.findViewById(R.id.tvStockTime);
//            llStock = (LinearLayout) convertView.findViewById(R.id.llStock);
//        }
//
//        public void setData(int pos) {
//            CircleChatEntity chat = getItem(pos);
//            if (tvName != null) tvName.setText(chat.user_name);
//
//            if (tvRoleName != null) {
//                if (!TextUtils.isEmpty(chat.roleName)) {
//                    tvRoleName.setVisibility(View.VISIBLE);
//                    tvRoleName.setText(chat.roleName);
//                } else {
//                    tvRoleName.setVisibility(View.GONE);
//                }
//            }
//
//            String say = chat.say;
//            if (!TextUtils.isEmpty(say)) say = say.replace(CircleChatTypeEnum.SAY_FDM.type, "");
//            String[] value = say.split(",");
//            tvJc.setText(value[0]);
//            tvFullcode.setText(value[1]);
//            tvCurrPrice.setText(value[2]);
////            tvCurrPrice.setTextColor(CommonUtil.getRateColorNew(Double.parseDouble(value[3])));
//            tvRate.setTextColor(CommonUtil.getRateColorNew(Double.parseDouble(value[3])));
//
//            double rate = Double.parseDouble(value[3]);
//            if (rate > 0) {
//                tvRate.setText("+" + value[3] + "%");
//            } else {
//                tvRate.setText(value[3] + "%");
//            }
//
//            if (value != null && value.length == 5) {
//                tvStockTime.setText(VeDate.getStringDateOfString(value[4], "yyyyMMddHHmmss", "MM/dd HH:mm"));
//            }
//            llStock.setOnClickListener(new MyStockOnClick(value[0], value[1]));
//            super.setData(pos);
//        }
//    }
//
//    class ViewHolderFdmRight extends ViewHolderFdmLeft {
//        ProgressBar pb;
//        ImageView ivReSend;
//
//        public ViewHolderFdmRight(View convertView) {
//            super(convertView);
//            pb = (ProgressBar) convertView.findViewById(R.id.pb);
//            ivReSend = (ImageView) convertView.findViewById(R.id.ivReSend);
//        }
//
//        public void setData(int pos) {
//            final CircleChatEntity entity = getItem(pos);
//            if (entity.state == 1) {
//                pb.setVisibility(View.VISIBLE);
//                ivReSend.setVisibility(View.GONE);
//            } else if (entity.state == 0) {
//                pb.setVisibility(View.GONE);
//                ivReSend.setVisibility(View.GONE);
//            } else {
//                pb.setVisibility(View.GONE);
//                ivReSend.setVisibility(View.VISIBLE);
//            }
//
//            ivReSend.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    showReSendMsgDialog(entity);
//                }
//            });
//            super.setData(pos);
//
//        }
//
//    }

    class ViewHolderTip extends ViewHolderBase {
//        int s=R.layout.circle_chat_item_join_tips;

        TextView tvJoinTips;


        public ViewHolderTip(View convertView) {
            super(convertView);
            tvJoinTips = (TextView) convertView.findViewById(R.id.tvJoinTips);
        }

        public void setData(int pos) {
            CircleChatEntity entity = getItem(pos);
            String say = entity.say;
            Log.d("setData", "say==" + say);
            if (!TextUtils.isEmpty(say)) {
                if (say.startsWith(CircleChatTypeEnum.SAY_TIP.type)) {//?????????2?????????
                    say = say.replace(CircleChatTypeEnum.SAY_TIP.type, "");
//                    tvJoinTips.setText(Html.fromHtml(say));

                    tvJoinTips.setText(MyClickableSpanUtil.getCustomText(say, StockTextUtils.parserTestToStock(say), false, context));
                    tvJoinTips.setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
//                    tvJoinTips.setText(Html.fromHtml("<font color=\"#00FF00\" size=\"20\">??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????</font>"));

                } else {
                    tvJoinTips.setText(entity.name + " ??????????????????,????????????????????????");
                }
            }
            super.setData(pos);
        }
    }

    private AlertDialog dilog;

    private class MyOnLongClickListener implements View.OnLongClickListener {
        CircleChatEntity entity;

        public MyOnLongClickListener(CircleChatEntity entity) {
            this.entity = entity;
        }

        @Override
        public boolean onLongClick(View v) {
            if (v instanceof TextView) {
                Spannable span = (Spannable) ((TextView) v).getText();
                Selection.removeSelection(span);
            }

            String[] items = null;
            String say = entity.say;
            if (entity.type == 0) {//??????,???????????????????????????????????????
                items = new String[]{"??????", "??????"};
            } else {
                items = new String[]{"??????"};
            }
//            else if (entity.type == 1) {//??????
//                items = new String[]{"??????"};
//            } else if (entity.type == 2) {//??????
//                items = new String[]{"??????"};
//            }else if (entity.type == 3) {//??????
//                items = new String[]{"??????"};
//            }else if (entity.type == 4) {//??????
//                items = new String[]{"??????"};
//            }else if (entity.type == 5) {//??????
//                items = new String[]{"??????"};
//            }

            if (dilog != null && dilog.isShowing()) {
//                dilog.dismiss();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("??????").setCancelable(true).setItems(items, new MyDialogInterface(entity));
                dilog = builder.create();
                dilog.setCanceledOnTouchOutside(true);
                dilog.show();
            }

            return false;
        }
    }

    private class MyImgOnClick implements View.OnClickListener {
        private CircleChatEntity entity;

        public MyImgOnClick(CircleChatEntity entity) {
            this.entity = entity;
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(CommonConst.CHAT_TOPIC, entity.chatTopic);
            bundle.putInt(CommonConst.CIRCLECHATMARK, entity.chatMark);
            bundle.putBoolean(CommonConst.ISPRIVATECHAT, true);
            PageJumpUtil.pageJumpToData(context, CircleChatViewPagerPhotoViewActivity.class, bundle);

        }
    }

//    private class MyStockOnClick implements View.OnClickListener {
//        private String jc;
//        private String fdm;
//
//        public MyStockOnClick(String jc, String fdm) {
//            this.jc = jc;
//            this.fdm = fdm;
//        }
//
//        @Override
//        public void onClick(View v) {
//            CommonUtil.jumpStockByFdm(context, fdm, jc);
//        }
//    }

    private class MyDialogInterface implements DialogInterface.OnClickListener {
        private CircleChatEntity entity;

        public MyDialogInterface(CircleChatEntity entity) {
            this.entity = entity;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case 0:
                    if (entity.type == 0) {
                        String text = filterAtName(entity.say.replace(CircleChatTypeEnum.SAY_TEXT.type, ""));
                        CommonUtil.copyText(context, text);
                    } else {
                        int result = db.delPrivateChat(user.getUserId(), entity.chatTopic, entity.chatMark);
                        Log.d("removeItem", "result==" + result);
                        if (result > 0) {
                            removeItem(entity);
                            notifyDataSetChanged();
                        }
                    }
                    break;
                case 1:
                    int result = db.delPrivateChat(user.getUserId(), entity.chatTopic, entity.chatMark);
                    Log.d("removeItem", "result==" + result);
                    if (result > 0) {
                        removeItem(entity);
                        notifyDataSetChanged();
                    }
                    break;
            }

        }
    }

    public String filterAtName(String say) {
        List<StockTextUtils.MatchEntity> map = new ArrayList<StockTextUtils.MatchEntity>();
        //@??????
        String regex3 = CommonConst.AT_MATCHES;//
        Pattern pa3 = Pattern.compile(regex3, Pattern.DOTALL); //
        Matcher ma3 = pa3.matcher(say);
        while (ma3.find()) {
            if (ma3.groupCount() == 2) {
                Log.d("Matcher", "ma3.group(0)==" + ma3.group(0) + " ma3.group(1)==" + ma3.group(1) + " ma3.group(2)==" + ma3.group(2));
                map.add(new StockTextUtils.MatchEntity(ma3.group(0), ma3.group(1)));
                if (context.namesMap != null) {
                    context.namesMap.put(ma3.group(1), ma3.group(0));
                }
                if (context.etNamesList != null) {
                    context.etNamesList.add(new StockTextUtils.MatchEntity(ma3.group(1), ma3.group(0)));
                }
            }
        }
        int at_from_start = 0;
        if (map.size() > 0) {
            SpannableStringBuilder ss = new SpannableStringBuilder(say);
            for (StockTextUtils.MatchEntity entry : map) {
                if (entry.getValue() != null) {
                    if (entry.getValue().startsWith("@")) {
                        int vlen = entry.getKey().length();
                        int in = ss.toString().indexOf(entry.getKey(), at_from_start);
                        Log.d("getCustomText", "key==" + entry.getKey());
                        Log.d("getCustomText", "value==" + entry.getValue());
                        String value = entry.getValue();
                        ss.replace(in, in + vlen, value);
                        at_from_start = in + value.length();
                    }
                }
            }
            return ss.toString();
        }
        return say;
    }


    protected void showReSendMsgDialog(CircleChatEntity entity) {
        this.reSendMsg = entity;
        if (reSendMsgDialog == null) {
            reSendMsgDialog = new TjrBaseDialog(context) {
                @Override
                public void setDownProgress(int progress) {

                }

                @Override
                public void onclickOk() {
                    dismiss();
                    context.reSendMsg(reSendMsg);
                }

                @Override
                public void onclickClose() {
                    dismiss();

                }
            };
            reSendMsgDialog.setCancelable(false);
            reSendMsgDialog.setTvTitle("??????");
            reSendMsgDialog.setMessage("?????????????????????");
            reSendMsgDialog.setBtnColseText("??????");
            reSendMsgDialog.setBtnOkText("??????");
        }
        if (!reSendMsgDialog.isShowing()) reSendMsgDialog.show();
    }

}
