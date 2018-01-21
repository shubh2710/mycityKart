package in.mycitycart.com.mycitycart.adapters;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.mycitycart.com.mycitycart.R;

/**
 * Created by shubham on 2/25/2017.
 */

public class CustomAdapterForListViewRightDrawer extends BaseAdapter {
    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomAdapterForListViewRightDrawer(Context mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.custom_ist_view, null);
        holder.tv=(TextView) rowView.findViewById(R.id.tvListViewName);
        holder.img=(ImageView) rowView.findViewById(R.id.ivListViewIcon);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);
        /*rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();

            }
        });*/
        return rowView;
    }
}
