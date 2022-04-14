package myjpabook.jpashop.web;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderForm {
     private Long id;
     private Long itemId;
     private int count;
}
