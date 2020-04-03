package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.*;
import org.csu.mypetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@SessionScope
@SessionAttributes({"account","authenticated","myList","order"})
@RequestMapping("cart")
public class Cartcontroller {
    @Autowired
    private CatalogService catalogService;
    @Autowired
    private Cart cart;
    @Autowired
    private Order order;

    private boolean confirmed;
    private boolean shippingAddressRequired;
    private static final List<String> CARD_TYPE_LIST;
    static {
        List<String>cardList = new ArrayList<String>();
        cardList.add("Visa");
        cardList.add("MasterCard");
        cardList.add("American Express");
        CARD_TYPE_LIST = Collections.unmodifiableList(cardList);
    }

    @GetMapping("viewCart")
    public String viewCart(Model model){
        model.addAttribute("cart",cart);
        return "cart/cart";
    }

    @GetMapping("addItemToCart")
    public String addItemToCart(String workingItemId, Model model){
        if(cart.containsItemId(workingItemId)){
            cart.incrementQuantityByItemId(workingItemId);
        }else{
            boolean isInStock = catalogService.isItemInStock(workingItemId);
            Item item = catalogService.getItem(workingItemId);
            cart.addItem(item,isInStock);
        }
        model.addAttribute("cart",cart);
        return "cart/cart";
    }

    @GetMapping("removeItemFromCart")
    public String removeItemFromCart(String workingItemId, Model model){
        Item item = cart.removeItemById(workingItemId);
        model.addAttribute("cart",cart);
        if(item == null){
            model.addAttribute("msg", "Attempted to remove null CartItem from Cart.");
            return "common/error";
        }else{
            return "cart/cart";
        }
    }

    @PostMapping("updateCartQuantities")
    public String updateCartQuantities(HttpServletRequest request, Model model){
        Iterator<CartItem> cartItems = cart.getAllCartItems();
        while (cartItems.hasNext()){
            CartItem cartItem = cartItems.next();
            String itemId = cartItem.getItem().getItemId();
            try{
                int quantity = Integer.parseInt(request.getParameter(itemId));
                cart.setQuantityByItemId(itemId,quantity);
                if(quantity < 1){
                    cartItems.remove();
                }
            }catch (Exception e){

            }
        }
        model.addAttribute("cart",cart);
        return "cart/cart";
    }

    @GetMapping("checkout")
    public String checkout(Model model){
        model.addAttribute("cart",cart);
        return "cart/checkout";
    }

    @GetMapping("success")
    public String success(Account account,Model model){
        if(cart!=null){
            order.initOrder(account,cart);
            model.addAttribute("order",order);
        }
        Iterator<CartItem> cartItems = cart.getAllCartItems();
        while (cartItems.hasNext())
        {
            CartItem cartItem = cartItems.next();
            String itemId = cartItem.getItem().getItemId();
            catalogService.updateInventoryQuantity(itemId,cartItem.getQuantity());
            Item item = cart.removeItemById(itemId);
            model.addAttribute("cart",cart);
            if(item == null){
                model.addAttribute("msg", "Please do it again");
                return "common/error";
            }else{
                return "order/NewOrderForm";
            }
        }
        return "order/NewOrderForm";
    }
}
