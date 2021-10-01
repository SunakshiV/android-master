package com.procoin.module.circle.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.module.circle.entity.CircleChatMore;
import com.procoin.R;
import com.procoin.util.InflaterUtils;

import java.util.List;

public class MoreFragment extends Fragment {
    private OnItemClickListener onItemClickLister;
    private List<CircleChatMore> data;


    public static MoreFragment newInstance() {
        MoreFragment moreFragment = new MoreFragment();
//        moreFragment.data = data;
//        moreFragment.onItemClickLister = onItemClickLister;
//        Log.d("MoreFragment", "MoreFragment==" + data.size());
        return moreFragment;
    }

    public void setData(List<CircleChatMore> data, OnItemClickListener onItemClickLister) {
        this.data = data;
        this.onItemClickLister = onItemClickLister;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View home = inflater.inflate(R.layout.circle_more_gv, container, false);
        GridView gvMore = (GridView) home.findViewById(R.id.gvMore);
        CircleMoreAdapter moreAdapter = new CircleMoreAdapter(getActivity());
        gvMore.setAdapter(moreAdapter);
        gvMore.setOnItemClickListener(onItemClickLister);
        return home;
    }

    class CircleMoreAdapter extends BaseAdapter {
        private Context context;

        public CircleMoreAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return data != null ? data.size() : 0;
        }

        @Override
        public CircleChatMore getItem(int position) {
            if (data == null) {//碎片销毁之后childGroup为空了，后面再优化，先临时处理
                getActivity().finish();
            }
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = InflaterUtils.inflateView(getActivity(), R.layout.circle_more_item);
            ImageView iv_pic = (ImageView) convertView.findViewById(R.id.iv_pic);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            CircleChatMore circleChatMore = getItem(position);
            if (circleChatMore != null) {
                iv_pic.setImageResource(circleChatMore.imageRes);
                tvTitle.setText(circleChatMore.title);
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
