package com.crazy.food.app.crazyfood;


public class MyUserMeal {
    private String strMeal;
    private String strMealThumb;
    private String idMeal;

    public MyUserMeal(String strMeal, String strMealThumb, String idMeal) {
        this.strMeal=strMeal;
        this.strMealThumb=strMealThumb;
        this.idMeal=idMeal;
    }

    public String getStrMeal() {
        return strMeal;
    }

    public void setStrMeal(String strMeal) {
        this.strMeal = strMeal;
    }

    public String getStrMealThumb() {
        return strMealThumb;
    }

    public void setStrMealThumb(String strMealThumb) {
        this.strMealThumb = strMealThumb;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public void setIdMeal(String idMeal) {
        this.idMeal = idMeal;
    }
}
