package il.co.myapp.tickets.utils;

import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import il.co.myapp.tickets.model.Ticket;

public class ImageParser {

    Ticket ticket = new Ticket ();
    public ImageParser() {

    }

    public Ticket extractText (FirebaseVisionText firebaseVisionText) {
        for (FirebaseVisionText.Block block: firebaseVisionText.getBlocks()) {
            Rect boundingBox = block.getBoundingBox();
            Point[] cornerPoints = block.getCornerPoints();
            String text = block.getText();

            for (FirebaseVisionText.Line line: block.getLines()) {
                // ...
                for (FirebaseVisionText.Element element: line.getElements()) {
                    // ...
                }
            }
        }
        return ticket;
    }
}
