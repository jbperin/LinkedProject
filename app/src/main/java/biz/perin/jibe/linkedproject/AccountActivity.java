package biz.perin.jibe.linkedproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

import static biz.perin.jibe.linkedproject.MyHelper.json2dict;

public class AccountActivity extends AppCompatActivity {
    private final static String TAG = AccountActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        String jsAccount = LetsClient.getInstance().getModel().getAccount();
        HashMap<String, String> dAccountAttributes = json2dict(jsAccount);

        String lastPublish = dAccountAttributes.get("derniere_publication");
        String nbExchange = dAccountAttributes.get("nbEchange");
        String solde = dAccountAttributes.get("solde");
        String soldeActuel = dAccountAttributes.get("soldeActuel");

        String synthese = "";

        if ((solde != null) && ( ! solde.equals("null"))) {
            synthese += String.format("Votre solde initial est de %s clous\n", solde);
            if ((lastPublish != null) && ( ! lastPublish.equals("null"))) {
                synthese += String.format("publié %s\n", lastPublish);
            }
        }
        if ((nbExchange != null) && ( ! nbExchange.equals("null"))) {
            synthese += String.format("Vous avez procédé à %s échanges\n", nbExchange);
        }
        if ((soldeActuel != null) && ( ! soldeActuel.equals("null"))) {
            synthese += String.format("Votre solde actuel est de %s clous\n", soldeActuel);
        }

        TextView tvSyntheseAccount = (TextView)findViewById(R.id.tvSyntheseAccount);
        tvSyntheseAccount.setText(synthese);

        // TODO populate list of transaction
        Log.d (TAG, "Account = " + dAccountAttributes.toString());

    }
}
