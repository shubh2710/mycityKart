package in.mycitycart.com.mycitycart.adapters;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.fragments.Fragment_LogInForm;
import in.mycitycart.com.mycitycart.fragments.Fragment_RegistrationForm;


/**
 * Created by shubham on 2/20/2017.
 */

public class MypagerAdapterSignOrLogin extends FragmentPagerAdapter {
    String[] tabs;
    private Resources resources;
    FragmentManager manager;
    int[] icons={R.drawable.ic_menu_send,R.drawable.ic_menu_share};
    public MypagerAdapterSignOrLogin(FragmentManager fm, Resources resources){
        super(fm);
        this.manager=fm;
        this.resources=resources;
        tabs=resources.getStringArray(R.array.sign);
    }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position){
                case 0:
                    fragment=Fragment_LogInForm.newInstance("","");
                    break;
                case 1:
                    fragment=Fragment_RegistrationForm.newInstance("","");
                    break;
            }
            return fragment;
        }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
    public Drawable geticon(int position){
        return resources.getDrawable(icons[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}
