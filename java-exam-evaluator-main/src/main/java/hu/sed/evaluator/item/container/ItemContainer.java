package hu.sed.evaluator.item.container;

import hu.sed.evaluator.item.Item;

import java.util.List;

public interface ItemContainer extends Item {

    void setItems(List<Item> items);

    List<Item> getItems();
}
