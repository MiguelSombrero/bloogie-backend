
package bloogie.backend.validator;

import bloogie.backend.domain.Account;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author miika
 */

@Component
public class AccountValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return Account.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
        
        Account a = (Account) o;
        
        if (a.getName().trim().length() < 5) {
            errors.rejectValue("name", "name.short");
        } else if (a.getName().trim().length() > 20) {
            errors.rejectValue("name", "name.long");
        }
        
        if (a.getUsername().trim().length() < 5) {
            errors.rejectValue("username", "username.short");
        } else if (a.getUsername().trim().length() > 20) {
            errors.rejectValue("username", "username.long");
        }
        
        if (a.getPassword().trim().length() < 5) {
            errors.rejectValue("password", "password.short");
        } else if (a.getPassword().trim().length() > 20) {
            errors.rejectValue("password", "password.long");
        }
    }
    
}
