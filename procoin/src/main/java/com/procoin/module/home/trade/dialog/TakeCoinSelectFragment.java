package com.procoin.module.home.trade.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.module.home.trade.entity.TakeCoin;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-7-20.
 */

public class TakeCoinSelectFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;


    private Group<TakeCoin> coinGroup;

    private TakeCoinSelectAdapter selectCoinAdapter;
    private OnSelectCoinListen onSelectCoinListen;

    public void setOnSelectCoinListen(OnSelectCoinListen onSelectCoinListen) {
        this.onSelectCoinListen = onSelectCoinListen;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static TakeCoinSelectFragment newInstance(Group<TakeCoin> coinGroup) {
        TakeCoinSelectFragment fragment = new TakeCoinSelectFragment();
        fragment.coinGroup=coinGroup;
//        Bundle bundle = new Bundle();
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_take_coin, container, false);
        ButterKnife.bind(this, v);
        Bundle bundle = getArguments();
        selectCoinAdapter = new TakeCoinSelectAdapter();
        rvList.setAdapter(selectCoinAdapter);
        selectCoinAdapter.setItems(coinGroup);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(new SimpleRecycleDivider(getActivity()));
        tvCancel.setOnClickListener(this);
        return v;
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                dismiss();
                break;

        }
    }

    public class TakeCoinSelectAdapter extends RecyclerView.Adapter {
        private Group<TakeCoin> items;

        public void setItems(Group<TakeCoin> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder holder1 = (ViewHolder) holder;
            final TakeCoin takeCoin = items.get(position);
            holder1.text1.setText(takeCoin.symbol);
            holder1.text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (onSelectCoinListen != null) {
                        onSelectCoinListen.onclick(takeCoin);
                    }
                }
            });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView text1;

            public ViewHolder(View itemView) {
                super(itemView);
                text1 = itemView.findViewById(android.R.id.text1);
                text1.setGravity(Gravity.CENTER);
            }

        }
    }

    public interface OnSelectCoinListen {
        void onclick(TakeCoin takeCoin);
    }
}
