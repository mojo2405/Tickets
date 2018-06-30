package il.co.myapp.tickets.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.IOUtils;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import il.co.myapp.tickets.R;
import il.co.myapp.tickets.controller.AppController;
import il.co.myapp.tickets.data.AsyncTicketResponse;
import il.co.myapp.tickets.data.TrafficTicketData;
import il.co.myapp.tickets.menu.MenuActivity;
import il.co.myapp.tickets.model.Ticket;

public class NewTicketFinalPhase extends MenuActivity {
    private static final String TAG = NewTicketFinalPhase.class.getSimpleName();

    private Context context;

    static final int GALLERY_REPORT_REQUEST = 2;
    static final int GALLERY_ID_REQUEST = 3;
    static final int GALLERY_PROOFS_REQUEST = 4;
    static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE=101;

    private Button submitTicketButton;
    private ImageButton  getImageFromGallery, addIdButton, addProofsButton;
    String idBlob;
    JSONArray proofsArray = new JSONArray();
    private String scanBlob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket_final_phase);

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



        submitTicketButton = (Button) findViewById(R.id.newTicketSubmitButton);
        submitTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new TrafficTicketData().SubmitTicket(getApplicationContext(),
                            GetTicketsDetailsValues(), new AsyncTicketResponse() {
                                @Override
                                public void TicketsDataReceived(List<Ticket> tickets) {

                                    Log.w(TAG, tickets.toString());
                                    startActivity(new Intent(NewTicketFinalPhase.this,
                                            DashboardActivity.class));
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private HashMap<String, String> GetTicketsDetailsValues() {
        HashMap<String, String> tickets = AppController.getInstance().getTicketFields();

        tickets.put("email", AppController.getInstance().getUser().getEmail());
        tickets.put("scanBlobBase64",scanBlob);
        tickets.put("idBlobBase64",idBlob);
        tickets.put("proofsArrayBase64",proofsArray.toString());

        return tickets;
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
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        }else {
            getImageFromGalleryIntent(GALLERY_REPORT_REQUEST);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImageFromGalleryIntent(GALLERY_REPORT_REQUEST);
                } else {
                    Toast.makeText(getApplicationContext(),
                            context.getString(R.string.readPermissionRequired) ,
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private void getImageFromGalleryIntent(int galleryReportRequest) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, galleryReportRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 2;

            switch (requestCode) {
                case GALLERY_REPORT_REQUEST:
                    try {
                        Bitmap bitmap = getSelectedImage(data.getData(), options);
                        scanBlob =  Base64.encodeToString(bitmapToBlob(bitmap), Base64.DEFAULT);
                        findViewById(R.id.galleryAttachedTotalText).setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Scan Image from gallery
//                        pathToPhotoFile = getRealPathFromURI(getContentResolver(),data.getData(),null);
//                        new GoogleVision().execute();
//                        Log.v(TAG,"Got path " + pathToPhotoFile);
                    break;
                case GALLERY_ID_REQUEST:
                    try {
                        Bitmap bitmap = getSelectedImage(data.getData(), options);
                        idBlob =  Base64.encodeToString(bitmapToBlob(bitmap), Base64.DEFAULT);
                        findViewById(R.id.idTotalText).setVisibility(View.VISIBLE);
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
                        TextView proofsText = findViewById(R.id.proofsTotalText);
                        proofsText.setVisibility(View.VISIBLE);
                        proofsText.setText(getResources().getQuantityString(R.plurals.proofsAttachedText,count,count));

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

}
