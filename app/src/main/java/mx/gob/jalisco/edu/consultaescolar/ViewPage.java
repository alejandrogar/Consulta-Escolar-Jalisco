package mx.gob.jalisco.edu.consultaescolar;


import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ViewPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_page);

        Bundle bundle = this.getIntent().getExtras();

        String URL = bundle.getString("URL");
        String TITLE = bundle.getString("TITLE");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(TITLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        WebView webview = (WebView) findViewById(R.id.viewPage);
        assert webview != null;
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webview.loadUrl(URL);


        webview.setDownloadListener(new DownloadListener()
        {
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength)
            {
                //for downloading directly through download manager
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "download");
                DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                dm.enqueue(request);
            }
        });

    }

}
