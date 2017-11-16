package io.multy.masterkeygeneration;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AlertDialog.Builder(this)
                .setMessage(Arrays.toString(MasterKeyGenerator.generateKey(this)))
                .setCancelable(false)
                .create()
                .show();
    }
}
