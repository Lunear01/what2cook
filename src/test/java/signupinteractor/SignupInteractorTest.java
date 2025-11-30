package use_case.signup;

import entity.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

// Simple test doubles

class FakeSignupPresenter implements SignupOutputBoundary {
    String lastFailure;
    SignupOutputData lastSuccess;

    @Override
    public void presentFailure(String error) {
        this.lastFailure = error;
        this.lastSuccess = null;
    }

    @Override
    public void presentSuccess(SignupOutputData outputData) {
        this.lastSuccess = outputData;
        this.lastFailure = null;
    }
}

class FakeEmailValidation implements EmailValidation {
    boolean valid = true;

    @Override
    public boolean isValid(String email) {
        return valid;
    }
}

class FakePasswordValidation implements PasswordValidation {
    boolean strong = true;

    @Override
    public boolean isStrong(String password) {
        return strong;
    }
}

class FakeSignupUserDataAccess implements SignupUserDataAccessInterface {
    boolean exists = false;
    User savedUser;

    @Override
    public boolean existsByName(String username) {
        return exists;
    }

    @Override
    public void save(User user) {
        this.savedUser = user;
    }
}

public class SignupInteractorTest {

    private FakeSignupUserDataAccess userData;
    private FakeSignupPresenter presenter;
    private FakeEmailValidation emailValidation;
    private FakePasswordValidation passwordValidation;
    private SignupInteractor interactor;

    @Before
    public void setUp() {
        userData = new FakeSignupUserDataAccess();
        presenter = new FakeSignupPresenter();
        emailValidation = new FakeEmailValidation();
        passwordValidation = new FakePasswordValidation();
        interactor = new SignupInteractor(userData, presenter, emailValidation, passwordValidation);
    }

    // 1. username null / empty

    @Test
    public void execute_usernameNull_triggersUsernameError() {
        SignupInputData input = new SignupInputData(
                null, "a@b.com", "Password1!", "Password1!"
        );

        interactor.execute(input);

        assertEquals("Username cannot be empty", presenter.lastFailure);
        assertNull(presenter.lastSuccess);
    }

    @Test
    public void execute_usernameBlank_triggersUsernameError() {
        SignupInputData input = new SignupInputData(
                "   ", "a@b.com", "Password1!", "Password1!"
        );

        interactor.execute(input);

        assertEquals("Username cannot be empty", presenter.lastFailure);
    }

    // 2. password null / empty

    @Test
    public void execute_passwordNull_triggersPasswordError() {
        SignupInputData input = new SignupInputData(
                "user", "a@b.com", null, "anything"
        );

        interactor.execute(input);

        assertEquals("Password cannot be empty", presenter.lastFailure);
    }

    @Test
    public void execute_passwordBlank_triggersPasswordError() {
        SignupInputData input = new SignupInputData(
                "user", "a@b.com", "   ", "   "
        );

        interactor.execute(input);

        assertEquals("Password cannot be empty", presenter.lastFailure);
    }

    // 3. weak password

    @Test
    public void execute_weakPassword_triggersStrengthMessage() {
        passwordValidation.strong = false;

        SignupInputData input = new SignupInputData(
                "user", "a@b.com", "weak", "weak"
        );

        interactor.execute(input);

        assertNotNull(presenter.lastFailure);
        assertTrue(presenter.lastFailure.contains("The password must include"));
    }

    // 4. invalid email

    @Test
    public void execute_invalidEmail_triggersEmailError() {
        passwordValidation.strong = true;
        emailValidation.valid = false;

        SignupInputData input = new SignupInputData(
                "user", "not-an-email", "Password1!", "Password1!"
        );

        interactor.execute(input);

        assertEquals("Invalid email format", presenter.lastFailure);
    }

    // 5. passwords do not match

    @Test
    public void execute_passwordsDoNotMatch_triggersMismatchError() {
        passwordValidation.strong = true;
        emailValidation.valid = true;

        SignupInputData input = new SignupInputData(
                "user", "a@b.com", "Password1!", "Different1!"
        );

        interactor.execute(input);

        assertEquals("Passwords do not match", presenter.lastFailure);
    }

    // 6. username already exists

    @Test
    public void execute_usernameAlreadyExists_triggersExistsError() {
        passwordValidation.strong = true;
        emailValidation.valid = true;
        userData.exists = true;

        SignupInputData input = new SignupInputData(
                "existingUser", "a@b.com", "Password1!", "Password1!"
        );

        interactor.execute(input);

        assertEquals("Username already exists", presenter.lastFailure);
        assertNull(userData.savedUser);
    }

    // 7. happy path

    @Test
    public void execute_validInput_savesUser_andPresentsSuccess() {
        passwordValidation.strong = true;
        emailValidation.valid = true;
        userData.exists = false;

        SignupInputData input = new SignupInputData(
                "newUser", "new@user.com", "Password1!", "Password1!"
        );

        interactor.execute(input);

        // presenter called with success
        assertNull(presenter.lastFailure);
        assertNotNull(presenter.lastSuccess);
        assertEquals("newUser", presenter.lastSuccess.getUsername());
        assertTrue(presenter.lastSuccess.isCreated());

        // user saved via DAO
        assertNotNull(userData.savedUser);
        assertEquals("newUser", userData.savedUser.getName());
        assertEquals("new@user.com", userData.savedUser.getEmail());
    }

    // ---------- PasswordValidationService tests ----------

    @Test
    public void passwordService_strongAndWeakCases() {
        PasswordValidationService service = new PasswordValidationService();

        assertTrue(service.isStrong("Abcdef1!"));   // strong
        assertFalse(service.isStrong("Ab1!a"));      // too short
        assertFalse(service.isStrong("abcdef1!"));   // no upper
        assertFalse(service.isStrong("ABCDEF1!"));   // no lower
        assertFalse(service.isStrong("Abcdefg!"));   // no digit
        assertFalse(service.isStrong("Abcdef12"));   // no special
        assertFalse(service.isStrong(null));         // null
    }

    // ---------- JmailValidationService tests ----------

    @Test
    public void jmailService_validAndInvalidEmails() {
        JmailValidationService service = new JmailValidationService();

        assertTrue(service.isValid("user@example.com"));
        assertFalse(service.isValid("userexample.com"));
        assertFalse(service.isValid("user@example"));
    }

}
