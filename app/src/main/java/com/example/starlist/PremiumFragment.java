package com.example.starlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

import Util.Constants;

public class PremiumFragment extends Fragment implements BillingProcessor.IBillingHandler {

    private Button buy_button;
    private View view;
    private Context context;
    private BillingProcessor bp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_premium, container, false);

        setUpUI();

        return view;
    }

    private void setUpUI() {
        context = getContext();
        bp = new BillingProcessor(getActivity(), Constants.base64EncodedPublicKey, this);
        bp.initialize();
        buy_button = (Button) view.findViewById(R.id.buy_buttom);
        buy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.purchase(getActivity(), Constants.SKU);
            }
        });
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        buy_button.setText(getResources().getString(R.string.already_bought));
        buy_button.setEnabled(false);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("bought", true);
        editor.commit();
    }

    @Override
    public void onPurchaseHistoryRestored() {
        buy_button.setText(getResources().getString(R.string.already_bought));
        buy_button.setEnabled(false);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("bought", true);
        editor.commit();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        Log.d("ERROR ON BILLING", "AN ERROR OCCURRED");
    }

    @Override
    public void onBillingInitialized() {
        Log.d("INITIATING BILLING", "INITIATING...");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }

        super.onDestroy();
    }
}