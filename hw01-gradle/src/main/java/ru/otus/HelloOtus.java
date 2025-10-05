package ru.otus;

import com.google.common.base.Joiner;
import java.util.List;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String... args) {
        var words = List.of("Hello", "Otus");

        // 2️⃣ Склеиваем элементы списка через пробел
        String result = Joiner.on(" ").join(words);

        System.out.println(result); // Выведет: Hello Guava World
    }
}
