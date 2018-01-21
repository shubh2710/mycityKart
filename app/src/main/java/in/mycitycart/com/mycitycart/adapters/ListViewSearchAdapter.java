package in.mycitycart.com.mycitycart.adapters;

/**
 * Created by shubham on 3/15/2017.
 */


    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.BaseAdapter;
    import android.widget.ImageView;
    import android.widget.SearchView;
    import android.widget.TextView;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Locale;
    import in.mycitycart.com.mycitycart.R;
    import in.mycitycart.com.mycitycart.activties.SearchActivity;
    import in.mycitycart.com.mycitycart.database.RecentSearchDataBase;

public class ListViewSearchAdapter extends BaseAdapter {

        // Declare Variables
        SearchView editsearch;
        Context mContext;
        LayoutInflater inflater;
        private List<String> textList = null;
        private ArrayList<String> arraylist;

        public ListViewSearchAdapter(Context context, List<String> textList, SearchView editsearch) {
            mContext = context;
            this.textList = textList;
            inflater = LayoutInflater.from(mContext);
            this.arraylist = new ArrayList<String>();
            this.arraylist.addAll(textList);
            this.editsearch=editsearch;
        }

        public class ViewHolder {
            TextView name;
            ImageView chngeQuary;
        }
        @Override
        public int getCount() {
            return textList.size();
        }

        @Override
        public String getItem(int position) {
            return textList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.search_view_row_vew, null);
                // Locate the TextViews in listview_item.xml
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.chngeQuary=(ImageView) view.findViewById(R.id.iv_changeQuary);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            // Set the results into TextViews
            holder.name.setText(textList.get(position));
            holder.chngeQuary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editsearch.setQuery(textList.get(position),false);
                }
            });
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecentSearchDataBase mydb=new RecentSearchDataBase(mContext,"recent_search_db");
                    mydb.open();
                    mydb.createEntry(textList.get(position));
                    mydb.close();
                    Intent searchActivity=new Intent(mContext,SearchActivity.class);
                    searchActivity.putExtra("search",textList.get(position));
                    mContext.startActivity(searchActivity);
                }
            });
            return view;
        }

        // Filter Class
        public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            textList.clear();
            if (charText.length() == 0) {
                textList.clear();
            } else {
                for (String wp : arraylist) {
                    if (wp.toLowerCase(Locale.getDefault()).contains(charText)) {
                        textList.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }
    // Filter Class
    public void filter() {
            textList.clear();
        notifyDataSetChanged();
    }

    }
