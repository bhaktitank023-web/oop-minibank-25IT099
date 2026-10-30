
package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import model.annotation.Id;
import model.annotation.MaxLength;
import model.annotation.Positive;

public class AnnotationValidator {

    public static String[] validate(Object obj) {

        List<String> errors = new ArrayList<>();

        Class<?> currentClass = obj.getClass();

        while (currentClass != null
                && currentClass != Object.class) {

            Field[] fields = currentClass.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);

                try {
                    Object value = field.get(obj);

                    // Check @Id
                    if (field.isAnnotationPresent(Id.class)) {
                        if (value == null
                                || value.toString().isBlank()) {
                            errors.add(
                                    field.getName() + " must not be empty"
                            );
                        }
                    }

                    // Check @Positive
                    if (field.isAnnotationPresent(Positive.class)) {

                        Positive annotation =
                                field.getAnnotation(Positive.class);

                        if (value instanceof Number number
                                && number.doubleValue() <= 0) {

                            errors.add(annotation.message());
                        }
                    }

                    // Check @MaxLength
                    if (field.isAnnotationPresent(MaxLength.class)) {

                        MaxLength annotation =
                                field.getAnnotation(MaxLength.class);

                        if (value instanceof String text
                                && text.length() > annotation.value()) {

                            errors.add(
                                    field.getName()
                                    + " exceeds maximum length "
                                    + annotation.value()
                            );
                        }
                    }

                } catch (IllegalAccessException e) {
                    errors.add(
                            "Unable to access field: " + field.getName()
                    );
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return errors.toArray(new String[0]);
    }
}
