package me.brandonzx3.disableoffhand;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class DisableOffHand implements ClientModInitializer {

    private static KeyBinding enableKey = KeyBindingHelper.registerKeyBinding(new KeyBinding("Disable Off Hand", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Y, "Disable Off Hand"));
    private boolean enable = false;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::OnClientTick);
        UseItemCallback.EVENT.register(new UseItemCallback(){
            @Override
            public TypedActionResult<ItemStack> interact(PlayerEntity player, World world, Hand hand) {
                if(enable && hand == Hand.OFF_HAND) {
                    return TypedActionResult.fail(null);
                } else {
                    return TypedActionResult.pass(null);
                }
            } 
        });
        UseBlockCallback.EVENT.register(new UseBlockCallback(){

            @Override
            public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
                if(enable && hand == Hand.OFF_HAND) {
                    return ActionResult.FAIL;
                } else {
                    return ActionResult.PASS;
                }
            }
            
        });
    }

    void OnClientTick(MinecraftClient client) {
        PlayerEntity player = client.player;
        if(player == null) return;
        
        if (enableKey.wasPressed()) {
            client.world.playSound(player.getBlockPos(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 1.0f, 1.0f, false);
            enable = !enable;
            if(enable) {
                player.sendMessage(new LiteralText("off hand disabled"), true);
            } else {
                player.sendMessage(new LiteralText("off hand enabled"), true);
            }
        }
    }
}
