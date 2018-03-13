package g13capstone.spotter.persistence;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class HintAdapter extends ArrayAdapter<String> {

    public HintAdapter(Context context, List<String> object, int layout){
        super(context, layout, object);
    }

    @Override
    public int getCount(){
        // use the last item as hint and unselectable
        int count = super.getCount();
        return count > 0 ? count - 1 : count;
    }
}
