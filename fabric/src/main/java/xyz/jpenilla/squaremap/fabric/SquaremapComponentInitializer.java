package xyz.jpenilla.squaremap.fabric;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.component.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class SquaremapComponentInitializer implements EntityComponentInitializer {
    public static final ComponentKey<PlayerComponent> SQUAREMAP_PLAYER_COMPONENT =
        ComponentRegistry.INSTANCE.registerIfAbsent(new ResourceLocation("squaremap:player_component"), PlayerComponent.class);

    @Override
    public void registerEntityComponentFactories(final EntityComponentFactoryRegistry registry) {
        registry.registerFor(ServerPlayer.class, SQUAREMAP_PLAYER_COMPONENT, player -> new PlayerComponentImpl());
    }

    public interface PlayerComponent extends Component {
        boolean hidden();

        void hidden(boolean hidden);
    }

    private static final class PlayerComponentImpl implements PlayerComponent {
        private static final String HIDDEN_KEY = "hidden";

        private boolean hidden;

        @Override
        public boolean hidden() {
            return this.hidden;
        }

        @Override
        public void hidden(final boolean hidden) {
            this.hidden = hidden;
        }

        @Override
        public void fromTag(final CompoundTag tag) {
            this.hidden = tag.getBoolean(HIDDEN_KEY);
        }

        @Override
        public CompoundTag toTag(final CompoundTag tag) {
            tag.putBoolean(HIDDEN_KEY, this.hidden);
            return tag;
        }
    }
}
