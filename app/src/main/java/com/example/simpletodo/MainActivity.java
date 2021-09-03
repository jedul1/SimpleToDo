package com.example.simpletodo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;



import java.io.File;
import org.apache.commons.io.FileUtils;
import java.nio.charset.Charset;
import android.util.Log;


import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button AddButton;
    EditText EditTextItem;
    RecyclerView ToDoItems;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AddButton = findViewById(R.id.AddButton);
        EditTextItem = findViewById(R.id.EditTextItem);
        ToDoItems = findViewById(R.id.ToDoItems);

        loadItems();

        items = new ArrayList<>();
        items.add("Read Bible");
        items.add("Go to Gym");
        items.add("Study COSC 338");
        items.add("Do CLCO 261 Homework");

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete item from model
                items.remove(position);
                //Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };
        final ItemsAdapter itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        ToDoItems.setAdapter(itemsAdapter);
        ToDoItems.setLayoutManager(new LinearLayoutManager(this));

        AddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = EditTextItem.getText().toString();
                //Add item to the model
                items.add(todoItem);
                //Notify the adapter that the item is inserted
                itemsAdapter.notifyItemInserted(items.size() - 1);
                EditTextItem.setText("");
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                saveItems();
            }

        });
    }
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }
    //This function will load items by reading every line of the data file
    private void loadItems(){
        try{
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("MainActivity", "Error reading item", e);
            items = new ArrayList<>();
        }
        }
    //This function saves items by writing them into the data file
    private void saveItems() {
        try{
            FileUtils.writeLines(getDataFile(), items);
        }  catch (IOException e){
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}