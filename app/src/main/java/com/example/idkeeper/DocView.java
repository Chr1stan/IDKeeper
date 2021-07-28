
package com.example.idkeeper;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DocView extends AppCompatActivity {

    private GridView list;
    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private ActionBarDrawerToggle toggle;

    private databaseHelper db;
    private databaseHelper2 db2;

    private TextView tvWelcome;
    private PdfDocument pdfDoc;
    private String currentPhotoPath;

    private boolean fabExpanded;
    private FloatingActionButton btAddId, btTakePhoto, btGallery, btConvertToPdf;

    private void closeSubMenusFab(){
        btAddId.setImageResource(R.drawable.ic_baseline_add_24);
        //btConvertToPdf.setVisibility(View.INVISIBLE);
        btTakePhoto.setVisibility(View.INVISIBLE);
        btGallery.setVisibility(View.INVISIBLE);
        fabExpanded = false;
    }

    private void openSubMenusFab(){
        btAddId.setImageResource(R.drawable.ic_baseline_close_24);
        //btConvertToPdf.setVisibility(View.VISIBLE);
        btTakePhoto.setVisibility(View.VISIBLE);
        btGallery.setVisibility(View.VISIBLE);
        fabExpanded = true;
    }

    public Bitmap bitmap;
    private int images[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_view);

        btTakePhoto =  this.findViewById(R.id.btTakePhoto);
        btTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File imagefile = File.createTempFile("image", ".jpg", dir);

                    currentPhotoPath = imagefile.getAbsolutePath();

                    Uri imageUri = FileProvider.getUriForFile(DocView.this, "com.example.idkeeper.fileprovider", imagefile);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent , 1);
                } catch (IOException e) {e.printStackTrace();}
            }
        });

        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        btGallery = this.findViewById(R.id.btGallery);
        btGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{startActivityForResult(intentGallery, 2);}catch (Exception e){}
            }
        });

        btConvertToPdf = this.findViewById(R.id.btConvertToPDF);
        btConvertToPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*convertToPDF(bitmap);*/
            }
        });

        btAddId =  this.findViewById(R.id.btAddId);
        btAddId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                }else{
                    openSubMenusFab();
                }
            }
        });

        db = new databaseHelper(this);
        Cursor cursor = db.getUser();

        db2 = new databaseHelper2(this);
        Cursor cursor2 = db2.getID();

        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setVisibility(View.VISIBLE);
        try{
            if(cursor.getCount() == 0) {
                tvWelcome.setText("Click the button\n to add Your ID's");
            }else{
                cursor.moveToLast();
                tvWelcome.setText("Welcome, " +cursor.getString(1)+"!");
            }
        }catch (Exception e){e.printStackTrace();}

        ArrayList <String> arrayList = new ArrayList<>();

        if(cursor2.getCount() == 0){
            Toast.makeText(this, "No ID's", Toast.LENGTH_SHORT).show();
            arrayList.add("CLICK BELLOW TO ADD DOCUMENTS");
        }else{
            cursor2.moveToFirst();
            arrayList.add(cursor2.getString(1));
            while (cursor.moveToNext()){
                arrayList.add(cursor2.getString(1));
            }
        }
        String id[] = arrayList.toString().split(" ");
        MainAdapter arrayAdapter = new MainAdapter(DocView.this, id, images);

        list = findViewById(R.id.list);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = String.valueOf(list.getItemAtPosition(position));
                Toast.makeText(DocView.this, clickedItem, Toast.LENGTH_SHORT).show();
            }
        });

        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menuItem1:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        Toast.makeText(DocView.this, "Dark theme", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuItem2:
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        Toast.makeText(DocView.this, "Ligth theme", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuItem3:
                        Toast.makeText(DocView.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menuItem4:
                        AlertDialog.Builder adb = new AlertDialog.Builder(DocView.this);
                        adb.setTitle("Follow me on instagram :)");
                        adb.setMessage("christian.jpg");
                        adb.setPositiveButton("Ok", null);
                        adb.show();
                        break;
                    default: break;
                }
                return true;
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(toggle.onOptionsItemSelected(item)){return true;}
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent returnIntent) {
        super.onActivityResult(requestCode, resultCode, returnIntent);
        if(requestCode == 1 && resultCode == RESULT_OK) {
            bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            Intent editView_intent = new Intent(getApplicationContext(), EditView.class);
            //editView_intent.putExtra("bitmap", bitmap);
            startActivity(editView_intent);
        }
    }

    public void convertToPDF(Bitmap bitmap){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            pdfDoc = new PdfDocument();
            PdfDocument.PageInfo pInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();

            PdfDocument.Page page = pdfDoc.startPage(pInfo);
            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#F4AE59"));
            canvas.drawPaint(paint);

            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
            paint.setColor(Color.BLUE);
            canvas.drawBitmap(bitmap, 0, 0, null);
            pdfDoc.finishPage(page);

            File dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            try {
                File file = File.createTempFile("converted", ".pdf", dir);
                FileOutputStream fileOut = new FileOutputStream(file);
                pdfDoc.writeTo(fileOut);
            } catch (Exception e) {e.printStackTrace();}
            pdfDoc.close();
        }
    }
}
//class for the grid view
class MainAdapter extends BaseAdapter {

    private databaseHelper2 db2;
    private Context context;
    private LayoutInflater inflater;
    private String[] numberWord;
    private int[] numberImages;


    public MainAdapter(Context context, String[] numberWord, int[] numberImages) {
        this.context = context;
        this.numberWord = numberWord;
        this.numberImages = numberImages;
    }

    @Override
    public int getCount() {return numberWord.length;}

    @Override
    public Object getItem(int position) {
        db2 = new databaseHelper2(context);
        Cursor cursor = db2.getID();

        if (cursor.getCount() != 0) {
            try{cursor.move(position+1);}catch(Exception e){return "the cursor at this position is empty";}
        }
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle("ID Code");
        try{adb.setMessage("Your "+cursor.getString(1)+" has code:\n"+cursor.getString(2).trim());}catch (Exception e){return "couldnt display message";}
        adb.setPositiveButton("Ok", null);
        adb.setNegativeButton("view", null);
        adb.show();
        return position+"|"+cursor.getString(2);
    }

    @Override
    public long getItemId(int position) {return 0;}

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_item, null);
        }
        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView textView = convertView.findViewById(R.id.text_view);

        //imageView.setImageResource(numberImages[position]);
        textView.setText(numberWord[position]);
        return convertView;
    }
}


