package my.shop.product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class ProductManager {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	private DataSource ds;
	
	
	public ProductManager() {
		try {
			Context context = new InitialContext();
			// DB CP(Connection Pooling)
			ds = (DataSource)context.lookup("java:comp/env/jdbc_maria");
		} catch (Exception e) {
			System.out.println("DBCP Lookup err: " + e);
		}
	}
	
	public ArrayList<ProductBean> getProductAll(){
		ArrayList<ProductBean> list = new ArrayList<ProductBean>();
		try {
			conn = ds.getConnection();
			String sql = "select * from shop_product";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
	
			while(rs.next()) {
				ProductBean bean = new ProductBean();
				bean.setNo(rs.getInt("no"));
				bean.setName(rs.getString("name"));
				bean.setPrice(rs.getString("price"));
				bean.setDetail(rs.getString("detail"));
				bean.setSdate(rs.getString("sdate"));
				bean.setStock(rs.getString("stock"));
				bean.setImage(rs.getString("image"));
				list.add(bean);
			}			
		} catch (Exception e) {
			System.out.println("getProductAll() err: " + e);
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				System.out.println("getProductAll() finally err: " + e2);
			}
		}
		
		return list;
	}
	
	public boolean insertProduct(HttpServletRequest request) {
		boolean result = false;
		
		// 업로드할 이미지 절대 경로
		String uploadDir = "C:\\work\\wsou\\myshop\\WebContent\\data";
	
		try {
			MultipartRequest multi = new MultipartRequest(request, uploadDir, 1024*1024*5, "UTF-8", new DefaultFileRenamePolicy()); // cos.jar 필요
			conn = ds.getConnection();
			String sql = "insert into shop_product(name, price,detail,sdate,stock,image) values(?,?,?,now(),?,?)";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  multi.getParameter("name"));
			pstmt.setString(2, multi.getParameter("price"));
			pstmt.setString(3, multi.getParameter("detail"));
			pstmt.setString(4, multi.getParameter("stock"));
			if(multi.getFilesystemName("image") == null) {
				pstmt.setString(5, "ready.gif");
			}else {
				pstmt.setString(5, multi.getFilesystemName("image"));
			}
			
			if(pstmt.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			System.out.println("insertProduct() err " + e);
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				System.out.println("insertProduct() finally err: " + e2);
			}
		}
		return result;
	}
	
	
	public ProductBean getProduct(String no) {
		ProductBean bean = null;
		try {
			conn = ds.getConnection();
			String sql = "select * from shop_product where no = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, no);
			
			rs = pstmt.executeQuery();
			
			pstmt = conn.prepareStatement(sql);
			
			if(rs.next()) {
				bean = new ProductBean();
				bean.setNo(rs.getInt("no"));
				bean.setName(rs.getString("name"));
				bean.setPrice(rs.getString("price"));
				bean.setDetail(rs.getString("detail"));
				bean.setSdate(rs.getString("sdate"));
				bean.setStock(rs.getString("stock"));
				bean.setImage(rs.getString("image"));
			}			
		} catch (Exception e) {
			System.out.println("getProduct() err: " + e);
		}finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				System.out.println("getProduct() finally err: " + e2);
			}
		}
		
		return bean;
	}
	
	
	// 관리자 udpate 상품
	public boolean updateProduct(HttpServletRequest request) {
		
		boolean result =false;
		String uploadDir = "C:\\work\\wsou\\myshop\\WebContent\\data";
		
		try {
			MultipartRequest multi = new MultipartRequest(request, uploadDir, 1024*1024*5, "UTF-8", new DefaultFileRenamePolicy()); // cos.jar 필요
			conn = ds.getConnection();
			String sql = "update shop_product set name=?, price=?, detail=?, stock=?, image=? where no = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  multi.getParameter("name"));
			pstmt.setString(2, multi.getParameter("price"));
			pstmt.setString(3, multi.getParameter("detail"));
			pstmt.setString(4, multi.getParameter("stock"));
			if(multi.getFilesystemName("image") == null) {
				pstmt.setString(5, "ready.gif");
			}else {
				pstmt.setString(5, multi.getFilesystemName("image"));
			}
			System.out.println("test no : " +multi.getParameter("no") );
			pstmt.setString(6, multi.getParameter("no"));
			
			if(pstmt.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			System.out.println("updateProduct() err " + e);
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				System.out.println("updateProduct() finally err: " + e2);
			}
		}
		return result;
	}
	// 관리자 delete 상품
	public boolean deleteProduct(String no) {
		boolean result =false;
		
		try {
			conn = ds.getConnection();
			String sql = "delete from shop_product where no = ?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  no);


			if(pstmt.executeUpdate() > 0) {
				result = true;
			}
		} catch (Exception e) {
			System.out.println("updateProduct() err " + e);
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				System.out.println("updateProduct() finally err: " + e2);
			}
		}
		return result;
	}
		

	// 고객이 상품 주문 시 주문 수 만큼 재고량에서 빼기
	public void reduceProduct(OrderBean order) {

		try {
			conn = ds.getConnection();
			String sql = "update shop_product set stock=(stock-?) where no = ?";
			
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,  order.getQuantity());
			pstmt.setString(2,  order.getProduct_no());

			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("reduceProduct() err " + e);
		} finally {
			try {
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (Exception e2) {
				System.out.println("reduceProduct() finally err: " + e2);
			}
		}
	}
		
}
