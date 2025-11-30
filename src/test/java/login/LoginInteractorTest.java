package use_case.login;

import entity.User;
import entity.UserBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

// Simple test doubles

class FakeLoginPresenter implements LoginOutputBoundary {
    String lastFailure;
    LoginOutputData lastSuccess;

    @Override
    public void presentFailure(String error) {
        this.lastFailure = error;
        this.lastSuccess = null;
    }

    @Override
    public void presentSuccess(LoginOutputData outputData) {
        this.lastSuccess = outputData;
        this.lastFailure = null;
    }
}

class FakeLoginUserDataAccess implements LoginUserDataAccessInterface {
    boolean exists = false;
    User userToReturn = null;

    @Override
    public User get(String username, String password) {
        return userToReturn;
    }

    @Override
    public void save(User user) {
        // Not used in login, but required by interface
    }

    @Override
    public boolean existsByName(String username) {
        return exists;
    }
}

public class LoginInteractorTest {

    private FakeLoginUserDataAccess userDataAccess;
    private FakeLoginPresenter presenter;
    private LoginInteractor interactor;

    @Before
    public void setUp() {
        userDataAccess = new FakeLoginUserDataAccess();
        presenter = new FakeLoginPresenter();
        interactor = new LoginInteractor(userDataAccess, presenter);
    }

    // ========== Username Validation Tests ==========

    @Test
    public void execute_usernameNull_triggersUsernameError() {
        LoginInputData input = new LoginInputData(null, "password123");

        interactor.execute(input);

        assertEquals("Username cannot be empty", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_usernameBlank_triggersUsernameError() {
        LoginInputData input = new LoginInputData("   ", "password123");

        interactor.execute(input);

        assertEquals("Username cannot be empty", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_usernameEmpty_triggersUsernameError() {
        LoginInputData input = new LoginInputData("", "password123");

        interactor.execute(input);

        assertEquals("Username cannot be empty", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    // ========== Password Validation Tests ==========

    @Test
    public void execute_passwordNull_triggersPasswordError() {
        LoginInputData input = new LoginInputData("user", null);

        interactor.execute(input);

        assertEquals("Password cannot be empty", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_passwordBlank_triggersPasswordError() {
        LoginInputData input = new LoginInputData("user", "   ");

        interactor.execute(input);

        assertEquals("Password cannot be empty", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_passwordEmpty_triggersPasswordError() {
        LoginInputData input = new LoginInputData("user", "");

        interactor.execute(input);

        assertEquals("Password cannot be empty", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    // ========== User Not Found Tests ==========

    @Test
    public void execute_usernameDoesNotExist_triggersUserNotFoundError() {
        userDataAccess.exists = false;
        userDataAccess.userToReturn = null;

        LoginInputData input = new LoginInputData("nonexistentUser", "password123");

        interactor.execute(input);

        assertEquals("User not found", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    // ========== Wrong Password Tests ==========

    @Test
    public void execute_usernameExistsButPasswordWrong_triggersWrongPasswordError() {
        userDataAccess.exists = true;
        userDataAccess.userToReturn = null;  // Simulates wrong password

        LoginInputData input = new LoginInputData("existingUser", "wrongPassword");

        interactor.execute(input);

        assertEquals("Wrong username or password", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    // ========== Happy Path Tests ==========

    @Test
    public void execute_validCredentials_logsInSuccessfully() {
        User validUser = new UserBuilder()
                .withName("testUser")
                .withEmail("test@example.com")
                .withPassword("correctPassword")
                .build();

        userDataAccess.exists = true;
        userDataAccess.userToReturn = validUser;

        LoginInputData input = new LoginInputData("testUser", "correctPassword");

        interactor.execute(input);

        assertNull(presenter.lastFailure);
        assertNotNull(presenter.lastSuccess);
        assertEquals("testUser", presenter.lastSuccess.getUsername());
        assertTrue(presenter.lastSuccess.isSuccess());
    }

    @Test
    public void execute_validCredentialsWithDifferentUsername_logsInSuccessfully() {
        User validUser = new UserBuilder()
                .withName("anotherUser")
                .withEmail("another@example.com")
                .withPassword("myPassword")
                .build();

        userDataAccess.exists = true;
        userDataAccess.userToReturn = validUser;

        LoginInputData input = new LoginInputData("anotherUser", "myPassword");

        interactor.execute(input);

        assertNull(presenter.lastFailure);
        assertNotNull(presenter.lastSuccess);
        assertEquals("anotherUser", presenter.lastSuccess.getUsername());
        assertTrue(presenter.lastSuccess.isSuccess());
    }

    // ========== Edge Case: Username vs existsByName mismatch ==========

    @Test
    public void execute_userExistsButGetReturnsNull_triggersWrongPasswordError() {
        // This tests the branch where existsByName returns true but get returns null
        userDataAccess.exists = true;
        userDataAccess.userToReturn = null;

        LoginInputData input = new LoginInputData("existingUser", "password");

        interactor.execute(input);

        assertEquals("Wrong username or password", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    // ========== LoginInputData Tests ==========

    @Test
    public void loginInputData_getUsername_returnsCorrectUsername() {
        LoginInputData input = new LoginInputData("testUser", "password");

        assertEquals("testUser", input.getUsername());
    }

    @Test
    public void loginInputData_getPassword_returnsCorrectPassword() {
        LoginInputData input = new LoginInputData("testUser", "testPassword");

        assertEquals("testPassword", input.getPassword());
    }

    // ========== LoginOutputData Tests ==========

    @Test
    public void loginOutputData_getUsername_returnsCorrectUsername() {
        LoginOutputData output = new LoginOutputData("successUser", true);

        assertEquals("successUser", output.getUsername());
    }

    @Test
    public void loginOutputData_isSuccess_returnsTrue() {
        LoginOutputData output = new LoginOutputData("successUser", true);

        assertTrue(output.isSuccess());
    }

    @Test
    public void loginOutputData_isSuccess_returnsFalse() {
        LoginOutputData output = new LoginOutputData("failedUser", false);

        assertFalse(output.isSuccess());
    }

}

