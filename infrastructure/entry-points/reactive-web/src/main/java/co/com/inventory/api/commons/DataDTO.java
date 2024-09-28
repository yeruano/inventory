package co.com.inventory.api.commons;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataDTO<T extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3669378153770628153L;

    @NonNull
    private T data;

    public static <T extends Serializable> DataDTO<T> buildDataDTO(T data) {
        return DataDTO.<T>builder().data(data).build();
    }
}
