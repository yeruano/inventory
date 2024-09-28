package co.com.inventory.r2dbc.auth.mapper;

import co.com.inventory.model.user.PasswordHistory;
import co.com.inventory.r2dbc.auth.data.PasswordHistoryData;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PasswordHistoryMapper {

    public PasswordHistoryData buildPasswordHistoryData(PasswordHistory passwordHistory) {
        return PasswordHistoryData.builder()
                .password(passwordHistory.getPassword())
                .changeDate(passwordHistory.getChangeDate())
                .userId(passwordHistory.getUserId())
                .build();
    }

    public PasswordHistory buildPasswordHistory(PasswordHistoryData passwordHistoryData) {
        return PasswordHistory.builder()
                .id(passwordHistoryData.getId())
                .password(passwordHistoryData.getPassword())
                .changeDate(passwordHistoryData.getChangeDate())
                .userId(passwordHistoryData.getUserId())
                .build();
    }
}
