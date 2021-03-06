package edu.utep.cs.cs4330.battleship;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class PvpType extends AppCompatActivity {
boolean internet=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvp_type);
        final Button join = (Button) findViewById(R.id.join);//button to complete the placement of ships
        final Button host = (Button) findViewById(R.id.host);//clear board button
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            internet = extras.getBoolean("isInternet");
        }

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nIntent = new Intent(PvpType.this,PlaceYourShips.class);
                Bundle extras = getIntent().getExtras();
                //boolean internet=false;


                    //Bundle extras = nIntent.getExtras();
                    boolean client = true;
                    nIntent.putExtra("isClient", client);
                nIntent.putExtra("isHost", false);

                nIntent.putExtra("internet",internet);
                    //nIntent.putExtras(nBundle);
                    startActivity(nIntent);
            }
        });
        host.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PvpType.this,PlaceYourShips.class);
                Bundle extras = getIntent().getExtras();
                //boolean  internet=false;

                boolean host = true;
                extras.putBoolean("isHost", host);
                extras.putBoolean("isClient",false);
                extras.putBoolean("internet", internet);

                i.putExtras(extras);
                startActivity(i);
            }
        });

    }
    // the following two methods save and restore on screen orientation
    @Override
    protected void onSaveInstanceState(Bundle outState){

        outState.putBoolean("internet",internet);

        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        internet = savedInstanceState.getBoolean("internet");
    }
}
