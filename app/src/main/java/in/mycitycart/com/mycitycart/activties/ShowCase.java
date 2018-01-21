package in.mycitycart.com.mycitycart.activties;

import android.accessibilityservice.GestureDescription;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.networks.VollyConnection;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.*;
import static in.mycitycart.com.mycitycart.informations.GetDomain.KEYS.*;

public class ShowCase extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener, View.OnClickListener {

    private SliderLayout mDemoSlider;
    private Toolbar toolbar;
    private ArrayList<String> pics;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private Context context;
    private ProgressDialog pd=null;
    private TextView tv_name,tv_subname,tv_mrp,tv_offer,tv_rate,tv_type,tv_soldbyLine1,tv_soldbyLine2,tv_ItemDetailLine1,tv_ItemDetailLine2,tv_ItemDetailLine3,tv_ItemDetailLine4;
   private Button buy,addToCart;
    String mrp,rate,pid,name,tag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        setContentView(R.layout.activity_show_case);
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        setup();
        pics=new ArrayList<>();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle details=getIntent().getBundleExtra("extra");
        try{
                 mrp=details.getString("mrp");
                 rate=details.getString("rate");
                 pid=details.getString("id");
                 name=details.getString("name");
                 tag=details.getString("tag");
                    setdata(name,pid,mrp,rate," "," "," ",tag,tag);
                    pics=details.getStringArrayList("image");
        }catch (Exception e){
            mrp=null;
            rate=null;
            pid=null;
            name=null;
            tag=null;
            setdata("","","",""," "," "," ","","");
            pics.clear();
        }
        mDemoSlider =(SliderLayout)findViewById(R.id.slidershowCase);
        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Hannibal",R.drawable.pic);
        for(String key : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                   // .description(name)
                    .image(file_maps.get(key))
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",key);
            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.removeAllSliders();
        for(String pic:pics){
        TextSliderView textSliderView = new TextSliderView(this);
        // initialize a SliderLayout
        textSliderView
                // .description(name)
                .image(pic)
                .setScaleType(BaseSliderView.ScaleType.CenterInside)
                .setOnSliderClickListener(this);

        //add your extra information
        textSliderView.bundle(new Bundle());
        textSliderView.getBundle()
                .putString("extra",pic);
        mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.FlipPage);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.stopAutoCycle();
    }
    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }
    @Override
    public void onPageScrollStateChanged(int state) {
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
    public void setup(){
        buy=(Button) findViewById(R.id.b_showCase_buy);
        addToCart=(Button) findViewById(R.id.b_showCase_cart);
        buy.setOnClickListener(this);
        addToCart.setOnClickListener(this);
        tv_name=(TextView)findViewById(R.id.tvShowCase_productname);
        tv_subname=(TextView)findViewById(R.id.tvShowCase_productSubname);
        tv_offer=(TextView)findViewById(R.id.tvShowCase_productPercentOffer);
        tv_rate=(TextView)findViewById(R.id.tvShowCase_productRate);
        tv_type=(TextView)findViewById(R.id.tvShowCase_productType);
        tv_soldbyLine1=(TextView)findViewById(R.id.tvShowCase_productSoldbyLine1);
        tv_soldbyLine2=(TextView)findViewById(R.id.tvShowCase_productSoldbyLine2);
        tv_ItemDetailLine1=(TextView)findViewById(R.id.tvShowCase_productDetailLine1);
        tv_ItemDetailLine2=(TextView)findViewById(R.id.tvShowCase_productDetailLine2);
        tv_ItemDetailLine3=(TextView)findViewById(R.id.tvShowCase_productDetailLine3);
        tv_ItemDetailLine4=(TextView)findViewById(R.id.tvShowCase_productDetailLine4);
    }
    public void setdata(String s_name,String s_subname,String s_offer,String s_rate,String s_type,String s_SoldLine1,String s_SoldLine2,String s_DetailLine1,String s_DetailLine2){
        tv_name.setText(s_name);
        tv_subname.setText(s_subname);
        tv_offer.setText(s_offer);
        tv_rate.setText(s_rate);
        tv_type.setText(s_type);
        tv_soldbyLine1.setText(s_SoldLine1);
        tv_soldbyLine2.setText(s_SoldLine2);
        tv_ItemDetailLine1.setText(s_DetailLine1);
        tv_ItemDetailLine2.setText(s_DetailLine2);
        tv_ItemDetailLine3.setText(s_DetailLine1);
        tv_ItemDetailLine4.setText(s_DetailLine2);
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    @Override
    public void onClick(View v) {
        String email=null;
        String uid=null;
        switch (v.getId()){

            case R.id.b_showCase_buy:
                email=readPrefes(this,KEY_EMAIL,"");
                uid=readPrefes(this,KEY_UID,"");
                if(email.length()>1 && uid.length()>1){
                    pd = ProgressDialog.show(this, "Adding Item into wishlist", "wait...");
                    pd.setCancelable(true);
                    requestAddTowishlist(uid,pid,email,"1","add");
                }else
                    Toast.makeText(this,"you must login first",Toast.LENGTH_LONG).show();
                //requestBuy();
                break;
            case R.id.b_showCase_cart:
                email=readPrefes(this,KEY_EMAIL,"");
                uid=readPrefes(this,KEY_UID,"");
                if(email.length()>1 && uid.length()>1){
                    pd = ProgressDialog.show(this, "Adding Item into cart", "wait...");
                    pd.setCancelable(true);
                    requestAddToCart(uid,pid,email,"1","add");
                }else
                Toast.makeText(this,"you must login first",Toast.LENGTH_LONG).show();
                break;
        }
    }
    public void requestAddToCart(final String uid,final String pid,final String email,final String quentity,final String data){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KEY_CART_MANAGER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            pd.dismiss();
                            JSONObject obj = new JSONObject(response);
                            Log.d("JSON_CART"+":"+uid+":"+pid+":"+email+":"+quentity+":", obj.toString());
                            parseJSONResponce(obj,uid,pid,email,quentity);
                        } catch (Exception t) {
                            Log.e("JSON_CART", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",uid);
                params.put("email",email);
                params.put("pid",pid);
                params.put("data",data);
                params.put("quantity",quentity);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    public void requestAddTowishlist(final String uid,final String pid,final String email,final String quentity,final String data){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KEY_WISHLIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            pd.dismiss();
                            JSONObject obj = new JSONObject(response);
                            Log.d("JSON_CART"+":"+uid+":"+pid+":"+email+":"+quentity+":", obj.toString());
                            parseJSONResponce(obj,uid,pid,email,quentity);
                        } catch (Exception t) {
                            Log.e("JSON_CART", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("id",uid);
                params.put("email",email);
                params.put("pid",pid);
                params.put("data",data);
                params.put("type","product");
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    private void parseJSONResponce(JSONObject response,String uid,String pid,String email,String quentity) {
        if(response==null || response.length()==0){
            return;
        }
        try{
            Boolean result=response.getBoolean("success");
            if(result){
            Log.d("JSON_CART_RESULT",result.toString());
                Toast.makeText(this,"Producted Added To Cart successfully",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Can't Add Product To Cart",Toast.LENGTH_LONG).show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}