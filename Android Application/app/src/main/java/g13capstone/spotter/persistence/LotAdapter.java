package g13capstone.spotter.persistence;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import g13capstone.spotter.R;
import g13capstone.spotter.interfaces.LotClickListener;
import g13capstone.spotter.objects.Lot;


public class LotAdapter extends RecyclerView.Adapter<LotAdapter.LotViewHolder>{

    Context context;
    private List<Lot> lotArray;
    private LotClickListener listener;


    public LotAdapter(Context context, List<Lot> lotArray, LotClickListener listener ){

        this.lotArray = lotArray;
        this.listener = listener;
    }

    public static class LotViewHolder extends RecyclerView.ViewHolder{


        static CardView cv;
        static TextView lotName;
        static TextView lotID;
        static TextView lotLocation;
        static TextView lotPrice;
        static TextView lotDistance;
        static TextView lotAvail;
        static TextView lotCapacity;
        static ImageView lotImage;

        LotViewHolder(View v) {
            super(v);
            cv = (CardView) itemView.findViewById(R.id.cv);
            lotName = (TextView) v.findViewById(R.id.lotNameView);
            lotID = (TextView) v.findViewById(R.id.lotID);
            lotLocation = (TextView) v.findViewById(R.id.lotLocation);
            lotPrice = (TextView) v.findViewById(R.id.lotPrice);
            lotDistance = (TextView) v.findViewById(R.id.lotDistance);
            lotAvail = (TextView) v.findViewById(R.id.lotAvail);
            lotImage = (ImageView) v.findViewById(R.id.imageView);
            lotCapacity = (TextView)v.findViewById(R.id.lotCapacity);
        }

    }

    @Override
    public int getItemCount(){
        return lotArray.size();

    }

    @Override
    public LotViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lot_model, parent, false);
        final LotViewHolder viewHolder = new LotViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });

        ImageView image = (ImageView)v.findViewById(R.id.imageView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, viewHolder.getPosition());
            }
        });


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LotViewHolder lotViewHolder, int i) {

        //LotViewHolder.lotImage.setImageResource(R.drawable.img_20401);
        LotViewHolder.lotImage.setImageResource(lotArray.get(i).getImageID());
        LotViewHolder.lotName.setText(lotArray.get(i).getName());
        LotViewHolder.lotID.setText("Lot ID: " + lotArray.get(i).getId());
        LotViewHolder.lotLocation.setText(lotArray.get(i).getLocation());
        LotViewHolder.lotPrice.setText("Price: $" + lotArray.get(i).getPrice()+"/hr");
        LotViewHolder.lotCapacity.setText("Capacity: "+lotArray.get(i).getCapacity());
        LotViewHolder.lotAvail.setText("Available: "+ lotArray.get(i).getAvail());
        if ((int)lotArray.get(i).getDist() < 1000 ) {
            LotViewHolder.lotDistance.setText("Distance: " + (int) lotArray.get(i).getDist() + " m");
        } else {
            LotViewHolder.lotDistance.setText(String.format("Distance: %.1f km", (double) lotArray.get(i).getDist()/1000));
        }

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
