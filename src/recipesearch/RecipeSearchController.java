
package recipesearch;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    @FXML private Label recipeDetailNameLabel;
    @FXML private ImageView recipeDetailImageView;
    @FXML private Button recipeDetailCloseButton;
    @FXML private SplitPane searchPane;
    @FXML private AnchorPane recipeDetailPane;
    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    private RecipeBackendController backendController;
    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        backendController = new RecipeBackendController();
        initComboBoxes();
        initToggleGroups();
        initSpinner();
        initSlider();
        for(Recipe recipe : backendController.getRecipes()){
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
            searchResultFlowPane.getChildren().add(recipeListItem);
        }
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

    // TODO: Make sure only allowed numbers can be selected and that the label is updated when the slider is dragged
    private void initSlider(){
        maxTimeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            maxTimeLabel.textProperty().setValue((newVal.intValue()-newVal.intValue()%10) + " min");
            if(newVal != null && !newVal.equals(oldVal) && !maxTimeSlider.isValueChanging()) {
                backendController.setMaxTime(newVal.intValue()-newVal.intValue()%10);
                updateRecipeList();
                maxTimeSlider.setValue(newVal.intValue()-newVal.intValue()%10);
            }
        });
       /* maxTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldVal,
                    Number newVal) {
                maxTimeLabel.textProperty().setValue(String.valueOf(newVal.intValue()) + " min");
                if(newVal != null && !newVal.equals(oldVal) && !maxTimeSlider.isValueChanging()) {
                   // backendController.setMaxTime(newVal.intValue());
                    //updateRecipeList();
            }
        }
        });*/
    }

    private void populateRecipeDetailView(Recipe recipe){
        recipeDetailNameLabel.setText(recipe.getName());
        recipeDetailImageView.setImage(recipe.getFXImage());
    }

    @FXML
    public  void closeRecipeView(){
        searchPane.toFront();
    }

    public void showRecipeDetailView(Recipe recipe){
        populateRecipeDetailView(recipe);
        recipeDetailPane.toFront();
    }

/*    public final EventHandler<? super MouseEvent> getOnDragDetected() {
    }*/
    private void updateRecipeList(){
        searchResultFlowPane.getChildren().clear();
        var recipes = backendController.getRecipes();

        for (Recipe recipe : recipes){
            searchResultFlowPane.getChildren().add(recipeListItemMap.get(recipe.getName()));
        }
    }

}