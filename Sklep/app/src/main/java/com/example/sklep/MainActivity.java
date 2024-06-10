package com.example.sklep;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String PREFS_NAME = "OrderPrefs";
    private static final String PREFS_SAVE_ENABLED = "SaveEnabled";
    private Spinner computerSpinner, mouseSpinner, keyboardSpinner, webcamSpinner;
    private CheckBox mouseCheckbox, keyboardCheckbox, webcamCheckbox;
    private Button orderButton;
    private SeekBar quantitySeekBar;
    private TextView selectedQuantityTextView, totalPriceTextView;
    private EditText nameEditText, emailEditText, phoneEditText;;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));

        computerSpinner = findViewById(R.id.computer_spinner);
        mouseSpinner = findViewById(R.id.mouse_spinner);
        keyboardSpinner = findViewById(R.id.keyboard_spinner);
        webcamSpinner = findViewById(R.id.webcam_spinner);
        mouseCheckbox = findViewById(R.id.mouse_checkbox);
        keyboardCheckbox = findViewById(R.id.keyboard_checkbox);
        webcamCheckbox = findViewById(R.id.webcam_checkbox);
        orderButton = findViewById(R.id.order_button);
        quantitySeekBar = findViewById(R.id.quantity_seekbar);
        selectedQuantityTextView = findViewById(R.id.selected_quantity_textview);
        totalPriceTextView = findViewById(R.id.price_label);
        nameEditText = findViewById(R.id.customer_name);
        emailEditText = findViewById(R.id.edittext_email);
        phoneEditText = findViewById(R.id.edittext_phone);

        dbHelper = new DatabaseHelper(this);

        checkAndRequestPermissions();

