package biz.perin.jibe.linkedproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);


        Button btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TextView tvName = (TextView)findViewById(R.id.cntctName);
                String name = tvName.getText().toString();
                TextView tvMail = (TextView)findViewById(R.id.cntctMail);
                String mail = tvMail.getText().toString();
                TextView tvSubject = (TextView)findViewById(R.id.cntctSubject);
                String subject = tvSubject.getText().toString();
                TextView tvText = (TextView)findViewById(R.id.cntctText);
                String text = tvText.getText().toString();
                System.out.println (String.format("name = %s\nmail=%s\nsubject=%s\nText = %s\n",
                        name, mail, subject, text
                        ));
                // TODO Check argument before sending
                WebHelper.getInstance().sendContactMessage(name, mail, subject, text);
                finish();
            }
        });

    }
}
