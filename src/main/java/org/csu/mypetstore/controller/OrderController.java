package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.service.AccountService;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("order")
@SessionAttributes({"account","authenticated","myList","order"})
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private Order order;
    @Autowired
    private AccountService accountService;
    @Autowired
    private Cart cart;


    private boolean confirmed;
    private boolean shippingAddressRequired;
    private static final List<String>CARD_TYPE_LIST;
    static {
        List<String>cardList = new ArrayList<String>();
        cardList.add("Visa");
        cardList.add("MasterCard");
        cardList.add("American Express");
        CARD_TYPE_LIST = Collections.unmodifiableList(cardList);
    }
    @GetMapping("viewOrder")
    public  String viewOrder(Model model){
//        order = orderService.getOrder(order.getOrderId());
        if(accountService.getAccount(order.getUsername()).getUsername().equals(order.getUsername())){
            return "order/ViewOrder";
        }else{
            order = null;
            model.addAttribute("msg","you may only view your own orders.");
            return "common/error";
        }
    }
    @RequestMapping("confirm")
    public String newOrder(HttpServletRequest request, Model model){
        String a = request.getParameter("shippingAddressRequired");
        if(a != null){
            shippingAddressRequired = false;
            model.addAttribute("order",order);
            return "order/ShippingForm";
        }else if(!confirmed){
            model.addAttribute("order",order);
            return "order/ConfirmOrder";
        }else if(order != null){
            orderService.insertOrder(order);
            model.addAttribute("msg","Thank you, your order has been submitted.");

            return "order/ViewOrder";
        }else{
            model.addAttribute("msg","An error occurred processing your order (order was null).");
            return "common/error";
        }

    }
    @RequestMapping("newOrderForm")
    public String newOrderForm( Account account,@ModelAttribute("authenticated")boolean authenticated, Model model){
        if(account == null || !authenticated){
            model.addAttribute("msg","You must sign on before attempting to check out.  Please sign on and try checking out again.");
            return "account/signon";
        }else if(cart!=null){
            order.initOrder(account,cart);
            return "order/NewOrderForm";
        }
        else{
            model.addAttribute("msg","An order could not be created because a cart could not be found.");
            return "common/error";
            //aaa
        }
    }
    @GetMapping("listOrders")
    public String listOrders(@ModelAttribute("account")Account account,Model model){
        List<Order> orderList = orderService.getOrdersByUsername(account.getUsername());
        model.addAttribute("orderList",orderList);
        return "order/ListOrders";
    }

}