package com.project.WebStore.common.util;

import java.util.Map;
import java.util.Random;

public class RandomUtils {

  private RandomUtils() {}

  public static <E> E getWeightedRandom(Map<E, Double> map, Random random) {
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
}
