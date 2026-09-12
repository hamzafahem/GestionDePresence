package ma.uh1.mpresence.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorDto {
    private Instant timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
