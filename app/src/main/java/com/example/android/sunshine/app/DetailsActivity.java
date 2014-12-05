package com.example.android.sunshine.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class DetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ImageView image = (ImageView) findViewById(R.id.bird_image);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final String LOG_TAG = DetailsActivity.class.getSimpleName();
            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_details, container, false);
            if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
                String birdStr = intent.getStringExtra(Intent.EXTRA_TEXT);
                String position = intent.getStringExtra(Intent.EXTRA_TITLE);
                Log.v(LOG_TAG, birdStr+"...position="+position);
                //((TextView)rootView.findViewById(R.id.detail_text)).setText(birdStr);


              //  ImageView image;
              //  image = (ImageView)findViewById(R.id.bird_image);

             /*   File imgFile = new  File("/sdcard/Android/data/com.example.android.sunshine.app/bird.jpg");

                if(imgFile.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                    ImageView myImage = (ImageView) findViewById(R.id.bird_image);

                    myImage.setImageBitmap(myBitmap);

                }
                Bitmap bmImg = BitmapFactory.decodeFile("path of your img1");
                imageView.setImageBitmap(bmImg);
*/
                File imgFile = new  File("/sdcard/Android/data/com.example.android.sunshine.app/bird"+position+".jpg");
                if(imgFile.exists())
                {
                    Log.v(LOG_TAG, "imgFile exist!");
                    ImageView myImage = (ImageView) rootView.findViewById(R.id.bird_image);
                    myImage.setImageURI(Uri.fromFile(imgFile));

                }else{
                    Log.v(LOG_TAG, "imgFile not downloaded yet!");
                    ImageManager imageManager = new ImageManager();
                    imageManager.DownloadFromUrl(birdStr, "bird"+position+".jpg");
                    if(imgFile.exists())
                    {
                        Log.v(LOG_TAG, "imgFile not exist!");
                        ImageView myImage = (ImageView) rootView.findViewById(R.id.bird_image);
                        myImage.setImageURI(Uri.fromFile(imgFile));

                    }
                }


                 /*   String IMAGE_URL= birdStr;
                Uri builtUri = Uri.parse(IMAGE_URL).buildUpon().build();

                // Create the request to OpenWeatherMap, and open the connection
                /*URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();*/
               /* ((ImageView)rootView.findViewById(R.id.bird_image)).setImageURI(builtUri);


                ImageView image = (ImageView) rootView.findViewById(R.id.bird_image);

                String imageUrl = birdStr;
                try {
                    ImageView i = (ImageView)rootView.findViewById(R.id.bird_image);
                    Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageUrl).getContent());
                    i.setImageBitmap(bitmap);
                    //setContentView(image);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }
            return rootView;
        }
    }

    public static class ImageManager {
        private final String LOG_TAG = ImageManager.class.getSimpleName();
        private final String PATH = "/sdcard/Android/data/com.example.android.sunshine.app/";
       // File root = Environment.getExternalStorageDirectory();

        public void DownloadFromUrl(String imageURL, String fileName) {  //this is the downloader method
            try {
                URL url = new URL(imageURL); //you can write here any link

              //  File file = new File(root, fileName);
              //  file.createNewFile();
                File sunshineDirectory = new File("/sdcard/Android/data/com.example.android.sunshine.app/");
                // have the object build the directory structure, if needed.
                sunshineDirectory.mkdirs();
                File file = new File(sunshineDirectory, fileName);
                long startTime = System.currentTimeMillis();
                Log.d(LOG_TAG, "download begining");
                Log.d(LOG_TAG, "download url:" + url);
                Log.d(LOG_TAG, "downloaded file name:" + fileName);
                        /* Open a connection to that URL. */
                URLConnection ucon = url.openConnection();

                        /*
                         * Define InputStreams to read from the URLConnection.
                         */
                InputStream is = ucon.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                        /*
                         * Read bytes to the Buffer until there is nothing more to read(-1).
                         */
                ByteArrayBuffer baf = new ByteArrayBuffer(50);
                int current = 0;
                while ((current = bis.read()) != -1) {
                    baf.append((byte) current);
                }
                        /* Convert the Bytes read to a String. */
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baf.toByteArray());
                fos.close();
                Log.d(LOG_TAG, "download ready in"
                        + ((System.currentTimeMillis() - startTime) / 1000)
                        + " sec");

            } catch (IOException e) {
                Log.d(LOG_TAG, "Error: " + e);
            }

        }
    }

}
