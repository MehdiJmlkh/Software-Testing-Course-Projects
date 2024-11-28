package mizdooni.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.*;

import mizdooni.MizdooniApplication;
import mizdooni.exceptions.UserNotManager;
import mizdooni.model.Restaurant;
import mizdooni.model.RestaurantSearchFilter;
import mizdooni.response.PagedList;
import mizdooni.response.Response;
import mizdooni.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static mizdooni.controllers.ControllerUtils.*;
import static mizdooni.utils.CreateSample.*;
import static mizdooni.utils.CreateSample.createSampleRestaurantSearchFilter;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = MizdooniApplication.class)
public class RestaurantControllerApiTest {
    @MockBean
    private RestaurantService restaurantService;
    @Autowired
    private TableController tableController;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private void performAndExpectBadRequestOfParamsBadType(Map<String, Object> params) throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(PARAMS_BAD_TYPE));
    }

    private void performAndExpectBadRequestOfTypeParamsMissing(Map<String, Object> params) throws Exception {
        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(PARAMS_MISSING));
    }

    @Test
    void getRestaurant_ExistedRestaurant_ReturnsOkResponse() throws Exception {
        Restaurant restaurant = createSampleRestaurant();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        var formatter = DateTimeFormatter.ofPattern("HH:mm");

        mockMvc.perform(get("/restaurants/{restaurantId}", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant found"))
                .andExpect(jsonPath("$.data.id").value(restaurant.getId()))
                .andExpect(jsonPath("$.data.name").value(restaurant.getName()))
                .andExpect(jsonPath("$.data.type").value(restaurant.getType()))
                .andExpect(jsonPath("$.data.startTime").value(restaurant.getStartTime().format(formatter)))
                .andExpect(jsonPath("$.data.endTime").value(restaurant.getEndTime().format(formatter)))
                .andExpect(jsonPath("$.data.address.country").value(restaurant.getAddress().getCountry()))
                .andExpect(jsonPath("$.data.address.city").value(restaurant.getAddress().getCity()))
                .andExpect(jsonPath("$.data.address.street").value(restaurant.getAddress().getStreet()))
                .andExpect(jsonPath("$.data.starCount").value(restaurant.getStarCount()))
                .andExpect(jsonPath("$.data.maxSeatsNumber").value(restaurant.getMaxSeatsNumber()))
                .andExpect(jsonPath("$.data.averageRating.food").value(restaurant.getAverageRating().food))
                .andExpect(jsonPath("$.data.averageRating.service").value(restaurant.getAverageRating().service))
                .andExpect(jsonPath("$.data.averageRating.ambiance").value(restaurant.getAverageRating().ambiance))
                .andExpect(jsonPath("$.data.averageRating.overall").value(restaurant.getAverageRating().overall))
                .andExpect(jsonPath("$.data.managerUsername").value(restaurant.getManager().getUsername()));
    }

    @Test
    void getRestaurantApi_NotExistedRestaurant_ReturnsNotFoundResponse() throws Exception {
        when(restaurantService.getRestaurant(anyInt())).thenReturn(null);

        mockMvc.perform(get("/restaurants/{restaurantId}", createSampleId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("restaurant not found"));
    }

    @Test
    void getRestaurantsApi_ValidArgs_ReturnsOkResponse() throws Exception {
        int page = createSamplePositiveNumber();
        RestaurantSearchFilter filter = createSampleRestaurantSearchFilter();
        PagedList<Restaurant> restaurants = createSamplePagedListOfRestaurants(page);
        Restaurant restaurant = restaurants.getPageList().getFirst();
        when(restaurantService.getRestaurants(anyInt(), any())).thenReturn(restaurants);

        mockMvc.perform(get("/restaurants")
                        .param("page", Integer.toString(page))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurants listed"))
                .andExpect(jsonPath("$.data.page").value(page))
                .andExpect(jsonPath("$.data.totalPages").value(1))
                .andExpect(jsonPath("$.data.pageList[0].id").value(restaurant.getId()))
                .andExpect(jsonPath("$.data.pageList[0].name").value(restaurant.getName()))
                .andExpect(jsonPath("$.data.pageList[0].address.country").value(restaurant.getAddress().getCountry()))
                .andExpect(jsonPath("$.data.pageList[0].address.city").value(restaurant.getAddress().getCity()))
                .andExpect(jsonPath("$.data.pageList[0].address.street").value(restaurant.getAddress().getStreet()))
                .andExpect(jsonPath("$.data.pageList[0].averageRating.food").value(restaurant.getAverageRating().food))
                .andExpect(jsonPath("$.data.pageList[0].averageRating.service").value(restaurant.getAverageRating().service))
                .andExpect(jsonPath("$.data.pageList[0].averageRating.ambiance").value(restaurant.getAverageRating().ambiance))
                .andExpect(jsonPath("$.data.pageList[0].averageRating.overall").value(restaurant.getAverageRating().overall))
                .andExpect(jsonPath("$.data.pageList[0].managerUsername").value(restaurant.getManager().getUsername()));
    }

    @Test
    void getManagerRestaurantsApi_ValidArgs_ReturnsOkResponse() throws Exception {
        List<Restaurant> restaurants = createSampleListOfRestaurants();
        Restaurant restaurant = restaurants.getFirst();
        int managerId = createSampleId();
        when(restaurantService.getManagerRestaurants(managerId)).thenReturn(restaurants);

        var formatter = DateTimeFormatter.ofPattern("HH:mm");

        mockMvc.perform(get("/restaurants/manager/{managerId}", managerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("manager restaurants listed"))
                .andExpect(jsonPath("$.data[0].id").value(restaurant.getId()))
                .andExpect(jsonPath("$.data[0].name").value(restaurant.getName()))
                .andExpect(jsonPath("$.data[0].type").value(restaurant.getType()))
                .andExpect(jsonPath("$.data[0].startTime").value(restaurant.getStartTime().format(formatter)))
                .andExpect(jsonPath("$.data[0].endTime").value(restaurant.getEndTime().format(formatter)))
                .andExpect(jsonPath("$.data[0].address.country").value(restaurant.getAddress().getCountry()))
                .andExpect(jsonPath("$.data[0].address.city").value(restaurant.getAddress().getCity()))
                .andExpect(jsonPath("$.data[0].address.street").value(restaurant.getAddress().getStreet()))
                .andExpect(jsonPath("$.data[0].starCount").value(restaurant.getStarCount()))
                .andExpect(jsonPath("$.data[0].maxSeatsNumber").value(restaurant.getMaxSeatsNumber()))
                .andExpect(jsonPath("$.data[0].averageRating.food").value(restaurant.getAverageRating().food))
                .andExpect(jsonPath("$.data[0].averageRating.service").value(restaurant.getAverageRating().service))
                .andExpect(jsonPath("$.data[0].averageRating.ambiance").value(restaurant.getAverageRating().ambiance))
                .andExpect(jsonPath("$.data[0].averageRating.overall").value(restaurant.getAverageRating().overall))
                .andExpect(jsonPath("$.data[0].managerUsername").value(restaurant.getManager().getUsername()));
    }

    @Test
    void addRestaurantApi_ValidArgs_ReturnsOkResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        int id = createSampleId();
        when(restaurantService.addRestaurant(
                any(), any(), any(), any(), any(), any(), any()))
                .thenReturn(id);

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant added"))
                .andExpect(jsonPath("$.data").value(String.valueOf(id)));
    }

    @Test
    void addRestaurantApi_NameMissing_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.remove("name");

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_TypeMissing_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.remove("type");

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_StartTimeMissing_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.remove("startTime");

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_EndTimeMissing_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.remove("endTime");

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_DescriptionMissing_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.remove("description");

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_AddressMissing_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.remove("address");

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_BadTypeName_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("name", 0);

        performAndExpectBadRequestOfParamsBadType(params);
    }

    @Test
    void addRestaurantApi_BadTypeType_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("type", 0);

        performAndExpectBadRequestOfParamsBadType(params);
    }

    @Test
    void addRestaurantApi_BadTypeDescription_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("description", 0);

        performAndExpectBadRequestOfParamsBadType(params);
    }

    @Test
    void addRestaurantApi_BadTypeStartTime_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("startTime", 0);

        performAndExpectBadRequestOfParamsBadType(params);
    }

    @Test
    void addRestaurantApi_BadTypeEndTime_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("endTime", 0);

        performAndExpectBadRequestOfParamsBadType(params);
    }

    @Test
    void addRestaurantApi_BadTypeAddress_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("address", 0);

        performAndExpectBadRequestOfParamsBadType(params);
    }

    @Test
    void addRestaurantApi_BadTypeImage_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("image", 0);

        performAndExpectBadRequestOfParamsBadType(params);
    }

    @Test
    void addRestaurantApi_BlankName_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("name", createSampleBlankString());

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_BlankType_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("type", createSampleBlankString());

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_BlankDescription_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("description", createSampleBlankString());

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_BlankImage_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("image", createSampleBlankString());

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_BlankCountry_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("address", Map.of(
                "country", createSampleBlankString(),
                "city","city",
                "street","street"));

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_BlankCity_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("address", Map.of(
                "country", "country",
                "city",createSampleBlankString(),
                "street","street"));

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_BlankStreet_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        params.put("address", Map.of(
                "country", "country",
                "city","city",
                "street",createSampleBlankString()));

        performAndExpectBadRequestOfTypeParamsMissing(params);
    }

    @Test
    void addRestaurantApi_restaurantServiceThrowsException_ReturnsBadRequestResponse() throws Exception {
        Map<String, Object> params = createSampleRestaurantMapping();
        UserNotManager exception = createSampleUserNotManagerException();
        doThrow(exception).when(restaurantService).addRestaurant(
                any(), any(), any(), any(), any(), any(), any());

        mockMvc.perform(post("/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    void validateRestaurantNameApi_AvailableName_ReturnsOkResponse() throws Exception {
        String name = createSampleRestaurantName();
        when(restaurantService.restaurantExists(name)).thenReturn(false);

        mockMvc.perform(get("/validate/restaurant-name")
                .param("data", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant name is available"));

    }

    @Test
    void validateRestaurantNameApi_TakenName_ReturnsOkResponse() throws Exception {
        String name = createSampleRestaurantName();
        when(restaurantService.restaurantExists(name)).thenReturn(true);

        mockMvc.perform(get("/validate/restaurant-name")
                        .param("data", name))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("restaurant name is taken"));

    }

    @Test
    void getRestaurantTypesApi_ValidArgs_ReturnsOkResponse() throws Exception {
        Set<String> types = createSampleSetOfRestaurantTypes();
        var typesList = types.stream().toList();
        when(restaurantService.getRestaurantTypes()).thenReturn(types);

        mockMvc.perform(get("/restaurants/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant types"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", containsInAnyOrder(
                        typesList.get(0),
                        typesList.get(1),
                        typesList.get(2)
                )));
    }

    @Test
    void getRestaurantLocationsApi__() throws Exception {
        Map<String, Set<String>> locations = Map.of(
                "location1", Set.of("city1", "street1"),
                "location2", Set.of("city2", "street2"));;
        when(restaurantService.getRestaurantLocations()).thenReturn(locations);

        mockMvc.perform(get("/restaurants/locations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("restaurant locations"))
                .andExpect(jsonPath("$.data.location1").isArray())
                .andExpect(jsonPath("$.data.location1", containsInAnyOrder("city1", "street1")))
                .andExpect(jsonPath("$.data.location2").isArray())
                .andExpect(jsonPath("$.data.location2", containsInAnyOrder("city2", "street2")));
    }

}


