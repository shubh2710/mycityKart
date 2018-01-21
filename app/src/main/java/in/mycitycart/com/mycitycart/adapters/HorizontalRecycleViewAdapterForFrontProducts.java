package in.mycitycart.com.mycitycart.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.mycitycart.com.mycitycart.R;
import in.mycitycart.com.mycitycart.activties.ShowCase;
import in.mycitycart.com.mycitycart.informations.Front_page_catagory_details;
import in.mycitycart.com.mycitycart.networks.VollyConnection;

/**
 * Created by shubham on 2/19/2017.
 */

public class HorizontalRecycleViewAdapterForFrontProducts extends RecyclerView.Adapter<HorizontalRecycleViewAdapterForFrontProducts.MyViewHolder>{

    private Context context;
    private VollyConnection vollyConnection;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private List<Front_page_catagory_details> RowProducts=new ArrayList<>();
    private LayoutInflater inflater;

    public HorizontalRecycleViewAdapterForFrontProducts(Context context){
        inflater= LayoutInflater.from(context);
        this.context=context;

        vollyConnection= VollyConnection.getsInstance();
        imageLoader=vollyConnection.getImageLoader();
        requestQueue=VollyConnection.getsInstance().getRequestQueue();
    }
    public void setProductsrow(List<Front_page_catagory_details> banners){

        RowProducts=banners;
        Log.d("DaTaInAdptor",banners.toString());
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.custom_page_view_slider_layout,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        if(RowProducts.get(position).getPic_product().get(0)!=null)
        Picasso.with(context)
                .load(RowProducts.get(position).getPic_product().get(0))
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);
        holder.name.setText(RowProducts.get(position).getName_product());
        holder.mrp.setText(RowProducts.get(position).getMrp_product()+" Rs");
        int price ,mrpRate,discount=0;
        price=Integer.parseInt(RowProducts.get(position).getRate_product());
        mrpRate=Integer.parseInt(RowProducts.get(position).getMrp_product());
        discount=((mrpRate-price)*100)/mrpRate;
        Log.d("DICOUNT",discount+"");

        holder.discount.setText(discount+"");
        holder.rate.setText(RowProducts.get(position).getRate_product()+" Rs");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Front_page_catagory_details current=singleRowData.get(pos);
                Bundle details=new Bundle();
                details.putStringArrayList("image",RowProducts.get(position).getPic_product());
                details.putString("mrp",RowProducts.get(position).getMrp_product());
                details.putString("rate",RowProducts.get(position).getRate_product());
                details.putString("id",RowProducts.get(position).getId_product());
                details.putString("name",RowProducts.get(position).getName_product());
                details.putString("tag",RowProducts.get(position).getTag_product());
                Intent showcase=new Intent(context, ShowCase.class);
                showcase.putExtra("extra",details);
                context.startActivity(showcase);
                Toast.makeText(context,position+"",Toast.LENGTH_SHORT).show();
            }
        });
        Log.d("POSITION","horizontal postion  on bind"+position);
    }

    @Override
    public int getItemCount() {
        return RowProducts.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
       TextView name;
       TextView rate;
       TextView offer;
        TextView discount;
        TextView mrp;
       ImageView imageView;

       public MyViewHolder(View itemView) {
           super(itemView);
           discount=(TextView)itemView.findViewById(R.id.tvslider_product_Discount);
           mrp=(TextView)itemView.findViewById(R.id.tvslider_product_mrp);
           mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
           name=(TextView)itemView.findViewById(R.id.tvslider_product_name);
           rate=(TextView)itemView.findViewById(R.id.tvslider_product_rate);
           offer=(TextView)itemView.findViewById(R.id.tvslider_product_offer);
           imageView=(ImageView)itemView.findViewById(R.id.ivslider_product_image);

   }
}
}