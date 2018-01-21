package in.mycitycart.com.mycitycart.activties;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.adapters.RecycleViewAdapterMyCartProductList;
import in.mycitycart.com.mycitycart.adapters.RecycleViewAdapterMyWishlistProduct;
import in.mycitycart.com.mycitycart.informations.Products_in_cart_details;
import in.mycitycart.com.mycitycart.informations.Products_in_wishList_details;
import in.mycitycart.com.mycitycart.networks.VollyConnection;

import static in.mycitycart.com.mycitycart.informations.GetDomain.KEYS.KEY_CART_MANAGER_URL;
import static in.mycitycart.com.mycitycart.informations.GetDomain.KEYS.KEY_WISHLIST_URL;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.KEY_EMAIL;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.KEY_UID;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;

public class ActivityMyWishList extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerview;
    public RecycleViewAdapterMyWishlistProduct adaptor;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private Context context;
    private TextView subtotal,dilavery,tax,discount,netTota,itemCount;
    private List<Products_in_wishList_details> product_List=Collections.emptyList();
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vollyConnection= VollyConnection.getsInstance();
        context=this;
        pd = ProgressDialog.show(this, "Loading WishList", "wait...");
        pd.setCancelable(true);

        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        setContentView(R.layout.activity_my_wishlist);
        setTitle("WIshList");
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerview=(RecyclerView)findViewById(R.id.rc_Product_List_in_wishlist);
        product_List=new ArrayList<>();
        product_List.clear();
        adaptor=new RecycleViewAdapterMyWishlistProduct(this,product_List);
        recyclerview.setAdapter(adaptor);
        recyclerview.setFocusable(false);

        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.addOnItemTouchListener(new RecyclerTouchListner(this, recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        String email=readPrefes(this,KEY_EMAIL,"");
        String uid=readPrefes(this,KEY_UID,"");
        if(email.length()>1 && uid.length()>1){
            requestShowCart(uid,"",email,"","list");
        }else {
            Toast.makeText(this, "you must login first", Toast.LENGTH_LONG).show();
            pd.dismiss();
        }

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
            case R.id.rightDrawerOpingIcon:
                break;
            case android.R.id.home:
                finish();
                break;
            case R.id.mycart:
                Intent cart=new Intent(this,ActivityMyCart.class);
                startActivity(cart);
                finish();
                break;
            case R.id.searchBar:
                finish();
                Intent search=new Intent(this,ActivitySearchBox.class);
                startActivity(search);
                break;
        }
        return false;
    }
    public  class RecyclerTouchListner implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clicklistner;

        public RecyclerTouchListner(Context context, final RecyclerView recyclerview, final ClickListener clicklistner) {
            this.clicklistner = clicklistner;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerview.findChildViewUnder(e.getX(), e.getY());
                    if (childView != null && clicklistner != null) {
                        clicklistner.onLongClick(childView, recyclerview.getChildPosition(childView));
                    }
                    super.onLongPress(e);
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && clicklistner != null && gestureDetector.onTouchEvent(e)) {
                clicklistner.onClick(childView, rv.getChildPosition(childView));
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
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
    public void requestShowCart(final String uid,final String pid,final String email,final String quentity,final String data){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KEY_WISHLIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
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
                    JSONArray Productarray=response.getJSONArray("product");
                    for(int i=0;i<Productarray.length();i++) {
                        Products_in_wishList_details prouctDetails=new Products_in_wishList_details();
                        JSONObject currentObjecr = Productarray.getJSONObject(i);
                        String product_id = currentObjecr.getString("id");
                        String product_shopid = currentObjecr.getString("sid");
                        String product_name = currentObjecr.getString("name");
                        String product_mrp = currentObjecr.getString("mrp");
                        String product_actualproce = currentObjecr.getString("actualprice");
                        Boolean product_stock = currentObjecr.getBoolean("instock");
                        String product_rating = currentObjecr.getString("rating");
                        String product_offer = currentObjecr.getString("offer");
                        JSONArray product_pic=currentObjecr.getJSONArray("photo");
                        ArrayList<String> pics=new ArrayList<>();
                        for(int j=0;j<product_pic.length();j++){
                            pics.add(product_pic.getString(j));
                        }
                        if(product_id!="null"){
                            prouctDetails.add_product(product_name,product_id,product_actualproce,product_mrp,product_shopid,product_stock,"",product_rating,product_offer,pics);
                            // add row date like recent to array list
                            product_List.add(prouctDetails);
                            adaptor.notifyDataSetChanged();
                        }
                    Log.d("WishList PRODUCTS", product_id + product_mrp + product_name +"\n");
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
