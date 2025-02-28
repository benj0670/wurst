/*
 * Copyright (c) 2014-2021 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.util;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.wurstclient.WurstClient;
import org.lwjgl.opengl.GL11;

public enum RenderUtils
{
	;
	
	private static final AxisAlignedBB DEFAULT_AABB = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
	
	public static void scissorBox(int startX, int startY, int endX, int endY)
	{
		int width = endX - startX;
		int height = endY - startY;
		int bottomY = WurstClient.MC.currentScreen.height - endY;
		double factor = WurstClient.MC.getMainWindow().getGuiScaleFactor();
		
		int scissorX = (int)(startX * factor);
		int scissorY = (int)(bottomY * factor);
		int scissorWidth = (int)(width * factor);
		int scissorHeight = (int)(height * factor);
		GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
	}
	
	public static void applyRenderOffset()
	{
		applyCameraRotationOnly();
		Vector3d camPos = getCameraPos();
		GL11.glTranslated(-camPos.x, -camPos.y, -camPos.z);
	}

	public static void applyRegionalRenderOffset()
	{
		applyCameraRotationOnly();

		Vector3d camPos = getCameraPos();
		Vector3d blockPos = getCameraBlockPos();

		int regionX = (int) blockPos.getX();
		regionX = (regionX>> 9) * 512;
		int regionZ = (int) blockPos.getZ();
		regionZ = (regionZ>> 9) * 512;

		GL11.glTranslated(regionX - camPos.x, -camPos.y, regionZ - camPos.z);
	}
	
	public static void applyCameraRotationOnly()
	{
		ActiveRenderInfo camera = TileEntityRendererDispatcher.instance.renderInfo;
		GL11.glRotated(MathHelper.wrapDegrees(camera.getPitch()), 1, 0, 0);
		GL11.glRotated(MathHelper.wrapDegrees(camera.getYaw() + 180.0), 0, 1,
			0);
	}
	
	public static Vector3d getCameraPos()
	{
		return TileEntityRendererDispatcher.instance.renderInfo.getProjectedView();
	}

	public static Vector3d getCameraBlockPos()
	{
		return WurstClient.MC.player.getLookVec();
	}
	
	public static void drawSolidBox(Vector3d targetBox)
	{
		drawSolidBox(DEFAULT_AABB);
	}
	
	public static void drawSolidBox(AxisAlignedBB bb)
	{
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glEnd();
	}
	
	public static void drawOutlinedBox(Vector3d bb)
	{
		drawOutlinedBox(DEFAULT_AABB);
	}
	
	public static void drawOutlinedBox(AxisAlignedBB bb)
	{
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glEnd();
	}
	
	public static void drawCrossBox()
	{
		drawCrossBox(DEFAULT_AABB);
	}
	
	public static void drawCrossBox(AxisAlignedBB bb)
	{
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
		
		GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
		GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
		GL11.glEnd();
	}
	
	public static void drawNode(AxisAlignedBB bb)
	{
		double midX = (bb.minX + bb.maxX) / 2;
		double midY = (bb.minY + bb.maxY) / 2;
		double midZ = (bb.minZ + bb.maxZ) / 2;
		
		GL11.glVertex3d(midX, midY, bb.maxZ);
		GL11.glVertex3d(bb.minX, midY, midZ);
		
		GL11.glVertex3d(bb.minX, midY, midZ);
		GL11.glVertex3d(midX, midY, bb.minZ);
		
		GL11.glVertex3d(midX, midY, bb.minZ);
		GL11.glVertex3d(bb.maxX, midY, midZ);
		
		GL11.glVertex3d(bb.maxX, midY, midZ);
		GL11.glVertex3d(midX, midY, bb.maxZ);
		
		GL11.glVertex3d(midX, bb.maxY, midZ);
		GL11.glVertex3d(bb.maxX, midY, midZ);
		
		GL11.glVertex3d(midX, bb.maxY, midZ);
		GL11.glVertex3d(bb.minX, midY, midZ);
		
		GL11.glVertex3d(midX, bb.maxY, midZ);
		GL11.glVertex3d(midX, midY, bb.minZ);
		
		GL11.glVertex3d(midX, bb.maxY, midZ);
		GL11.glVertex3d(midX, midY, bb.maxZ);
		
		GL11.glVertex3d(midX, bb.minY, midZ);
		GL11.glVertex3d(bb.maxX, midY, midZ);
		
		GL11.glVertex3d(midX, bb.minY, midZ);
		GL11.glVertex3d(bb.minX, midY, midZ);
		
		GL11.glVertex3d(midX, bb.minY, midZ);
		GL11.glVertex3d(midX, midY, bb.minZ);
		
		GL11.glVertex3d(midX, bb.minY, midZ);
		GL11.glVertex3d(midX, midY, bb.maxZ);
	}
	
	public static void drawArrow(Vector3d from, Vector3d to)
	{
		double startX = from.x;
		double startY = from.y;
		double startZ = from.z;
		
		double endX = to.x;
		double endY = to.y;
		double endZ = to.z;
		
		GL11.glPushMatrix();
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(startX, startY, startZ);
		GL11.glVertex3d(endX, endY, endZ);
		GL11.glEnd();
		
		GL11.glTranslated(endX, endY, endZ);
		GL11.glScaled(0.1, 0.1, 0.1);
		
		double angleX = Math.atan2(endY - startY, startZ - endZ);
		GL11.glRotated(Math.toDegrees(angleX) + 90, 1, 0, 0);
		
		double angleZ = Math.atan2(endX - startX,
			Math.sqrt(Math.pow(endY - startY, 2) + Math.pow(endZ - startZ, 2)));
		GL11.glRotated(Math.toDegrees(angleZ), 0, 0, 1);
		
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3d(0, 2, 1);
		GL11.glVertex3d(-1, 2, 0);
		
		GL11.glVertex3d(-1, 2, 0);
		GL11.glVertex3d(0, 2, -1);
		
		GL11.glVertex3d(0, 2, -1);
		GL11.glVertex3d(1, 2, 0);
		
		GL11.glVertex3d(1, 2, 0);
		GL11.glVertex3d(0, 2, 1);
		
		GL11.glVertex3d(1, 2, 0);
		GL11.glVertex3d(-1, 2, 0);
		
		GL11.glVertex3d(0, 2, 1);
		GL11.glVertex3d(0, 2, -1);
		
		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(1, 2, 0);
		
		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(-1, 2, 0);
		
		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(0, 2, -1);
		
		GL11.glVertex3d(0, 0, 0);
		GL11.glVertex3d(0, 2, 1);
		GL11.glEnd();
		
		GL11.glPopMatrix();
	}
}
