package com.utn.entities;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@EqualsAndHashCode(of = {"id"})
public abstract class Base {

    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

}
