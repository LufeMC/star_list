package com.example.starlist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Locale;

public class ConfiguracoesFragment extends Fragment {

    private int versionCode;
    private String versionName;
    private TextView version_name;
    private TextView account_type;
    private TextView account_desc;
    private TextView language_text;
    private TableRow privacy;
    private AlertDialog.Builder alertDialogBuilder;
    private Context context;
    private boolean initialBought;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = getContext();
        View view = inflater.inflate(R.layout.fragment_configuracoes, container, false);

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        initialBought = sharedPreferences.getBoolean("bought", false);


        versionCode = BuildConfig.VERSION_CODE;
        versionName = BuildConfig.VERSION_NAME;

        version_name = (TextView) view.findViewById(R.id.version_number);
        account_type = (TextView) view.findViewById(R.id.account_type);
        account_desc = (TextView) view.findViewById(R.id.account_description);
        if (!initialBought) {
            account_type.setText(getResources().getString(R.string.basic_account));
            account_desc.setText(getResources().getString(R.string.basic_desc));
        } else {
            account_type.setText(getResources().getString(R.string.premium_account));
            account_desc.setText(getResources().getString(R.string.premium_desc));
        }
        language_text = (TextView) view.findViewById(R.id.language_text);

        version_name.setText(versionName);

        privacy = (TableRow) view.findViewById(R.id.language);
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLanguageDialog();
            }
        });

        return view;
    }

    private void showChangeLanguageDialog() {
        final String[] languages = {"Portugues", "English"};

        alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle(getResources().getString(R.string.choose_language));
        alertDialogBuilder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    setLocale("pt", "BR", context);
                    getActivity().recreate();
                } else if (i == 1) {
                    setLocale("en", "US", context);
                    getActivity().recreate();
                }

                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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

        SharedPreferences preferences = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("My_Lang", lang);
        editor.putString("My_Country", country);
        editor.apply();
    }


}