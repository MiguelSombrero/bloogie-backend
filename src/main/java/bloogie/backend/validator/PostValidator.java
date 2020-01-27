
package bloogie.backend.validator;

import bloogie.backend.domain.Post;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Class for validating Post objects. Note that these validation
 * constraints ARE NOT sufficient for production environment.
 * 
 * @author miika
 */

@Component
public class PostValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return Post.class.equals(clazz);
    }

    /**
     * Validates given Post. Title must be within 1-50 characters and
     * content within 5-5000 characters. Also white space is no allowed.
     * 
     * @param o Object to validate
     * @param errors Validation errors
     */
    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "field.required");
        
        Post b = (Post) o;
        
        if (b.getTitle().trim().length() < 1) {
            errors.rejectValue("title", "title.short");
        } else if (b.getTitle().trim().length() > 50) {
            errors.rejectValue("title", "title.long");
        }
        
        if (b.getContent().trim().length() < 5) {
            errors.rejectValue("content", "content.short");
        } else if (b.getContent().trim().length() > 5000) {
            errors.rejectValue("content", "content.long");
        }
    }
    
}

