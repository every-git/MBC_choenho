<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
  PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.mybatis.example.BlogMapper">
	<insert id="insertAuthor">
	insert into tbl_member (name, password, email)
	values (#{name},#{password},#{email})
	</insert>
</mapper>