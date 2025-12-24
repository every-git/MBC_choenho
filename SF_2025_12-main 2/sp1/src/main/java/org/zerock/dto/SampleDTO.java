package org.zerock.dto;

import lombok.Setter;
import lombok.ToString;
import lombok.Builder;

@Setter
@ToString
@Builder
public class SampleDTO {
	private String name;
	private int age;
}
