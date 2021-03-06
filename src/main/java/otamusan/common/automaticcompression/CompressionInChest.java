package otamusan.common.automaticcompression;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class CompressionInChest {
	@SubscribeEvent
	public void onUpdate(WorldTickEvent event) {
		List<Entity> entitylist = event.world.getLoadedEntityList();
		for (Entity entity : entitylist) {
			if (!(entity instanceof EntityItemFrame))
				continue;
			EntityItemFrame frame = (EntityItemFrame) entity;
			if (!(frame.getDisplayedItem().getItem() instanceof ItemBlock))
				continue;

			BlockPos pos = entity.getPosition().offset(frame.getAdjustedHorizontalFacing(), -1).down();
			if (!(event.world.getTileEntity(pos) instanceof IInventory))
				continue;
			IInventory inventory = (IInventory) event.world.getTileEntity(pos);

			if (((ItemBlock) frame.getDisplayedItem().getItem()).getBlock() == Blocks.PISTON) {
				AutoCompression.autocompression(inventory);
			} else if (((ItemBlock) frame.getDisplayedItem().getItem()).getBlock() == Blocks.STICKY_PISTON) {
				List<ItemStack> remains = AutoCompression.fastautocompression(inventory);

				for (ItemStack itemStack : remains) {
					Block.spawnAsEntity(event.world, pos.add(0.5, 0.5, 0.5), itemStack);

				}
			}

		}
	}
}
