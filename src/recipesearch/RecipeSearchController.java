
package recipesearch;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.layout.FlowPane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import static javafx.collections.FXCollections.observableArrayList;


public class RecipeSearchController implements Initializable {
    //Search parameters
    @FXML private ComboBox<String> mainIngredientComboBox;
    @FXML private ComboBox<String> cuisineComboBox;
    @FXML private RadioButton allRadioButton;
    @FXML private RadioButton easyRadioButton;
    @FXML private RadioButton mediumRadioButton;
    @FXML private RadioButton hardRadioButton;
    @FXML private Spinner maxPriceSpinner;
    @FXML private Slider maxTimeSlider;
    @FXML private FlowPane searchResultFlowPane;
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private RecipeBackendController backendController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backendController = new RecipeBackendController();
        mainIngredientComboBox.setItems(javafx.collections.FXCollections.observableArrayList(backendController.getAllowedMainIngredients()));
        mainIngredientComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateRecipeList());
    }



    private void updateRecipeList(){
        searchResultFlowPane.getChildren().clear();
        var recipes = backendController.getRecipes();

        for (Recipe recipe : recipes){
            searchResultFlowPane.getChildren().add(new RecipeListItem(recipe, this));
        }
    }

}