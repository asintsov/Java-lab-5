package sample.automata;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import sample.processes.CookingProcess;
import sample.processes.NeedCoinProcess;
import sample.processes.Process;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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
    public Automata() {
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
    public ArrayList<Product> getProducts(){
        return this.products;
    }
    public STATES getState(){
        return this.state;
    }
    public String getStrCash() { return this.strCash; }
    public String getStrChange(){ return this.strChange; }
    public Process getProcess(){ return this.process; }

    //setters
    public void setState(STATES state){ this.state = state; }
    public void setCash(double value){
        this.cash += value;
        this.strCash = Double.toString(this.cash);
    }
    public void setChange(double value){
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

    public void on(){
        if (this.state == STATES.OFF) {
            setState(STATES.WAIT);
            setCash(-this.cash);
            setChange(-this.change);
        }
    }

    public void off(){
        if (this.cash == 0){
            setState(STATES.OFF);
            this.strCash = "";
            this.strChange = "";
        }
    }

    public void coin(double coinValue){
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

    public void cancel(){
        if (this.state == STATES.ACCEPT || this.state == STATES.CHECK || this.state == STATES.NEEDCOIN){
            setState(STATES.WAIT);
            setChange(this.cash);
            setCash(-this.cash);
        }
    }

    public void choice(int prodNumber){
        if (this.state == STATES.ACCEPT || this.state == STATES.WAIT) {
            setState(STATES.CHECK);
            setChange(-this.change);
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

    public void getMenufromJSON() {
        //File jsfile = new File("./products_and_prices.json");
        File jsfile = this.getResourceAsFile();
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

    public File getResourceAsFile() {
        try {
            InputStream inStream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream("sample/products_and_prices.json");
            if (inStream == null) {
                return null;
            }
            File tempFile = File.createTempFile(String.valueOf(inStream.hashCode()), ".tmp");
            tempFile.deleteOnExit();

            try (FileOutputStream outStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }
            }
            return tempFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}