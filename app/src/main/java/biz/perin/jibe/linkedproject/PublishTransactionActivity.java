package biz.perin.jibe.linkedproject;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.*;

import static biz.perin.jibe.linkedproject.Constants.COUNTERPART;

import static biz.perin.jibe.linkedproject.MyHelper.json2dict;

public class PublishTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    private final String TAG = PublishTransactionActivity.class.getName();

    private int day, month, year;

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

        TextView tvNbClous = (TextView)findViewById(R.id.etNbClous);
        //tvNbClous.setMinValue(1);
        tvNbClous.setText("60");
        //tvNbClous.setMaxValue(100);



        TextView tvDateDisplay = (TextView) findViewById(R.id.tvDateDisplay);
        Calendar now = Calendar.getInstance();
        day= now.get(Calendar.DATE);
        month = now.get(Calendar.MONTH)+1;
        year = now.get(Calendar.YEAR);
        tvDateDisplay.setText(String.format("%02d/%02d/%04d", day, month, year));
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                this   , this, year, month-1, day );
        tvDateDisplay.setOnClickListener(new
                                                 View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {
                                                         datePickerDialog.show();
                                                     }
                                                 });

        Button btEnregistrer = (Button) findViewById(R.id.btnPublishTransaction);
        btEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nbClous = Integer.parseInt(((TextView) findViewById(R.id.etNbClous)).getText().toString());
                String pseudo = ((TextView) findViewById(R.id.etPseudo)).getText().toString();
                String nature = ((TextView) findViewById(R.id.etNatureEchange)).getText().toString();
                RadioButton rbOffre = (RadioButton) findViewById(R.id.rbtPublishGive);
                RadioButton rbDemande = (RadioButton) findViewById(R.id.rbtPublishReceive);


                if (lPseudo.contains(pseudo) && (rbDemande.isChecked() || rbOffre.isChecked())) {
                    String sens = "offre";
                    if (rbDemande.isChecked()) {
                        sens = "demande";
                        //nbClous = -nbClous;
                    }
                    Log.d(TAG, "publish transaction : " + nbClous + " avec " + pseudo + " en date du " + String.format("%02d/%02d/%04d", day, month, year) + " à propos de " + nature);
                    LetsClient.getInstance().publishTansaction(day, month, year, nbClous, nature, pseudo);
                    finish();
                } else  {


                }

            }
        });
    }

    @Override
    public void onDateSet(DatePicker datePicker, int selectedYear,
                          int selectedMonth, int selectedDay) {
        day = selectedDay;
        month = selectedMonth+1;
        year = selectedYear;
        TextView tvDateDisplay = (TextView) findViewById(R.id.tvDateDisplay);
        tvDateDisplay.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                + selectedYear);
        Log.d(TAG, "onDateSet " + day + " i1 " + month + " i2 " + year);
    }
}
