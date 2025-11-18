package com.saeyan.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.saeyan.dto.ProductVO;

import util.DBManager;

public class ProductDAO {

	private static ProductDAO instance = new ProductDAO();
	
	private ProductDAO() {}
	
	public static ProductDAO getInstance() {
		return instance;
	}
	
	
	//전체 데이타 가져오기
	public List<ProductVO> selectAllProuducts() {
		
		Connection con= null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql =  "select * from product order by code desc";
		
		List<ProductVO> list = new ArrayList<ProductVO>();
		
		ProductVO vo = null;
		
		try {
			//1. DB연결
			con = DBManager.getConnection();
			
			//2. sql 구문 전송
			pstmt = con.prepareStatement(sql);
			
			//3. sql 구문 맵핑
			
			//4. sql 실행
			rs = pstmt.executeQuery();
			
			//5. 가져온 데이타 VO 클래스 저장
			/*
			 * create table product(
					code int  auto_increment primary key,
				    name varchar(100),
				    price int,
				    pictureurl varchar(50),
				    description varchar(1000)    
				);
			 */
			while(rs.next()) {
				vo = new ProductVO();
				//6	개념을 콕콕 잡아주는 데이터베이스	27000	db.jpg	데이터베이스에 관한 모든 것을 쉽고 재미있게 정리한 교재...
				
				vo.setCode(rs.getInt("code"));
				vo.setName(rs.getString("name"));
				vo.setPrice(rs.getInt("price"));
				vo.setPictureUrl(rs.getString("pictureurl"));
				vo.setDescription(rs.getString("description"));
				
				list.add(vo);
 			}
		
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			DBManager.close(con, pstmt, rs);
		}
		
		return list;
	} //end selectAllProuducts

	public void insertProduct(ProductVO vo) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql = "insert into product(name, price, pictureurl, description) "
				+ "values(?, ?, ?, ?)";
		try {
			//1. DB연결
			con = DBManager.getConnection();
			//2. sql 구문 전송
			pstmt = con.prepareStatement(sql);
			//3. sql 구문 맵핑
			pstmt.setString(1, vo.getName());
			pstmt.setInt(2, vo.getPrice());
			pstmt.setString(3, vo.getPictureUrl());
			pstmt.setString(4, vo.getDescription());
			//4. sql 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
	} //end insertProduct

	public ProductVO selectProductByCode(String code) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		String sql = "select * from product where code = ?";
		ProductVO vo = new ProductVO();
		
		try {
			//1. DB연결
			con = DBManager.getConnection();
			//2. sql 구문 전송
			pstmt = con.prepareStatement(sql);
			//3. sql 구문 맵핑
			pstmt.setInt(1, Integer.parseInt(code));
			
			//4. sql 실행
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				vo.setCode(rs.getInt("code"));
				vo.setName(rs.getString("name"));
				vo.setPrice(rs.getInt("price"));
				vo.setPictureUrl(rs.getString("pictureurl"));
				vo.setDescription(rs.getString("description"));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt, rs);
		}
		return vo;
	}

	public void updateProduct(ProductVO vo) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "UPDATE product SET name = ?, price = ?, pictureurl = ?, description = ? WHERE code = ?";
		try {
			//1. DB연결
			con = DBManager.getConnection();
			//2. sql 구문 전송
			pstmt = con.prepareStatement(sql);
			//3. sql 구문 맵핑
			pstmt.setString(1, vo.getName());
			pstmt.setInt(2, vo.getPrice());
			pstmt.setString(3, vo.getPictureUrl());
			pstmt.setString(4, vo.getDescription());
			pstmt.setInt(5, vo.getCode()); // WHERE 절의 code 값 설정
			//4. sql 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt);
		}
		
	}//end updateProduct

	public void deleteProduct(int code) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "DELETE FROM product WHERE code = ?";
		
		try {
			//1. DB연결
			con = DBManager.getConnection();
			//2. sql 구문 전송
			pstmt = con.prepareStatement(sql);
			//3. sql 구문 맵핑
			pstmt.setInt(1, code);
			//4. sql 실행
			pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManager.close(con, pstmt);
		}
	}//end deleteProduct

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
