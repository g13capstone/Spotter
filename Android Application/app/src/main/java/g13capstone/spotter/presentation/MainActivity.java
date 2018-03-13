package g13capstone.spotter.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import g13capstone.spotter.R;
import g13capstone.spotter.interfaces.LotListListener;
import g13capstone.spotter.objects.Lot;
import g13capstone.spotter.persistence.DBLotListAccess;

public class MainActivity extends AppCompatActivity implements LotListListener {

    private List<Lot> lotArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBLotListAccess task = new DBLotListAccess(this);
        task.setOnListener(this);
        task.execute(getResources().getString(R.string.lot_link));
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_refresh_main:
                RecyclerView rv = (RecyclerView)this.findViewById(R.id.lotList);
                rv.setVisibility(View.INVISIBLE);
                DBLotListAccess task = new DBLotListAccess(this);
                task.setOnListener(this);
                task.execute(getResources().getString(R.string.lot_link));
                ProgressBar progressBar = (ProgressBar) this.findViewById(R.id.lotProgressBar);
                progressBar.setVisibility(View.VISIBLE);
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            case R.id.action_locate_map:
                Intent mapIntent = new Intent(this, MapsActivity.class);
                Bundle mapbundle = new Bundle();
                mapbundle.putParcelableArrayList("data", (ArrayList<Lot>) lotArrayList);
                mapIntent.putExtras(mapbundle);
                this.startActivity(mapIntent);
                return true;
            case R.id.action_comment_main:
                Intent newIntent = new Intent(this, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("activity", "Main");
                newIntent.putExtras(bundle);
                this.startActivity(newIntent);
                return true;
    }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLotListResults(List<Lot> lotArrayList) {
        this.lotArrayList = lotArrayList;
    }
}
