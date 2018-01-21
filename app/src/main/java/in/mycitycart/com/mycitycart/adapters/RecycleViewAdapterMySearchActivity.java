package in.mycitycart.com.mycitycart.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.activties.SearchActivity;
import in.mycitycart.com.mycitycart.informations.Products_in_Search_details;
import in.mycitycart.com.mycitycart.networks.VollyConnection;

import static in.mycitycart.com.mycitycart.informations.GetDomain.KEYS.KEY_WISHLIST_URL;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.KEY_EMAIL;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.KEY_UID;
import static in.mycitycart.com.mycitycart.informations.SheardPrefsKeys.KEYS.PREF_FILE_NAME;

/**
 * Created by shubham on 2/19/2017.
 */
public class RecycleViewAdapterMySearchActivity extends RecyclerView.Adapter<RecycleViewAdapterMySearchActivity.MyViewHolder>{

    private List<Products_in_Search_details> data= Collections.emptyList();
    private LayoutInflater inflater;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ProgressDialog pd;
    private SearchActivity activity;
    private boolean whichLayout=false;
    public RecycleViewAdapterMySearchActivity(SearchActivity activity, List<Products_in_Search_details> data,boolean whichLayout){
        inflater= LayoutInflater.from(activity);
        this.data=data;
        this.activity=activity;
            this.whichLayout=whichLayout;
        vollyConnection= VollyConnection.getsInstance();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
        Log.d("NOTIFIE","constructor called");
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(whichLayout)
            view=inflater.inflate(R.layout.row_search_view_list_layout,parent,false);
        else
            view=inflater.inflate(R.layout.row_search_view,parent,false);
        Log.d("NOTIFIE","onCreateView called");
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        Products_in_Search_details currentdata=data.get(position);
        final String id=currentdata.getProduct_id();
        Log.d("NOTIFIE","onbindViewholder called");
        setdata(currentdata.getProduct_name(),
                currentdata.getProduct_actualprice(),
                currentdata.getProduct_id(),
                currentdata.getProduct_shopid(),
                "In Stock",
                currentdata.getPics().get(0),
                holder);

        holder.remove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                pd = ProgressDialog.show(activity, "Removing item", "wait...");
                pd.setCancelable(true);
                String email=readPrefes(activity,KEY_EMAIL,"");
                String uid=readPrefes(activity,KEY_UID,"");
                if(email.length()>1 && uid.length()>1){
                    //requestRemoveItemCart(uid,id,email,"","delete",position);
                }else {
                    pd.dismiss();
                    Toast.makeText(activity, "you must login first", Toast.LENGTH_LONG).show();
                }
            }
        });
        Log.d("POSITION","postion  on bind"+position);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }




    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_subname,tv_rate,tv_shopname;
        private TextView tv_quantity;
        private ImageView image;
        private ImageButton remove,update;
        public MyViewHolder(View itemView) {
           super(itemView);
            setup(itemView);
           Log.d("NOTIFIE","MyviewHoldder called");
       }
        public void setup(View itemView){
            tv_name=(TextView)itemView.findViewById(R.id.tv_product_in_search_name);
            tv_rate=(TextView)itemView.findViewById(R.id.tv_product_in_search_rate);
            tv_shopname=(TextView)itemView.findViewById(R.id.tv_product_in_search_sub_shop_name);
            tv_subname=(TextView)itemView.findViewById(R.id.tv_product_in_search_subname);
            tv_quantity=(TextView) itemView.findViewById(R.id.et_product_in_search_quentity);
            image=(ImageView)itemView.findViewById(R.id.iv_product_image_in_search_layout);
            remove=(ImageButton)itemView.findViewById(R.id.b_searchview_addtowishlist);
        }
   }
    public void setdata(String name,String rate,String subname,String shopname,String quantity,String imageURL
            ,MyViewHolder holder){
        holder.tv_name.setText(name);
        holder.tv_rate.setText(rate);
        holder.tv_subname.setText(subname);
        holder.tv_shopname.setText(shopname);
        if(quantity.equals("In Stock"))
        holder.tv_quantity.setTextColor(Color.GREEN);
        else
            holder.tv_quantity.setTextColor(Color.RED);
        holder.tv_quantity.setText(quantity);
        Picasso.with(activity)
                .load(imageURL)
                .placeholder(R.drawable.placeholder)
                .into(holder.image);
    }
    public void requestRemoveItemCart(final String uid,final String pid,final String email,final String quentity,final String data,final int pos){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, KEY_WISHLIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            pd.dismiss();
                            JSONObject obj = new JSONObject(response);
                            Log.d("JSON_wishlist"+":"+uid+":"+pid+":"+email+":"+quentity+":", obj.toString());
                            parseJSONResponce(obj,uid,pid,email,quentity,pos);
                        } catch (Exception t) {
                            Log.e("JSON_wishlist", t.toString() +":"+ response );
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(activity,error.toString(),Toast.LENGTH_LONG).show();
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
    private void parseJSONResponce(JSONObject response,String uid,String pid,String email,String quentity,int position) {
        if(response==null || response.length()==0){
            return;
        }
        try{
            Boolean result=response.getBoolean("success");
            if(result){
                data.remove(position);
                Toast.makeText(activity,"one item remove", Toast.LENGTH_SHORT).show();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,data.size());
                Log.d("JSON_WIshList_RESULT",result.toString());
                Toast.makeText(activity,"Producted removed",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(activity,"Can't remove",Toast.LENGTH_LONG).show();
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public static String readPrefes(Context context, String prefesName, String defaultValue){
        SharedPreferences sharedPrefs=context.getSharedPreferences(PREF_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPrefs.getString(prefesName,defaultValue);
    }
}
