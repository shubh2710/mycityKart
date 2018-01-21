package in.mycitycart.com.mycitycart.activties;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import in.mycitycart.com.mycitycart.adapters.RecycleViewAdapterMySearchActivity;
import in.mycitycart.com.mycitycart.informations.MinMax;
import in.mycitycart.com.mycitycart.informations.Products_in_Search_details;
import in.mycitycart.com.mycitycart.informations.filtersortInformation;
import in.mycitycart.com.mycitycart.networks.VollyConnection;
import static in.mycitycart.com.mycitycart.informations.GetDomain.KEYS.*;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private RecyclerView recyclerview;
    public RecycleViewAdapterMySearchActivity adaptor;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private Context context;
    private boolean isGrid=true;
    private boolean isFilter=false;
    private boolean isSort=false;
    private int issort=0;
    private ImageButton layout;
    private Button sort,filter;
    private TextView Searchresult;
    private List<Products_in_Search_details> product_List= Collections.emptyList();
    private ProgressDialog pd;
    private  filtersortInformation filters;
    private String queary="",bar="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        context=this;
        toolbar=(Toolbar)findViewById(R.id.appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerview=(RecyclerView)findViewById(R.id.rc_ProductSerach);
        product_List=new ArrayList<>();
        product_List.clear();
        setrecyclerView(isGrid);
        recyclerview.setFocusable(false);
        recyclerview.addOnItemTouchListener(new RecyclerTouchListner(this, recyclerview, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Bundle details=new Bundle();
                details.putStringArrayList("image",product_List.get(position).getPics());
                details.putString("mrp",product_List.get(position).getProduct_mrp());
                details.putString("rate",product_List.get(position).getProduct_actualprice());
                details.putString("id",product_List.get(position).getProduct_id());
                details.putString("name",product_List.get(position).getProduct_name());
                details.putString("tag",product_List.get(position).getProduct_offer());
                Intent showcase=new Intent(context, ShowCase.class);
                showcase.putExtra("extra",details);
                context.startActivity(showcase);

            }
            @Override
            public void onLongClick(View view, int position) {
            }
        }));
        sort=(Button)findViewById(R.id.b_searchview_sort);
        filter=(Button)findViewById(R.id.b_searchview_filter);
        layout=(ImageButton)findViewById(R.id.b_searchview_layout);
        Searchresult=(TextView)findViewById(R.id.tv_activitySerch_result);
        sort.setOnClickListener(this);
        layout.setOnClickListener(this);
        filter.setOnClickListener(this);
        try{
            bar=getIntent().getStringExtra("bar");
            Log.d("search data",bar);
        }catch (Exception e){
            }
        try{
            queary=getIntent().getStringExtra("search");
            Log.d("search data",queary);

        }catch (Exception e){
        }
        if(bar!=null){
            search(bar,"bar");
            pd = ProgressDialog.show(this, "Loading search", "wait...");
            pd.setCancelable(true);
            setTitle(bar);
            Searchresult.setText("serching for bar code "+bar);
        }else{
            search(queary,"search");
            pd = ProgressDialog.show(this, "Loading search", "wait...");
            pd.setCancelable(true);
            setTitle(queary);
            Searchresult.setText("serching for "+queary);
        }
    }

    private void setrecyclerView(Boolean layout) {


        GridLayoutManager glm;
        if(layout){
            adaptor=new RecycleViewAdapterMySearchActivity(this,product_List,true);
            recyclerview.setAdapter(adaptor);
            recyclerview.setLayoutManager(new LinearLayoutManager(this));
        }
        else{
            adaptor=new RecycleViewAdapterMySearchActivity(this,product_List,false);
            recyclerview.setAdapter(adaptor);
            glm = new GridLayoutManager(this, 2);
            recyclerview.setLayoutManager(glm);
        }
    }

    public void search(final String search,final String type){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KEY_SEARCHEXP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            Log.d("JSON_Search",response.toString());
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj,search);
                        } catch (Exception t) {
                            Searchresult.setText("opps something is not correct");
                            Log.e("JSON_Search", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        Searchresult.setText("slow net connection");
                        pd.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
               // params.put("search","search");
                //params.put("data",search);
                params.put("search",type);
                params.put("data",search);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }
    public void searchWithFilter(final String SearchType, final String searchData, final String discount, final String prate, final String srate,final String pricemin, final String pricemax){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,KEY_SEARCHEXP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        try {
                            Log.d("JSON_Search",response.toString());
                            JSONObject obj = new JSONObject(response);
                            parseJSONResponce(obj,"");
                        } catch (Exception t) {
                            Searchresult.setText("opps something is not correct");
                            Log.e("JSON_Search", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
                        Searchresult.setText("slow net connection");
                        pd.dismiss();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                // params.put("search","search");
                //params.put("data",search);
                params.put("search",SearchType);
                params.put("data",searchData);
                params.put("discount",discount);
                params.put("prate",prate);
                params.put("srate",srate);
                params.put("pricemin",pricemin);
                params.put("pricemax",pricemax);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(6000, 1, 1));
        requestQueue.add(stringRequest);
    }

    private void parseJSONResponce(JSONObject response,String search) {
        if(response==null || response.length()==0){
            return;
        }
        try{
            Boolean result=response.getBoolean("success");
            if(result){
                product_List.clear();
                JSONArray Productarray=response.getJSONArray("list");
                for(int i=0;i<Productarray.length();i++) {
                    Products_in_Search_details prouctDetails=new Products_in_Search_details();
                    JSONObject currentObjecr = Productarray.getJSONObject(i);
                    String product_id = currentObjecr.getString("pid");
                    String product_shopid = currentObjecr.getString("sid");
                    String product_name = currentObjecr.getString("name");
                    String product_mrp = currentObjecr.getString("mrp");
                    String product_actualproce = currentObjecr.getString("actualprice");
                    String product_stockquantity = null;
                    String product_cartquantity = null;
                    String product_rating =null;
                    String product_offer = null;
                    JSONArray product_pic=currentObjecr.getJSONArray("photo");
                    ArrayList<String> pics=new ArrayList<>();
                    Searchresult.setText("Total "+(i+1)+" product found in search matching to "+"'"+search+"'");
                    for(int j=0;j<product_pic.length();j++){
                        pics.add(product_pic.getString(j));
                    }
                    if(product_id!="null"){
                        prouctDetails.add_product(product_name
                                ,product_id
                                ,product_actualproce
                                ,product_mrp
                                ,product_shopid
                                ,product_stockquantity
                                ,product_cartquantity
                                ,product_rating,product_offer
                                ,pics);
                        // add row date like recent to array list
                        product_List.add(prouctDetails);
                        adaptor.notifyDataSetChanged();
                    }
                    Log.d("Search Products", product_id + product_mrp + product_name +"\n");
                }
            }
            else {
                Searchresult.setText("No product found matching to "+"'"+search+"'");
                product_List.clear();
                adaptor.notifyDataSetChanged();
            }
        }catch (JSONException e){
            e.printStackTrace();
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
            case android.R.id.home:
                finish();
                break;
            case R.id.searchBar:
                finish();
                break;
            case R.id.mycart:
                finish();
                Intent cart=new Intent(this,ActivityMyCart.class);
                startActivity(cart);
            case R.id.rightDrawerOpingIcon:
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.b_searchview_filter:
                Intent filter=new Intent(this,ActivityFilters.class);
                startActivityForResult(filter,2);
                break;
            case R.id.b_searchview_sort:
                Intent sort=new Intent(this,SortActivity.class);
                startActivityForResult(sort,3);
                break;
            case R.id.b_searchview_layout:
                if(isGrid){
                    isGrid=false;
                    layout.setImageResource(R.drawable.ic_format_list);
                    setrecyclerView(isGrid);
                }else{
                    isGrid=true;
                    layout.setImageResource(R.drawable.ic_grid_format);
                    setrecyclerView(isGrid);
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) {
            issort=0;
            isFilter=true;
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<MinMax> myList=new ArrayList<>();
                ArrayList<MinMax> mynewList=new ArrayList<>();
                try{
                myList = (ArrayList<MinMax>) data.getSerializableExtra("result");
                    removedata(myList);
                    int i=0;
                    for(MinMax min:myList) {
                                Log.d("filtered_resultSearch",
                                        min.getListNumber()
                                        + " " + min.getMin()
                                        + " " + min.getMax()
                                        + " " + min.getisapply());
                                mynewList.add(new MinMax(min.getMin(), min.getMax(), min.getListNumber(), min.getisapply()));
                    }
                    filters=new filtersortInformation(
                            mynewList.get(0)
                            , mynewList.get(1)
                            , mynewList.get(2)
                            , mynewList.get(3)
                            ,queary
                            ,issort);
                    filters.showData();
                    searchWithFilter(filters.getFilterString(),queary,filters.getMinDiscount(),filters.getMinPRate(),filters.getMinSRate(),filters.getMinPrice(),filters.getMaxPrice());
                    Log.d("APPLYFILTERS",
                            filters.getFilterString()
                                    +" "+queary
                                    +" "+filters.getMinDiscount()
                                    +" "+filters.getMinPRate()
                                    +" "+filters.getMinSRate()
                                    +" "+filters.getMaxPrice()
                                    +" "+filters.getMinPrice());
                    pd = ProgressDialog.show(this, "Loading search", "wait...");
                    pd.setCancelable(true);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode==3){
            if(resultCode == Activity.RESULT_OK){
                int ty = data.getIntExtra("type", 0);
                if(isFilter) {
                    filters.setIsSortNumber(ty);
                    filters.showData();
                    searchWithFilter(filters.getFilterString(), queary, filters.getMinDiscount(), filters.getMinPRate(), filters.getMinSRate(), filters.getMaxPrice(), filters.getMinPrice());
                    Log.d("APPLYFILTERSWITH_SORT",
                            filters.getFilterString()
                                    + " " + queary
                                    + " " + filters.getMinDiscount()
                                    + " " + filters.getMinPRate()
                                    + " " + filters.getMinSRate()
                                    + " " + filters.getMaxPrice()
                                    + " " + filters.getMinPrice());
                }
                else
                    searchWithFilter(ty+",0",queary,"0","0","0","0","0");
                pd = ProgressDialog.show(this, "Loading search", "wait...");
                pd.setCancelable(true);
            }
        }
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
    public void removedata(ArrayList<MinMax> list){
    }
}