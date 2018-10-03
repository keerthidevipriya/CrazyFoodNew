package com.crazy.food.app.crazyfood;

import java.util.List;

/**
 * Created by DELL on 24-06-2018.
 */

class MyUserFood {
    private List<MyUserMeal> meals = null;

    public List<MyUserMeal> getMeals() {
        return meals;
    }

    public void setMeals(List<MyUserMeal> meals) {
        this.meals = meals;
    }
}
