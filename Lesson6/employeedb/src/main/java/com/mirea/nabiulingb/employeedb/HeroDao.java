package com.mirea.nabiulingb.employeedb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface HeroDao {
    @Query("SELECT * FROM heroes")
    List<Hero> getAllHeroes();

    @Insert
    void insertHero(Hero hero);

    @Update
    void updateHero(Hero hero);

    @Delete
    void deleteHero(Hero hero);

    @Query("SELECT * FROM heroes WHERE id = :id")
    Hero getHeroById(int id);
}