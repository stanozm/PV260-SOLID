package pv260.solid.lsp.original;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import static pv260.solid.lsp.original.CSVSerializer.LambdaTypeAdapter.inline;

public class CSVSerializer implements SimpleSerializer {

    private static final Pattern EXTRACT_VALUE = Pattern.compile("[^,\\[\\]]+|\\[[^\\]]+\\]");

    private static final Pattern DROP_SEPARATOR = Pattern.compile(",");

    private static final Map<Class, Object> DEFAULTS;

    static {
        Map<Class, Object> temp = new HashMap<>();
        temp.put(byte.class, 0);
        temp.put(short.class, 0);
        temp.put(int.class, 0);
        temp.put(long.class, 0);
        temp.put(float.class, 0);
        temp.put(double.class, 0);
        temp.put(boolean.class, false);
        temp.put(Object.class, null);
        DEFAULTS = Collections.unmodifiableMap(temp);
    }

    private static final Map<Class, TypeAdapter> ADAPTERS;

    static {
        Map<Class, TypeAdapter> temp = new HashMap<>();
        temp.put(byte.class, inline(((field, target, serializedValue) -> field.setByte(target, Byte.parseByte(serializedValue))),
                                    (array, idx, serializedValue) -> Array.setByte(array, idx, Byte.parseByte(serializedValue))));
        temp.put(short.class, inline(((field, target, serializedValue) -> field.setShort(target, Short.parseShort(serializedValue))),
                                     (array, idx, serializedValue) -> Array.setShort(array, idx, Short.parseShort(serializedValue))));
        temp.put(int.class, inline(((field, target, serializedValue) -> field.setInt(target, Integer.parseInt(serializedValue))),
                                   (array, idx, serializedValue) -> Array.setInt(array, idx, Integer.parseInt(serializedValue))));
        temp.put(long.class, inline(((field, target, serializedValue) -> field.setLong(target, Long.parseLong(serializedValue))),
                                    (array, idx, serializedValue) -> Array.setLong(array, idx, Long.parseLong(serializedValue))));
        temp.put(float.class, inline(((field, target, serializedValue) -> field.setFloat(target, Float.parseFloat(serializedValue))),
                                     (array, idx, serializedValue) -> Array.setFloat(array, idx, Float.parseFloat(serializedValue))));
        temp.put(double.class, inline(((field, target, serializedValue) -> field.setDouble(target, Double.parseDouble(serializedValue))),
                                      (array, idx, serializedValue) -> Array.setDouble(array, idx, Double.parseDouble(serializedValue))));
        temp.put(boolean.class, inline(((field, target, serializedValue) -> field.setBoolean(target, Boolean.parseBoolean(serializedValue))),
                                       (array, idx, serializedValue) -> Array.setBoolean(array, idx, Boolean.parseBoolean(serializedValue))));
        temp.put(String.class, inline(((field, target, serializedValue) -> field.set(target, serializedValue)),
                                      (array, idx, serializedValue) -> Array.set(array, idx, serializedValue)));
        ADAPTERS = Collections.unmodifiableMap(temp);
    }

    private static final TypeAdapter ARRAY_ADAPTER = new TypeAdapter() {

        @Override
        public void setField(Field field, Object target, String joinedSerializedValue) throws IllegalAccessException {
            Class<?> componentType = field.getType().getComponentType();
            TypeAdapter componentAdapter = ADAPTERS.get(componentType);
            String[] splitSerializedValues = joinedSerializedValue.substring(1, joinedSerializedValue.length() - 1)
                                                                  .split(",");
            field.setAccessible(true);
            field.set(target, Array.newInstance(componentType, splitSerializedValues.length));
            for (int i = 0; i < splitSerializedValues.length; i++) {
                componentAdapter.setArrayElement(forceGet(field, target), i, splitSerializedValues[i]);
            }
        }

        @Override
        public void setArrayElement(Object array, int idx, String serializedValue) throws IllegalAccessException {
            throw new UnsupportedOperationException("Netsed arrays are not suppoerted");
        }
    };

    private StringBuilder serializedForm;

    private Scanner scan;

