package maxmag_change.fancyvfx.event;

import maxmag_change.fancyvfx.FancyVFXConfig;
import maxmag_change.fancyvfx.particles.FancyVFXParticleRegistry;
import maxmag_change.fancyvfx.util.MathHelper;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.world.LodestoneWorldParticle;

import java.awt.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class WorldRenderHandler implements WorldRenderEvents.Start{



    public Pair<Box, Integer> continueLightGroup(ClientWorld world,BlockPos blockPos,Box lightGroup,int GroupRadius,int steps,int MaxSteps){

        Predicate<BlockState> predicate = blockState -> blockState.getLuminance() != 0;

        for (int xi = -GroupRadius; xi <= GroupRadius; xi++) {
            for (int yi = -GroupRadius; yi <= GroupRadius; yi++) {
                for (int zi = -GroupRadius; zi <= GroupRadius; zi++) {
                    BlockState givenBlockState = world.getBlockState(blockPos.add(xi,yi,zi));
                    if (predicate.test(givenBlockState)&&steps<MaxSteps){
                        BlockPos blockPosNew = blockPos.add(xi,yi,zi);
                        if (!lightGroup.contains(blockPosNew.toCenterPos())){
                            if (!givenBlockState.getOutlineShape(world,blockPosNew).isEmpty()){
                                lightGroup = lightGroup.union(givenBlockState.getOutlineShape(world,blockPosNew).getBoundingBox().offset(blockPosNew));
                            } else {
                                lightGroup = lightGroup.union(new Box(blockPosNew));
                            }
                            steps++;
                            if (FancyVFXConfig.connectingLightAura){
                                lightGroup = continueLightGroup(world,blockPosNew,lightGroup,GroupRadius,steps,MaxSteps).getLeft();
                            }
                        }
                    }
                }
            }
        }

        return new Pair<>(lightGroup,steps);
    }

    int renderTick = 0;

    @Override
    public void onStart(WorldRenderContext context) {

        ClientWorld world = context.world();

        if (renderTick>=360){
            renderTick=0;
        } else {
            renderTick++;
        }

        if (world!=null&&context.frustum()!=null&& FancyVFXConfig.lightAura && MinecraftClient.getInstance().player!=null) {

            int ChunkRadius = 3;

            int GroupRadius = 1;

            Predicate<BlockState> predicate = blockState -> blockState.getLuminance() != 0;

            for (int x = -ChunkRadius; x <= ChunkRadius; x++){
                for (int z = -ChunkRadius; z <= ChunkRadius; z++){
                    BlockPos cameraPos = context.camera().getBlockPos();

                    List<Box> lightGroups = new java.util.ArrayList<>(List.of());

                    BlockPos.Mutable mutable = new BlockPos.Mutable();
                    Chunk chunk = world.getChunk(ChunkSectionPos.getSectionCoord(cameraPos.getX())+x, ChunkSectionPos.getSectionCoord(cameraPos.getZ())+z);

                    for (int i = chunk.getBottomSectionCoord(); i < chunk.getTopSectionCoord(); i++) {
                        ChunkSection chunkSection = chunk.getSection(chunk.sectionCoordToIndex(i));
                        if (chunkSection.hasAny(predicate)) {
                            for (int j = 0; j < 16; j++) {
                                for (int k = 0; k < 16; k++) {
                                    for (int l = 0; l < 16; l++) {
                                        BlockState blockState = chunkSection.getBlockState(l, j, k);

                                        BlockPos blockPos = ChunkSectionPos.from(chunk.getPos(), i).getMinPos();

                                        blockPos = mutable.set(blockPos, l, j, k);

                                        if (predicate.test(blockState)&&context.frustum().isVisible(new Box(blockPos))&&context.camera().getBlockPos().isWithinDistance(blockPos,ChunkRadius*16)){

                                            Box lightGroup = new Box(blockPos);

                                            boolean cont = true;

                                            for (Box box : lightGroups) {
                                                if (box.contains(lightGroup.getCenter())) {
                                                    cont=false;
                                                }
                                            }
                                            if (!cont){
                                                continue;
                                            }
                                            lightGroup = continueLightGroup(world,blockPos,lightGroup,GroupRadius,0, FancyVFXConfig.maxConnectingLightAura).getLeft();

                                            for (Box box : lightGroups) {
                                                if (box.contains(lightGroup.getCenter())) {
                                                    cont=false;
                                                }
                                            }
                                            if (!cont){
                                                continue;
                                            }

                                            lightGroups.add(lightGroup);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    lightGroups.forEach(lightGroup->{

                        int pointsIntensity = MathHelper.pointsSeen(MinecraftClient.getInstance().player,lightGroup.expand(0.5));

                        if (context.frustum().isVisible(lightGroup)&& pointsIntensity>=1) {

                            Vec3d toLightSource = lightGroup.getCenter().subtract(MinecraftClient.getInstance().player.getPos()).normalize();
                            double lookIntensity = toLightSource.dotProduct(MinecraftClient.getInstance().player.getRotationVecClient());

                            Vec3d pos = lightGroup.getCenter();

                            float size = (float) new Vec3d(lightGroup.maxX, lightGroup.maxY, lightGroup.maxZ).distanceTo(new Vec3d(lightGroup.minX, lightGroup.minY, lightGroup.minZ));

                            WorldParticleBuilder.create(FancyVFXParticleRegistry.RING_PARTICLE)
                                    .setScaleData(GenericParticleData.create((float) (size*pointsIntensity/8*lookIntensity)).build())
                                    .setTransparencyData(GenericParticleData.create((float) (0.015f*world.getLightLevel(BlockPos.ofFloored(pos))*lookIntensity*pointsIntensity/8)).build())
                                    .setColorData(ColorParticleData.create(new Color(232, 195, 60)).build())
                                    .setSpinData(SpinParticleData.create(0.2f, 0.4f).setSpinOffset((world.getTime() * 0.05f)).setEasing(Easing.CIRC_OUT).build())
                                    .setLifetime(1)
                                    .setFullBrightLighting()
                                    .addRenderActor(new Consumer<LodestoneWorldParticle>() {
                                        @Override
                                        public void accept(LodestoneWorldParticle lodestoneWorldParticle) {
                                            lodestoneWorldParticle.lifeDelay = lodestoneWorldParticle.lifeDelay-1;
                                            if (lodestoneWorldParticle.lifeDelay==0){
                                                lodestoneWorldParticle.markDead();
                                            }
                                        }
                                    })
                                    .spawn(world, pos.getX(), pos.getY(), pos.getZ());
                        }
                    });
                }
            }
        }
    }
}