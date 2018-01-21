package in.mycitycart.com.mycitycart.activties;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.fragments.Fragment_ChangePassword;
import in.mycitycart.com.mycitycart.fragments.Fragment_MyAddress;
import in.mycitycart.com.mycitycart.fragments.Fragment_MyPhoneNumber;
import in.mycitycart.com.mycitycart.fragments.Fragment_MyProfile;
import in.mycitycart.com.mycitycart.fragments.Fragment_MyProfilePic;
import in.mycitycart.com.mycitycart.fragments.Fragment_MySavedCards;
import in.mycitycart.com.mycitycart.fragments.Fragment_PleaseLogin;
import in.mycitycart.com.mycitycart.fragments.NavigationDrawerForUserDetail;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.*;

public class signInOrLogIn extends AppCompatActivity {

    private Toolbar toolbar;
    private Button next;
    private boolean ifLogin=false;
    private  Fragment fragmen=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_or_log_in);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setTitle("My Account");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerForUserDetail drawerFragment=(NavigationDrawerForUserDetail)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer_forUserDatail);
        drawerFragment.setUp(R.id.fragment_navigation_drawer_forUserDatail,(DrawerLayout)findViewById(R.id.drawerlayoutSignOrLogin),toolbar);
        final Fragment LayoutFragments[]={new Fragment_MyProfile(),new Fragment_ChangePassword(),new Fragment_MyAddress(),
                                    new Fragment_MyProfilePic(),new Fragment_MyPhoneNumber(),new Fragment_MySavedCards()};
        ifLogin=Boolean.valueOf(readPrefes(this,KEY_LOGIN,"false"));
        if(!ifLogin){
        fragmen=new Fragment_PleaseLogin();
        SetFragment(fragmen);
        }
        else{
            fragmen = LayoutFragments[0];
            SetFragment(fragmen);
            }
    }
    private void SetFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        transaction.replace(R.id.UserDetailsesFragrmentSwitcher, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
                case android.R.id.home:
                    finish();
                    break;
                case R.id.mycart:
                    finish();
                    Intent cart=new Intent(this,ActivityMyCart.class);
                    startActivity(cart);
                    break;
                case R.id.searchBar:
                    finish();
                    Intent search=new Intent(this,ActivitySearchBox.class);
                    startActivity(search);
                    break;
            }
        return false;
    }
}