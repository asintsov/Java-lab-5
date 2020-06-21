package sample.automata;

public class Product {
    private int prodNumber;
    private String prodName;
    private double prodPrice;

    public Product(int prodNumber, String prodName, double prodPrice) {
        this.prodNumber = prodNumber;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
    }

    public int getProdNumber() {return prodNumber;}
    public String getProdName() {return prodName;}
    public double getProdPrice() {return prodPrice;}
}