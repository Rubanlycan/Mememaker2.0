package com.a0.mememaker2.com.mememaker20;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.drm.DrmStore;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    ImageConverter ic = new ImageConverter();
    private ProgressBar spinner;
    private RelativeLayout mLayout, edt_layout;
    ImageView image_preview, edt_image, txt_edit, txt_size, txt_font, txt_color, txt_bg, txt_delete,
            crop_image,bg_addphoto,bg_color,bg_border,bg_save,menu_more;
    AlertDialog alertDialog_edit, alertDialog_size;
    EditText textEditor;
    SeekBar seek_edit;

    public final int bg_code = 2000;

    float seekvalue;
    boolean image_bool = true;
    boolean colors_pick=true;
    int defaultColor;
    Boolean click_color, click_bg;
    TextView textView;

    String str_txt;
    EditText edt_txt;
    Uri imageUri;
    static int randomNo;
    Uri bg_image_uri;

    float dX;
    float dY;
    int lastAction;
    TextView textview ;
    int txt_getId;
    String strid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         spinner = findViewById(R.id.progressBar);
         spinner.setVisibility(View.GONE);

         menu_more = findViewById(R.id.image_more);
         menu_more.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
                showPopup(v);
             }
         });
        crop_image = (ImageView) findViewById(R.id.image_crop);
        bg_save = (ImageView) findViewById(R.id.image_save);
        bg_addphoto = (ImageView) findViewById(R.id.image_addphoto);
        bg_border = (ImageView) findViewById(R.id.image_bg);
        bg_color = (ImageView) findViewById(R.id.image_bgcolor);

        bg_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                spinner.setVisibility(View.VISIBLE);

              isStoragePermissionGranted();
            }
        });

        bg_addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addphotobg();
            }
        });

        bg_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onColorPickBG();
            }
        });


        mLayout = (RelativeLayout) findViewById(R.id.View_element);
        edt_layout = (RelativeLayout) findViewById(R.id.relative3);
        edt_txt = (EditText) findViewById(R.id.edt_txt);
        edt_image = (ImageView) findViewById(R.id.img_text);

        txt_edit = (ImageView) findViewById(R.id.txt_edit);
        txt_size = (ImageView) findViewById(R.id.txt_size);
        txt_color = (ImageView) findViewById(R.id.txt_color);
        txt_font = (ImageView) findViewById(R.id.txt_font);
        txt_bg = (ImageView) findViewById(R.id.txt_bg);
        txt_delete = (ImageView) findViewById(R.id.txt_delete);


        defaultColor = ContextCompat.getColor(this, R.color.colorPrimary);

        edt_image.setOnClickListener(onClick());
        textView = new TextView(this);
        textView.setText("new Text");


        alertDialog_edit = new AlertDialog.Builder(this).create();
        textEditor = new EditText(this);

        alertDialog_edit.setTitle("Edit the Text");
        alertDialog_edit.setView(textEditor);


        image_preview = (ImageView) findViewById(R.id.img_preview);
        String imageStr = getIntent().getStringExtra("bmp");
        imageUri = Uri.parse(imageStr);
        image_bool = true;
        image_preview.setImageURI(imageUri);



        crop_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_bool) {
                    CropImage.activity(imageUri)
                            .start(MainActivity.this);
                }
                else if(image_bool==false)
                {
                    CropImage.activity(bg_image_uri)
                            .start(MainActivity.this);
                }
            }


        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                image_preview.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                String str_error = error.toString();
                Toast.makeText(getApplicationContext(), str_error, Toast.LENGTH_LONG).show();
            }

        }
        else if(requestCode==bg_code)
        {
            bg_image_uri = data.getData();
            image_preview.setImageURI(bg_image_uri);

        }

    }

    //background image edit tools
    public void addphotobg()
    {
        AlertDialog.Builder customDial = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.custom_dialogue,null);
        Button diag_bg = (Button)mView.findViewById(R.id.diag_bg);
        ImageView diag_bg_remove = (ImageView)mView.findViewById(R.id.diag_bg_remove);
        Button diag_bg_image = (Button)mView.findViewById(R.id.diag_image);
        Button diag_bg_png = (Button)mView.findViewById(R.id.dial_png);
        Button dial_logo = (Button) mView.findViewById(R.id.dial_logo);
        ImageView logo_view = (ImageView)findViewById(R.id.logo_view);



        customDial.setView(mView);
        final  AlertDialog dialog = customDial.create();
        dialog.show();

        diag_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,bg_code);
                image_bool = false;
                dialog.dismiss();
            }
        });



        diag_bg_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                image_preview.setImageURI(null);
                dialog.dismiss();
            }
        });



    }



    //end of bg tools

    //onclick method to add Textview in layout and convert to string
    private View.OnClickListener onClick() {

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLayout.addView(createNewTextView(edt_txt.getText().toString()));

            }
        };
    }

    //method to set textView in layout
    private TextView createNewTextView(String text) {
        final RelativeLayout.LayoutParams lparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textView = new TextView(this);
        textView.setLayoutParams(lparams);
        textView.setText(text);
        textView.setTextSize(30);
        textView.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.color_white));


        textView.setOnTouchListener(MainActivity.this);

        alertDialog_edit.setButton(DialogInterface.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textView.setText(textEditor.getText());
            }
        });


        txt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textEditor.setText(textView.getText());
                alertDialog_edit.show();
            }
        });

        txt_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog btmsheetdialog = new BottomSheetDialog(MainActivity.this);
                final View bView_size = getLayoutInflater().inflate(R.layout.custom_textsize,null);
                btmsheetdialog.setContentView(bView_size);
                btmsheetdialog.show();
                seek_edit = (SeekBar)bView_size.findViewById(R.id.seek1);
                final TextView text_seek = (TextView)bView_size.findViewById(R.id.btm_text_size);
                final TextView txt_pgs =(TextView)bView_size.findViewById(R.id.txt_progress);
                SeekBar seek_rotate =  (SeekBar)bView_size.findViewById(R.id.seek2);
                Switch switch_transparent = (Switch)bView_size.findViewById(R.id.switch_transparent);
                final  TextView text_pgs2= (TextView)bView_size.findViewById(R.id.txt_progress2);
                seek_edit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seekvalue = progress;
                        txt_pgs.setText(progress+"%");
                        textView.setTextSize(progress);

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {


                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                seek_rotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress2, boolean fromUser) {
                        seekvalue = progress2;
                        text_pgs2.setText(progress2);
                        textView.setRotation(progress2);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                switch_transparent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked)
                        {
                         bView_size.setBackgroundColor(Color.parseColor("#8C000000"));
                        }
                        else
                        {
                            bView_size.setBackgroundColor(Color.parseColor("#2d2b2b"));
                        }
                    }
                });
            }
        });

        txt_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "color clicked", Toast.LENGTH_SHORT).show();
                colors_pick=true;
                onColorPick(textView);
            }
        });

        txt_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                colors_pick=false;
                onColorPick(textView);
            }
        });

        txt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayout.removeView(textView);
            }
        });

        return textView;

    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                dX = v.getX() - event.getRawX();
                dY = v.getY() - event.getRawY();
                lastAction = MotionEvent.ACTION_DOWN;
                break;

            case MotionEvent.ACTION_MOVE:
                v.setY(event.getRawY() + dY);
                v.setX(event.getRawX() + dX);
                lastAction = MotionEvent.ACTION_MOVE;
                break;

            case MotionEvent.ACTION_UP:
                if (lastAction == MotionEvent.ACTION_DOWN)
                {
                    edt_layout.setVisibility(View.VISIBLE);

                }


                break;

            default:
                return false;
        }
        return true;
    }

    public void onColorPick(final TextView textcolor) {


        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }


            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                if (colors_pick==true) {
                    textcolor.setTextColor(defaultColor);
                }
                else if (colors_pick==false)
                {
                    textcolor.setBackgroundColor(defaultColor);
                }

            }
        });

        colorPicker.show();

    }


    public void onColorPickBG() {


        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }


            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                mLayout.setBackgroundColor(defaultColor);


            }
        });

        colorPicker.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==1)
        {
            Log.i("tag","Permission is granted");
         saveImage();
        }
    }

    private boolean isStoragePermissionGranted()
    {
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {

                Log.i("tag","Permission is granted");

             saveImage();
                return true;
            }
            else {
                Log.i("tag", "Permission is revoked");
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("tag","Permission is Automatically granted");
            saveImage();
            return true;
        }
    }

public  void saveImage()
{
    File file = ic.saveBitMap(MainActivity.this,mLayout);
    if (file != null) {
        Log.i("TAG", "Drawing saved to the gallery!");
        spinner.setVisibility(View.GONE);
        Snackbar.make(mLayout,"Image saved successfully",3000).show();
    } else {
        Log.i("TAG", "Oops! Image could not be saved.");
        spinner.setVisibility(View.GONE);
        Snackbar.make(mLayout,"Error while saving image",3000).show();
    }

}

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId())
            {
                case R.id.action_share:
                    Toast.makeText(MainActivity.this,"share",Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_tips:
                    Toast.makeText(MainActivity.this,"Tips",Toast.LENGTH_SHORT).show();
                    return true;
                default:

                    Toast.makeText(MainActivity.this,"default",Toast.LENGTH_SHORT).show();
                    return false;
            }
            }
        });
        popup.show();
    }



}
