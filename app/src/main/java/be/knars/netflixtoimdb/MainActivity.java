package be.knars.netflixtoimdb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScrollView scrollView = findViewById(R.id.scrollView);
        TextView textView = findViewById(R.id.textView);

        try
        {
            String message = getIntent().getStringExtra(Intent.EXTRA_TEXT);

            if( message == null )
            {
                findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                return;
            }
            
            message = message.replace("„", "\"").replace("“", "\"")

            Matcher matcher = Pattern.compile("^[^\"]+\"([^\"]+)\"[^\"]+$").matcher(message);

            if( ! matcher.find() )
            {
                throw new Exception("Here we go. Netflix changed something without consulting me. <<<"+message+">>>");
            }

            message = matcher.group(1);

            Intent outIntent = new Intent();
            outIntent.setAction(Intent.ACTION_SEARCH);
            outIntent.setPackage("com.imdb.mobile");
            outIntent.putExtra(SearchManager.QUERY,message);

            if( outIntent.resolveActivity(getPackageManager()) != null )
            {
                startActivity(outIntent);
                finish();
                return;
            }

            outIntent = new Intent();
            outIntent.setAction(Intent.ACTION_VIEW);
            outIntent.setData(Uri.parse("https://m.imdb.com/find?q="+ URLEncoder.encode( message, "UTF-8" )));

            if( outIntent.resolveActivity(getPackageManager()) != null )
            {
                startActivity(outIntent);
                finish();
                return;
            }

            throw new Exception("No internet browser found. This must be a Nokia 3310. Nice try.");
        }
        catch( Exception e )
        {
            scrollView.setVisibility(View.VISIBLE);
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            textView.setText(sw.getBuffer().toString());
        }
    }
}
