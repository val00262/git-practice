package jp.co.val.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
		req.setCharacterEncoding("UTF-8");
		String name = req.getParameter("name");
		int price = Integer.parseInt(req.getParameter("price"));
		
		res.setCharacterEncoding("UTF-8");
		PrintWriter writer = res.getWriter();
		writer.println(name + "(" + price + "円)を追加しました。");
		
		try {
			InitialContext context = new InitialContext();
			DataSource ds = (DataSource)context.lookup("java:comp/env/jdbc");
			try {
				Connection connection = ds.getConnection();
				try {
					Statement statement = connection.createStatement();
					try {
						statement.execute("INSERT INTO product(name, price) VALUES('" + name + "'," + price + ")");
						connection.commit();
					} finally {
						statement.close();
					}
				} finally {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (NamingException e) {
		    e.printStackTrace();
		}
	}
}