package org.zerock.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * CREATE TABLE simple_todo (
	id INT AUTO_INCREMENT PRIMARY KEY,
	title VARCHAR(200) NOT NULL,
	description VARCHAR(500),
	done BOOLEAN DEFAULT FALSE,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TodoDTO {
	private int id;
	private String title;
	private String description;
	private boolean done;
	private LocalDateTime createdAt;

	public String getCreatedDate() {
		if (createdAt != null) {
			return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}
		return "";
	}
	
	public String getDoneStatus() {
		return done ? "완료" : "진행중";
	}
}
