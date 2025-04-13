package com.mattutos.arkfuture.crafting.recipe.transform;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class TransformCircumstance {

    private final String type;

    public TransformCircumstance(String type) {
        this.type = type;
    }

    public static final TransformCircumstance EXPLOSION = new TransformCircumstance("explosion");
    private static final MapCodec<TransformCircumstance> EXPLOSION_CODEC = MapCodec.unit(EXPLOSION);

    public static final Codec<TransformCircumstance> CODEC = Codec.STRING.dispatch(t -> t.type, type -> switch (type) {
        case "explosion" -> EXPLOSION_CODEC;
        default -> throw new IllegalStateException("Invalid type: " + type);
    });

    public static final StreamCodec<RegistryFriendlyByteBuf, TransformCircumstance> STREAM_CODEC = StreamCodec.ofMember(
            TransformCircumstance::toNetwork,
            TransformCircumstance::fromNetwork);

    void toNetwork(FriendlyByteBuf buf) {
        buf.writeUtf(type);
    }

    static TransformCircumstance fromNetwork(FriendlyByteBuf buf) {
        String type = buf.readUtf();
        if (type.equals("explosion"))
            return explosion();
        else
            throw new DecoderException("Invalid transform recipe type " + type);
    }

    public static TransformCircumstance explosion() {
        return EXPLOSION;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof TransformCircumstance other && type.equals(other.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    public boolean isExplosion() {
        return type.equals("explosion");
    }

}
