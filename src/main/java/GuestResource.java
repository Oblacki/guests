import com.fasterxml.jackson.databind.ObjectMapper;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.discovery.annotations.DiscoverService;

import javax.enterprise.context.RequestScoped;
import com.kumuluz.ee.logs.cdi.Log;
import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("guests")
@Log
public class GuestResource {

    private Client httpClient;
    private ObjectMapper objectMapper;

    @Inject
    @DiscoverService(value = "apartments", version = "1.0.x", environment = "dev")
    private Optional<String> baseUrl;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        objectMapper = new ObjectMapper();
    }

    @Inject
    private RestProperties restProperties;

    @GET
    @Metered
    public Response getAllGuests() {
        List<Guest> guests = Database.getGuests();
        return Response.ok(guests).build();
    }

    @GET
    @Path("{guestId}")
    public Response getGuest(@PathParam("guestId") String guestId) {
        Guest guest = Database.getGuest(guestId);

        List<Apartment> apartments = null;

        Optional<Boolean> apartmentServiceEnabled = ConfigurationUtil.getInstance().getBoolean("rest-properties.external-services.apartment-service.enabled");
        System.out.println("----------------------" +apartmentServiceEnabled.get()+ "-------------------------");


        System.out.println("---------------" + restProperties.isApartmentServiceEnabled() + "---------------------");
        if(restProperties.isApartmentServiceEnabled())
            apartments = getApartments(guestId);

        guest.setApartmentHistory(apartments);

        return guest != null
                ? Response.ok(guest).build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response addNewGuest(Guest guest) {
        Database.addGuest(guest);
        return Response.noContent().build();
    }

    @DELETE
    @Path("{guestId}")
    public Response deleteGuest(@PathParam("guestId") String guestId) {
        Database.deleteGuest(guestId);
        return Response.noContent().build();
    }

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getApartmentsFallback")
    @Timeout//(value = 2, unit = ChronoUnit.SECONDS)
    public List<Apartment> getApartments(String guestId){

        if(baseUrl.isPresent()) {
            try {
                System.out.println(baseUrl.get() + "/v1/apartments/guest/" + guestId);
                return httpClient.target(baseUrl.get() + "/v1/apartments/guest/" + guestId).request()
                        .get(new GenericType<List<Apartment>>() {});


            } catch (Exception e) {
                String msg = e.getClass().getName() + " occured: " + e.getMessage();
                System.out.println(msg);
                throw new InternalServerErrorException(msg);
            }
        }
        else
            return new ArrayList<>();
    }

    public List<Apartment> getApartmentsFallback(String guestId) {

        List<Apartment> apartments = new ArrayList<>();

        Apartment ap = new Apartment();

        ap.setId("N/A");
        ap.setNumOfBeds(0);
        List<String> guestIds = new ArrayList<String>();
        guestIds.add("N/A");

        ap.setGuestIds(guestIds);

        apartments.add(ap);

        return apartments;

    }

    private List<Apartment> getObjects(String json) throws IOException {
        return json == null ? new ArrayList<>() : objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Apartment.class));
    }
}
