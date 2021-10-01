package com.procoin.common.base;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.procoin.subpush.ReceiveModel;

import java.util.Observable;

public abstract class TjrBaseSoftKeyBoardActivity extends TJRBaseActionBarSwipeBackObserverActivity {

    private boolean keyboardListenersAttached = false;
    private ViewGroup rootLayout;
    protected InputMethodManager im;
    protected volatile int keyboardHeight;
    private SharedPreferences mPreferences;
    protected final int init_keyboard_height = 450, init_delayed = 300;

    private FrameLayout containerBottom;//这个View的高度就等于键盘的高度
    private EditText msgEdit;//这个是eidtText  ，用于焦点的设置

    private OnGlobalLayoutListener keyboardLayoutListener = new OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            int statusBarHeight = frame.top;
            int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight() - statusBarHeight;
            int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

                      Log.d("onGlobalLayout","statusBarHeight=="+statusBarHeight);
            Log.d("onGlobalLayout","heightDiff=="+heightDiff);
            Log.d("onGlobalLayout","rootLayout.getRootView().getHeight()=="+rootLayout.getRootView().getHeight());
            Log.d("onGlobalLayout","contentViewTop=="+contentViewTop);

            Log.d("onGlobalLayout","========================");

            if (heightDiff <= contentViewTop) {
                onHideKeyboard();
            } else {
                int keyboardHeight = heightDiff - contentViewTop;
                if ( keyboardHeight > init_keyboard_height) {
                    onShowKeyboard(keyboardHeight);
                    saveKeyboardHeight(keyboardHeight);
//				Intent intent = new Intent("KeyboardHeight");
//				intent.putExtra("KeyboardHeight", keyboardHeight);
//				LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(TjrBaseSoftKeyBoardActivity.this);
//				broadcastManager.sendBroadcast(intent);
//                    if (keyboardHeight != TjrBaseSoftKeyBoardActivity.this.keyboardHeight) {
//                        saveKeyboardHeight(keyboardHeight);
//                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences("keyboard_height", MODE_PRIVATE);//Context.CONTEXT_IGNORE_SECURITY
        keyboardHeight = mPreferences.getInt("height", init_keyboard_height);
    }

    protected void saveKeyboardHeight(int keyboardHeight) {
        if (mPreferences == null) return;

        this.keyboardHeight = keyboardHeight;
        containerBottom.getLayoutParams().height = keyboardHeight;
        Editor editor = mPreferences.edit();
        editor.putInt("height", keyboardHeight);
        editor.commit();
    }

    protected void onShowKeyboard(int keyboardHeight) {
//        msgEdit.requestFocus();
        Log.d("keyBoard","onShowKeyboard....."+keyboardHeight);
    }

    protected void onHideKeyboard() {
//        msgEdit.clearFocus();

        Log.d("keyBoard", "onHideKeyboard....." );
    }

    protected void attachKeyboardListeners(ViewGroup rootLayout, FrameLayout containerBottom, EditText msgEdit) {
        if (keyboardListenersAttached) {
            return;
        }
        im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        this.rootLayout = rootLayout;
        this.containerBottom = containerBottom;
        this.msgEdit = msgEdit;
        rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
        keyboardListenersAttached = true;
    }

    public void openSoftKeyboard() {
        if (im == null) return;
        im.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
    }

    public void closeSoftKeyboard() {
        if (im == null) return;
        im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (keyboardListenersAttached) {
            if (Build.VERSION.SDK_INT < 16) {
                removeLayoutListenerPre16(rootLayout.getViewTreeObserver(), keyboardLayoutListener);
            } else {
                removeLayoutListenerPost16(rootLayout.getViewTreeObserver(), keyboardLayoutListener);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void removeLayoutListenerPre16(ViewTreeObserver observer, OnGlobalLayoutListener listener) {
        observer.removeGlobalOnLayoutListener(listener);
    }

    @SuppressLint("NewApi")
    private void removeLayoutListenerPost16(ViewTreeObserver observer, OnGlobalLayoutListener listener) {
        observer.removeOnGlobalLayoutListener(listener);
    }

    @Override
    public void update(Observable observable, Object data) {
        super.update(observable, data);
    }

    @Override
    protected void handlerMsg(ReceiveModel model) {

    }
}
