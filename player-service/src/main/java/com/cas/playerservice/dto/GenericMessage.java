/* (C)2023 */
package com.cas.playerservice.dto;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.http.HttpStatusCode;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder(toBuilder = true)
public class GenericMessage<T> {
    private HttpStatusCode status;
    private String message;
    @Nullable private T data;
}
