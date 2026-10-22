package pl.dwudziesty.nametagstudio.mixin;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pl.dwudziesty.nametagstudio.config.NametagConfig;

/** Applies the configurable transform around vanilla's label renderer. */
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true, require = 0)
    private void nametagstudio$beforeLabel(T entity, Text text, MatrixStack matrices,
                                             VertexConsumerProvider vertexConsumers, int light,
                                             CallbackInfo ci) {
        NametagConfig.Settings s = NametagConfig.settings;
        if (!s.enabled || !s.showText) { ci.cancel(); return; }
        matrices.push();
        // X controls horizontal stretch, Y controls height; Z remains unchanged.
        matrices.translate(0.0, s.verticalOffset, 0.0);
        matrices.scale(s.textScale * s.widthScale, s.textScale * s.heightScale, 1.0f);
    }

    @Inject(method = "renderLabelIfPresent", at = @At("RETURN"), require = 0)
    private void nametagstudio$afterLabel(T entity, Text text, MatrixStack matrices,
                                            VertexConsumerProvider vertexConsumers, int light,
                                            CallbackInfo ci) {
        NametagConfig.Settings s = NametagConfig.settings;
        if (s.enabled && s.showText) matrices.pop();
    }
}
