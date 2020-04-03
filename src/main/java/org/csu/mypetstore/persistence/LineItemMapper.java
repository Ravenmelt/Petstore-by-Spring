package org.csu.mypetstore.persistence;

import org.springframework.stereotype.Repository;
import  org.csu.mypetstore.domain.LineItem;
import java.util.List;

@Repository
public interface LineItemMapper {
    List<LineItem> getLineItemsByOrderId(int orderId);
    void insertLineItem(LineItem lineItem);
}
