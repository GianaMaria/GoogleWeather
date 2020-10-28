package com.example.googleauthfirebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.googleauthfirebase.fragments.DialogBuilderFragment;
import com.example.googleauthfirebase.fragments.HistoryOfRequestsByCityFragment;
import com.example.googleauthfirebase.fragments.MainFragment;
import com.example.googleauthfirebase.fragments.OtherCitiesFragment;
import com.example.googleauthfirebase.fragments.SettingsFragment;
import com.example.googleauthfirebase.model.City;
import com.example.googleauthfirebase.notif.MessageReceiver;
import com.example.googleauthfirebase.notif.NetworkReceiver;
import com.example.googleauthfirebase.util.CircleTransformation;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private MessageReceiver messageReceiver;
    private NetworkReceiver networkReceiver;

    ImageView imageView;

    private DialogBuilderFragment dialogBuilderFragment;
    private OtherCitiesFragment otherCitiesFragment = new OtherCitiesFragment();

    private ArrayList<City> cities = new ArrayList<City>();

    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "GoogleAuth";

    private GoogleSignInClient googleSignInClient;

    private com.google.android.gms.common.SignInButton buttonSignIn;
    private MaterialButton buttonSingOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mTitle = mDrawerTitle = getTitle();

        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                toolbar, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            //Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            //Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            fragmentTransaction(new MainFragment());
        }

        dialogBuilderFragment = new DialogBuilderFragment();

        imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);

        Picasso.with(this)
                .load("https://klike.net/uploads/posts/2019-03/1551511784_4.jpg")
                .transform(new CircleTransformation())
                .placeholder(R.drawable.ic_action_name_account)
                .error(R.drawable.ic_action_name_error)
                .into(imageView);

        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver, new IntentFilter(Intent.ACTION_BATTERY_LOW));

        networkReceiver = new NetworkReceiver();
        registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        initNotificationChannel();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        buttonSignIn = findViewById(R.id.sign_in_button);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                signIn();
                                            }
                                        }
        );


        buttonSingOut = findViewById(R.id.sing_out_button);
        buttonSingOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

    }

    // инициализация канала нотификаций
    private void initNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel("2", "name", importance);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);

        MenuItem search = menu.findItem(R.id.action_search);

        final SearchView searchText = (SearchView) search.getActionView();

        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Snackbar.make(searchText, s, Snackbar.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_search).setVisible(!mDrawerLayout.isDrawerOpen(GravityCompat.START));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Fragment fragment;

        switch (id) {
            case R.id.nav_home:
                fragment = new MainFragment();
                mTitle = getString(R.string.app_name);
                break;
            case R.id.nav_cities:
                fragment = new OtherCitiesFragment();
                mTitle = getString(R.string.cities_nav);
                break;
            case R.id.nav_settings:
                fragment = new SettingsFragment();
                mTitle = getString(R.string.action_settings);
                break;
            case R.id.nav_history:
                fragment = new HistoryOfRequestsByCityFragment();
                mTitle = getString(R.string.history_nav);
                break;
//            case R.id.nav_send:
//                fragment = new SendFragment();
//                mTitle  = getString(R.string.send);
//                break;
            default:
                fragment = new MainFragment();
                mTitle = getString(R.string.app_name);
                break;
        }

        fragmentTransaction(fragment);
        return true;
    }

    private void fragmentTransaction(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state     after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    // Вызов диалога с билдером
    public void onClickDialogBuilder(View view) {
        dialogBuilderFragment.show(getSupportFragmentManager(), "dialogBuilder");

    }

    // Метод для общения с диалоговыми окнами
    public void onDialogResult(String resultDialog) {
        fragmentTransaction(otherCitiesFragment);
        Toast.makeText(this, "Выбрано " + resultDialog, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<City> getArrayCities() {
        return cities;
    }

    public void addCityArray(String city, long date) {
        cities.add(new City(city, date));
    }


    @Override
    protected void onStart() {
        super.onStart();
        enableSign();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {

            disableSign();
            updateUI(account.getEmail());
        }
    }

    // Получаем результаты аутентификации от окна регистрации пользователя
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // Инициация регистрации пользователя
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Выход из учётной записи в приложении
    private void signOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI("email");
                        enableSign();
                    }
                });
    }

    //https://developers.google.com/identity/sign-in/android/backend-auth?authuser=1
    // Получение данных пользователя
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            disableSign();
            updateUI(account.getEmail());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e);
        }
    }

    // Обновляем данные о пользователе на экране
    private void updateUI(String idToken) {
        TextView token = findViewById(R.id.token);
        token.setText(idToken);
    }

    private void enableSign() {
        buttonSignIn.setEnabled(true);
        buttonSingOut.setEnabled(false);
    }

    private void disableSign() {
        buttonSignIn.setEnabled(false);
        buttonSingOut.setEnabled(true);
    }

}