    @Override
    public void serializeInto(Object instance, OutputStream into) throws IOException {
        try {
            this.serializedForm = new StringBuilder();
            this.appendValue(instance.getClass().getName());
            for (Field field : instance.getClass().getDeclaredFields()) {
                if (shouldTouch(field)) {
                    Object value = forceGet(field, instance);
                    this.appendValue(serializeValue(value));
                }
            }
            into.write(this.serializedForm.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static String serializeValue(Object value) throws IllegalAccessException {
        if (value.getClass().isArray()) {
            StringBuilder arrayBuilder = new StringBuilder();
            serializeArray(value, arrayBuilder);
            return arrayBuilder.toString();
        }
        return value.toString();
    }

    private static void serializeArray(Object array, StringBuilder arrayBuilder) throws IllegalAccessException {
        arrayBuilder.append('[');
        for (int i = 0; i < Array.getLength(array); i++) {
            if (i != 0) {
                arrayBuilder.append(',');
            }
            arrayBuilder.append(serializeValue(Array.get(array, i)));
        }
        arrayBuilder.append(']');
    }

    @Override
    public Object deserializeFrom(InputStream from) throws IOException {
        try {
            this.scan = new Scanner(from);
            Class<?> type = Class.forName(nextValue());
            Object instance = forceCreate(type);
            for (Field field : type.getDeclaredFields()) {
                if (shouldTouch(field)) {
                    field.setAccessible(true);
                    adapterFor(field.getType())
                                               .setField(field,
                                                         instance,
                                                         this.nextValue());
                }
            }
            return instance;
        } catch (ClassNotFoundException
                 | InstantiationException
                 | IllegalAccessException
                 | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        return "CSV";
    }

    private void appendValue(Object value) {
        this.serializedForm.append(value).append(',');
    }

    private String nextValue() {
        String value = scan.findInLine(EXTRACT_VALUE);
        scan.findInLine(DROP_SEPARATOR);
        return value;
    }

    private boolean shouldTouch(Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }

    private static Object forceCreate(Class<?> type) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor ctor = type.getConstructors()[0];
        ctor.setAccessible(true);
        Class[] params = ctor.getParameterTypes();
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            args[i] = DEFAULTS.get(rootSupertype(params[i]));
        }
        return ctor.newInstance(args);
    }

    private static Object forceGet(Field field, Object target) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(target);
    }

    private static Class<?> rootSupertype(final Class<?> of) {
        Class<?> at = of;
        if (at.getSuperclass() != null) {
            at = at.getSuperclass();
        }
        return at;
    }

    private static TypeAdapter adapterFor(Class<?> type) {
        if (type.isArray()) {
            return ARRAY_ADAPTER;
        }
        return ADAPTERS.get(type);
    }

    interface TypeAdapter {

        /**
         * @param field guaranteed to be accessible
         * @param target to set the field on
         * @param serializedValue serialized value of the field, adapter parses this to type of field
         */
        void setField(Field field, Object target, String serializedValue) throws IllegalAccessException;

        /**
         * @param array to set the element to
         * @param idx of the element to set
         * @param serializedValue to set, adapter parses this to type of array
         */
        void setArrayElement(Object array, int idx, String serializedValue) throws IllegalAccessException;
    }

    static class LambdaTypeAdapter implements TypeAdapter {

        private final TriConsumer<Field, Object, String> setFieldMethod;

        private final TriConsumer<Object, Integer, String> setArrayElementMethod;

        public LambdaTypeAdapter(TriConsumer<Field, Object, String> setFieldMethod,
                TriConsumer<Object, Integer, String> setArrayElementMethod) {
            this.setFieldMethod = setFieldMethod;
            this.setArrayElementMethod = setArrayElementMethod;
        }

        public static LambdaTypeAdapter inline(TriConsumer<Field, Object, String> setFieldMethod,
                TriConsumer<Object, Integer, String> setArrayElementMethod) {
            return new LambdaTypeAdapter(setFieldMethod, setArrayElementMethod);
        }

        @Override
        public void setField(Field field, Object target, String serializedValue) throws IllegalAccessException {
            this.setFieldMethod.accept(field, target, serializedValue);
        }

        @Override
        public void setArrayElement(Object array, int idx, String serializedValue) throws IllegalAccessException {
            this.setArrayElementMethod.accept(array, idx, serializedValue);
        }
    }

    /**
     * Same as standard Java Consumer and BiConsumer, but for three arguments
     */
    @FunctionalInterface
    interface TriConsumer<T, U, V> {

        //the throws is here for convenience so we dont have to care about it and handle it
        //this would not be a good idea for general purpose TriConsumer
        void accept(T t, U u, V v) throws IllegalAccessException;
    }
}
