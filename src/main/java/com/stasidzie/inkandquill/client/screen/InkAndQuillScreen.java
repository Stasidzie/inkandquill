package com.stasidzie.inkandquill.client.screen;

import com.stasidzie.inkandquill.network.ModPackets;
import com.stasidzie.inkandquill.network.packet.ChangeNameViaInkAndQuillC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault

@OnlyIn(Dist.CLIENT)
public class InkAndQuillScreen extends Screen {

    private static final ResourceLocation GUI_IMAGE = new ResourceLocation("inkandquill","textures/gui/ink_and_quill.png");
    /** The X size of the gui window in pixels. */
    private final int imageWidth = 176;
    /** The Y size of the gui window in pixels. */
    private final int imageHeight = 76;

    private final ItemStack itemToRename;
    private final String originalItemName;
    private final InteractionHand hand;

    private EditBox renameBox;

    public InkAndQuillScreen(ItemStack itemToRename, String itemName, InteractionHand handWithQuill) {
        super(Component.translatable("ink_and_quill.title"));
        this.itemToRename = itemToRename;
        this.originalItemName = itemName;
        this.hand = handWithQuill;
    }

    @Override
    public void tick() {
        this.renameBox.tick();
    }

    @Override
    protected void init() {
        super.init();

        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;


        this.addRenderableOnly((pGuiGraphics, pMouseX, pMouseY, pPartialTick) -> pGuiGraphics.drawString(this.font, this.title, i + 30, j + 7, 4210752, false));

        this.addRenderableOnly((pGuiGraphics, pMouseX, pMouseY, pPartialTick) -> pGuiGraphics.renderItem(itemToRename, i + 8, j + 20));
        this.addRenderableOnly((pGuiGraphics, pMouseX, pMouseY, pPartialTick) -> pGuiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemToRename, i + 8, j + 20));

        this.addRenderableWidget(new InkAndQuillScreen.ConfirmButton(i + 7, j + 45));
        this.addRenderableWidget(new InkAndQuillScreen.CancelButton(i + 35, j + 45));

        this.renameBox = new EditBox(this.font, i + 32, j + 24, 133, 12, Component.translatable("ink_and_quill.title"));
        this.renameBox.setCanLoseFocus(false);
        this.renameBox.setTextColor(-1);
        this.renameBox.setTextColorUneditable(-1);
        this.renameBox.setBordered(false);
        this.renameBox.setMaxLength(50);
        this.renameBox.setValue(originalItemName);
        this.addWidget(this.renameBox);
        this.setInitialFocus(this.renameBox);
        this.renameBox.setEditable(true);
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.renameBox.getValue();
        this.init(minecraft, width, height);
        this.renameBox.setValue(s);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (keyCode == 257) {
            this.saveAndClose();
            return true;
        }
        if (keyCode != 258) {
            this.setFocused(renameBox);
        }

        return this.renameBox.keyPressed(keyCode, scanCode, modifiers);
    }

    private void saveAndClose() {
        ModPackets.sendToServer(new ChangeNameViaInkAndQuillC2SPacket(renameBox.getValue(), hand));
        this.onClose();
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class SuperButton extends AbstractButton {

        private final int imageOffsetX;
        private static final int imageOffsetY = 76;

        protected SuperButton(int buttonX, int buttonY, Component message, int imageOffsetX) {
            super(buttonX, buttonY, 22, 22, message);
            this.imageOffsetX = imageOffsetX;
        }

        @Override
        public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            this.defaultButtonNarrationText(pNarrationElementOutput);
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            pGuiGraphics.blit(InkAndQuillScreen.GUI_IMAGE, this.getX(), this.getY(), this.imageOffsetX + (this.isHoveredOrFocused() ? this.width : 0), imageOffsetY, this.width, this.height);
        }
    }

    @OnlyIn(Dist.CLIENT)
    class ConfirmButton extends SuperButton {

        public ConfirmButton(int pX, int pY) {
            super(pX, pY, CommonComponents.GUI_DONE, 0);
        }

        public void onPress() {
            InkAndQuillScreen.this.saveAndClose();
        }
    }

    @OnlyIn(Dist.CLIENT)
    class CancelButton extends SuperButton {

        public CancelButton(int pX, int pY) {
            super(pX, pY, CommonComponents.GUI_CANCEL, 44);
        }

        public void onPress() {
            InkAndQuillScreen.this.onClose();
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        int left = (this.width - this.imageWidth) / 2;
        int top = (this.height - this.imageHeight) / 2;
        pGuiGraphics.blit(GUI_IMAGE, left, top, 0, 0, this.imageWidth, this.imageHeight);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renameBox.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
    }
}
