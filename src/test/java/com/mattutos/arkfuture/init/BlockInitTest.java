package com.mattutos.arkfuture.init;

import com.mattutos.arkfuture.ArkFuture;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.gametest.GameTestHolder;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@GameTestHolder(ArkFuture.MOD_ID)
public class BlockInitTest {

    @GameTest
    public static void testRegister(GameTestHelper helper) {
        log.info("Testing BlockInit");
        var block = BlockInit.COAL_POWER_GENERATOR.get();
        BlockState blockState = block.defaultBlockState();

        assertNotNull(block);

        // Crie um jogador fictício no mundo de testes
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        // Obtém a posição do jogador (posição do 'player')
        var playerPos = player.blockPosition();

        // Coloca um bloco em uma posição ao redor do jogador (por exemplo, a frente dele)
        helper.setBlock(playerPos.north(), blockState);

        // Verifique se o bloco foi colocado corretamente
        helper.assertBlockPresent(block, playerPos.north());
    }
}