package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.automata.Automata;
import sample.automata.Product;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Button btnOn;
    @FXML
    private Button btnOff;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnCoin;
    @FXML
    private Button btnMenu1;
    @FXML
    private Button btnMenu2;
    @FXML
    private Button btnMenu3;
    @FXML
    private Button btnMenu4;
    @FXML
    private Button btnMenu5;
    @FXML
    private Button btnCoin1;
    @FXML
    private Button btnCoin2;
    @FXML
    private Button btnCoin3;

    @FXML
    private TextArea taState;

    @FXML
    private TextField tfCash;
    @FXML
    private TextField tfChange;

    @FXML
    private TableView<Product> tblvMenu;
    @FXML
    private TableColumn<Product, Integer> tcProdNumber;
    @FXML
    private TableColumn<Product, String> tcProdName;
    @FXML
    private TableColumn<Product, Double> tcProdPrice;

    @FXML
    private ProgressBar pbCookingProgress;

    private Automata automata;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //URL jsnFileURL = getClass().getResource("resources/products and prices.json");
        //String jsnFdir = jsnFileURL.toString();
        //System.out.println(jsnFdir);
        //automata = new Automata("./src/sample/resources/products and prices.json");
        automata = new Automata();
        automata.getMenufromJSON();
        getAutomataState();
        tcProdNumber.setCellValueFactory(new PropertyValueFactory<Product,Integer>("prodNumber"));
        tcProdName.setCellValueFactory(new PropertyValueFactory<Product,String>("prodName"));
        tcProdPrice.setCellValueFactory(new PropertyValueFactory<Product,Double>("prodPrice"));
    }

    private void getAutomataState(){
        taState.textProperty().unbind();
        taState.setText(automata.getState().toString());
        tfCash.setText(automata.getStrCash());
        tfChange.setText(automata.getStrChange());
    }

    private void setMenu(List<Product> products){
        ObservableList<Product> olProducts = FXCollections.observableArrayList(products);
        tblvMenu.setItems(olProducts);
    }

    public void coinClick(int coinValue){
        automata.coin(coinValue);
        getAutomataState();
    }

    public void setBtnMenu(int prodNumber){
        automata.choice(prodNumber);
        pbCookingProgress.progressProperty().bind(automata.getProcess().progressProperty());
        taState.textProperty().bind(automata.getProcess().messageProperty());
        tfCash.setText(automata.getStrCash());
        tfChange.setText(automata.getStrChange());
    }

    public void onClick(){
        automata.on();
        getAutomataState();
        setMenu(automata.getProducts());
    }

    public void offClick(){
        automata.off();
        getAutomataState();
        setMenu(new ArrayList<Product>());
    }

    public void coin1Click(){
        coinClick(5);
    }
    public void coin2Click(){
        coinClick(10);
    }
    public void coin3Click(){
        coinClick(50);
    }

    public void cancelClick(){
        automata.cancel();
        getAutomataState();
    }

    public void setBtnMenu1(){
        setBtnMenu(0);
    }
    public void setBtnMenu2(){
        setBtnMenu(1);
    }
    public void setBtnMenu3(){
        setBtnMenu(2);
    }
    public void setBtnMenu4(){
        setBtnMenu(3);
    }
    public void setBtnMenu5(){
        setBtnMenu(4);
    }
}