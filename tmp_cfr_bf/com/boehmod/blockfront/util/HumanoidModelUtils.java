/*
 * Decompiled with CFR.
 */
package com.boehmod.blockfront.util;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;

public final class HumanoidModelUtils {
    public static void match(HumanoidModel<?> model1, HumanoidModel<?> model2) {
        model1.crouching = model2.crouching;
        model1.attackTime = model2.attackTime;
        model1.swimAmount = model2.swimAmount;
        model1.head.visible = model2.head.visible;
        model1.head.xRot = model2.head.xRot;
        model1.head.yRot = model2.head.yRot;
        model1.head.zRot = model2.head.zRot;
        model1.hat.visible = model2.hat.visible;
        model1.hat.xRot = model2.hat.xRot;
        model1.hat.yRot = model2.hat.yRot;
        model1.hat.zRot = model2.hat.zRot;
        if (model1 instanceof PlayerModel) {
            PlayerModel playerModel = (PlayerModel)model1;
            model1.rightArm.visible = playerModel.rightSleeve.visible = model2.rightArm.visible;
            model1.rightArm.xRot = playerModel.rightSleeve.xRot = model2.rightArm.xRot;
            model1.rightArm.yRot = playerModel.rightSleeve.yRot = model2.rightArm.yRot;
            model1.rightArm.zRot = playerModel.rightSleeve.zRot = model2.rightArm.zRot;
            model1.leftArm.visible = playerModel.leftSleeve.visible = model2.leftArm.visible;
            model1.leftArm.xRot = playerModel.leftSleeve.xRot = model2.leftArm.xRot;
            model1.leftArm.yRot = playerModel.leftSleeve.yRot = model2.leftArm.yRot;
            model1.leftArm.zRot = playerModel.leftSleeve.zRot = model2.leftArm.zRot;
            model1.rightLeg.visible = playerModel.rightPants.visible = model2.rightLeg.visible;
            model1.rightLeg.xRot = playerModel.rightPants.xRot = model2.rightLeg.xRot;
            model1.rightLeg.yRot = playerModel.rightPants.yRot = model2.rightLeg.yRot;
            model1.rightLeg.zRot = playerModel.rightPants.zRot = model2.rightLeg.zRot;
            model1.leftLeg.visible = playerModel.leftPants.visible = model2.leftLeg.visible;
            model1.leftLeg.xRot = playerModel.leftPants.xRot = model2.leftLeg.xRot;
            model1.leftLeg.yRot = playerModel.leftPants.yRot = model2.leftLeg.yRot;
            model1.leftLeg.zRot = playerModel.leftPants.zRot = model2.leftLeg.zRot;
        } else {
            model1.rightArm.visible = model2.rightArm.visible;
            model1.rightArm.xRot = model2.rightArm.xRot;
            model1.rightArm.yRot = model2.rightArm.yRot;
            model1.rightArm.zRot = model2.rightArm.zRot;
            model1.leftArm.visible = model2.leftArm.visible;
            model1.leftArm.xRot = model2.leftArm.xRot;
            model1.leftArm.yRot = model2.leftArm.yRot;
            model1.leftArm.zRot = model2.leftArm.zRot;
            model1.rightLeg.visible = model2.rightLeg.visible;
            model1.rightLeg.xRot = model2.rightLeg.xRot;
            model1.rightLeg.yRot = model2.rightLeg.yRot;
            model1.rightLeg.zRot = model2.rightLeg.zRot;
            model1.leftLeg.visible = model2.leftLeg.visible;
            model1.leftLeg.xRot = model2.leftLeg.xRot;
            model1.leftLeg.yRot = model2.leftLeg.yRot;
            model1.leftLeg.zRot = model2.leftLeg.zRot;
        }
    }
}

