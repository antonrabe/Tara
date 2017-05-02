package edu.ateneo.cie199.tara;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private ArrayList<User> mUserList = new ArrayList<>();

    private SyncUsersTask mSyncUsersTask = null;

    private String mName = "";
    private String mUsername = "";
    private String mPassword = "";
    private String mCompany = "";
    private String mContact = "";

    private static final int PICK_IMAGE = 100;
    ImageView imageView;
    Uri imageUri;

    private UploadRegisterTask mUploadRegisterTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText _edtName = (EditText) findViewById(R.id.edt_name);
        final EditText _edtCompany = (EditText) findViewById(R.id.edt_company);
        final EditText _edtContact = (EditText) findViewById(R.id.edt_contact);
        final EditText _edtUsername = (EditText) findViewById(R.id.edt_username);
        final EditText _edtPassword = (EditText) findViewById(R.id.edt_password);

        Intent recvdIntent = getIntent();

        mName = recvdIntent.getStringExtra("NAME");
        mUsername = recvdIntent.getStringExtra("USERNAME");
        mPassword = recvdIntent.getStringExtra("PASSWORD");
        mCompany = recvdIntent.getStringExtra("COMPANY");
        mContact = recvdIntent.getStringExtra("CONTACT");

        imageView = (ImageView)findViewById(R.id.img_profile);

        Button btnSubmit = (Button) findViewById(R.id.btn_register);
        btnSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String rName = _edtName.getText().toString();
                        String rCompany = _edtCompany.getText().toString();
                        String rContact = _edtContact.getText().toString();
                        String rUsername = _edtUsername.getText().toString();
                        String rPassword = _edtPassword.getText().toString();

                        mUploadRegisterTask = new UploadRegisterTask();
                        mUploadRegisterTask.execute(rUsername, rName, rCompany, rContact, rPassword);

                        Intent launchLoginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(launchLoginIntent);

                        registerToast();
                        return;
                    }
                }
        );

        Button upload = (Button) findViewById(R.id.btn_upload);
        upload.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGallery();
                    }
                });
        return;
    }


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }


    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void registerToast() {
        ImageView profImg = (ImageView) findViewById(R.id.img_profile);
        EditText edtName = (EditText) findViewById(R.id.edt_name);
        EditText edtCompany = (EditText) findViewById(R.id.edt_company);
        EditText edtContact = (EditText) findViewById(R.id.edt_contact);
        EditText edtUsername = (EditText) findViewById(R.id.edt_username);
        EditText edtPassword = (EditText) findViewById(R.id.edt_password);


        String nameStr = edtName.getText().toString();
        String companyStr = edtCompany.getText().toString();
        String contactStr = edtContact.getText().toString();
        String usernameStr = edtUsername.getText().toString();
        String passwordStr = edtPassword.getText().toString();

        if (nameStr.isEmpty()) {
            edtName.setError("Must enter name");
        }

        else if (companyStr.isEmpty()) {
            edtPassword.setError("Must enter company");
        }

        else if (contactStr.isEmpty()) {
            edtContact.setError("Must enter contact");
        }

        else if (usernameStr.isEmpty() || usernameStr.length() < 6) {
            edtUsername.setError("Username must be at least 6 characters");
        }

        else if (passwordStr.isEmpty() || passwordStr.length() < 6) {
            edtPassword.setError("Password must be at least 6 characters");
        }

        else  {

            submitUserData();
            dialogPrompt();
        }
    }

    public void dialogPrompt()
    {
        AlertDialog .Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Congratulations!");
        builder.setMessage("You have successfully registered!");
        builder.setPositiveButton("Tara na!", new DialogInterface.OnClickListener() {
            //  @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(RegisterActivity.this, Category.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }
    private void submitUserData() {
        EditText edtName = (EditText) findViewById(R.id.edt_name);
        EditText edtCompany = (EditText) findViewById(R.id.edt_company);
        EditText edtContact = (EditText) findViewById(R.id.edt_contact);
        EditText edtUsername = (EditText) findViewById(R.id.edt_username);
        EditText edtPassword = (EditText) findViewById(R.id.edt_password);

        TaraApp app = (TaraApp) getApplication();
        app.saveUserData(edtName.getText().toString(),edtUsername.getText().toString(),
                edtPassword.getText().toString(),
                edtCompany.getText().toString(),
                edtContact.getText().toString() );
        finish();
        return;
    }

    private class UploadRegisterTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            TaraApp app = (TaraApp) getApplication();
            return app.registerNewUser(params[0], params[1], params[2], params[3], params[4]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mUploadRegisterTask = null;

            if (result == true) {
                if (mUploadRegisterTask != null) {
                    Toast.makeText(RegisterActivity.this,
                            "Already registering",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                mSyncUsersTask = new SyncUsersTask();
                mSyncUsersTask.execute();
            } else {
                Toast.makeText(RegisterActivity.this,
                        "Register failed",
                        Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(result);
            return;
        }
    }

    private class SyncUsersTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            TaraApp app = (TaraApp) getApplication();
            ArrayList<User> syncedUsers = app.syncUser();

            if (syncedUsers.size() > 0) {
                mUserList.clear();
                mUserList.addAll(syncedUsers);
            }

            for(int i=0; i<mUserList.size(); i++) {
                Log.i("Users: ", mUserList.get(i).toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mSyncUsersTask = null;

            super.onPostExecute(aVoid);
            return;
        }
    }
}
