package com.example.recipedia;

public class DataClass {

    private String recipe_name;
    private String recipe_ingredient;
    private String recipe_time;
    private String recipe_photo;
    private String recipe_serving;
    private String recipe_calories;
    private String recipe_video;
    private float recipe_rating;
    private String recipe_uid;
    private String recipe_user;
    private String recipe_direction;
    private String recipe_category;

    public String getRecipe_direction() {
        return recipe_direction;
    }
    public String getRecipe_serving() {
        return recipe_serving;
    }

    public String getRecipe_calories() {
        return recipe_calories;
    }

    public String getRecipe_video() {
        return recipe_video;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public String getRecipe_ingredient() {
        return recipe_ingredient;
    }

    public String getRecipe_time() {
        return recipe_time;
    }

    public String getRecipe_photo() {
        return recipe_photo;
    }

    public String getRecipe_category(){ return recipe_category; }
    public String getRecipe_user() {
        return recipe_user;
    }
    public float getRecipe_rating() {
        return recipe_rating;
    }
    public String getRecipe_uid(){
        return recipe_uid;
    }


    public DataClass(){

    }

    public DataClass(String recipe_name, String recipe_ingredient, String recipe_time, String recipe_photo, String recipe_serving, String recipe_calories, String recipe_video, String recipe_category, String recipe_direction, String recipe_user, float recipe_rating, String recipe_uid) {
        this.recipe_name = recipe_name;
        this.recipe_ingredient = recipe_ingredient;
        this.recipe_time = recipe_time;
        this.recipe_photo = recipe_photo;
        this.recipe_serving = recipe_serving;
        this.recipe_calories = recipe_calories;
        this.recipe_video = recipe_video;
        this.recipe_category = recipe_category;
        this.recipe_direction = recipe_direction;
        this.recipe_user = recipe_user;
        this.recipe_rating = recipe_rating;
        this.recipe_uid = recipe_uid;
    }
}
