package com.abdalladelessa.demo;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.abdalladelessa.rxlocmanager.RxLocManager;
import com.abdalladelessa.rxlocmanager.RxLocUtils;

import rx.Subscription;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RxLocManager rxLocManager;
    private Subscription subscribe;
    private FloatingActionButton fab;
    private TextView tvLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rxLocManager = RxLocManager.getBestManager(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        tvLabel = (TextView) findViewById(R.id.tvLabel);
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(subscribe != null) {
            subscribe.unsubscribe();
        }
        subscribe = rxLocManager.getLocationUpdates(MainActivity.this).subscribe(new Action1<Location>() {
            @Override
            public void call(Location location) {
                tvLabel.setText(" Location : " + location.getLatitude() + " : " + location.getLongitude());
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable e) {
                String error = RxLocUtils.getRxLocErrorMessage(e);
                tvLabel.setText("Error :" + error);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        rxLocManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if(subscribe != null) {
            subscribe.unsubscribe();
        }
        super.onDestroy();
    }
}
