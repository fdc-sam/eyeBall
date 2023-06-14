package live.sai.eyeball.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Product {
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public void setLowLevelStock(String lowLevelStock) {
        this.lowLevelStock = lowLevelStock;
    }

    public void setDateCreate(String dateCreate) {
        this.dateCreate = dateCreate;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    private String id;
    private String name;
    private String type;
    private String cost;
    private String stocks;
    private String lowLevelStock;
    private String dateCreate;
    private String dateModified;
    private byte[] image;

    public Product(String id, String name, String type, String cost, String stocks, String lowLevelStock, String dateCreate, String dateModified) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.stocks = stocks;
        this.lowLevelStock = lowLevelStock;
        this.dateCreate = dateCreate;
        this.dateModified = dateModified;
    }

    public Product() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getCost() {
        return cost;
    }

    public String getStocks() {
        return stocks;
    }

    public String getLowLevelStock() {
        return lowLevelStock;
    }

    public String getDateCreate() {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(dateCreate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateCreate;
    }

    public String getDateModified() {
        return dateModified;
    }



    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }
}
