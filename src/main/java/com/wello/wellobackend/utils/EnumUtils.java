package com.wello.wellobackend.utils;

import com.wello.wellobackend.dto.responses.AnswerOptionResponse;
import com.wello.wellobackend.enums.ActivityLevel;
import com.wello.wellobackend.enums.Goal;
import com.wello.wellobackend.enums.Gender;

import java.lang.reflect.Method;
import java.util.*;

public class EnumUtils {

    // Map để liên kết key → Enum
    private static final Map<String, Enum<?>[]> optionsMap = new HashMap<>();

    static {
        // Đăng ký các enum tương ứng với key
        optionsMap.put("ACTIVITY", ActivityLevel.values());
        optionsMap.put("GOAL", Goal.values());
        optionsMap.put("GENDER", Gender.values());
        // Thêm enum mới ở đây nếu cần
    }

    /**
     * Chuyển mảng Enum<?> thành List<OptionAnswer>
     * Hỗ trợ getMoTa() nếu enum có
     */
    public static List<AnswerOptionResponse> toOptionList(Enum<?>[] values) {
        List<AnswerOptionResponse> list = new ArrayList<>();
        if (values == null) return list;

        for (Enum<?> value : values) {
            String label = value.toString();

            // Lấy getter getMoTa() nếu có
            try {
                Method m = value.getClass().getMethod("getMoTa");
                Object result = m.invoke(value);
                if (result != null) {
                    label = result.toString();
                }
            } catch (Exception ignored) {
                // Không có getMoTa → dùng default toString()
            }

            list.add(new AnswerOptionResponse(value.name(), label));
        }

        return list;
    }

    /**
     * Lấy danh sách OptionAnswer theo key câu hỏi
     * Trả về emptyList nếu key null hoặc không tồn tại
     */
    public static List<AnswerOptionResponse> getOptionsForQuestion(String key) {
        if (key == null) return Collections.emptyList();

        Enum<?>[] enums = optionsMap.get(key);
        return enums != null ? toOptionList(enums) : Collections.emptyList();
    }
}
