import java.util.List;

/**
 * @author Matej Dolenc
 */
public class Apartment {

    private String id;
    private int numOfBeds;
    private String customerId;
    private List<String> guestIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumOfBeds() {
        return numOfBeds;
    }

    public void setNumOfBeds(int numOfBeds) {
        this.numOfBeds = numOfBeds;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<String> getGuestIds() {
        return guestIds;
    }

    public void setGuestIds(List<String> guestIds) {
        this.guestIds = guestIds;
    }

    public void setCustomerId(String customerId) {
       this.customerId = customerId;
    }

}
