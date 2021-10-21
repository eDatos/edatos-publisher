package es.gobcan.istac.edatos.publisher.core.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.FieldUtils;

public class CriteriaUtil {

    private final Class<?> clazz;

    public CriteriaUtil(Class<?> clazz) {
        this.clazz = clazz;
    }

    /**
     * Tells if a field name exists for a class.
     * <p>
     * The reasoning behind this method is that it fails quickly if the declared
     * entity field name doesn't exists, either because it was changed, mispelled, or
     * another reason that may cause to fail silently in production otherwise, if not
     * correctly tested.
     * <p>
     * Be aware that to fail at startup this class need to be instantiated early.
     * Marking this as a {@code @Component} will delegate this to Spring.
     *
     * @implNote Limitations: this method DOES NOT check for nested entity properties
     *           like {@code user.login}.
     * @param fieldName the name of the field that is going to be checked.
     * @return the same field name passed as argument, if it exists.
     * @throws IllegalArgumentException if the field doesn't exists.
     */
    public String validateFieldExists(String fieldName) {
        List<String> fieldNames = FieldUtils.getAllFieldsList(clazz).stream().map(Field::getName).collect(Collectors.toList());
        if (fieldNames.contains(fieldName)) {
            return fieldName;
        } else {
            throw new IllegalArgumentException("Field name '" + fieldName + "' doesn't exists in '" + clazz.toString() + "'");
        }
    }
}
