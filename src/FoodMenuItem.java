/*
    Elvis Presley Gene (217304338)
    Nico Fortuin (216237912)
 */

public class FoodMenuItem {
    private String foodItem;
    private String category;
    private double price;
    private Sale saleInfo;

    public FoodMenuItem() {}

    public FoodMenuItem(String foodItem, String category, double price, Sale saleInfo) {
        this.foodItem = foodItem;
        this.category = category;
        this.price = price;
        this.saleInfo = saleInfo;
    }

    public String getFoodItem() {
        return foodItem;
    }

    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Sale getSaleInfo() {
        return saleInfo;
    }

    public void setSaleInfo(Sale saleInfo) {
        this.saleInfo = saleInfo;
    }

    @Override
    public String toString() { // Used in sales transaction JList
        //TODO: Fix spacing
        return  foodItem + "    R" + price;
    }

}
