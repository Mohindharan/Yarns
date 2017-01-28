package com.mako.srikrishnayarns;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Mako on 1/20/2017.
 */
public class horizontalAdapter extends RecyclerView.Adapter<horizontalAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private Context context;
    List<String>  text;
    boolean b;

    public horizontalAdapter(Context context, List<String> text, boolean b) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.text = text;
        this.b = b;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;
        boolean b;
        public ViewHolder(View v, final boolean b){
            super(v);
            mTextView = (TextView) v.findViewById(R.id.card_text);
            this.b=b;
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {}
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.custom_card,parent,false);
//        v.setMinimumWidth(parent.getMeasuredWidth());
        ViewHolder vh = new ViewHolder(v,b);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(text.get(position));
    }
    @Override
    public int getItemCount() {
        return text.size();
    }
}
