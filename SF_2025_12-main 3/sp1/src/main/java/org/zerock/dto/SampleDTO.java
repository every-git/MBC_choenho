package org.zerock.dto;

import lombok.Builder;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Builder
public class SampleDTO {
	private String name;
	private int age;
}
