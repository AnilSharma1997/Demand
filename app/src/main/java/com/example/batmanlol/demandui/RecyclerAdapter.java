package com.example.batmanlol.demandui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private ArrayList<Mydemand_model> objectList;
    private LayoutInflater inflater;
    Context ctx;

    public RecyclerAdapter(Context context, ArrayList<Mydemand_model> objectList) {

        this.ctx = context;
        inflater = LayoutInflater.from(context);
        this.objectList = objectList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view, ctx, objectList);
        return holder;

    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Mydemand_model current = objectList.get(position);
        holder.setData(current, position);

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView title, date, upvote;
        private int position;
        private Mydemand_model currentObject;
        ArrayList<Mydemand_model> contacts = new ArrayList<Mydemand_model>();
        Context ctx;
        public MyViewHolder(View itemView, Context ctx, ArrayList<Mydemand_model> contacts) {
            super(itemView);
            this.contacts = contacts;
            this.ctx = ctx;
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.demand_title);
            date = (TextView) itemView.findViewById(R.id.demand_date);
            upvote = (TextView) itemView.findViewById(R.id.likes);

        }

        public void setData(Mydemand_model currentObject, int position) {

            this.title.setText(currentObject.getDemandTitle());
            this.date.setText(currentObject.getDemandDate());
            this.upvote.setText(currentObject.getUpvotes());
            this.currentObject = currentObject;

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Mydemand_model contact = this.contacts.get(position);
            Intent intent = new Intent(ctx, DemandDetails.class);

            intent.putExtra("demandID", contact.getDemandId());
            this.ctx.startActivity(intent);
        }
    }


}
