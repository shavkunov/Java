package ru.spbau.shavkunov;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * Реализация сериализации классов, у которых поля могут быть примитивным типом или строкой.
 */
public class Serialization {
    /**
     * Сериализация в поток.
     * @param obj этот объект будет сериализован.
     * @param stream в этот поток будет сериализован объект.
     */
    public static void serialize(@NotNull Object obj, @NotNull OutputStream stream)
                                 throws IllegalAccessException, IOException {
        for (Field f : obj.getClass().getDeclaredFields()){
            f.setAccessible(true);
            if (!f.getType().getName().equals("java.lang.String")) {
                Object value = f.get(obj);
                String v = value.toString() + '\n';
                stream.write(v.getBytes(Charset.forName("UTF-8")));
            } else {
                String value = (String) f.get(obj);
                byte[] bytes = value.getBytes();
                String byteString = Byte.toString(bytes[0]);
                stream.write(byteString.getBytes());

                for (int i = 1; i < bytes.length; i++) {
                    stream.write((int) ' ');
                    byteString = Byte.toString(bytes[i]);
                    stream.write(byteString.getBytes());
                }

                stream.write((int) '\n');
            }
        }
    }

    /**
     * Десериализация объекта из потока.
     * @param stream из этого потока будет десериализован объект.
     * @param c класс объекта.
     * @param <T> тип объекта.
     * @return метод возвращает объект, у которого поля инициализируются данными из потока.
     */
    public static <T> @NotNull T deserialize(@NotNull InputStream stream, @NotNull Class<T> c)
                                             throws IOException, IllegalAccessException, InstantiationException {
        T res = c.newInstance();
        Scanner scanner = new Scanner(stream);
        for (Field f : c.getDeclaredFields()) {
            f.setAccessible(true);
            String fieldType = f.getType().getName();
            String s = scanner.nextLine();
            if (!fieldType.equals("java.lang.String")) {
                switch (fieldType) {
                    case "int":
                        f.setInt(res, Integer.parseInt(s));
                        break;

                    case "short":
                        f.setShort(res, Short.parseShort(s));
                        break;

                    case "byte":
                        f.setByte(res, Byte.parseByte(s));
                        break;

                    case "boolean":
                        f.setBoolean(res, Boolean.parseBoolean(s));
                        break;

                    case "long":
                        f.setLong(res, Long.parseLong(s));
                        break;

                    case "double":
                        f.setDouble(res, Double.parseDouble(s));
                        break;

                    case "float":
                        f.setFloat(res, Float.parseFloat(s));
                        break;

                    case "char":
                        f.setChar(res, s.charAt(0));
                        break;
                }
            } else {
                String[] stringBytes = s.split(" ");

                byte[] bytes = new byte[stringBytes.length];
                for (int i = 0; i < stringBytes.length; i++) {
                    bytes[i] = Byte.valueOf(stringBytes[i]);
                }

                String str = new String(bytes);
                f.set(res, str);
            }
        }
        return res;
    }
}
