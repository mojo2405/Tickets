package il.co.myapp.tickets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.ImageContext;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import il.co.myapp.tickets.model.Ticket;
import il.co.myapp.tickets.utils.TicketParser;

public class TicketDetailsActivity extends AppCompatActivity{

    static final int REQUEST_TAKE_PHOTO = 1;
    static final String GOOGLE_API_KEY = "AIzaSyB0a6pQ_pWETAOYm3v5ISJ-xrmE3ge766g";
    private static final String TAG = "AddNewTicketActivity";
    private Spinner driversSpinner;
    private Context context;
    private FloatingActionButton addDriverButton, captureReportImage;
    private RadioGroup cancelReasonRadioGroup;
    private String selectedCancelReason;
    private List<String> detectedWordsFromTicket;
    ProgressBar progressBar;
    private Button submitTicketButton;
    private String pathToPhotoFile;
    HashMap<String, EditText> ticketTextEditFields;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_ticket);
        captureReportImage = findViewById(R.id.newTicketCaptureReportId);
        captureReportImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        context = this;

        progressBar = findViewById(R.id.newTicketProgressBar);


        submitTicketButton = (Button) findViewById(R.id.newTicketsubmitTicket);
//        submitTicketButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.w("driverlist", driversList.toString());
//                if (driversList == null || selectedCancelReason == null) {
//                    Toast.makeText(getApplicationContext(),
//                            "אנא בחר נהג וסיבת בקשה", Toast.LENGTH_LONG).show();
//                    return;
//                }
//                new TrafficTicketData().SubmitTicket(GetTicketsDetailsValues(), new AsyncTicketResponse() {
//                    @Override
//                    public void TicketsDataReceived(List<Ticket> tickets) {
//
//                        Log.w(TAG, tickets.toString());
//                        startActivity(new Intent(AddNewTicketActivity.this, DashboardActivity.class));
//                    }
//                });
//            }
//        });


        final List<String> driversArray = new ArrayList<String>();



        getTicketFieldsReferences();

    }

    private HashMap<String, String> GetTicketsDetailsValues() {
        HashMap<String, String> tickets = new HashMap<>();
//        tickets.put("id", driversList.get(0).GetDriverId());
        tickets.put("DriverRequest", selectedCancelReason);
        for (String key : ticketTextEditFields.keySet()) {
            if (ticketTextEditFields.get(key).getText().length()==0)
                tickets.put(key, null);
            else
                tickets.put(key, ticketTextEditFields.get(key).getText().toString());
        }
        return tickets;
    }

    private void getTicketFieldsReferences() {
        ticketTextEditFields = new HashMap<>();
        ConstraintLayout mainCoinstraintLayout = findViewById(R.id.newTicketMainConstraintLayoutId);
        for (int i = 0; i < mainCoinstraintLayout.getChildCount(); i++)
            if (mainCoinstraintLayout.getChildAt(i) instanceof LinearLayout) {

                LinearLayout linearLayoutHolder = (LinearLayout) mainCoinstraintLayout.getChildAt(i);
                for (int y = 0; y < linearLayoutHolder.getChildCount(); y++) {
                    if (linearLayoutHolder.getChildAt(y) instanceof EditText) {
                        EditText ticketField = (EditText) linearLayoutHolder.getChildAt(y);

                        String ticketFieldId = ticketField.getTag().toString();
                        ticketTextEditFields.put(ticketFieldId, ticketField);
                    }
                }
            }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        pathToPhotoFile = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "il.co.myapp.tickets.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                new GoogleVision().execute();
            }
        }
    }


    private byte[] photoToBytArray() throws IOException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap;
        options.inSampleSize = 2;
        if (isEmulator()) {
            InputStream inputStream =
                    getResources().openRawResource(R.raw.ticketphoto);

            byte[] photoData = IOUtils.toByteArray(inputStream);
            inputStream.close();

            bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length, options);
        } else
            bitmap = BitmapFactory.decodeFile(pathToPhotoFile, options);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        return stream.toByteArray();
    }


    private boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }

    private class GoogleVision extends AsyncTask<Void, Void, BatchAnnotateImagesResponse> {
        private static final String TAG = "GoogleVision";

        @Override
        protected BatchAnnotateImagesResponse doInBackground(Void... voids) {
            BatchAnnotateImagesResponse batchResponse;
            Vision.Builder visionBuilder = new Vision.Builder(
                    new NetHttpTransport(),
                    new AndroidJsonFactory(),
                    null);
            visionBuilder.setVisionRequestInitializer(
                    new VisionRequestInitializer(GOOGLE_API_KEY));
            Vision vision = visionBuilder.build();
            try {
                Image inputImage = new Image();
                inputImage.encodeContent(photoToBytArray());
                List<String> languagesHints = new ArrayList<>();
                languagesHints.add("iw");
                Feature desiredFeature = new Feature();
                desiredFeature.setType("TEXT_DETECTION");
                ImageContext imageContext = new ImageContext();
                imageContext.setLanguageHints(languagesHints);
                AnnotateImageRequest request = new AnnotateImageRequest();
                request.setImageContext(imageContext);
                request.setImage(inputImage);
                request.setFeatures(Arrays.asList(desiredFeature));
                BatchAnnotateImagesRequest batchRequest =
                        new BatchAnnotateImagesRequest();

                batchRequest.setRequests(Arrays.asList(request));
                batchResponse = vision.images().annotate(batchRequest).execute();
                return batchResponse;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                return null;
            }


        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(),
                    context.getString(R.string.scanStarted),
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(BatchAnnotateImagesResponse response) {

            detectedWordsFromTicket = new ArrayList<>();
            if (response == null || response.getResponses() == null) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),
                        context.getString(R.string.scanFailed),
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        context.getString(R.string.scanFinished),
                        Toast.LENGTH_LONG).show();
                final List<EntityAnnotation> text = response.getResponses()
                        .get(0).getTextAnnotations();
                for (int i = 0; i < text.size(); i++) {
                    detectedWordsFromTicket.add(text.get(i).getDescription());
                    Log.w(TAG + ":" + i, text.get(i).getDescription());
                }
                ticketTextEditFields = new TicketParser(detectedWordsFromTicket, ticketTextEditFields).GetParsedFields();
                progressBar.setVisibility(View.GONE);
            }
        }
    }
}
