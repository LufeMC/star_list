package Model;

public class GroceryList {

    private int id;
    private String listName;
    private String totalQuantity;
    private String databaseName;
    private String wasEdited;
    private String outdated_name;

    public GroceryList(int id, String listName, String totalQuantity, String databaseName, String wasEdited, String outdated_name) {
        this.id = id;
        this.listName = listName;
        this.totalQuantity = totalQuantity;
        this.databaseName = databaseName;
        this.wasEdited = wasEdited;
        this.outdated_name = outdated_name;
    }

    public GroceryList() {


    }

    public GroceryList(int id, String listName, String totalQuantity, String databaseName, String wasEdited) {
        this.id = id;
        this.listName = listName;
        this.totalQuantity = totalQuantity;
        this.databaseName = databaseName;
        this.wasEdited = wasEdited;
    }

    public GroceryList(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public GroceryList(int id, String listName, String databaseName, String totalQuantity) {
        this.id = id;
        this.listName = listName;
        this.databaseName = databaseName;
        this.totalQuantity = totalQuantity;
    }

    public GroceryList(String listName, String totalQuantity) {
        this.listName = listName;
        this.databaseName = totalQuantity;
    }

    public GroceryList(String listName, String databaseName, String totalQuantity) {
        this.listName = listName;
        this.totalQuantity = totalQuantity;
        this.databaseName = databaseName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getWasEdited() {
        return wasEdited;
    }

    public void setWasEdited(String wasEdited) {
        this.wasEdited = wasEdited;
    }

    public String getOutdated_name() {
        return outdated_name;
    }

    public void setOutdated_name(String outdated_name) {
        this.outdated_name = outdated_name;
    }
}
