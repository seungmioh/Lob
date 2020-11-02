package com.example.lob.Activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.example.lob.R;
import com.example.lob.Service.Storage;
import com.example.lob.UI.board.BoardUpdateFragment;
import com.example.lob.UI.basket.BasketFragment;
import com.example.lob.UI.board.BoardFragment;
import com.example.lob.UI.board.BoardWriteFragment;
import com.example.lob.UI.calendar.CalendarFragment;
import com.example.lob.UI.consumption.ConsumptionFragment;
import com.example.lob.UI.cooking.CookingFragment;
import com.example.lob.UI.diet.DietFragment;
import com.example.lob.UI.favorite.FavoriteFragment;
import com.example.lob.UI.home.HomeFragment;
import com.example.lob.UI.refrigerator.RefrigeratorFragment;
import com.example.lob.UI.setting.SettingFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.content.CursorLoader;


public class UserActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Uri userProfile = null;
    private FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeFragment homeFragment = new HomeFragment();
    private DietFragment dietFragment = new DietFragment();

    private SettingFragment settingFragment = new SettingFragment();
    private BoardFragment boardFragment = new BoardFragment();
    private ConsumptionFragment consumptionFragment = new ConsumptionFragment();
    private RefrigeratorFragment refrigeratorFragment = new RefrigeratorFragment();
    private BasketFragment basketFragment = new BasketFragment();
    private CalendarFragment calendarFragment = new CalendarFragment();
    private CookingFragment cookingFragment = new CookingFragment();
    private FavoriteFragment favoriteFragment = new FavoriteFragment();
    private BoardWriteFragment boardWriteFragment = new BoardWriteFragment();
    private BoardUpdateFragment boardUpdateFragment = new BoardUpdateFragment();

    private StorageReference storageReference;
    private static final int PICK_FROM_ALBUM=1;
    private FirebaseAuth  googleAuth = FirebaseAuth.getInstance();
    public static Context CONTEXT;
    private  FirebaseUser  currentUser = googleAuth.getCurrentUser();
    Toolbar toolbar;
    Fragment fragment;
    private DrawerLayout mDrawerLayout;
    private Context context = this;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        super.onCreate(savedInstanceState);


        CONTEXT = this;
        setContentView(R.layout.user);
        onStart();
        toolbar = (Toolbar) findViewById(R.id.toolbar); //툴바설정
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.closeDrawers();



        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //transaction.add(R.id.fragment_container, homeFragment.newInstance()).commit();
        transaction.replace(R.id.fragment_container, homeFragment).commitAllowingStateLoss();
        NavigationView toolbarNavigationView = findViewById(R.id.navView);
        if (toolbarNavigationView != null) {
            toolbarNavigationView.setNavigationItemSelectedListener(this);
        }
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId()) {

                    case R.id.menu_home:
                        Log.e("asdasdasd", (String) item.getTitle());
                        mDrawerLayout.closeDrawers();
                        transaction.replace(R.id.fragment_container, homeFragment).commitAllowingStateLoss();
                        break;
                    case R.id.menu_consumption:
                        mDrawerLayout.closeDrawers();
                        Log.e("asdasdasd", (String) item.getTitle());

                        transaction.replace(R.id.fragment_container, consumptionFragment).commitAllowingStateLoss();
                        break;
                    case R.id.menu_diet:
                        mDrawerLayout.closeDrawers();
                        Log.e("asdasdasd", (String) item.getTitle());

                        transaction.replace(R.id.fragment_container, dietFragment).commitAllowingStateLoss();
                        break;
                    case R.id.menu_board:
                        mDrawerLayout.closeDrawers();
                        Log.e("asdasdasd", (String) item.getTitle());

                        transaction.replace(R.id.fragment_container, boardFragment).commitAllowingStateLoss();
                        break;
                    case R.id.menu_setting:
                        mDrawerLayout.closeDrawers();
                        Log.e("asdasdasd", (String) item.getTitle());

                        transaction.replace(R.id.fragment_container, settingFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });

    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, boardFragment).commit();      // Fragment로 사용할 MainActivity내의 layout공간을 선택합니다.
    }

    public void writeClick(View view) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if(view.getId() == R.id.board_Button_Write){
            transaction.replace(R.id.fragment_container,boardWriteFragment).commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);

                ImageView userImage = mDrawerLayout.findViewById(R.id.userProfile);
              TextView userEmail = mDrawerLayout.findViewById(R.id.userEmail);
              userEmail.setText(currentUser.getEmail().substring(0,currentUser.getEmail().lastIndexOf("@"))+"님");
              if(userProfile !=null){
            final   Intent profileSetting = new Intent(this, ProfileSetting.class);
                  Glide.with(UserActivity.this)
                          .load(userProfile)
                          .apply(RequestOptions.circleCropTransform())
                          .into(userImage);
                  userImage.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          startActivity(profileSetting);
                      }
                  });
              }
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public String getPath(Uri uri ){
        String [] proj ={MediaStore.Images.Media.DATA};
        Cursor cursor =null;
        int index;
        String name = null;
        CursorLoader cursorLoader= new CursorLoader(this.getApplicationContext(),uri,proj,null,null,null);
        if(cursorLoader!=null) {
            cursor = cursorLoader.loadInBackground();
            index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            name=cursor.getString(index);
            cursor.close();
        }
        return name;
    }
    @Override
    public void onStart() {
        super.onStart();
        if(currentUser!=null){
            storageReference=firebaseStorage.getReference().child("/profile/"+currentUser.getUid());
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        userProfile=task.getResult();
                    } else userProfile= null;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Storage storage = new Storage();
                    Uri drawablePath = getURLForResource(R.drawable.normal_profile);
                    storage.UploadProfile(drawablePath,currentUser.getUid());
                    onResume();
                }
            });

        }
    }
    @Override
    public synchronized  void onResume() {
        super.onResume();
        final  ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("ProgressDialog running...");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
        progressDialog.show();
        updateProfile();
        progressDialog.dismiss();

    }

    public  void updateProfile(){
        if(currentUser!=null){
            storageReference=firebaseStorage.getReference().child("/profile/"+currentUser.getUid());
            storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                            userProfile=task.getResult();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Storage storage = new Storage();
                    Uri drawablePath = getURLForResource(R.drawable.normal_profile);
                    storage.UploadProfile(drawablePath,currentUser.getUid());
                    onResume();
                }
            });

        }
    }
    private Uri getURLForResource(int resId) {
        Resources resources = CONTEXT.getResources();
    return     Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (item.getItemId()){
            case R.id.nav_home:
                mDrawerLayout.closeDrawers();
                transaction.replace(R.id.fragment_container, homeFragment).commitAllowingStateLoss();
                break;
            case R.id.nav_refrigerator:
                mDrawerLayout.closeDrawers();
                transaction.replace(R.id.fragment_container, refrigeratorFragment).commitAllowingStateLoss();
                break;
            case R.id.nav_basket:
                mDrawerLayout.closeDrawers();
                transaction.replace(R.id.fragment_container, basketFragment).commitAllowingStateLoss();
                break;
            case R.id.nav_calendar:
                mDrawerLayout.closeDrawers();
                transaction.replace(R.id.fragment_container, calendarFragment).commitAllowingStateLoss();
                break;
            case R.id.nav_cooking:
                mDrawerLayout.closeDrawers();
                transaction.replace(R.id.fragment_container, cookingFragment).commitAllowingStateLoss();
                break;
            case R.id.nav_favorite:
                mDrawerLayout.closeDrawers();
                transaction.replace(R.id.fragment_container, favoriteFragment).commitAllowingStateLoss();
                break;
        }
        return false;
    }


}

