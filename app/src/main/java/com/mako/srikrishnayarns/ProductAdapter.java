package com.mako.srikrishnayarns;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
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
 * Created by Mako on 1/28/2017.
 */
public class ProductAdapter extends RecyclerView.Adapter <ProductAdapter.ViewHolder>  implements RecyclerViewFastScroller.BubbleTextGetter {
    private List<String> ProductList;
    private List<String> ProductKey;

    Context context;

    public ProductAdapter(List<String> mDataArray, List<String> mDatakey,  FragmentActivity activity) {
        this.ProductList =mDataArray;
        this.context=activity;
        this.ProductKey =mDatakey;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        holder.tx.setText(ProductList.get(position));
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(String.valueOf(ProductList.get(position).charAt(0)),color);
        holder.img.setImageDrawable(drawable);
        holder.img.setContentDescription(ProductList.get(position));
    }

    @Override
    public int getItemCount() {
        return ProductList.size();
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= ProductList.size())
            return null;

        String name = ProductList.get(pos);
        if (name == null || name.length() < 1)
            return null;

        return ProductList.get(pos).substring(0, 1);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tx;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);
            tx = (TextView) view.findViewById(R.id.contact_name_tx);
            img = (ImageView) view.findViewById(R.id.image_view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FragmentTransaction ft =  ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction().addToBackStack(null);
            ft.setCustomAnimations(R.anim.pull_in_left,R.anim.push_out_right);
            ft.replace(R.id.content_frame, new Displayproduct(ProductKey.get(getLayoutPosition())));
            ft.commit();
        }
    }
}
