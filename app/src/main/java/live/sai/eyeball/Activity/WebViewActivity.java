package live.sai.eyeball.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;

import live.sai.eyeball.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // - get the url or link
        Uri uri = getIntent().getData();

        WebView webView = (WebView)findViewById(R.id.web_view);
        webView.loadUrl("https://english.fdc-inc.com/HtmlTextbook/index?connect_id=11&html_dir=grammar_entry&chapter_id=9&screen_flag=user&order_no=1");
    }
}