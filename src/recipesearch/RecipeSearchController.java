
package recipesearch;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;

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

    @FXML private ImageView recipeDetailFlagImageView;
    @FXML private ImageView recipeDetailMainIngredientImageView;
    @FXML private ImageView recipeDetailDifficultytImageView;
    @FXML private Label recipeDetailTimeLabel;
    @FXML private Label recipeDetailPriceLabel;

    @FXML private ImageView closeImageView;

    @FXML private TextArea recipeDetailTextArea;
    @FXML private TextArea ingredientsTextArea;
    @FXML private TextArea recipeSummaryTextArea;
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
        populateMainIngredientComboBox();
        populateCuisineComboBox();
        intitTextAreas();
        for(Recipe recipe : backendController.getRecipes()){
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
            searchResultFlowPane.getChildren().add(recipeListItem);
        }
    }

    private void intitTextAreas() {
        ingredientsTextArea.setWrapText(true);
        recipeSummaryTextArea.setWrapText(true);
        recipeDetailTextArea.setWrapText(true);
    }


    private void initComboBoxes(){
        mainIngredientComboBox.getItems().addAll(backendController.getAllowedMainIngredients());
        cuisineComboBox.getItems().addAll(backendController.getAllowedCuisines());

        mainIngredientComboBox.getSelectionModel().selectFirst();
        cuisineComboBox.getSelectionModel().selectFirst();

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
            SpinnerValueFactory<Integer> maxPriceValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,300,0,5);
        maxPriceSpinner.setValueFactory(maxPriceValueFactory);
        maxPriceSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            backendController.setMaxPrice(Integer.parseInt(newVal.toString()));
            updateRecipeList();
        });
    }

    
    private void initSlider(){
        maxTimeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            maxTimeLabel.textProperty().setValue((newVal.intValue()-newVal.intValue()%10) + " min");
            if(newVal != null && !newVal.equals(oldVal) && !maxTimeSlider.isValueChanging()) {
                backendController.setMaxTime(newVal.intValue()-newVal.intValue()%10);
                updateRecipeList();
                maxTimeSlider.setValue(newVal.intValue()-newVal.intValue()%10);
            }
        });
    }

    private void populateRecipeDetailView(Recipe recipe){
        recipeDetailNameLabel.setText(recipe.getName());
        recipeDetailImageView.setImage(recipe.getFXImage());
        recipeDetailMainIngredientImageView.setImage(getMainIngredientImage(recipe.getMainIngredient()));
        recipeDetailDifficultytImageView.setImage(getDifficultyImage(recipe.getDifficulty()));
        recipeDetailFlagImageView.setImage(getCuisineImage(recipe.getCuisine()));

        recipeDetailTimeLabel.setText(recipe.getTime() + " min");
        recipeDetailPriceLabel.setText(recipe.getPrice() + " kr");

        recipeDetailTextArea.setText(recipe.getInstruction().replace(',','\n'));
        ingredientsTextArea.setText(recipe.getIngredients().toString().replace(',','\n').replace('[',' ').replace(']',' '));
        recipeSummaryTextArea.setText(recipe.getDescription().replace(',','\n'));
    }

    private Image getMainIngredientImage(String mainIngredient){
        String iconPath;
        switch (mainIngredient) {
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_meat.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kyckling":
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Vegetariskt":
                iconPath = "RecipeSearch/resources/icon_main_vegetarian.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                return null;
        }
    }

    private Image getDifficultyImage(String difficulty){
        String iconPath;
        switch (difficulty) {
            case "Lätt":
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                return null;
        }
    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredientComboBox.setButtonCell(cellFactory.call(null));
        mainIngredientComboBox.setCellFactory(cellFactory);
    }

    private void populateCuisineComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cuisineComboBox.setButtonCell(cellFactory.call(null));
        cuisineComboBox.setCellFactory(cellFactory);
    }

    public Image getCuisineImage(String cuisine) {
        String iconPath;
        switch (cuisine) {
            case "Sverige":
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            default:
                return null;
        }

        }


        @FXML
    public  void closeRecipeView(){
        searchPane.toFront();
    }

    @FXML
    public void closeButtonMouseEntered(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png")));
    }

    @FXML
    public void closeButtonMouseExited(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close.png")));
    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
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