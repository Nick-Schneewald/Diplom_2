package ru.yandex_praktikum.diplom_2.dataproviders;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex_praktikum.diplom_2.clients.UtilsClient;
import ru.yandex_praktikum.diplom_2.pojo.CreateOrderRequest;
import ru.yandex_praktikum.diplom_2.pojo.IngredientsData;

import java.util.ArrayList;
import java.util.List;

public class OrderProvider {
    private static final int DEFAULT_HASH_START_INDEX = 0;
    private static final int DEFAULT_HASH_END_INDEX = 24;
    private static final int DEFAULT_INGREDIENTS_COUNT = 15;

    public static CreateOrderRequest getRandomCreateOrderRequest() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        UtilsClient utilsClient = new UtilsClient();
        List<String> availableIngredients = new ArrayList<>();
        List<String> availableBuns = new ArrayList<>();

        List<IngredientsData> ingredientsData = utilsClient.getIngredients()
                .extract().jsonPath().getList("data", IngredientsData.class);


        for (IngredientsData listItem : ingredientsData) {
            if (listItem.getType().equals("bun")) {
                availableBuns.add(listItem.get_id());
            } else if (listItem.getType().equals("main") || listItem.getType().equals("sauce")) {
                availableIngredients.add(listItem.get_id());
            }
        }

        int numberOfIngredients = (1 + (int) (Math.random() * availableIngredients.size()));
        List<String> chosenIngredients = new ArrayList<>();
        while (numberOfIngredients > 0) {
            chosenIngredients.add(availableIngredients.get((int) (Math.random() * availableIngredients.size())));
            numberOfIngredients--;
        }
        String chosenBun = availableBuns.get((int) (Math.random() * availableBuns.size()));

        List<String> burgerComplete = new ArrayList<>(chosenIngredients);
        burgerComplete.add(0, chosenBun);
        burgerComplete.add(burgerComplete.size() - 1, chosenBun);
        createOrderRequest.setIngredients(burgerComplete);

        return createOrderRequest;
    }

    public static CreateOrderRequest getEmptyCreateOrderRequest() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setIngredients(null);
        return createOrderRequest;
    }

    public static CreateOrderRequest getCreateOrderRequestWithWrongHashes() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();

        //создание списка ингридиентов с максимальной длиной равной кол-ву исходных ингридиентов
        List<String> ingredientsHashes = new ArrayList<>();
        int listLen = (1 + (int) (Math.random() * DEFAULT_INGREDIENTS_COUNT));
        for (int i = 0; i < listLen; i++) {
            String srcString = RandomStringUtils.randomAlphabetic(8);
            ingredientsHashes.add(DigestUtils.md5Hex(srcString));
        }

        createOrderRequest.setIngredients(ingredientsHashes);
        return createOrderRequest;
    }

    public static CreateOrderRequest getCreateOrderRequestWithPredefinedHashes() {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();

        List<String> ingredientsHashes = new ArrayList<>();
        int listLen = (1 + (int) (Math.random() * DEFAULT_INGREDIENTS_COUNT));
        for (int idx = 0; idx < listLen; idx++) {
            String src = RandomStringUtils.randomAlphabetic(3);
            String md5 = DigestUtils.md5Hex(src);
            String validIngredientsHash = md5.substring(DEFAULT_HASH_START_INDEX, DEFAULT_HASH_END_INDEX);
            ingredientsHashes.add(validIngredientsHash);
        }

        createOrderRequest.setIngredients(ingredientsHashes);
        return createOrderRequest;
    }
}
