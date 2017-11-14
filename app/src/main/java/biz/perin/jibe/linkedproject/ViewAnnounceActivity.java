package biz.perin.jibe.linkedproject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class ViewAnnounceActivity extends AppCompatActivity {
    final static String TAG = "ViewAnnounceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_announce);
        Log.d(TAG, "on Create");
        Bundle bun = getIntent().getBundleExtra("biz.perin.jibe.ANNOUNCE_INDEX");
        int position = bun.getInt("position");
        String values = bun.getString("values");
        HashMap<String, String> dictValues = MyHelper.json2dict(values);

        System.out.println(values);

        ImageView tvDirection = (ImageView) findViewById(R.id.ann_direction);
        TextView tvIdAnnonce = (TextView) findViewById(R.id.idAnn);
        TextView tvCategory = (TextView) findViewById(R.id.category);
        TextView tvPseudo = (TextView) findViewById(R.id.pseudo);
        TextView tvDate = (TextView) findViewById(R.id.date);
        TextView tvCorpAnnonce = (TextView) findViewById(R.id.corpAnnonce);

        tvIdAnnonce.setText(dictValues.get("id"));
        tvCategory.setText(dictValues.get("category"));
        tvCorpAnnonce.setText(dictValues.get("description"));
        tvPseudo.setText(dictValues.get("pseudo"));
        tvDate.setText(dictValues.get("date"));

        if (dictValues.get("direction").equals("DEMAND")) {
            tvDirection.setImageDrawable(new ColorDrawable(Color.GREEN));
        } else {
            tvDirection.setImageDrawable(new ColorDrawable(Color.BLUE));
        }
    }
}
