package org.csu.mypetstore;

import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.AccountService;
//import org.csu.mypetstore.service.CartService;
import org.csu.mypetstore.service.CatalogService;
import org.csu.mypetstore.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;

@SpringBootTest
@MapperScan("org.csu.mypetstore.persistence")
class MypetstoreApplicationTests {
    @Autowired
    CatalogService catalogService;
    @Autowired
    AccountService accountService;
    @Autowired
    OrderService orderService;
//    @Autowired
//    CartService cartService;

    @Autowired
    DataSource dataSource;
    @Test
    void contextLoads() {
    }

    @Test
    void testCatagory(){
        Category c=catalogService.getCategory("BIRDS");
        System.out.println(c.getName()+","+ c.getDescription());
    }
    @Test
    void testProdect(){
        List<Product> productList=catalogService.getProductListByCategory("BIRDS");
        System.out.print(productList.size());
    }
    @Test
    void testItem(){
        Item item=catalogService.getItem("EST-14");
        System.out.println(item.getItemId()+" ,"+item.getListPrice()+","+item.getAttribute1());
    }
    @Test
    void testAccount(){
        Account account=accountService.getAccount("j2ee");
        System.out.println(account.getAddress1()+","+account.getEmail()+"1");

        Account account1=accountService.getAccount("j2ee","j2ee");
        System.out.println(account1.getEmail()+","+account.getUsername()+"2");
    }
    @Test
    void testDatabase()
    {
        try {
            Connection connection =dataSource.getConnection();
            System.out.print(connection);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



//    @Test
//    void testCart(){
//      CartItem cartItem=cartService.getCartByUsername("j2ee");
//      System.out.println(cartItem.getItem());
////        Account account=accountService.getAccount("j2ee");
////        CartItem cartItem=new CartItem();
////        Item item=catalogService.getItem("EST-14");
////        cartItem.setItem(item);
////        cartItem.setQuantity(5);
////        cartItem.setInStock(false);
////        cartService.insertCartItem(cartItem,account);
//
//
//    }

}
