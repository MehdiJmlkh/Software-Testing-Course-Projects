package mizdooni.controllers;

import mizdooni.model.Address;
import mizdooni.model.User;
import mizdooni.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.when;
import mizdooni.exceptions.*;
import mizdooni.model.Reservation;
import mizdooni.model.Restaurant;
import mizdooni.response.ResponseException;
import mizdooni.service.ReservationService;
import mizdooni.service.RestaurantService;
import mizdooni.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static mizdooni.controllers.ControllerUtils.*;
import static mizdooni.utils.CreateSample.*;
import static mizdooni.utils.CustomAssertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void getUser_UserExists_ReturnsOkResponse() {
        User user = createSampleUser();
        when(userService.getCurrentUser()).thenReturn(user);

        Response result = authenticationController.user();

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("current user", result.getMessage());
        assertEquals(user, result.getData());
    }

    @Test
    public void getUser_NoUser_ThrowsException() {
        when(userService.getCurrentUser()).thenReturn(null);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.user();
        });

        ResponseException expected = new ResponseException(HttpStatus.UNAUTHORIZED, "no user logged in");
        assertEquals(expected, result);
    }

    @Test
    public void login_ValidArgs_ReturnsOkResponse() {
        User user = createSampleUser();
        Map<String, String> params = Map.of("username", createSampleNonBlankString(),
                                            "password", createSampleNonBlankString());
        when(userService.login(params.get("username"), params.get("password"))).thenReturn(true);
        when(userService.getCurrentUser()).thenReturn(user);

        Response result = authenticationController.login(params);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("login successful", result.getMessage());
        assertEquals(user, result.getData());
    }

    @Test
    public void login_ParamsMissing_ThrowsException() {
        Map<String, String> params = Map.of("password", createSampleNonBlankString());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.login(params);
        });

        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, PARAMS_MISSING);
        assertEquals(expected, result);
    }

    @Test
    public void login_InvalidParams_ThrowsException() {
        Map<String, String> params = Map.of("password", createSampleNonBlankString(),
                                            "username", createSampleNonBlankString());

        when(userService.login(any(), any())).thenReturn(false);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.login(params);
        });

        ResponseException expected = new ResponseException(HttpStatus.UNAUTHORIZED, "invalid username or password");
        assertEquals(expected, result);
    }

    @Test
    public void signup_ValidArgs_CallsMethodOfUserServiceCorrectly() throws DuplicatedUsernameEmail, InvalidUsernameFormat, InvalidEmailFormat {
        Map<String, String> mapAddress = createSampleMapAddress();
        String password = createSamplePassword();
        String username = createSampleUsername();
        String email = createSampleEmail();
        String role = createSampleRole();
        Map<String, Object> params = Map.of("username", username,
                                            "password", password,
                                            "email", email,
                                            "address", mapAddress,
                                            "role", role);

        authenticationController.signup(params);

        InOrder inOrder = inOrder(userService);
        inOrder.verify(userService).signup(eq(username), eq(password), eq(email),
                argThat(address ->
                        address.getCountry().equals(mapAddress.get("country")) &&
                        address.getCity().equals(mapAddress.get("city")) &&
                        address.getStreet().equals(mapAddress.get("street"))
                ),
                eq(User.Role.valueOf(role))
        );
        inOrder.verify(userService).login(eq(username), eq(password));
    }

    @Test
    public void signup_ValidArgs_ReturnsOkResponse() throws DuplicatedUsernameEmail, InvalidUsernameFormat, InvalidEmailFormat {
        Map<String, String> mapAddress = createSampleMapAddress();
        String password = createSamplePassword();
        String username = createSampleUsername();
        String email = createSampleEmail();
        String role = createSampleRole();
        Map<String, Object> params = Map.of("username", username,
                "password", password,
                "email", email,
                "address", mapAddress,
                "role", role);

        User user = createSampleUser();
        when(userService.getCurrentUser()).thenReturn(user);

        Response result = authenticationController.signup(params);

        Response expected = Response.ok("signup successful", user);
        assertEquals(expected, result);
    }

    @Test
    public void logout_LoggedInUser_ReturnsOkResponse() {
        when(userService.logout()).thenReturn(true);

        Response result = authenticationController.logout();

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("logout successful", result.getMessage());
    }

    @Test
    public void logout_NonLoggedInUser_ThrowsException() {
        when(userService.logout()).thenReturn(false);

        ResponseException result =assertThrows(ResponseException.class, () -> {
            authenticationController.logout();
        });

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatus());
        assertEquals("no user logged in", result.getMessage());
    }

    @Test
    public void validateUsername_UsernameIsValidAndAvailable_ReturnsOkResponse() {
        String username = createSampleUsername();
        when(userService.usernameExists(username)).thenReturn(false);

        Response result = authenticationController.validateUsername(username);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("username is available", result.getMessage());
    }

    @Test
    public void validateUsername_UsernameIsValidAndNotAvailable_ThrowsException() {
        String username = createSampleUsername();
        when(userService.usernameExists(any())).thenReturn(true);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.validateUsername(username);
        });

        assertEquals(HttpStatus.CONFLICT, result.getStatus());
        assertEquals("username already exists", result.getMessage());
    }

    @Test
    public void validateUsername_UsernameIsInvalid_ThrowsException() {
        String username = createSampleInvalidUsername();

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.validateUsername(username);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("invalid username format", result.getMessage());
    }

    @Test
    public void validateEmail_EmailIsValidAndAvailable_ReturnsOkResponse() {
        String email = createSampleEmail();
        when(userService.emailExists(email)).thenReturn(false);

        Response result = authenticationController.validateEmail(email);

        assertEquals(HttpStatus.OK, result.getStatus());
        assertEquals("email not registered", result.getMessage());
    }

    @Test
    public void validateEmail_EmailIsValidAndNotAvailable_ThrowsException() {
        String email = createSampleEmail();
        when(userService.emailExists(any())).thenReturn(true);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.validateEmail(email);
        });

        assertEquals(HttpStatus.CONFLICT, result.getStatus());
        assertEquals("email already registered", result.getMessage());
    }

    @Test
    public void validateEmail_UsernameIsInvalid_ThrowsException() {
        String email = createSampleInvalidEmail();

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.validateEmail(email);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals("invalid email format", result.getMessage());
    }


}
