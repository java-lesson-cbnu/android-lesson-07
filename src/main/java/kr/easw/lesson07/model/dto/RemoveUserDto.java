
package kr.easw.lesson07.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RemoveUserDto {
    private String userId;

    public RemoveUserDto(String userId) {
        this.userId = userId;
    }
}