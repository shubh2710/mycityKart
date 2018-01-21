package in.mycitycart.com.mycitycart.fragments;

import android.os.Bundle;
    import android.support.v4.app.Fragment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.TextView;

    import in.mycitycart.com.mycitycart.R;
public class Fragment_MyProfile extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup viewGroup, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_user_detail_my_profile, viewGroup, false);
            //TextView output= (TextView)view.findViewById(R.id.msg1);
            //output.setText("Fragment One");
            return view;
        }
    }