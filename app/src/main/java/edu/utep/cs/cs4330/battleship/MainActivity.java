package edu.utep.cs.cs4330.battleship;
//Thomas Payan and Alfred Cazares
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button pvp = (Button) findViewById(R.id.pvp);//button to complete the placement of ships
        final Button comp = (Button) findViewById(R.id.comp);//clear board button


        pvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(MainActivity.this,PvpType.class);

                startActivity(i);

            }
        });
        comp.setOnClickListener(new View.OnClickListener() {//resets board
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,PlaceYourShips.class);

                startActivity(i);

            }
        });


    }
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
