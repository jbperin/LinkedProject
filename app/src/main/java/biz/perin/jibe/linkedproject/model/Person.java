package biz.perin.jibe.linkedproject.model;

/**
 * Created by Famille PERIN on 20/10/2017.
 */
public class Person {


    private String pseudo;
    private Integer solde=null;
    private String address = null;
    private String name = null;
    private String phone1 = null;
    private String phone2 = null;
    private Integer numberOfExchange =null;
    private String lastPublish = null;

    public Person() { }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone1() {
        return phone1;
    }
    public String getPhone2() {
        return phone2;
    }

    public void setPhone1(String phone) {
        this.phone1 = phone;
    }
    public void setPhone2(String phone) {
        this.phone2 = phone;
    }

    public String getPseudo() {
        return pseudo;
    }

    @Override
    public String toString() {
        return "Person{" +
                "pseudo='" + pseudo + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", phone1='" + phone1 + '\'' +
                ", phone2='" + phone2 + '\'' +
                '}';
    }

    public Integer getSolde() {
        return solde;
    }

    public void setSolde(Integer i) {
        this.solde = i;
    }

    public Integer getNumberOfExchange() {
        return numberOfExchange;
    }

    public void setNumberOfExchange(Integer i) {
        this.numberOfExchange = i;
    }

    public void setLastPublish(String param) {
        this.lastPublish = param;
    }

    public String getLastPublish() {
        return lastPublish;
    }

}
