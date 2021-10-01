package com.procoin.module.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.procoin.module.circle.entity.CircleRoleEnum;
import com.procoin.R;
import com.procoin.widgets.SimpleRecycleDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-7-20.
 */

public class ManageCircleFragment extends BottomSheetDialogFragment implements View.OnClickListener {


    @BindView(R.id.rv_list)
    RecyclerView rvList;
//    @BindView(R.id.tv_cancel)
//    TextView tvCancel;

    private ManagerCircleAdapter managerCircleAdapter;
    private int myRole;
    private int targetRole;

    private OnManagerCircleMember onManagerCircleMember;

    public void setOnManagerCircleMember(OnManagerCircleMember onManagerCircleMember) {
        this.onManagerCircleMember = onManagerCircleMember;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static ManageCircleFragment newInstance(int myRole, int targetRole) {
        ManageCircleFragment fragment = new ManageCircleFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("myRole", myRole);
        bundle.putInt("targetRole", targetRole);
        fragment.setArguments(bundle);
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
        View v = inflater.inflate(R.layout.fragment_manager_circle, container, false);
        ButterKnife.bind(this, v);
        Bundle bundle = getArguments();
        myRole = bundle.getInt("myRole");
        targetRole = bundle.getInt("targetRole");
        managerCircleAdapter = new ManagerCircleAdapter();
        rvList.setAdapter(managerCircleAdapter);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(new SimpleRecycleDivider(getActivity()));
        managerCircleAdapter.setItems(getItems());
//        tvCancel.setOnClickListener(this);
        return v;
    }

    private List<ManagerCircleItemType> getItems() {
        List<ManagerCircleItemType> managerCircleItemTypes = new ArrayList<>();
        Log.d("getItems", "myRole==" + myRole + "  targetRole==" + targetRole);
        managerCircleItemTypes.add(new ManagerCircleItemType("查看资料", 5));
        managerCircleItemTypes.add(new ManagerCircleItemType("私聊", 6));
        if (CircleRoleEnum.isRoot(myRole)) {//我是圈主
            if (CircleRoleEnum.isAdmin(targetRole)) {//对方是管理员
                managerCircleItemTypes.add(new ManagerCircleItemType("取消管理员", 3));
            } else {
                managerCircleItemTypes.add(new ManagerCircleItemType("设为管理员", 4));
            }
            managerCircleItemTypes.add(new ManagerCircleItemType("移除出圈子", 1));
            managerCircleItemTypes.add(new ManagerCircleItemType("移除出圈子并拉黑", 2));

        } else if (CircleRoleEnum.isAdmin(myRole)) {//我是管理员
            if (CircleRoleEnum.isMember(targetRole)) {//对方是普通成员
                managerCircleItemTypes.add(new ManagerCircleItemType("移除出圈子", 1));
                managerCircleItemTypes.add(new ManagerCircleItemType("移除出圈子并拉黑", 2));
            }
        }


        return managerCircleItemTypes;


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_cancel:
//                dismiss();
//                break;

        }
    }

    public class ManagerCircleAdapter extends RecyclerView.Adapter {
        private List<ManagerCircleItemType> items;

        public void setItems(List<ManagerCircleItemType> items) {
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
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder holder1 = (ViewHolder) holder;
            holder1.text1.setText(items.get(position).text);
            holder1.text1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (onManagerCircleMember != null) {
                        onManagerCircleMember.onclick(items.get(position).type);
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

    public interface OnManagerCircleMember {
        /**
         * @param type "移除出圈子", 1
         *             "移除出圈子并拉黑", 2
         *             "取消管理员", 3
         *             "设为管理员", 4
         *             "查看资料", 5
         *             "私聊", 6
         */
        void onclick(int type);
    }
}
