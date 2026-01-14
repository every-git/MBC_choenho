package com.example.jpa.repository;

import com.example.jpa.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

//Mybatis 대신 쓰는것.
@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    List<Member> findMemberByName(String name);
    List<Member> findByPhone(String phone);
    List<Member> findByAge(int age);

    // age가 파라미터로 넘어온 값보다 크거나 같으면(>=) 조회
    List<Member> findByAgeGreaterThanEqual(int age);

    Member findByNameAndAddress(String name, String address);
    List<Member> findByAddressLike(String address);
    List<Member> findByAddressOrderByAgeDesc(String address);

    @Query(
            "select m from Member m where m.address like %:address% order by m.age asc"
    )
    List<Member> findByAddressOrderByAgeAsc(String address);

    @Query(
            "select m from Member m where m.age >= :age order by m.age desc"
    )
    List<Member> findByAgeGreaterThanEqualOrderByAgeDesc(int age);
}
