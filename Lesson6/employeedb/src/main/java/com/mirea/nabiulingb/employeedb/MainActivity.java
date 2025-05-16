package com.mirea.nabiulingb.employeedb;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HeroAdapter.OnHeroClickListener {
    private EditText etName, etSuperpower, etPowerLevel;
    private RecyclerView rvHeroes;
    private HeroDao heroDao;
    private HeroAdapter adapter;
    private Hero currentHero = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.getName);
        etSuperpower = findViewById(R.id.getSuperpower);
        etPowerLevel = findViewById(R.id.getPowerLevel);
        rvHeroes = findViewById(R.id.rvHeroes);
        Button btnAdd = findViewById(R.id.btnAdd);

        rvHeroes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HeroAdapter(this);
        rvHeroes.setAdapter(adapter);

        heroDao = AppDatabase.getInstance(this).heroDao();

        loadHeroes();

        btnAdd.setOnClickListener(v -> saveOrUpdateHero());
    }

    private void saveOrUpdateHero() {
        String name = etName.getText().toString();
        String superpower = etSuperpower.getText().toString();
        String powerLevelStr = etPowerLevel.getText().toString();

        if (name.isEmpty() || superpower.isEmpty() || powerLevelStr.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        int powerLevel = Integer.parseInt(powerLevelStr);

        if (currentHero == null) {
            Hero newHero = new Hero(name, superpower, powerLevel);
            new Thread(() -> {
                heroDao.insertHero(newHero);
                runOnUiThread(() -> {
                    Log.d("MainActivity", "Hero added: " + name);
                    loadHeroes();
                    clearFields();
                });
            }).start();
        } else {
            currentHero.name = name;
            currentHero.superpower = superpower;
            currentHero.powerLevel = powerLevel;
            new Thread(() -> {
                heroDao.updateHero(currentHero);
                runOnUiThread(() -> {
                    Log.d("MainActivity", "Hero updated: " + name);
                    currentHero = null;
                    loadHeroes();
                    clearFields();
                });
            }).start();
        }
    }

    private void loadHeroes() {
        new Thread(() -> {
            List<Hero> heroes = heroDao.getAllHeroes();
            runOnUiThread(() -> {
                adapter.setHeroes(heroes);
                Log.d("MainActivity", "Loaded " + heroes.size() + " heroes");
            });
        }).start();
    }

    private void clearFields() {
        etName.setText("");
        etSuperpower.setText("");
        etPowerLevel.setText("");
    }

    @Override
    public void onEditClick(Hero hero) {
        currentHero = hero;
        etName.setText(hero.name);
        etSuperpower.setText(hero.superpower);
        etPowerLevel.setText(String.valueOf(hero.powerLevel));
        Log.d("MainActivity", "Editing hero: " + hero.name);
    }

    @Override
    public void onDeleteClick(Hero hero) {
        new Thread(() -> {
            heroDao.deleteHero(hero);
            runOnUiThread(() -> {
                loadHeroes();
                Toast.makeText(this, "Герой удален", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", "Deleted hero: " + hero.name);
            });
        }).start();
    }
}