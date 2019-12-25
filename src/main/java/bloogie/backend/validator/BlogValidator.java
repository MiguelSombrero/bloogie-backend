
package bloogie.backend.validator;

import bloogie.backend.domain.Blog;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author miika
 */

@Component
public class BlogValidator implements Validator {

    @Override
    public boolean supports(Class clazz) {
        return Blog.class.equals(clazz);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "content", "field.required");
        
        Blog b = (Blog) o;
        
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

