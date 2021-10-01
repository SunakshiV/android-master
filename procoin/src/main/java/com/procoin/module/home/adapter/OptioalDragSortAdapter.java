package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.http.base.Group;
import com.procoin.module.home.entity.Market;
import com.procoin.util.CommonUtil;
import com.procoin.widgets.OnDragVHListener;
import com.procoin.widgets.OnItemMoveListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 18-10-24.
 */

public class OptioalDragSortAdapter extends BaseImageLoaderRecycleAdapter<Market> implements OnItemMoveListener {

    private Context context;
    private Call<ResponseBody> delCall;
    private String sorts = "";

    private OnStartDragListener onStartDragListener;

//    private List<String> selectedSymbolList = new ArrayList<>();

    public OptioalDragSortAdapter(Context context) {
        this.context = context;
    }

    public void setOnStartDragListener(OnStartDragListener onStartDragListener) {
        this.onStartDragListener = onStartDragListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new MyHoldHolder(LayoutInflater.from(context).inflate(R.layout.home_market_sort_item, parent, false));
    }


    @Override
    public void setGroup(Group<Market> g) {
        super.setGroup(g);
        sorts = getSort();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MyHoldHolder) holder).setData(getItem(position), position);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Market item = getGroup().get(fromPosition);
        getGroup().remove(fromPosition);
        getGroup().add(toPosition, item);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onFinish() {
        notifyDataSetChanged();//完成一定要刷新，否则删除的时候位置会错乱
        sortToka();
    }

    public void setAllChecked(boolean allChecked) {
        if (group != null && group.size() > 0) {
            for (Market market : group) {
                market.checked = allChecked;
            }
            notifyDataSetChanged();
        }

    }

    @Override
    public void onItemDissmiss(int position) {
        notifyDataSetChanged();
    }

    class MyHoldHolder extends RecyclerView.ViewHolder implements OnDragVHListener {
        @BindView(R.id.cbSign)
        CheckBox cbSign;
        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvSubSymbol)
        TextView tvSubSymbol;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvTips)
        TextView tvTips;
        //        @BindView(R.id.tv24H)
//        TextView tv24H;
        @BindView(R.id.ivSort)
        ImageView ivSort;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public MyHoldHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Market data, final int pos) {
            int index = data.symbol.indexOf("/");
            if (index > 0) {
                tvSymbol.setText(CommonUtil.getOriginSymbol(data.symbol));
                tvSubSymbol.setText("/" + CommonUtil.getUnitSymbol(data.symbol));
            } else {
                tvSymbol.setText(data.symbol);
                tvSubSymbol.setText("");
            }
            tvName.setText(data.name);
            if (!TextUtils.isEmpty(data.tip)) {
                tvTips.setVisibility(View.VISIBLE);
                tvTips.setText(data.tip);
            } else {
                tvTips.setVisibility(View.GONE);
            }
//            cbSign.setChecked(selectedSymbolList.contains(data.symbol));
            cbSign.setChecked(data.checked);
            cbSign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!buttonView.isPressed())return;//这句不能少，否则出错
//                    selectedSymbolList.remove(data.symbol);
//                    if (isChecked) {
//                        selectedSymbolList.add(data.symbol);
//                    }
//                    Log.d("selectedSymbolList","111=="+selectedSymbolList.toString()+"  buttonView.isPressed()=="+buttonView.isPressed());
//                    notifyDataSetChanged();
                    data.checked = isChecked;
                    if (onStartDragListener != null) {
                        onStartDragListener.onCheckedChanged();
                    }
                }
            });

//            tv24H.setText("24H量 " + data.amount);
            ivSort.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            //通知ItemTouchHelper开始拖拽
                            onStartDragListener.onStartDrag(MyHoldHolder.this);
                            break;

                    }
                    return false;
                }
            });


        }


        /**
         * item 被选中时
         */
        @Override
        public void onItemSelected() {
            llItem.setSelected(true);

//            textView.setBackgroundResource(R.drawable.bg_channel_p);
//            ll_whole.setBackgroundResource(R.color.cf2f2f2);
//            ll_whole.setScaleY(1.2f);
//            ll_whole.setScaleX(1.2f);
        }

        /**
         * item 取消选中时
         */
        @Override
        public void onItemFinish() {
            llItem.setSelected(false);

//            textView.setBackgroundResource(R.drawable.bg_channel);
//            ll_whole.setSelected(false);
//            ll_whole.setScaleY(1f);
//            ll_whole.setScaleX(1f);
        }
    }

    private void delToka(String symbol, final int position) {
        CommonUtil.cancelCall(delCall);
//        delCall = RedzHttpServiceManager.getInstance().getRedzService().delToka(symbol);
//        delCall.enqueue(new MyCallBack(context) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    removeItem(position);
//                }
//            }
//        });
    }


    private String getSort() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, m = getGroup().size(); i < m; i++) {
            Market myHoldPositions = getGroup().get(i);
            sb.append(myHoldPositions.symbol + "," + i);
            if (i != m - 1) {
                sb.append(";");
            }
        }
        return sb.toString();
    }

    private void sortToka() {
        String s = getSort();
        Log.d("onItemMove", "sorts==" + s);
        if (sorts.equals(s)) {
            return;
        }
        sorts = s;
        CommonUtil.cancelCall(delCall);
//        delCall = RedzHttpServiceManager.getInstance().getRedzService().tokaSort(sorts);
//        delCall.enqueue(new MyCallBack(context) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    CommonUtil.showmessage(resultData.msg, context);
//                }
//            }
//        });
    }


    public interface OnStartDragListener {
        /**
         * 当View需要拖拽时回调
         *
         * @param viewHolder The holder of view to drag
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);


        /**
         * 监听是否全选
         */
        void onCheckedChanged();
    }
}
