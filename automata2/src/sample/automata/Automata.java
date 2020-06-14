package sample.automata;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.processes.CookingProcess;
import sample.processes.NeedCoinProcess;
import sample.processes.Process;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Automata {
    private STATES state;
    private double cash;
    private double change;
    private String strCash;
    private String strChange;
    private ArrayList<Product> products;
    private int prodNumber;
    private Process process;
    private String jsonFileDirection;

    //constructor
    public Automata(String jsonFileDirection) {
        this.state = STATES.OFF;
        this.cash = 0.0;
        this.change = 0.0;
        this.strCash = "";
        this.strChange = "";
        this.products = new ArrayList<>();
        this.process = new CookingProcess();
        this.jsonFileDirection = jsonFileDirection;
    }

    //getters
    ArrayList<Product> getProducts(){
        return this.products;
    }
    STATES getState(){
        return this.state;
    }
    String getStrCash() { return this.strCash; }
    String getStrChange(){ return this.strChange; }
    Process getProcess(){ return this.process; }

    //setters
    private void setState(STATES state){ this.state = state; }
    private void setCash(double value){
        this.cash += value;
        this.strCash = Double.toString(this.cash);
    }
    private void setChange(double value){
        this.change += value;
        this.strChange = Double.toString(this.change);
    }
    public void setJsonFileDirection(String jsonFileDirection){
        this.jsonFileDirection = jsonFileDirection;
    }

    //methods
    private double getProdPrice(){
        return products.get(this.prodNumber).getProdPrice();
    }

    protected void on(){
        if (this.state == STATES.OFF) {
            setState(STATES.WAIT);
            setCash(0);
            setChange(0);
        }
    }

    protected void off(){
        if (this.cash == 0){
            setState(STATES.OFF);
            this.strCash = "";
            this.strChange = "";
        }
    }

    protected void coin(double coinValue){
        if (this.state == STATES.WAIT || this.state == STATES.NEEDCOIN){
            setState(STATES.ACCEPT);
            setCash(coinValue);
        }
        else if (this.state == STATES.ACCEPT){
            setCash(coinValue);
        }
        else{
            setChange(coinValue);
        }
    }

    protected void cancel(){
        if (this.state == STATES.ACCEPT || this.state == STATES.CHECK || this.state == STATES.NEEDCOIN){
            setState(STATES.WAIT);
            setChange(this.cash);
            setCash(-this.cash);
        }
    }

    protected void choice(int prodNumber){
        if (this.state == STATES.ACCEPT || this.state == STATES.WAIT) {
            setState(STATES.CHECK);
            this.prodNumber = prodNumber;
            this.check();
        }
    }

    private void check(){
        if (this.cash < this.getProdPrice())
            this.needCoin();
        else
            this.cook();
    }

    private void needCoin(){
        setState(STATES.NEEDCOIN);
        this.process = new NeedCoinProcess();
        Thread thread = new Thread(this.process);
        thread.setDaemon(true);
        thread.start();
    }

    private void cook(){
        setState(STATES.COOK);
        this.process = new CookingProcess();
        Thread thread = new Thread(this.process);
        thread.setDaemon(true);
        thread.start();
        this.finish();
    }

    private void finish(){
        if (this.state == STATES.COOK) {
            setState(STATES.WAIT);
            setChange(this.cash - this.getProdPrice());
            setCash(-this.cash);
        }
    }

    protected void getMenufromJSON() {
        //File jsfile = new File("./products and prices.json");
        File jsfile = new File(jsonFileDirection);
        try {
            String content = FileUtils.readFileToString(jsfile, "utf-8");
            JSONObject jsList = new JSONObject(content);
            JSONArray jsProducts = (JSONArray) jsList.get("products");
            for (Object obj : jsProducts) {
                int prodNumber = Integer.parseInt(((JSONObject) obj).get("number").toString());
                String prodName = ((JSONObject) obj).get("name").toString();
                double prodPrice = Double.parseDouble(((JSONObject) obj).get("price").toString());
                Product product = new Product(prodNumber, prodName, prodPrice);
                this.products.add(product);
            }
        }
        catch (IOException e) {
        }
    }
}