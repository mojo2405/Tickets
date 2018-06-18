package il.co.myapp.tickets.activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.data.AsyncTicketResponse;
import il.co.myapp.tickets.data.TrafficTicketData;
import il.co.myapp.tickets.model.Ticket;
import il.co.myapp.tickets.utils.TicketParser;

public class NewTicketActivity extends AppCompatActivity{

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int GALLERY_REPORT_REQUEST = 2;
    static final int GALLERY_ID_REQUEST = 3;
    static final int GALLERY_PROOFS_REQUEST = 4;

    static final String GOOGLE_API_KEY = "AIzaSyB0a6pQ_pWETAOYm3v5ISJ-xrmE3ge766g";
    private static final String TAG = NewTicketActivity.class.getSimpleName();
    private Spinner driversSpinner;
    private Context context;
    private List<String> detectedWordsFromTicket;
    ProgressBar progressBar;
    private Button submitTicketButton, captureReportImage, getImageFromGallery, addIdButton,
    addProofsButton;
    private String pathToPhotoFile;
    HashMap<String, EditText> ticketTextEditFields;
    String idBlob;
    JSONArray proofsArray = new JSONArray();



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
        getImageFromGallery = findViewById(R.id.newTicketGalleryButton);
        getImageFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTicketFromGalley();
            }
        });
        addIdButton = findViewById(R.id.newTicketIdButton);
        addIdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdFromGallery();
            }
        });
        addProofsButton = findViewById(R.id.newTicketProofsButton);
        addProofsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProofsFromGallery();
            }
        });
        context = this;



        progressBar = findViewById(R.id.newTicketProgressBar);


        submitTicketButton = (Button) findViewById(R.id.newTicketsubmitTicket);
        submitTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new TrafficTicketData().SubmitTicket(getApplicationContext(),
                            GetTicketsDetailsValues(), new AsyncTicketResponse() {
                        @Override
                        public void TicketsDataReceived(List<Ticket> tickets) {

                            Log.w(TAG, tickets.toString());
                            startActivity(new Intent(NewTicketActivity.this,
                                    DashboardActivity.class));
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        getTicketFieldsReferences();

    }

    private void getProofsFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(photoPickerIntent, GALLERY_PROOFS_REQUEST);
    }

    private void getIdFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_ID_REQUEST);
    }

    private void getTicketFromGalley() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, GALLERY_REPORT_REQUEST);
    }

    private HashMap<String, String> GetTicketsDetailsValues() {
        HashMap<String, String> tickets = new HashMap<>();
        tickets.put("email",AppController.getInstance().getUser().getEmail());
        tickets.put("idBlobBase64",idBlob);
        tickets.put("proofsArrayBase64",proofsArray.toString());
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
        if (resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 2;

            switch (requestCode) {
                case REQUEST_TAKE_PHOTO:
                    new GoogleVision().execute();
                    break;
                case GALLERY_REPORT_REQUEST:
                        pathToPhotoFile = getRealPathFromURI(getContentResolver(),data.getData(),null);
                        new GoogleVision().execute();
                        Log.v(TAG,"Got path " + pathToPhotoFile);
                    break;
                case GALLERY_ID_REQUEST:
                    try {
                        Bitmap bitmap = getSelectedImage(data.getData(), options);
                        idBlob =  Base64.encodeToString(bitmapToBlob(bitmap), Base64.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case GALLERY_PROOFS_REQUEST:
                    if(data.getClipData() != null) {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for(int i = 0; i < count; i++) {
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            try {
                                Bitmap bitmap = getSelectedImage(imageUri, options);
                                String blob =  Base64.encodeToString(bitmapToBlob(bitmap), Base64.DEFAULT);
                                proofsArray.put(blob);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //do something with the image (save it to some directory or whatever you need to do with it here)
                        }

                    } else if(data.getData() != null) {
//                        String imagePath = data.getData().getPath();
                        try {
                            Bitmap bitmap = getSelectedImage(data.getData(), options);
                            String blob =  Base64.encodeToString(bitmapToBlob(bitmap), Base64.DEFAULT);
                            proofsArray.put(blob);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //do something with the image (save it to some directory or whatever you need to do with it here)
                    }
            }
        }
    }

    private byte[] bitmapToBlob(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bos);
        byte[] bArray = bos.toByteArray();
        return bArray;
    }

    public String getRealPathFromURI(ContentResolver contentResolver, Uri uri, String whereClause) {
        String ret = "";

        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if(cursor!=null)
        {
            boolean moveToFirst = cursor.moveToFirst();
            if(moveToFirst)
            {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if( uri==MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Images.Media.DATA;
                }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Audio.Media.DATA;
                }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }

        return ret;
    }

    private Bitmap getSelectedImage(Uri selectedImage, BitmapFactory.Options options) throws IOException {
        Bitmap bitmap;
        if (isEmulator()) {
            InputStream inputStream =
                    getResources().openRawResource(R.raw.ticketphoto);

            byte[] photoData = new byte[0];
            photoData = IOUtils.toByteArray(inputStream);
            inputStream.close();

            bitmap = BitmapFactory.decodeByteArray(photoData, 0, photoData.length, options);
        }else {
            bitmap = MediaStore.Images.Media.getBitmap(
                    getApplicationContext().getContentResolver(), selectedImage);
        }
        return bitmap;
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
        private String errorMessage ;

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
                errorMessage = e.getMessage();
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
                        context.getString(R.string.scanFailed)  + errorMessage,
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
