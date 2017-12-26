

import java.util.ArrayList;
import java.util.List;

//
public class Database {
    
    private static List<Guest> guests = new ArrayList<>();
    private static List<Apartment> apartments = new ArrayList<>();

    public static List<Guest> getGuests() {
        return guests;
    }
    public static List<Apartment> getApartments() { return apartments; }

    //guests
    public static Guest getGuest(String guestId) {
        for (Guest guest : guests) {
            if (guest.getId().equals(guestId))
                return guest;
        }
        return null;
    }

    public static void addGuest(Guest guest) {
        guests.add(guest);
    }

    public static void deleteGuest(String guestId) {
        for (Guest guest : guests) {
            if (guest.getId().equals(guestId)) {
                guests.remove(guest);
                break;
            }
        }
    }


    //apartments
    public static void deleteApartment(String apartmentId) {
        for (Apartment apartment : apartments) {
            if (apartment.getId().equals(apartmentId)) {
                apartments.remove(apartment);
                break;
            }
        }
    }

    public static Apartment getApartment(String apartmentId) {
        for (Apartment apartment : apartments) {
            if (apartment.getId().equals(apartmentId))
                return apartment;
        }
        return null;
    }

    public static void addApartment(Apartment apartment) {
        apartments.add(apartment);
    }

    public static List<Apartment> getApartmentByGuestId(String guestId) {
        List<Apartment> filteredApartments = new ArrayList<Apartment>();

        for (Apartment apartment : apartments) {
            if (apartment.getGuestIds().contains(guestId)) {
                System.out.println(apartment.toString());
                filteredApartments.add(apartment);
            }
        }
        return filteredApartments;
    }

}
