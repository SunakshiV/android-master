package com.procoin.module.home.fragment;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import android.util.Log;

import com.procoin.MainApplication;
import com.procoin.http.model.User;

/**
 * Created by zhengmj on 19-4-17.
 */

public class UserBaseFragment extends Fragment {
//	protected User user;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        MainApplication application = (MainApplication) activity.getApplicationContext();
//		user = application.getUser();
//		if(user==null)user =new User(0l,"");
        // if(user==null)getActivity().finish();
    }
//    protected String getUserId() {
//        return "1";
//    }

    protected String getUserId() {
        if (getActivity() == null) return "";
        MainApplication application = (MainApplication) getActivity().getApplicationContext();
        User user = application.getUser();
        Log.d("getUserId","user=="+(user==null?"null":user.userId));
        if (user != null && user.getUserId() > 0) {
            return String.valueOf(user.getUserId());
        } else {
            return "";
        }
    }

    protected User getUser() {
        if (getActivity() == null) return null;
        MainApplication application = (MainApplication) getActivity().getApplicationContext();
        return application.getUser();
    }

    //TODO Test
    @Override
    public void onResume() {
        super.onResume();

//		com.tjrv.social.util.CommonUtil.LogLa(2, "Fragment user_name is =========== " + this.getClass().getName());
    }
}
