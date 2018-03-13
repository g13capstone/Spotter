package g13capstone.spotter.persistence;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import g13capstone.spotter.R;
import g13capstone.spotter.objects.Stall;

public class SpotAdapter extends ArrayAdapter<Stall> {

    private List<Stall> stalls;
    private Activity context;

    public SpotAdapter(Activity context, List<Stall> stalls) {
        super(context, R.layout.list_stall_model, stalls);

        this.context=context;
        this.stalls=stalls;
    } // end of constructor

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        SpotAdapter.ViewHolder viewHolder=null;
        if (r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.list_stall_model,parent, false);
            viewHolder=new SpotAdapter.ViewHolder(r);
            r.setTag(viewHolder);
        }

        else {
            viewHolder= (SpotAdapter.ViewHolder) r.getTag();
        }

        viewHolder.tvw1.setText("Spot ID: " + stalls.get(position).getSpotID());
        viewHolder.tvw2.setText("Status: " + stalls.get(position).getStatus());

        return r;
    } // end of getView

    class ViewHolder{
        TextView tvw1;
        TextView tvw2;
        ImageView ivw;
        ViewHolder(View v){
            tvw1=(TextView)v.findViewById(R.id.spotName);
            tvw2=(TextView)v.findViewById(R.id.spotStatus);
        }
    } // end of iewHolder class
} // end of SpotAdapter

