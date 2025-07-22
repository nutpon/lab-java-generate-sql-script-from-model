package lab.reflex;

import jakarta.persistence.Column;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class ReflexApplication {

	public static void main(String[] args) {

//        GenerateValue
//        GenerateUpdate
//        StringArraysToNString
//        sortingArrayToString
//        Int32ArraysToString


        try {

            System.out.println("String.class: " + String.class);

            System.out.println("boolean.class: " + boolean.class);
            System.out.println("Boolean.class: " + Boolean.class);

            System.out.println("byte.class: " + byte.class);
            System.out.println("Byte.class: " + Byte.class);

            System.out.println("short.class: " + short.class);
            System.out.println("Short.class: " + Short.class);

            System.out.println("int.class: " + int.class);
            System.out.println("Integer.class: " + Integer.class);

            System.out.println("long.class: " + long.class);
            System.out.println("Long.class: " + Long.class);

            System.out.println("float.class: " + float.class);
            System.out.println("Float.class: " + Float.class);

            System.out.println("double.class: " + double.class);
            System.out.println("Double.class: " + Double.class);

            System.out.println("char.class: " + char.class);
            System.out.println("Character.class: " + Character.class);

            System.out.println("Date.class: " + Date.class);

            System.out.println("\nComparison with TYPE constants:");
            System.out.println("boolean.class == Boolean.TYPE: " + (boolean.class == Boolean.TYPE));
            System.out.println("int.class == Integer.TYPE: " + (int.class == Integer.TYPE));

            MyClass model = new MyClass();
            model.setPrivateField("test");
//            GenerateScript(model);

            //GenerateColumn
//            System.out.println(GenerateColumns(model,new String[]{"publicField"}));

            //GenerateColumnWithInclude
//            System.out.println(GenerateColumnWithInclude(model,new String[]{"privateField"}));

            //GenerateAliasColumnWithInclude
//            System.out.println(GenerateAliasColumnWithInclude(model,new String[]{"publicField"},"tms"));

            //GenerateValue
//            System.out.println(GenerateValue(model,new String[]{"privateField"}));

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

        if(columns.isEmpty()) {
            return "";
        }

        return String.join(",",columns);
    }

    public static String GenerateColumnsWithAliasName( Object dataModel , String[] ignoreFields,String alias) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> myClass = dataModel.getClass();

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

        if(columns.isEmpty()) {
            return "";
        }

        return String.join(",",columns);
    }

    public static String GenerateColumnWithInclude( Object dataModel , String[] includeFields){
        Class<?> myClass = dataModel.getClass();

        Field[] fields = myClass.getFields();

        List<String> columns = new ArrayList<>();

        for (Field field : fields) {

            if(field.getAnnotation(Column.class) != null){
                boolean isMatched = Arrays.stream(includeFields).anyMatch(x-> x.equals(field.getName()));
                if(isMatched){
                    String annotationName = field.getAnnotation(Column.class).name();
                    String columnName = String.format("%s%s%s","[", annotationName, "]");
                    columns.add(columnName);
                }
            }

        }

        if(columns.isEmpty()) {
            return "";
        }

        return String.join(",",columns);
    }

    public static String GenerateAliasColumnWithInclude( Object dataModel , String[] includeFields,String alias) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> myClass = dataModel.getClass();

        Field[] fields = myClass.getFields();

        List<String> columns = new ArrayList<>();

        for (Field field : fields) {

            if (field.getAnnotation(Column.class) != null) {
                boolean isMatched = Arrays.stream(includeFields).anyMatch(x -> x.equals(field.getName()));
                if (isMatched) {
                    String annotationName = field.getAnnotation(Column.class).name();
                    String columnName = String.format("%s.%s%s%s", alias, "[", annotationName, "]");
                    columns.add(columnName);
                }
            }

        }

        if(columns.isEmpty()) {
            return "";
        }

        return String.join(",",columns);
    }

   public static String GenerateValue(Object dataModel, String[] ignoreFields) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
       Class<?> myClass = dataModel.getClass();
       Object instance = myClass.getDeclaredConstructor().newInstance();

       Field[] fields = myClass.getFields();
       Field field = fields[0];
       System.out.println("Get data type name  " +field.getType());

       Method[] methods = myClass.getDeclaredMethods();
       methods = Arrays.stream(methods).filter(method -> method.getName().substring(0,3).equals("get")).toArray(Method[]::new);

       for (int i = 0; i < methods.length ; i++) {
           System.out.println(int.class.getTypeName());
           System.out.println("Get Method name  " +methods[i].getName());
           System.out.println("Get Method type  " +methods[i].getGenericReturnType().getTypeName());
           System.out.println("Get Method Value  " +methods[i].invoke(instance));
       }

       return "";
   }

}
