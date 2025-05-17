package com.example.arithmeticgame;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopActivity extends AppCompatActivity {

    private ListView shopItemsListView;
    private Button backButton;
    private TextView pointsTextView;

    private int playerPoints;
    private SharedPreferences prefs;

    // Shop items
    private List<ShopItem> shopItems;

    public static class ShopItem {
        private String id;
        private String name;
        private String description;
        private int price;
        private int iconResId;
        private int quantity;

        public ShopItem(String id, String name, String description, int price, int iconResId) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.price = price;
            this.iconResId = iconResId;
            this.quantity = 0;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public int getPrice() {
            return price;
        }

        public int getIconResId() {
            return iconResId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void incrementQuantity() {
            this.quantity++;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Initialize UI components
        shopItemsListView = findViewById(R.id.shopItemsListView);
        backButton = findViewById(R.id.backButton);
        pointsTextView = findViewById(R.id.pointsTextView);

        // Initialize shared preferences
        prefs = getSharedPreferences("MНастройки математической игры", MODE_PRIVATE);

        // Load player points
        playerPoints = prefs.getInt("PLAYER_POINTS", 0);

        // Initialize shop items
        initializeShopItems();

        // Load purchased items
        loadPurchasedItems();

        // Update points display
        updatePointsDisplay();

        // Set up back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Display shop items
        displayShopItems();
    }

    private void initializeShopItems() {
        shopItems = new ArrayList<>();

        // Add shop items
        shopItems.add(new ShopItem(
                "extra_time",
                "Extra Time Power-up",
                "Adds 10 seconds to the timer",
                50,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        shopItems.add(new ShopItem(
                "hint",
                "Hint Power-up",
                "Shows the first digit of the answer",
                75,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        shopItems.add(new ShopItem(
                "skip",
                "Skip Problem Power-up",
                "Skips the current problem without penalty",
                100,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        shopItems.add(new ShopItem(
                "double_points",
                "Double Points Power-up",
                "Doubles points for 30 seconds",
                150,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));

        shopItems.add(new ShopItem(
                "health_boost",
                "Health Boost",
                "Increases mob health by 25%",
                200,
                R.drawable.ic_launcher_foreground // Replace with actual icon
        ));
    }

    private void loadPurchasedItems() {
        for (ShopItem item : shopItems) {
            int quantity = prefs.getInt("SHOP_ITEM_" + item.getId(), 0);
            item.setQuantity(quantity);
        }
    }

    private void savePurchasedItems() {
        SharedPreferences.Editor editor = prefs.edit();
        for (ShopItem item : shopItems) {
            editor.putInt("SHOP_ITEM_" + item.getId(), item.getQuantity());
        }
        editor.apply();
    }

    private void updatePointsDisplay() {
        pointsTextView.setText("Баллы: " + playerPoints);
    }

    private void displayShopItems() {
        // Prepare data for adapter
        List<Map<String, Object>> data = new ArrayList<>();
        for (final ShopItem item : shopItems) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("name", item.getName());
            itemMap.put("description", item.getDescription());
            itemMap.put("price", "Price: " + item.getPrice() + " points");
            itemMap.put("quantity", "Owned: " + item.getQuantity());
            itemMap.put("icon", item.getIconResId());
            itemMap.put("buyAction", "Buy");
            data.add(itemMap);
        }

        // Create adapter
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.shop_item,
                new String[]{"name", "description", "price", "quantity", "icon", "buyAction"},
                new int[]{R.id.shopItemName, R.id.shopItemDescription, R.id.shopItemPrice,
                        R.id.shopItemQuantity, R.id.shopItemIcon, R.id.buyButton}
        );

        // Set adapter
        shopItemsListView.setAdapter(adapter);

        // Set up buy buttons
        for (int i = 0; i < shopItemsListView.getCount(); i++) {
            final int position = i;
            View itemView = shopItemsListView.getChildAt(position);
            if (itemView != null) {
                Button buyButton = itemView.findViewById(R.id.buyButton);
                buyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        purchaseItem(position);
                    }
                });
            }
        }
    }

    private void purchaseItem(int position) {
        if (position < 0 || position >= shopItems.size()) {
            return;
        }

        ShopItem item = shopItems.get(position);

        // Check if player has enough points
        if (playerPoints >= item.getPrice()) {
            // Deduct points
            playerPoints -= item.getPrice();

            // Increment item quantity
            item.incrementQuantity();

            // Save changes
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("PLAYER_POINTS", playerPoints);
            editor.apply();

            savePurchasedItems();

            // Update UI
            updatePointsDisplay();
            displayShopItems();

            Toast.makeText(this, "Приобретенный " + item.getName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Недостаточно очков!", Toast.LENGTH_SHORT).show();
        }
    }
}
