package fr.bluesam.tavernia;

public class SheetHelperClass {
    String name, cClass;

    public SheetHelperClass(String name, String cClass) {
        this.cClass = cClass;
        this.name = name;
    }

    public SheetHelperClass() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcClass() {
        return cClass;
    }

    public void setcClass(String cClass) {
        this.cClass = cClass;
    }
}
