package com.ykko.app.ui.admin_food_detail;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.ykko.app.R;
import com.ykko.app.data.model.FoodMenu;
import com.ykko.app.ui.FirebaseDatabaseHelper;

import org.w3c.dom.Text;

import static android.app.Activity.RESULT_OK;

public class AdminFoodDetailFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    public String key;
    private StorageTask mUploadTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_admin_food_detail, container, false);
        final FoodMenu foodMenu = getArguments().getParcelable("menuDetailKey");
         key = getArguments().getString("menuDetailKeyKey");
        ImageView imageView = root.findViewById(R.id.menu_detail_image);
        TextView name = root.findViewById(R.id.food_detail_name);
        TextView type = root.findViewById(R.id.food_detail_type);
        TextView price = root.findViewById(R.id.food_detail_price);
        Button deleteBtn = root.findViewById(R.id.delete_detail_menu_button);
        Button editbtn = root.findViewById(R.id.edit_detail_menu_button);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("menuDetailKey", foodMenu);
                bundle.putString("menuDetailKeyKey",key);
                Navigation.findNavController(v).navigate(R.id.nav_admin_add_menu,bundle);
            }
        });
        Picasso.get().load(foodMenu.imageUrl).into(imageView);
        name.setText(foodMenu.foodStickName);
        type.setText(foodMenu.foodStickType);
        price.setText(foodMenu.price);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabaseHelper databaseHelper = new FirebaseDatabaseHelper();
                databaseHelper.deleteData("menuPosts", key, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {
                        Toast.makeText(getActivity(),"Delete Food Menu",Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                    }
                });
            }
        });


        return root;
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }



//    private String getFileExtension(Uri uri) {
//        ContentResolver cR = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }
//
//    private void uploadFile() {
//        if (mImageUri != null) {
//            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
//                    + "." + getFileExtension(mImageUri));
//
//            mUploadTask = fileReference.putFile(mImageUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mProgressBar.setProgress(0);
//                                }
//                            }, 500);
//
//                            Toast.makeText(MainActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
//                            Upload upload = new Upload(mEditTextFileName.getText().toString().trim(),
//                                    taskSnapshot.getDownloadUrl().toString());
//                            String uploadId = mDatabaseRef.push().getKey();
//                            mDatabaseRef.child(uploadId).setValue(upload);
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            mProgressBar.setProgress((int) progress);
//                        }
//                    });
//        } else {
//            Toast.makeText(getActivity(), "No file selected", Toast.LENGTH_SHORT).show();
//        }
//    }
}
