package com.example.regulatorclient;

import javafx.util.Pair;

/**
 * Обработчик ошибок
 */
public class ErrorHandler {

    /**
     * Метод получения описания ошибки
     * @param code код ошибки, который поступил от регулятора
     * @return объект в виде ключа и значение. Где ключ - это флаг есть ли ошибка или нет, значение - текст ошибки
     */
    public static Pair<Boolean, String> error(int code) {
        String errorMessage = switch (code) {
            case 0 -> "";
            case 1, 2 -> "Техническая ошибка. Некорректно сформировано сообщение к регулятору";
            case 3 -> "Техническая ошибка. Некорректно сформирована операция к регулятору";
            case 4 -> "Превышено значения количества данных, которые требуется получить от регулятора";
            default -> "Не удалось получить описание по указанному коду ошибки";
        };

        if (!errorMessage.isEmpty()) {
            return new Pair<>(true, errorMessage);
        }

        return new Pair<>(false, errorMessage);
    }

}
