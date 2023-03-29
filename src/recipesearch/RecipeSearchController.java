
package recipesearch;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {


    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}