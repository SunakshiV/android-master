package com.procoin.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.procoin.module.chat.util.ChatSmileyParser;
import com.procoin.util.MyClickableSpanUtil;
import com.procoin.util.TextViewFixTouchConsume;
import com.procoin.util.StockTextUtils;

import java.util.ArrayList;

/**
 * Created by zhengmj on 19-2-21.
 * 加入超链接检测
 * 加入长按气泡菜单
 * 加入Tjr表情支持
 */

@SuppressLint("AppCompatCustomView")
public class TjrCustomTextView extends TextView{
    private Context context;
    private TjrPopMenu.OnMenuItemClickListener menuListener;
    private ArrayList<String> optionMenus;
    private TjrPopMenu popMenu;
    private ChatSmileyParser chatSmileyParser;
    private OnLongClickListener longClickListener;
    private int x;
    private int y;
    public void setOnShowMenuListener(TjrPopMenu.OnMenuItemClickListener menuListener, ArrayList<String> optionMenus){
        this.menuListener = menuListener;
        this.optionMenus = optionMenus;
        if (popMenu == null){
            popMenu = new TjrPopMenu(context);
            popMenu.setTrigger(this);
        }
        popMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                TjrCustomTextView.this.setSelected(false);
            }
        });
        popMenu.setMenu(optionMenus);
        popMenu.setOnMenuItemClickLIstner(menuListener);
    }
    public TjrCustomTextView(Context context) {
        super(context);
        this.context = context;
        popMenu = new TjrPopMenu(context);
        popMenu.setTrigger(this);
        init();
    }
    public OnLongClickListener getLongClickListener(){
        return longClickListener;
    }
    public TjrCustomTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }
    private void init(){
        chatSmileyParser = ChatSmileyParser.getInstance(context);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x = (int) motionEvent.getX();
                        y = (int) motionEvent.getY();
                        break;
                }
                return false;
            }
        });
        longClickListener = new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (menuListener!=null){

                    popMenu.setTouchCoordinate(x,y);
                    popMenu.show();
                    TjrCustomTextView.this.setSelected(true);
                }
                return false;
            }
        };
        setOnLongClickListener(longClickListener);
    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }

    public void setCustomText(CharSequence text){
        TextPaint textPaint = getPaint();
        setText(chatSmileyParser.replaceSmallAutoFill(
                MyClickableSpanUtil.getCustomText( text,
                        StockTextUtils.parserTestToStock( text),
                        false, context),textPaint));
        setMovementMethod(TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance());
    }
    public void setCustomTextWithoutWebcheck(CharSequence text){
        setText(chatSmileyParser.replaceSmallWithPadding(text, 0.5, 10));
    }
    public interface OnShowMenuListener{
        void onShowUp(int position);
    }
}
