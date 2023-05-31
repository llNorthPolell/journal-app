package com.northpole.journalentrypublisher.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {

    @NotEmpty
    @NotNull
    private String recKey;

    @NotEmpty
    @NotNull
    private String recValue;
}
