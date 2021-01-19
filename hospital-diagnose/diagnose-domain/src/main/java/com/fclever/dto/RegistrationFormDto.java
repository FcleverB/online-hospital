package com.fclever.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 挂号功能中保存挂号信息的Dto
 * @author Fclever
 * @create 2021-01-18 19:02
 */
@ApiModel(value="com-fclever-dto-RegistrationFormDto")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationFormDto implements Serializable {

    private PatientDto patientDto;

    private RegistrationDto registrationDto;
}
