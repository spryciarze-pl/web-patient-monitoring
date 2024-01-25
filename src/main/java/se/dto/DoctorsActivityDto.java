package se.dto;

import lombok.Data;
import se.db.model.DoctorsActivity;
import se.db.model.User;

import java.time.LocalDateTime;

@Data
public class DoctorsActivityDto {

    private String fullName;

    private String activityType;

    private LocalDateTime localDateTime;

    public DoctorsActivityDto(User user, DoctorsActivity activity) {
        this.fullName = user.getName() + " " + user.getSurname();
        this.activityType = activity.getType();
        this.localDateTime = activity.getDeadlineTime();
    }

    public DoctorsActivityDto(DoctorsActivity activity) {
        this.activityType = activity.getType();
        this.localDateTime = activity.getDeadlineTime();
    }

}
