package com.web.batch.domain;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 45, nullable = true)
	private String name;
	@Column(length = 45, nullable = true)
	private String nickname;
	@Column(length = 45, nullable = true)
	private String phone;
	@Column
	private Timestamp registerDate;
	
	@Builder
	public User(String name, String nickname, String phone) {
		this.name = name;
		this.nickname = nickname;
		this.phone = phone;
	}
	
	@PrePersist
	public void prePersist() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String toDate = sdf.format(date);
		Timestamp toTimestamp = Timestamp.valueOf(toDate);
		this.registerDate = this.registerDate == null? this.registerDate = toTimestamp:this.registerDate;
	}
}
