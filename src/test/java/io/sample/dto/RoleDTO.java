package io.sample.dto;

import io.hypercriteria.Annotations.ConstructorName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import lombok.ToString;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoleDTO implements Serializable {

    private Long id;
    private String name;
    private String description;
    private Boolean isAdmin;

    @ConstructorName("nomenclator")
    public RoleDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
 
}
