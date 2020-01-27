
package bloogie.backend.validator;

import bloogie.backend.domain.Blog;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Class for validating Blog objects.
 * 
 * @author miika
 */

@Component
public class BlogValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return Blog.class.equals(clazz);
    }

    /**
     * Validates given Blog. Blogs name must be
     * within 5-100 characters. Also white space is no allowed.
     * 
     * @param o Object to validate
     * @param errors Validation errors
     */
    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "field.required");
        
        Blog b = (Blog) o;
        
        if (b.getName().trim().length() < 5) {
            errors.rejectValue("name", "name.short");
        } else if (b.getName().trim().length() > 100) {
            errors.rejectValue("name", "name.long");
        }
    }
    
}

