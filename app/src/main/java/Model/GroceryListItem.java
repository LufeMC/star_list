package Model;

public class GroceryListItem {

    private int id;
    private String item_colunm;
    private String item;
    private String quantity;
    private String checked;

    public GroceryListItem() {
    }

    public GroceryListItem(int id, String item_colunm, String item, String quantity, String checked) {
        this.id = id;
        this.item_colunm = item_colunm;
        this.item = item;
        this.quantity = quantity;
        this.checked = checked;
    }

    public GroceryListItem(int id, String item_colunm, String item, String quantity) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.item_colunm = item_colunm;
    }

    public GroceryListItem(String item_colunm, String item, String quantity) {
        this.item_colunm = item_colunm;
        this.item = item;
        this.quantity = quantity;
    }

    public GroceryListItem(String item_colunm, String item, String quantity, String checked) {
        this.item_colunm = item_colunm;
        this.item = item;
        this.quantity = quantity;
        this.checked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getItem_colunm() {
        return item_colunm;
    }

    public void setItem_colunm(String item_colunm) {
        this.item_colunm = item_colunm;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }
}
