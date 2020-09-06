package com.example.starlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Util.Constants;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView title_name;
    private ImageView menu_icon;
    private ImageView back_icon;
    private Menu menu;
    private NavController navController;
    private AdView adViewMain;
    private int s = 0;
    private boolean initialBought = false;
    private BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUI();
        recreateLang(menu);
        startAdView();
        content();
        total_refresh();
    }

    public void content() {
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.main_menu);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int id = destination.getId();
                switch (id) {
                    case R.id.listasID:
                        destination.setLabel(getResources().getString(R.string.listas));
                        break;
                    case R.id.premiumID:
                        destination.setLabel(getResources().getString(R.string.seja_premium));
                        break;
                    case R.id.shareID:
                        destination.setLabel(getResources().getString(R.string.share));
                        break;
                    //case R.id.feedbackID:
                    //destination.setLabel(getResources().getString(R.string.nos_avalie));
                    //break;
                    case R.id.settingsID:
                        destination.setLabel(getResources().getString(R.string.configuracoes));
                        break;

                }

                title_name.setText(destination.getLabel());

                if (!(destination.getId() == R.id.listasID)) {
                    menu_icon.setVisibility(View.GONE);
                    back_icon.setVisibility(View.VISIBLE);
                } else {
                    menu_icon.setVisibility(View.VISIBLE);
                    back_icon.setVisibility(View.GONE);
                }

                if (destination.getId() == R.id.shareID) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    String sharebody = getResources().getString(R.string.share_body);
                    String subject = getResources().getString(R.string.share_subject);
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, sharebody);
                    startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share)));

                    navController.navigate(R.id.listasID);
                }
            }
        });
    }

    public void setUpUI() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        bp = new BillingProcessor(this, null, this);
        bp.initialize();

        navController = Navigation.findNavController(this, R.id.navHostFragement);
        NavigationUI.setupWithNavController(navigationView, navController);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        title_name = (TextView) findViewById(R.id.title_name);
        menu_icon = (ImageView) findViewById(R.id.menu_icon);
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
                bp.loadOwnedPurchasesFromGoogle();
                bp.listOwnedProducts();

                for (String s : bp.listOwnedProducts()) {
                    Log.d("PRODUCT_ID", s);
                }
            }
        });

        back_icon = (ImageView) findViewById(R.id.back_icon);
        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.listasID);
            }
        });

        menu = navigationView.getMenu();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        initialBought = sharedPreferences.getBoolean("bought", false);
        Log.d("ITISBOUGHT", String.valueOf(initialBought));

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else if (!(navController.getCurrentDestination().getId() == R.id.listasID)) {
            navController.navigate(R.id.listasID);
        } else {
            super.onBackPressed();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        recreateLang(menu);
        startAdView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreateLang(menu);
        startAdView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recreateLang(menu);
        startAdView();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (bp != null) {
            bp.release();
        }

        super.onDestroy();
    }

    public void setLocale(String lang, String country, Context context) {
        Locale locale = new Locale(lang, country);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        SharedPreferences preferences = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("My_Lang", lang);
        editor.putString("My_Country", country);
        editor.apply();
    }

    private void loadLocale(Context context) {
        SharedPreferences preferences = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        String country = preferences.getString("My_Country", "");
        setLocale(language, country, context);
    }

    private void startAdView() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        if (initialBought) {
            adViewMain.setVisibility(View.GONE);
        } else {
            try {
                adViewMain = (AdView) findViewById(R.id.adViewMain);
                AdRequest adRequest = new AdRequest.Builder().build();
                adViewMain.loadAd(adRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void recreateLang(Menu menu) {
        SharedPreferences preferences = this.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        String country = preferences.getString("My_Country", "");
        Log.d("ADD_LIST_TEXT", language);
        Log.d("ADD_LIST_TEXT", getResources().getString(R.string.addListText));

        if (!(language.equals(""))) {
            loadLocale(this);

            for (int i = 0; i < menu.size(); i++) {
                MenuItem menuItem = menu.getItem(i);
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.listasID:
                        menuItem.setTitle(getResources().getString(R.string.listas));
                        break;
                    case R.id.premiumID:
                        menuItem.setTitle(getResources().getString(R.string.seja_premium));
                        break;
                    case R.id.shareID:
                        menuItem.setTitle(getResources().getString(R.string.compartilhar_app));
                        break;
                    case R.id.feedbackID:
                        menuItem.setTitle(getResources().getString(R.string.nos_avalie));
                        break;
                    case R.id.settingsID:
                        menuItem.setTitle(getResources().getString(R.string.configuracoes));
                        break;
                }

                total_refresh();
            }
        }
    }

    final Handler handler = new Handler();

    Runnable my_runnable = new Runnable() {
        @Override
        public void run() {
            content();
            onPurchaseHistoryRestored();
            recreateLang(menu);
            setUpUI();
        }
    };

    public void refresh(int milliseconds) {
        handler.postDelayed(my_runnable, milliseconds);
    }

    public void stop_refresh() {
        handler.removeCallbacks(my_runnable);
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
    }

    @Override
    public void onPurchaseHistoryRestored() {
        bp.loadOwnedPurchasesFromGoogle();
        if (!bp.listOwnedProducts().isEmpty()) {
            Log.d("OLAAAA", String.valueOf(initialBought));
            for (String sku: bp.listOwnedProducts()) {
                Log.d("PRODUCTIDLOG", sku);
                if (sku.equals(Constants.SKU)) {
                    SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("bought", true);
                    editor.commit();
                }
            }
        }
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void total_refresh() {
        s++;

        refresh(100);

        if (s > 4) {
            stop_refresh();
            s = 0;
        }
    }
}