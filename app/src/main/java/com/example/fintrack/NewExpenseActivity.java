package com.example.fintrack;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fintrack.OpenExpensesActivity;
import com.example.fintrack.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class NewExpenseActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    private String userID;
    private String userType;
    private String expenseStatus;
    private String expenseTitle1;
    private String title;
    private String imageUrl;
    private String date;
    private String monthYear;
    Resources res;
    private String[] months;

    private EditText expenseTitle;
    private EditText expenseItemTitle;
    private EditText expenseItemDate;
    private EditText expenseItemPrice;
    private ImageView expenseItemImage;
    private Button addExpenseImage;
    private Button addExpenseItem;
    private Spinner gSpinner;
    private Uri imageUri;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    DatabaseReference mDatabase;

    private final ActivityResultLauncher<Intent> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                    expenseItemImage.setImageBitmap(photo);
                    imageUri = getImageUri(photo);
                }
            });

    private final ActivityResultLauncher<Intent> choosePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    imageUri = result.getData().getData();
                    expenseItemImage.setImageURI(imageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        userID = getIntent().getStringExtra("userID");
        expenseStatus = getIntent().getStringExtra("expenseStatus");
        userType = getIntent().getStringExtra("userType");

        expenseTitle = findViewById(R.id.new_expense_title);
        expenseItemTitle = findViewById(R.id.new_expense_item_title);
        expenseItemDate = findViewById(R.id.new_expense_item_date);
        expenseItemPrice = findViewById(R.id.new_expense_item_price);
        expenseItemImage = findViewById(R.id.new_expense_item_image);
        addExpenseImage = findViewById(R.id.new_expense_add_image);
        addExpenseItem = findViewById(R.id.add_expense_item);
        gSpinner = findViewById(R.id.spinner_expense_group);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.expenseGroups, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gSpinner.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference(expenseStatus).child(userID);

        res = getResources();
        months = res.getStringArray(R.array.months);

        expenseItemDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        NewExpenseActivity.this,
                        android.R.style.Theme_Holo_Light_DarkActionBar,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                if(month < 10) {
                    date = day + ".0" + month + "." + year;
                } else {
                    date = day + "." + month + "." + year;
                }
                monthYear = months[month - 1] + " " +year;
                expenseItemDate.setText(date);
            }
        };

        addExpenseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    showImageSourceOptions();
                }
            }
        });

        addExpenseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseTitle1 = expenseTitle.getText().toString().trim();
                title = expenseItemTitle.getText().toString().trim();
                //String date = expenseItemDate.getText().toString().trim();
                String price = expenseItemPrice.getText().toString().trim();

                if (imageUri != null) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();
                    StorageReference imagesRef = storageRef.child("images/" + imageUri.getLastPathSegment());

                    imagesRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    imageUrl = uri.toString();
                                    mDatabase.child(expenseTitle1).child(title).child("expenseImage").setValue(imageUrl);
                                });
                            })
                            .addOnFailureListener(e -> Toast.makeText(NewExpenseActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(NewExpenseActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
                }

                mDatabase.child(expenseTitle1).child(title).child("expenseDate").setValue(date);
                mDatabase.child(expenseTitle1).child(title).child("expenseMoney").setValue(price);
                //mDatabase.child(expenseTitle1).child(title).child("expenseImage").setValue(imageUrl);
                mDatabase.child(expenseTitle1).child(title).child("expenseGroup").setValue(gSpinner.getSelectedItem().toString());
                mDatabase.child(expenseTitle1).child(title).child("expenseMonthYear").setValue(monthYear);

                /*Intent intent = new Intent(NewExpenseActivity.this, OpenExpensesActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("userType",userType);
                intent.putExtra("expenseStatus", expenseStatus);
                startActivity(intent);*/
                Intent intent = new Intent(NewExpenseActivity.this, ExpenseActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("expenseStatus", expenseStatus);
                intent.putExtra("expenseTitle", expenseTitle1);
                intent.putExtra("currentTitle", title);
                startActivity(intent);
            }
        });

    }

    private Uri getImageUri(Bitmap bitmap) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "title");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    private boolean checkAndRequestPermissions() {
        boolean cameraPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storagePermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (!cameraPermissionGranted || !storagePermissionGranted) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showImageSourceOptions();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showImageSourceOptions() {
        String[] options = {"Camera", "Gallery"};
        new AlertDialog.Builder(this)
                .setTitle("Choose an option")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        takePictureLauncher.launch(takePictureIntent);
                    } else {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        choosePictureLauncher.launch(pickPhoto);
                    }
                })
                .show();
    }
}