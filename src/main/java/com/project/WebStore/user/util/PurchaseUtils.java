package com.project.WebStore.user.util;

import static com.project.WebStore.common.type.ItemType.FIXED_POINT_BOX_ITEM;
import static com.project.WebStore.common.type.ItemType.RANDOM_POINT_BOX_ITEM;
import static com.project.WebStore.error.ErrorCode.POINT_BOX_ITEM_TYPE_NOT_EXIST;

import com.project.WebStore.error.exception.WebStoreException;
import com.project.WebStore.item.entity.CashItemEntity;
import com.project.WebStore.item.entity.PointBoxItemEntity;
import com.project.WebStore.item.entity.RandomPointEntity;
import com.project.WebStore.user.dto.PurchaseDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class PurchaseUtils {

  private PurchaseUtils() {}

  public static int getDecreasePoint(int requiredPoint, int itemQuantity) {
    return requiredPoint * itemQuantity;
  }

  public static int getEarnPoint(PurchaseDto.Request request, PointBoxItemEntity pointBoxItemEntity) {
    int itemQuantity = request.getQuantity();

    if (request.getType() == FIXED_POINT_BOX_ITEM) {
      return calcEarnPoint(itemQuantity, () -> getFixedPoint(pointBoxItemEntity));
    } else if(request.getType() == RANDOM_POINT_BOX_ITEM) {
      return calcEarnPoint(itemQuantity, () -> getRandomPoint(pointBoxItemEntity));
    } else {
      throw new WebStoreException(POINT_BOX_ITEM_TYPE_NOT_EXIST);
    }
  }

  private static int calcEarnPoint(int itemQuantity, Supplier<Integer> supplier) {
    return IntStream.range(0, itemQuantity)
        .map(i -> supplier.get())
        .sum();
  }

  private static int getFixedPoint(PointBoxItemEntity pointBoxItemEntity) {
    Map<Integer, Double> map = new HashMap<>();
    pointBoxItemEntity.getFixedPointEntities()
        .forEach(fixedPointEntity -> map.put(fixedPointEntity.getPointAmount(), fixedPointEntity.getProbability()));
    return getWeightedRandom(map, ThreadLocalRandom.current());
  }

  private static <E> E getWeightedRandom(Map<E, Double> map, Random random) {
    E result = null;
    double bestValue = Double.MAX_VALUE;

    for (E element : map.keySet()) {
      double value = -Math.log(random.nextDouble()) / map.get(element);
      if (value < bestValue) {
        bestValue = value;
        result = element;
      }
    }
    return result;
  }

  private static int getRandomPoint(PointBoxItemEntity pointBoxItemEntity) {
    RandomPointEntity randomPointEntity = pointBoxItemEntity.getRandomPointEntities().get(0);
    int minPoint = randomPointEntity.getMinPoint();
    int maxPoint = randomPointEntity.getMaxPoint();

    return ThreadLocalRandom.current().nextInt(minPoint, maxPoint + 1);
  }

  public static int getEarnCash(PurchaseDto.Request request, CashItemEntity cashItemEntity) {
    int itemQuantity = request.getQuantity();
    int cashAmount = cashItemEntity.getCashAmount();
    return itemQuantity * cashAmount;
  }
}
