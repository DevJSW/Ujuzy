package com.ujuzy.ujuzy.ujuzy.Activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmUser;

import io.realm.Realm;
import io.realm.RealmChangeListener;

public class EditProfileActivity extends AppCompatActivity {

    private Realm realm;
    private RealmChangeListener realmChangeListener;
    private ImageView editPhoto, back;
    private Uri resultUri = null;
    private Uri mImageUri = null;
    private static int GALLERY_REQUEST =1;
    private EditText phoneInput, nameInput, bioInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initEdit();
        initEditInputs();
    }

    private void initEditInputs()
    {
        phoneInput = (EditText) findViewById(R.id.editPhone);
        nameInput = (EditText) findViewById(R.id.editName);
        bioInput = (EditText) findViewById(R.id.editBio);

        realm = Realm.getDefaultInstance();
        RealmUser realmUser = realm.where(RealmUser.class).findFirst();
        if (realmUser != null)
        {
            if (realmUser.getFirstname() != null)
                nameInput.setText(realmUser.getFirstname() + " " + realmUser.getLastname());
            if (realmUser.getPhone() != null)
                phoneInput.setText(realmUser.getPhone());
            if (realmUser.getBio() != null)
                bioInput.setText((Integer) realmUser.getBio());
        }

    }

    private void initEdit()
    {
        editPhoto = (ImageView) findViewById(R.id.editPhoto);
        editPhoto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {

            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1, 1)
                    .start(EditProfileActivity.this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                editPhoto.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        realm = Realm.getDefaultInstance();
        if (realmChangeListener != null)
            realm.removeChangeListener(realmChangeListener);
    }

}
