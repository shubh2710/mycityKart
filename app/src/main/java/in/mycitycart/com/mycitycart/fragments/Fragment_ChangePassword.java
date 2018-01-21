package in.mycitycart.com.mycitycart.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.mycitycart.com.mycitycart.R;

public class Fragment_ChangePassword extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_change_password, viewGroup, false);
            //TextView output= (TextView)view.findViewById(R.id.msg1);
            //output.setText("Fragment One");
            return view;
        }
    }