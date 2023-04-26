package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;
    @FXML private AnchorPane recipeListItem;
    @FXML private ImageView recipeImageView;
    @FXML private Label recipeNameLabel;
    @FXML private Label recipeTextArea;
    @FXML private Label listItemPrice;
    @FXML private Label listItemTime;
    @FXML private ImageView listItemCuisine;
    @FXML private ImageView listItemDiff;
    @FXML private ImageView listItemIngredient;


    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;

        try {
            recipeImageView.setImage(recipe.getFXImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            recipeNameLabel.setText(recipe.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            recipeTextArea.setText(recipe.getDescription().replace(',','\n'));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            listItemPrice.setText(recipe.getPrice() + " kr");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            listItemTime.setText(recipe.getTime() + " min");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            listItemDiff.setImage(parentController.getDifficultyImage(recipe.getDifficulty()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            listItemCuisine.setImage(parentController.getCuisineImage(recipe.getCuisine()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            listItemIngredient.setImage(parentController.getMainIngredientImage((recipe.getMainIngredient())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onRecipeClick(Event event){
        parentController.showRecipeDetailView(recipe);
    }
}
