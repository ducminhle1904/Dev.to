package dev.dl.common.helper;

import com.sun.istack.NotNull;
import dev.dl.common.exception.DLException;
import org.apache.logging.log4j.util.Strings;
import org.reflections.Reflections;
import org.springframework.http.HttpStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class ObjectHelper {

    public static final Integer MIN_LEN;
    public static final String EMPTY_STRING;

    static {
        MIN_LEN = 0;
        EMPTY_STRING = Strings.EMPTY;
    }

    /**
     * <p> merge {@code second} class to {@code first} class</p>
     * <ul>
     *     tạo ra 1 object mới, set các field của object đó là giá trị của các field trong {@code first}
     *     <li>- nếu như {@code nullReplace} là <i>true</i>: các field null của {@code first} được thay thế bằng các field của {@code second}</li>
     *     <li>- nếu như {@code nullReplace} là <i>false</i>: các field không null của {@code second} sẽ được thay thế vào các field của {@code first} cho dù field đó của {@code first} không null</li>
     * </ul>
     *
     * @param first
     * @param second
     * @param nullReplace
     * @param <T>
     * @return
     */
    public static <T> T mergeObjects(T first, @NotNull T second, boolean nullReplace) throws DLException {
        if (!first.getClass().equals(second.getClass())) {
            DLException exception = new DLException();
            exception.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            exception.setTimestamp(LocalDateTime.now());
            exception.setMessage(String.format("%1$s is not equal to %2$s", first.getClass(), second.getClass()));
            throw exception;
        }
        // get class of 'first'
        Class<?> clas = first.getClass();
        // get all fields (properties/attribute) of 'clas'
        List<Field> fields = new ArrayList<>(List.of(clas.getDeclaredFields()));
        // check if class has super class, then add all super field
        if (clas.getSuperclass() != null) {
            List<Field> superClassField = List.of(clas.getSuperclass().getDeclaredFields());
            fields.addAll(superClassField);
        }
        Object result = null;
        try {
            // put all fields of 'clas' to 'result'
            result = clas.getDeclaredConstructor().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                // get field value of 'first'
                Object value1 = field.get(first);
                // get field value of 'second'
                Object value2 = field.get(second);
                Object value;
                if (nullReplace) {
                    value = (value1 != null) ? value1 : value2;
                } else {
                    value = (value2 != null) ? value2 : value1;
                }
                field.set(result, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T) result;
    }

    /**
     * map các field của {@code source} vào các field của {@code target} có trùng tên với field của {@code source}
     *
     * @param source
     * @param target
     * @param <SOURCE>
     * @param <TARGET>
     * @return
     * @throws Exception
     */
    public static <SOURCE, TARGET> TARGET mapObjects(SOURCE source, Class<TARGET> target) throws Exception {
        // Get all field of source class and target class
        List<Field> sourceClassFields = new ArrayList<>(List.of(source.getClass().getDeclaredFields()));
        List<Field> targetClassFields = new ArrayList<>(List.of(target.getDeclaredFields()));
        // Get all field of super class of both
        if (source.getClass().getSuperclass() != null) {
            List<Field> superClassField = List.of(source.getClass().getSuperclass().getDeclaredFields());
            sourceClassFields.addAll(superClassField);
        }
        if (target.getSuperclass() != null) {
            List<Field> superClassField = List.of(target.getSuperclass().getDeclaredFields());
            targetClassFields.addAll(superClassField);
        }
        // Filter: only get fields in source that its name is included in target
        List<String> targetClassFieldNames = targetClassFields.stream().map(Field::getName).collect(Collectors.toList());
        sourceClassFields = sourceClassFields
                .stream()
                .filter(field -> targetClassFieldNames.contains(field.getName()))
                .collect(Collectors.toList());
        targetClassFields.sort(Comparator.comparing(Field::getName));
        sourceClassFields.sort(Comparator.comparing(Field::getName));
        TARGET result = target.getDeclaredConstructor().newInstance();
        // Start mapping
        // Lưu ý: khi dùng field.set(ObjectToSet, valueToSetToFeild) thì ObjectToSet phải là Object mà field đó thuộc về, nó hoạt động giống như setter,
        // tương tự với value = field.get(ObjectToGet), nó hoạt động giống như getter (phải dùng hàm field.setAccessible(true) trước)
        sourceClassFields.forEach(field -> {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(source);
                if (value != null) {
                    Field fieldToSet = target.getDeclaredField(field.getName());
                    fieldToSet.setAccessible(true);
                    fieldToSet.set(result, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    public static <T> T generateClassFromName(String className, Class<T> classType) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return classType.cast(Class.forName(className).getDeclaredConstructor().newInstance());
    }

    public void getIntegrationInfo() {
        Class<?> clazz = this.getClass();
        System.out.println("GETTING INTEGRATION INFO OF SERVICE: " + clazz.getSimpleName());
        System.out.println("GETTING INTEGRATION INFO OF SERVICE: " + clazz.getName());
    }

    public static Object getMethodName(Integer index) {
        if (index != null) {
            return new ArrayList<StackTraceElement>(List.of(Thread.currentThread().getStackTrace())).get(index);
        }
        return new ArrayList<StackTraceElement>(List.of(Thread.currentThread().getStackTrace()));
    }

    /**
     * Lấy tất cả các class trong 1 package
     *
     * @param packageName
     * @return
     */
    public static Set<Class<?>> findAllClassesUsingClassLoader(String packageName) throws IOException {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        if (stream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            try {
                return reader.lines()
                        .filter(line -> line.endsWith(".class"))
                        .map(line -> getClass(line, packageName))
                        .collect(Collectors.toSet());
            } finally {
                stream.close();
                reader.close();
            }
        }
        return new HashSet<>();
    }

    private static Class<?> getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }

    public static List<String> findPackageNamesStartingWith(String prefix) {
        return List.of(Package.getPackages()).stream()
                .map(Package::getName)
                .filter(n -> n.startsWith(prefix))
                .collect(Collectors.toList());
    }

    /**
     * Lấy tất cả sub-class của 1 interface hoặc (abstract) class
     *
     * @param superClass
     * @param <S>
     * @return
     */
    public static <S> List<Class<? extends S>> getAllSubClass(Class<S> superClass) {
        List<String> classNameInList = new ArrayList<>(List.of(superClass.getName().split("\\.")));
        classNameInList.remove(classNameInList.size() - 1);
        String packageName = String.join(".", classNameInList);
        Reflections reflections = new Reflections(packageName);
        return new ArrayList<>(reflections.getSubTypesOf(superClass));
    }

    public static boolean isNullOrEmpty(Object object) {
        if (Optional.ofNullable(object).isEmpty()) {
            return true;
        }
        if (object instanceof Iterable) {
            Collection<?> objectChecking = (Collection<?>) object;
            return objectChecking.isEmpty();
        }
        if (object instanceof String) {
            String objectChecking = (String) object;
            return objectChecking.trim().isEmpty();
        }
        return false;
    }

    public static boolean isNotNullAndNotEmpty(Object nonNullObject) {
        boolean isNotNull = Optional.ofNullable(nonNullObject).isPresent();
        boolean isNotEmpty = true;
        if (nonNullObject instanceof Iterable) {
            Collection<?> objectChecking = (Collection<?>) nonNullObject;
            isNotEmpty = !objectChecking.isEmpty();
        } else if (nonNullObject instanceof String) {
            String objectChecking = (String) nonNullObject;
            isNotEmpty = !objectChecking.trim().isEmpty();
        }
        return isNotNull && isNotEmpty;
    }

    public static boolean objectIsNull(Object object) {
        return Optional.ofNullable(object).isEmpty();
    }

    public static boolean objectIsNotNull(Object object) {
        return Optional.ofNullable(object).isPresent();
    }

    public static boolean objectIsEmpty(Object object) {
        if (object instanceof Iterable) {
            Collection<?> objectChecking = (Collection<?>) object;
            return objectChecking.isEmpty();
        }
        if (object instanceof String) {
            String objectChecking = (String) object;
            return objectChecking.trim().isEmpty();
        }
        return false;
    }

    public static boolean objectIsNotEmpty(Object object) {
        if (object instanceof Iterable) {
            Collection<?> objectChecking = (Collection<?>) object;
            return !objectChecking.isEmpty();
        }
        if (object instanceof String) {
            String objectChecking = (String) object;
            return !objectChecking.trim().isEmpty();
        }
        return false;
    }

}
