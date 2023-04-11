
package recipesearch;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    @FXML private Label maxTimeLabel;
    @FXML private FlowPane searchResultFlowPane;
    @FXML private ToggleGroup difficultyToggleGroup = new ToggleGroup();
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private RecipeBackendController backendController;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backendController = new RecipeBackendController();
        initComboBoxes();
        initToggleGroups();
        initSpinner();
        initSlider();
    }


    private void initComboBoxes(){
        mainIngredientComboBox.getItems().addAll(backendController.getAllowedMainIngredients());
        cuisineComboBox.getItems().addAll(backendController.getAllowedCuisines());

        mainIngredientComboBox.valueProperty().addListener((obs, oldVal, newVal) ->  {
            backendController.setMainIngredient(newVal);updateRecipeList();
        });
        cuisineComboBox.valueProperty().addListener((obs, oldVal, newVal) ->  {
            backendController.setCuisine(newVal);updateRecipeList();
        });
    }

    private void initToggleGroups() {
        allRadioButton.setToggleGroup(difficultyToggleGroup);
        easyRadioButton.setToggleGroup(difficultyToggleGroup);
        mediumRadioButton.setToggleGroup(difficultyToggleGroup);
        hardRadioButton.setToggleGroup(difficultyToggleGroup);

        allRadioButton.setSelected(true);

        difficultyToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) ->  {
            if (difficultyToggleGroup.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                backendController.setDifficulty(selected.getText());
                updateRecipeList();
            }
        });
    }

    private void initSpinner(){
        SpinnerValueFactory<Integer> maxPriceValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,999,0,5);
        maxPriceSpinner.setValueFactory(maxPriceValueFactory);
        maxPriceSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            backendController.setMaxPrice(Integer.parseInt(newVal.toString()));
            updateRecipeList();
        });
    }

    private void initSlider(){
        maxTimeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null && !newVal.equals(oldVal) && !maxTimeSlider.isValueChanging()) {
                backendController.setMaxTime(newVal.intValue());
                updateRecipeList();
                maxTimeLabel.setText(newVal.intValue() + " min");
            }
        });
    }
    private void updateRecipeList(){
        searchResultFlowPane.getChildren().clear();
        var recipes = backendController.getRecipes();

        for (Recipe recipe : recipes){
            searchResultFlowPane.getChildren().add(new RecipeListItem(recipe, this));
        }
    }

}