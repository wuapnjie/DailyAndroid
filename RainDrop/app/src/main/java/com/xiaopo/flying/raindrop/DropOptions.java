package com.xiaopo.flying.raindrop;

/**
 * @author wupanjie
 */

public class DropOptions {
  public int minR = 10;
  public int maxR = 40;
  public int maxDrops = 900;
  public float rainChance = 0.3f;
  public int rainLimit = 3;
  public int dropletsRate = 50;
  public int[] dropletsSize = { 2, 4 };
  public float dropletsCleaningRadiusMultiplier = 0.43f;
  public boolean raining = true;
  public int globalTimeScale = 1;
  public int trailRate = 1;
  public boolean autoShrink = true;
  public float[] spawnArea = { -0.1f, 0.95f };
  public float[] trailScaleRange = { 0.2f, 0.5f };
  public float collisionRadius = 0.65f;
  public float dropFallMultiplier = 0.01f;
  public float collisionBoostMultiplier = 0.05f;
  public int collisionBoost = 1;
}
