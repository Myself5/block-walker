package com.gooogle.code.blockWalker.AI;

import java.util.LinkedList;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;

import com.gooogle.code.blockWalker.Player;
import com.gooogle.code.blockWalker.Resources;

/**
 * @author brooks Sep 22, 2011
 */
public class DumbAI implements ITimerCallback {
	
	private static final int SIGHT = 300;
	private boolean running = true;
	private TimerHandler time;
	private int widthOffset;
	private static LinkedList<Rectangle> platforms = new LinkedList<Rectangle>();
	
	/**
	 * 
	 */
	public DumbAI() {
		
		widthOffset = (int) (Monster.MONSTER_SIZE / 2);
		Resources.getmScene().registerUpdateHandler(
				time = new TimerHandler(1, this));
	}
	
	/**
	 * @param mMonster
	 *            void
	 */
	public static void setPatrol(Monster mMonster) {
		float monsterX = Math.abs(mMonster.getX());
		float monsterY = Math.abs(mMonster.getY());
		float closeX = Float.MAX_VALUE;
		float closeY = Float.MAX_VALUE;
		Rectangle temp;
		float x, diffX, y, diffY;
		Rectangle closest = platforms.get(0);
		for (int i = 0; i < platforms.size(); i++) {
			temp = platforms.get(i);
			x = Math.abs(temp.getX());
			y = Math.abs(temp.getY());
			x += temp.getWidth() / 2;
			y += temp.getHeight() / 2;
			// Debug.d(x + " " + y + "\n");
			
			diffX = Math.abs(x - monsterX);
			diffY = Math.abs(y - monsterY);
			if (diffX < closeX && diffY < closeY) {
				closeX = diffX;
				closeY = diffY;
				closest = temp;
			}
		}
		// Debug.d("closest" + closest.getX() + "  " + closest.getY());
		mMonster.setLeft(closest.getX() - (closest.getWidth() / 4));
		mMonster.setRight(closest.getX() + closest.getWidth()
				- (closest.getWidth() / 4));
	}
	
	/**
	 * @param rect
	 */
	public static void addPlatform(Rectangle rect) {
		platforms.add(rect);
	}
	
	/**
	 */
	public static void clearPlatform() {
		platforms.clear();
	}
	
	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		Player player = Resources.getmPlayer();
		boolean lineOfSight = false;
		
		LinkedList<Attackable> monsters = Resources.getMonsters();
		Attackable temp;
		for (int index = 0; index < monsters.size(); index++) {
			temp = monsters.get(index);
			if (!temp.isBoss()) {
				Monster mMonster = (Monster) temp;
				if (Math.sqrt(Math.pow((player.getX() - mMonster.getX()), 2)
						+ (player.getY() - mMonster.getY())) < SIGHT) {
					Line sight = new Line(mMonster.getX() + widthOffset,
							mMonster.getY(), player.getX(), player.getY(), 8);
					lineOfSight = true;
					for (int i = 0; i < platforms.size() && lineOfSight; i++) {
						if (sight.collidesWith(platforms.get(i))) {
							// Debug.d(temp.collidesWith(platforms.get(i)) +
							// "");
							lineOfSight = false;
							break;
						}
					}
				}
				if (lineOfSight) {
					// Debug.d("Line of sight!!!!!!!!!!!!!!!           " +
					// (1*(mMonster.getX() - player.getX() )));
					float direction = (mMonster.getX() - player.getX());
					if (direction < 0) {
						mMonster.right();
					} else {
						mMonster.left();
					}
				} else {
					/*
					 * Debug.d("Random!!!!!!!!!!!!!!            " +
					 * (mMonster.getRight() - mMonster.getX() < mMonster.getX()
					 * - mMonster.getLeft())); Debug.d((mMonster.getRight() -
					 * mMonster.getX()) + " " + (mMonster.getX() -
					 * mMonster.getLeft()));
					 */
					if (mMonster.getRight() - mMonster.getX() < mMonster.getX()
							- mMonster.getLeft()) {
						mMonster.left();
					} else {
						mMonster.right();
					}
					
				}
			}
		}
		if (running) {
			pTimerHandler.reset();
		}
	}
	
	/**
	 * Stop this from running and dettach it.
	 */
	public void stop() {
		Resources.getmScene().unregisterUpdateHandler(time);
		running = false;
	}
}
