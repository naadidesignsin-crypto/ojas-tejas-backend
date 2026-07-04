package com.naadi.ojas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemoBookingRequest {

        @NotBlank(message = "Parent name is required")
        @Size(max = 100, message = "Parent name must be under 100 characters")
        private String parentName;

        @NotBlank(message = "Child name is required")
        @Size(max = 100, message = "Child name must be under 100 characters")
        private String childName;

        @NotNull(message = "Child age is required")
        @Min(value = 3, message = "Child age must be at least 3")
        @Max(value = 18, message = "Child age must be at most 18")
        private Integer childAge;

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^[6-9][0-9]{9}$",
                message = "Phone number must be a valid 10 digit Indian mobile number"
        )
        private String phone;

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 150, message = "Email must be under 150 characters")
        private String email;

        @NotBlank(message = "Preferred class is required")
        @Size(max = 100, message = "Preferred class must be under 100 characters")
        private String preferredClass;

        @Size(max = 500, message = "Message must be under 500 characters")
        private String message;
}