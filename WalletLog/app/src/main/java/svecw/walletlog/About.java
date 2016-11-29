package svecw.walletlog;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView facebook = (ImageView) findViewById(R.id.facebook);
        ImageView help = (ImageView) findViewById(R.id.help);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.facebook.com/WalletLog-185851838455160/?fref=ts"));
                startActivity(intent);
            }
        });

        TextView link1 = (TextView) findViewById(R.id.link1);
        if (link1 != null) {
            link1.setMovementMethod(LinkMovementMethod.getInstance());
        }

        TextView link2 = (TextView) findViewById(R.id.link2);
        if (link2 != null) {
            link2.setMovementMethod(LinkMovementMethod.getInstance());
        }

        TextView link3 = (TextView) findViewById(R.id.link3);
        if (link3 != null) {
            link3.setMovementMethod(LinkMovementMethod.getInstance());
        }

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://accounts.google.com/ServiceLogin?sacu=1&scc=1&continue=https%3A%2F%2Fmail.google.com%2Fmail%2F&osid=1&service=mail&ss=1&ltmpl=default&rm=false#identifier"));
                startActivity(intent);
            }
        });



    }
}
