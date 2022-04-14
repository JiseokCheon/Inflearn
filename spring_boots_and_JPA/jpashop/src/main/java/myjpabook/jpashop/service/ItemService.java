package myjpabook.jpashop.service;

import lombok.RequiredArgsConstructor;
import myjpabook.jpashop.domain.item.Item;
import myjpabook.jpashop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item){
        return itemRepository.save(item);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(long itemId){
        return itemRepository.findOne(itemId);
    }

    @Transactional
    public void updateItem(Long id, String name, int price, int quantity){
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(quantity);
    }
}
