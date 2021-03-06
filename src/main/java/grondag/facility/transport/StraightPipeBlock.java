/*******************************************************************************
 * Copyright 2019, 2020 grondag
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package grondag.facility.transport;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;

import grondag.fermion.modkeys.api.ModKeys;
import grondag.xm.api.block.XmProperties;

public class StraightPipeBlock extends PipeBlock {
	public StraightPipeBlock(Block.Settings settings, Supplier<BlockEntity> beFactory) {
		super(settings, beFactory);
	}

	@Override
	protected void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(XmProperties.AXIS);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return super.getPlacementState(context).with(XmProperties.AXIS,
				ModKeys.isSecondaryPressed(context.getPlayer()) || context.getSide() == null ? context.getPlayerLookDirection().getAxis() : context.getSide().getAxis());
	}
}
