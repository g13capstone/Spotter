package g13capstone.spotter.interfaces;

import java.util.List;

import g13capstone.spotter.objects.Lot;

public interface LotListListener {
    void onLotListResults(List<Lot> lotArrayList);
}
