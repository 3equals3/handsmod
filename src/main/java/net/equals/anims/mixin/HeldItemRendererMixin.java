package net.equals.anims.mixin;

import com.google.common.base.MoreObjects;
import net.equals.anims.OtherHands;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    // Scale
    private float scaleXMain = 1;
    private float scaleYMain = 1;
    private float scaleZMain = 1;
    private float scaleXOff = 1;
    private float scaleYOff = 1;
    private float scaleZOff = 1;
    private float scaleXArm = 1;
    private float scaleYArm = 1;
    private float scaleZArm = 1;

    // Position
    private float posXMain = 0;
    private float posYMain = 0;
    private float posZMain = 0;
    private float posXOff = 0;
    private float posYOff = 0;
    private float posZOff = 0;
    private float posXArm = 0;
    private float posYArm = 0;
    private float posZArm = 0;

    // Rotation
    private float rotationXMain = 0;
    private float rotationYMain = 0;
    private float rotationZMain = 0;
    private float rotationXOff = 0;
    private float rotationYOff = 0;
    private float rotationZOff = 0;
    private float rotationXArm = 0;
    private float rotationYArm = 0;
    private float rotationZArm = 0;

    private void reserRotations(){
        rotationXMain = 0;
        rotationYMain = 0;
        rotationZMain = 0;
        rotationXOff = 0;
        rotationYOff = 0;
        rotationZOff = 0;
        rotationXArm = 0;
        rotationYArm = 0;
        rotationZArm = 0;
    };

    private double movex = 0; //(-0.1 - 0.1)
    private double movez = 0; //(0 - 0.2)
    private int movestatex = 0; //0 - nothing, 1 - right, 2 - left
    private int movestatez = 0; //0 - nothing, 1 - back, 2 - forward

    private double movexsin = 0.1;


    //Я не нашел нужного метода для проверки
    //искал в PlayerEntityRenderer, но там через useAction
    //и этот метод для меня не подходит, потому что он не учитывает мечи, топоры и т.п.
    //пожалуйста, не бейте за говнокод ^_^

    private boolean isForFix(ItemStack item){
        if(item.getItem().getGroup() != null){
            if(item.getItem().getGroup().getName() == "food" || item.getItem().getGroup().getName() == "brewing" || item.getItem().getGroup().getName() == "misc"){
                if(!item.isOf(Items.BEACON) && !item.isOf(Items.STICK) && !item.isOf(Items.BONE) && !item.isOf(Items.WHEAT) && !item.isOf(Items.BLAZE_ROD)) return true;
            }
        }
        if(item.isOf(Items.ENCHANTED_BOOK) || item.isOf(Items.TOTEM_OF_UNDYING) || item.isOf(Items.REDSTONE) || item.isOf(Items.COMPASS)|| item.isOf(Items.RECOVERY_COMPASS) || item.isOf(Items.CLOCK)|| item.isOf(Items.ELYTRA) || item.isOf(Items.TORCH)){
            return true;
        }
        return false;
    };

    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void onRenderItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (OtherHands.player != null) {
            float h = MathHelper.lerp(tickDelta, OtherHands.player.lastRenderPitch, OtherHands.player.renderPitch);
            float i = MathHelper.lerp(tickDelta, OtherHands.player.lastRenderYaw, OtherHands.player.renderYaw);
            //combat building_blocks decorations redstone transportation misc food tools combat brewing
            if (hand == Hand.MAIN_HAND) {
                if(item.isOf(Items.SHIELD)) {
                    matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0 -  (player.getPitch(tickDelta) - h) * 0.3f));
                    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0 - (player.getYaw(tickDelta) - i) * 0.3f));
                    matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));
                }else{
                    if(isForFix(item)){
                        if(player.getActiveHand() == hand && player.isUsingItem()) {
                            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0 - (player.getPitch(tickDelta) - h) * 0.3f));
                            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0 - (player.getYaw(tickDelta) - i) * 0.3f));
                            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));
                        }else{
                            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(76 - (player.getPitch(tickDelta) - h) * 0.15f));
                            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(64));
                            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-75.5f));
                            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-(player.getYaw(tickDelta) - i) * 0.15f));
                        }
                    }else{
                        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0 - (player.getPitch(tickDelta) - h) * 0.3f));
                        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0 - (player.getYaw(tickDelta) - i) * 0.3f));
                        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));

                    }

                }
                if(isForFix(item)) {
                    matrices.scale(0.9f, 0.9f, 0.9f);
                    matrices.translate(0.036, 0, 0.036);
                }
             } else {
                if(item.isOf(Items.SHIELD)) {
                    matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0 -  (player.getPitch(tickDelta) - h) * 0.3f));
                    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0 - (player.getYaw(tickDelta) - i) * 0.3f));
                    matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));//matrices.scale(scaleXOff, scaleYOff, scaleZOff);
                }else{
                    if(isForFix(item)){
                        if(player.getActiveHand() == hand && player.isUsingItem()) {
                            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0 - (player.getPitch(tickDelta) - h) * 0.3f));
                            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0 + (player.getYaw(tickDelta) - i) * 0.3f));
                            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));
                        }else{
                            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(-100 - (player.getPitch(tickDelta) - h) * 0.15f));
                            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-120));
                            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-100));
                            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-(player.getYaw(tickDelta) - i) * 0.15f));
                        }
                    }else{
                        matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0 - (player.getPitch(tickDelta) - h) * 0.3f));
                        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0 - (player.getYaw(tickDelta) - i) * 0.3f));
                        matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));
                    }
                }
                if(isForFix(item)) {
                    matrices.scale(0.9f, 0.9f, 0.9f);
                }
            }
             reserRotations();

        }
    }


    @Inject(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderArmHoldingItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IFFLnet/minecraft/util/Arm;)V"))
    private void onRenderArm(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if(OtherHands.player != null) {
            float h = MathHelper.lerp(tickDelta, OtherHands.player.lastRenderPitch, OtherHands.player.renderPitch);
            float i = MathHelper.lerp(tickDelta, OtherHands.player.lastRenderYaw, OtherHands.player.renderYaw);
            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(rotationXArm-(player.getPitch(tickDelta) - h) *0.1f));
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(rotationYArm-(player.getYaw(tickDelta) - i)*0.1f));
            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(rotationZArm));


        }
    }


    @Inject(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "HEAD"))
    private void rendItem1(float tickDelta, MatrixStack matrices, VertexConsumerProvider.Immediate vertexConsumers, ClientPlayerEntity player, int light, CallbackInfo ci) {
        OtherHands.deltatick = tickDelta;
        OtherHands.player = player;
    }
    @ModifyArg(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lnet/minecraft/util/math/Quaternion;)V"))
    private Quaternion rendItem(Quaternion quaternion) {
        return Vec3f.POSITIVE_X.getDegreesQuaternion(0);
    }
}
