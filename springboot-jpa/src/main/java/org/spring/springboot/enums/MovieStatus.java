package org.spring.springboot.enums;

public enum MovieStatus {

    // 使用数字映射数据库中的 tinyint(1)，0表示不活跃，1表示活跃
    INACTIVE(0, "Inactive"),
    ACTIVE(1, "Active");

    private final int code;
    private final String description;

    MovieStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MovieStatus fromCode(int code) {
        for (MovieStatus status : MovieStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
