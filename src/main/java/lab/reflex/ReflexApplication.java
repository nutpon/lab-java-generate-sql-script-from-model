package lab.reflex;

import jakarta.persistence.Column;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class ReflexApplication {

	public static void main(String[] args) {

        try {
            MyClass model = new MyClass();
            model.setPrivateField("test");
//            GenerateScript(model);

             System.out.println(GenerateColumns(model,new String[]{"publicField"}));
            System.out.println(GenerateColumnsWithAliasName(model,new String[]{"publicField"},"tms"));

//            // 1. รับ Class Object
//            Class<?> myClass = MyClass.class; // รับ Class Object จากชื่อคลาส (Fully Qualified Name)
//            // หรือ Class<MyClass> myClass = MyClass.class; // ถ้าคลาสมีใน Compile-time
//            // หรือ MyClass obj = new MyClass("test"); Class<?> myClass = obj.getClass();
//
//            System.out.println("Class Name: " + myClass.getName());
//
//            // 2. เข้าถึง Constructors และสร้าง Instance
//            Constructor<?> constructor = myClass.getConstructor(String.class); // รับ Constructor ที่รับ String
//            MyClass instance = (MyClass) constructor.newInstance("Value via Reflection");
//            System.out.println("Instance created. privateField: " + instance.privateField); // ยังเข้าถึงไม่ได้ตรงๆ เพราะ private
//
//            Field[] fields = myClass.getFields();
//            System.out.println("Public fields slice : " + fields[0]);
//
//            // 3. เข้าถึง Public Fields
//            Field publicField = myClass.getField("publicField"); // เข้าถึง public field
//            System.out.println("Public Field Name: " + publicField.getName());
//            System.out.println("Public Field Value (before): " + publicField.get(instance));
//            publicField.set(instance, 20); // เปลี่ยนค่า public field
//            System.out.println("Public Field Value (after): " + publicField.get(instance));
//
//            // 4. เข้าถึง Private Fields (ต้องตั้งค่า setAccessible(true))
//            Field privateField = myClass.getDeclaredField("privateField"); // getDeclaredField() สำหรับ private/protected/default
//            privateField.setAccessible(true); // อนุญาตให้เข้าถึง private field
//            System.out.println("Private Field Name: " + privateField.getName());
//            System.out.println("Private Field Value: " + privateField.get(instance));
//            privateField.set(instance, "New Private Value"); // เปลี่ยนค่า private field
//            System.out.println("Private Field Value (after set): " + privateField.get(instance));
//
//
//            // 5. เข้าถึง Public Methods และเรียกใช้
//            Method publicMethod = myClass.getMethod("publicMethod", String.class); // รับเมธอด publicMethod ที่รับ String
//            publicMethod.invoke(instance, "Hello from Reflection!"); // เรียกใช้เมธอด publicMethod
//
//            // 6. เข้าถึง Private Methods และเรียกใช้ (ต้องตั้งค่า setAccessible(true))
//            Method privateMethod = myClass.getDeclaredMethod("privateMethod", int.class); // รับเมธอด privateMethod ที่รับ int
//            privateMethod.setAccessible(true); // อนุญาตให้เรียกใช้ private method
//            String privateResult = (String) privateMethod.invoke(instance, 5); // เรียกใช้เมธอด privateMethod
//            System.out.println(privateResult);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        SpringApplication.run(ReflexApplication.class, args);
	}

    public static String GenerateScript( Object dataModel) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        Class<?> myClass = dataModel.getClass();
        Object instance = myClass.getDeclaredConstructor().newInstance();

        Field[] fields = myClass.getFields();
        Field field = fields[0];
        System.out.println("Get data type name  " +field.getType());

        Method[] methods = myClass.getDeclaredMethods();
        methods = Arrays.stream(methods).filter(method -> method.getName().substring(0,3).equals("get")).toArray(Method[]::new);

        for (int i = 0; i < methods.length ; i++) {
            System.out.println("Get Method name  " +methods[i].getName());
            System.out.println("Get Method type  " +methods[i].getGenericReturnType().getTypeName());
            System.out.println("Get Method Value  " +methods[i].invoke(instance));
        }

        return "";
    }

    public static String GenerateColumns( Object dataModel , String[] ignoreFields) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> myClass = dataModel.getClass();
        Object instance = myClass.getDeclaredConstructor().newInstance();

        Field[] fields = myClass.getFields();

        List<String> columns = new ArrayList<>();

        for (Field field : fields) {

            if(field.getAnnotation(Column.class) != null){
                boolean isMatched = Arrays.stream(ignoreFields).anyMatch(x-> x.equals(field.getName()));
                if(isMatched){
                    continue;
                }

                String annotationName = field.getAnnotation(Column.class).name();
                String columnName = String.format("%s%s%s","[", annotationName, "]");
                columns.add(columnName);
            }

        }

        if(columns.size() == 0 ) {
            return "";
        }

        return String.join(",",columns);
    }

    public static String GenerateColumnsWithAliasName( Object dataModel , String[] ignoreFields,String alias) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> myClass = dataModel.getClass();
        Object instance = myClass.getDeclaredConstructor().newInstance();

        Field[] fields = myClass.getFields();

        List<String> columns = new ArrayList<>();

        for (Field field : fields) {

            if (field.getAnnotation(Column.class) != null) {
                boolean isMatched = Arrays.stream(ignoreFields).anyMatch(x -> x.equals(field.getName()));
                if (isMatched) {
                    continue;
                }

                String annotationName = field.getAnnotation(Column.class).name();
                String columnName = String.format("%s.%s%s%s", alias, "[", annotationName, "]");
                columns.add(columnName);
            }

        }

        if(columns.size() == 0 ) {
            return "";
        }

        return String.join(",",columns);
    }

}
