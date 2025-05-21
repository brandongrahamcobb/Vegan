/*  Helpers.java The purpose of this program is to support the Vytuous class
 *  for any values which would make the legibility of the code worsen if it
 *  was inluded explicitly.
 *
 *  Copyright (C) 2025  github.com/brandongrahamcobb
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.brandongcobb.vegan.store.ai.utils.inc;

import com.brandongcobb.metadata.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helpers {

    public static boolean containsString(String[] array, String target) {
        for (String item : array) {
            if (item.equals(target)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertValue(Object value, Class<T> type) {
        if (type.isInstance(value)) {
            return (T) value;
        }
        if (type == Boolean.class) {
            if (value instanceof String) return (T) Boolean.valueOf((String) value);
        } else if (type == Integer.class) {
            if (value instanceof Number) return (T) Integer.valueOf(((Number) value).intValue());
            if (value instanceof String) return (T) Integer.valueOf(Integer.parseInt((String) value));
        } else if (type == Long.class) {
            if (value instanceof Number) return (T) Long.valueOf(((Number) value).longValue());
            if (value instanceof String) return (T) Long.valueOf(Long.parseLong((String) value));
        } else if (type == Float.class) {
            if (value instanceof Number) return (T) Float.valueOf(((Number) value).floatValue());
            if (value instanceof String) return (T) Float.valueOf(Float.parseFloat((String) value));
        } else if (type == Double.class) {
            if (value instanceof Number) return (T) Double.valueOf(((Number) value).doubleValue());
            if (value instanceof String) return (T) Double.valueOf(Double.parseDouble((String) value));
        } else if (type == String.class) {
            return (T) value.toString();
        }
        throw new IllegalArgumentException("Unsupported type conversion for: " + type.getName());
    }

    @SuppressWarnings("unchecked")

    public static boolean isNullOrEmpty(Object[] objects) {
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] instanceof String) {
                if (objects[i] == null || ((String) objects[i]).trim().isEmpty()) {
                    return true;
                }
            } else if (objects[i] == null) {
                return true;
            }
        }
        return false;
    }

    public static Long parseCommaNumber(String number) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            char c = number.charAt(i);
            if (c != ',') {
                sb.append(c);
            }
        }
        String cleanedNumber = sb.toString();
        try {
            int intVal = Integer.parseInt(cleanedNumber);
            return (long) intVal;
        } catch (NumberFormatException e) {
            return Long.parseLong(cleanedNumber);
        }
    }
}
