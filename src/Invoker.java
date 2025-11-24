import java.lang.reflect.Method;

@SuppressWarnings("java:S106") // Подавляем предупреждение для всего класса
public class Invoker {
    public void invokeAnnotatedMethods() {
        try {
            MyClass myObject = new MyClass();
            Class<?> myClass = myObject.getClass();
            System.out.println("=== ВЫЗОВ АННОТИРОВАННЫХ МЕТОДОВ ===");

            // Получаем все методы класса
            Method[] methods = myClass.getDeclaredMethods();
            for (Method method : methods) {
                // Проверяем, есть ли аннотация @Repeat
                if (method.isAnnotationPresent(Repeat.class)) {
                    Repeat repeatAnnotation = method.getAnnotation(Repeat.class);
                    int times = repeatAnnotation.times();
                    System.out.println("\nМетод: " + method.getName());
                    System.out.println("Модификатор: " + getAccessModifier(method));
                    System.out.println("Аннотация @Repeat(times=" + times + ")");
                    // Разрешаем доступ к приватным и защищенным методам
                    method.setAccessible(true);
                    // Вызываем метод указанное количество раз
                    for (int i = 0; i < times; i++) {
                        System.out.print("Вызов " + (i + 1) + ": ");
                        // Обрабатываем методы с параметрами и без
                        if (method.getParameterCount() == 0) {
                            // Метод без параметров
                            method.invoke(myObject);
                        } else {
                            // Метод с параметрами - передаем тестовые значения
                            callMethodWithParameters(method, myObject);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Ошибка при вызове метода: " + e.getMessage());
        }
    }

    private void callMethodWithParameters(Method method, Object target) throws Exception {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        // Заполняем параметры тестовыми значениями в зависимости от типа
        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == String.class) {
                parameters[i] = "тест";
            } else if (parameterTypes[i] == int.class) {
                parameters[i] = i + 1;
            } else if (parameterTypes[i] == double.class) {
                parameters[i] = (i + 1) * 1.5;
            } else {
                parameters[i] = null;
            }
        }
        method.invoke(target, parameters);
    }

    private String getAccessModifier(Method method) {
        if (java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
            return "public";
        } else if (java.lang.reflect.Modifier.isProtected(method.getModifiers())) {
            return "protected";
        } else if (java.lang.reflect.Modifier.isPrivate(method.getModifiers())) {
            return "private";
        } else {
            return "package-private";
        }
    }
}