package com.ykko.app.ui.admin_add_menu;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.ykko.app.MainActivity;
import com.ykko.app.R;
import com.ykko.app.data.model.FoodMenu;
import com.ykko.app.data.model.Upload;
import com.ykko.app.ui.FirebaseDatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class AddMenuFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;
    private String downloadUrl = "";
    private TextInputEditText foodNameEditText;
    private TextInputEditText foodTypeEditText;
    private TextInputEditText foodPriceEditText;
    private TextInputEditText foodAvailableEditText;
    private CircleImageView circleImageView;
    private FoodMenu newMenu = new FoodMenu();
    public String key;
    public Button saveBtn;
    public FoodMenu editmenu = new FoodMenu();
    public ProgressBar spinner;
    public LinearLayout main_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_menu, container, false);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("menuPosts").child("posts");

        main_layout=root.findViewById(R.id.admin_save_main_layout);
        spinner = root.findViewById(R.id.saving_loading_progress);
        foodNameEditText = root.findViewById(R.id.add_food_menu_name);
        foodTypeEditText = root.findViewById(R.id.add_food_menu_type);
        foodPriceEditText = root.findViewById(R.id.add_food_menu_price);
        foodAvailableEditText = root.findViewById(R.id.add_food_menu_available);
        circleImageView = root.findViewById(R.id.circle_image_view);
        final Button chooseBtn = root.findViewById(R.id.choose_photo_button);

        saveBtn = root.findViewById(R.id.save_menu_button);

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

//////////////////////////////////Edit
        boolean task = false;

        if (getArguments() != null) {
            // do something with task

        editmenu = getArguments().getParcelable("menuDetailKey");
        key = getArguments().getString("menuDetailKeyKey");

        if (editmenu != null) {
            try {
                Picasso.get().load(editmenu.imageUrl).into(circleImageView);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
//                try {
//
//                    mImageUri= BitmapFactory.decodeStream((InputStream)url.getContent()).;
//                } catch (IOException e) {
//                    //Log.e(TAG, e.getMessage());
//                }
                URL url = new URL(editmenu.imageUrl);
                mImageUri = getImageUri(Objects.requireNonNull(getContext()), BitmapFactory.decodeStream((InputStream)url.getContent()));
                //mImageUri = Uri.parse(editmenu.imageUrl);
                foodNameEditText.setText(editmenu.foodStickName);
                foodTypeEditText.setText(editmenu.foodStickType);
                foodPriceEditText.setText(editmenu.price);
                foodAvailableEditText.setText(editmenu.available_branch);
            } catch (Exception ex) {

            }
        }
        }
//////////////////////////Edit


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinner.setVisibility(View.VISIBLE);
                saveBtn.setEnabled(false);
                Float fl = (float) 0.3;
                main_layout.setAlpha(fl);
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
                } else {
                    uploadFile(v);
                }
            }
        });


        return root;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(circleImageView);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(final View v) {
        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));


            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //mProgressBar.setProgress(0);
                                }
                            }, 500);

                            //Toast.makeText(getActivity(), "Upload successful", Toast.LENGTH_LONG).show();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = uri.toString();
                                    saveMenu(v);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            //mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
            spinner.setVisibility(View.GONE);
            saveBtn.setEnabled(true);
            Float fl = (float) 1;
            main_layout.setAlpha(fl);
        }
    }

    private void saveMenu(View v) {
        String name = foodNameEditText.getText().toString();
        String type = foodTypeEditText.getText().toString();
        String price = foodPriceEditText.getText().toString();
        String branch = foodAvailableEditText.getText().toString();
        newMenu = new FoodMenu(name, type, price, branch, downloadUrl);

        FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
        if (getArguments() != null) {
            databaseHelper.deleteData("menuPosts", key, new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsInserted() {

                }

                @Override
                public void DataIsUpdated() {

                }

                @Override
                public void DataIsDeleted() {
                }
            });
            databaseHelper.addData("menuPosts", newMenu, new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsInserted() {
                    Toast.makeText(getActivity(), "Save Menu Successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void DataIsUpdated() {

                }

                @Override
                public void DataIsDeleted() {

                }
            });
        } else {
            databaseHelper.addData("menuPosts", newMenu, new FirebaseDatabaseHelper.DataStatus() {
                @Override
                public void DataIsInserted() {
                    Toast.makeText(getActivity(), "Save Menu Successful", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void DataIsUpdated() {

                }

                @Override
                public void DataIsDeleted() {

                }
            });
        }
        spinner.setVisibility(View.GONE);
        saveBtn.setEnabled(true);
        Float fl = (float) 1;
        main_layout.setAlpha(fl);
        Navigation.findNavController(v).navigate(R.id.nav_admin_home);
    }


}
