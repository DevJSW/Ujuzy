package com.ujuzy.ujuzy.ujuzy.BottomSheetFragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.ujuzy.ujuzy.ujuzy.R;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmFavouriteHelper;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmFavouritesAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceAdapter;
import com.ujuzy.ujuzy.ujuzy.Realm.RealmServiceImage;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

import static android.app.Activity.RESULT_OK;
import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragments extends BottomSheetDialogFragment {

    private ImageView editPhoto, back;
    private Uri resultUri = null;
    private Uri mImageUri = null;
    private static int GALLERY_REQUEST =1;
    View v;

    public EditProfileFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_edit, container, false);

        initEdit();
        return v;
    }

    private void initEdit()
    {
        editPhoto = (ImageView) v.findViewById(R.id.editPhoto);
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
                    .start(getActivity());

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

}
