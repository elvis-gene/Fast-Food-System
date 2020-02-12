/*
    Elvis Presley Gene (217304338)
    Nico Fortuin (216237912)
 */


public class Sale {
    private int itemSold;
    private double totalSalesValue;

    public Sale() {
        itemSold = 0;
        totalSalesValue = 0.00;
    }

    public void incrementItemSold(int quantity){
        this.itemSold += quantity;
    }

    public void incrementTotalSales(double pr){
        totalSalesValue += pr;
    }

    public int getItemSold(){
        return itemSold;
    }

    public double getTotalSalesValue(){
        return totalSalesValue;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "itemSold=" + itemSold +
                ", totalSalesValue=" + totalSalesValue +
                '}';
    }
}
