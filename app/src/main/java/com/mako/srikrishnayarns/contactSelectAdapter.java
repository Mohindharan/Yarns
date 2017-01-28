package com.mako.srikrishnayarns;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.viethoa.RecyclerViewFastScroller;


import java.util.List;

/**
 * Created by Mako on 1/23/2017.
 */

public class contactSelectAdapter extends RecyclerView.Adapter<contactSelectAdapter.ViewHolder>  implements RecyclerViewFastScroller.BubbleTextGetter {
    private List<String> personList;
    private List<String> personKey;
    private String category;
    Context context;

    @Override
    public contactSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(contactSelectAdapter.ViewHolder holder, int position) {
        holder.tx.setText(personList.get(position));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(personList.get(position).charAt(0)),color);
        holder.img.setImageDrawable(drawable);
        holder.img.setContentDescription(personList.get(position));
    }

    public contactSelectAdapter(List<String> mDataArray, List<String> mDatakey, String category, Context context){
        this.personList=mDataArray;
        this.context=context;
        this.personKey=mDatakey;
        this.category=category;
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= personList.size())
            return null;

        String name = personList.get(pos);
        if (name == null || name.length() < 1)
            return null;

        return personList.get(pos).substring(0, 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tx;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);
            tx=(TextView)view.findViewById(R.id.contact_name_tx);
            img=(ImageView)view.findViewById(R.id.image_view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("key", personKey.get(getLayoutPosition()));
            returnIntent.putExtra("person",personList.get(getLayoutPosition()));
            ((Activity)context).setResult(Activity.RESULT_OK, returnIntent);
            ((Activity)context).finish();

        }
    }
}
