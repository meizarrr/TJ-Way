package com.example.meizar.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import net.glxn.qrgen.android.QRCode;

public class QRCodeActivity extends AppCompatActivity {

    String halteAsal;
    String halteTujuan;
    String busPilihan;
    String orderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        halteAsal = getIntent().getStringExtra("halteAsal");
        halteTujuan = getIntent().getStringExtra("halteTujuan");
        busPilihan = getIntent().getStringExtra("busPilihan");
        orderKey = getIntent().getStringExtra("orderKey");

        TextView halteAsalText = (TextView) findViewById(R.id.halteAsalText2);
        TextView halteTujuanText = (TextView) findViewById(R.id.halteTujuanText2);
        TextView platText = (TextView) findViewById(R.id.platText2);
        //TextView orderKeyText = (TextView) findViewById(R.id.kapasitasText2);

        halteAsalText.setText(halteAsal);
        halteTujuanText.setText(halteTujuan);
        platText.setText(busPilihan);
        //orderKeyText.setText(orderKey);

        Bitmap qrCode = QRCode.from(orderKey).withSize(300, 300).bitmap();
        ImageView myImage = (ImageView) findViewById(R.id.QRCodeImage);
        myImage.setImageBitmap(qrCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(QRCodeActivity.this, ProfileActivity.class));
        }
        return true;
    }
}
