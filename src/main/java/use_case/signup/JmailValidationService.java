package use_case.signup;

import com.sanctionco.jmail.EmailValidator;
import com.sanctionco.jmail.JMail;

/**
 * Uses External library JMail to check for mail format validity.
 */
public class JmailValidationService implements EmailValidation {

    private static final EmailValidator VALIDATOR =
            JMail.strictValidator()
                    .requireAscii()
                    .requireTopLevelDomain()
                    .requireValidMXRecord(1000, 3);

    /**
     * Email Validation method.
     * Returns True if the email is valid, False otherwise.
     * @param email the email to be validated.
     */
    @Override
    public boolean isValid(String email) {
        return VALIDATOR.isValid(email);
    }
}
