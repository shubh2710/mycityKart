package in.mycitycart.com.mycitycart.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.activties.SearchActivity;
import in.mycitycart.com.mycitycart.activties.ShopsListActivity;
import in.mycitycart.com.mycitycart.activties.SimpleScannerActivity;
import in.mycitycart.com.mycitycart.activties.testActivity;
import in.mycitycart.com.mycitycart.adapters.RecycleViewAdapter;
import in.mycitycart.com.mycitycart.informations.information;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment {


    private RecyclerView recyclerview;
    public RecycleViewAdapter adaptor;
    public static final String PREF_FILE_NAME="testprefs";
    public static final String KEY_USER_LEAREN="ueser_learned_drawer";
    private View containerView;
    private boolean mUserLearnedDrawer;
    private ActionBarDrawerToggle mdrawerToggle;
    private boolean mFromSavedInstanetState;
    private DrawerLayout mdrawerlayout;
    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer=Boolean.valueOf(readPrefes(getActivity(),KEY_USER_LEAREN,"false"));
        if(savedInstanceState==null){
            mFromSavedInstanetState=true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout=inflater.inflate(R.layout.fragment_navigation_drawer,container,false);
        recyclerview=(RecyclerView) layout.findViewById(R.id.rcfragmentNavigationList);
        adaptor=new RecycleViewAdapter(getActivity(),getdate(),getResources());
        recyclerview.setAdapter(adaptor);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addOnItemTouchListener(new RecyclerTouchListner(getActivity(), recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                mdrawerlayout.closeDrawer(Gravity.LEFT);
                switch (position){
                    case 0:
                        Intent search1=new Intent(getActivity(),SearchActivity.class);
                        search1.putExtra("search","daily needs");
                        startActivity(search1);
                        break;
                    case 1:
                        Intent daily=new Intent(getActivity(),SearchActivity.class);
                        daily.putExtra("search","daily needs");
                        startActivity(daily);
                        break;
                    case 2:
                        Intent search2=new Intent(getActivity(),SearchActivity.class);
                        search2.putExtra("search","electronics");
                        startActivity(search2);
                        break;
                    case 3:
                        Intent search3=new Intent(getActivity(),SearchActivity.class);
                        search3.putExtra("search","books");
                        startActivity(search3);
                        break;
                    case 4:
                        Intent search4=new Intent(getActivity(),SearchActivity.class);
                        search4.putExtra("search","sweets");
                        startActivity(search4);
                        break;
                    case 5:
                        Intent shoplist=new Intent(getActivity(), ShopsListActivity.class);
                        getActivity().startActivity(shoplist);
                        break;
                    case 6:
                        IntentIntegrator integrator = new IntentIntegrator(getActivity());
                        integrator.setPrompt("PLEASE MAKE SURE LINE COVER FULL BAR CODE");
                        integrator.setOrientationLocked(false);
                        integrator.initiateScan();

                        break;
                    case 7:
                        Intent more=new Intent(getActivity(), testActivity.class);
                        getActivity().startActivity(more);
                        break;
                }

                Toast.makeText(getContext(),"on click"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(),"on longclick"+position,Toast.LENGTH_SHORT).show();
            }
        }));
        return layout;

    }
    public static List<information> getdate(){
        List<information> data=new ArrayList<>();
        int[] icons={R.drawable.ic_001_shopping_basket,
                R.drawable.ic_027_groceries,
                R.drawable.ic_051_atm_mobile,
                R.drawable.ic_017_wishlist,
                R.drawable.ic_051_sweet_shop,
                R.drawable.ic_040_shop,
                R.drawable.ic_009_barcode,
                R.drawable.ic_050_faq_help};
        String[] title ={"FrontPageTabShopping","Daily products","Electronic product","Books","Sweets","Shops","Scan Bar Code","More"};
        for(int i=0;i<title.length;i++){
            information current =new information();
            current.iconId=icons[i];
            current.title=title[i];
            data.add(current);
        }
        return data;
    }
    public void setUp(int fragmentId, DrawerLayout dl, Toolbar toolbar) {

        containerView=getActivity().findViewById(fragmentId);
        mdrawerlayout=dl;
        mdrawerToggle=new ActionBarDrawerToggle(getActivity(),dl,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    saveToPref(getActivity(),KEY_USER_LEAREN,mUserLearnedDrawer+"");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            /*@Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if(slideOffset<0.6)
                    tb.setAlpha(1-slideOffset);

            }*/
        };
        if(mUserLearnedDrawer && !mFromSavedInstanetState){
            mdrawerlayout.openDrawer(containerView);
        }
        mdrawerlayout.setDrawerListener(mdrawerToggle);
        mdrawerlayout.post(new Runnable() {
            @Override
            public void run() {
            mdrawerToggle.syncState();
            }
        });
    }
    public static void saveToPref(Context context, String preferenceName, String preferenceValue ){
        SharedPreferences sheredPreference=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sheredPreference.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();
    }
    public static String readPrefes(Context context, String prefesName,String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
   public  class RecyclerTouchListner implements RecyclerView.OnItemTouchListener{

       private GestureDetector gestureDetector;
       private ClickListener clicklistner;
        public RecyclerTouchListner(Context context, final RecyclerView recyclerview, final ClickListener clicklistner){
            this.clicklistner=clicklistner;
            gestureDetector =new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                   View childView= recyclerview.findChildViewUnder(e.getX(),e.getY());
                    if(childView!=null && clicklistner!=null){
                        clicklistner.onLongClick(childView,recyclerview.getChildPosition(childView));
                    }
                    super.onLongPress(e);
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView= rv.findChildViewUnder(e.getX(),e.getY());
            if(childView!=null && clicklistner!=null && gestureDetector.onTouchEvent(e)){
                clicklistner.onClick(childView,rv.getChildPosition(childView));

            }
            return false;
        }
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }

    }
    public static interface ClickListener{
        public void onClick(View view, int position);
        public void onLongClick(View view, int postion);
    }
}
