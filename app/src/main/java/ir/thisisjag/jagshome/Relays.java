package ir.thisisjag.jagshome;

public class Relays {
    private int _id;
    private int id;
    private int subId;
    private String name;
    private int state;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    public Relays(int _id, int id, int subId, String name, int state) {
        this._id=_id;
        this.id = id;
        this.subId = subId;
        this.name = name;
        this.state = state;
    }

    @Override
    public String toString() {
        return "Relays{" +
                "_id=" + _id +
                "id=" + id +
                ", subId=" + subId +
                ", name='" + name + '\'' +
                ", state=" + state +
                '}';
    }

}
