package mizdooni.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import mizdooni.MizdooniApplication;
import mizdooni.exceptions.UserNotManager;
import mizdooni.model.Restaurant;
import mizdooni.model.Table;
import mizdooni.service.RestaurantService;
import mizdooni.service.TableService;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static mizdooni.controllers.ControllerUtils.PARAMS_BAD_TYPE;
import static mizdooni.controllers.ControllerUtils.PARAMS_MISSING;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static mizdooni.utils.CreateSample.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = MizdooniApplication.class)
public class TableControllerApiTest {
    private static final Logger log = LoggerFactory.getLogger(TableControllerApiTest.class);
    @MockBean
    private RestaurantService restaurantService;
    @MockBean
    private TableService tableService;
    @Autowired
    private TableController tableController;
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getTablesApi_ExistedRestaurant_ReturnsOkResponse() throws Exception {
        Restaurant restaurant = createSampleRestaurant();
        List<Table> tables = createSampleListOfTables();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        when(tableService.getTables(anyInt())).thenReturn(tables);

        mockMvc.perform(get("/tables/{restaurantId}", restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("tables listed"))
                .andExpect(jsonPath("$.data[0].tableNumber").value(tables.getFirst().getTableNumber()))
                .andExpect(jsonPath("$.data[0].seatsNumber").value(tables.getFirst().getSeatsNumber()))
                .andExpect(jsonPath("$.data[1].tableNumber").value(tables.get(1).getTableNumber()))
                .andExpect(jsonPath("$.data[1].seatsNumber").value(tables.get(1).getSeatsNumber()));
    }

    @Test
    void getTablesApi_NotExistedRestaurant_ReturnsNotFoundResponse() throws Exception {
        Restaurant restaurant = createSampleRestaurant();
        List<Table> tables = createSampleListOfTables();
        when(restaurantService.getRestaurant(anyInt())).thenReturn(null);

        mockMvc.perform(get("/tables/{restaurantId}", restaurant.getId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("restaurant not found"));
    }

    @Test
    void addTableApi_ValidArgs_ReturnsOkResponse() throws Exception {
        Restaurant restaurant = createSampleRestaurant();
        when(restaurantService.getRestaurant(restaurant.getId())).thenReturn(restaurant);
        Map<String, String> tables = createSampleTableMap();

        mockMvc.perform(post("/tables/{restaurantId}", restaurant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tables)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("table added"));
    }

    @Test
    void addTableApi_NotExistedRestaurant_ReturnsNotFoundResponse() throws Exception {
        when(restaurantService.getRestaurant(anyInt())).thenReturn(null);
        Map<String, String> tables = createSampleTableMap();

        mockMvc.perform(post("/tables/{restaurantId}", createSampleId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tables)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("restaurant not found"));
    }

    @Test
    void addTableApi_SeatsNumberMissing_ReturnsBadRequestResponse() throws Exception {
        Restaurant restaurant = createSampleRestaurant();
        when(restaurantService.getRestaurant(anyInt())).thenReturn(restaurant);
        Map<String, String> tables = Map.of();

        mockMvc.perform(post("/tables/{restaurantId}", restaurant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tables)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(PARAMS_MISSING));
    }

    @Test
    void addTableApi_NonIntegerSeatsNumber_ReturnsBadRequestResponse() throws Exception {
        Restaurant restaurant = createSampleRestaurant();
        when(restaurantService.getRestaurant(anyInt())).thenReturn(restaurant);
        Map<String, String> tables = Map.of("seatsNumber", Double.toString(createSampleDoubleNumber()));

        mockMvc.perform(post("/tables/{restaurantId}", restaurant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tables)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(PARAMS_BAD_TYPE));
    }

    @Test
    void addTableApi_TableServiceThrowsException_ReturnsBadRequestResponse() throws Exception {
        Restaurant restaurant = createSampleRestaurant();
        when(restaurantService.getRestaurant(anyInt())).thenReturn(restaurant);
        UserNotManager exception = createSampleUserNotManagerException();
        doThrow(exception).when(tableService).addTable(anyInt(), anyInt());
        Map<String, String> tables = createSampleTableMap();

        mockMvc.perform(post("/tables/{restaurantId}", restaurant.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tables)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User is not a manager."));
    }
}
