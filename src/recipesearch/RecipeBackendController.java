package recipesearch;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.Arrays;
import java.util.List;

public class RecipeBackendController {
    public RecipeBackendController() {
    }
    private RecipeSearchController searchController = new RecipeSearchController();

    private List<String> allowedCuisines = (Arrays.asList("Visa alla","Sverige", "Grekland", "Indien", "Asien", "Afrika", "Frankrike"));
    private List<String> allowedMainIngredients = (Arrays.asList("Visa alla","Kött", "Fisk", "Kyckling", "Vegetarisk"));

    public List<String> getAllowedCuisines() {
        return allowedCuisines;
    }

    public List<String> getAllowedMainIngredients() {
        return allowedMainIngredients;
    }

    public List<String> getAllowedDifficulties() {
        return allowedDifficulties;
    }

    public List<Integer> getAllowedMaxTime() {
        return allowedMaxTime;
    }

    private List<String> allowedDifficulties = (Arrays.asList("Lätt", "Mellan", "Svår"));
    private List<Integer> allowedMaxTime = (Arrays.asList(10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150));

    private String cuisine = null;
    private String mainIngredient = null;
    private String difficulty = null;
    private int maxPrice = 0;
    private int maxTime = 0;
    private SearchFilter filter = new SearchFilter(null, 0, null, 0, null);



    public List<Recipe> getRecipes(){
        filter = new SearchFilter(difficulty, maxTime, cuisine, maxPrice, mainIngredient);
        return RecipeDatabase.getSharedInstance().search(filter);
    }

    public void setCuisine(String cuisine){
        if (!allowedCuisines.contains(cuisine)){
            this.cuisine = null;
        } else {
            this.cuisine = cuisine;
        }
    }

    public void setMainIngredient(String mainIngredient){
        if (!allowedMainIngredients.contains(mainIngredient)){
            this.mainIngredient = null;
        } else {
            this.mainIngredient = mainIngredient;
        }
    }

    public void setDifficulty(String difficulty){
        if (!allowedDifficulties.contains(difficulty)){
            this.difficulty = null;
        } else {
            this.difficulty = difficulty;
        }
    }

    public void setMaxPrice(int maxPrice){
        if (maxPrice > 0){
            this.maxPrice = maxPrice;
        } else {
            this.maxPrice = 0;
        }
    }

    public void setMaxTime(int maxTime){
        if (!allowedMaxTime.contains(maxTime)){
            this.maxTime = 0;
        } else {
            this.maxTime = maxTime;
        }
    }

}
