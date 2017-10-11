package com.example.hopef.testproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.hopef.testproject.Serializer.serialize;

public class NewPageActivity extends AppCompatActivity {

    String bookName = "";
    final int RESULT_LOAD_IMAGE = 5645;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        String mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        bookName = intent.getStringExtra("Item Title");
        Intent pickPictureIntent = new Intent(Intent.ACTION_PICK);
        pickPictureIntent.setType("image/*");
        pickPictureIntent.putExtra("Item Title", bookName);
        startActivityForResult(pickPictureIntent, RESULT_LOAD_IMAGE);
        /**if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Log.e("TAG!", this.toString());

                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }**/
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap ph = null;
        try {
            Uri uri = data.getData();
            ph = (Bitmap) BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException fnfEx) {
            Log.e("TAG!", "No such file");
        }
        //Bitmap ph = (Bitmap) data.getExtras().get(MediaStore.EXTRA_OUTPUT);
        try {
            callCloudVision(ph);
        } catch (IOException ioE) {
        }
        BookReaderDbHelper mDBHelper = new BookReaderDbHelper(this);
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put("BookName", bookName);
        insertValues.put("PageData", serialize(ph));
        db.insert("PageList", null, insertValues);
        Intent i = new Intent(NewPageActivity.this, BookActivity.class);
        i.putExtra("Item Title", bookName);
        startActivity(i);
    }

    private void callCloudVision(final Bitmap bitmap) throws IOException {
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    TextRecognizer.Builder trb = new TextRecognizer.Builder(NewPageActivity.this);
                    TextRecognizer textRec = trb.build();
                    Frame.Builder frBuild = new Frame.Builder();
                    frBuild.setBitmap(bitmap);
                    if (textRec.isOperational()) {
                        SparseArray<TextBlock> sATB = textRec.detect(frBuild.build());
                        for (int i = 0; i < sATB.size(); i++) {
                            TextBlock item = sATB.valueAt(i);
                            Log.e("TAG!", item.getValue());
                        }
                    }
                    textRec.release();
                } catch (Exception e) {
                }
                return "A";
                    /**HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    VisionRequestInitializer requestInitializer =
                            new VisionRequestInitializer(CLOUD_VISION_API_KEY) {**/
                                /**
                                 * We override this so we can inject important identifying fields into the HTTP
                                 * headers. This enables use of a restricted cloud platform API key.
                                 */
                                /**@Override
                                protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                        throws IOException {
                                    super.initializeVisionRequest(visionRequest);

                                    String packageName = getPackageName();
                                    visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                                    String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                                    visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                                }
                            };

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(requestInitializer);

                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.d("TAG", "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d("TAG", "failed to make API request because of other IOException " +
                            e.getMessage());
                };
                return "Cloud Vision API request failed. Check logs for details.";*/
            }
        }.execute();
    }
}
