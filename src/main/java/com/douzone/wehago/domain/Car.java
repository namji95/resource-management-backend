package com.douzone.wehago.domain;

import lombok.*;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    private Integer carSeq;
    private String carName;
    private String carNumber;
    private String carDistance;
    private LocalDate carYear;
    private String carImage;
    private String carExplain;
    private Integer copSeq;
    private Integer rscSeq;
    private Boolean carState;
    private LocalDate carCreated;
    private LocalDate carUpdated;

}

