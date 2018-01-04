package com.example.amrez.newsappproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by amrez on 10/20/2017.
 */

public class NewsAdapter extends ArrayAdapter<NewsObjects> {
    ArrayList<NewsObjects> NewsArrayList;
    Context context;
    LayoutInflater vi;

    //constructor
    public NewsAdapter(Context context, ArrayList<NewsObjects> objects) {
        super(context, 0, objects);
        NewsArrayList = objects;
        this.context = context;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //List checking
        View listItemView = convertView;
        ViewHolder holder;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_item_list, parent, false);

            // find view  by the holder
            holder = new ViewHolder();
            holder.title_of_the_article = (TextView) listItemView.findViewById(R.id.title_of_the_article);
            holder.name_of_the_section = (TextView) listItemView.findViewById(R.id.name_of_the_section);
            holder.author_name = (TextView) listItemView.findViewById(R.id.author_name);
            holder.date = (TextView) listItemView.findViewById(R.id.date);

            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }
        //set data
        holder.title_of_the_article.setText(NewsArrayList.get(position).getArticleTitle());
        holder.name_of_the_section.setText(NewsArrayList.get(position).getSectionName());
        holder.author_name.setText(NewsArrayList.get(position).getAuthorName());
        holder.date.setText(NewsArrayList.get(position).getDate());

        return listItemView;
    }

    // holder for clas definition
    static class ViewHolder {
        public TextView title_of_the_article;
        public TextView name_of_the_section;
        public TextView author_name;
        public TextView date;
    }

}
