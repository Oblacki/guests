
import java.util.List;



public class Guest {

    private String id;
    private String firstName;
    private String lastName;
    private List<Apartment> apartmentHistory;

    public String getId() { return id; }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public List<Apartment> getApartmentHistory() { return apartmentHistory; }

    public void setApartmentHistory (List<Apartment> apartments) { this.apartmentHistory = apartments; }
}