//        restoreOrderFromPreferences();

        Product[] computers = {
                new Product(R.drawable.komputer1, "Intel Core i5 12400, 16GB DDR4, SSD 250GB 3000zł", 3000),
                new Product(R.drawable.komputer2, "AMD Ryzen 5 5600X, 16GB DDR4, SSD 500GB 3500zł", 3500),
                new Product(R.drawable.komputer3, "Intel Core i7 12700, 32GB DDR4, SSD 1TB 5000zł", 5000)
        };

        Product[] mice = {
                new Product(R.drawable.logitech_g502_hero, "Mysz Logitech G502 HERO, cena 229zł", 229),
                new Product(R.drawable.steelseries_rival_5, "Mysz SteelSeries Rival 5, cena 239zł", 239),
                new Product(R.drawable.razer_basilisk_v3, "Mysz Razer Basilisk V3, cena 199zł", 199)
        };

        Product[] keyboards = {
                new Product(R.drawable.steelseries_apex_3, "Klawiatura SteelSeries Apex 3, cena 239zł", 239),
                new Product(R.drawable.logitech_g213_prodigy, "Klawiatura Logitech G213 PRODIGY, cena 169zł", 169),
                new Product(R.drawable.genesis_thor_401_rgb, "Klawiatura Genesis Thor 401 RGB, cena 229zł", 229)
        };

        Product[] webcams = {
                new Product(R.drawable.imilab_webcam_1080p_usb, "Kamera Imilab WebCam 1080P USB, cena 69zł", 69),
                new Product(R.drawable.creative_live_cam_sync_1080p_v2, "Kamera Creative Live! Cam Sync 1080p V2, cena 149zł", 149),
                new Product(R.drawable.thronmax_stream_go_x1_1080p, "Kamera Thronmax Stream GO X1 1080P, cena 219zł", 219)
        };

        setSpinnerAdapter(computerSpinner, computers);
        setSpinnerAdapter(mouseSpinner, mice);
        setSpinnerAdapter(keyboardSpinner, keyboards);
        setSpinnerAdapter(webcamSpinner, webcams);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNameValid()) {
                    saveOrderToDatabase();
                    showOrderSummary();
                    calculateTotalPrice();

                    resetOrderContent();
                }
            }
        });

        quantitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedQuantityTextView.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        setupListeners();
    }

    private void setupListeners() {
        computerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateTotalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mouseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateTotalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        keyboardSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateTotalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        webcamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculateTotalPrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        mouseCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calculateTotalPrice();
            }
        });

        keyboardCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calculateTotalPrice();
            }
        });

        webcamCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                calculateTotalPrice();
            }
        });

        quantitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                selectedQuantityTextView.setText(String.valueOf(progress));
                calculateTotalPrice();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Przyznano dostęp", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Odmówiono dostępu", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNameValid() {
        String name = nameEditText.getText().toString().trim();
        if (name.isEmpty()) {
            showAlertDialog();
            return false;
        }
        return true;
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Błędne dane")
                .setMessage("Proszę o wprowadzenie imenia i nazwiska lub nazwę firmy.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void saveOrderToDatabase() {
        Product selectedComputer = (Product) computerSpinner.getSelectedItem();
        Product selectedMouse = mouseCheckbox.isChecked() ? (Product) mouseSpinner.getSelectedItem() : null;
        Product selectedKeyboard = keyboardCheckbox.isChecked() ? (Product) keyboardSpinner.getSelectedItem() : null;
        Product selectedWebcam = webcamCheckbox.isChecked() ? (Product) webcamSpinner.getSelectedItem() : null;

        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        int quantity = quantitySeekBar.getProgress();
        int totalPrice = calculateTotalPriceValue();

        email = email.isEmpty() ? null : email;
        phone = phone.isEmpty() ? null : phone;
        String computerName = selectedComputer.getName();
        String mouseName = selectedMouse != null ? selectedMouse.getName() : null;
        String keyboardName = selectedKeyboard != null ? selectedKeyboard.getName() : null;
        String webcamName = selectedWebcam != null ? selectedWebcam.getName() : null;

        String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

        dbHelper.insertOrder(computerName, mouseName, keyboardName, webcamName, name, email, phone, quantity, currentDateTime, totalPrice);

        if (email != null) {
            sendOrderEmail(email, name, computerName, mouseName, keyboardName, webcamName, quantity, totalPrice, currentDateTime);
        }
        if (phone != null) {
            sendOrderSMS(phone, name, computerName, mouseName, keyboardName, webcamName, quantity, totalPrice, currentDateTime);
        }
    }

    private void sendOrderEmail(String email, String name, String computerName, String mouseName, String keyboardName, String webcamName, int quantity, int totalPrice, String dateTime) {
        StringBuilder emailBody = new StringBuilder();
        emailBody.append("Szczegóły zamówienia:\n");
        emailBody.append("Imię: ").append(name).append("\n");
        emailBody.append("Komputer: ").append(computerName).append("\n");

        if (mouseName != null) {
            emailBody.append("Mysz: ").append(mouseName).append("\n");
        }

        if (keyboardName != null) {
            emailBody.append("Klawiatura: ").append(keyboardName).append("\n");
        }

        if (webcamName != null) {
            emailBody.append("Kamera: ").append(webcamName).append("\n");
        }

        emailBody.append("Ilość: ").append(quantity).append("\n");
        emailBody.append("Suma: ").append(totalPrice).append("zł\n");
        emailBody.append("Data i czas zamówienia: ").append(dateTime).append("\n");

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Potwierdzenie zamówienia");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody.toString());

        try {
            startActivity(Intent.createChooser(emailIntent, "Wyślij email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "Brak klienta e-mail zainstalowanego na urządzeniu.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendOrderSMS(String phone, String name, String computerName, String mouseName, String keyboardName, String webcamName, int quantity, int totalPrice, String dateTime) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            StringBuilder smsBody = new StringBuilder();
            smsBody.append("Szczegóły zamówienia:\n");
            smsBody.append("Imię: ").append(name).append("\n");
            smsBody.append("Komputer: ").append(computerName).append("\n");

            if (mouseName != null) {
                smsBody.append("Mysz: ").append(mouseName).append("\n");
            }

            if (keyboardName != null) {
                smsBody.append("Klawiatura: ").append(keyboardName).append("\n");
            }

            if (webcamName != null) {
                smsBody.append("Kamera: ").append(webcamName).append("\n");
            }

            smsBody.append("Ilość: ").append(quantity).append("\n");
            smsBody.append("Suma: ").append(totalPrice).append("zł\n");
            smsBody.append("Data i czas zamówienia: ").append(dateTime).append("\n");

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, smsBody.toString(), null, null);
        } else {
            Toast.makeText(this, "Nie przyznano uprawnień do wysyłanie SMSów", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetOrderContent() {
        nameEditText.getText().clear();
        emailEditText.getText().clear();
        phoneEditText.getText().clear();
        quantitySeekBar.setProgress(0);
        computerSpinner.setSelection(0);
        mouseSpinner.setSelection(0);
        keyboardSpinner.setSelection(0);
        webcamSpinner.setSelection(0);
        mouseCheckbox.setChecked(false);
        keyboardCheckbox.setChecked(false);
        webcamCheckbox.setChecked(false);
        totalPriceTextView.setText("Cena: 3000zł");
    }

    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void showLanguageSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.wybierz_jezyk)
                .setItems(R.array.language_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            setLocale("pl");
                        } else if (which == 1) {
                            setLocale("en");
                        }
                    }
                });
        builder.create().show();
    }

    private void showOrderSummary() {
        Product selectedComputer = (Product) computerSpinner.getSelectedItem();
        StringBuilder orderSummary = new StringBuilder("Zamówienie:\n");
        orderSummary.append("Komputer: ").append(selectedComputer.getName()).append("\n");

        if (mouseCheckbox.isChecked()) {
            Product selectedMouse = (Product) mouseSpinner.getSelectedItem();
            orderSummary.append("Mysz: ").append(selectedMouse.getName()).append("\n");
        }

        if (keyboardCheckbox.isChecked()) {
            Product selectedKeyboard = (Product) keyboardSpinner.getSelectedItem();
            orderSummary.append("Klawiatura: ").append(selectedKeyboard.getName()).append("\n");
        }

        if (webcamCheckbox.isChecked()) {
            Product selectedWebcam = (Product) webcamSpinner.getSelectedItem();
            orderSummary.append("Kamera: ").append(selectedWebcam.getName()).append("\n");
        }

        orderSummary.append("Ilość: ").append(quantitySeekBar.getProgress()).append("\n");
        orderSummary.append("Cena całkowita: ").append(totalPriceTextView.getText().toString());

        new AlertDialog.Builder(this)
                .setTitle("Podsumowanie zamówienia")
                .setMessage(orderSummary.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private int calculateTotalPriceValue() {
        int totalPrice = 0;

        Product selectedComputer = (Product) computerSpinner.getSelectedItem();
        totalPrice += selectedComputer.getPrice();

        if (mouseCheckbox.isChecked()) {
            Product selectedMouse = (Product) mouseSpinner.getSelectedItem();
            totalPrice += selectedMouse.getPrice();
        }

        if (keyboardCheckbox.isChecked()) {
            Product selectedKeyboard = (Product) keyboardSpinner.getSelectedItem();
            totalPrice += selectedKeyboard.getPrice();
        }

        if (webcamCheckbox.isChecked()) {
            Product selectedWebcam = (Product) webcamSpinner.getSelectedItem();
            totalPrice += selectedWebcam.getPrice();
        }

        int quantity = quantitySeekBar.getProgress();
        totalPrice *= quantity;

        return totalPrice;
    }

    private void calculateTotalPrice() {
        int totalPrice = calculateTotalPriceValue();
        totalPriceTextView.setText("Cena: " + totalPrice + "zł");
    }

    private void setSpinnerAdapter(Spinner spinner, Product[] products) {
        ArrayAdapter<Product> adapter = new ArrayAdapter<Product>(this, R.layout.spinner_item, products) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return createViewFromResource(position, convertView, parent, R.layout.spinner_item);
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                return createViewFromResource(position, convertView, parent, R.layout.spinner_item);
            }

            private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(resource, parent, false);
                }
                ImageView imageView = convertView.findViewById(R.id.spinner_item_image);
                TextView textView = convertView.findViewById(R.id.spinner_item_text);

                Product item = getItem(position);

                if (item != null) {
                    imageView.setImageResource(item.getImageResId());
                    textView.setText(item.getName());
                }

                return convertView;
            }
        };
        spinner.setAdapter(adapter);
    }

    private void saveOrderToPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("computerSpinner", computerSpinner.getSelectedItemPosition());
        editor.putInt("mouseSpinner", mouseSpinner.getSelectedItemPosition());
        editor.putInt("keyboardSpinner", keyboardSpinner.getSelectedItemPosition());
        editor.putInt("webcamSpinner", webcamSpinner.getSelectedItemPosition());
        editor.putBoolean("mouseCheckbox", mouseCheckbox.isChecked());
        editor.putBoolean("keyboardCheckbox", keyboardCheckbox.isChecked());
        editor.putBoolean("webcamCheckbox", webcamCheckbox.isChecked());
        editor.putInt("quantitySeekBar", quantitySeekBar.getProgress());
        editor.putString("nameEditText", nameEditText.getText().toString());
        editor.putString("emailEditText", emailEditText.getText().toString());
        editor.putString("phoneEditText", phoneEditText.getText().toString());
        editor.putString("totalPriceTextView", totalPriceTextView.getText().toString());

        editor.apply();
    }

    private void restoreOrderFromPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        boolean saveEnabled = sharedPreferences.getBoolean(PREFS_SAVE_ENABLED, true);
        if (saveEnabled) {
            computerSpinner.setSelection(sharedPreferences.getInt("computerSpinner", 0));
            mouseSpinner.setSelection(sharedPreferences.getInt("mouseSpinner", 0));
            keyboardSpinner.setSelection(sharedPreferences.getInt("keyboardSpinner", 0));
            webcamSpinner.setSelection(sharedPreferences.getInt("webcamSpinner", 0));
            mouseCheckbox.setChecked(sharedPreferences.getBoolean("mouseCheckbox", false));
            keyboardCheckbox.setChecked(sharedPreferences.getBoolean("keyboardCheckbox", false));
            webcamCheckbox.setChecked(sharedPreferences.getBoolean("webcamCheckbox", false));
            quantitySeekBar.setProgress(sharedPreferences.getInt("quantitySeekBar", 1));
            nameEditText.setText(sharedPreferences.getString("nameEditText", ""));
            emailEditText.setText(sharedPreferences.getString("emailEditText", ""));
            phoneEditText.setText(sharedPreferences.getString("phoneEditText", ""));
            totalPriceTextView.setText(sharedPreferences.getString("totalPriceTextView", "Cena całkowita: 0zł"));

            calculateTotalPrice();
        }
    }

    private void toggleSaveSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean saveEnabled = sharedPreferences.getBoolean(PREFS_SAVE_ENABLED, true);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREFS_SAVE_ENABLED, !saveEnabled);
        editor.apply();
        
        String message = !saveEnabled ? "Zapis ustawień włączony" : "Zapis ustawień wyłączony";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreOrderFromPreferences();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveOrderToPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_orders:
                Intent intent = new Intent(this, OrderListActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_language:
                showLanguageSelectionDialog();
                return true;
            case R.id.menu_save_settings:
                toggleSaveSettings();
                return true;
            case R.id.menu_about:
                Intent intent2 = new Intent(this, AboutProgramActivity.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
