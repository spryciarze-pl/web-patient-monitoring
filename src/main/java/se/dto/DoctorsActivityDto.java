package se.dto;

import lombok.Data;
import se.db.model.DoctorsActivity;
import se.db.model.User;

import java.time.LocalDateTime;

@Data
public class DoctorsActivityDto {

    private Integer activityId;

    private String fullName;

    private String activityType;

    private LocalDateTime localDateTime;

    private boolean completed;

    public DoctorsActivityDto(User user, DoctorsActivity activity) {
        this.fullName = user.getName() + " " + user.getSurname();
        this.activityType = activity.getType();
        this.localDateTime = activity.getDeadlineTime();
        this.activityId = activity.getId();
        this.completed = activity.isCompleted();
    }

    public DoctorsActivityDto(DoctorsActivity activity) {
        this.activityType = activity.getType();
        this.localDateTime = activity.getDeadlineTime();
        this.activityId = activity.getId();
        this.completed = activity.isCompleted();
    }

}
