package be.knars.netflixtoimdb;

import androidx.appcompat.app.AppCompatActivity;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

        TextView textView = findViewById(R.id.textView);

        try
        {
            Intent inIntent = getIntent();
            String message = inIntent.getStringExtra(Intent.EXTRA_TEXT);

            if( message == null )
            {
                textView.setText("Hint: tap the share icon in the Netflix app.");
                return;
            }

            Pattern pattern = Pattern.compile("^[^\"]+\"([^\"]+)\"[^\"]+$");
            Matcher matcher = pattern.matcher(message);
            if( matcher.find() ) { message = matcher.group(1); }

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
            outIntent.setData(Uri.parse("https://m.imdb.com/find?q="+ URLEncoder.encode( message )));

            if( outIntent.resolveActivity(getPackageManager()) != null )
            {
                startActivity(outIntent);
                finish();
                return;
            }

            textView.setText("Hint: install the IMDB app or an internet browser.");





        }
        catch( Exception e )
        {
            textView.setText( getStackTrace(e) );
        }



    }

    public static String getStackTrace(final Throwable throwable)
    {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);

        pw.println("\"Astral, I'm going to need a sharp scalpel and my long handle stainless spoon.\"");
        pw.println("-- Dr. Walter Bishop, Fringe");
        pw.println("");

        throwable.printStackTrace(pw);

        return sw.getBuffer().toString();
    }
}
