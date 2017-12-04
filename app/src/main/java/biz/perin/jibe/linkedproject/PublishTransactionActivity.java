package biz.perin.jibe.linkedproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static biz.perin.jibe.linkedproject.Constants.COUNTERPART;

import static biz.perin.jibe.linkedproject.MyHelper.json2dict;

public class PublishTransactionActivity extends AppCompatActivity {

    private final String TAG = PublishTransactionActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_transaction);

        String counterpart = getIntent().getStringExtra(COUNTERPART);

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.etPseudo);

        if (! counterpart.equals("unknown")) {
            textView.setText(counterpart);
        }

        List<String> jsPers = LetsClient.getInstance().getModel().getAnnuaire();
        final List<String> lPseudo = new ArrayList<>();
        Log.d(TAG, " there are "+jsPers.size()+" pseudo to suggest");
        final String tabPseudo[] = new String[jsPers.size()];
        int ii = 0;
        for (String jspers: jsPers) {
            HashMap<String, String> dPersonAttributes = json2dict(jspers);
            tabPseudo[ii] = dPersonAttributes.get("pseudo");
            Log.d(TAG, "Added " + tabPseudo[ii]);
            lPseudo.add (tabPseudo[ii]);
            ii++;
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,  tabPseudo);
        textView.setAdapter(adapter);
        textView.setThreshold(1);

        NumberPicker npNbClous = (NumberPicker)findViewById(R.id.numberPicker);
        npNbClous.setMinValue(1);
        npNbClous.setValue(60);
        npNbClous.setMaxValue(100);

        Button btEnregistrer = (Button) findViewById(R.id.btnPublishTransaction);
        btEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nbClous = ((NumberPicker) findViewById(R.id.numberPicker)).getValue();
                String pseudo = ((TextView) findViewById(R.id.etPseudo)).getText().toString();
                String nature = ((TextView) findViewById(R.id.etNatureEchange)).getText().toString();
                RadioButton rbOffre = (RadioButton) findViewById(R.id.rbtPublishGive);
                RadioButton rbDemande = (RadioButton) findViewById(R.id.rbtPublishReceive);

                Log.d(TAG, "publish transaction : " + nbClous + " avec " + pseudo + " à propos de " + nature);
                if (lPseudo.contains(pseudo) && (rbDemande.isChecked() || rbOffre.isChecked())) {
                    String sens = "offre";
                    if (rbDemande.isChecked()) {
                        sens = "demande";
                    }

                } else  {


                }
                finish();
            }
        });
    }
}
