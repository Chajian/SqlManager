package com.xyl.sqlmanager.extractor;

import com.xyl.sqlmanager.util.WordUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordRetriverTest {
    List<String> sqlList = new ArrayList();
    WordRetriver wordRetriver = new WordRetriver();

    @Before
    public void before(){
//        sqlList.add("SELECT * FROM customers WHERE age BETWEEN ? AND ?;");
//        sqlList.add("SELECT first_name, last_name, COUNT(*) as total_orders FROM customers JOIN orders ON customers.id = orders.customer_id GROUP BY customers.id HAVING total_orders >= ?;");
//        sqlList.add("SELECT p.product_name, c.category_name FROM products p INNER JOIN categories c ON p.category_id = c.category_id WHERE p.price BETWEEN ? AND ? AND c.is_active = ?;");
//        sqlList.add("SELECT DISTINCT country FROM customers WHERE registration_date >= ? ORDER BY country DESC;");
//        sqlList.add("SELECT CONCAT(first_name, ' ', last_name) AS full_name, email FROM users WHERE created_at >= ?;");
//        sqlList.add("SELECT * FROM products WHERE product_name LIKE ? OR product_name LIKE ? LIMIT ? OFFSET ?;");
//        sqlList.add("SELECT AVG(price) AS average_price, MIN(price) AS min_price, MAX(price) AS max_price FROM products WHERE category_id IN (?, ?, ?);");
//        sqlList.add("SELECT p.product_name, c.category_name FROM products p LEFT JOIN categories c ON p.category_id = c.category_id WHERE c.category_id IS NOT NULL;");
//        sqlList.add("SELECT COUNT(*) AS total_orders, YEAR(order_date) AS order_year FROM orders GROUP BY order_year, MONTH(order_date) ORDER BY order_year DESC, MONTH(order_date) DESC;");
//        sqlList.add("SELECT c.customer_name, SUM(o.total_amount) AS total_spent FROM customers c INNER JOIN orders o ON c.customer_id = o.customer_id GROUP BY c.customer_id HAVING total_spent BETWEEN ? AND ?;");
        sqlList.add("SELECT * FROM customers WHERE age > (SELECT AVG(age) FROM customers) AND gender = ?;");

    }

    @Test
    public void test(){
        for(String s:sqlList){
            wordRetriver.Retriver(s);
        }
    }

    @Test
    public void testSeperateSql(){
        for(String s:sqlList){
            List list = WordUtils.seperateWords(s);
            System.out.println(Arrays.toString(list.toArray()));
        }
    }
}
