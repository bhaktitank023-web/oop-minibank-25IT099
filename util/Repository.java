
package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import model.annotation.Id;

public class Repository<T> {

    private final HashMap<String, T> storage = new HashMap<>();

    // Save an entity using its @Id field
    public T save(T entity) {

        if (entity == null) {
            throw new IllegalArgumentException(
                    "Entity cannot be null."
            );
        }

        try {
            Class<?> currentClass = entity.getClass();

            while (currentClass != null) {

                for (Field field : currentClass.getDeclaredFields()) {

                    if (field.isAnnotationPresent(Id.class)) {

                        field.setAccessible(true);

                        Object idValue = field.get(entity);

                        if (idValue == null) {
                            throw new IllegalArgumentException(
                                    "@Id field cannot be null."
                            );
                        }

                        String id = idValue.toString();

                        storage.put(id, entity);

                        return entity;
                    }
                }

                currentClass = currentClass.getSuperclass();
            }

            throw new IllegalArgumentException(
                    "No field with @Id annotation found."
            );

        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                    "Unable to access @Id field.", e
            );
        }
    }

    // Find entity using ID
    public T findById(String id) {
        return storage.get(id);
    }

    // Return all stored entities
    public ArrayList<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    // Delete entity using ID
    public boolean delete(String id) {
        return storage.remove(id) != null;
    }

    // Save multiple entities
    @SafeVarargs
    public final void saveAll(T... items) {

        for (T item : items) {
            save(item);
        }
    }

    // Return number of stored entities
    public int count() {
        return storage.size();
    }

    // Bounded generic method
    public static <N extends Number> double average(N[] values) {

        if (values == null || values.length == 0) {
            throw new IllegalArgumentException(
                    "Array cannot be null or empty."
            );
        }

        double sum = 0;

        for (N value : values) {
            sum += value.doubleValue();
        }

        return sum / values.length;
    }
}
