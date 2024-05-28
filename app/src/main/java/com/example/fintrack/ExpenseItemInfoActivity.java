package com.example.fintrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExpenseItemInfoActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final String TAG = "ExpenseItemInfoActivity";

    private String userID;
    private String employeeID;
    private String userType;
    private String userType1;
    private String expenseTitle;
    private String expenseItemTitle;
    private String expenseStatus;

    private String date;
    private String money;
    private String image;

    private TextView expenseItemTitle1;
    private TextView expenseItemDate;
    private TextView expenseItemPrice;
    private ImageView expenseItemImage;
    private Button deleteExpenseItem;
    private Button downloadImage;

    private Uri imageUri;
    DatabaseReference mDatabase;
    FirebaseStorage mStorage;
    StorageReference imageRef;
    Bitmap mImage = null;
    Executor myExecutor = Executors.newSingleThreadExecutor();
    Handler myHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_item_info);

        userID = getIntent().getStringExtra("userID");
        employeeID = getIntent().getStringExtra("employeeID");
        userType = getIntent().getStringExtra("userType");
        userType1 = getIntent().getStringExtra("userType1");
        expenseTitle = getIntent().getStringExtra("expenseTitle");
        expenseItemTitle = getIntent().getStringExtra("expenseItemTitle");
        expenseStatus = getIntent().getStringExtra("expenseStatus");

        if(employeeID == null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(userID).child(expenseTitle).child(expenseItemTitle);
        }

        if(employeeID != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(employeeID).child(expenseTitle).child(expenseItemTitle);
        }
        expenseItemTitle1 = findViewById(R.id.expense_item_title1);
        expenseItemDate = findViewById(R.id.expense_item_date1);
        expenseItemPrice = findViewById(R.id.expense_item_price1);
        expenseItemImage = findViewById(R.id.new_expense_item_image2);
        deleteExpenseItem = findViewById(R.id.new_expense_delete);
        downloadImage = findViewById(R.id.download_image);

        if (expenseStatus.equals("requestedexpenses")) {
            deleteExpenseItem.setVisibility(View.VISIBLE);
            deleteExpenseItem.setEnabled(true);
            downloadImage.setEnabled(false);
        }

        if ((expenseStatus.equals("openexpenses") || expenseStatus.equals("myexpenses")) && userType1!= null) {
            downloadImage.setVisibility(View.VISIBLE);
            downloadImage.setEnabled(true);
            deleteExpenseItem.setEnabled(false);
        }

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                date = snapshot.child("expenseDate").getValue(String.class);
                money = snapshot.child("expenseMoney").getValue(String.class);
                image = snapshot.child("expenseImage").getValue(String.class);

                if(image == null){
                    image = "https://firebasestorage.googleapis.com/v0/b/fintrack-b36fa.appspot.com/o/images%2Fwhite.jpg?alt=media&token=db5a29aa-f963-4e76-9ec6-83a6da50e0fe";
                }

                expenseItemTitle1.setText(expenseItemTitle);
                expenseItemDate.setText("Date: " + date);
                expenseItemPrice.setText("Price: " + money + "$");

                if (image != null && !image.isEmpty()) {
                    imageUri = Uri.parse(image);
                } else {
                    imageUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/fintrack-b36fa.appspot.com/o/images%2Fwhite.jpg?alt=media&token=db5a29aa-f963-4e76-9ec6-83a6da50e0fe");
                }

                if (imageUri != null) {
                    Glide.with(ExpenseItemInfoActivity.this)
                            .load(imageUri)
                            .into(expenseItemImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        deleteExpenseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ExpenseItemInfoActivity.this, "Expense item deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ExpenseItemInfoActivity.this, "Failed to delete expense item", Toast.LENGTH_SHORT).show();
                    }
                });

                String[] parts = image.split("%2F");
                String pathAndToken = parts[parts.length - 1];
                String path = pathAndToken.split("\\?")[0];

                mStorage = FirebaseStorage.getInstance();
                imageRef = mStorage.getReference().child("images/").child(path);

                imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Image deleted successfully");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
        });

        downloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myExecutor.execute(() -> {
                    mImage = mLoad(image);
                    myHandler.post(() -> {
                        expenseItemImage.setImageBitmap(mImage);
                        if (mImage != null) {
                            mSaveMediaToStorage(mImage);
                        }
                    });
                });
            }
        });
    }

    private Bitmap mLoad(String string) {
        URL url = mStringToURL(string);
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            return BitmapFactory.decodeStream(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show());
        }
        return null;
    }

    private URL mStringToURL(String string) {
        try {
            return new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void mSaveMediaToStorage(Bitmap bitmap) {
        String filename = System.currentTimeMillis() + ".jpg";
        OutputStream fos = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            try {
                fos = getContentResolver().openOutputStream(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            File imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File image = new File(imagesDir, filename);
            try {
                fos = new FileOutputStream(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (fos != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Saved to Gallery", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
