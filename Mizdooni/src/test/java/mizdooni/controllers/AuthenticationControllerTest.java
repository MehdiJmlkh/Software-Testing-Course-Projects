package mizdooni.controllers;

import mizdooni.model.User;
import mizdooni.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static mizdooni.utils.CreateSample.createSampleEmail;
import static org.mockito.Mockito.when;
import mizdooni.exceptions.*;
import mizdooni.response.ResponseException;
import mizdooni.response.Response;

import static mizdooni.controllers.ControllerUtils.*;
import static mizdooni.utils.CreateSample.*;
import static mizdooni.utils.CustomAssertions.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

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
    public void getUser_NoCurrentUser_ThrowsException() {
        when(userService.getCurrentUser()).thenReturn(null);

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.user();
        });

        ResponseException expected = new ResponseException(HttpStatus.UNAUTHORIZED, "no user logged in");
        assertEquals(expected, result);
    }

    @Test
    public void login_ValidParameters_ReturnsOkResponse() {
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
    public void login_UsernameMissing_ThrowsException() {
        Map<String, String> params = Map.of("password", createSampleNonBlankString());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.login(params);
        });

        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, PARAMS_MISSING);
        assertEquals(expected, result);
    }

    @Test
    public void login_PasswordMissing_ThrowsException() {
        Map<String, String> params = Map.of("username", createSampleNonBlankString());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.login(params);
        });

        ResponseException expected = new ResponseException(HttpStatus.BAD_REQUEST, PARAMS_MISSING);
        assertEquals(expected, result);
    }

    @Test
    public void login_UnauthorizedUser_ThrowsException() {
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
    public void signup_ValidParameters_CallsMethodsOfUserServiceCorrectly() throws DuplicatedUsernameEmail, InvalidUsernameFormat, InvalidEmailFormat {
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
    public void signup_ValidParameters_ReturnsOkResponse() {
        Map<String, Object> params = Map.of("username",  createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role",  createSampleRole());

        User user = createSampleUser();
        when(userService.getCurrentUser()).thenReturn(user);

        Response result = authenticationController.signup(params);

        Response expected = Response.ok("signup successful", user);
        assertEquals(expected, result);
    }

    @Test
    public void signup_UsernameMissing_ThrowsException() {
        Map<String, Object> params = Map.of(
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role",  createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_PasswordMissing_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role",  createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_EmailMissing_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "address", createSampleMapAddress(),
                "role",  createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_AddressMissing_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "role",  createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_RoleMissing_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_BadTypeUsername_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSamplePositiveNumber(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void signup_BadTypePassword_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUser(),
                "password", createSamplePositiveNumber(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void signup_BadTypeEmail_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSamplePositiveNumber(),
                "address", createSampleMapAddress(),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void signup_BadTypeAddress_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", createSamplePositiveNumber(),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void signup_BadTypeRole_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role", createSamplePositiveNumber());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void signup_BlankUsername_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleBlankString(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_BlankPassword_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSampleBlankString(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_BlankEmail_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleBlankString(),
                "address", createSampleMapAddress(),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_BlankCountry_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", Map.of(
                        "country", createSampleBlankString(),
                        "city", createSampleNonBlankString(),
                        "street", createSampleNonBlankString()),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_BlankCity_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", Map.of(
                        "country", createSampleNonBlankString(),
                        "city", createSampleBlankString(),
                        "street", createSampleNonBlankString()),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_BlankStreet_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", Map.of(
                        "country", createSampleNonBlankString(),
                        "city", createSampleNonBlankString(),
                        "street", createSampleBlankString()),
                "role", createSampleRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_MISSING, result.getMessage());
    }

    @Test
    public void signup_NotExistedRole_ThrowsException() {
        Map<String, Object> params = Map.of(
                "username", createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role", createSampleNotExistedRole());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(PARAMS_BAD_TYPE, result.getMessage());
    }

    @Test
    public void signup_UserServiceThrowsException_ThrowsException() throws DuplicatedUsernameEmail, InvalidUsernameFormat, InvalidEmailFormat {
        Map<String, Object> params = Map.of("username",  createSampleUsername(),
                "password", createSamplePassword(),
                "email", createSampleEmail(),
                "address", createSampleMapAddress(),
                "role",  createSampleRole());

        DuplicatedUsernameEmail exception = createSampleDuplicatedUsernameEmailException();
        doThrow(exception).when(userService).signup(any(), any(), any(), any(), any());

        ResponseException result = assertThrows(ResponseException.class, () -> {
            authenticationController.signup(params);
        });

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatus());
        assertEquals(exception.getMessage(), result.getMessage());
        assertEquals(exception.getClass().getSimpleName(), result.getError());
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
