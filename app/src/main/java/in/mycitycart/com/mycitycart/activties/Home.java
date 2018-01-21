package in.mycitycart.com.mycitycart.activties;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.adapters.MypagerAdapter;
import in.mycitycart.com.mycitycart.fragments.NavigationDrawerFragment;
import in.mycitycart.com.mycitycart.fragments.NavigationDrawerFragmentRight;
import in.mycitycart.com.mycitycart.tabs.SlidingTabLayout;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class Home extends AppCompatActivity  implements MaterialTabListener {

    private SlidingTabLayout mTabs;
    private ViewPager mPager;
    private MaterialTabHost tabHost;
    private Toolbar toolbar;
    private MypagerAdapter myadapter;
    NavigationDrawerFragmentRight drawerFragment_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationDrawerFragment drawerFragment=(NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,(DrawerLayout)findViewById(R.id.drawerlayout),toolbar);


        drawerFragment_right=(NavigationDrawerFragmentRight)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawerRight);
        drawerFragment_right.setUp(R.id.fragment_navigation_drawerRight,(DrawerLayout)findViewById(R.id.drawerlayout),toolbar);

        mPager=(ViewPager)findViewById(R.id.pager);
        myadapter= new MypagerAdapter(getSupportFragmentManager(),getResources());
        mPager.setAdapter(myadapter);
        tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < myadapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            //.setIcon(myadapter.geticon(i))
                            .setText(myadapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
    }

   @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case R.id.searchBar:
                Intent search=new Intent(this,ActivitySearchBox.class);
                startActivity(search);
                break;
            case R.id.rightDrawerOpingIcon:
                drawerFragment_right.OpenDrawer();
                break;
            case R.id.mycart:
                Intent cart=new Intent(this,ActivityMyCart.class);
                startActivity(cart);
        }
        return false;
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        mPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {

            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                Intent searchActivity = new Intent(this, SearchActivity.class);
                searchActivity.putExtra("bar", result.getContents());
                startActivity(searchActivity);
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
