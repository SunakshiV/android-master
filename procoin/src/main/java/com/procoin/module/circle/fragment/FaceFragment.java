package com.procoin.module.circle.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.procoin.module.chat.util.ChatSmileyParser;
import com.procoin.util.InflaterUtils;
import com.procoin.R;

import java.util.List;

public class FaceFragment extends Fragment {
    private int page;
    private List<String> childGroup;
    private int size;
    private ChatSmileyParser chatSmileyParser;
    private OnItemClickListener onItemClickListener;
    private OnBackspaceClickedListener onBackspaceClickedListener;

    public static FaceFragment newInstance(int page, List<String> childGroup, int size, ChatSmileyParser chatSmileyParser, OnItemClickListener onItemClickListener, OnBackspaceClickedListener onBackspaceClickedListener) {
        FaceFragment faceFragment = new FaceFragment();
        faceFragment.page = page;
        faceFragment.childGroup = childGroup;
        Log.d("face", "childSize==" + childGroup.size());
        faceFragment.size = size;
        faceFragment.chatSmileyParser = chatSmileyParser;
        faceFragment.onItemClickListener = onItemClickListener;
        faceFragment.onBackspaceClickedListener = onBackspaceClickedListener;
        return faceFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.circle_face_gv, container, false);
        GridView gvFace = (GridView) home.findViewById(R.id.gvFace);
        CircleFaceAdapter faceAdapter = new CircleFaceAdapter(getActivity());
        gvFace.setAdapter(faceAdapter);
        gvFace.setOnItemClickListener(onItemClickListener);
        return home;
    }

    class CircleFaceAdapter extends BaseAdapter {
        private Context context;

        public CircleFaceAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return size + 1;//加一个backspace
        }

        @Override
        public String getItem(int position) {
            if (childGroup == null) {//碎片销毁之后childGroup为空了，后面再优化，先临时处理
                getActivity().finish();
            }
            if (position > childGroup.size() - 1) return "";
            return childGroup.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = InflaterUtils.inflateView(getActivity(), R.layout.circle_face_item);
                convertView.setPadding(5, 5, 5, 5);
                // convertView.setLayoutParams(new
                // GridView.LayoutParams(dpToPx(context.getResources(), 32),
                // dpToPx(context.getResources(), 32)));
            }
            if (TextUtils.isEmpty(getItem(position))) {
                if (position == getCount() - 1) {//最后一项并且内容为空 ，退格
                    convertView.setVisibility(View.VISIBLE);
                    ((ImageView) convertView.findViewById(R.id.ivFace)).setImageResource(R.drawable.ic_face_backspace);
                    convertView.setOnTouchListener(new RepeatListener(500, 50, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onBackspaceClickedListener != null) {
                                onBackspaceClickedListener.onBackspaceClicked(v);
                            }
                        }
                    }));
                } else {
                    convertView.setVisibility(View.INVISIBLE);
                }
            } else {
                convertView.setVisibility(View.VISIBLE);
                Drawable drawable = context.getResources().getDrawable(chatSmileyParser.getmSmileyToRes().get(getItem(position)));
                ((ImageView) convertView.findViewById(R.id.ivFace)).setImageDrawable(drawable);
            }

            return convertView;
        }
    }


    /**
     * A class, that can be used as a TouchListener on any view (e.g. a Button).
     * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
     * click is fired immediately, next before initialInterval, and subsequent before
     * normalInterval.
     * <p/>
     * <p>Interval is scheduled before the onClick completes, so it has to run fast.
     * If it runs slow, it does not generate skipped onClicks.
     */
    public static class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler();

        private int initialInterval;
        private final int normalInterval;
        private final View.OnClickListener clickListener;

        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        public RepeatListener(int initialInterval, int normalInterval, View.OnClickListener clickListener) {
            if (clickListener == null)
                throw new IllegalArgumentException("null runnable");
            if (initialInterval < 0 || normalInterval < 0)
                throw new IllegalArgumentException("negative interval");

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downView = view;
                    handler.removeCallbacks(handlerRunnable);
                    handler.postAtTime(handlerRunnable, downView, SystemClock.uptimeMillis() + initialInterval);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.removeCallbacksAndMessages(downView);
                    downView = null;
                    return true;
            }
            return false;
        }
    }

    /**
     * 点击退格键 包括长按
     *
     * @author zhengmj
     */
    public interface OnBackspaceClickedListener {
        void onBackspaceClicked(View v);
    }

}
