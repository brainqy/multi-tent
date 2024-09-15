package com.yash.ytms.constants;

/**
 * Description of the class or file.
 *
 * @author Dnyaneshwar Somwanshi
 * @version 1.0
 * @project multi-tent
 * @since 15-09-2024
 */
public enum BadgeRule {
    XP_BASED("XP Based"),
    TASK_COMPLETION("Task Completion"),
    COURSE_COMPLETION("Course Completion"),
    LEADERBOARD("Leaderboard"),
    EVENT_PARTICIPATION("Event Participation"),
    CUSTOM_GOAL("Custom Goal"),
    TIME_BASED("Time Based");

    private final String displayName;

    BadgeRule(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static BadgeRule fromDisplayName(String displayName) {
        for (BadgeRule rule : values()) {
            if (rule.getDisplayName().equalsIgnoreCase(displayName)) {
                return rule;
            }
        }
        throw new IllegalArgumentException("No enum constant with display name " + displayName);
    }
}

