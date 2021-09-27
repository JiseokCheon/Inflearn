package myjpabook.jpashop.service;

import myjpabook.jpashop.domain.item.Item;
import myjpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemServiceTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemService itemService;

    @Test
    public void 상품등록(){
        Item item = new Item();
        item.setName("사과");


        Long findItem = itemService.saveItem(item);

        Assertions.assertThat(item.getName()).isEqualTo(itemRepository.findOne(findItem).getName());
    }
}